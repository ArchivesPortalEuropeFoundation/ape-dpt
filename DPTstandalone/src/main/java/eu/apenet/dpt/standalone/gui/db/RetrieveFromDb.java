package eu.apenet.dpt.standalone.gui.db;

import eu.apenet.dpt.standalone.gui.Utilities;

import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: 09/11/2012
 *
 * @author Yoann Moranville
 */
public class RetrieveFromDb {

    private DBUtil dbUtil;

    public RetrieveFromDb() {
        dbUtil = new DBUtil();
    }

    public String retrieveCurrentLoadingChecks() {
        return retrieve(DBUtil.OptionKeys.OPTION_CHECKS_LOADING_FILES.getName(), "NO");
    }

    public void saveLoadingChecks(String value) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), value, DBUtil.OptionKeys.OPTION_CHECKS_LOADING_FILES.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveRepositoryCode() {
        return retrieve(DBUtil.OptionKeys.OPTION_GLOBALID.getName(), null);
    }

    public void saveRepositoryCode(String value) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), value, DBUtil.OptionKeys.OPTION_GLOBALID.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveCountryCode() {
        return retrieve(DBUtil.OptionKeys.OPTION_COUNTRYCODE.getName(), null);
    }

    public void saveCountryCode(String value) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), value, DBUtil.OptionKeys.OPTION_COUNTRYCODE.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveOpenLocation() {
        return retrieve(DBUtil.OptionKeys.OPTION_OPEN_LOCATION.getName(), System.getProperty("user.home"));
    }

    public void saveOpenLocation(String location) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), location, DBUtil.OptionKeys.OPTION_OPEN_LOCATION.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveDefaultSaveFolder() {
        return retrieve(DBUtil.OptionKeys.OPTION_SAVE_FOLDER.getName(), Utilities.LOG_DIR); //Default should be user's home directory???
    }

    public void saveDefaultSaveFolder(String location) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), location, DBUtil.OptionKeys.OPTION_SAVE_FOLDER.getName());
        dbUtil.doSqlQuery(query, null);
    }

    private String retrieve(String optionKey, String defaultValue) {
        String query = DBUtil.createSelectQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), optionKey);
        String[] res = dbUtil.retrieveSqlListResult(query, DBUtil.DBNames.COLUMN_VALUE);
        if(res.length > 0) {
            return res[0];
        } else {
            query = DBUtil.createInsertQuery(DBUtil.DBNames.TABLE_OPTIONS.getName());
            dbUtil.doSqlQuery(query, Arrays.asList(optionKey, defaultValue));
            return defaultValue;
        }
    }
}
