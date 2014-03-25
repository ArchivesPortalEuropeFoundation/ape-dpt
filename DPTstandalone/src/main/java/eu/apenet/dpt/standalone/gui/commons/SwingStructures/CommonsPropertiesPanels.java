package eu.apenet.dpt.standalone.gui.commons.SwingStructures;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.utils.util.LanguageIsoList;

import javax.swing.*;
import java.util.*;

/**
 * Commons properties in the different panels related to EAG and EAC-CPF
 */
public abstract class CommonsPropertiesPanels {
  
    private static final int NB_ROWS = 150;
    private static final String EDITOR_ROW = "p, 3dlu, ";
    protected static String EDITOR_ROW_SPEC;
    public static String[] languages;
    public static String[] languagesDisplay;
    static {
        String temp = "";
        for(int i = 0; i < NB_ROWS; i++) {
            temp += EDITOR_ROW;
        }
        temp += "p";
        EDITOR_ROW_SPEC = temp;

        List<String> languagesList = LanguageIsoList.getLanguageIsoList();
        languages = languagesList.toArray(new String[]{});
        languagesList.add("---");
        languagesDisplay = languagesList.toArray(new String[]{});
    }

    protected final String[] webPrefixes = {"http://", "https://", "ftp://"};

   
    protected int rowNb;
    protected JTabbedPane tabbedPane;
    protected JTabbedPane mainTabbedPane;
    
    protected ProfileListModel model;
    protected ResourceBundle labels;
    
    protected void setNextRow() {
        rowNb += 2;
    }

    protected static JLabel createErrorLabel(String errorMsg) {
        JLabel label = new JLabel("<html><font color=red>" + errorMsg + "</font></html>");
        label.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        return label;
    }

    protected abstract JComponent buildEditorPanel(List<String> errors);

}
