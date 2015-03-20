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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * User: Yoann Moranville
 * Date: 06/04/2012
 *
 * @author Yoann Moranville
 */
public class XsltSelectorListener implements ActionListener {
    private DataPreparationToolGUI dataPreparationToolGUI;

    public XsltSelectorListener(DataPreparationToolGUI dataPreparationToolGUI) {
        this.dataPreparationToolGUI = dataPreparationToolGUI;
    }

    public void actionPerformed(ActionEvent e) {
        JList list = dataPreparationToolGUI.getXmlEadList();
        if(list.getSelectedValue() != null){
            for(Object object : list.getSelectedValues()){
                FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(((File)object).getName());
                fileInstance.setConversionScriptName(e.getActionCommand());
                if(e.getActionCommand().equals("eac2eaccpf.xsl")){
                    fileInstance.setFileType(FileInstance.FileType.EAC_CPF);
                    fileInstance.setValidationSchema(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_APE_EAC_SCHEMA.getPath()));
                } else if(e.getActionCommand().equals("eag2eag.xsl")){
                    fileInstance.setFileType(FileInstance.FileType.EAG);
                    fileInstance.setValidationSchema(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAG_2012_SCHEMA.getPath()));
                } else if(e.getActionCommand().equals("default-apeEAD.xsl")) {
                    if(!fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAD_SCHEMA.getPath())))
                        fileInstance.setValidationSchema(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_APE_SCHEMA.getPath()));
                } else if(e.getActionCommand().equals("default-apeEAC-CPF.xsl")) {
                    fileInstance.setFileType(FileInstance.FileType.EAC_CPF);
                    if(!fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAC_SCHEMA.getPath())))
                        fileInstance.setValidationSchema(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_APE_EAC_SCHEMA.getPath()));
                }
                dataPreparationToolGUI.refreshButtons(fileInstance, Utilities.XSD_GROUP);
            }
        }
    }
}
