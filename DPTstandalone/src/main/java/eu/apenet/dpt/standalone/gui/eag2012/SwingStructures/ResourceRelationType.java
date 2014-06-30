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

import javax.swing.*;

import eu.apenet.dpt.standalone.gui.commons.swingstructures.TextAreaWithLanguage;

import java.util.*;

/**
 * User: Yoann Moranville
 * Date: 11/12/2012
 *
 * @author Yoann Moranville
 */
public class ResourceRelationType {
    private static Map<String, String> resourceRelationMap;
    private static Map<String, String> institutionRelationMap;
    private final static String DEFAULT_VALUE ="---";
    static {
        resourceRelationMap = new LinkedHashMap<String, String>();
        resourceRelationMap.put("creator of", "creatorOf");
        resourceRelationMap.put("subject of", "subjectOf");
        resourceRelationMap.put("other", "other");

        institutionRelationMap = new LinkedHashMap<String, String>();
        institutionRelationMap.put(DEFAULT_VALUE, DEFAULT_VALUE);
        institutionRelationMap.put("hierarchical (child)", "hierarchical-child");
        institutionRelationMap.put("hierarchical (parent)", "hierarchical-parent");
        institutionRelationMap.put("temporal (earlier)", "temporal-earlier");
        institutionRelationMap.put("temporal (later)", "temporal-later");
        institutionRelationMap.put("associative", "associative");
    }

    private JComboBox typeRelations;
    private JTextField website;
    private JTextField titleAndId;
    private TextAreaWithLanguage description;

    public ResourceRelationType(String typeRelationValue, String websiteDesc, String titleIdValue, String descriptionOfRel, String langOfDesc, boolean isResourceRelation) {
        website = new JTextField(websiteDesc);
        titleAndId = new JTextField(titleIdValue);
        description = new TextAreaWithLanguage(descriptionOfRel, langOfDesc);
        String keySelected;
        if(isResourceRelation) {
            typeRelations = new JComboBox(resourceRelationMap.keySet().toArray(new String[]{}));
            if((keySelected = getKeyOfSet(resourceRelationMap, typeRelationValue)) != null)
                typeRelations.setSelectedItem(keySelected);
            else 
            	typeRelations.setSelectedIndex(0);
        } else {
            typeRelations = new JComboBox(institutionRelationMap.keySet().toArray(new String[]{}));
            if((keySelected = getKeyOfSet(institutionRelationMap, typeRelationValue)) != null)
                typeRelations.setSelectedItem(keySelected);
            else
                typeRelations.setSelectedItem(DEFAULT_VALUE);            	
        }
    }

    public String getKeyOfSet(Map<String, String> set, String current) {
        for(String key : set.keySet()) {
            if(set.get(key).equals(current)) {
                return key;
            }
        }
        return null;
    }

    public JTextField getWebsite() {
        return website;
    }

    public JTextField getTitleAndId() {
        return titleAndId;
    }

    public TextAreaWithLanguage getDescription() {
        return description;
    }

    public JComboBox getTypeRelations() {
        return typeRelations;
    }

    public String getTypeRelationsValue() {
        if(resourceRelationMap.get((String) typeRelations.getSelectedItem()) != null) {
            return resourceRelationMap.get((String) typeRelations.getSelectedItem());
        } else if(institutionRelationMap.get((String) typeRelations.getSelectedItem()) != null) {
            return institutionRelationMap.get((String) typeRelations.getSelectedItem());
        }
        return null;
    }

    public String getWebsiteValue() {
        return website.getText();
    }

    public String getTitleAndIdValue() {
        return  titleAndId.getText();
    }

    public String getDescriptionLanguage() {
        return description.getLanguage();
    }

    public String getDescriptionValue() {
        return description.getTextValue();
    }
}
