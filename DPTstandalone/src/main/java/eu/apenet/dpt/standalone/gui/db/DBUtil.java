package eu.apenet.dpt.standalone.gui.db;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: Aug 17, 2010
 *
 * @author Yoann Moranville
 */
public class DBUtil {
    private Connection dbConnection;
    private final static Logger LOG = Logger.getLogger(DBUtil.class);

    public enum DBNames{
        TABLE_OPTIONS("ape_options"),
        TABLE_TITLES("hg_titles"),
        TABLE_IDS("hg_ids"),
        TABLE_XSD("xsd_addition"),
        COLUMN_TITLE("TITLE"),
        COLUMN_ID("IDENTIFIER"),
        COLUMN_VALUE("VALUE"),
        COLUMN_PRIMARY_ID("ID"),
        COLUMN_MYKEY("MYKEY"),
        COLUMN_ISSYSTEM("ISSYSTEM"),
        COLUMN_ISXSD11("ISXSD11"),
        COLUMN_FILETYPE("FILETYPE");

        private String name;
        DBNames(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    public enum OptionKeys {
        OPTION_GLOBALID("globalId"),
        OPTION_COUNTRYCODE("countrycode"),
        OPTION_ROLETYPE("roleType"),
        OPTION_USE_EXISTING_ROLETYPE("useExistingRoleType"),
        OPTION_EAD_RIGHTS("eadRights"),
        OPTION_EAD_RIGHTS_DESC("eadRightsDesc"),
        OPTION_EAD_RIGHTS_HOLDER("eadRightsHolder"),
        OPTION_DAO_RIGHTS("daoRights"),
        OPTION_DAO_RIGHTS_DESC("daoRightsDesc"),
        OPTION_DAO_RIGHTS_HOLDER("daoRightsHolder"),
        OPTION_UPDATEDATE("lastUpdateCheck"),
        OPTION_CHECKS_LOADING_FILES("checksLoadingFiles"),
        OPTION_OPEN_LOCATION("openLocation"),
        OPTION_SAVE_FOLDER("savedFolder"),
        OPTION_DEFAULT_XSL("defaultXsl"),
        OPTION_DEFAULT_XSD("defaultXsd"),
        OPTION_C_IDENTIFIER_SOURCE("cIdentifierSource"),
        OPTION_LANDING_PAGE_BASE("landingPageBase");

        private String name;
        OptionKeys(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    public DBUtil(){
        String dbPath = "jdbc:derby:apeoptions";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();

            System.setProperty("derby.system.home", "xsl");
            dbConnection = DriverManager.getConnection(dbPath);
//            dropTable(DBNames.TABLE_XSD.getName());
//            createTables();
        } catch (Exception e){
            try{
                dbPath += ";create=true";
                dbConnection = DriverManager.getConnection(dbPath);
                createTables();
//                createExamples();
            } catch (SQLException ex) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createTables(){
        LOG.info("Creating the database tables, because it is the first launch of the tool.");
        List<String> queries = Arrays.asList(
                "CREATE TABLE ape_options (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), MYKEY VARCHAR(30), VALUE VARCHAR(32672))",
                "CREATE TABLE hg_titles (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), TITLE VARCHAR(64))",
                "CREATE TABLE hg_ids (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), IDENTIFIER VARCHAR(64))",
                "CREATE TABLE xsd_addition (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), TITLE VARCHAR(64), VALUE VARCHAR(256), ISSYSTEM INTEGER, ISXSD11 INTEGER, FILETYPE VARCHAR(64))"
        );
        Statement statement;
        try{
            for(String createTableQuery : queries) {
                statement = dbConnection.createStatement();
                statement.execute(createTableQuery);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createXsdTable() {
        String query = "CREATE TABLE xsd_addition (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), TITLE VARCHAR(64), VALUE VARCHAR(256), ISSYSTEM INTEGER, ISXSD11 INTEGER, FILETYPE VARCHAR(64))";
        Statement statement;
        try{
         statement = dbConnection.createStatement();
         statement.execute(query);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void dropTable(String tableName){
        String query = "DROP TABLE " + tableName;
        try {
            Statement statement = dbConnection.createStatement();
            statement.execute(query);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void resetTables(){
        List<String> queries = Arrays.asList(
                "DROP TABLE " + DBNames.TABLE_OPTIONS.getName(),
                "DROP TABLE " + DBNames.TABLE_TITLES,
                "DROP TABLE " + DBNames.TABLE_IDS
        );
        Statement statement;
        try{
            for(String createTableQuery : queries) {
                statement = dbConnection.createStatement();
                statement.execute(createTableQuery);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void createExamples(){
        String q = "INSERT INTO ape_options (MYKEY, VALUE) VALUES (?, ?)";
        doSqlQuery(q, Arrays.asList("GlobalId", "FR_DAF"));

        q = "INSERT INTO hg_titles (TITLE) VALUES (?)";
        doSqlQuery(q, Arrays.asList("Holdings Guide from Poland"));

        q = "INSERT INTO hg_titles (TITLE) VALUES (?)";
        doSqlQuery(q, Arrays.asList("HG from PL"));

        q = "INSERT INTO hg_ids (IDENTIFIER) VALUES (?)";
        doSqlQuery(q, Arrays.asList("PL-35"));
    }

    public int doSqlQuery(String query, List<String> params){
        PreparedStatement statement;
        try{
            statement = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.clearParameters();
            if(params != null)
                for(int i = 0; i < params.size(); i++)
                    statement.setString(i+1, params.get(i));
            statement.executeUpdate();
            ResultSet results = statement.getGeneratedKeys();
            if(results != null && results.next())
                return results.getInt(1);
        } catch (SQLException e){
            LOG.error("Error when processing the query: " + query, e);
        }
        return -1;
    }

    public String[] retrieveSqlListResult(String query, DBNames columnName) {
        List<String> resList = new ArrayList<String>();
        Statement statement;
        try {
            statement = dbConnection.createStatement();
            ResultSet results = statement.executeQuery(query);
            while(results.next())
                resList.add(results.getString(columnName.getName()));
            return resList.toArray(new String[]{});
        } catch (SQLException e) {
            LOG.error(e);
        }
        return null;
    }

    public ResultSet retrieveSqlResultSet(String query) {
        Statement statement;
        try {
            statement = dbConnection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            LOG.error(e);
        }
        return null;
    }

    public static String createSelectQuery(String tableName, String keyName){
        return "SELECT * FROM " + tableName + " WHERE MYKEY='" + keyName + "'";
    }
    public static String createUpdateQuery(String tableName, String columnName, String value, String keyName){
        return "UPDATE " + tableName + " SET " + columnName + "='" + value + "' WHERE MYKEY='" + keyName + "'";
    }
    public static String createInsertQuery(String tableName){
        return "INSERT INTO " + tableName + " (MYKEY, VALUE) VALUES (?, ?)";
    }
    public static String createInsertQueryInXsd(String tableName){
        return "INSERT INTO " + tableName + " (TITLE, VALUE, ISSYSTEM, ISXSD11, FILETYPE) VALUES (?, ?, ?, ?, ?)";
    }
    public static String createSelectAllQuery(String tableName){
        return "SELECT * FROM " + tableName;
    }
}
