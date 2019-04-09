package com.itattitude.atlas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.atlas.ApplicationProperties;
import org.apache.atlas.AtlasException;
import org.apache.atlas.model.typedef.AtlasTypesDef;
import org.apache.atlas.type.AtlasTypeUtil;
import org.apache.atlas.utils.AuthenticationUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.ArrayUtils;
import org.apache.hadoop.security.UserGroupInformation;

import com.itattitude.atlas.ClassDef.BaseClassType;
import com.itattitude.atlas.DataCatalogueException.ErrorCodeEnum;
import com.itattitude.atlas.RelationshipDef.PropagationType;
import com.itattitude.atlas.RelationshipDef.UMLCategory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DataCatalogue Helper
 *
 */
public class DataCatalogue 
{
    static final String ATLAS_REST_ADDRESS          = "atlas.rest.address";
	public static final boolean isDebug() {
		return java.lang.management.ManagementFactory.getRuntimeMXBean().
		    getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}
	private static final Logger LOG = LoggerFactory.getLogger(DataCatalogue.class);

	private DataCatalogueClientV2 _atlasClientV2;
	private String[] _urls;
	private List<EnumDef> _enumsDef;
	private List<StructDef> _structsDef;
	private List<ClassificationDef> _classificationsDef;
	private List<ClassDef> _classesDef;
	private List<RelationshipDef> _relationshipsDef;
	private String _userName;
	
    DataCatalogue() {
    	_enumsDef = new ArrayList<EnumDef>();
    	_structsDef = new ArrayList<StructDef>();
    	_classificationsDef = new ArrayList<ClassificationDef>();
    	_classesDef = new ArrayList<ClassDef>();;
    	_relationshipsDef = new ArrayList<RelationshipDef>();;
    }

	public DataCatalogue(String[] args, String[] credentials) throws DataCatalogueException {
		this();
		
		if(_urls == null)
			_urls = this.getServerUrls(args);
		else if(_urls.length==0)
			_urls = this.getServerUrls(args);
		
		if(credentials==null) {
			if(!AuthenticationUtil.isKerberosAuthenticationEnabled()) {
				throw new DataCatalogueException("Need user name and password",
						ErrorCodeEnum.MISSING_USERNAME_AND_PASSWORD);
			} else {
				try {
					_atlasClientV2 = new DataCatalogueClientV2(_urls);
					UserGroupInformation.getCurrentUser().getUserName();
				} catch (DataCatalogueException | IOException e) {
					throw new DataCatalogueException(e);
				}
			}
		} else {
			_atlasClientV2 = new DataCatalogueClientV2(_urls, credentials);
			_userName = credentials[0];
		}
		LOG.info("Connected Atlas Server {} with username: {}",_atlasClientV2.getActiveServerAddress(), 
				_userName);
	}
	public void setCredentials(String[] credentials) throws DataCatalogueException {
		_atlasClientV2 = new DataCatalogueClientV2(_urls,credentials);
		_userName = credentials[0];
		LOG.info("Connected Atlas Server {} with username: {}",_userName);
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
    
    public void createRelationships( RelationshipDef ... args) {
    	for(RelationshipDef a: args) {
    		_relationshipsDef.add(a);
    	}
    }
	
	public EnumDef createEnum(String name, String description, String typeVersion, String serviceType, List<EnumElementDef> enumElementDef) {
    	return new EnumDef(name,description,typeVersion,serviceType, enumElementDef);
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
	
	public RelationshipDef createRelationship(String name, String description, String typeVersion, 
			String serviceType, UMLCategory category, PropagationType propagationType, EndDef def1, 
			EndDef def2 ) {
		return new RelationshipDef(name, description, typeVersion, serviceType, category, propagationType, def1, def2);
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
    															ClassDef.asAtlasEntityDef(_classesDef),
    															RelationshipDef.asAtlasRelationshipDef(_relationshipsDef));
    	try {
			_atlasClientV2.createAtlasTypeDefs(atlasTypesDef);
		} catch (DataCatalogueException e) {
			throw manageAtlasServiceException(e);
		}
    }

    public void getAllTypeDefs(SearchFilter searchFilter) throws DataCatalogueException {
        AtlasTypesDef searchDefs   = _atlasClientV2.getAllTypeDefs(searchFilter);
        if(searchDefs.isEmpty()) {
        	throw new DataCatalogueException("Error creating type",ErrorCodeEnum.TYPE_NOT_CREATED);
        }
    }
	private DataCatalogueException manageAtlasServiceException(DataCatalogueException e) {
		
		if(e.getStatus()!=null)
			return new DataCatalogueException(e.getMessage(),e.getStatus(),e.getCause());
		else
			return new DataCatalogueException(e.getMessage(),e.getCause());
	}


}
