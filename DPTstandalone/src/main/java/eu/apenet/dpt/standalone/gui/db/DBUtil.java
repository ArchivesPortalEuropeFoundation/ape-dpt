package eu.apenet.dpt.standalone.gui.db;

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
        COLUMN_TITLE("TITLE"),
        COLUMN_ID("IDENTIFIER"),
        COLUMN_VALUE("VALUE");

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
        OPTION_UPDATEDATE("lastUpdateCheck"),
        OPTION_CHECKS_LOADING_FILES("checksLoadingFiles");

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
                "CREATE TABLE ape_options (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), MYKEY VARCHAR(30), VALUE VARCHAR(30))",
                "CREATE TABLE hg_titles (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), TITLE VARCHAR(64))",
                "CREATE TABLE hg_ids (ID INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), IDENTIFIER VARCHAR(64))"
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

    public static String createSelectQuery(String tableName, String keyName){
        return "SELECT * FROM " + tableName + " WHERE MYKEY='" + keyName + "'";
    }
    public static String createUpdateQuery(String tableName, String columnName, String value, String keyName){
        return "UPDATE " + tableName + " SET " + columnName + "='" + value + "' WHERE MYKEY='" + keyName + "'";
    }
    public static String createInsertQuery(String tableName){
        return "INSERT INTO " + tableName + " (MYKEY, VALUE) VALUES (?, ?)";
    }

    public void saveOrUpdateRoleType(String roleType, boolean useExistingValue) {
        String query = createUpdateQuery(DBNames.TABLE_OPTIONS.getName(), DBNames.COLUMN_VALUE.getName(), roleType, OptionKeys.OPTION_ROLETYPE.getName());
        doSqlQuery(query, null);
        query = createUpdateQuery(DBNames.TABLE_OPTIONS.getName(), DBNames.COLUMN_VALUE.getName(), Boolean.toString(useExistingValue), OptionKeys.OPTION_USE_EXISTING_ROLETYPE.getName());
        doSqlQuery(query, null);
    }

    public String retrieveRoleType(){
        String query = createSelectQuery(DBNames.TABLE_OPTIONS.getName(), OptionKeys.OPTION_ROLETYPE.getName());
        String[] res = retrieveSqlListResult(query, DBNames.COLUMN_VALUE);
        if(res.length > 0)
            return res[0];
        query = createInsertQuery(DBNames.TABLE_OPTIONS.getName());
        doSqlQuery(query, Arrays.asList(DBUtil.OptionKeys.OPTION_ROLETYPE.getName(), "UNSPECIFIED"));
        return "UNSPECIFIED";
    }

    public Boolean retrieveUseExistingRoleType(){
        String query = createSelectQuery(DBNames.TABLE_OPTIONS.getName(), OptionKeys.OPTION_USE_EXISTING_ROLETYPE.getName());
        String[] res = retrieveSqlListResult(query, DBNames.COLUMN_VALUE);
        if(res.length > 0)
            return Boolean.parseBoolean(res[0]);
        query = createInsertQuery(DBNames.TABLE_OPTIONS.getName());
        doSqlQuery(query, Arrays.asList(OptionKeys.OPTION_USE_EXISTING_ROLETYPE.getName(), "true"));
        return true;
    }
}
