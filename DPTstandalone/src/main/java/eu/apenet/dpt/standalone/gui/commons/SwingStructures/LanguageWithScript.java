package eu.apenet.dpt.standalone.gui.commons.SwingStructures;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 11/12/2012
 *
 * @author Yoann Moranville
 */
public class LanguageWithScript extends StructureWithLanguage {
    private JComboBox scriptBox;

    private static final String[] scripts = {"Arab", "Armn", "Cprt", "Cyrl", "Geor", "Grek", "Hebr", "Latn"};
    private static String[] scriptsDisplay;

    static {
        List<String> scriptList = new ArrayList<String>();
        scriptList.add("---");
        scriptList.addAll(Arrays.asList(scripts));
        scriptsDisplay = scriptList.toArray(new String[]{});
    }

    public LanguageWithScript(String language, String script) {
        super(language);
        scriptBox = new JComboBox(scriptsDisplay);
        if(Arrays.asList(scripts).contains(script))
            scriptBox.setSelectedItem(script);
    }

    public JComboBox getScriptBox() {
        return scriptBox;
    }

    public String getScript() {
        if(Arrays.asList(scripts).contains((String) scriptBox.getSelectedItem()))
            return (String)scriptBox.getSelectedItem();
        return null;
    }
}
