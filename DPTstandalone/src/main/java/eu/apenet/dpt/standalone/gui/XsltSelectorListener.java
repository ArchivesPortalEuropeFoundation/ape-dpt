package eu.apenet.dpt.standalone.gui;

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
            FileInstance fileInstance = dataPreparationToolGUI.getFileInstances().get(((File)list.getSelectedValue()).getName());
            fileInstance.setConversionScriptName(e.getActionCommand());
            if(e.getActionCommand().equals("eac2eaccpf.xsl")){
                fileInstance.setFileType(FileInstance.FileType.EAC_CPF);
                fileInstance.setValidationSchema(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAC_SCHEMA.getPath()));
            } else if(e.getActionCommand().equals("eag2eag.xsl")){
                fileInstance.setFileType(FileInstance.FileType.EAG);
                if(!fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAG_SCHEMA.getPath())))
                    fileInstance.setValidationSchema(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAG_2012_SCHEMA.getPath()));
            } else if(e.getActionCommand().equals("default.xsl")) {
                if(!fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAD_SCHEMA.getPath())) && !fileInstance.getValidationSchema().equals(Utilities.getXsdObjectFromPath(Xsd_enum.XSD1_0_APE_SCHEMA.getPath())))
                    fileInstance.setValidationSchema(Utilities.getXsdObjectFromPath(Xsd_enum.XSD_APE_SCHEMA.getPath()));
            }
            dataPreparationToolGUI.refreshButtons(fileInstance, Utilities.XSD_GROUP);
        }
    }
}