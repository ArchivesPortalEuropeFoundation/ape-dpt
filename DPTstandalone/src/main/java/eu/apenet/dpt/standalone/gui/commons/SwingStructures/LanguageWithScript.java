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
