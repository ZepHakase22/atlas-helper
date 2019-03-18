package com.itattitude.atlas.enel;

import java.io.Console;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;
import com.itattitude.atlas.DataCatalogueException;
import com.itattitude.atlas.DataCatalogueException.ErrorCodeEnum;

public class CreateEnelDataCatalogue {
	
	@VisibleForTesting
    public static String[] getBasicAuthenticationInput() {
        String username = null;
        String password = null;

        try {
            Console console = System.console();
            if (console == null) {
                System.err.println("Couldn't get a console object for user input");
                System.exit(1);
            }

            username = console.readLine("Enter username for atlas :- ");

            char[] pwdChar = console.readPassword("Enter password for atlas :- ");
            if(pwdChar != null) {
                password = new String(pwdChar);
            }

        } catch (Exception e) {
            System.out.print("Error while reading user input");
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
		List<String> names=enelDataCatalogue.createEnelTypes();

		assert (!names.isEmpty());
		names.forEach(s-> System.out.println("Created type [" + s + "]"));

		System.out.println("\nCreating all Enel types!!");
	}
	
	public static void main(String[] args) {
    	
		try {
			createCatalogue(args);
		} catch (DataCatalogueException e) {
			if(e.getErrorCode()==ErrorCodeEnum.MISSING_ATLAS_REST_ADDRESS) {
				System.out.println("com.itattitude.atlas.CreateEnelDataCatalogue "
						+ "<missing Atlas Rest address "
						+ "\nUSAGE: <http/https>://<atlas-fqdn>:<atlas-port>" 
						+ "\nExample: http://localhost:21000");
			} else if(e.getErrorCode() == ErrorCodeEnum.CLIENT_RESPONSE_ERROR) {
				System.out.println("com.itattitude.atlas.CreateEnelDataCatalogue "
						+ "<"+e.getMessage());
			} else e.printStackTrace();
			System.exit(-1);
		}
	}

}
