package com.matchrate.RISE.etl.utils;

public class Queries {

    public static class DBLinks {

        public static String createLamiplusDbLink(String hostname, String port, String user, String password, String dbname, String hostAddr) {
            return String.format("DO $$BEGIN IF NOT EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'dblink') THEN CREATE EXTENSION dblink; END IF; END$$;\n" +
                    "SELECT dblink_connect('host=%s user=%s password=%s dbname=%s');\n" +
                    "DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_foreign_data_wrapper WHERE fdwname = 'postgres') THEN CREATE FOREIGN DATA WRAPPER postgres VALIDATOR postgresql_fdw_validator; END IF; END $$;\n" +
                    "DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_foreign_server WHERE srvname = 'etlapp') THEN CREATE SERVER etlapp FOREIGN DATA WRAPPER postgres OPTIONS (host '%s', dbname '%s'); END IF; END $$;\n" +
                    "DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_user_mappings WHERE usename = 'postgres' AND srvname = 'etlapp' ) THEN CREATE USER MAPPING FOR postgres SERVER etlapp OPTIONS (user '%s', password '%s'); END IF; END $$;\n" +
                    "SELECT dblink_connect('etlapp');", hostname, user, password, dbname, hostAddr, dbname, user, password);
        }


        public static String createEtlDblink(String hostname, String port, String user, String password, String dbname, String hostAddr) {

            return String.format("DO $$BEGIN IF NOT EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'dblink') THEN CREATE EXTENSION dblink; END IF; END$$;\n" +
                    "        SELECT dblink_connect('host=%s user= %s password=%s dbname=%s');\n" +
                    "        DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_foreign_data_wrapper WHERE fdwname = 'postgres') THEN CREATE FOREIGN DATA WRAPPER postgres VALIDATOR postgresql_fdw_validator; END IF; END $$;\n" +
                    "        DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_foreign_server WHERE srvname = 'etlapp') THEN CREATE SERVER etlapp FOREIGN DATA WRAPPER postgres OPTIONS (hostaddr '%s', dbname '%s'); END IF; END $$;" +
                    "        DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_user_mappings WHERE usename = 'postgres' AND srvname = 'etlapp' ) THEN CREATE USER MAPPING FOR postgres SERVER etlapp OPTIONS (user '%s', password '%s'); END IF;END $$;\n" +
                    "        SELECT dblink_connect('etlapp');", hostname, user, password, dbname, hostAddr, dbname, user, password);
        }
    }


    public static class biometric {


//            public static final String biometric_QUERY = "INSERT INTO target_table (facility_id, person_uuid, biometric_id, biometric_data, enrollment_date, template_type, archived, recapture, replacement_date)\n" +
//            "SELECT * FROM dblink('etlapp', '\n" +
//            "SELECT facility_id::Bigint, person_uuid::UUID, id::UUID, template::bytea, enrollment_date::Date, template_type::Varchar(255), archived:: Bigint, recapture::Bigint, replace_date::Date\n" +
//            "FROM biometric\n" + // Replace with the actual name of the remote table
//            "')\n" +
//            "AS biometric(facility_id BIGINT, person_uuid UUID, id UUID, template BYTEA, enrollment_date DATE, template_type VARCHAR(255), archived BIGINT, recapture BIGINT, replace_date DATE);";
//

        public static final String biometric_QUERY = "INSERT INTO target_table (facility_id, person_uuid, biometric_id, biometric_data, enrollment_date, template_type, archived, recapture, replacement_date)\n" +
                "SELECT facility_id::Bigint, person_uuid::UUID, id::UUID, template::bytea, enrollment_date::Date, template_type::Varchar(255), archived:: Bigint, recapture::Bigint, replace_date::Date\n" +
                "FROM (\n" +
                "  SELECT *\n" +
                "  FROM dblink('etlapp', '\n" +
                "    SELECT facility_id, person_uuid, id, template, enrollment_date, template_type, archived, recapture, replace_date\n" +
                "    FROM biometric\n" + // Replace with the actual name of the remote table
                "    WHERE person_uuid ~ ''^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$''\n" +
                "      AND id ~ ''^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$''\n" +
                "  ')\n" +
                "  AS biometric(facility_id BIGINT, person_uuid UUID, id UUID, template BYTEA, enrollment_date DATE, template_type VARCHAR(255), archived BIGINT, recapture BIGINT, replace_date DATE)\n" +
                ") AS valid_biometrics;";
    }
}






