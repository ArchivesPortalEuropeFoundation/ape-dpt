package eu.apenet.dpt.standalone.gui.commons.swingstructures;

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

import javax.swing.JTextField;


/**
 * User: Yoann Moranville
 * Date: 26/04/2013
 *
 * @author Yoann Moranville
 */
public class TextAreaWithLanguage extends StructureWithLanguage {
    private ScrollPaneHolder scrollPaneHolder;
    private JTextField extraField;
    private JTextField secondExtraField;

    public TextAreaWithLanguage(String text, String language, String extraText, String secondExtraText) {
        super(language);
        scrollPaneHolder = new ScrollPaneHolder(text);
        extraField = new JTextField(extraText);
        secondExtraField = new JTextField(secondExtraText);
    }

    public TextAreaWithLanguage(String text, String language, String extraText) {
        this(text, language, extraText, "");
    }

    public TextAreaWithLanguage(String text, String language) {
        this(text, language, "");
    }

    public ScrollPane getTextField() {
        return scrollPaneHolder.getScrollPane();
    }

    public JTextField getExtraField() {
        return extraField;
    }

    public JTextField getSecondExtraField() {
        return secondExtraField;
    }

    public String getTextValue() {
        return scrollPaneHolder.getText();
    }

    public String getExtraValue() {
        return extraField.getText();
    }

    public String getSecondExtraValue() {
        return secondExtraField.getText();
    }
}
