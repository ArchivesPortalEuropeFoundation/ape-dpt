package eu.apenet.dpt.standalone.gui.eagcreation;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: Nov 19, 2010
 *
 * @author Yoann Moranville
 */
public class MyTableModel extends AbstractTableModel {

    public MyTableModel(ResourceBundle labels, String mainagencycode){
        rowData = new Object[][]{
            {labels.getString("eagName"), "", "name"},
            {labels.getString("eagEnglishName"), "", "english_name"},
            {labels.getString("eagId"), mainagencycode, "eagid"},
            {labels.getString("eagCountry"), "", "country"},
            {labels.getString("eagTown"), "", "city"},
            {labels.getString("eagPostalCode"), "", "postalcode"},
            {labels.getString("eagStreet"), "", "street"},
            {labels.getString("eagPhone"), "", "telephone"},
            {labels.getString("eagEmail"), "", "email"},
            {labels.getString("eagWebpage"), "", "webpage"}
        };
    }

    private Object[] columnNames = {"Name", "Value"};
    private Object[][] rowData;

    public String getColumnName(int col) {
        return columnNames[col].toString();
    }
    public int getRowCount() {
        return rowData.length;
    }
    public int getColumnCount() {
        return columnNames.length;
    }
    public Object getValueAt(int row, int col) {
        return rowData[row][col];
    }
    public boolean isCellEditable(int row, int col){
        return col != 0;
    }
    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    public void eraseAllData(){
        for(int i = 0; i < rowData.length; i++)
            setValueAt("", i, 1);
    }
    public HashMap<String, String> getParamsEag() {
        HashMap<String, String> params = new HashMap<String, String>();
        for (Object[] aRowData : rowData){
            if(aRowData[1] == null || "".equals(aRowData[1]))
                return null;
            params.put((String) aRowData[2], (String) aRowData[1]);
        }
        return params;
    }
}
