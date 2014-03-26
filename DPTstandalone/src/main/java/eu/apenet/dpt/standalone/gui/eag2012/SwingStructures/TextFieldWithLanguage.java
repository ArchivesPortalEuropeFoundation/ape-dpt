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

import eu.apenet.dpt.standalone.gui.commons.SwingStructures.StructureWithLanguage;
import eu.apenet.dpt.standalone.gui.eag2012.EagPanels;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.EagScrollPane;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.EagScrollPaneHolder;
import eu.apenet.dpt.utils.util.LanguageIsoList;

import javax.swing.*;
import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: 05/12/2012
 *
 * To be used when a TextField also has a language
 *
 * @author Yoann Moranville
 */
public class TextFieldWithLanguage extends StructureWithLanguage {
    private JTextField textField;
    private JTextField extraField;
    private JTextField secondExtraField;

    public TextFieldWithLanguage(String text, String language, String extraText, String secondExtraText) {
        super(language);
        textField = new JTextField(text);
        extraField = new JTextField(extraText);
        secondExtraField = new JTextField(secondExtraText);
    }

    public TextFieldWithLanguage(String text, String language, String extraText) {
        this(text, language, extraText, "");
    }

    public TextFieldWithLanguage(String text, String language) {
        this(text, language, "");
    }

    public JTextField getTextField() {
        return textField;
    }

    public JTextField getExtraField() {
        return extraField;
    }

    public JTextField getSecondExtraField() {
        return secondExtraField;
    }

    public String getTextValue() {
        return textField.getText();
    }

    public String getExtraValue() {
        return extraField.getText();
    }

    public String getSecondExtraValue() {
        return secondExtraField.getText();
    }
}
