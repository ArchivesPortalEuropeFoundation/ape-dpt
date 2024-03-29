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

import eu.apenet.dpt.standalone.gui.BareBonesBrowserLaunch;
import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.xsdaddition.XsdObject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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
        return retrieve(DBUtil.OptionKeys.OPTION_SAVE_FOLDER.getName(), new File(Utilities.LOG_DIR).getAbsolutePath() + "/"); //Default should be user's home directory???
    }

    public void saveDefaultSaveFolder(String location) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), location, DBUtil.OptionKeys.OPTION_SAVE_FOLDER.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveDefaultXsl() {
        return retrieve(DBUtil.OptionKeys.OPTION_DEFAULT_XSL.getName(), "default-apeEAD.xsl");
    }

    public void saveDefaultXsl(String nameXsl) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), nameXsl, DBUtil.OptionKeys.OPTION_DEFAULT_XSL.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveDefaultXsd() {
        return retrieve(DBUtil.OptionKeys.OPTION_DEFAULT_XSD.getName(), "apeEAD");
    }

    public void saveDefaultXsd(String nameXsd) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), nameXsd, DBUtil.OptionKeys.OPTION_DEFAULT_XSD.getName());
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

    public String retrieveEadRights() {
        return retrieve(DBUtil.OptionKeys.OPTION_EAD_RIGHTS.getName(), "");
    }

    public String retrieveEadRightsDesc(){
        return retrieve(DBUtil.OptionKeys.OPTION_EAD_RIGHTS_DESC.getName(), "");
    }

    public String retrieveEadRightsHolder() {
        return retrieve(DBUtil.OptionKeys.OPTION_EAD_RIGHTS_HOLDER.getName(), "");
    }

    public void saveEadRights(String url, String desc, String holder) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), url, DBUtil.OptionKeys.OPTION_EAD_RIGHTS.getName());
        dbUtil.doSqlQuery(query, null);
        query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), desc, DBUtil.OptionKeys.OPTION_EAD_RIGHTS_DESC.getName());
        dbUtil.doSqlQuery(query, null);
        query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), holder, DBUtil.OptionKeys.OPTION_EAD_RIGHTS_HOLDER.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveDaoRights(){
        return retrieve(DBUtil.OptionKeys.OPTION_DAO_RIGHTS.getName(), "");
    }

    public String retrieveDaoRightsDesc() {
        return retrieve(DBUtil.OptionKeys.OPTION_DAO_RIGHTS_DESC.getName(), "");
    }

    public String retrieveDaoRightsHolder(){
        return retrieve(DBUtil.OptionKeys.OPTION_DAO_RIGHTS_HOLDER.getName(), "");
    }

    public void saveDaoRights(String url, String desc, String holder) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), url, DBUtil.OptionKeys.OPTION_DAO_RIGHTS.getName());
        dbUtil.doSqlQuery(query, null);
        query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), desc, DBUtil.OptionKeys.OPTION_DAO_RIGHTS_DESC.getName());
        dbUtil.doSqlQuery(query, null);
        query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), holder, DBUtil.OptionKeys.OPTION_DAO_RIGHTS_HOLDER.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public List<XsdObject> retrieveAdditionalXsds() {
        ResultSet set = selectAllFromTable(DBUtil.DBNames.TABLE_XSD.getName());
        if(set == null) {
            dbUtil.createXsdTable();
            return new ArrayList<XsdObject>();
        } else {
            List<XsdObject> list = new ArrayList<XsdObject>();
            XsdObject xsdEntry;
            try {
                while (set.next()) {
                    xsdEntry = new XsdObject(set.getInt(DBUtil.DBNames.COLUMN_PRIMARY_ID.getName()), set.getString(DBUtil.DBNames.COLUMN_TITLE.getName()), set.getString(DBUtil.DBNames.COLUMN_VALUE.getName()), set.getInt(DBUtil.DBNames.COLUMN_ISXSD11.getName()), set.getInt(DBUtil.DBNames.COLUMN_ISSYSTEM.getName()), set.getString(DBUtil.DBNames.COLUMN_FILETYPE.getName()));
                    list.add(xsdEntry);
                }
            } catch (SQLException e) {
                LOG.error("Error retrieving the XSDs from the database", e);
            }
            return list;
        }
    }

    public boolean saveNewAdditionalXsd(String name, String location, boolean isSystem, boolean isXsd11, FileInstance.FileType fileType) {
        try {
            String query = DBUtil.createInsertQueryInXsd(DBUtil.DBNames.TABLE_XSD.getName());
            dbUtil.doSqlQuery(query, Arrays.asList(name, location, isSystem?"1":"0", isXsd11?"1":"0", fileType.getFilePrefix()));
            return true;
        } catch (Exception e) {
            LOG.error("Could not save in database a new XSD", e);
            return false;
        }
    }

    public void saveCIdentifierSource(String cIdentifierSource) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), cIdentifierSource, DBUtil.OptionKeys.OPTION_C_IDENTIFIER_SOURCE.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveCIdentifierSource() {
        return retrieve(DBUtil.OptionKeys.OPTION_C_IDENTIFIER_SOURCE.getName(), "unitid");
    }

    public void saveLandingPageBase(String landingPageBase) {
        String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), landingPageBase, DBUtil.OptionKeys.OPTION_LANDING_PAGE_BASE.getName());
        dbUtil.doSqlQuery(query, null);
    }

    public String retrieveLandingPageBase() {
        return retrieve(DBUtil.OptionKeys.OPTION_LANDING_PAGE_BASE.getName(), "http://www.archivesportaleurope.net");
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
            long difference = today.getTime() - lastUpdate.getTime();
            long oneWeeksInMilliseconds = 7 * 24 * 60 * 60 * 1000;
            if((oneWeeksInMilliseconds - difference) < 0){
                LOG.info("We do a check for updates - it has been more than 1 week since the last one.");
                doCheckUpdate = true;
                String query = DBUtil.createUpdateQuery(DBUtil.DBNames.TABLE_OPTIONS.getName(), DBUtil.DBNames.COLUMN_VALUE.getName(), dateFormatYYYYMMDD.format(today), DBUtil.OptionKeys.OPTION_UPDATEDATE.getName());
                dbUtil.doSqlQuery(query, null);
                LOG.info("We just updated the database with the date of today - next check in 1 week.");
            } else {
                LOG.info("We do not do a check for updates");
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        doCheckUpdate = true;
        if(doCheckUpdate){
            try {
                URL url = new URL("https://archivesportaleurope.net/Dashboard/dptVersionApi.action?versionNb=" + versionNb);
//                URL url = new URL("http://localhost:8080/Portal/dptupdate/version?versionNb=" + versionNb);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if(connection.getResponseCode() == 200){
                    String newVersionNb = IOUtils.toString(connection.getInputStream());
                    LOG.info("New version available: " + newVersionNb);
                    if(JOptionPane.showConfirmDialog(contentPane, textNewAvailableVersion) == 0) {
                        BareBonesBrowserLaunch.openURL("https://github.com/ArchivesPortalEuropeFoundation/ape-dpt/releases/tag/DPT-project-" + newVersionNb);
                        System.exit(0);
                    }
                }
                int responseCode = connection.getResponseCode();
            } catch (Exception e) {
                LOG.error("Error to connect for checking the new version (probably no internet connection)", e);
            }
        }
    }

    public ResultSet selectAllFromTable(String dbname) {
        return dbUtil.retrieveSqlResultSet(DBUtil.createSelectAllQuery(dbname));
    }
}
