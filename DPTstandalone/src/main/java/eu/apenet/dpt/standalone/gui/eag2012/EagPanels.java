package eu.apenet.dpt.standalone.gui.eag2012;

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

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import eu.apenet.dpt.standalone.gui.commons.swingstructures.TextFieldWithLanguage;
import eu.apenet.dpt.standalone.gui.options.DigitalObjectAndRightsOptionFrame;
import eu.apenet.dpt.utils.eag2012.RightsDeclaration;
import eu.apenet.dpt.utils.util.RightsInformation;
import eu.apenet.dpt.utils.util.RightsInformationList;
import org.apache.log4j.Logger;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.commons.swingstructures.CommonsPropertiesPanels;
import eu.apenet.dpt.utils.eag2012.Eag;

/**
 * User: Yoann Moranville
 * Date: 03/10/2012
 *
 * @author Yoann Moranville
 */
public abstract class EagPanels extends CommonsPropertiesPanels{
    protected static final Logger LOG = Logger.getLogger(EagPanels.class);

    protected final String[] continents = {"Africa", "Antarctica", "Asia", "Australia", "Europe", "North America", "South America"};
    protected final String[] yesOrNoNotMandatory = {"---", "yes", "no"};
    protected final String[] yesOrNo = {"yes", "no"};
    protected final String[] photographAllowance = {"---", "depending on the material", "no", "yes", "yes (without flash)"};
    protected final String[] repositoryRoles = {"---", "Branch", "Head quarter", "Interim archive"};
    protected final String[] webPrefixes = {"http://", "https://", "ftp://"};
    protected final String[] rightsInformationList = RightsInformationList.getRightsInformationStringList();

    protected JComboBox continentCombo = new JComboBox(continents);
    protected JComboBox accessiblePublicCombo = new JComboBox(yesOrNo);
    protected JComboBox facilitiesForDisabledCombo = new JComboBox(yesOrNo);
    protected JComboBox photographAllowanceCombo = new JComboBox(photographAllowance);
    protected JComboBox rightsCombo = new JComboBox(rightsInformationList);

    protected Eag eag;
    protected JFrame eag2012Frame;
    protected int repositoryNb;
    protected boolean isDataValid;

    public EagPanels(Eag eag, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels, int repositoryNb) {
        this.eag2012Frame = eag2012Frame;
        this.eag = eag;
        this.tabbedPane = tabbedPane;
        this.mainTabbedPane = mainTabbedPane;
        this.model = model;
        this.labels = labels;
        this.repositoryNb = repositoryNb;
    }

    protected void closeFrame() {
        eag2012Frame.dispose();
        eag2012Frame.setVisible(false);
    }

    protected void setInstitutionTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public int getRepositoryNb() {
        return repositoryNb;
    }

    public void setRepositoryNb(int repositoryNb) {
        this.repositoryNb = repositoryNb;
    }
}
