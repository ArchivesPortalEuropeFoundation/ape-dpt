package eu.apenet.dpt.standalone.gui;

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

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Yoann Moranville
 * Date: 04/04/2012
 *
 * @author Yoann Moranville
 */
public class TabItemActionListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(TabItemActionListener.class);
    private APEPanel apePanel;
    private int tab;

    public TabItemActionListener(APEPanel apePanel, int tab) {
        this.apePanel = apePanel;
        this.tab = tab;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        apePanel.getApeTabbedPane().setSelectedIndex(tab);
        apePanel.getApeTabbedPane().changeBackgroundColor(tab, Utilities.TAB_COLOR);
    }
}
