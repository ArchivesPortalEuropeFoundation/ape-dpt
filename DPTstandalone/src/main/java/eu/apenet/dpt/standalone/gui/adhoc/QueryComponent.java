package eu.apenet.dpt.standalone.gui.adhoc;

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
