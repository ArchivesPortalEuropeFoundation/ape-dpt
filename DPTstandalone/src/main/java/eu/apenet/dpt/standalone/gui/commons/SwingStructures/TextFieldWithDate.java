package eu.apenet.dpt.standalone.gui.commons.SwingStructures;

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


/**
 * User: Yoann Moranville
 * Date: 10/12/2012
 *
 * @author Yoann Moranville
 */
public class TextFieldWithDate {
    private TextFieldWithLanguage textFieldWithLanguage;
    private JTextField dateField;
    private JTextField fromDateField;
    private JTextField toDateField;
    private boolean isDateRange;

    public TextFieldWithDate(String text, String language, String fromDate, String toDate, String date) {
        textFieldWithLanguage = new TextFieldWithLanguage(text, language);
        fromDateField = new JTextField(fromDate);
        toDateField = new JTextField(toDate);
        dateField = new JTextField(date);
        isDateRange = false;
    }

    public boolean isDateRange() {
        return isDateRange;
    }

    public void setDateRange(boolean dateRange) {
        isDateRange = dateRange;
    }

    public JTextField getTextField() {
        return textFieldWithLanguage.getTextField();
    }

    public JComboBox getLanguageBox() {
        return textFieldWithLanguage.getLanguageBox();
    }

    public String getTextValue() {
        return textFieldWithLanguage.getTextValue();
    }

    public String getLanguage() {
        return textFieldWithLanguage.getLanguage();
    }

    public JTextField getFromDateField() {
        return fromDateField;
    }

    public JTextField getToDateField() {
        return toDateField;
    }

    public JTextField getDateField() {
        return dateField;
    }

    public String getFromDate() {
        return fromDateField.getText();
    }

    public String getToDate() {
        return toDateField.getText();
    }

    public String getDate() {
        return dateField.getText();
    }
}
