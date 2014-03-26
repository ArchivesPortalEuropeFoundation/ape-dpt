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

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: Dec 1, 2010
 *
 * @author Yoann Moranville
 */
public class EadidQueryComponent {
    private JPanel panel = new JPanel();
    private JComboBox boxEadid = new JComboBox();
    private ResourceBundle labels;

    public EadidQueryComponent(ResourceBundle labels){
        this.labels = labels;
        boxEadid.setModel(new DefaultComboBoxModel(new String[]{labels.getString("eadidQuery")}));
        boxEadid.setEditable(true);

        panel.setLayout(new GridLayout(2,2,2,5));
        panel.add(new JLabel("EADID"));
        panel.add(boxEadid);
    }

    public EadidQueryComponent(String unitid){

        boxEadid.setModel(new DefaultComboBoxModel(new String[]{unitid}));
        boxEadid.setEditable(true);

        panel.setLayout(new GridLayout(2,2,2,5));
        panel.add(new JLabel("EADID"));
        panel.add(boxEadid);
    }

    public String getEntryEadid(){
        return (String) boxEadid.getSelectedItem();
    }

    public JComponent getMainPanel(){
        return panel;
    }
}
