package com.itattitude.atlas;

import org.apache.atlas.ApplicationProperties;
import org.apache.atlas.AtlasClientV2;
import org.apache.atlas.AtlasException;
import org.apache.atlas.utils.AuthenticationUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.ArrayUtils;
import com.itattitude.atlas.DataCatalogueException;
import com.itattitude.atlas.DataCatalogueException.ErrorCodeEnum;


/**
 * DataCatalogue Helper
 *
 */
public class DataCatalogue 
{
    static final String ATLAS_REST_ADDRESS          = "atlas.rest.address";

	private AtlasClientV2 _atlasClientV2;
	private String[] _urls;
	
	public DataCatalogue(String[] args, String[] credentials) throws DataCatalogueException {

		if(_urls.length==0)
			_urls = this.getServerUrls(args);
		
		if(credentials==null) {
			if(!AuthenticationUtil.isKerberosAuthenticationEnabled()) {
				throw new DataCatalogueException("Need user name and password",ErrorCodeEnum.MISSING_USERNAME_AND_PASSWORD);
			} else {
				try {
					_atlasClientV2 = new AtlasClientV2(_urls);
				} catch (AtlasException e) {
					throw new DataCatalogueException(e);
				}
			}
		} else {
			_atlasClientV2 = new AtlasClientV2(_urls,credentials);
		}
	}
	
	
	public DataCatalogue(String[] args) throws DataCatalogueException {

		this(args, null);
	}
	public String[] getServerUrls(String[] args) throws DataCatalogueException {
		
		Configuration configuration;
		
		if(args.length>0)
			return args[0].split(",");
		
		try {
			configuration = ApplicationProperties.get();
		} catch (AtlasException e) {
			// TODO Auto-generated catch block
			throw new DataCatalogueException(e);
		}
		String[] urls = configuration.getStringArray(ATLAS_REST_ADDRESS);
		if(ArrayUtils.isEmpty(urls)) 
			throw new DataCatalogueException("Missing Atlas Rest address.", ErrorCodeEnum.MISSING_ATLAS_REST_ADDRESS);
		return urls;	
	}
}
