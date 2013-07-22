package eu.apenet.dpt.standalone.gui.adhoc;

import eu.apenet.dpt.standalone.gui.db.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: Dec 1, 2010
 *
 * @author Yoann Moranville
 */
public class QueryComponent {
    private JPanel panel = new JPanel();
    private JComboBox boxTitle = new JComboBox();
    private JComboBox boxId = new JComboBox();
    private String[] titles;
    private String[] ids;
    private DBUtil dbUtil;

    public QueryComponent(DBUtil dbUtil, String title, String id, String tooltip){
        this.dbUtil = dbUtil;
        //titles = {"Holdings Guide from Poland", "HG from PL"};
        titles = this.dbUtil.retrieveSqlListResult("SELECT * FROM " + DBUtil.DBNames.TABLE_TITLES.getName(), DBUtil.DBNames.COLUMN_TITLE);
        boxTitle.setModel(new DefaultComboBoxModel(titles));
        boxTitle.setEditable(true);
        //ids = {"PL35", "PL53"};
        ids = this.dbUtil.retrieveSqlListResult("SELECT * FROM " + DBUtil.DBNames.TABLE_IDS.getName(), DBUtil.DBNames.COLUMN_ID);
        boxId.setModel(new DefaultComboBoxModel(ids));
        boxId.setEditable(true);
        panel.setLayout(new GridLayout(2,2,2,5));
        panel.add(new JLabel(title));
        panel.add(boxTitle);
        JLabel label = new JLabel(id);
        label.setToolTipText(tooltip);
        panel.add(label);
        panel.add(boxId);
    }

    public String getEntryTitle(){
        String entry = (String) boxTitle.getSelectedItem();
        if(!Arrays.asList(titles).contains(entry)){
            String q = "INSERT INTO " + DBUtil.DBNames.TABLE_TITLES.getName() + " (" + DBUtil.DBNames.COLUMN_TITLE.getName() + ") VALUES (?)";
            dbUtil.doSqlQuery(q, Arrays.asList(entry));
        }
        return entry;
    }

    public String getEntryId(){
        String entry = (String)boxId.getSelectedItem();
        if(!Arrays.asList(ids).contains(entry)){
            String q = "INSERT INTO " + DBUtil.DBNames.TABLE_IDS.getName() + " (" + DBUtil.DBNames.COLUMN_ID.getName() + ") VALUES (?)";
            dbUtil.doSqlQuery(q, Arrays.asList(entry));
        }
        return entry;
    }

    public JComponent getMainPanel(){
        return panel;
    }
}