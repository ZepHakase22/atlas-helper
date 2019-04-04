package com.itattitude.atlas;

import static org.apache.atlas.security.SecurityProperties.TLS_ENABLED;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.atlas.ApplicationProperties;
import org.apache.atlas.AtlasException;
import org.apache.atlas.AtlasServerEnsemble;
import org.apache.atlas.AtlasServiceException;
import org.apache.atlas.security.SecureClientUtils;
import org.apache.atlas.utils.AuthenticationUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.google.common.annotations.VisibleForTesting;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import com.sun.jersey.multipart.impl.MultiPartWriter;

public abstract class DataCatalogueBaseClient extends DataCatalogueAPI{
	private static final Logger LOG = LoggerFactory.getLogger(DataCatalogueBaseClient.class);
    static final String ATLAS_CLIENT_HA_RETRIES_KEY           = "atlas.client.ha.retries";
    // Setting the default value based on testing failovers while client code like quickstart is running.
    static final        int    	DEFAULT_NUM_RETRIES                   = 4;
    static final        String 	UNKNOWN_STATUS                        = "Unknown status";
    public static final String 	BASE_URI = "api/atlas/";
    public static final String 	ADMIN_STATUS = "admin/status";
    public static final API 	API_STATUS  = new API(BASE_URI + ADMIN_STATUS, HttpMethod.GET, Response.Status.OK);
    public static final String 	STATUS = "Status";
    static final        String 	ATLAS_CLIENT_HA_SLEEP_INTERVAL_MS_KEY = "atlas.client.ha.sleep.interval.ms";
    // Setting the default value based on testing failovers while client code like quickstart is running.
    // With number of retries, this gives a total time of about 20s for the server to start.
    static final int DEFAULT_SLEEP_BETWEEN_RETRIES_MS = 5000;


	private static Configuration configuration;
	private DataCatalogueClientContext dataCatalogueClientContext = null;

	
	//touch
    protected DataCatalogueBaseClient(String ... baseUrls) throws DataCatalogueException {
        this(getCurrentUGI(), baseUrls);
    }
    
    protected DataCatalogueBaseClient(UserGroupInformation ugi, String[] baseUrls) throws DataCatalogueException {
        this(ugi, ugi.getShortUserName(), baseUrls);
    }
    
    protected DataCatalogueBaseClient(UserGroupInformation ugi, String doAsUser, String[] baseUrls) 
    		throws DataCatalogueException {
        this(getClientProperties(), ugi, doAsUser, baseUrls, null, null);
    }
    
    protected DataCatalogueBaseClient(String[] baseUrls, String[] basicAuthUserNamePassword) 
    		throws DataCatalogueException {
    	this(getClientProperties(), baseUrls, basicAuthUserNamePassword);
    }
    
    @VisibleForTesting
    protected DataCatalogueBaseClient(Configuration configuration, String[] baseUrls, String[] basicAuthUserNamePassword) 
    		throws DataCatalogueException {
    	this(configuration, null, null, baseUrls, basicAuthUserNamePassword, null);
    }
    
    protected DataCatalogueBaseClient(String[] baseUrls, Cookie cookie) throws DataCatalogueException {
    	this(getClientProperties(), null, null, baseUrls, null, cookie);
    }

    /* Da capire dopo i test
    @VisibleForTesting
    protected DataCatalogueBaseClient(WebResource service, Configuration configuration) {
        this.service = service;
        this.configuration = configuration;
    }
*/

    
    // touched
    @VisibleForTesting
    protected DataCatalogueBaseClient(Configuration configuration, UserGroupInformation ugi, String doAsUser, 
    		String[] baseUrls, String[] basicAuthUserNamePassword, Cookie cookie) throws DataCatalogueException {
 
    	if( cookie != null) {
    		super.setCookie(cookie);
    	}
    	if(configuration == null)
    		DataCatalogueBaseClient.configuration = configuration;
    	Client client = getClient(configuration, ugi, doAsUser);
    	
    	if((!AuthenticationUtil.isKerberosAuthenticationEnabled())) {
    		final HTTPBasicAuthFilter authFilter;
    		if(basicAuthUserNamePassword!=null) 
    			if(basicAuthUserNamePassword.length == 2) {
	            authFilter = new HTTPBasicAuthFilter(basicAuthUserNamePassword[0],basicAuthUserNamePassword[1]);
	            	client.addFilter(authFilter);
    		}
    	}
        dataCatalogueClientContext = new DataCatalogueClientContext(baseUrls, client, ugi, doAsUser);
        dataCatalogueClientContext.setActiveServerUrl(determineActiveServiceURL(baseUrls));
    }
    
    protected static Configuration getClientProperties() throws DataCatalogueException {
        try {
            if (configuration == null) {
                configuration = ApplicationProperties.get();
            }
        } catch (AtlasException e) {
            throw new DataCatalogueException(e);
        }
        return configuration;
    }
    
    protected static UserGroupInformation getCurrentUGI() throws DataCatalogueException {
        try {
            return UserGroupInformation.getCurrentUser();
        } catch (IOException e) {
            throw new DataCatalogueException(e);
        }
    }
    
    @Override
    protected final int getNumberOfRetries() {
        return configuration.getInt(DataCatalogueBaseClient.ATLAS_CLIENT_HA_RETRIES_KEY, 
        		DataCatalogueBaseClient.DEFAULT_NUM_RETRIES);
    }
    
    private WebResource appendPathParams(WebResource resource, String[] pathParams) {
        if (pathParams != null) {
            for (String pathParam : pathParams) {
                resource = resource.path(pathParam);
            }
        }
        return resource;
    }
    
    private WebResource appendQueryParams(MultivaluedMap<String, String> queryParams, WebResource resource) {
        if (null != queryParams && !queryParams.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                for (String value : entry.getValue()) {
                    if (StringUtils.isNotBlank(value)) {
                        resource = resource.queryParam(entry.getKey(), value);
                    }
                }
            }
        }
        return resource;
    }
    
    @Override
    protected WebResource getResource(API api, String... pathParams) {
        return getResource(api.getNormalizedPath(), pathParams);
    }
    
    private WebResource getResource(String path, String... pathParams) {
    	WebResource service = dataCatalogueClientContext.getClient().
    			resource(UriBuilder.fromUri(dataCatalogueClientContext.getActiveServerUrl()).build());
        return getResource(service, path, pathParams);
    }
    
    private WebResource getResource(WebResource service, String path, String... pathParams) {
        WebResource resource = service.path(path);
        resource = appendPathParams(resource, pathParams);
        return resource;
    }
    
    @Override
    protected WebResource getResource(API api, MultivaluedMap<String, String> queryParams) {
    	WebResource service = dataCatalogueClientContext.getClient().
    			resource(UriBuilder.fromUri(dataCatalogueClientContext.getActiveServerUrl()).build());
        return getResource(service, api, queryParams);
    }

    // Modify URL to include the query params
    private WebResource getResource(WebResource service, API api, MultivaluedMap<String, String> queryParams) {
        WebResource resource = service.path(api.getNormalizedPath());
        resource = appendQueryParams(queryParams, resource);
        return resource;
    }

    // Public Area
    
    public String getAdminStatus() throws DataCatalogueException {
        String      result    = DataCatalogueBaseClient.UNKNOWN_STATUS;
        		
    	if(dataCatalogueClientContext!=null) {
    		String activeServerAddress = dataCatalogueClientContext.getActiveServerUrl();
    		result = getAdminStatus(activeServerAddress);
    	}
        return result;
    }
    
    public String getActiveServerAddress() {
    	if(dataCatalogueClientContext != null) {
    		return dataCatalogueClientContext.getActiveServerUrl();
    	}
    	return null;
    }
    
    /**
     * Return status of the service instance the client is pointing to.
     *
     * @return One of the values in ServiceState.ServiceStateValue or {@link #UNKNOWN_STATUS} if
     * there is a JSON parse exception
     * @throws AtlasServiceException if there is a HTTP error.
     */
    private String getAdminStatus(String activeServerAddress) throws DataCatalogueException {
        String      result    = DataCatalogueBaseClient.UNKNOWN_STATUS;
        if(dataCatalogueClientContext!=null) {
	        Client client = dataCatalogueClientContext.getClient();
	        if(client!=null) {
	        	try {
			        WebResource service = client.resource(UriBuilder.fromUri(activeServerAddress).build());
			        WebResource resource  = getResource(service, API_STATUS.getNormalizedPath());
			        ObjectNode  response  = callAPIWithResource(API_STATUS, resource, null, ObjectNode.class);
			        if (response.has(STATUS)) {
			            result = response.get(STATUS).asText();
			        }
	        	} catch (DataCatalogueException | IllegalArgumentException e) {
	        		throw new DataCatalogueException(e);
	        	}
	        }
        }
        return result;
    }
    
    private int getSleepBetweenRetriesMs() {
        return configuration.getInt(DataCatalogueBaseClient.ATLAS_CLIENT_HA_SLEEP_INTERVAL_MS_KEY, 
        		DataCatalogueBaseClient.DEFAULT_SLEEP_BETWEEN_RETRIES_MS);
    }

    @Override
    void sleepBetweenRetries() {
        try {
            Thread.sleep(getSleepBetweenRetriesMs());
        } catch (InterruptedException e) {
            LOG.error("Interrupted from sleeping between retries.", e);
        }
    }
    
    private String getAddressIfActive(String serverInstance) {
        String activeServerAddress = null;
        for (int i = 0; i < getNumberOfRetries(); i++) {
            try {
                String adminStatus = getAdminStatus(serverInstance);
                if (StringUtils.equals(adminStatus, "ACTIVE")) {
                    activeServerAddress = serverInstance;
                    break;
                } else {
                    LOG.info("attempt #{}: Service {} - is not active. status={}", (i + 1), serverInstance, adminStatus);
                }
            } catch (Exception e) {
                LOG.error("attempt #{}: Service {} - could not get status", (i + 1), serverInstance);
            }
            sleepBetweenRetries();
        }
        return activeServerAddress;
    }
    
    private String selectActiveServerAddress( AtlasServerEnsemble serverEnsemble)
            throws DataCatalogueException {
        List<String> serverInstances = serverEnsemble.getMembers();
        String activeServerAddress = null;
        for (String serverInstance : serverInstances) {
            LOG.info("Trying with address {}", serverInstance);
            activeServerAddress = getAddressIfActive(serverInstance);
            if (activeServerAddress != null) {
                LOG.info("Found service {} as active service.", serverInstance);
                break;
            }
        }
        if (activeServerAddress != null)
            return activeServerAddress;
        else
            throw new DataCatalogueException(API_STATUS, new RuntimeException("Could not find any active instance"));
    }
    
    @VisibleForTesting
    protected String determineActiveServiceURL(String[] baseUrls) throws DataCatalogueException {
        if (baseUrls.length == 0) {
            throw new DataCatalogueException("Base URLs cannot be null or empty", 
            		DataCatalogueException.ErrorCodeEnum.MISSING_ATLAS_REST_ADDRESS);
        }
        final String baseUrl;
        AtlasServerEnsemble atlasServerEnsemble = new AtlasServerEnsemble(baseUrls);

        try {
            baseUrl = selectActiveServerAddress(atlasServerEnsemble);
        } catch (DataCatalogueException e) {
            LOG.error("None of the passed URLs are active: {}", atlasServerEnsemble, e);
            throw new DataCatalogueException("None of the passed URLs are active " + atlasServerEnsemble, 
            		DataCatalogueException.ErrorCodeEnum.INVALID_OR_NOT_ACTIVE_SERVER_ADDRESS, e);
        }
        return baseUrl;
    }
    @VisibleForTesting
    protected Client getClient(Configuration configuration, UserGroupInformation ugi, String doAsUser) {
        DefaultClientConfig config = new DefaultClientConfig();
        // Enable POJO mapping feature
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        config.getClasses().add(JacksonJaxbJsonProvider.class);
        config.getClasses().add(MultiPartWriter.class);

        int readTimeout = configuration.getInt("atlas.client.readTimeoutMSecs", 60000);
        int connectTimeout = configuration.getInt("atlas.client.connectTimeoutMSecs", 60000);
        if (configuration.getBoolean(TLS_ENABLED, false)) {
            // create an SSL properties configuration if one doesn't exist.  SSLFactory expects a file, so forced
            // to create a
            // configuration object, persist it, then subsequently pass in an empty configuration to SSLFactory
            try {
                SecureClientUtils.persistSSLClientConfiguration(configuration, System.getProperty("atlas.conf") );
            } catch (Exception e) {
                LOG.info("Error processing client configuration.", e);
            }
        }

        final URLConnectionClientHandler handler;

        boolean isKerberosEnabled = AuthenticationUtil.isKerberosAuthenticationEnabled(ugi);

        if (isKerberosEnabled) {
            handler = SecureClientUtils.getClientConnectionHandler(config, configuration, doAsUser, ugi);
        } else {
            if (configuration.getBoolean(TLS_ENABLED, false)) {
                handler = SecureClientUtils.getUrlConnectionClientHandler();
            } else {
                handler = new URLConnectionClientHandler();
            }
        }
        Client client = new Client(handler, config);
        client.setReadTimeout(readTimeout);
        client.setConnectTimeout(connectTimeout);
        return client;
    }
    
    /**
     * A class to capture input state while creating the client.
     *
     * The information here will be reused when the client is re-initialized on switch-over
     * in case of High Availability.
     */
    private class DataCatalogueClientContext {
        private String[] baseUrls;
        private Client client;
        private String doAsUser;
        private UserGroupInformation ugi;
        private String baseUrl;

        public DataCatalogueClientContext(String[] baseUrls, Client client, UserGroupInformation ugi, 
        		String doAsUser) {
            this.baseUrls = baseUrls;
            this.client = client;
            this.ugi = ugi;
            this.doAsUser = doAsUser;
            this.baseUrl = null;
        }

        public Client getClient() {
            return client;
        }

        public String[] getBaseUrls() {
            return baseUrls;
        }

        public String getDoAsUser() {
            return doAsUser;
        }

        public UserGroupInformation getUgi() {
            return ugi;
        }
        public String getActiveServerUrl() {
        	return baseUrl;
        }
        void setActiveServerUrl(String serverUrl) {
        	baseUrl = serverUrl;
        }
    }
}
