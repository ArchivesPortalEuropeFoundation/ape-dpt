/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.standalone.gui.eag2012;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author Stefan Papp
 * @date 2013/04/24
 */
public class FormerlyUsedName {
    private TextFieldWithLanguage textFieldWithLanguage;
    private List<TextFieldWithDate> dateList;
    
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
}