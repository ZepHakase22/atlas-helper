package com.itattitude.atlas.enel;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
    static void createCatalogue(String[] hosts) throws DataCatalogueException {
		
		EnelDataCatalogue enelDataCatalogue;
		String[] basicAuthUsernamePassword;
		
		try {
			enelDataCatalogue = new EnelDataCatalogue(hosts);
		} catch (DataCatalogueException e){
			if(!e.isUnrecoverable() && e.getErrorCode()==ErrorCodeEnum.MISSING_USERNAME_AND_PASSWORD) {
				basicAuthUsernamePassword = getBasicAuthenticationInput();
				enelDataCatalogue=new EnelDataCatalogue(hosts,basicAuthUsernamePassword);
			} else throw e;
		}
		
		// Create v2 enel types in Atlas for the enel metadata model
		enelDataCatalogue.createEnelTypes();
		LOG.info("\nCreating all Enel types!!");
	}
	
	@VisibleForTesting
	static void deleteCatalogue(String [] hosts) throws DataCatalogueException {
		
	}
	
	@VisibleForTesting
	static void listCatalogue(String [] hosts) throws DataCatalogueException {
		
	}

	
	private static void Usage(String[] args) throws DataCatalogueException {
		
		Options options = new Options();
		Option server = new Option("s", "servers", true, "The uri of the Atlas Servers");
		server.setArgs(Option.UNLIMITED_VALUES);
		server.setArgName("url1,url2,...,urlN");
		server.setValueSeparator(',');
		server.setRequired(false);
		
		Option create = new Option("c","create",false,"Create enel data types");
		Option delete = new Option("d","delete",false,"Delete enel data types");
		Option list = new Option("l","list",false,"List enel data types");
		Option help = new Option("h","help", false, "Print this help");

		OptionGroup operatorGroup = new OptionGroup();
		
		operatorGroup.addOption(help);
		operatorGroup.addOption(create);
		operatorGroup.addOption(delete);
		operatorGroup.addOption(list);
		
		OptionGroup serverGroup =new OptionGroup();
		
		serverGroup.addOption(help);
		serverGroup.addOption(server);
		
		options.addOptionGroup(operatorGroup).addOptionGroup(serverGroup);

		CommandLine cmd;	
		String[] hosts = null;
		HelpFormatter formatter = new HelpFormatter();
		
		CommandLineParser parser = new DefaultParser();
		try {
			cmd = parser.parse(options,args);
			hosts = cmd.getOptionValues("s");
			if(cmd.hasOption('h')) {
				formatter.printHelp("CreateEnelDataGovernance", "Manage Enel Data Catalogue: default=list", 
						options, "Report  error to ZepHakase2@gmail.com", true);
				System.exit(-1);
			}
			if(cmd.hasOption('s'))
				hosts = cmd.getOptionValues("s");
			if(cmd.hasOption('c'))
				createCatalogue(hosts);
			else if(cmd.hasOption('d')) 
				deleteCatalogue(hosts);
			else
				listCatalogue(hosts);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			formatter.printHelp("CreateEnelDataGovernance", "Manage Enel Data Catalogue: default=list", options, 
					"Report error to ZepHakase2@gmail.com", true);
			System.exit(-1);
		}
	}
	public static void main(String[] args) {
		

		if(DataCatalogue.isDebug())
			org.apache.log4j.BasicConfigurator.configure();
		
		try {
			Usage(args);
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
