package eu.apenet.dpt.standalone.gui.db;

import eu.apenet.dpt.standalone.gui.BareBonesBrowserLaunch;
import eu.apenet.dpt.standalone.gui.Utilities;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * User: Yoann Moranville
 * Date: 09/11/2012
 *
 * @author Yoann Moranville
 */
public class RetrieveFromDb {
    private static final Logger LOG = Logger.getLogger(RetrieveFromDb.class);

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

    public void saveOrUpdateRoleType(String roleType, boolean useExistingValue) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), roleType, DBUtil.OptionKeys.OPTION_ROLETYPE.getName());
        dbUtil.doSqlQuery(query, null);
        query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), Boolean.toString(useExistingValue), DBUtil.OptionKeys.OPTION_USE_EXISTING_ROLETYPE.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveRoleType() {
        return retrieve(DBUtil.OptionKeys.OPTION_ROLETYPE.getName(), "UNSPECIFIED");
    }

    public Boolean retrieveUseExistingRoleType(){
        return Boolean.parseBoolean(retrieve(DBUtil.OptionKeys.OPTION_USE_EXISTING_ROLETYPE.getName(), "true"));
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

    public void checkForUpdates(Container contentPane, String textNewAvailableVersion, String versionNb){
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        String date = dateFormatYYYYMMDD.format(today);
        date = retrieve(DBUtil.OptionKeys.OPTION_UPDATEDATE.getName(), date);

        boolean doCheckUpdate = false;
        try {
            Date lastUpdate = dateFormatYYYYMMDD.parse(date);
            //Milliseconds between both dates
            long difference = today.getTime() - lastUpdate.getTime();

            long twoWeeksInMilliseconds = 14 * 24 * 60 * 60 * 1000;

            if((twoWeeksInMilliseconds - difference) < 0){
                LOG.info("We do a check for updates - it has been more than 2 weeks since the last one.");
                doCheckUpdate = true;
                String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), dateFormatYYYYMMDD.format(today), DBUtil.OptionKeys.OPTION_UPDATEDATE.getName());
                dbUtil.doSqlQuery(query, null);
                LOG.info("We just updated the database with the date of today - next check in 2 weeks.");
            } else {
                LOG.info("We do not do a check for updates");
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        if(doCheckUpdate){
            try {
                URL url = new URL("http://www.archivesportaleurope.eu/Portal/dptupate/version.action?versionNb=" + versionNb);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if(connection.getResponseCode() == 200){
                    //There is a new version
                    LOG.info("New version available...");
                    if(JOptionPane.showConfirmDialog(contentPane, textNewAvailableVersion) == 0){
                        BareBonesBrowserLaunch.openURL("http://www.apenet.eu/index.php?option=com_content&view=article&id=94&Itemid=150&lang=en");
                        System.exit(0);
                    }
                }
//                LOG.info(connection.getResponseCode());
            } catch (Exception e) {
                LOG.error("Error to connect for checking the new version (probably no internet connection)", e);
            }
        }
    }
}
