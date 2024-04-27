package com.matchrate.RISE.etl.utils;

public class Queries {

    public static class DBLinks {

        public static String createLamiplusDbLink(String hostname, String port, String user, String password, String dbname, String hostAddr) {
            return String.format("DO $$BEGIN IF NOT EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'dblink') THEN CREATE EXTENSION dblink; END IF; END$$;\n" +
                    "        SELECT dblink_connect('host=%s user= %s password=%s dbname=%s');\n" +
                    "        DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_foreign_data_wrapper WHERE fdwname = 'postgres') THEN CREATE FOREIGN DATA WRAPPER postgres VALIDATOR postgresql_fdw_validator; END IF; END $$;\n" +
                    "        DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_foreign_server WHERE srvname = 'etlapp') THEN CREATE SERVER etlapp FOREIGN DATA WRAPPER postgres OPTIONS (hostaddr '%s', dbname '%s'); END IF; END $$;\n" +
                    "        DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_user_mappings WHERE usename = 'postgres' AND srvname = 'etlapp' ) THEN CREATE USER MAPPING FOR postgres SERVER etlapp OPTIONS (user '%s', password '%s'); END IF; END $$;\n" +
                    "        SELECT dblink_connect('etlapp');",hostname, user, password, dbname, hostAddr, dbname, user, password);
        }


        public static String createetlDblink (String hostname, String port, String user, String password, String dbname, String hostAddr){

            return  String.format("DO $$BEGIN IF NOT EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'dblink') THEN CREATE EXTENSION dblink; END IF; END$$;\n" +
                    "        SELECT dblink_connect('host=%s user= %s password=%s dbname=%s');\n" +
                    "        DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_foreign_data_wrapper WHERE fdwname = 'postgres') THEN CREATE FOREIGN DATA WRAPPER postgres VALIDATOR postgresql_fdw_validator; END IF; END $$;\n" +
                    "        DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_foreign_server WHERE srvname = 'etlapp') THEN CREATE SERVER etlapp FOREIGN DATA WRAPPER postgres OPTIONS (hostaddr '%s', dbname '%s'); END IF; END $$;" +
                    "        DO $$ BEGIN IF NOT EXISTS (SELECT 1 FROM pg_user_mappings WHERE usename = 'postgres' AND srvname = 'etlapp' ) THEN CREATE USER MAPPING FOR postgres SERVER etlapp OPTIONS (user '%s', password '%s'); END IF;END $$;\n"+
                    "        SELECT dblink_connect('etlapp');", hostname, user, password, dbname, hostAddr, dbname, user, password);
        }
    }
    public static class biometric {

        public static final String  biometric_QUERY = "INSERT INTO target_table (facility_id, person_uuid, biometric_id, baseline, enrollment_date, template_type, archived, recapture, replacement_date)\n" +
                "SELECT * FROM dblink('etlapp', '\n" +
                "SELECT facility_id::Bigint, person_uuid::UUID as person_uuid, biometric_id::UUID as id, baseline::bytea as template, enrollment_date::Date AS enrollment_date ,  template_type::Varchar(255) AS template_type, archived:: Bigint AS archived, recapture::Bigint AS recapture, repcacement_date::Date AS replace_date,\n" +
                "facility_id BIGINT,\n" +
                "Person_uuid uuid,\n" +
                "id uuid,\n" +
                "template  bytea,\n" +
                "enrollment_date DATE,\n" +
                "template_type VARCHAR(255),\n" +
                "archived BIGINT,\n" +
                "recapture BIGINT,\n" +
                "replace_date DATE,\n" ;
    }
}
