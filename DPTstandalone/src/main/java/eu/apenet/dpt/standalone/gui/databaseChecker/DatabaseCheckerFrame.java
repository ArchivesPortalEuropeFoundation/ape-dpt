package eu.apenet.dpt.standalone.gui.databaseChecker;

import eu.apenet.dpt.standalone.gui.db.DBUtil;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    private JPanel forTable;
    private RetrieveFromDb retrieveFromDb;
    private DefaultTableModel model;
    private DefaultTableColumnModel tableColumnModel;

    public DatabaseCheckerFrame(RetrieveFromDb retrieveFromDb) {
        this.retrieveFromDb = retrieveFromDb;
        listOfTables = createListOfTables();
        listOfTables.setDragEnabled(false);
        createListeners();
        model = new DefaultTableModel(new Object[]{}, 0);
        tableColumnModel = new DefaultTableColumnModel();
        table = new JTable(model, tableColumnModel);
        table.setAutoCreateColumnsFromModel(false);
        createFrame();
        createTable(new Object[]{});
    }

    private void createTable(Object[] columnNames) {
        for(int i = 0; i < tableColumnModel.getColumnCount(); i++) {
            tableColumnModel.removeColumn(tableColumnModel.getColumn(i));
        }
        for(Object column : columnNames) {
            LOG.info(column);
            TableColumn tableColumn = new TableColumn();
            tableColumn.setHeaderValue(column);
            model.addColumn(tableColumn);
        }
    }

    private JList createListOfTables() {
        DefaultListModel model = new DefaultListModel();
         model.addElement(DBUtil.DBNames.TABLE_TITLES.getName());
         model.addElement(DBUtil.DBNames.TABLE_OPTIONS.getName());
         model.addElement(DBUtil.DBNames.TABLE_IDS.getName());
         model.addElement(DBUtil.DBNames.TABLE_XSD.getName());
        return new JList(model);
    }

    private void createListeners() {
        listOfTables.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
//                    SwingUtilities.invokeLater(new Runnable(){public void run(){
                    fillTable((String) listOfTables.getSelectedValue());
                    forTable.revalidate();
//                    }});
                }
            }
        });
    }

    private void fillTable(String valueTable) {
        LOG.info("1: " + valueTable);
        ResultSet set = retrieveFromDb.selectAllFromTable(valueTable);
        try {
            Object[] column = new Object[]{};
            List<Object[]> rows = new ArrayList<Object[]>();
            while(set.next()) {
                Object[] row;

                if(valueTable.equals(DBUtil.DBNames.TABLE_IDS.getName())) {
                    row = new Object[]{set.getInt(DBUtil.DBNames.COLUMN_PRIMARY_ID.getName()), set.getInt(DBUtil.DBNames.COLUMN_ID.getName())};
                    column = new Object[]{DBUtil.DBNames.COLUMN_PRIMARY_ID, DBUtil.DBNames.COLUMN_ID};
                } else if(valueTable.equals(DBUtil.DBNames.TABLE_OPTIONS.getName())) {
                    row = new Object[]{set.getInt(DBUtil.DBNames.COLUMN_PRIMARY_ID.getName()), set.getString(DBUtil.DBNames.COLUMN_MYKEY.getName()), set.getString(DBUtil.DBNames.COLUMN_VALUE.getName())};
                    column = new Object[]{DBUtil.DBNames.COLUMN_PRIMARY_ID, DBUtil.DBNames.COLUMN_MYKEY, DBUtil.DBNames.COLUMN_VALUE};
                } else if(valueTable.equals(DBUtil.DBNames.TABLE_XSD.getName())) {
                    row = new Object[]{set.getInt(DBUtil.DBNames.COLUMN_PRIMARY_ID.getName()), set.getString(DBUtil.DBNames.COLUMN_TITLE.getName()), set.getString(DBUtil.DBNames.COLUMN_VALUE.getName())};
                    column = new Object[]{DBUtil.DBNames.COLUMN_PRIMARY_ID, DBUtil.DBNames.COLUMN_TITLE, DBUtil.DBNames.COLUMN_VALUE, DBUtil.DBNames.COLUMN_ISSYSTEM, DBUtil.DBNames.COLUMN_ISXSD11, DBUtil.DBNames.COLUMN_FILETYPE};
                } else {
                    row = new Object[]{set.getInt(DBUtil.DBNames.COLUMN_PRIMARY_ID.getName()), set.getString(DBUtil.DBNames.COLUMN_TITLE.getName())};
                    column = new Object[]{DBUtil.DBNames.COLUMN_PRIMARY_ID, DBUtil.DBNames.COLUMN_TITLE};
                }
                rows.add(row);
            }
            createTable(column);

            for(Object[] row : rows)
                ((DefaultTableModel)(table.getModel())).addRow(row);
        } catch (SQLException e) {
            LOG.error("Error", e);
        }
    }

    private void createFrame() {
        setTitle("Database Checker");

        JPanel pane = new JPanel(new BorderLayout());
        pane.add(new JScrollPane(listOfTables), BorderLayout.WEST);

        forTable = new JPanel();
        forTable.add(new JScrollPane(table));

        pane.add(forTable);
        add(pane);
    }
}
