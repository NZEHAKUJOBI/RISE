package com.matchrate.RISE.etl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {

    private static Properties properties;
    private static Map<String, String> connectionParameters;

    private static Map<String, String> customParameters;
    private static Map<String, String> externalProperties;
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

//    private static void initialize() {
//        properties = new Properties();
//        connectionParameters = new HashMap<>();
//        customParameters = new HashMap<>();
//        try {
//            File resourceFile = new File(Constants.DEFAULT_PROPERTIES);
//            String propertiesLink = resourceFile.exists() ? getExternalProperties().get("externalProperties") : Constants.DEFAULT_PROPERTIES;
//            logger.info(resourceFile.exists()  ? String.format("External properties file %s was provided.", propertiesLink)
//                    : String.format("No external properties file provided using default %s.", propertiesLink));
//            if(resourceFile.exists()) {
//                try (InputStream inputStream = Files.newInputStream(resourceFile.toPath())) {
//                    properties.load(inputStream);
//                }
//            }else {
//                ClassLoader classLoader = Config.class.getClassLoader();
//                try (InputStream inputStream = classLoader.getResourceAsStream(propertiesLink)) {
//                    properties.load(inputStream);
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading properties file: " + e.getMessage());
//        }
//    }
static {
    initialize();
}

    private static void initialize() {
        if (properties == null) {
            properties = new Properties();
            connectionParameters = new HashMap<>();
            try {
                String propertiesLink = getPropertiesLink();
                logger.info(resourceFileExists(propertiesLink) ?
                        String.format("External properties file %s was provided.", propertiesLink) :
                        String.format("No external properties file provided, using default %s.", propertiesLink));
                try (InputStream inputStream = Files.newInputStream(Paths.get(propertiesLink))) {
                    properties.load(inputStream);
                }
            } catch (Exception e) {
                logger.error("Error reading properties file: {}", e.getMessage());
            }
            // Initialize connectionParameters here
            connectionParameters = getConnectionParameters();
        }
    }

    private static String getPropertiesLink() {
        return resourceFileExists(Constants.DEFAULT_PROPERTIES) ?
                Constants.DEFAULT_PROPERTIES :
                Constants.DEFAULT_PROPERTIES; // Default to application.properties if external not found
    }

    private static boolean resourceFileExists(String fileName) {
        return Files.exists(Paths.get(fileName));
    }


    public static Connection connectToDatabase(String dbName) {
        initialize();
        String url = properties.getProperty("spring.datasource.url");
        String user = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");

        if(dbName.equals(Constants.LAMISPLUS)){
            url = properties.getProperty("lamisplus.db.url");

            user = properties.getProperty("lamisplus.db.user");
            password = properties.getProperty("lamisplus.db.password");
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.printf("Connected to the %s Database server successfully.%n", dbName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static Map<String, String> getConnectionParameters(){
        try{
            initialize();

            String etlUrl = properties.getProperty("spring.datasource.url");
            String etlUser = properties.getProperty("spring.datasource.username");
            String etlPassword = properties.getProperty("spring.datasource.password");
            String etlDBName = properties.getProperty("spring.datasource.name");
            String lamisPlusUrl = properties.getProperty("lamisplus.db.url");
            String lamisPlusUser = properties.getProperty("lamisplus.db.user");
            String lamisPlusPassword = properties.getProperty("lamisplus.db.password");
            String lamisPlusDBName = properties.getProperty("lamisplus.db.name");
            String projectStartDate = properties.getProperty("project.start.date");
            String suspended = properties.getProperty("suspended");
            String Commenced = properties.getProperty("commenced");



            connectionParameters.put("etlUrl", etlUrl);
            connectionParameters.put("etlUser", etlUser);
            connectionParameters.put("etlPassword", etlPassword);
            connectionParameters.put("etlDBName", etlDBName);
            connectionParameters.put("lamisPlusUrl", lamisPlusUrl);
            connectionParameters.put("lamisPlusUser", lamisPlusUser);
            connectionParameters.put("lamisPlusPassword", lamisPlusPassword);
            connectionParameters.put("lamisPlusDBName", lamisPlusDBName);
            connectionParameters.put("projectStartDate", projectStartDate);
            connectionParameters.put("suspended", suspended);
            connectionParameters.put("Commenced", Commenced);


        }catch(Exception ex){
            ex.printStackTrace();
        }

        return connectionParameters;
    }

    public static Map<String, String> getCustomParameters(){
        try{
            initialize();
            String projectStartDate = properties.getProperty("project.start.date");
            customParameters.put("projectStartDate", projectStartDate);

        }catch(Exception ex){
            ex.printStackTrace();
        }

        return customParameters;
    }

    public static void setExternalProperties(String externalProps) {

        try {
            initialize();
            getExternalProperties().put("externalProperties", externalProps);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static Map<String, String> getExternalProperties(){
        //Feed data from an external file
        externalProperties = new HashMap<>();
        externalProperties.put("externalProperties", Constants.DEFAULT_PROPERTIES);
        return externalProperties;
    }

    public static class Constants {

        public static String biometric = "biometric";
        public static String LAMISPLUS = "lamisplus";

//        public static String DEFAULT_PROPERTIES = "application.properties";
        public static final String DEFAULT_PROPERTIES = "src/main/resources/application.properties";


    }
}
