package com.itattitude.atlas.enel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.itattitude.atlas.AttributeDef;
import com.itattitude.atlas.ClassDef.BaseClassType;
import com.itattitude.atlas.DataCatalogue;
import com.itattitude.atlas.DataCatalogueException;
import com.itattitude.atlas.DataCatalogueException.ErrorCodeEnum;
import com.itattitude.atlas.EndDef;
import com.itattitude.atlas.EnumElementDef;
import com.itattitude.atlas.RelationshipDef.PropagationType;
import com.itattitude.atlas.RelationshipDef.UMLCategory;
import com.itattitude.atlas.SearchFilter;
import com.itattitude.atlas.AttributeDef.DataCatalogueCardinality;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class EnelDataCatalogue extends DataCatalogue {

	private String FREQUENCY_PRINCIPAL_TYPE = "frequency_principal_type";
	private String ACTIVITY_TYPE_KEY_VALUES = "activity_type_key";
	private String TYPE_PREVALENT_ACTIVITY_VALUES = "otype_prevalent_activity";
	private String ENEL_PROCESS = "enel_process";
	private String ENEL_GDS_PROCESS = "enel_gds_process";
	private String AFC_TABLE_TO_HIVE_PROCESS = "AFC_table_to_hive_process";
	private String AFC_ML_PROCESS_FROM_WINDOWS_OS = "AFC_ML_process_from_Windows_OS";
	private String TG_TABLE_TO_HIVE_PROCESS ="TG_table_to_hive_process";
	private String P_O_TABLE_TO_HIVE_PROCESS = "P_O_table_to_hive_process";
	private String ENEL_TABLE = "enel_table";
	private String ENEL_FOREIGN_KEY = "enel_foreign_key";
	private String ENEL_INDEX = "enel_index";
	private String ENEL_GDS_TABLE = "enel_gds_table";
	private String AFC_TABLE = "AFC_table";
	private String P_O_TABLE = "P_O_table";
	private String TG_TABLE = "TG_table";
	private String TD_TABLE = "TD_table";
	private String ENEL_COLUMN = "enel_column";
	private String ENEL_GDS_COLUMN = "enel_gds_column";
	private String AFC_COLUMN = "AFC_column";
	private String P_O_COLUMN = "P_O_column";
	private String TG_COLUMN = "TG_column";
	private String TD_COLUMN = "TD_column";
	private String ENEL_TABLE_FOREIGN_KEYS = "enel_table_foreign_keys";
	private String ENEL_TABLE_INDEXES = "enel_table_indexes";
	private String ENEL_FOREIGN_KEY_KEY_COLUMNS = "enel_foreign_key_key_columns";
	private String ENEL_FOREIGN_KEY_REFERENCES_TABLE = "enel_foreign_key_references_table";
	private String ENEL_FOREIGN_KEY_REFERENCES_COLUMNS = "enel_foreign_key_references_columns";
	private String ENEL_INDEX_COLUMNS = "enel_index_columns";
	private String ENEL_TABLE_COLUMNS = "enel_table_columns";
	
    private String[] TYPES = {  FREQUENCY_PRINCIPAL_TYPE, ACTIVITY_TYPE_KEY_VALUES, 
    		TYPE_PREVALENT_ACTIVITY_VALUES, ENEL_PROCESS, ENEL_GDS_PROCESS, AFC_TABLE_TO_HIVE_PROCESS,
    		AFC_ML_PROCESS_FROM_WINDOWS_OS, TG_TABLE_TO_HIVE_PROCESS, P_O_TABLE_TO_HIVE_PROCESS,
    		ENEL_TABLE,ENEL_FOREIGN_KEY,ENEL_INDEX,ENEL_GDS_TABLE,AFC_TABLE,P_O_TABLE,TG_TABLE,
    		TD_TABLE,ENEL_COLUMN, ENEL_GDS_COLUMN, AFC_COLUMN, P_O_COLUMN, TG_COLUMN, TD_COLUMN,
    		ENEL_TABLE_FOREIGN_KEYS, ENEL_TABLE_INDEXES, ENEL_FOREIGN_KEY_KEY_COLUMNS, 
    		ENEL_FOREIGN_KEY_REFERENCES_TABLE, ENEL_FOREIGN_KEY_REFERENCES_COLUMNS, ENEL_INDEX_COLUMNS,
    		ENEL_TABLE_COLUMNS
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
										"enel",
										frequencyPrincipalTypeValues),
					   createEnum(ACTIVITY_TYPE_KEY_VALUES,
							   			"The activity type",
							   			"2.0",
							   			"enel",
							   			activityTypeKeyValues),
					   createEnum(TYPE_PREVALENT_ACTIVITY_VALUES,
							   			"The prevalent activity inside the organization",
							   			"2.0",
							   			"enel",
							   			typePrevalentActivityValues));
    }

	private void createEnelClasses() {
		
		List<AttributeDef> enelProcessAttributesDef = Arrays.asList(
				new AttributeDef("URI","string",true,DataCatalogueCardinality.SINGLE,false,false),
				new AttributeDef("expression","string",true,DataCatalogueCardinality.SINGLE,false,
						false)
			);
		
		List<AttributeDef> enelGdsProcessAttributesDef = Arrays.asList(
				new AttributeDef("businessName","string",
						"Refers to the functional name of the table in scope",false,
						DataCatalogueCardinality.SINGLE,false,true),
				new AttributeDef("dataOwner","string",
						"Refers to the Data Owner Accountable for the table in scope ", false, 
						DataCatalogueCardinality.SINGLE,false,true),
				new AttributeDef("dataManager", "string", 
						"Refers to the Data Manager Accountable for the table in scope ", false,
						DataCatalogueCardinality.SINGLE,false,true),
				new AttributeDef("GBL", "string",
						"Refers to the GBL in scope for the table. NOTE: we need define as enumeration",
						false,DataCatalogueCardinality.SINGLE, false,true),
				new AttributeDef("GDS", "string", 
						"Refers to the GDS in scope for the table. NOTE: we need define as enumeration", 
						false, DataCatalogueCardinality.SINGLE, false, true),
				new AttributeDef("AuthorizationAndConfidentiality", "string", 
						"Refers to the name of the 'Authorization schema'. An 'Authorization schema' "
						+ "defines the policies on data Accessibility & Confidentiality. "
						+ "NOTE: must be defined", false, DataCatalogueCardinality.SINGLE, false, 
						false),
				new AttributeDef("ReferenceGlossary", "string", 
						"Refers Link / WIKI supporting the table comprehension", true, 
						DataCatalogueCardinality.SINGLE, false, false)
			);
		
		List<AttributeDef> tableToHiveProcessAttributesDef = Arrays.asList(
				new AttributeDef("HiveDatabaseName","string",false, DataCatalogueCardinality.SINGLE,
						false, false),
				new AttributeDef("HiveServer", "string", false, DataCatalogueCardinality.SINGLE, 
						false, false),
				new AttributeDef("HiveUserName", "string", false, DataCatalogueCardinality.SINGLE, 
						false, false),
				new AttributeDef("HivePassword", "string", false, DataCatalogueCardinality.SINGLE, 
						false, false)
			);
		
		List<AttributeDef> afcMlProcessFromWindowsOsAttributesDef = Arrays.asList(
				new AttributeDef("DSN","string",false,DataCatalogueCardinality.SINGLE,false,false)
			);
		
		List<AttributeDef> enelIndexAttributesDef = Arrays.asList(
				new AttributeDef("indexType", "string", true, DataCatalogueCardinality.SINGLE,false,
						false),
				new AttributeDef("isUnique", "boolean", true, DataCatalogueCardinality.SINGLE,false,
						false),
				new AttributeDef("comment", "string", true, DataCatalogueCardinality.SINGLE,false,false)
			);

		List<AttributeDef> enelTableAttributesDef= Arrays.asList(
				new AttributeDef("URI","string",
						"Refers to the URI name where the file or table is located. "
						+ "NOTE: must be defined a naming convention",false,
						DataCatalogueCardinality.SINGLE,true,false),
				new AttributeDef("gdsOwners", "array<string>", 
						"Refers the IT administrator/ data eng. accountable for the table in scope",false,
						DataCatalogueCardinality.SET,false,false),
				new AttributeDef("tableCreationTime", "date","Refers file/table creation time",false,
						DataCatalogueCardinality.SINGLE,false,false),
				new AttributeDef("tableLastAccessTime","date","Refers file/table last access time",
						false,DataCatalogueCardinality.SINGLE,false,false)
				
			);
		
		HashMap<String,String> enelTableOptions = new HashMap<String,String>() {
			private static final long serialVersionUID = 1L;

			{
				put("schemaElementsAttribute", "columns");
			}
		};
		
		List<AttributeDef> enelGdsTableAttributesDef = Arrays.asList(
				new AttributeDef("dataOwners", "array<string>", 
						"Refers to the Data Owner Accountable for the table in scope ",true, 
						DataCatalogueCardinality.SET,false,true),
				new AttributeDef("dataManagers", "array<string>",
						"Refers to the Data Manager Accountable for the table in scope ",false,
						DataCatalogueCardinality.SET,false, true),
				new AttributeDef("issueFrequency", FREQUENCY_PRINCIPAL_TYPE,
						"Define the data updating frequency. It could be: -  Fixed: - daily - "
						+ "monthly - etc..- On event. NOTE: We must define enumerationfor this "
						+ "attribute", false, DataCatalogueCardinality.SINGLE,false, false),
				new AttributeDef("issueFrequencyNote", "string",
						"To be fulfilled only if the Frequency is 'On event'. This is a free note "
						+ "filed describing the 'event' / 'rule'. NOTE: it must appear as constraint "
						+ "on 'on event' issue frequency", true, DataCatalogueCardinality.SINGLE, false,
						false),
				new AttributeDef("GBL", "string", 
						"Refers to the GBL in scope for the table. NOTE: we need define as enumeration",
						false, DataCatalogueCardinality.SINGLE, false, true),
				new AttributeDef("GDS", "string",
						"Refers to the GDS in scope for the table. NOTE: we need define as enumeration",
						false, DataCatalogueCardinality.SINGLE, false, true),
				new AttributeDef("thirdOwner", "string",
						"Refers to the external data provider in scope for the table. NOTE: Not clear",
						true, DataCatalogueCardinality.SINGLE, false, true),
				new AttributeDef("businessProcesses", "array<string>",
						"Refers to the Business process supported by the table in scope, e.g.: O&M; "
						+ "E&C; GRID; etc.…", true, DataCatalogueCardinality.LIST, false, true),
				new AttributeDef("dataQuality", "string",
						"Refers data quality of the current entity. NOTE: To deepen, indicates the "
						+ "quality level of the table. Maybe will become enumeration", false,
						DataCatalogueCardinality.SINGLE, false, true),
				new AttributeDef("retention", "int",
						"Refers to the name of the 'Retention schema'. A 'Retention schema' defines "
						+ "the policies of persistent data and records management for meeting legal "
						+ "and business data archival requirements. Once the right of use is expired "
						+ "shall be defined if the data shall be deleted or make 'anonymous'. NOTE: "
						+ "this schema must be defined", true, DataCatalogueCardinality.SINGLE,false,
						false),
				new AttributeDef("authorizationAndConfidentiality", "string",
						"Refers to the name of the 'Authorization schema'. An 'Authorization schema' "
						+ "defines the policies on data Accessibility & Confidentiality. NOTE: must "
						+ "be defined", false, DataCatalogueCardinality.SINGLE, false, false),
				new AttributeDef("referenceGlossary", "string",
						"Refers Link / WIKI supporting the table comprehension", true, 
						DataCatalogueCardinality.SINGLE,false, false),
				new AttributeDef("tableLastAccessUser", "string",
						"User name of the user who is logged in", false,
						DataCatalogueCardinality.SINGLE, false, false)
			);
		List<AttributeDef> specificTableAttributesDef = Arrays.asList(
				new AttributeDef("technicalTableName", "string",
						"Refers to the technical name of the table in scope ", false,
						DataCatalogueCardinality.SINGLE, false, true),
				new AttributeDef("sourceSystem", "string",
						"Refers to the data source (last application where the data are coming from "
						+ "before the data ingestion)", true, DataCatalogueCardinality.SINGLE, false,
						true)
			);
		List<AttributeDef> enelColumnAttributesDef = Arrays.asList(
				new AttributeDef("dataType", "string", 
						"Refers to the 'Data Type' characterizing the data in scope for a given "
						+ "column, for example: Array; String; Boolean; others... NOTE: To change in "
						+ "enum type",true, DataCatalogueCardinality.SINGLE, false, true),
				new AttributeDef("length", "int", 
						"Refers to the length allowed for the data in scope. NOTE: to add constraint",
						true, DataCatalogueCardinality.SINGLE, false, false),
				new AttributeDef("defaultValue", "string",
						"Refers to the default value of the column", true,
						DataCatalogueCardinality.SINGLE, false, false),
				new AttributeDef("comment", "string", true, DataCatalogueCardinality.SINGLE,false,
						false),
				new AttributeDef("isNullable", "boolean", "Refers the field could be nullable or not",
						true, DataCatalogueCardinality.SINGLE, false, false),
				new AttributeDef("isPrimaryKey", "boolean", 
						"Refers if the field MUST be during the ingestion", true,
						DataCatalogueCardinality.SINGLE, false, false),
				new AttributeDef("lasUpdate", "date", 
						"A time stamp for the last upfate of the field", true,
						DataCatalogueCardinality.SINGLE, false, false)
			);
		
		HashMap<String,String> enelColumnOptions = new HashMap<String,String>() {
			private static final long serialVersionUID = 1L;

			{
				put("schemaAttributes", "[\"name\", \"description\", \"owner\", \"dataType\", "
						+ "\"defaultValue\", \"comment\", \"isNullable\", \"isPrimaryKey\", \"lastUpdate\", " 
				 		+ "\"dataOwners\", \"dataManagers\", \"retention\", \"authorizationAndConfidentiality\", "
				 		+ "\"frequency\", \"issueFrequencyNote\", \"domainName\", \"technicalName\"");
			} 
		};
			
		List<AttributeDef> enelGDSColumnAttributesDef = Arrays.asList(
				new AttributeDef("dataOwners", "array<string>", 
						"Refers to the Data Owners Accountable for the 'data' in scope", true,
						DataCatalogueCardinality.SET, false, true),
				new AttributeDef("dataManagers", "array<string>", 
						"Refers to the Data Owners Accountable for the 'data' in scope", true,
						DataCatalogueCardinality.SET, false, true),
				new AttributeDef("retention", "int", 
						"Refers to the name of the 'Retention schema'. A 'Retention schema' "
						+ "defines the policies of persistent data and records management for meeting "
						+ "legal and business data archival requirements. Once the right of use is expired "
						+ "shall be defined if the data shall be deleted or make 'anonymous'. NOTE: this schema "
						+ "must be defined", true, DataCatalogueCardinality.SINGLE,false, false),
				new AttributeDef("authorizationAndConfidentiality", "string",
						"Refers to the name of the 'Authorization schema'. An 'Authorization schema' "
						+ "defines the policies on data Accessibility & Confidentiality. NOTE: must "
						+ "be defined", true, DataCatalogueCardinality.SINGLE, false, false),
				new AttributeDef("frequency", FREQUENCY_PRINCIPAL_TYPE,
						"Define the data updating frequency. It could be: -  Fixed: - daily- monthly - On event. "
						+ "NOTE: define the enum type", true, DataCatalogueCardinality.SINGLE, false, false),
				new AttributeDef("issueFrequencyNote", ENEL_PROCESS,
						"To be fulfilled only if the Frequency is 'On event'. This is a free note filed describing "
						+ "the 'event' / 'rule' NOTE: must be defined the constraint", true,
						DataCatalogueCardinality.SINGLE, false, false)
			);

		List<AttributeDef> specificColumnAttributesDef = Arrays.asList(
				new AttributeDef("domainName", "string", 
						"It relates to the domain of membership of the entity , which in the case of an enel "
						+ "column, COULD be operational unit that CAN coincide with the business name of the "
						+ "membership table", false, DataCatalogueCardinality.SINGLE, false, true),
				new AttributeDef("technicalName", "string", "Refers to the technical name of the column in scope",
						false, DataCatalogueCardinality.SINGLE, false, true)
			);
 
		createClasses(createClass(ENEL_PROCESS, 
									"The base enel process", 
									"2.0", 
									BaseClassType.PROCESS,
									"enel",
									enelProcessAttributesDef),
						createClass(ENEL_GDS_PROCESS,
									"The GDS enel process",
									"2.0",
									ENEL_PROCESS,
									"enel",
									enelGdsProcessAttributesDef),
						createClass(AFC_TABLE_TO_HIVE_PROCESS,
									"The Process for the AFC_table",
									"2.0",
									ENEL_GDS_PROCESS,
									"enel",
									tableToHiveProcessAttributesDef),
						createClass(AFC_ML_PROCESS_FROM_WINDOWS_OS,
									"A test machine-learning process",
									"2.0",
									ENEL_GDS_PROCESS,
									"enel",
									afcMlProcessFromWindowsOsAttributesDef),
						createClass(TG_TABLE_TO_HIVE_PROCESS,
									"The Process for the TG_table",
									"2.0",
									ENEL_GDS_PROCESS,
									"enel",
									tableToHiveProcessAttributesDef),
						createClass(P_O_TABLE_TO_HIVE_PROCESS, 
									"The Process for the PO_table", 
									"2.0", 
									ENEL_GDS_PROCESS, 
									"enel",
									tableToHiveProcessAttributesDef),
						createClass(ENEL_FOREIGN_KEY, 
									"The type representing the foreign key",
									"2.0",
									BaseClassType.DATASET,
									"enel",
									Collections.<AttributeDef>emptyList()),
						createClass(ENEL_INDEX,
									"An index on an ENEL table",
									"2.0",
									BaseClassType.DATASET,
									"enel",
									enelIndexAttributesDef),
						createClass(ENEL_TABLE,
									"The base Enel table",
									"2.0",
									BaseClassType.DATASET,
									"enel",
									enelTableAttributesDef,
									enelTableOptions),
						createClass(ENEL_GDS_TABLE,
									"The GDS part of the enel tables",
									"2.0",
									ENEL_TABLE,
									"enel",
									enelGdsTableAttributesDef),
						createClass(AFC_TABLE,
									 "The base AFC table",
									 "2.0",
									 ENEL_GDS_TABLE,
									 "enel",
									 specificTableAttributesDef),
						createClass(P_O_TABLE,
									 "The base Person nd Organization table",
									 "2.0",
									 ENEL_GDS_TABLE,
									 "enel",
									 specificTableAttributesDef),
						createClass(TG_TABLE,
									 "The base Thermal Generation table",
									 "2.0",
									 ENEL_GDS_TABLE,
									 "enel",
									 specificTableAttributesDef),
						createClass(TD_TABLE,
									 "The base Trading table",
									 "2.0",
									 ENEL_GDS_TABLE,
									 "enel",
									 specificTableAttributesDef),
						createClass(ENEL_COLUMN,
									"The base column for Enel Tables",
									"2.0",
									BaseClassType.DATASET,
									"enel",
									enelColumnAttributesDef,
									enelColumnOptions),
						createClass(ENEL_GDS_COLUMN,
									"The base column with business entity definited",
									"2.0",
									ENEL_COLUMN,
									"enel",
									enelGDSColumnAttributesDef),
						createClass(AFC_COLUMN,
									"The base column for AFC columns",
									"2.0",
									ENEL_GDS_COLUMN,
									"enel",
									specificColumnAttributesDef),
						createClass(P_O_COLUMN,
									"The base column for Person and Organization columns",
									"2.0",
									ENEL_GDS_COLUMN,
									"enel",
									specificColumnAttributesDef),
						createClass(TG_COLUMN,
									"The base column for Thermal Generation columns",
									"2.0",
									ENEL_GDS_COLUMN,
									"enel",
									specificColumnAttributesDef),
						createClass(TD_COLUMN,
									"The base column for Trading columns",
									"2.0",
									ENEL_GDS_COLUMN,
									"enel",
									specificColumnAttributesDef)
						);
	}
	
	private void createEnelRelationship() {
			
		EndDef addForeignKeysToEnelTable = new EndDef("foreign_keys", ENEL_TABLE, DataCatalogueCardinality.SET, 
											true);
		EndDef addTableToEnelForeignKey = new EndDef("table", ENEL_FOREIGN_KEY,DataCatalogueCardinality.SINGLE);
		
		EndDef addIndexesToEnelTable = new EndDef("indexes", ENEL_TABLE, DataCatalogueCardinality.SET, true);
		EndDef addTableToEnelIndex = new EndDef("table", ENEL_INDEX, DataCatalogueCardinality.SINGLE);
		
		EndDef addKeyColumnsToEnelForeignKey = new EndDef("key_columns", ENEL_FOREIGN_KEY, 
												DataCatalogueCardinality.SET);
		EndDef addKeyColumnReferencesToEnelColumn = new EndDef("key_column_references", ENEL_COLUMN, 
													DataCatalogueCardinality.SET);
		
		EndDef addReferencesTableToEnelForeignKey = new EndDef("references_table", ENEL_FOREIGN_KEY, 
													DataCatalogueCardinality.SINGLE);
		EndDef addReferencesForeignKeyToEnelTable = new EndDef("foreign_key_references", ENEL_TABLE,
													DataCatalogueCardinality.SET);
		
		EndDef addReferencesColumnsToEnelForeignKey = new EndDef("references_columns", ENEL_FOREIGN_KEY,
												DataCatalogueCardinality.SET);
		EndDef addReferencesForeignKeyToEnelColumn = new EndDef("foreign_key_references", ENEL_COLUMN, 
														DataCatalogueCardinality.SET);
		
		EndDef addColumnsToEnelIndex = new EndDef("columns", ENEL_INDEX, DataCatalogueCardinality.SET);
		EndDef addIndexesToenelColumn = new EndDef("indexes", ENEL_COLUMN, DataCatalogueCardinality.SET);
		
		EndDef addColumnsToEnelTable = new EndDef("columns", ENEL_TABLE, DataCatalogueCardinality.SET, true);
		EndDef addTableToEnelColumn = new EndDef("table", ENEL_COLUMN,  DataCatalogueCardinality.SINGLE);
		
		createRelationships(createRelationship(ENEL_TABLE_FOREIGN_KEYS,
												"Relation between enel tables and their foreign keys", 
												"2.0.0", 
												"enel",
												UMLCategory.COMPOSITION,
												PropagationType.NONE,
												addForeignKeysToEnelTable, addTableToEnelForeignKey),
							createRelationship(ENEL_TABLE_INDEXES, 
												"Relation between enel tables and their indexes", 
												"2.0.0",
												"enel", 
												UMLCategory.COMPOSITION, 
												PropagationType.NONE, 
												addIndexesToEnelTable, addTableToEnelIndex),
							createRelationship(ENEL_FOREIGN_KEY_KEY_COLUMNS, 
												"Association between the Enel columns and the foreign keys", 
												"2.0.0", 
												"enel", 
												UMLCategory.ASSOCIATION, 
												PropagationType.NONE, 
												addKeyColumnsToEnelForeignKey, addKeyColumnReferencesToEnelColumn),
							createRelationship(ENEL_FOREIGN_KEY_REFERENCES_TABLE, 
												"Association between the Enel foreign key and the correnspondin enel table", 
												"2.0.0", 
												"enel", 
												UMLCategory.ASSOCIATION, 
												PropagationType.NONE, 
												addReferencesTableToEnelForeignKey, 
												addReferencesForeignKeyToEnelTable),
							createRelationship(ENEL_FOREIGN_KEY_REFERENCES_COLUMNS, 
												"Association betwen the foreign key references and the columns referenced", 
												"2.0.0", 
												"enel", 
												UMLCategory.ASSOCIATION, 
												PropagationType.NONE, 
												addReferencesColumnsToEnelForeignKey, 
												addReferencesForeignKeyToEnelColumn),
							createRelationship(ENEL_INDEX_COLUMNS,
												"Association the enel index and the columns",
												"2.0.0",
												"enel",
												UMLCategory.ASSOCIATION,
												PropagationType.NONE,
												addColumnsToEnelIndex, addIndexesToenelColumn),
							createRelationship(ENEL_TABLE_COLUMNS,
												"Relation beween enel table and its columns",
												"2.0.0",
												"enel",
												UMLCategory.COMPOSITION,
												PropagationType.NONE,
												addColumnsToEnelTable,
												addTableToEnelColumn)
				);
	}



	private void createEnelTypesDefinitions() {

		createEnelEnums();
    	createEnelClasses();
    	createEnelRelationship();
    }
	void verifyTypesCreated() throws DataCatalogueException {
		MultivaluedMap<String, String> searchParams = new MultivaluedMapImpl();

        for (String typeName : TYPES) {
            searchParams.clear();
            searchParams.add(SearchFilter.PARAM_NAME, typeName);

            SearchFilter  searchFilter = new SearchFilter(searchParams);
            try {
                getAllTypeDefs(searchFilter);
            } catch (DataCatalogueException e) {
            	if(e.getErrorCode() == ErrorCodeEnum.TYPE_NOT_CREATED) {
            		throw new DataCatalogueException(e.getMessage() + " " + typeName, e.getErrorCode());
            	}
            }
        }
	}
	void createEnelTypes() throws DataCatalogueException {
		
    	createEnelTypesDefinitions();
    	saveTypesDef();
    	verifyTypesCreated();
	}
}
