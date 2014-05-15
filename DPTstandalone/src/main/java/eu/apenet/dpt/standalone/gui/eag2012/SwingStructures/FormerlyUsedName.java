/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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


import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithDate;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithLanguage;

/**
 *
 * @author Stefan Papp
 * @date 2013/04/24
 */
public class FormerlyUsedName {
    private TextFieldWithLanguage textFieldWithLanguage;
    private List<TextFieldWithDate> dateList;
    private int orderInXmlFile;
    
    public FormerlyUsedName(String name, String language, List<TextFieldWithDate> dateList){
        this.textFieldWithLanguage = new TextFieldWithLanguage(name, language);
        this.dateList = dateList;
    }

    public FormerlyUsedName(String name, String language){
        this.textFieldWithLanguage = new TextFieldWithLanguage(name, language);
        this.dateList = new ArrayList<TextFieldWithDate>();
    }

    public JTextField getNameTextField() {
        return textFieldWithLanguage.getTextField();
    }

    public JComboBox getLanguageBox() {
        return textFieldWithLanguage.getLanguageBox();
    }

    public String getName() {
        return textFieldWithLanguage.getTextValue();
    }

    public String getLanguage() {
        return textFieldWithLanguage.getLanguage();
    }

    public List<TextFieldWithDate> getDateList() {
        return dateList;
    }

    public void addDate(String date){
        TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", "", "", date);
        dateList.add(textFieldWithDate);
    }
    
    public void addDateRange (String from, String to){
        TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", from, to, "");
        dateList.add(textFieldWithDate);
    }
    
    public void removeDate (String date){
        for (TextFieldWithDate textFieldWithDate : dateList) {
            if(textFieldWithDate.getDate().equals(date)){
                dateList.remove(textFieldWithDate);
                break;
            }
        }
    }

    public void removeDateRange (String from, String to){
        for (TextFieldWithDate textFieldWithDate : dateList) {
            if(textFieldWithDate.getFromDate().equals(from) && textFieldWithDate.getToDate().equals(to)){
                dateList.remove(textFieldWithDate);
                break;
            }
        }
    }

    public TextFieldWithLanguage getTextFieldWithLanguage() {
        return textFieldWithLanguage;
    }

    public int getOrderInXmlFile() {
        return orderInXmlFile;
    }

    public void setOrderInXmlFile(int orderInXmlFile) {
        this.orderInXmlFile = orderInXmlFile;
    }
}
