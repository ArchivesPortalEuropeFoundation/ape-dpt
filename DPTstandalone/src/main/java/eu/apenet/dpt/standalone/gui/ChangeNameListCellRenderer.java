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
import java.awt.*;
import java.io.File;
import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: 04/12/2013
 *
 * @author Yoann Moranville
 */
public class ChangeNameListCellRenderer extends IconListCellRenderer {

    public ChangeNameListCellRenderer(Map<String, FileInstance> fileInstances) {
        super(fileInstances);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        File file = (File) value;
        if(file.getName().startsWith("temp_"))
            setText(file.getName().replace("temp_", ""));

        return component;
    }
}
