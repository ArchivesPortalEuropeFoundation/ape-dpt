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

import javax.swing.*;
import javax.swing.plaf.metal.MetalIconFactory;
import java.awt.Component;
import java.awt.Color;
import java.io.File;
import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: 16/01/2012
 *
 * @author Yoann Moranville
 */
public class IconListCellRenderer extends DefaultListCellRenderer {
    private Map<String, FileInstance> fileInstances;

    public IconListCellRenderer(Map<String, FileInstance> fileInstances){
        this.fileInstances = fileInstances;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        File file = (File) value;
        super.getListCellRendererComponent(list, file.getName(), index, isSelected, cellHasFocus);
        FileInstance fileInstance = fileInstances.get(file.getName());
        if(fileInstance != null) {
            if(fileInstance.isValid()){
                if(isSelected){
                    setBackground(new Color(100, 180, 100));
                    setBorder(BorderFactory.createLineBorder(getBackground()));
                } else
                    setForeground(new Color(100, 180, 100));
            } else if(fileInstance.getValidationErrors() != null && !fileInstance.getValidationErrors().equals("")){
                if(isSelected){
                    setBackground(new Color(200, 90, 90));
                    setBorder(BorderFactory.createLineBorder(getBackground()));
                } else
                    setForeground(new Color(200, 90, 90));
            }

            if(fileInstance.getLastOperation().equals(FileInstance.Operation.SAVE))
                setIcon(MetalIconFactory.getTreeFloppyDriveIcon());
        }
        return this;
    }
}
