package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import eu.apenet.dpt.standalone.gui.eag2012.EagPanels;
import eu.apenet.dpt.utils.util.LanguageIsoList;

import javax.swing.*;
import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: 15/05/2013
 *
 * @author Yoann Moranville
 */
public class StructureWithLanguage {
    private JComboBox languageBox;
    private static final String[] languages = EagPanels.languages;
    private static final String[] languagesDisplay = EagPanels.languagesDisplay;

    public StructureWithLanguage(String language) {
        languageBox = new JComboBox(languagesDisplay);
        if(Arrays.asList(languages).contains(LanguageIsoList.getLanguageStr(language)))
            languageBox.setSelectedItem(LanguageIsoList.getLanguageStr(language));
        else
            languageBox.setSelectedItem("---");
    }

    public JComboBox getLanguageBox() {
        return languageBox;
    }

    public String getLanguage() {
        if(Arrays.asList(languages).contains((String) languageBox.getSelectedItem()))
            return LanguageIsoList.getIsoCode((String)languageBox.getSelectedItem());
        return null;
    }
}
