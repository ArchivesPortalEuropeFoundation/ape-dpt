package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

import javax.swing.*;
import java.util.Arrays;

/**
 * User: Yoann Moranville
 * Date: 11/12/2012
 *
 * @author Yoann Moranville
 */
public class ResourceRelationType {
    private static final String[] resourceRelations = {"creatorOf", "subjectOf", "other"};
    private static final String[] institutionRelations = {"hierarchical-child", "hierarchical-parent", "temporal-earlier", "temporal-later", "associative"};

    private JComboBox typeRelations;
    private JTextField website;
    private JTextField titleAndId;
    private TextAreaWithLanguage description;

    public ResourceRelationType(String typeRelationValue, String websiteDesc, String titleIdValue, String descriptionOfRel, String langOfDesc, boolean isResourceRelation) {
        website = new JTextField(websiteDesc);
        titleAndId = new JTextField(titleIdValue);
        description = new TextAreaWithLanguage(descriptionOfRel, langOfDesc);
        if(isResourceRelation) {
            typeRelations = new JComboBox(resourceRelations);
            if(Arrays.asList(resourceRelations).contains(typeRelationValue))
                typeRelations.setSelectedItem(typeRelationValue);
        } else {
            typeRelations = new JComboBox(institutionRelations);
            if(Arrays.asList(institutionRelations).contains(typeRelationValue))
                typeRelations.setSelectedItem(typeRelationValue);
        }
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
        if(Arrays.asList(resourceRelations).contains((String) typeRelations.getSelectedItem()) || Arrays.asList(institutionRelations).contains((String) typeRelations.getSelectedItem()))
            return (String)typeRelations.getSelectedItem();
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
