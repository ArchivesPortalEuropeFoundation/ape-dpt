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

import eu.apenet.dpt.utils.util.Xsd_enum;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

/**
 * User: Yoann Moranville Date: 06/04/2012
 *
 * @author Yoann Moranville
 */
public class XsdSelectorListener implements ActionListener {

    private DataPreparationToolGUI dataPreparationToolGUI;

    public XsdSelectorListener(DataPreparationToolGUI dataPreparationToolGUI) {
        this.dataPreparationToolGUI = dataPreparationToolGUI;
    }

    public void actionPerformed(ActionEvent e) {
        if (dataPreparationToolGUI.getXmlEadList().getSelectedValue() != null) {
            Object[] fileInstances = dataPreparationToolGUI.getXmlEadList().getSelectedValues();
            for (Object object : fileInstances) {
                FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(((File)object).getName());
                fileInstance.setValidationSchema(e.getActionCommand());
                if (fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAG_SCHEMA.getPath())) || fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAG_2012_SCHEMA.getPath()))) {
                    fileInstance.setFileType(FileInstance.FileType.EAG);
                } else if (fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAC_SCHEMA.getPath())) || fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_APE_EAC_SCHEMA.getPath()))) {
                    fileInstance.setFileType(FileInstance.FileType.EAC_CPF);
                }
                if (!fileInstance.isValid()){
                    dataPreparationToolGUI.enableValidationBtns();
                }
            }
        }
    }
}
