package eu.apenet.dpt.standalone.gui.databasechecker;

import eu.apenet.dpt.standalone.gui.xsdAddition.XsdObject;
import eu.apenet.dpt.standalone.gui.db.DBUtil;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: Yoann Moranville
 * Date: 07/02/2013
 *
 * @author Yoann Moranville
 */
public class DatabaseCheckerFrame extends JFrame {
    private static final Logger LOG = Logger.getLogger(DatabaseCheckerFrame.class);
    private JList listOfTables;
    private JTable table;

    private RetrieveFromDb retrieveFromDb;

    public DatabaseCheckerFrame(RetrieveFromDb retrieveFromDb) {
        this.retrieveFromDb = retrieveFromDb;
        listOfTables = createListOfTables();
        listOfTables.setDragEnabled(false);
        createListeners();
        Object[] columnNames = createColumns();
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        createFrame();
    }

    private JList createListOfTables() {
        DefaultListModel model = new DefaultListModel();
        model.addElement(DBUtil.DBNames.TABLE_TITLES.getName());
        model.addElement(DBUtil.DBNames.TABLE_OPTIONS.getName());
        model.addElement(DBUtil.DBNames.TABLE_IDS.getName());
        model.addElement(DBUtil.DBNames.TABLE_XSD.getName());
        return new JList(model);
    }

    private Object[] createColumns() {
        return new Object[]{DBUtil.DBNames.COLUMN_PRIMARY_ID, DBUtil.DBNames.COLUMN_TITLE, DBUtil.DBNames.COLUMN_ID, DBUtil.DBNames.COLUMN_MYKEY, DBUtil.DBNames.COLUMN_VALUE, DBUtil.DBNames.COLUMN_ISSYSTEM, DBUtil.DBNames.COLUMN_ISXSD11, DBUtil.DBNames.COLUMN_FILETYPE};
    }

    private void createListeners() {
        listOfTables.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                createRows((String) listOfTables.getSelectedValue());
            }
        });
    }

    private void createRows(String valueTable) {
        ((DefaultTableModel)(table.getModel())).setNumRows(0);
        ResultSet set = retrieveFromDb.selectAllFromTable(valueTable);
        try {
            while(set.next()) {
                Object[] row;
                if(valueTable.equals(DBUtil.DBNames.TABLE_IDS.getName())) {
                    row = new Object[]{set.getInt(DBUtil.DBNames.COLUMN_PRIMARY_ID.getName()), "", set.getInt(DBUtil.DBNames.COLUMN_ID.getName()), "", "", "", "", ""};
                } else if(valueTable.equals(DBUtil.DBNames.TABLE_OPTIONS.getName())) {
                    row = new Object[]{set.getInt(DBUtil.DBNames.COLUMN_PRIMARY_ID.getName()), set.getString(DBUtil.DBNames.COLUMN_MYKEY.getName()), set.getString(DBUtil.DBNames.COLUMN_VALUE.getName()), "", "", ""};
                } else if(valueTable.equals(DBUtil.DBNames.TABLE_XSD.getName())) {
                    XsdObject xsd = new XsdObject(set.getInt(DBUtil.DBNames.COLUMN_PRIMARY_ID.getName()), set.getString(DBUtil.DBNames.COLUMN_TITLE.getName()), set.getString(DBUtil.DBNames.COLUMN_VALUE.getName()), set.getInt(DBUtil.DBNames.COLUMN_ISXSD11.getName()), set.getInt(DBUtil.DBNames.COLUMN_ISSYSTEM.getName()), set.getString(DBUtil.DBNames.COLUMN_FILETYPE.getName()));
                    row = new Object[]{xsd.getId(), xsd.getName(), "", "", xsd.getPath(), xsd.isSystem(), xsd.isXsd11(), (xsd.getFileType()!=null)?xsd.getFileType().getFilePrefix():""};
                } else {
                    row = new Object[]{set.getInt(DBUtil.DBNames.COLUMN_PRIMARY_ID.getName()), set.getString(DBUtil.DBNames.COLUMN_TITLE.getName()), "", "", "", "", "", ""};
                }
                ((DefaultTableModel)(table.getModel())).addRow(row);
            }
        } catch (SQLException e) {
            LOG.error("Error", e);
        }
    }

    private void createFrame() {
        setTitle("Database Checker");

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(new JScrollPane(listOfTables), BorderLayout.WEST);
        pane.add(new JScrollPane(table), BorderLayout.CENTER);
        add(pane);
    }
}
