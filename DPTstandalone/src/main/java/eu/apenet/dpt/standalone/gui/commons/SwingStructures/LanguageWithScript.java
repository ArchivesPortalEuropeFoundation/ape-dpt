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

import eu.apenet.dpt.standalone.gui.eacCpf.SwingStructures.TextFieldWithComboBoxEacCpf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 11/12/2012
 *
 * @author Yoann Moranville
 */
public class LanguageWithScript extends StructureWithLanguage {
    private JComboBox<String> scriptBox;
    private static final String[] scripts = {"Arab", "Armn", "Cprt", "Cyrl", "Geor", "Grek", "Hebr", "Latn"};
    private static String[] scriptsDisplay;
    
    public LanguageWithScript(String language, String script, ResourceBundle labels) {
        super(language);
        scriptsDisplay = fillTranslationComboBoxValues(labels);
        scriptBox = new JComboBox<String>(scriptsDisplay);
        if(Arrays.asList(scripts).contains(script)){
        	scriptBox.setSelectedIndex(Arrays.asList(scripts).indexOf(script) + 1);
        }
            
    }

    public JComboBox<String> getScriptBox() {
        return scriptBox;
    }

    public String getScript() {
    	return (String) Arrays.asList(scripts).get(this.scriptBox.getSelectedIndex() - 1);
    }
    
    /**
	 * Method to fill the combo box with translated labels.
	 *
	 * @param labels
	 * @return the array of translated labels.
	 */
    private static String[] fillTranslationComboBoxValues(ResourceBundle labels) {
    	List<String> scriptList = new  LinkedList<String>();
    	scriptList.add("---");
    	for(int i=0; i< scripts.length;i++){
    		scriptList.add(labels.getString("eaccpf.commons.script." + scripts[i]));
    	}
		return scriptList.toArray(new String[scriptList.size()]);
	}
}
