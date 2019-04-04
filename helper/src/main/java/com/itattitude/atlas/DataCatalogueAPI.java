package com.itattitude.atlas;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.atlas.utils.AtlasJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

public abstract class DataCatalogueAPI  {
	private static final Logger LOG = LoggerFactory.getLogger(DataCatalogueAPI.class);
    static final String JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON + "; charset=UTF-8";

	private Cookie cookie;
	
	abstract void sleepBetweenRetries();
	protected abstract int getNumberOfRetries();
    protected abstract WebResource getResource(API api, String... pathParams) ;
    protected abstract WebResource getResource(API api, MultivaluedMap<String, String> queryParams);
	
	
	public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }
	
    public <T> T callAPI(API api, Class<T> responseType, MultivaluedMap<String, String> queryParams)
            throws DataCatalogueException {
        return callAPIWithResource(api, getResource(api, queryParams), null, responseType);
    }
	
    public <T> T callAPI(API api, Class<T> responseType, Object requestObject, String... params)
            throws DataCatalogueException {
        return callAPIWithResource(api, getResource(api, params), requestObject, responseType);
    }
    
    protected <T> T callAPIWithResource(API api, WebResource resource, Object requestObject, 
    		Class<T> responseType) throws DataCatalogueException {

    	GenericType<T> genericType = null;
        if (responseType != null) {
            genericType = new GenericType<>(responseType);
        }
        return callAPIWithResource(api, resource, requestObject, genericType);
    }
    
    @SuppressWarnings("unchecked")
	protected <T> T callAPIWithResource(API api, WebResource resource, Object requestObject, 
    		GenericType<T> responseType) throws DataCatalogueException {

    	ClientResponse clientResponse = null;
        int i = 0;
        do {
            if (LOG.isDebugEnabled()) {
                LOG.debug("------------------------------------------------------");
                LOG.debug("Call         : {} {}", api.getMethod(), api.getNormalizedPath());
                LOG.debug("Content-type : {} ", api.getConsumes());
                LOG.debug("Accept       : {} ", api.getProduces());
                if (requestObject != null) {
                    LOG.debug("Request      : {}", requestObject);
                }
            }

            WebResource.Builder requestBuilder = resource.getRequestBuilder();

            // Set content headers
            requestBuilder
                    .accept(api.getProduces())
                    .type(api.getConsumes());

            // Set cookie if present
            if (cookie != null) {
                requestBuilder.cookie(cookie);
            }

            try {
            	clientResponse = requestBuilder.method(api.getMethod(), ClientResponse.class, requestObject);
            } catch (ClientHandlerException e) {
            	LOG.debug("HTTP Status  : 503 Server Unaivable");
            } catch(Exception e) {
            	throw new DataCatalogueException(e);
            }
            LOG.debug("HTTP Status  : {}", clientResponse.getStatus());

            if (!LOG.isDebugEnabled()) {
                LOG.info("method={} path={} contentType={} accept={} status={}", api.getMethod(),
                    api.getNormalizedPath(), api.getConsumes(), api.getProduces(), clientResponse.getStatus());
            }

            if (clientResponse.getStatus() == api.getExpectedStatus().getStatusCode()) {
                if (responseType == null) {
                    return null;
                }
                try {
                    if(api.getProduces().equals(MediaType.APPLICATION_OCTET_STREAM)) {
                        return (T) clientResponse.getEntityInputStream();
                    } else if (responseType.getRawClass().equals(ObjectNode.class)) {
                        String stringEntity = clientResponse.getEntity(String.class);
                        try {
                            JsonNode jsonObject = AtlasJson.parseToV1JsonNode(stringEntity);
                            LOG.debug("Response     : {}", jsonObject);
                            LOG.debug("------------------------------------------------------");
                            return (T) jsonObject;
                        } catch (IOException e) {
                            throw new DataCatalogueException(api, e);
                        }
                    } else {
                        T entity = clientResponse.getEntity(responseType);
                        LOG.debug("Response     : {}", entity);
                        LOG.debug("------------------------------------------------------");
                        return entity;
                    }
                } catch (ClientHandlerException e) {
                    throw new DataCatalogueException(api, e);
                }
            } else if (clientResponse.getStatus() != ClientResponse.Status.SERVICE_UNAVAILABLE.getStatusCode()) {
                break;
            } else {
                LOG.error("Got a service unavailable when calling: {}, will retry..", resource);
                sleepBetweenRetries();
            }

            i++;
        } while (i < getNumberOfRetries());

        throw new DataCatalogueException(api, clientResponse);
    }
    public static class API {
        private final String method;
        private final String path;
        private final String consumes;
        private final String produces;
        private final Response.Status status;

        private static final Logger LOG = LoggerFactory.getLogger(API.class);

        public API(String path, String method, Response.Status status) {
            this(path, method, status, JSON_MEDIA_TYPE, MediaType.APPLICATION_JSON);
        }

        public API(String path, String method, Response.Status status, String consumes, String produces) {
            this.path = path;
            this.method = method;
            this.status = status;
            this.consumes = consumes;
            this.produces = produces;
        }

        public String getMethod() {
            return method;
        }

        public String getPath() {
            return path;
        }

        public String getNormalizedPath() {
            // This method used to return Paths.get(path).normalize().toString(), but
            // the use of Paths.get(path) on Windows produces a path with Windows
            // path separators (i.e. back-slashes) which is not valid for a URI
            // and will result in an HTTP 404 status code.
            URI uri = null;
            String resultUri = null;

            try {
                uri = new URI(path);
                if (uri != null) {
                    URI normalizedUri = uri.normalize();
                    resultUri = normalizedUri.toString();
                }
            } catch (Exception e) {
                LOG.error("getNormalizedPath() caught exception for path={}", path, e);
                resultUri = null;
            }

            return resultUri;
        }

        public Response.Status getExpectedStatus() {
            return status;
        }

        public String getConsumes() {
            return consumes;
        }

        public String getProduces() {
            return produces;
        }
    }

}
