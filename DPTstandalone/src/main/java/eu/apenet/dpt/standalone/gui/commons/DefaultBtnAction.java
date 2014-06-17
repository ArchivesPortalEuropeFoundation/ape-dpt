package eu.apenet.dpt.standalone.gui.commons;

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

import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.eacCpf.EacCpfControlPanel;
import eu.apenet.dpt.standalone.gui.eacCpf.EacCpfDescriptionPanel;
import eu.apenet.dpt.standalone.gui.eacCpf.EacCpfFormException;
import eu.apenet.dpt.standalone.gui.eacCpf.EacCpfFrame;
import eu.apenet.dpt.standalone.gui.eacCpf.EacCpfIdentityPanel;
import eu.apenet.dpt.standalone.gui.eacCpf.EacCpfPanel;
import eu.apenet.dpt.standalone.gui.eacCpf.EacCpfRelationsPanel;
import eu.apenet.dpt.standalone.gui.eag2012.*;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.namespace.EacCpfNamespaceMapper;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.namespace.EagNamespaceMapper;
import eu.apenet.dpt.utils.util.XmlTypeEacCpf;
import eu.apenet.dpt.utils.util.Xsd_enum;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 03/10/2012
 *
 * @author Yoann Moranville
 */
public abstract class DefaultBtnAction implements ActionListener {
	
	protected static final String SIMPLE = "simple";
	
    protected Eag eag;
    protected EacCpf eaccpf;
    protected JTabbedPane tabbedPane;
    protected List<String> errors;
    protected ProfileListModel model;
    private boolean isEag;

    public DefaultBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
        this.eag = eag;
        this.tabbedPane = tabbedPane;
        this.model = model;
        this.isEag = true;
    }

    public DefaultBtnAction(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model) {
        this.eaccpf = eaccpf;
        this.tabbedPane = tabbedPane;
        this.model = model;
        this.isEag = false;
    }

    public abstract void actionPerformed(ActionEvent actionEvent);
    protected abstract void updateJAXBObject(boolean save) throws Eag2012FormException, EacCpfFormException;

    protected boolean notEqual(String newValue, String originalValue) {
        return !newValue.equals(originalValue);
    }

    protected List<String> getErrors() {
        return errors;
    }

    protected void saveFile(String id) {
    	if (this.isEag) {
            Eag2012Frame.inUse(false);
    	} else {
    		EacCpfFrame.inUse(false);
    	}
        try {
            if(model == null) {
            	if (this.isEag) {
            		throw new Eag2012FormException("The model is null, we can not add the EAG to the list...");
            	} else {
            		throw new EacCpfFormException("The model is null, we can not add the EAC-CPF to the list...");
            	}
            }
            JAXBContext jaxbContext;
            if (this.isEag) {
            	jaxbContext = JAXBContext.newInstance(Eag.class);
            } else {
            	jaxbContext = JAXBContext.newInstance(EacCpf.class);
            }
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            File file;
            if (this.isEag) {
                jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EagNamespaceMapper());
            	file = new File(Utilities.TEMP_DIR + "EAG2012_" + id + ".xml");
            } else {
                jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EacCpfNamespaceMapper());
            	file = new File(Utilities.TEMP_DIR + "EAC-CPF_" + id + ".xml");
            }
            if(model.existsFile(file)) {
                model.removeFile(file);
                file.delete();
            }

            if (this.isEag) {
            	jaxbMarshaller.marshal(eag, file);
            	model.addFile(file, Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAG_2012_SCHEMA.getPath()), FileInstance.FileType.EAG);
            } else {
            	jaxbMarshaller.marshal(this.eaccpf, file);
            	model.addFile(file, Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAC_SCHEMA.getPath()), FileInstance.FileType.EAC_CPF);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected EagPanels getCorrectEagPanels(int tabSelectedIndex, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ResourceBundle labels, int repositoryNb) {
        switch (tabSelectedIndex) {
            case 0: return new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, false, labels, repositoryNb);
            case 1: return new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
            case 2: return new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, false, labels, repositoryNb);
            case 3: return new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
            case 4: return new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
            case 5: return new EagControlPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
            case 6: return new EagRelationsPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
        }
        return null;
    }

    protected EacCpfPanel getCorrectEacCpfPanels(int tabSelectedIndex, JTabbedPane mainTabbedPane, JFrame eacCpfFrame, ResourceBundle labels, XmlTypeEacCpf entityType, String firstLanguage, String firstScript) {
        switch (tabSelectedIndex) {
            case 0:
				String mainagencycode = eaccpf.getControl().getMaintenanceAgency().getAgencyCode().getValue();
            	return new EacCpfIdentityPanel(this.eaccpf, this.tabbedPane, mainTabbedPane, eacCpfFrame, this.model, false, labels, entityType, firstLanguage, firstScript, mainagencycode);
            case 1: return new EacCpfDescriptionPanel(this.eaccpf, this.tabbedPane, mainTabbedPane, eacCpfFrame, this.model, labels, entityType, firstLanguage, firstScript);
            case 2: return new EacCpfRelationsPanel(this.eaccpf, this.tabbedPane, mainTabbedPane, eacCpfFrame, this.model, labels, entityType, firstLanguage, firstScript);
            case 3: return new EacCpfControlPanel(this.eaccpf, this.tabbedPane, mainTabbedPane, eacCpfFrame, this.model, labels, entityType, firstLanguage, firstScript);
        }
        return null;
    }

    protected void reloadTabbedPanel(JComponent panel, int tabPosition) {
        JScrollPane jScrollPane = new JScrollPane(panel);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        tabbedPane.setComponentAt(tabPosition, jScrollPane);
        tabbedPane.setSelectedIndex(tabPosition);
    }
}
