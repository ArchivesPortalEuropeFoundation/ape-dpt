package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import javax.swing.*;
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
    static {
        resourceRelationMap = new HashMap<String, String>();
        resourceRelationMap.put("creator of", "creatorOf");
        resourceRelationMap.put("subject of", "subjectOf");
        resourceRelationMap.put("other", "other");

        institutionRelationMap = new HashMap<String, String>();
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
        } else {
            typeRelations = new JComboBox(institutionRelationMap.keySet().toArray(new String[]{}));
            if((keySelected = getKeyOfSet(institutionRelationMap, typeRelationValue)) != null)
                typeRelations.setSelectedItem(keySelected);
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
