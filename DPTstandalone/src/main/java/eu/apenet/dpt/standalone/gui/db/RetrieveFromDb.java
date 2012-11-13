package eu.apenet.dpt.standalone.gui.db;

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
        String result = retrieve(DBUtil.OptionKeys.OPTION_CHECKS_LOADING_FILES.getName());
        if(result == null) {
            String query = DBUtil.createInsertQuery(DBUtil.DBNames.TABLE_OPTIONS.getName());
            dbUtil.doSqlQuery(query, Arrays.asList(DBUtil.OptionKeys.OPTION_CHECKS_LOADING_FILES.getName(), "NO"));
            return "NO";
        }
        return result;
    }

    public void saveLoadingChecks(String value) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), value, DBUtil.OptionKeys.OPTION_CHECKS_LOADING_FILES.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveRepositoryCode() {
        return retrieve(DBUtil.OptionKeys.OPTION_GLOBALID.getName());
    }

    public void saveRepositoryCode(String value) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), value, DBUtil.OptionKeys.OPTION_GLOBALID.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveCountryCode() {
        return retrieve(DBUtil.OptionKeys.OPTION_COUNTRYCODE.getName());
    }

    public void saveCountryCode(String value) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), value, DBUtil.OptionKeys.OPTION_COUNTRYCODE.getName());
        dbUtil.doSqlQuery(query, null);
    }

    private String retrieve(String optionKey) {
        String query = DBUtil.createSelectQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), optionKey);
        String[] res = dbUtil.retrieveSqlListResult(query, DBUtil.DBNames.COLUMN_VALUE);
        if(res.length > 0){
            return res[0];
        }
        return null;
    }
}
