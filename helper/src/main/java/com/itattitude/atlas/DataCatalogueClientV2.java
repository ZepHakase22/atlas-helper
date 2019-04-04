package com.itattitude.atlas;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;

import org.apache.atlas.model.SearchFilter;
import org.apache.atlas.model.typedef.AtlasTypesDef;
import org.apache.atlas.type.AtlasType;

final class DataCatalogueClientV2 extends DataCatalogueBaseClient {
    public static final  String TYPES_API            = BASE_URI + "v2/types/";
    private static final String TYPEDEF_BY_NAME      = TYPES_API + "typedef/name/";
    private static final String TYPEDEF_BY_GUID      = TYPES_API + "typedef/guid/";
    private static final String TYPEDEFS_API         = TYPES_API + "typedefs/";
    public static final  String ENTITY_API           = BASE_URI + "v2/entity/";
    private static final String ENTITY_BULK_API      = ENTITY_API + "bulk/";
    private static final String LINEAGE_URI  = BASE_URI + "v2/lineage/";
    private static final String DISCOVERY_URI      = BASE_URI + "v2/search";
    private static final String DSL_URI            = DISCOVERY_URI + "/dsl";
    private static final String FULL_TEXT_URI      = DISCOVERY_URI + "/fulltext";
    private static final String BASIC_SEARCH_URI   = DISCOVERY_URI + "/basic";
    private static final String FACETED_SEARCH_URI = BASIC_SEARCH_URI;
    private static final String RELATIONSHIPS_URI  = BASE_URI + "v2/relationship/";
    private static final String BULK_HEADERS = "bulk/headers";
    private static final String BULK_SET_CLASSIFICATIONS = "bulk/setClassifications";
	
    public static class API_V2 extends API {
        public static final API_V2 GET_TYPEDEF_BY_NAME         = new API_V2(TYPEDEF_BY_NAME, HttpMethod.GET, Response.Status.OK);
        public static final API_V2 GET_TYPEDEF_BY_GUID         = new API_V2(TYPEDEF_BY_GUID, HttpMethod.GET, Response.Status.OK);
        public static final API_V2 GET_ALL_TYPE_DEFS           = new API_V2(TYPEDEFS_API, HttpMethod.GET, Response.Status.OK);
        public static final API_V2 CREATE_ALL_TYPE_DEFS        = new API_V2(TYPEDEFS_API, HttpMethod.POST, Response.Status.OK);
        public static final API_V2 UPDATE_ALL_TYPE_DEFS        = new API_V2(TYPEDEFS_API, HttpMethod.PUT, Response.Status.OK);
        public static final API_V2 DELETE_ALL_TYPE_DEFS        = new API_V2(TYPEDEFS_API, HttpMethod.DELETE, Response.Status.NO_CONTENT);
        public static final API_V2 GET_ENTITY_BY_GUID          = new API_V2(ENTITY_API + "guid/", HttpMethod.GET, Response.Status.OK);
        public static final API_V2 GET_ENTITY_BY_ATTRIBUTE     = new API_V2(ENTITY_API + "uniqueAttribute/type/", HttpMethod.GET, Response.Status.OK);
        public static final API_V2 CREATE_ENTITY               = new API_V2(ENTITY_API, HttpMethod.POST, Response.Status.OK);
        public static final API_V2 UPDATE_ENTITY               = new API_V2(ENTITY_API, HttpMethod.POST, Response.Status.OK);
        public static final API_V2 UPDATE_ENTITY_BY_ATTRIBUTE  = new API_V2(ENTITY_API + "uniqueAttribute/type/", HttpMethod.PUT, Response.Status.OK);
        public static final API_V2 DELETE_ENTITY_BY_GUID       = new API_V2(ENTITY_API + "guid/", HttpMethod.DELETE, Response.Status.OK);
        public static final API_V2 DELETE_ENTITY_BY_ATTRIBUTE  = new API_V2(ENTITY_API + "uniqueAttribute/type/", HttpMethod.DELETE, Response.Status.OK);
        public static final API_V2 GET_ENTITIES_BY_GUIDS       = new API_V2(ENTITY_BULK_API, HttpMethod.GET, Response.Status.OK);
        public static final API_V2 CREATE_ENTITIES             = new API_V2(ENTITY_BULK_API, HttpMethod.POST, Response.Status.OK);
        public static final API_V2 UPDATE_ENTITIES             = new API_V2(ENTITY_BULK_API, HttpMethod.POST, Response.Status.OK);
        public static final API_V2 DELETE_ENTITIES_BY_GUIDS    = new API_V2(ENTITY_BULK_API, HttpMethod.DELETE, Response.Status.OK);
        public static final API_V2 GET_CLASSIFICATIONS         = new API_V2(ENTITY_API + "guid/%s/classifications", HttpMethod.GET, Response.Status.OK);
        public static final API_V2 ADD_CLASSIFICATIONS         = new API_V2(ENTITY_API + "guid/%s/classifications", HttpMethod.POST, Response.Status.NO_CONTENT);
        public static final API_V2 UPDATE_CLASSIFICATIONS      = new API_V2(ENTITY_API + "guid/%s/classifications", HttpMethod.PUT, Response.Status.NO_CONTENT);
        public static final API_V2 DELETE_CLASSIFICATION       = new API_V2(ENTITY_API + "guid/%s/classification/%s", HttpMethod.DELETE, Response.Status.NO_CONTENT);
        public static final API_V2 LINEAGE_INFO                = new API_V2(LINEAGE_URI, HttpMethod.GET, Response.Status.OK);
        public static final API_V2 DSL_SEARCH                  = new API_V2(DSL_URI, HttpMethod.GET, Response.Status.OK);
        public static final API_V2 FULL_TEXT_SEARCH            = new API_V2(FULL_TEXT_URI, HttpMethod.GET, Response.Status.OK);
        public static final API_V2 BASIC_SEARCH                = new API_V2(BASIC_SEARCH_URI, HttpMethod.GET, Response.Status.OK);
        public static final API_V2 FACETED_SEARCH              = new API_V2(FACETED_SEARCH_URI, HttpMethod.POST, Response.Status.OK);
        public static final API_V2 GET_RELATIONSHIP_BY_GUID    = new API_V2(RELATIONSHIPS_URI + "guid/", HttpMethod.GET, Response.Status.OK);
        public static final API_V2 DELETE_RELATIONSHIP_BY_GUID = new API_V2(RELATIONSHIPS_URI + "guid/", HttpMethod.DELETE, Response.Status.NO_CONTENT);
        public static final API_V2 CREATE_RELATIONSHIP         = new API_V2(RELATIONSHIPS_URI , HttpMethod.POST, Response.Status.OK);
        public static final API_V2 UPDATE_RELATIONSHIP         = new API_V2(RELATIONSHIPS_URI , HttpMethod.PUT, Response.Status.OK);
        public static final API_V2 GET_BULK_HEADERS 		   = new API_V2(ENTITY_API + BULK_HEADERS, HttpMethod.GET, Response.Status.OK);
        public static final API_V2 UPDATE_BULK_SET_CLASSIFICATIONS = new API_V2(ENTITY_API + DataCatalogueClientV2.BULK_SET_CLASSIFICATIONS, HttpMethod.POST, Response.Status.OK);

        private API_V2(String path, String method, Response.Status status) {
            super(path, method, status);
        }
    }
        
    public DataCatalogueClientV2(String... baseUrls) throws DataCatalogueException {
        super(baseUrls);
    }
    
    public DataCatalogueClientV2(String[] baseUrls, String[] credentials) throws DataCatalogueException {
    	super(baseUrls, credentials);
    }
    
    /**
     * Bulk create APIs for all atlas type definitions, only new definitions will be created.
     * Any changes to the existing definitions will be discarded
     *
     * @param typesDef A composite wrapper object with corresponding lists of the type definition
     * @return A composite wrapper object with lists of type definitions that were successfully
     * created
     */
    public AtlasTypesDef createAtlasTypeDefs(final AtlasTypesDef typesDef) throws DataCatalogueException {
        return callAPI(API_V2.CREATE_ALL_TYPE_DEFS, AtlasTypesDef.class, AtlasType.toJson(typesDef));
    }
    
    /**
     * Bulk retrieval API for retrieving all type definitions in Atlas
     *
     * @return A composite wrapper object with lists of all type definitions
     */
    public AtlasTypesDef getAllTypeDefs(SearchFilter searchFilter) throws DataCatalogueException {
        return callAPI(API_V2.GET_ALL_TYPE_DEFS, AtlasTypesDef.class, searchFilter.getParams());
    }
    
}
