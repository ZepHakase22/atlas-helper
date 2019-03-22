package com.itattitude.atlas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.atlas.ApplicationProperties;
import org.apache.atlas.AtlasClientV2;
import org.apache.atlas.AtlasException;
import org.apache.atlas.AtlasServiceException;
import org.apache.atlas.model.typedef.AtlasTypesDef;
import org.apache.atlas.type.AtlasTypeUtil;
import org.apache.atlas.utils.AuthenticationUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.ArrayUtils;

import com.itattitude.atlas.ClassDef.BaseClassType;
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
	private List<EnumDef> _enumsDef;
	private List<StructDef> _structsDef;
	private List<ClassificationDef> _classificationsDef;
	private List<ClassDef> _classesDef;
	
    DataCatalogue() {
    	_enumsDef = Collections.<EnumDef>emptyList();
    	_structsDef = Collections.<StructDef>emptyList();
    	_classificationsDef = Collections.<ClassificationDef>emptyList();
    	_classesDef = Collections.<ClassDef>emptyList();
    }

	public DataCatalogue(String[] args, String[] credentials) throws DataCatalogueException {
		this();
		
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
	
    public void createEnums( EnumDef ... args) {

    	for(EnumDef a: args) {
    		_enumsDef.add(a);
    	}
    }
    
    public void createClasses( ClassDef ... args) {
    	for(ClassDef a: args) {
    		_classesDef.add(a);
    	}
    }
	
	public EnumDef createEnum(String name, String description, String typeVersion, String serviceType, List<EnumElementDef> enumElementDef) {
    	return new EnumDef(name,description,typeVersion,serviceType, enumElementDef);
    }

	public String[] getServerUrls(String[] args) throws DataCatalogueException {
		
		Configuration configuration;
		
		if(args.length>0)
			return args[0].split(",");
		
		try {
			configuration = ApplicationProperties.get();
		} catch (AtlasException e) {
			throw new DataCatalogueException(e);
		}
		String[] urls = configuration.getStringArray(ATLAS_REST_ADDRESS);
		if(ArrayUtils.isEmpty(urls)) 
			throw new DataCatalogueException("Missing Atlas Rest address.", ErrorCodeEnum.MISSING_ATLAS_REST_ADDRESS);
		return urls;	
	}
    public void saveTypesDef() throws DataCatalogueException  {
    	AtlasTypesDef atlasTypesDef = AtlasTypeUtil.getTypesDef(EnumDef.asAtlasEnumDef(_enumsDef), 
    															StructDef.asAtlasStructDef(_structsDef),
    															ClassificationDef.asAtlasClassificationDef(_classificationsDef), 
    															ClassDef.asAtlasEntityDef(_classesDef));
    	try {
			_atlasClientV2.createAtlasTypeDefs(atlasTypesDef);
		} catch (AtlasServiceException e) {
			throw manageAtlasServiceException(e);
		}
    }

	public List<String> getAllTypeDefs(SearchFilter searchFilter) throws DataCatalogueException {
		List<String> names = new ArrayList<>();
        try {
			AtlasTypesDef searchDefs = _atlasClientV2.getAllTypeDefs(searchFilter);
			searchDefs.getEnumDefs().forEach(s-> names.add(s.getName()));
			searchDefs.getStructDefs().forEach(s-> names.add(s.getName()));
			searchDefs.getClassificationDefs().forEach(s-> names.add(s.getName()));
			searchDefs.getEntityDefs().forEach(s-> names.add(s.getName()));
			searchDefs.getRelationshipDefs().forEach(s-> names.add(s.getName()));
			
		} catch (AtlasServiceException e) {
			throw manageAtlasServiceException(e);
		}

		return names;
	}
	
	private DataCatalogueException manageAtlasServiceException(AtlasServiceException e) {
		
		if(e.getStatus()!=null)
			return new DataCatalogueException(e.getMessage(),e.getStatus(),e.getCause());
		else
			return new DataCatalogueException(e.getMessage(),e.getCause());
	}

	public ClassDef createClass(String name, String description, String typeVersion, 
			BaseClassType baseProcess, String serviceType, List<AttributeDef> attributesDef) {
		return createClass(name, description, typeVersion, baseProcess.toString(),serviceType, attributesDef);
	}

	public ClassDef createClass(String name, String description, String typeVersion, 
			BaseClassType baseProcess, String serviceType, List<AttributeDef> attributesDef,
			HashMap<String,String> options) {
		return createClass(name, description, typeVersion, baseProcess.toString(),serviceType, attributesDef, 
				null);
	}

	public ClassDef createClass(String name, String description, String typeVersion, 
			String baseType, String serviceType,List<AttributeDef> attributesDef) {
		return createClass(name, description, typeVersion, baseType,serviceType, attributesDef, null);
	}
	
	public ClassDef createClass(String name, String description, String typeVersion, 
			String baseType, String serviceType, List<AttributeDef> attributesDef,
			HashMap<String,String> options) {
		
		return new ClassDef(name, description, typeVersion, baseType, serviceType, attributesDef,options);
	}

}
