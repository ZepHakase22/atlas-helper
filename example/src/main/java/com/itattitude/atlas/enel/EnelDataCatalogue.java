package com.itattitude.atlas.enel;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.itattitude.atlas.DataCatalogue;
import com.itattitude.atlas.DataCatalogueException;
import com.itattitude.atlas.EnumElementDef;
import com.itattitude.atlas.SearchFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class EnelDataCatalogue extends DataCatalogue {

	private String FREQUENCY_PRINCIPAL_TYPE = "frequency_principal_type";
	private String ACTIVITY_TYPE_KEY_VALUES = "activity_type_key";
	private String TYPE_PREVALENT_ACTIVITY_VALUES = "otype_prevalent_activity";
	
    private String[] TYPES = { FREQUENCY_PRINCIPAL_TYPE, ACTIVITY_TYPE_KEY_VALUES, 
    		TYPE_PREVALENT_ACTIVITY_VALUES 
	};

	
	EnelDataCatalogue(String[] args) throws DataCatalogueException {
		super(args);
		
	}
	EnelDataCatalogue(String[] args, String[] credentials) throws DataCatalogueException {
		super(args,credentials);
	}
    private void createEnelEnums() {
    	List<EnumElementDef> frequencyPrincipalTypeValues = Arrays.asList( 
    			new EnumElementDef("hourly",1),
    			new EnumElementDef("daily",2),
    			new EnumElementDef("weekly",3),
    			new EnumElementDef("monthly",4),
    			new EnumElementDef("half-yearly",5),
    			new EnumElementDef("yearly",6),
    			new EnumElementDef("onEvent",7)
    	);
    	
    	List<EnumElementDef> activityTypeKeyValues =  Arrays.asList(
    			new EnumElementDef("30",30),
    			new EnumElementDef("31",31),
    			new EnumElementDef("32",32)
    	);
    	
    	List<EnumElementDef> typePrevalentActivityValues = Arrays.asList(
    			new EnumElementDef("Line", 1),
    			new EnumElementDef("Staff",2),
    			new EnumElementDef("Service",3)
    	);
    	
    	createEnums(createEnum(FREQUENCY_PRINCIPAL_TYPE,
										"The frequence wih which will be emitted the data",
										"2.0",
										frequencyPrincipalTypeValues),
					   createEnum(ACTIVITY_TYPE_KEY_VALUES,
							   			"The activity type",
							   			"2.0",
							   			activityTypeKeyValues),
					   createEnum(TYPE_PREVALENT_ACTIVITY_VALUES,
							   			"The prevalent activity inside the organization",
							   			"2.0",
							   			typePrevalentActivityValues));
    }

	private void createEnelTypesDefinitions() {

		createEnelEnums();
    	createEnelClasses();
    }
	private void createEnelClasses() {

		
	}
	List<String> verifyTypesCreated() throws DataCatalogueException {
        MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();

        for (String typeName : TYPES) {
            searchParams.add(SearchFilter.PARAM_NAME, typeName);
        }

        SearchFilter searchFilter = new SearchFilter(searchParams);
        return getAllTypeDefs(searchFilter);
	}
	List<String> createEnelTypes() throws DataCatalogueException {
		
    	createEnelTypesDefinitions();
    	saveTypesDef();
    	return verifyTypesCreated();		
    	
	}
}
