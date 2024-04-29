package com.matchrate.RISE.etl.extraction;
import com.matchrate.RISE.etl.utils.Config;
import com.matchrate.RISE.etl.utils.Queries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class extractor {

    static Logger logger = LoggerFactory.getLogger(extractor.class);

    public static void performDataInsertion() throws SQLException {
        Connection biometricDB = null;
        Connection lamisPlusDB = null;
        try {
            if(Config.getConnectionParameters().get("suspended").equals("false")) {
                logger.info(String.format("Starting Data Extraction at %s", new java.util.Date()));

                biometricDB = Config.connectToDatabase(Config.Constants.biometric);
                lamisPlusDB = Config.connectToDatabase(Config.Constants.LAMISPLUS);

                //Begin Data Extraction and Load
                //TODO: Create a database table to hold the status of extractions ans runs...
                dbLinks(lamisPlusDB, biometricDB);
                // Call the insertBiometric method here
                insertBiometric(biometricDB);
            }
        } catch(Exception ex) {
            closeConnections(lamisPlusDB, biometricDB);
            ex.printStackTrace();
        }finally{
            if(lamisPlusDB != null) lamisPlusDB.close();
            if(biometricDB != null) biometricDB.close();
            if(Config.getConnectionParameters().get("suspended").equals("false"))
                logger.info(String.format("Finished Data Extraction at %s", new java.util.Date()));
            else
                logger.info("Data Extraction is suspended, set suspended to false in config.properties to enable");
        }



    } private static void dbLinks (Connection lamisPlusDB, Connection etlDB){
        Statement lamisPlusStatement = null, etlStatement = null;

        try {
            Map<String, String> databaseCredentials = Config.getConnectionParameters();
            String hostname = "localhost";
            String port = "5432";
            String user = databaseCredentials.get("etlUser"); //TODO: We can create an environmental variable separately for this
            String password = databaseCredentials.get("etlPassword"); //TODO: We can create an environmental variable separately for this
            String hostAddr = "127.0.0.1";
            String etlDBName = databaseCredentials.get("etlDBName");
            String lamisPlusDBName = databaseCredentials.get("lamisPlusDBName");

            lamisPlusStatement = lamisPlusDB.createStatement();
            lamisPlusStatement.execute(Queries.DBLinks.createLamiplusDbLink(hostname,  port,  user,  password,   etlDBName,  hostAddr));

            etlStatement =  etlDB.createStatement();
            etlStatement.execute(Queries.DBLinks.createEtlDblink(hostname,  port,  user,  password,  lamisPlusDBName ,  hostAddr));

            //Call Next Ite
            logger.info("DB Links closed successfully...");
        } catch (Exception e) {
            logger.error(String.format("Errors encountered creating Database Links %s", e.getMessage()));
            closeStatement(lamisPlusStatement);
            closeStatement(etlStatement);
            e.printStackTrace();
        }
    }
      public static void insertBiometric(Connection biometric)  throws SQLException{
        Statement etlStatement = null;
        try {

            etlStatement = biometric.createStatement();
            etlStatement.executeUpdate(Queries.biometric.biometric_QUERY);
            logger.info("Inserting biometric");

            //Call Next Item
            logger.info("Users Data Transfer completely Done...");

        } catch (Exception e) {
            logger.error(String.format("Errors encountered State insertion shutting down - %s", e.getMessage()));
            closeStatement(etlStatement);
            e.printStackTrace();
        }
        finally{
            closeStatement(etlStatement);
        }
    }
    private static void closeConnections(Connection lamisPlus, Connection etlApp){
        try{
            if(lamisPlus != null) lamisPlus.close();
            if(etlApp != null) etlApp.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static void closeStatement(Statement statement){
        try{
            if(statement != null) statement.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
