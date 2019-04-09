package com.itattitude.atlas.enel;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.itattitude.atlas.DataCatalogue;
import com.itattitude.atlas.DataCatalogueException;
import com.itattitude.atlas.DataCatalogueException.ErrorCodeEnum;

public class CreateEnelDataCatalogue {
	private static final Logger LOG = LoggerFactory.getLogger(CreateEnelDataCatalogue.class);
	
	@VisibleForTesting
    public static String[] getBasicAuthenticationInput() {
        String username = null;
        String password = null;

        try {
            Console console = System.console();
            if (console == null) {
            	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            	System.out.print("Enter username for atlas :- ");
            	username = br.readLine();
            	System.out.print("Enter password for atlas (not masked):- ");
            	password = br.readLine();
            } else {
            	username = console.readLine("Enter username for atlas :- ");
            	char[] pwdChar = console.readPassword("Enter password for atlas :- ");
	            if(pwdChar != null) {
	                password = new String(pwdChar);
	            }
            }
        } catch (Exception e) {
            LOG.error("Error while reading user input");
            System.exit(1);
        }
        return new String[]{username, password};
    }

	@VisibleForTesting
    static void createCatalogue(String[] args) throws DataCatalogueException {
		
		EnelDataCatalogue enelDataCatalogue;
		String[] basicAuthUsernamePassword;
		
		try {
			enelDataCatalogue = new EnelDataCatalogue(args);
		} catch (DataCatalogueException e){
			if(!e.isUnrecoverable() && e.getErrorCode()==ErrorCodeEnum.MISSING_USERNAME_AND_PASSWORD) {
				basicAuthUsernamePassword = getBasicAuthenticationInput();
				enelDataCatalogue=new EnelDataCatalogue(args,basicAuthUsernamePassword);
			} else throw e;
		}
		
		// Create v2 enel types in Atlas for the enel metadata model
		enelDataCatalogue.createEnelTypes();
		LOG.info("\nCreating all Enel types!!");
	}
	
	public static void main(String[] args) {

		if(DataCatalogue.isDebug())
			org.apache.log4j.BasicConfigurator.configure();
		
		try {
			createCatalogue(args);
		} catch (DataCatalogueException e) {
			if(e.getErrorCode()==ErrorCodeEnum.MISSING_ATLAS_REST_ADDRESS) {
				LOG.error("Missing Atlas Rest address "
						+ "\nUSAGE: <http/https>://<atlas-fqdn>:<atlas-port>" 
						+ "\nExample: http://localhost:21000");
			} else if(e.getErrorCode() == ErrorCodeEnum.CLIENT_RESPONSE_ERROR || 
					e.getErrorCode() == ErrorCodeEnum.TYPE_NOT_CREATED) {
				LOG.error(e.getMessage());
			} else e.printStackTrace();
			System.exit(-1);
		}
	}

}
