package eu.apenet.dpt.standalone.gui.eag2012;

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

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.commons.ButtonTab;
import eu.apenet.dpt.standalone.gui.commons.DefaultBtnAction;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.ResourceRelationType;
import eu.apenet.dpt.utils.eag2012.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 28/11/2012
 *
 * @author Yoann Moranville
 */
public class EagRelationsPanel extends EagPanels {
    private List<ResourceRelationType> resourceRelationTypes;
    private List<ResourceRelationType> institutionRelationTypes;

    public EagRelationsPanel(Eag eag, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels, int repositoryNb) {
        super(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
    }

    @Override
    protected JComponent buildEditorPanel(List<String> errors) {
        if(errors == null)
            errors = new ArrayList<String>(0);
        else if(Utilities.isDev && errors.size() > 0) {
            LOG.info("Errors in form:");
            for(String error : errors) {
                LOG.info(error);
            }
        }

        FormLayout layout = new FormLayout(
                "right:max(50dlu;p), 4dlu, 100dlu, 7dlu, right:p, 4dlu, 100dlu",
                EDITOR_ROW_SPEC);

        layout.setColumnGroups(new int[][] { { 1, 3, 5, 7 } });
        PanelBuilder builder = new PanelBuilder(layout);

        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        rowNb = 1;
        if(eag.getRelations() == null)
            eag.setRelations(new Relations());
        Relations relations = eag.getRelations();

        builder.addSeparator(labels.getString("eag2012.relations.resourceRelations"), cc.xyw(1, rowNb, 7));
        setNextRow();

        resourceRelationTypes = new ArrayList<ResourceRelationType>(relations.getResourceRelation().size());
        
        if(relations.getResourceRelation().isEmpty())
        	relations.getResourceRelation().add(new ResourceRelation());
      
        for(ResourceRelation resourceRelation : relations.getResourceRelation()) {
            if(resourceRelation.getRelationEntry() == null)
                resourceRelation.setRelationEntry(new RelationEntry());
            if(resourceRelation.getDescriptiveNote() == null) {
                DescriptiveNote descriptiveNote = new DescriptiveNote();
                descriptiveNote.setP(new ArrayList<P>(){{add(new P());}});
                resourceRelation.setDescriptiveNote(descriptiveNote);
            }
            ResourceRelationType resourceRelationType = new ResourceRelationType(resourceRelation.getResourceRelationType(), resourceRelation.getHref(), resourceRelation.getRelationEntry().getContent(), resourceRelation.getDescriptiveNote().getP().get(0).getContent(), resourceRelation.getDescriptiveNote().getP().get(0).getLang(), true);
            resourceRelationTypes.add(resourceRelationType);

            builder.addLabel(labels.getString("eag2012.relations.linkToResourceRelation"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getWebsite(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.relations.typeOfRelation"), cc.xy(5, rowNb));
            builder.add(resourceRelationType.getTypeRelations(), cc.xy(7, rowNb));
            setNextRow();
            if(errors.contains("resourceRelationTypes")) {
                if(StringUtils.isNotBlank(resourceRelationType.getWebsite().getText()) && !StringUtils.startsWithAny(resourceRelationType.getWebsite().getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(resourceRelationType.getWebsite().getText()) && !StringUtils.startsWithAny(resourceRelationType.getWebsite().getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }

            builder.addLabel(labels.getString("eag2012.relations.titleOfRelatedMaterial"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getTitleAndId(), cc.xy(3, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.relations.descriptionOfRelation"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getDescription().getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(resourceRelationType.getDescription().getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
    
        JButton addResourceRelation = new ButtonTab(labels.getString("eag2012.relations.addNewResourceRelation"));
        builder.add(addResourceRelation, cc.xy (1, rowNb));
        addResourceRelation.addActionListener(new AddResourceRelationAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.relations.institutionRelation"), cc.xyw(1, rowNb, 7));
        setNextRow();

        institutionRelationTypes = new ArrayList<ResourceRelationType>(relations.getEagRelation().size());
    
        if(relations.getEagRelation().isEmpty())
        	relations.getEagRelation().add(new EagRelation());
  
        for(EagRelation eagRelation : relations.getEagRelation()) {
            if(eagRelation.getRelationEntry().size() == 0)
                eagRelation.getRelationEntry().add(new RelationEntry());
            if(eagRelation.getDescriptiveNote() == null) {
                DescriptiveNote descriptiveNote = new DescriptiveNote();
                descriptiveNote.setP(new ArrayList<P>(){{add(new P());}});
                eagRelation.setDescriptiveNote(descriptiveNote);
            }
            ResourceRelationType resourceRelationType = new ResourceRelationType(eagRelation.getEagRelationType(), eagRelation.getHref(), eagRelation.getRelationEntry().get(0).getContent(), eagRelation.getDescriptiveNote().getP().get(0).getContent(), eagRelation.getDescriptiveNote().getP().get(0).getLang(), false);
            institutionRelationTypes.add(resourceRelationType);

            builder.addLabel(labels.getString("eag2012.relations.websiteOfDescription"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getWebsite(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.relations.typeOfRelation"), cc.xy(5, rowNb));
            builder.add(resourceRelationType.getTypeRelations(), cc.xy(7, rowNb));
            setNextRow();
            if(errors.contains("institutionRelationTypes")) {
                if(StringUtils.isNotBlank(resourceRelationType.getWebsite().getText()) && !StringUtils.startsWithAny(resourceRelationType.getWebsite().getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(resourceRelationType.getWebsite().getText()) && !StringUtils.startsWithAny(resourceRelationType.getWebsite().getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }

            builder.addLabel(labels.getString("eag2012.relations.nameIdRelatedInstitution"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getTitleAndId(), cc.xy(3, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.relations.descriptionOfRelation"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getDescription().getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(resourceRelationType.getDescription().getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        
        JButton addInstitutionRelation = new ButtonTab(labels.getString("eag2012.relations.addNewInstitutionRelation"));
        builder.add(addInstitutionRelation, cc.xy (1, rowNb));
        addInstitutionRelation.addActionListener(new AddInstitutionRelationAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();

        JButton exitBtn = new ButtonTab(labels.getString("eag2012.commons.exit"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton previousTabBtn = new ButtonTab(labels.getString("eag2012.commons.previousTab"));
        builder.add(previousTabBtn, cc.xy (3, rowNb));
        previousTabBtn.addActionListener(new PreviousTabBtnAction(eag, tabbedPane, model));

        JButton saveBtn = new ButtonTab(labels.getString("eag2012.commons.save"));
        builder.add(saveBtn, cc.xy (5, rowNb));
        saveBtn.addActionListener(new SaveBtnAction(eag, tabbedPane, model));

        setNextRow();
        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();
        JButton nextInstitutionTabBtn = new ButtonTab(labels.getString("eag2012.controls.nextInstitution"));
        nextInstitutionTabBtn.addActionListener(new NextInstitutionTabBtnAction(eag, tabbedPane, model));
        builder.add(nextInstitutionTabBtn, cc.xy(5, rowNb));

        return builder.getPanel();
    }

    public class NextInstitutionTabBtnAction extends UpdateEagObject {
        NextInstitutionTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(true);

                int currentTabIndex = mainTabbedPane.getSelectedIndex();
                if(currentTabIndex+1 < mainTabbedPane.getTabCount()) {
                    reloadTabbedPanel(new EagRelationsPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 6);
                    mainTabbedPane.setEnabledAt(currentTabIndex, false);
                    mainTabbedPane.setEnabledAt(currentTabIndex+1, true);
                    mainTabbedPane.setSelectedIndex(currentTabIndex+1);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagRelationsPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 6);
            }
        }
    }

    public class AddResourceRelationAction extends UpdateEagObject {
        AddResourceRelationAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
           
           if(eag.getRelations() == null)
               eag.setRelations(new Relations());
           
            boolean empty = false;
            List <ResourceRelation> errResources = new ArrayList<ResourceRelation>();
            
            for (ResourceRelationType resourceRelationType: resourceRelationTypes){
            	if(StringUtils.isEmpty(resourceRelationType.getTypeRelationsValue()) || StringUtils.isEmpty(resourceRelationType.getWebsiteValue())){ 
            		empty = true;

            		boolean hasChanged = false;
            		ResourceRelation resourceRelation = new ResourceRelation();
                    resourceRelation.setResourceRelationType(resourceRelationType.getTypeRelationsValue());
                   
                    if (StringUtils.isNotEmpty(resourceRelationType.getWebsiteValue())) {
                    	resourceRelation.setHref(resourceRelationType.getWebsiteValue());
                    	hasChanged = true;
                    }
                    
                    if (StringUtils.isNotEmpty(resourceRelationType.getTitleAndIdValue()) || StringUtils.isNotEmpty(resourceRelationType.getDescriptionValue()) || StringUtils.isNotEmpty(resourceRelationType.getDescriptionLanguage())) {
	                    
                    	if(resourceRelation.getRelationEntry() == null)
	                        resourceRelation.setRelationEntry(new RelationEntry());

                    	if (StringUtils.isNotEmpty(resourceRelationType.getTitleAndIdValue())) {
	                    	resourceRelation.getRelationEntry().setContent(resourceRelationType.getTitleAndIdValue());
	                    	hasChanged = true;
                    	}

	                    if (StringUtils.isNotEmpty(resourceRelationType.getDescriptionValue()) || StringUtils.isNotEmpty(resourceRelationType.getDescriptionLanguage())){
		                    if(resourceRelation.getDescriptiveNote() == null)
		                        resourceRelation.setDescriptiveNote(new DescriptiveNote());
		                    if(resourceRelation.getDescriptiveNote().getP().size() == 0)
		                        resourceRelation.getDescriptiveNote().getP().add(new P());
		                    
		                    resourceRelation.getDescriptiveNote().getP().get(0).setContent(resourceRelationType.getDescriptionValue());
		                    resourceRelation.getDescriptiveNote().getP().get(0).setLang(resourceRelationType.getDescriptionLanguage());
	                    	hasChanged = true;
	                    }
                    }

                    if (hasChanged) {
                    	errResources.add(resourceRelation);
                    }
            	}
            }

			if (empty)
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.linkToResourceRelation"));

			if (!errResources.isEmpty()) {
				for (ResourceRelation resourceRelation : errResources) {
					eag.getRelations().getResourceRelation().add(resourceRelation);
				}
			} else {
				eag.getRelations().getResourceRelation().add(new ResourceRelation());
			}
            reloadTabbedPanel(new EagRelationsPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 6);
        }
    }
    
    public class AddInstitutionRelationAction extends UpdateEagObject {
        AddInstitutionRelationAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            
            
            if(eag.getRelations() == null)
				eag.setRelations(new Relations());

            boolean empty = false;
            List <EagRelation> errResources = new ArrayList<EagRelation>();
            
            
            for (ResourceRelationType eagRelationType: institutionRelationTypes){
            	if(StringUtils.isEmpty(eagRelationType.getTypeRelationsValue()) || StringUtils.isEmpty(eagRelationType.getWebsiteValue())){ 
            		empty = true;

            		boolean hasChanged = false;
            		EagRelation eagRelation = new EagRelation();
            		eagRelation.setEagRelationType(eagRelationType.getTypeRelationsValue());
                   
                    if (StringUtils.isNotEmpty(eagRelationType.getWebsiteValue())) {
                    	eagRelation.setHref(eagRelationType.getWebsiteValue());
                    	hasChanged = true;
                    }
                    
                    if (StringUtils.isNotEmpty(eagRelationType.getTitleAndIdValue()) || StringUtils.isNotEmpty(eagRelationType.getDescriptionValue()) || StringUtils.isNotEmpty(eagRelationType.getDescriptionLanguage())) {
	                    
                    	if(eagRelation.getRelationEntry() == null
                    			|| eagRelation.getRelationEntry().isEmpty())
                    		eagRelation.getRelationEntry().add(new RelationEntry());

                    	if (StringUtils.isNotEmpty(eagRelationType.getTitleAndIdValue())) {
                    		eagRelation.getRelationEntry().get(0).setContent(eagRelationType.getTitleAndIdValue());
	                    	hasChanged = true;
                    	}

	                    if (StringUtils.isNotEmpty(eagRelationType.getDescriptionValue()) || StringUtils.isNotEmpty(eagRelationType.getDescriptionLanguage())){
		                    if(eagRelation.getDescriptiveNote() == null)
		                    	eagRelation.setDescriptiveNote(new DescriptiveNote());
		                    if(eagRelation.getDescriptiveNote().getP().size() == 0)
		                    	eagRelation.getDescriptiveNote().getP().add(new P());
		                    
		                    eagRelation.getDescriptiveNote().getP().get(0).setContent(eagRelationType.getDescriptionValue());
		                    eagRelation.getDescriptiveNote().getP().get(0).setLang(eagRelationType.getDescriptionLanguage());
	                    	hasChanged = true;
	                    }
                    }

                    if (hasChanged) {
                    	errResources.add(eagRelation);
                    }
            	}
            }
            
            if (empty)
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.institutionRelation"));

			if (!errResources.isEmpty()) {
				for (EagRelation resourceRelation : errResources) {
					eag.getRelations().getEagRelation().add(resourceRelation);
				}
			} else {
				eag.getRelations().getEagRelation().add(new EagRelation());
			}
            
			reloadTabbedPanel(new EagRelationsPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 6);
        }
    }

    public class PreviousTabBtnAction extends UpdateEagObject {
        PreviousTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);

                reloadTabbedPanel(new EagControlPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 5);
                tabbedPane.setEnabledAt(5, true);
                tabbedPane.setEnabledAt(6, false);
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagRelationsPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 6);
            }
        }
    }

    public class SaveBtnAction extends UpdateEagObject {
        SaveBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(true);
                super.saveFile(eag.getControl().getRecordId().getValue());
                closeFrame();
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagRelationsPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 6);
            }
        }
    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        public UpdateEagObject(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        protected void updateJAXBObject(boolean save) throws Eag2012FormException {
            errors = new ArrayList<String>();

            boolean hasChanged = false;
            
            eag.getRelations().getResourceRelation().clear();
            for(ResourceRelationType resourceRelationType : resourceRelationTypes) {
                if(StringUtils.isNotEmpty(resourceRelationType.getWebsiteValue())) {
                    ResourceRelation resourceRelation = new ResourceRelation();
                    resourceRelation.setResourceRelationType(resourceRelationType.getTypeRelationsValue());
                    resourceRelation.setHref(resourceRelationType.getWebsiteValue());
                    if(!StringUtils.startsWithAny(resourceRelationType.getWebsiteValue(), webPrefixes))
                            errors.add("resourceRelationTypes");
                    if(resourceRelation.getRelationEntry() == null)
                        resourceRelation.setRelationEntry(new RelationEntry());
                    resourceRelation.getRelationEntry().setContent(resourceRelationType.getTitleAndIdValue());
                    if(resourceRelation.getDescriptiveNote() == null)
                        resourceRelation.setDescriptiveNote(new DescriptiveNote());
                    if(resourceRelation.getDescriptiveNote().getP().size() == 0)
                        resourceRelation.getDescriptiveNote().getP().add(new P());
                    resourceRelation.getDescriptiveNote().getP().get(0).setContent(resourceRelationType.getDescriptionValue());
                    resourceRelation.getDescriptiveNote().getP().get(0).setLang(resourceRelationType.getDescriptionLanguage());

                    eag.getRelations().getResourceRelation().add(resourceRelation);
                    hasChanged = true;
                }
            }

            eag.getRelations().getEagRelation().clear();
            for(ResourceRelationType resourceRelationType : institutionRelationTypes) {
                if(StringUtils.isNotEmpty(resourceRelationType.getWebsiteValue())) {
                    EagRelation eagRelation = new EagRelation();
                    eagRelation.setEagRelationType(resourceRelationType.getTypeRelationsValue());
                    eagRelation.setHref(resourceRelationType.getWebsiteValue());
                    if(!StringUtils.startsWithAny(resourceRelationType.getWebsiteValue(), webPrefixes))
                            errors.add("institutionRelationTypes");
                    RelationEntry relationEntry = new RelationEntry();
                    relationEntry.setContent(resourceRelationType.getTitleAndIdValue());
                    eagRelation.getRelationEntry().add(relationEntry);
                    if(eagRelation.getDescriptiveNote() == null)
                        eagRelation.setDescriptiveNote(new DescriptiveNote());
                    if(eagRelation.getDescriptiveNote().getP().size() == 0)
                        eagRelation.getDescriptiveNote().getP().add(new P());
                    eagRelation.getDescriptiveNote().getP().get(0).setContent(resourceRelationType.getDescriptionValue());
                    eagRelation.getDescriptiveNote().getP().get(0).setLang(resourceRelationType.getDescriptionLanguage());

                    eag.getRelations().getEagRelation().add(eagRelation);
                    hasChanged = true;
                }
            }

            if(eag.getRelations().getEagRelation().size() == 0 && eag.getRelations().getResourceRelation().size() == 0)
                eag.setRelations(null);

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }

}
