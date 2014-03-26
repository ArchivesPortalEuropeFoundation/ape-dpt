package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

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
import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: 13/03/2013
 *
 * @author Yoann Moranville
 */
public class TextFieldWithCheckbox {
    private JTextField textField;
    private JComboBox isilOrNotCombo;

    private static final String[] YES_OR_NO = {"yes", "no"};

    public TextFieldWithCheckbox(String text, String isilOrNot) {
        textField = new JTextField(text);
        isilOrNotCombo = new JComboBox(YES_OR_NO);
        if(Arrays.asList(YES_OR_NO).contains(isilOrNot))
            isilOrNotCombo.setSelectedItem(isilOrNot);
        else
            isilOrNotCombo.setSelectedItem("no");
    }

    public JTextField getTextField() {
        return textField;
    }

    public String getTextFieldValue() {
        return textField.getText();
    }

    public JComboBox getIsilOrNotCombo() {
        return isilOrNotCombo;
    }

    public String getisilOrNotComboValue() {
        return (String) isilOrNotCombo.getSelectedItem();
    }
}
