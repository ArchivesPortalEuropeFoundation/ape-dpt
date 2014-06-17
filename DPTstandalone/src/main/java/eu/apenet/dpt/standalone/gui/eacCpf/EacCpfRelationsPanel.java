package eu.apenet.dpt.standalone.gui.eacCpf;

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


import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.commons.ButtonTab;
import eu.apenet.dpt.standalone.gui.commons.DefaultBtnAction;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextAreaWithLanguage;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithLanguage;
import eu.apenet.dpt.standalone.gui.eacCpf.SwingStructures.TextFieldWithComboBoxEacCpf;
import eu.apenet.dpt.utils.eaccpf.AlternativeSet;
import eu.apenet.dpt.utils.eaccpf.ComponentEntry;
import eu.apenet.dpt.utils.eaccpf.CpfDescription;
import eu.apenet.dpt.utils.eaccpf.CpfRelation;
import eu.apenet.dpt.utils.eaccpf.DescriptiveNote;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.FunctionRelation;
import eu.apenet.dpt.utils.eaccpf.P;
import eu.apenet.dpt.utils.eaccpf.RelationEntry;
import eu.apenet.dpt.utils.eaccpf.Relations;
import eu.apenet.dpt.utils.eaccpf.ResourceRelation;
import eu.apenet.dpt.utils.eaccpf.SetComponent;
import eu.apenet.dpt.utils.util.XmlTypeEacCpf;

/**
 * Class for the panel "relations" of the apeEAC-CPF creation form.
 */
public class EacCpfRelationsPanel extends EacCpfPanel {
	// Constants for localType.
	private static final String LOCALTYPE_AGENCYNAME = "agencyName";
	private static final String LOCALTYPE_AGENCYCODE = "agencyCode";
	private static final String LOCALTYPE_ID = "id";
	private static final String LOCALTYPE_TITLE = "title";

	// Elements in the panel.
	private List<TextFieldWithLanguage> cpfRelationNameTfs;
	private List<JTextField> cpfRelationIdentifierTfs;
	private List<TextFieldWithComboBoxEacCpf> cpfRelationHrefAndTypeTfs;
	private List<TextAreaWithLanguage> cpfRelationDescriptionTas;
	private List<List<TextFieldWithLanguage>> cpfRelationOrganisationNameAndIdTfs;
	private ArrayList<TextFieldWithLanguage> resourceRelationNameTfs;
	private ArrayList<JTextField> resourceRelationIdentifierTfs;
	private ArrayList<TextFieldWithComboBoxEacCpf> resourceRelationHrefAndTypeTfs;
	private ArrayList<TextAreaWithLanguage> resourceRelationDescriptionTas;
	private Map<Integer, List<TextFieldWithLanguage>> resourceRelationOrganisationNameAndIdTfs;
	private ArrayList<TextFieldWithLanguage> functionRelationNameTfs;
	private ArrayList<JTextField> functionRelationIdentifierTfs;
	private ArrayList<TextFieldWithComboBoxEacCpf> functionRelationHrefAndTypeTfs;
	private ArrayList<TextAreaWithLanguage> functionRelationDescriptionTas;
	private Map<Integer, List<TextFieldWithLanguage>> functionRelationOrganisationNameAndIdTfs;

	/**
	 * Constructor.
	 *
	 * @param eaccpf
	 * @param tabbedPane
	 * @param mainTabbedPane
	 * @param eacCpfFrame
	 * @param model
	 * @param labels
	 * @param entityType
	 * @param firstLanguage
	 * @param firstScript
	 */
	public EacCpfRelationsPanel(EacCpf eaccpf, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eacCpfFrame, ProfileListModel model, ResourceBundle labels, XmlTypeEacCpf entityType, String firstLanguage, String firstScript) {
		super(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript);
	}

	/**
	 * Builds and answer the relations tab for the given layout.
	 *
	 * @param errors List of errors.
	 * @return the relations tab.
	 */
	protected JComponent buildEditorPanel(List<String> errors) {
		// Checks and initialize the errors list.
		if (errors == null) {
			errors = new ArrayList<String>(0);
		} else if (Utilities.isDev && errors.size() > 0) {
			LOG.info("Errors in form:");
			for (String error : errors) {
				LOG.info(error);
			}
		}

		// Define the layout for the form.
		FormLayout layout = new FormLayout(
				"right:max(50dlu;p), 4dlu, 100dlu, 7dlu, right:p, 4dlu, 100dlu",
				EDITOR_ROW_SPEC);
		layout.setColumnGroups(new int[][] { { 1, 3, 5, 7 } });

		// Construct the panel.
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		CellConstraints cc = new CellConstraints(); // Constraints for the cells;

		// Create object parts if needed.
		if (this.eaccpf.getCpfDescription() == null) {
			this.eaccpf.setCpfDescription(new CpfDescription());
		}
		if (this.eaccpf.getCpfDescription().getRelations() == null) {
			this.eaccpf.getCpfDescription().setRelations(new Relations());
		}

		// Call method to build the text of the entity type.
		builder = this.buildEntityTypeText(builder, cc);
		
		// init globals
		initCPFGlobals();
		
		// Set section title.
		this.setNextRow();
		builder.addLabel(this.labels.getString("eaccpf.relations.cpf.section"), cc.xyw(1, this.rowNb, 7));
		
		//Call method to build sets relations section.
		builder = buildSetRelationsSection(builder, cc);
		// Call method to build cpf relations section.
		builder = this.buildCPFRelationsSection(builder, cc);
		
		// Button to add new CPF/identity relation.
		this.setNextRow();
		JButton addFurtherCPFBtn = new ButtonTab(this.labels.getString("eaccpf.relations.add.further.cpf"));
		addFurtherCPFBtn.addActionListener(new AddFurtherCPF(this.eaccpf, this.tabbedPane, this.model));
		builder.add(addFurtherCPFBtn, cc.xy(1, this.rowNb));

		// Call method to build resource relations section.
		builder = buildResourceRelationsSection(builder, cc);

		// Call method to build functions relations section.
		builder = buildFunctionsRelationsSection(builder, cc);

		// Call method to build the main buttons zone.
		this.buildButtons(builder, cc);

		// Define the change tab listener.
		this.removeChangeListener();
		this.tabbedPane.addChangeListener(new ChangeTabListener (this.eaccpf, this.tabbedPane, this.model, 2));

		return builder.getPanel();
	}

	private PanelBuilder buildSetRelationsSection(PanelBuilder builder,CellConstraints cc) {
		
		// Define values of the part.
		if(this.eaccpf.getCpfDescription().getAlternativeSet()==null){
			this.eaccpf.getCpfDescription().setAlternativeSet(new AlternativeSet());
		}
		
		if (this.eaccpf.getCpfDescription().getAlternativeSet().getSetComponent().isEmpty()) {
//			this.eaccpf.getCpfDescription().getAlternativeSet().getSetComponent().add(new SetComponent());
		}

		List<SetComponent> setComponents = this.eaccpf.getCpfDescription().getAlternativeSet().getSetComponent();

		for (int i = 0; i < setComponents.size(); i++) {
			// Set title.
			this.setNextRow();
			builder.addSeparator(this.labels.getString("eaccpf.relations.cpf.relation"), cc.xyw(1, this.rowNb, 7));
			this.setNextRow();

			SetComponent setComponent = setComponents.get(i);

			// Create elements.
			TextFieldWithLanguage setComponentNameTf = null;
			JTextField setComponentIdentifierTf = null;
			TextFieldWithComboBoxEacCpf setComponentHrefAndTypeTf = new TextFieldWithComboBoxEacCpf(setComponent.getHref(), "identity", TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION, this.entityType, this.labels);
			TextAreaWithLanguage setComponentDescriptionTa = null;
			List<TextFieldWithLanguage> setComponentOrganisationNameAndIdTfList = new ArrayList<TextFieldWithLanguage>();

			// Recovers the values of the relation entry elements.
			String title = "";
			String other = "";
			String language = "";
			String id = "";
			List<String> agencyNameList = new ArrayList<String>();
			List<String> agencyCodeList = new ArrayList<String>();
			if (!setComponent.getComponentEntry().isEmpty()) {
				for (ComponentEntry componentEntry : setComponent.getComponentEntry()) {
					// Title of the relation.
					// /eacCpf/cpfDescription/relations/setComponent/componentEntry@localType='title'
					if(StringUtils.isNotEmpty(componentEntry.getLocalType()) && componentEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_TITLE)
							&& StringUtils.isNotEmpty(componentEntry.getContent())) {
						title = componentEntry.getContent();
					}
					// Id of the relation.
		            // /eacCpf/cpfDescription/relations/setComponent/componentEntry@localType='id'
					if(StringUtils.isNotEmpty(componentEntry.getLocalType()) && componentEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_ID)
							&& StringUtils.isNotEmpty(componentEntry.getContent())) {
						id = componentEntry.getContent();
					}

					// Agency name of the relation.
					// /eacCpf/cpfDescription/relations/setComponent/componentEntry@localType='agencyName'
					if(StringUtils.isNotEmpty(componentEntry.getLocalType()) && componentEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME)) {
						agencyNameList.add(componentEntry.getContent());
					}

					// Agency code of the relation.
					// /eacCpf/cpfDescription/relations/setComponent/componentEntry@localType='agencyCode'
					if(StringUtils.isNotEmpty(componentEntry.getLocalType()) && componentEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE)) {
						agencyCodeList.add(componentEntry.getContent());
					}

					// Texts of relation entries without @localType.
					// This texts will be loaded in field "name"
					if(StringUtils.isNotEmpty(componentEntry.getContent()) && StringUtils.isEmpty(componentEntry.getLocalType())) {
						if (!other.isEmpty()) {
							other = other + ". ";
						}
						other = other + componentEntry.getContent();
					}
							

					// Language of the relation.
					// /eacCpf/cpfDescription/relations/setComponent/componentEntry@lang
					if(StringUtils.isEmpty(language) && StringUtils.isNotEmpty(componentEntry.getLang())) {
						language = componentEntry.getLang();
					}
				}
			} else if (StringUtils.isNotEmpty(this.firstLanguage)) {
				language = this.firstLanguage;
			}

			// Name and language of the relation.
			if (title.isEmpty()) {
				title = other;
			}
			setComponentNameTf = new TextFieldWithLanguage(title, language);

			// Identifier of the relation.
			setComponentIdentifierTf = new JTextField(id);

			// Descriptive note.
			// /eacCpf/cpfDescription/relations/cpfRelation/descriptiveNote
			// Currently only recovers the content of the first value.
			String descriptiveNote = "";
			if(setComponent.getDescriptiveNote() != null && !setComponent.getDescriptiveNote().getP().isEmpty()
	                && StringUtils.isNotEmpty(setComponent.getDescriptiveNote().getP().get(0).getContent())){
				descriptiveNote = setComponent.getDescriptiveNote().getP().get(0).getContent();
			}
			setComponentDescriptionTa = new TextAreaWithLanguage(descriptiveNote, "");

			// Agency code and name of the relation.
			int size = 0;
			if (agencyNameList.size() > agencyCodeList.size()) {
				size = agencyNameList.size();
			} else {
				size = agencyCodeList.size();
			}

			if (size == 0) {
				agencyNameList.add("");
				agencyCodeList.add("");
				size = 1;
			}

			for (int j = 0; j < size; j++) {
				String agencyName = "";
				String agencyCode = "";

				if (agencyNameList.size() > j) {
					agencyName = agencyNameList.get(j);
				}

				if (agencyCodeList.size() > j) {
					agencyCode = agencyCodeList.get(j);
				}

				TextFieldWithLanguage cpfRelationOrganisationNameAndIdTf = new TextFieldWithLanguage(agencyName, language, agencyCode);
				setComponentOrganisationNameAndIdTfList.add(cpfRelationOrganisationNameAndIdTf);
			}

			// Add elements to the list.
			this.cpfRelationNameTfs.add(setComponentNameTf);
			this.cpfRelationIdentifierTfs.add(setComponentIdentifierTf);
			this.cpfRelationHrefAndTypeTfs.add(setComponentHrefAndTypeTf);
			this.cpfRelationDescriptionTas.add(setComponentDescriptionTa);
			
			cpfRelationOrganisationNameAndIdTfs.add(setComponentOrganisationNameAndIdTfList);
			
			// Add elements to the panel.
			// Name and language.
			builder.addLabel(this.labels.getString("eaccpf.relations.name"), cc.xy(1, this.rowNb));
			builder.add(setComponentNameTf.getTextField(), cc.xy(3, this.rowNb));
			builder.addLabel(this.labels.getString("eaccpf.commons.select.language"), cc.xy(5, this.rowNb));
			builder.add(setComponentNameTf.getLanguageBox(), cc.xy(7, this.rowNb));

			// Identity.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.cpf.identifier"), cc.xy(1, this.rowNb));
			builder.add(setComponentIdentifierTf, cc.xy(3, this.rowNb));

			// Link and relation type.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.link"), cc.xy(1, this.rowNb));
			builder.add(setComponentHrefAndTypeTf.getTextField(), cc.xy(3, this.rowNb));
			builder.addLabel(this.labels.getString("eaccpf.relations.relation.type"), cc.xy(5, this.rowNb));
			builder.add(setComponentHrefAndTypeTf.getComboBox(), cc.xy(7, this.rowNb));

			// Description.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.description"), cc.xy(1, this.rowNb));
			builder.add(setComponentDescriptionTa.getTextField(), cc.xyw(3, this.rowNb, 5));

			// Agency part.
			// Title.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.organisation"), cc.xyw(1, this.rowNb, 7));

			for (int j = 0; j < setComponentOrganisationNameAndIdTfList.size(); j++) {
				// Name and identifier.
				this.setNextRow();
				builder.addLabel(this.labels.getString("eaccpf.relations.name"), cc.xy(1, this.rowNb));
				builder.add(setComponentOrganisationNameAndIdTfList.get(j).getTextField(), cc.xy(3, this.rowNb));
				builder.addLabel(this.labels.getString("eaccpf.relations.identifier"), cc.xy(5, this.rowNb));
				builder.add(setComponentOrganisationNameAndIdTfList.get(j).getExtraField(), cc.xy(7, this.rowNb));
			}

			// Button to add new agency information.
			this.setNextRow();
			JButton addFurtherAgencyBtn = new ButtonTab(this.labels.getString("eaccpf.relations.add.further.organisation"));
			addFurtherAgencyBtn.addActionListener(new AddFurtherAgency(this.eaccpf, this.tabbedPane, this.model,AddFurtherAgency.CPF,i));
			builder.add(addFurtherAgencyBtn, cc.xy(1, this.rowNb));
		}

		return builder;
	}

	/**
	 * Method to build the label with the text related to the entity type.
	 *
	 * @param builder
	 * @param cc
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildEntityTypeText(PanelBuilder builder, CellConstraints cc) {
		// First row of the panel.
		this.rowNb = 1;

		// Try to recover the type.
		String type = "";
		if (this.eaccpf != null && this.eaccpf.getCpfDescription() != null && this.eaccpf.getCpfDescription().getIdentity() != null
				&& this.eaccpf.getCpfDescription().getIdentity().getEntityType() != null
				&& this.eaccpf.getCpfDescription().getIdentity().getEntityType().getValue() != null
				&& !StringUtils.isEmpty(this.eaccpf.getCpfDescription().getIdentity().getEntityType().getValue())) {
			type = this.eaccpf.getCpfDescription().getIdentity().getEntityType().getValue();
		} else {
			type = this.entityType.getName();
		}

		if (XmlTypeEacCpf.EAC_CPF_CORPORATEBODY.getName().equalsIgnoreCase(type)) {
			builder.addLabel(this.labels.getString("eaccpf.commons.type") + " " + this.labels.getString("eaccpf.commons.corporateBody"), cc.xyw(1, this.rowNb, 3));
			this.entityType = XmlTypeEacCpf.EAC_CPF_CORPORATEBODY;
		} else if (XmlTypeEacCpf.EAC_CPF_FAMILY.getName().equalsIgnoreCase(type)) {
			builder.addLabel(this.labels.getString("eaccpf.commons.type") + " " + this.labels.getString("eaccpf.commons.family"), cc.xyw(1, this.rowNb, 3));
			this.entityType = XmlTypeEacCpf.EAC_CPF_FAMILY;
		} else if (XmlTypeEacCpf.EAC_CPF_PERSON.getName().equalsIgnoreCase(type)) {
			builder.addLabel(this.labels.getString("eaccpf.commons.type") + " " + this.labels.getString("eaccpf.commons.person"), cc.xyw(1, this.rowNb, 3));
			this.entityType = XmlTypeEacCpf.EAC_CPF_PERSON;
		} else {
			builder.addLabel(this.labels.getString("eaccpf.commons.unrecognized.type"), cc.xyw (1, this.rowNb, 3));
		}

        this.setNextRow();
		builder.addSeparator("", cc.xyw(1, this.rowNb, 7));
        this.setNextRow();

		return builder;
	}

	/**
	 * Main method to build the section that contains the relations with other EAC-CPF files.
	 *
	 * @param builder
	 * @param cc
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildCPFRelationsSection(PanelBuilder builder, CellConstraints cc) {
		
		// Call method to build cpf relations part.
		builder = this.buildCPFRelation(builder, cc);

		return builder;
	}

	private void initCPFGlobals() {
		this.cpfRelationNameTfs = new ArrayList<TextFieldWithLanguage>();
		this.cpfRelationIdentifierTfs = new ArrayList<JTextField>();
		this.cpfRelationHrefAndTypeTfs = new ArrayList<TextFieldWithComboBoxEacCpf>();
		this.cpfRelationDescriptionTas = new ArrayList<TextAreaWithLanguage>();
		this.cpfRelationOrganisationNameAndIdTfs = new ArrayList<List<TextFieldWithLanguage>>();
	}

	/**
	 * Method to build the relations with other EAC-CPF files.
	 *
	 * @param builder
	 * @param cc
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildCPFRelation(PanelBuilder builder, CellConstraints cc) {
		// Define values of the part.
		if (this.eaccpf.getCpfDescription().getRelations().getCpfRelation().isEmpty()) {
			this.eaccpf.getCpfDescription().getRelations().getCpfRelation().add(new CpfRelation());
		}

		List<CpfRelation> cpfRelations = this.eaccpf.getCpfDescription().getRelations().getCpfRelation();

		for (int i = 0; i < cpfRelations.size(); i++) {
			// Set title.
			this.setNextRow();
			builder.addSeparator(this.labels.getString("eaccpf.relations.cpf.relation"), cc.xyw(1, this.rowNb, 7));
			this.setNextRow();

			CpfRelation cpfRelation = cpfRelations.get(i);

			// Create elements.
			TextFieldWithLanguage cpfRelationNameTf = null;
			JTextField cpfRelationIdentifierTf = null;
			TextFieldWithComboBoxEacCpf cpfRelationHrefAndTypeTf = null;
			TextAreaWithLanguage cpfRelationDescriptionTa = null;
			List<TextFieldWithLanguage> cpfRelationOrganisationNameAndIdTfList = new ArrayList<TextFieldWithLanguage>();

			// Recovers the values of the relation entry elements.
			String title = "";
			String other = "";
			String language = "";
			String id = "";
			List<String> agencyNameList = new ArrayList<String>();
			List<String> agencyCodeList = new ArrayList<String>();
			if (!cpfRelation.getRelationEntry().isEmpty()) {
				for (RelationEntry relationEntry : cpfRelation.getRelationEntry()) {
					// Title of the relation.
					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='title'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_TITLE)
							&& StringUtils.isNotEmpty(relationEntry.getContent())) {
						title = relationEntry.getContent();
					}
					// Id of the relation.
		            // /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='id'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_ID)
							&& StringUtils.isNotEmpty(relationEntry.getContent())) {
						id = relationEntry.getContent();
					}

					// Agency name of the relation.
					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='agencyName'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME)) {
						agencyNameList.add(relationEntry.getContent());
					}

					// Agency code of the relation.
					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='agencyCode'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE)) {
						agencyCodeList.add(relationEntry.getContent());
					}

					// Texts of relation entries without @localType.
					// This texts will be loaded in field "name"
					if(StringUtils.isNotEmpty(relationEntry.getContent()) && StringUtils.isEmpty(relationEntry.getLocalType())) {
						if (!other.isEmpty()) {
							other = other + ". ";
						}
						other = other + relationEntry.getContent();
					}
					

					// Language of the relation.
					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@lang
					if(StringUtils.isEmpty(language)
							&& StringUtils.isNotEmpty(relationEntry.getLang())) {
						language = relationEntry.getLang();
					}
				}
			} else if (StringUtils.isNotEmpty(this.firstLanguage)) {
				language = this.firstLanguage;
			}

			// Name and language of the relation.
			if (title.isEmpty()) {
				title = other;
			}
			cpfRelationNameTf = new TextFieldWithLanguage(title, language);

			// Identifier of the relation.
			cpfRelationIdentifierTf = new JTextField(id);

			// Type and link of the relation.
			String type = "";
			String link = "";
			// /eacCpf/cpfDescription/relations/cpfRelation@cpfRelationType
			if (StringUtils.isNotEmpty(cpfRelation.getCpfRelationType())) {
				type = cpfRelation.getCpfRelationType();
			}
			// /eacCpf/cpfDescription/relations/cpfRelation@type
			if (StringUtils.isEmpty(type)
					&& StringUtils.isNotEmpty(cpfRelation.getType())) {
				type = cpfRelation.getType();
			}
			// /eacCpf/cpfDescription/relations/cpfRelation@xlink:href
			if (StringUtils.isNotEmpty(cpfRelation.getHref())) {
				link = cpfRelation.getHref();
			}
			cpfRelationHrefAndTypeTf = new TextFieldWithComboBoxEacCpf(link, type, TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION, this.entityType, this.labels);

			// Descriptive note.
			// /eacCpf/cpfDescription/relations/cpfRelation/descriptiveNote
			// Currently only recovers the content of the first value.
			String descriptiveNote = "";
			if(cpfRelation.getDescriptiveNote() != null
	                && !cpfRelation.getDescriptiveNote().getP().isEmpty()
	                && StringUtils.isNotEmpty(cpfRelation.getDescriptiveNote().getP().get(0).getContent())){
				descriptiveNote = cpfRelation.getDescriptiveNote().getP().get(0).getContent();
			}
			cpfRelationDescriptionTa = new TextAreaWithLanguage(descriptiveNote, "");

			// Agency code and name of the relation.
			int size = 0;
			if (agencyNameList.size() > agencyCodeList.size()) {
				size = agencyNameList.size();
			} else {
				size = agencyCodeList.size();
			}

			if (size == 0) {
				agencyNameList.add("");
				agencyCodeList.add("");
				size = 1;
			}

			for (int j = 0; j < size; j++) {
				String agencyName = "";
				String agencyCode = "";

				if (agencyNameList.size() > j) {
					agencyName = agencyNameList.get(j);
				}

				if (agencyCodeList.size() > j) {
					agencyCode = agencyCodeList.get(j);
				}

				TextFieldWithLanguage cpfRelationOrganisationNameAndIdTf = new TextFieldWithLanguage(agencyName, language, agencyCode);
				cpfRelationOrganisationNameAndIdTfList.add(cpfRelationOrganisationNameAndIdTf);
			}

			// Add elements to the list.
			this.cpfRelationNameTfs.add(cpfRelationNameTf);
			this.cpfRelationIdentifierTfs.add(cpfRelationIdentifierTf);
			this.cpfRelationHrefAndTypeTfs.add(cpfRelationHrefAndTypeTf);
			this.cpfRelationDescriptionTas.add(cpfRelationDescriptionTa);

			this.cpfRelationOrganisationNameAndIdTfs.add(cpfRelationOrganisationNameAndIdTfList);

			// Add elements to the panel.
			// Name and language.
			builder.addLabel(this.labels.getString("eaccpf.relations.name"), cc.xy(1, this.rowNb));
			builder.add(cpfRelationNameTf.getTextField(), cc.xy(3, this.rowNb));
			builder.addLabel(this.labels.getString("eaccpf.commons.select.language"), cc.xy(5, this.rowNb));
			builder.add(cpfRelationNameTf.getLanguageBox(), cc.xy(7, this.rowNb));

			// Identity.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.cpf.identifier"), cc.xy(1, this.rowNb));
			builder.add(cpfRelationIdentifierTf, cc.xy(3, this.rowNb));

			// Link and relation type.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.link"), cc.xy(1, this.rowNb));
			builder.add(cpfRelationHrefAndTypeTf.getTextField(), cc.xy(3, this.rowNb));
			builder.addLabel(this.labels.getString("eaccpf.relations.relation.type"), cc.xy(5, this.rowNb));
			builder.add(cpfRelationHrefAndTypeTf.getComboBox(), cc.xy(7, this.rowNb));

			// Description.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.description"), cc.xy(1, this.rowNb));
			builder.add(cpfRelationDescriptionTa.getTextField(), cc.xyw(3, this.rowNb, 5));

			// Agency part.
			// Title.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.organisation"), cc.xyw(1, this.rowNb, 7));

			for (int j = 0; j < size; j++) {
				// Name and identifier.
				this.setNextRow();
				builder.addLabel(this.labels.getString("eaccpf.relations.name"), cc.xy(1, this.rowNb));
				builder.add(cpfRelationOrganisationNameAndIdTfList.get(j).getTextField(), cc.xy(3, this.rowNb));
				builder.addLabel(this.labels.getString("eaccpf.relations.identifier"), cc.xy(5, this.rowNb));
				builder.add(cpfRelationOrganisationNameAndIdTfList.get(j).getExtraField(), cc.xy(7, this.rowNb));
			}

			// Button to add new agency information.
			this.setNextRow();
			JButton addFurtherAgencyBtn = new ButtonTab(this.labels.getString("eaccpf.relations.add.further.organisation"));
			addFurtherAgencyBtn.addActionListener(new AddFurtherAgency(this.eaccpf, this.tabbedPane, this.model,AddFurtherAgency.CPF,i+this.eaccpf.getCpfDescription().getAlternativeSet().getSetComponent().size()));
			builder.add(addFurtherAgencyBtn, cc.xy(1, this.rowNb));
		}

		return builder;
	}

	/**
	 * Main method to build the section that contains the relations with other resources.
	 *
	 * @param builder
	 * @param cc
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildResourceRelationsSection(PanelBuilder builder, CellConstraints cc) {
		// Set section title.
		this.setNextRow();
		builder.addSeparator("", cc.xyw(1, this.rowNb, 7));
        this.setNextRow();
		builder.addLabel(this.labels.getString("eaccpf.relations.resources.section"), cc.xyw(1, this.rowNb, 7));
		this.setNextRow();

		// Define values of the part.
		if (this.eaccpf.getCpfDescription().getRelations().getResourceRelation().isEmpty()) {
			this.eaccpf.getCpfDescription().getRelations().getResourceRelation().add(new ResourceRelation());
		}

		List<ResourceRelation> resourceRelations = this.eaccpf.getCpfDescription().getRelations().getResourceRelation();

		this.resourceRelationNameTfs = new ArrayList<TextFieldWithLanguage>(resourceRelations.size());
		this.resourceRelationIdentifierTfs = new ArrayList<JTextField>(resourceRelations.size());
		this.resourceRelationHrefAndTypeTfs = new ArrayList<TextFieldWithComboBoxEacCpf>(resourceRelations.size());
		this.resourceRelationDescriptionTas = new ArrayList<TextAreaWithLanguage>(resourceRelations.size());
		this.resourceRelationOrganisationNameAndIdTfs = new HashMap<Integer, List<TextFieldWithLanguage>>(resourceRelations.size());

		for (int i = 0; i < resourceRelations.size(); i++) {
			// Set title.
			this.setNextRow();
			builder.addSeparator(this.labels.getString("eaccpf.relations.resource.relation"), cc.xyw(1, this.rowNb, 7));
			this.setNextRow();

			ResourceRelation resourceRelation = resourceRelations.get(i);

			// Create elements.
			TextFieldWithLanguage resourceRelationNameTf = null;
			JTextField resourceRelationIdentifierTf = null;
			TextFieldWithComboBoxEacCpf resourceRelationHrefAndTypeTf = null;
			TextAreaWithLanguage resourceRelationDescriptionTa = null;
			List<TextFieldWithLanguage> resourceRelationOrganisationNameAndIdTfsList = new ArrayList<TextFieldWithLanguage>();

			// Recovers the values of the relation entry elements.
			String title = "";
			String other = "";
			String language = "";
			String id = "";
			List<String> agencyNameList = new ArrayList<String>();
			List<String> agencyCodeList = new ArrayList<String>();
			if (!resourceRelation.getRelationEntry().isEmpty()) {
				for (RelationEntry relationEntry : resourceRelation.getRelationEntry()) {
					// Title of the relation.
					// /eacCpf/cpfDescription/relations/resourceRelation/relationEntry@localType='title'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_TITLE)
							&& StringUtils.isNotEmpty(relationEntry.getContent())) {
						title = relationEntry.getContent();
					}
					// Id of the relation.
		            // /eacCpf/cpfDescription/relations/resourceRelation/relationEntry@localType='id'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_ID)
							&& StringUtils.isNotEmpty(relationEntry.getContent())) {
						id = relationEntry.getContent();
					}

					// Agency name of the relation.
					// /eacCpf/cpfDescription/relations/resourceRelation/relationEntry@localType='agencyName'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME)) {
						agencyNameList.add(relationEntry.getContent());
					}

					// Agency code of the relation.
					// /eacCpf/cpfDescription/relations/resourceRelation/relationEntry@localType='agencyCode'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE)) {
						agencyCodeList.add(relationEntry.getContent());
					}

					// Texts of relation entries without @localType.
					// This texts will be loaded in field "name"
					if(StringUtils.isNotEmpty(relationEntry.getContent()) && StringUtils.isEmpty(relationEntry.getLocalType())) {
						if (!other.isEmpty()) {
							other = other + ". ";
						}
						other = other + relationEntry.getContent();
					}
					

					// Language of the relation.
					// /eacCpf/cpfDescription/relations/resourceRelation/relationEntry@lang
					if(StringUtils.isEmpty(language) && StringUtils.isNotEmpty(relationEntry.getLang())) {
						language = relationEntry.getLang();
					}
				}
			} else if (StringUtils.isNotEmpty(this.firstLanguage)) {
				language = this.firstLanguage;
			}

			// Name and language of the relation.
			if (title.isEmpty()) {
				title = other;
			}
			resourceRelationNameTf = new TextFieldWithLanguage(title, language);

			// Identifier of the relation.
			resourceRelationIdentifierTf = new JTextField(id);

			// Type and link of the relation.
			String type = "";
			String link = "";
			// /eacCpf/cpfDescription/relations/resourceRelation@resourceRelationType
			if (StringUtils.isNotEmpty(resourceRelation.getResourceRelationType())) {
				type = resourceRelation.getResourceRelationType();
			}
			// /eacCpf/cpfDescription/relations/resourceRelation@type
			if (StringUtils.isEmpty(type) && StringUtils.isNotEmpty(resourceRelation.getType())) {
				type = resourceRelation.getType();
			}
			// /eacCpf/cpfDescription/relations/resourceRelation@xlink:href
			if (StringUtils.isNotEmpty(resourceRelation.getHref())) {
				link = resourceRelation.getHref();
			}
			resourceRelationHrefAndTypeTf = new TextFieldWithComboBoxEacCpf(link, type, TextFieldWithComboBoxEacCpf.TYPE_RESOURCE_RELATION, this.entityType, this.labels);

			// Descriptive note.
			// /eacCpf/cpfDescription/relations/resourceRelation/descriptiveNote
			// Currently only recovers the content of the first value.
			String descriptiveNote = "";
			if(resourceRelation.getDescriptiveNote() != null && !resourceRelation.getDescriptiveNote().getP().isEmpty()
	                && StringUtils.isNotEmpty(resourceRelation.getDescriptiveNote().getP().get(0).getContent())){
				descriptiveNote = resourceRelation.getDescriptiveNote().getP().get(0).getContent();
			}
			resourceRelationDescriptionTa = new TextAreaWithLanguage(descriptiveNote, "");

			// Agency code and name of the relation.
			int size = 0;
			if (agencyNameList.size() > agencyCodeList.size()) {
				size = agencyNameList.size();
			} else {
				size = agencyCodeList.size();
			}

			if (size == 0) {
				agencyNameList.add("");
				agencyCodeList.add("");
				size = 1;
			}

			for (int j = 0; j < size; j++) {
				String agencyName = "";
				String agencyCode = "";

				if (agencyNameList.size() > j) {
					agencyName = agencyNameList.get(j);
				}

				if (agencyCodeList.size() > j) {
					agencyCode = agencyCodeList.get(j);
				}

				TextFieldWithLanguage resourceRelationOrganisationNameAndIdTf = new TextFieldWithLanguage(agencyName, language, agencyCode);
				resourceRelationOrganisationNameAndIdTfsList.add(resourceRelationOrganisationNameAndIdTf);
			}

			// Add elements to the list.
			this.resourceRelationNameTfs.add(resourceRelationNameTf);
			this.resourceRelationIdentifierTfs.add(resourceRelationIdentifierTf);
			this.resourceRelationHrefAndTypeTfs.add(resourceRelationHrefAndTypeTf);
			this.resourceRelationDescriptionTas.add(resourceRelationDescriptionTa);
			this.resourceRelationOrganisationNameAndIdTfs.put(Integer.valueOf(i), resourceRelationOrganisationNameAndIdTfsList);

			// Add elements to the panel.
			// Name and language.
			builder.addLabel(this.labels.getString("eaccpf.relations.name"), cc.xy(1, this.rowNb));
			builder.add(resourceRelationNameTf.getTextField(), cc.xy(3, this.rowNb));
			builder.addLabel(this.labels.getString("eaccpf.commons.select.language"), cc.xy(5, this.rowNb));
			builder.add(resourceRelationNameTf.getLanguageBox(), cc.xy(7, this.rowNb));

			// Identity.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.cpf.identifier"), cc.xy(1, this.rowNb));
			builder.add(resourceRelationIdentifierTf, cc.xy(3, this.rowNb));

			// Link and relation type.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.link"), cc.xy(1, this.rowNb));
			builder.add(resourceRelationHrefAndTypeTf.getTextField(), cc.xy(3, this.rowNb));
			builder.addLabel(this.labels.getString("eaccpf.relations.relation.type"), cc.xy(5, this.rowNb));
			builder.add(resourceRelationHrefAndTypeTf.getComboBox(), cc.xy(7, this.rowNb));

			// Description.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.description"), cc.xy(1, this.rowNb));
			builder.add(resourceRelationDescriptionTa.getTextField(), cc.xyw(3, this.rowNb, 5));

			// Agency part.
			// Title.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.organisation"), cc.xyw(1, this.rowNb, 7));

			for (int j = 0; j < size; j++) {
				// Name and identifier.
				this.setNextRow();
				builder.addLabel(this.labels.getString("eaccpf.relations.name"), cc.xy(1, this.rowNb));
				builder.add(resourceRelationOrganisationNameAndIdTfsList.get(j).getTextField(), cc.xy(3, this.rowNb));
				builder.addLabel(this.labels.getString("eaccpf.relations.identifier"), cc.xy(5, this.rowNb));
				builder.add(resourceRelationOrganisationNameAndIdTfsList.get(j).getExtraField(), cc.xy(7, this.rowNb));
			}

			// Button to add new agency information.
			this.setNextRow();
			JButton addFurtherAgencyBtn = new ButtonTab(this.labels.getString("eaccpf.relations.add.further.organisation"));
			addFurtherAgencyBtn.addActionListener(new AddFurtherAgency(this.eaccpf, this.tabbedPane, this.model, AddFurtherAgency.RESOURCE, i));
			builder.add(addFurtherAgencyBtn, cc.xy(1, this.rowNb));
		}

		// Button to add new resource relation.
		this.setNextRow();
		JButton addFurtherResourceBtn = new ButtonTab(this.labels.getString("eaccpf.relations.add.further.resource"));
		addFurtherResourceBtn.addActionListener(new AddFurtherResource(this.eaccpf, this.tabbedPane, this.model));
		builder.add(addFurtherResourceBtn, cc.xy(1, this.rowNb));

		return builder;
	}

	/**
	 * Main method to build the section that contains the relations with other functions.
	 *
	 * @param builder
	 * @param cc
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildFunctionsRelationsSection(PanelBuilder builder, CellConstraints cc) {
		// Set section title.
		this.setNextRow();
		builder.addSeparator("", cc.xyw(1, this.rowNb, 7));
        this.setNextRow();
		builder.addLabel(this.labels.getString("eaccpf.relations.functions.section"), cc.xyw(1, this.rowNb, 7));
		this.setNextRow();

		// Define values of the part.
		if (this.eaccpf.getCpfDescription().getRelations().getFunctionRelation().isEmpty()) {
			this.eaccpf.getCpfDescription().getRelations().getFunctionRelation().add(new FunctionRelation());
		}

		List<FunctionRelation> functionRelations = this.eaccpf.getCpfDescription().getRelations().getFunctionRelation();

		this.functionRelationNameTfs = new ArrayList<TextFieldWithLanguage>(functionRelations.size());
		this.functionRelationIdentifierTfs = new ArrayList<JTextField>(functionRelations.size());
		this.functionRelationHrefAndTypeTfs = new ArrayList<TextFieldWithComboBoxEacCpf>(functionRelations.size());
		this.functionRelationDescriptionTas = new ArrayList<TextAreaWithLanguage>(functionRelations.size());
		this.functionRelationOrganisationNameAndIdTfs = new HashMap<Integer, List<TextFieldWithLanguage>>(functionRelations.size());

		for (int i = 0; i < functionRelations.size(); i++) {
			// Set title.
			this.setNextRow();
			builder.addSeparator(this.labels.getString("eaccpf.relations.functions.relation"), cc.xyw(1, this.rowNb, 7));
			this.setNextRow();

			FunctionRelation functionRelation = functionRelations.get(i);

			// Create elements.
			TextFieldWithLanguage resourceRelationNameTf = null;
			JTextField resourceRelationIdentifierTf = null;
			TextFieldWithComboBoxEacCpf resourceRelationHrefAndTypeTf = null;
			TextAreaWithLanguage resourceRelationDescriptionTa = null;
			List<TextFieldWithLanguage> resourceRelationOrganisationNameAndIdTfsList = new ArrayList<TextFieldWithLanguage>();

			// Recovers the values of the relation entry elements.
			String title = "";
			String other = "";
			String language = "";
			String id = "";
			List<String> agencyNameList = new ArrayList<String>();
			List<String> agencyCodeList = new ArrayList<String>();
			if (!functionRelation.getRelationEntry().isEmpty()) {
				for (RelationEntry relationEntry : functionRelation.getRelationEntry()) {
					// Title of the relation.
					// /eacCpf/cpfDescription/relations/functionRelation/relationEntry@localType='title'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_TITLE)
							&& StringUtils.isNotEmpty(relationEntry.getContent())) {
						title = relationEntry.getContent();
					}
					// Id of the relation.
		            // /eacCpf/cpfDescription/relations/functionRelation/relationEntry@localType='id'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_ID)
							&& StringUtils.isNotEmpty(relationEntry.getContent())) {
						id = relationEntry.getContent();
					}

					// Agency name of the relation.
					// /eacCpf/cpfDescription/relations/functionRelation/relationEntry@localType='agencyName'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME)) {
						agencyNameList.add(relationEntry.getContent());
					}

					// Agency code of the relation.
					// /eacCpf/cpfDescription/relations/functionRelation/relationEntry@localType='agencyCode'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType()) && relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE)) {
						agencyCodeList.add(relationEntry.getContent());
					}

					// Texts of relation entries without @localType.
					// This texts will be loaded in field "name"
					if(StringUtils.isNotEmpty(relationEntry.getContent()) && StringUtils.isEmpty(relationEntry.getLocalType())) {
						if (!other.isEmpty()) {
							other = other + ". ";
						}
						other = other + relationEntry.getContent();
					}
					

					// Language of the relation.
					// /eacCpf/cpfDescription/relations/functionRelation/relationEntry@lang
					if(StringUtils.isEmpty(language) && StringUtils.isNotEmpty(relationEntry.getLang())) {
						language = relationEntry.getLang();
					}
				}
			} else if (StringUtils.isNotEmpty(this.firstLanguage)) {
				language = this.firstLanguage;
			}

			// Name and language of the relation.
			if (title.isEmpty()) {
				title = other;
			}
			resourceRelationNameTf = new TextFieldWithLanguage(title, language);

			// Identifier of the relation.
			resourceRelationIdentifierTf = new JTextField(id);

			// Type and link of the relation.
			String type = "";
			String link = "";
			// /eacCpf/cpfDescription/relations/functionRelation@functionRelationType
			if (StringUtils.isNotEmpty(functionRelation.getFunctionRelationType())) {
				type = functionRelation.getFunctionRelationType();
			}
			// /eacCpf/cpfDescription/relations/functionRelation@type
			if (StringUtils.isEmpty(type)
					&& StringUtils.isNotEmpty(functionRelation.getType())) {
				type = functionRelation.getType();
			}
			// /eacCpf/cpfDescription/relations/functionRelation@xlink:href
			if (StringUtils.isNotEmpty(functionRelation.getHref())) {
				link = functionRelation.getHref();
			}
			resourceRelationHrefAndTypeTf = new TextFieldWithComboBoxEacCpf(link, type, TextFieldWithComboBoxEacCpf.TYPE_FUNCTION_RELATION, this.entityType, this.labels);

			// Descriptive note.
			// /eacCpf/cpfDescription/relations/functionRelation/descriptiveNote
			// Currently only recovers the content of the first value.
			String descriptiveNote = "";
			if(functionRelation.getDescriptiveNote() != null && !functionRelation.getDescriptiveNote().getP().isEmpty()
	                && StringUtils.isNotEmpty(functionRelation.getDescriptiveNote().getP().get(0).getContent())){
				descriptiveNote = functionRelation.getDescriptiveNote().getP().get(0).getContent();
			}
			resourceRelationDescriptionTa = new TextAreaWithLanguage(descriptiveNote, "");

			// Agency code and name of the relation.
			int size = 0;
			if (agencyNameList.size() > agencyCodeList.size()) {
				size = agencyNameList.size();
			} else {
				size = agencyCodeList.size();
			}

			if (size == 0) {
				agencyNameList.add("");
				agencyCodeList.add("");
				size = 1;
			}

			for (int j = 0; j < size; j++) {
				String agencyName = "";
				String agencyCode = "";

				if (agencyNameList.size() > j) {
					agencyName = agencyNameList.get(j);
				}

				if (agencyCodeList.size() > j) {
					agencyCode = agencyCodeList.get(j);
				}

				TextFieldWithLanguage resourceRelationOrganisationNameAndIdTf = new TextFieldWithLanguage(agencyName, language, agencyCode);
				resourceRelationOrganisationNameAndIdTfsList.add(resourceRelationOrganisationNameAndIdTf);
			}

			// Add elements to the list.
			this.functionRelationNameTfs.add(resourceRelationNameTf);
			this.functionRelationIdentifierTfs.add(resourceRelationIdentifierTf);
			this.functionRelationHrefAndTypeTfs.add(resourceRelationHrefAndTypeTf);
			this.functionRelationDescriptionTas.add(resourceRelationDescriptionTa);
			this.functionRelationOrganisationNameAndIdTfs.put(Integer.valueOf(i), resourceRelationOrganisationNameAndIdTfsList);

			// Add elements to the panel.
			// Name and language.
			builder.addLabel(this.labels.getString("eaccpf.relations.name"), cc.xy(1, this.rowNb));
			builder.add(resourceRelationNameTf.getTextField(), cc.xy(3, this.rowNb));
			builder.addLabel(this.labels.getString("eaccpf.commons.select.language"), cc.xy(5, this.rowNb));
			builder.add(resourceRelationNameTf.getLanguageBox(), cc.xy(7, this.rowNb));

			// Identity.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.cpf.identifier"), cc.xy(1, this.rowNb));
			builder.add(resourceRelationIdentifierTf, cc.xy(3, this.rowNb));

			// Link and relation type.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.link"), cc.xy(1, this.rowNb));
			builder.add(resourceRelationHrefAndTypeTf.getTextField(), cc.xy(3, this.rowNb));
			builder.addLabel(this.labels.getString("eaccpf.relations.relation.type"), cc.xy(5, this.rowNb));
			builder.add(resourceRelationHrefAndTypeTf.getComboBox(), cc.xy(7, this.rowNb));

			// Description.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.description"), cc.xy(1, this.rowNb));
			builder.add(resourceRelationDescriptionTa.getTextField(), cc.xyw(3, this.rowNb, 5));

			// Agency part.
			// Title.
			this.setNextRow();
			builder.addLabel(this.labels.getString("eaccpf.relations.organisation"), cc.xyw(1, this.rowNb, 7));

			for (int j = 0; j < size; j++) {
				// Name and identifier.
				this.setNextRow();
				builder.addLabel(this.labels.getString("eaccpf.relations.name"), cc.xy(1, this.rowNb));
				builder.add(resourceRelationOrganisationNameAndIdTfsList.get(j).getTextField(), cc.xy(3, this.rowNb));
				builder.addLabel(this.labels.getString("eaccpf.relations.identifier"), cc.xy(5, this.rowNb));
				builder.add(resourceRelationOrganisationNameAndIdTfsList.get(j).getExtraField(), cc.xy(7, this.rowNb));
			}

			// Button to add new agency information.
			this.setNextRow();
			JButton addFurtherAgencyBtn = new ButtonTab(this.labels.getString("eaccpf.relations.add.further.organisation"));
			addFurtherAgencyBtn.addActionListener(new AddFurtherAgency(this.eaccpf, this.tabbedPane, this.model, AddFurtherAgency.FUNCTION,i));
			builder.add(addFurtherAgencyBtn, cc.xy(1, this.rowNb));
		}

		// Button to add new resource relation.
		this.setNextRow();
		JButton addFurtherFunctionBtn = new ButtonTab(this.labels.getString("eaccpf.relations.add.further.function"));
		addFurtherFunctionBtn.addActionListener(new AddFurtherFunction(this.eaccpf, this.tabbedPane, this.model));
		builder.add(addFurtherFunctionBtn, cc.xy(1, this.rowNb));

		return builder;
	}

	/**
	 * Method to build the main buttons zone.
	 *
	 * @param builder the PanelBuilder to add the buttons.
	 * @param cc the constraints to use.
	 * @return the PanelBuilder with the buttons.
	 */
	private PanelBuilder buildButtons(PanelBuilder builder, CellConstraints cc) {
		// Row for the next and previous tab buttons.
		setNextRow();
		builder.addSeparator("", cc.xyw(1, this.rowNb, 7));
		setNextRow();
		JButton previousTabBtn = new ButtonTab(labels.getString("eaccpf.commons.previousTab"));
		builder.add(previousTabBtn, cc.xy (1, rowNb));
		previousTabBtn.addActionListener(new ChangeTabBtnAction(this.eaccpf, this.tabbedPane, this.model, false));

		JButton nextTabBtn = new ButtonTab(this.labels.getString("eaccpf.commons.nextTab"));
		builder.add(nextTabBtn, cc.xy (5, this.rowNb));
		nextTabBtn.addActionListener(new ChangeTabBtnAction(this.eaccpf, this.tabbedPane, this.model, true));

		// Row for exit and save buttons.
		setNextRow();
		JButton exitBtn = new ButtonTab(this.labels.getString("eaccpf.commons.exit"));
		builder.add(exitBtn, cc.xy(1, this.rowNb));
		exitBtn.addActionListener(new ExitBtnAction(this.eaccpf, this.tabbedPane, this.model));
		JButton saveBtn = new ButtonTab(labels.getString("eaccpf.commons.save"));
		builder.add(saveBtn, cc.xy (5, this.rowNb));
		saveBtn.addActionListener(new SaveBtnAction(this.eaccpf, this.tabbedPane, this.model));

		return builder;
	}

	/**
	 * Method that removes the existing "ChangeTabListener".
	 */
	private void removeChangeListener() {
		// Check the current "ChangeListeners" and remove the non desired ones.
		ChangeListener[] changeListeners = this.tabbedPane.getChangeListeners();
		List<ChangeListener> changeListenerList = new LinkedList<ChangeListener>();
		for (int i = 0; i < changeListeners.length; i++) {
			ChangeListener changeListener = changeListeners[i];

			if (changeListener instanceof ChangeTabListener) {
				changeListenerList.add(changeListener);
			}
		}

		if (changeListenerList != null) {
			for (int i = 0; i < changeListenerList.size(); i++) {
				this.tabbedPane.removeChangeListener(changeListenerList.get(i));
			}
		}
	}

	/**
	 * Class to performs the addition of new row for the organization name and
	 * code in CPF relations section if the previous values are filled.
	 */
	public class AddFurtherAgency extends UpdateEacCpfObject {
		/** CONSTANTS **/
		public static final String CPF = "cpf-kind";
		public static final String RESOURCE = "resource-kind";
		public static final String FUNCTION = "function-kind";
		
		private int currentRelation; //figure relation
		private String relationKind; //kind of relation, {cpf,resource,function}

		/**
		 * Constructor.
		 *
		 * @param eaccpf
		 * @param tabbedPane
		 * @param model
		 * @param isNextTab
		 * @param counter
		 */
		public AddFurtherAgency(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model, String relationKind, int counter) {
			super(eaccpf, tabbedPane, model);
			this.currentRelation = counter;
			this.relationKind = relationKind;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(false);
			} catch (EacCpfFormException e) {
				reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
			}

			boolean emptyName = false;
			boolean emptyCode = false;
			
			//decide which case is for each kind of relation
			if(this.relationKind.equalsIgnoreCase(CPF)){
				List<TextFieldWithLanguage> cpfRelationOrganisationNameAndIdTf = cpfRelationOrganisationNameAndIdTfs.get(this.currentRelation);
				for (int i = 0; !emptyName && !emptyCode && i < cpfRelationOrganisationNameAndIdTf.size(); i++) {
					if (StringUtils.isEmpty(cpfRelationOrganisationNameAndIdTf.get(i).getTextValue())
							|| StringUtils.isEmpty(cpfRelationOrganisationNameAndIdTf.get(i).getExtraValue())) {
						JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.relations.error.empty.agency"));
					}
					if (StringUtils.isEmpty(cpfRelationOrganisationNameAndIdTf.get(i).getTextValue())) {
						emptyName = true;
					}
					if (StringUtils.isEmpty(cpfRelationOrganisationNameAndIdTf.get(i).getExtraValue())) {
						emptyCode = true;
					}
				}
				//Check the content and add the elements.
				if (eaccpf.getCpfDescription() == null) {
					eaccpf.setCpfDescription(new CpfDescription());
				}
				if (eaccpf.getCpfDescription().getRelations() == null) {
					eaccpf.getCpfDescription().setRelations(new Relations());
				}
				if(cpfRelationHrefAndTypeTfs.get(this.currentRelation).getComboBoxValue(TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION, entityType).equals(TextFieldWithComboBoxEacCpf.SELECTED_ALTERNATIVE_SET_VALUE)){ //setComponent part
					if (((emptyName && emptyCode)|| (!emptyName && !emptyCode)) && eaccpf.getCpfDescription().getAlternativeSet().getSetComponent().size() > this.currentRelation) {
						ComponentEntry componentEntryName = new ComponentEntry();
						componentEntryName.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME);
						eaccpf.getCpfDescription().getAlternativeSet().getSetComponent().get(this.currentRelation).getComponentEntry().add(componentEntryName);
						ComponentEntry componentEntryCode = new ComponentEntry();
						componentEntryCode.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE);
						eaccpf.getCpfDescription().getAlternativeSet().getSetComponent().get(this.currentRelation).getComponentEntry().add(componentEntryCode);
					}
				}else{ //cpf part
					int targetIndex = (eaccpf.getCpfDescription().getAlternativeSet()!=null && eaccpf.getCpfDescription().getAlternativeSet().getSetComponent()!=null)?(this.currentRelation - eaccpf.getCpfDescription().getAlternativeSet().getSetComponent().size()):0;
					if (((emptyName && emptyCode) || (!emptyName && !emptyCode))
							&& eaccpf.getCpfDescription().getRelations().getCpfRelation().size() > targetIndex) { //this part is necessary because reordenation can change the targetIndexes and needs to be discarded until targetIndex is refreshed
						RelationEntry relationEntryName = new RelationEntry();
						relationEntryName.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME);
						eaccpf.getCpfDescription().getRelations().getCpfRelation().get(targetIndex).getRelationEntry().add(relationEntryName);
						RelationEntry relationEntryCode = new RelationEntry();
						relationEntryCode.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE);
						eaccpf.getCpfDescription().getRelations().getCpfRelation().get(targetIndex).getRelationEntry().add(relationEntryCode);
					}
				}
			}else if(this.relationKind.equalsIgnoreCase(RESOURCE)){
				List<TextFieldWithLanguage> resourceRelationOrganisationNameAndIdTf = resourceRelationOrganisationNameAndIdTfs.get(this.currentRelation);
				for (int i = 0; !emptyName && !emptyCode && i < resourceRelationOrganisationNameAndIdTf.size(); i++) {
					if (StringUtils.isEmpty(resourceRelationOrganisationNameAndIdTf.get(i).getTextValue())
							|| StringUtils.isEmpty(resourceRelationOrganisationNameAndIdTf.get(i).getExtraValue())) {
						JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.relations.error.empty.agency"));
					}
					if (StringUtils.isEmpty(resourceRelationOrganisationNameAndIdTf.get(i).getTextValue())) {
						emptyName = true;
					}
					if (StringUtils.isEmpty(resourceRelationOrganisationNameAndIdTf.get(i).getExtraValue())) {
						emptyCode = true;
					}
				}
				// Check the content and add the elements.
				if (eaccpf.getCpfDescription() == null) {
					eaccpf.setCpfDescription(new CpfDescription());
				}
				if (eaccpf.getCpfDescription().getRelations() == null) {
					eaccpf.getCpfDescription().setRelations(new Relations());
				}
				if (((emptyName && emptyCode)
						|| (!emptyName && !emptyCode))
						&& eaccpf.getCpfDescription().getRelations().getResourceRelation().size() > this.currentRelation) {
					RelationEntry relationEntryName = new RelationEntry();
					relationEntryName.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME);
					eaccpf.getCpfDescription().getRelations().getResourceRelation().get(this.currentRelation).getRelationEntry().add(relationEntryName);
					RelationEntry relationEntryCode = new RelationEntry();
					relationEntryCode.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE);
					eaccpf.getCpfDescription().getRelations().getResourceRelation().get(this.currentRelation).getRelationEntry().add(relationEntryCode);
				}
			}else if(this.relationKind.equalsIgnoreCase(FUNCTION)){
				List<TextFieldWithLanguage> functionRelationOrganisationNameAndIdTf = functionRelationOrganisationNameAndIdTfs.get(this.currentRelation);
				for (int i = 0; !emptyName && !emptyCode && i < functionRelationOrganisationNameAndIdTf.size(); i++) {
					if (StringUtils.isEmpty(functionRelationOrganisationNameAndIdTf.get(i).getTextValue())
							|| StringUtils.isEmpty(functionRelationOrganisationNameAndIdTf.get(i).getExtraValue())) {
						JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.relations.error.empty.agency"));
					}
					if (StringUtils.isEmpty(functionRelationOrganisationNameAndIdTf.get(i).getTextValue())) {
						emptyName = true;
					}
					if (StringUtils.isEmpty(functionRelationOrganisationNameAndIdTf.get(i).getExtraValue())) {
						emptyCode = true;
					}
				}
				// Check the content and add the elements.
				if (eaccpf.getCpfDescription() == null) {
					eaccpf.setCpfDescription(new CpfDescription());
				}
				if (eaccpf.getCpfDescription().getRelations() == null) {
					eaccpf.getCpfDescription().setRelations(new Relations());
				}
				if (((emptyName && emptyCode)
						|| (!emptyName && !emptyCode))
						&& eaccpf.getCpfDescription().getRelations().getFunctionRelation().size() > this.currentRelation) {
					RelationEntry relationEntryName = new RelationEntry();
					relationEntryName.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME);
					eaccpf.getCpfDescription().getRelations().getFunctionRelation().get(this.currentRelation).getRelationEntry().add(relationEntryName);
					RelationEntry relationEntryCode = new RelationEntry();
					relationEntryCode.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE);
					eaccpf.getCpfDescription().getRelations().getFunctionRelation().get(this.currentRelation).getRelationEntry().add(relationEntryCode);
				}
			}
			//finally reload panel
			reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
		}
	}
	
	/**
	 * Class to performs the addition of new resource relation in the relations tab
	 * section if the previous values are filled.
	 */
	public class AddFurtherResource extends UpdateEacCpfObject {

		public AddFurtherResource(EacCpf eacCpf, JTabbedPane tabbedPane,ProfileListModel model) {
			super(eacCpf, tabbedPane, model);
			this.saveResources = false;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(false);
			} catch (EacCpfFormException e) {
				reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
			}

			boolean empty = false;
			List<TextFieldWithLanguage> resourceRelationNameTfsList = resourceRelationNameTfs;
			for (int i = 0; !empty && i < resourceRelationNameTfsList.size(); i++) {
				if (StringUtils.isEmpty(resourceRelationNameTfsList.get(i).getTextValue())) {
					JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.relations.error.empty.name"));
					empty = true;
				}
			}
			if(!empty){
				for(int i=0;i<resourceRelationHrefAndTypeTfs.size();i++){
					if (resourceRelationHrefAndTypeTfs.get(i).getComboBox().getSelectedItem().equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
						JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.relations.error.empty.relation"));
						empty = true;
					}
				}
			}

			// Check the content and add the elements.
			if (eaccpf.getCpfDescription() == null) {
				eaccpf.setCpfDescription(new CpfDescription());
			}
			if (eaccpf.getCpfDescription().getRelations() == null) {
				eaccpf.getCpfDescription().setRelations(new Relations());
			}
			if(!empty){
				eaccpf.getCpfDescription().getRelations().getResourceRelation().add(new ResourceRelation());
			}
			reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
		}
		
	}
	
	/**
	 * Class to performs the addition of new function relation in the relations tab
	 * section if the previous values are filled.
	 */
	public class AddFurtherFunction extends UpdateEacCpfObject {

		public AddFurtherFunction(EacCpf eacCpf, JTabbedPane tabbedPane,ProfileListModel model) {
			super(eacCpf, tabbedPane, model);
			this.saveFunctions = false;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(false);
			} catch (EacCpfFormException e) {
				reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
			}

			boolean empty = false;
			List<TextFieldWithLanguage> functionRelationNameTfsList = functionRelationNameTfs;
			for (int i = 0; !empty && i < functionRelationNameTfsList.size(); i++) {
				if (StringUtils.isEmpty(functionRelationNameTfsList.get(i).getTextValue())) {
					JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.relations.error.empty.name"));
					empty = true;
				}
			}
			if(!empty){
				for(int i=0;i<functionRelationHrefAndTypeTfs.size();i++){
					if (functionRelationHrefAndTypeTfs.get(i).getComboBox().getSelectedItem().equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
						JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.relations.error.empty.relation"));
						empty = true;
					}
				}
			}

			// Check the content and add the elements.
			if (eaccpf.getCpfDescription() == null) {
				eaccpf.setCpfDescription(new CpfDescription());
			}
			if (eaccpf.getCpfDescription().getRelations() == null) {
				eaccpf.getCpfDescription().setRelations(new Relations());
			}
			if(!empty){
				eaccpf.getCpfDescription().getRelations().getFunctionRelation().add(new FunctionRelation());
			}

			reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
		}
		
	}

	/**
	 * Class to performs the addition of new CPF relation in the CPF relations
	 * section if the previous values are filled.
	 */
	public class AddFurtherCPF extends UpdateEacCpfObject {
		
		public AddFurtherCPF(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model) {
			super(eaccpf, tabbedPane, model);
			this.saveCPF = false;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(false);
			} catch (EacCpfFormException e) {
				reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
			}

			boolean empty = false;
			List<TextFieldWithLanguage> cpfRelationNameTfsList = cpfRelationNameTfs;
			for (int i = 0; !empty && i < cpfRelationNameTfsList.size(); i++) {
				if (StringUtils.isEmpty(cpfRelationNameTfsList.get(i).getTextValue())) {
					JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.relations.error.empty.name"));
					empty = true;
				}
			}
			if(!empty){
				for(int i=0;i<cpfRelationHrefAndTypeTfs.size();i++){
					if (cpfRelationHrefAndTypeTfs.get(i).getComboBox().getSelectedItem().equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
						JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.relations.error.empty.relation"));
						empty = true;
					}
				}
			}

			// Check the content and add the elements.
			if (eaccpf.getCpfDescription() == null) {
				eaccpf.setCpfDescription(new CpfDescription());
			}
			if (eaccpf.getCpfDescription().getRelations() == null) {
				eaccpf.getCpfDescription().setRelations(new Relations());
			}
			if(!empty){
				eaccpf.getCpfDescription().getRelations().getCpfRelation().add(new CpfRelation());
			}

			reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
		}
	}
	
	protected boolean checkStartTabFields() {
		boolean state = true;
		if(firstLanguage==null || firstLanguage.isEmpty() || firstLanguage.equals("---")){
			state = false;
			JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.commons.error.emptylanguage"));
		}else if(firstScript==null || firstScript.isEmpty() || firstScript.equals("---")){
			state = false;
			JOptionPane.showMessageDialog(this.tabbedPane, labels.getString("eaccpf.commons.error.emptyscript"));
		}
		return state;
	}

	/**
	 * Class to performs the actions when the user clicks on button save.
	 */
	public class SaveBtnAction extends UpdateEacCpfObject {
		SaveBtnAction(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model) {
			super(eaccpf, tabbedPane, model);
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(true);
				if(checkStartTabFields()){
					super.saveFile(eaccpf.getControl().getRecordId().getValue());
				}
				reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
			} catch (EacCpfFormException e) {
				reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
			}
		}
	}

	/**
	 * Class to performs the actions when the user clicks on button for next or previous tab.
	 */
	public class ChangeTabBtnAction extends UpdateEacCpfObject {
		private boolean isNextTab;
		ChangeTabBtnAction(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model, boolean isNextTab) {
			super(eaccpf, tabbedPane, model);
			this.isNextTab = isNextTab;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(false);
				removeChangeListener();

				if (this.isNextTab) {
					reloadTabbedPanel(new EacCpfControlPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 3);
				} else {
					reloadTabbedPanel(new EacCpfDescriptionPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 1);
				}
			} catch (EacCpfFormException e) {
				reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
			}
		}
	}

	/**
	 * Class for update the JABX EAC-CPF object.
	 */
	public abstract class UpdateEacCpfObject extends DefaultBtnAction {
		
		protected boolean saveCPF = true;
		protected boolean saveResources = true;
		protected boolean saveFunctions = true;
		
		UpdateEacCpfObject(EacCpf eacCpf, JTabbedPane tabbedPane, ProfileListModel model) {
			super(eacCpf, tabbedPane, model);
		}

		protected void updateJAXBObject(boolean save) throws EacCpfFormException {
			errors = new ArrayList<String>();
//			boolean hasChanged = false;

			// Define values of the section "Relation".
			if (this.eaccpf.getCpfDescription() == null) {
				this.eaccpf.setCpfDescription(new CpfDescription());
			}
			if (this.eaccpf.getCpfDescription().getRelations() == null) {
				this.eaccpf.getCpfDescription().setRelations(new Relations());
			}

			Relations relations = this.eaccpf.getCpfDescription().getRelations();

			// Call method to save the information about CPF relations.
			this.updateCPFRelations(relations);
			// Call method to save the information about Resource relations.
			updateResourceRelations(relations);
			// Call method to save the information about Function relations.
			updateFunctionRelations(relations);

			if(!errors.isEmpty()) {
				throw new EacCpfFormException("Errors in validation of EAC-CPF");
			}
		}

		private void updateFunctionRelations(Relations relations) {
			// Clear the current function relations list.
			relations.getFunctionRelation().clear();
			// Check the number of relations.
			for (int i = 0; i < functionRelationNameTfs.size(); i++) {
				boolean hasChanged = false;
				FunctionRelation functionRelation = new FunctionRelation();
				// Try to recover the language of the relation (@lang).
				TextFieldWithLanguage functionRelationNameTf = functionRelationNameTfs.get(i);
				String language = null;
				if (StringUtils.isNotEmpty(functionRelationNameTf.getLanguage())
						&& !functionRelationNameTf.getLanguage().equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					language = functionRelationNameTf.getLanguage();
				}
				// Try to recover the type of the relation.
				// /eacCpf/cpfDescription/relations/functionRelation@cpfRelationType
				// /eacCpf/cpfDescription/relations/functionRelation@type
				TextFieldWithComboBoxEacCpf functionRelationHrefAndTypeTf = functionRelationHrefAndTypeTfs.get(i);
				String type = functionRelationHrefAndTypeTf.getComboBoxValue(TextFieldWithComboBoxEacCpf.TYPE_FUNCTION_RELATION, entityType);
				if (StringUtils.isNotEmpty(type) && !type.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					functionRelation.setFunctionRelationType(type);
					hasChanged = true;
				}
				// Try to recover the href of the relation.
				// /eacCpf/cpfDescription/relations/functionRelation@xlink:href
				String link = functionRelationHrefAndTypeTf.getTextFieldValue();
				if (StringUtils.isNotEmpty(link) && !link.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					functionRelation.setHref(link);
					functionRelation.setType(SIMPLE);
					hasChanged = true;
				}
				// Try to recover the descriptive note of the relation.
				// /eacCpf/cpfDescription/relations/functionRelation/descriptiveNote
				TextAreaWithLanguage functionRelationDescriptionTa = functionRelationDescriptionTas.get(i);
				String description = functionRelationDescriptionTa.getTextValue();
				if (StringUtils.isNotEmpty(description)) {
					DescriptiveNote descriptiveNote = new DescriptiveNote();
					P p = new P();
					p.setContent(description);
					if (StringUtils.isNotEmpty(language)) {
						p.setLang(language);
					}
					descriptiveNote.getP().add(p);
					functionRelation.setDescriptiveNote(descriptiveNote);
					hasChanged = true;
				}
				// Try to recover the name of the relation.
				// /eacCpf/cpfDescription/relations/functionRelation/relationEntry@localType='title'
				String name = functionRelationNameTf.getTextValue();
				if (StringUtils.isNotEmpty(name)) {
					RelationEntry relationEntry = new RelationEntry();
					relationEntry.setContent(name);
					relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_TITLE);
					if (StringUtils.isNotEmpty(language)) {
						relationEntry.setLang(language);
					}
					functionRelation.getRelationEntry().add(relationEntry);
					hasChanged = true;
				}
				// Try to recover the identifier of the relation.
				// /eacCpf/cpfDescription/relations/functionRelation/relationEntry@localType='id'
				String id = functionRelationIdentifierTfs.get(i).getText();
				if (StringUtils.isNotEmpty(id)) {
					RelationEntry relationEntry = new RelationEntry();
					relationEntry.setContent(id);
					relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_ID);
					if (StringUtils.isNotEmpty(language)) {
						relationEntry.setLang(language);
					}
					functionRelation.getRelationEntry().add(relationEntry);
					hasChanged = true;
				}
				// Try to recover the agency name and agency code of the relation.
				List<TextFieldWithLanguage> cpfRelationOrganisationNameAndIdTfsList = functionRelationOrganisationNameAndIdTfs.get(i);
				if(cpfRelationOrganisationNameAndIdTfsList!=null){
					for (TextFieldWithLanguage cpfRelationOrganisationNameAndIdTf : cpfRelationOrganisationNameAndIdTfsList) {
						// /eacCpf/cpfDescription/relations/functionRelation/relationEntry@localType='agencyName'
						String agencyName = cpfRelationOrganisationNameAndIdTf.getTextValue();
						if (StringUtils.isNotEmpty(agencyName)) {
							RelationEntry relationEntry = new RelationEntry();
							relationEntry.setContent(agencyName);
							relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME);
							if (StringUtils.isNotEmpty(language)) {
								relationEntry.setLang(language);
							}
							functionRelation.getRelationEntry().add(relationEntry);
							hasChanged = true;
						}
						// /eacCpf/cpfDescription/relations/functionRelation/relationEntry@localType='agencyCode'
						String agencyCode = cpfRelationOrganisationNameAndIdTf.getExtraValue();
						if (StringUtils.isNotEmpty(agencyCode)) {
							RelationEntry relationEntry = new RelationEntry();
							relationEntry.setContent(agencyCode);
							relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE);
							if (StringUtils.isNotEmpty(language)) {
								relationEntry.setLang(language);
							}
							functionRelation.getRelationEntry().add(relationEntry);
							hasChanged = true;
						}
					}
				}
				if (hasChanged || (!saveFunctions && i+1 == functionRelationNameTfs.size())) {
					relations.getFunctionRelation().add(functionRelation);
				}
			}
		}

		private void updateResourceRelations(Relations relations) {
			// Clear the current resource relations list.
			relations.getResourceRelation().clear();
			// Check the number of relations.
			for (int i = 0; i < resourceRelationNameTfs.size(); i++) {
				boolean hasChanged = false;
				ResourceRelation resourceRelation = new ResourceRelation();
				// Try to recover the language of the relation (@lang).
				TextFieldWithLanguage resourceRelationNameTf = resourceRelationNameTfs.get(i);
				String language = null;
				if (StringUtils.isNotEmpty(resourceRelationNameTf.getLanguage())
						&& !resourceRelationNameTf.getLanguage().equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					language = resourceRelationNameTf.getLanguage();
				}
				// Try to recover the type of the relation.
				// /eacCpf/cpfDescription/relations/resourceRelation@cpfRelationType
				// /eacCpf/cpfDescription/relations/resourceRelation@type
				TextFieldWithComboBoxEacCpf resourceRelationHrefAndTypeTf = resourceRelationHrefAndTypeTfs.get(i);
				String type = resourceRelationHrefAndTypeTf.getComboBoxValue(TextFieldWithComboBoxEacCpf.TYPE_RESOURCE_RELATION, entityType);
				if (StringUtils.isNotEmpty(type) && !type.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					resourceRelation.setResourceRelationType(type);
					hasChanged = true;
				}
				// Try to recover the href of the relation.
				// /eacCpf/cpfDescription/relations/resourceRelation@xlink:href
				String link = resourceRelationHrefAndTypeTf.getTextFieldValue();
				if (StringUtils.isNotEmpty(link) && !link.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					resourceRelation.setHref(link);
					resourceRelation.setType(SIMPLE);
					hasChanged = true;
				}
				// Try to recover the descriptive note of the relation.
				// /eacCpf/cpfDescription/relations/resourceRelation/descriptiveNote
				TextAreaWithLanguage resourceRelationDescriptionTa = resourceRelationDescriptionTas.get(i);
				String description = resourceRelationDescriptionTa.getTextValue();
				if (StringUtils.isNotEmpty(description)) {
					DescriptiveNote descriptiveNote = new DescriptiveNote();
					P p = new P();
					p.setContent(description);
					if (StringUtils.isNotEmpty(language)) {
						p.setLang(language);
					}
					descriptiveNote.getP().add(p);
					resourceRelation.setDescriptiveNote(descriptiveNote);
					hasChanged = true;
				}
				// Try to recover the name of the relation.
				// /eacCpf/cpfDescription/relations/resourceRelation/relationEntry@localType='title'
				String name = resourceRelationNameTf.getTextValue();
				if (StringUtils.isNotEmpty(name)) {
					RelationEntry relationEntry = new RelationEntry();
					relationEntry.setContent(name);
					relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_TITLE);
					if (StringUtils.isNotEmpty(language)) {
						relationEntry.setLang(language);
					}
					resourceRelation.getRelationEntry().add(relationEntry);
					hasChanged = true;
				}
				// Try to recover the identifier of the relation.
				// /eacCpf/cpfDescription/relations/resourceRelation/relationEntry@localType='id'
				String id = resourceRelationIdentifierTfs.get(i).getText();
				if (StringUtils.isNotEmpty(id)) {
					RelationEntry relationEntry = new RelationEntry();
					relationEntry.setContent(id);
					relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_ID);
					if (StringUtils.isNotEmpty(language)) {
						relationEntry.setLang(language);
					}
					resourceRelation.getRelationEntry().add(relationEntry);
					hasChanged = true;
				}
				// Try to recover the agency name and agency code of the relation.
				List<TextFieldWithLanguage> resourceRelationOrganisationNameAndIdTfsList = resourceRelationOrganisationNameAndIdTfs.get(i);
				if(resourceRelationOrganisationNameAndIdTfsList!=null){
					for (TextFieldWithLanguage resourceRelationOrganisationNameAndIdTf : resourceRelationOrganisationNameAndIdTfsList) {
						// /eacCpf/cpfDescription/relations/resourceRelation/relationEntry@localType='agencyName'
						String agencyName = resourceRelationOrganisationNameAndIdTf.getTextValue();
						if (StringUtils.isNotEmpty(agencyName)) {
							RelationEntry relationEntry = new RelationEntry();
							relationEntry.setContent(agencyName);
							relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME);
							if (StringUtils.isNotEmpty(language)) {
								relationEntry.setLang(language);
							}
							resourceRelation.getRelationEntry().add(relationEntry);
							hasChanged = true;
						}
						// /eacCpf/cpfDescription/relations/resourceRelation/relationEntry@localType='agencyCode'
						String agencyCode = resourceRelationOrganisationNameAndIdTf.getExtraValue();
						if (StringUtils.isNotEmpty(agencyCode)) {
							RelationEntry relationEntry = new RelationEntry();
							relationEntry.setContent(agencyCode);
							relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE);
							if (StringUtils.isNotEmpty(language)) {
								relationEntry.setLang(language);
							}
							resourceRelation.getRelationEntry().add(relationEntry);
							hasChanged = true;
						}
					}
				}
				if (hasChanged || (!saveResources && i+1 == resourceRelationNameTfs.size())) {
					relations.getResourceRelation().add(resourceRelation);
				}
			}
		}

		/**
		 * Method to update the CPF relations of the apeEAC-CPF file.
		 *
		 * @param relations
		 * @return boolean value.
		 */
		private boolean updateCPFRelations(Relations relations) {
			boolean hasChanged = false;

			// Clear the current CPF relations list.
			relations.getCpfRelation().clear();
			boolean found = false;
			for (int i = 0; !found && i < cpfRelationOrganisationNameAndIdTfs.size(); i++) {
				TextFieldWithComboBoxEacCpf cpfRelationHrefAndTypeTf = cpfRelationHrefAndTypeTfs.get(i);
				String type = cpfRelationHrefAndTypeTf.getComboBoxValue(TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION, entityType);
				if(type.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.SELECTED_ALTERNATIVE_SET_VALUE)){
					found = true;
				}
			}
			if(found){
				if(this.eaccpf.getCpfDescription().getAlternativeSet()==null){
					this.eaccpf.getCpfDescription().setAlternativeSet(new AlternativeSet());
				}else{
					this.eaccpf.getCpfDescription().getAlternativeSet().getSetComponent().clear();
				}
			}else{
				this.eaccpf.getCpfDescription().setAlternativeSet(null);
			}

			for (int i = 0; i < cpfRelationOrganisationNameAndIdTfs.size(); i++) {
				//control flag, decide if it's an old part for alternativeSet
				boolean continueWithEacCpfPart = false; 
				TextFieldWithComboBoxEacCpf cpfRelationHrefAndTypeTf = cpfRelationHrefAndTypeTfs.get(i);
				String type = cpfRelationHrefAndTypeTf.getComboBoxValue(TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION, entityType);
				if (StringUtils.isNotEmpty(type) && !type.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.SELECTED_ALTERNATIVE_SET_VALUE)) {
					continueWithEacCpfPart = true; //controls if a eaccpf part is selected or if needs jump to alternativeSet part
				}
				//now continue if necesary
				if(continueWithEacCpfPart || StringUtils.isEmpty(type)){
					relations = buildCPFRelationPart(relations,i); //builds the old CpfRelation part
				}else{
					
					hasChanged = buildAlternativeSet(i); //builds the new alternativeSet, it works with global 'eaccpf' -> this.eaccpf.getCpfDescription().getAlternativeSet().getSetComponent()
				}
			}
			
			return hasChanged;
		}
		
		/**
		 * Builds an <setComponent> into <alternativeSet>
		 * Based on new encoding in apeEAC-CPF (08.04.2015).
		 * See new_encoding_same_entity-20140508.doc for more details into #836
		 */
		private boolean buildAlternativeSet(int currentIndex) {
			
			boolean hasChanged = false;
			SetComponent setComponent = new SetComponent();
			
			// Try to recover the language of the relation (@lang).
			TextFieldWithLanguage relationNameTf = cpfRelationNameTfs.get(currentIndex);
			String language = null;
			if (StringUtils.isNotEmpty(relationNameTf.getLanguage()) && !relationNameTf.getLanguage().equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
				language = relationNameTf.getLanguage();
			}

			// Try to recover the type of the relation, always should be the same
			// /eacCpf/alternativeSet/setComponent@type
			TextFieldWithComboBoxEacCpf cpfRelationHrefAndTypeTf = cpfRelationHrefAndTypeTfs.get(currentIndex);
			// Try to recover the href of the relation.
			// /eacCpf/cpfDescription/setComponent@xlink:href
			String link = cpfRelationHrefAndTypeTf.getTextFieldValue();
			if (StringUtils.isNotEmpty(link) && !link.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
				setComponent.setHref(link);
				setComponent.setType(SIMPLE);
				hasChanged = true;
			}
			// textarea with combobox 
			// /eacCpf/cpfDescription/alternativeSet/setComponent/descriptiveNote/p
			// /eacCpf/cpfDescription/alternativeSet/setComponent/descriptiveNote/p@lang
			TextAreaWithLanguage cpfRelationDescriptionTa = cpfRelationDescriptionTas.get(currentIndex);
			String description = cpfRelationDescriptionTa.getTextValue();
			if (StringUtils.isNotEmpty(description)) {
				DescriptiveNote descriptiveNote = new DescriptiveNote();
				P p = new P();
				p.setContent(description);
				if (StringUtils.isNotEmpty(language)) {
					p.setLang(language);
				}
				descriptiveNote.getP().add(p);
				setComponent.setDescriptiveNote(descriptiveNote);
				hasChanged = true;
			}
			
			// Try to recover the name of the relation.
			// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='title'
			String name = relationNameTf.getTextValue();
			if (StringUtils.isNotEmpty(name)) {
				ComponentEntry componentEntry = new ComponentEntry();
				componentEntry.setContent(name);
				componentEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_TITLE);
				if (StringUtils.isNotEmpty(language)) {
					componentEntry.setLang(language);
				}
				setComponent.getComponentEntry().add(componentEntry);
				hasChanged = true;
			}
			
			// Try to recover the identifier of the relation.
			// /eacCpf/cpfDescription/alternativeSet/setComponent/componentEntry@localType='id'
			String id = cpfRelationIdentifierTfs.get(currentIndex).getText();
			if (StringUtils.isNotEmpty(id)) {
				ComponentEntry componentEntry = new ComponentEntry();
				componentEntry.setContent(id);
				componentEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_ID);
				if (StringUtils.isNotEmpty(language)) {
					componentEntry.setLang(language);
				}
				setComponent.getComponentEntry().add(componentEntry);
				hasChanged = true;
			}
			
			// Try to recover the agency name and agency code of the relation.
			
			List<TextFieldWithLanguage> cpfRelationOrganisationNameAndIdTfsList = null;
			
			cpfRelationOrganisationNameAndIdTfsList = cpfRelationOrganisationNameAndIdTfs.get(currentIndex);

			if(cpfRelationOrganisationNameAndIdTfsList!=null){
				for (TextFieldWithLanguage cpfRelationOrganisationNameAndIdTf : cpfRelationOrganisationNameAndIdTfsList) {
					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='agencyName'
					String agencyName = cpfRelationOrganisationNameAndIdTf.getTextValue();
					if (StringUtils.isNotEmpty(agencyName)) {
						ComponentEntry componentEntry = new ComponentEntry();
						componentEntry.setContent(agencyName);
						componentEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME);
						if (StringUtils.isNotEmpty(language)) {
							componentEntry.setLang(language);
						}
						setComponent.getComponentEntry().add(componentEntry);
						hasChanged = true;
					}

					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='agencyCode'
					String agencyCode = cpfRelationOrganisationNameAndIdTf.getExtraValue();
					if (StringUtils.isNotEmpty(agencyCode)) {
						ComponentEntry componentEntry = new ComponentEntry();
						componentEntry.setContent(agencyCode);
						componentEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE);
						if (StringUtils.isNotEmpty(language)) {
							componentEntry.setLang(language);
						}
						setComponent.getComponentEntry().add(componentEntry);
						hasChanged = true;
					}
				}
			}
			
			if(hasChanged || (!saveCPF && currentIndex+1 == cpfRelationOrganisationNameAndIdTfs.size())){
				this.eaccpf.getCpfDescription().getAlternativeSet().getSetComponent().add(setComponent);
			}
			return hasChanged;
		}

		/**
		 * Used to build a CpfRelation object.
		 * based on old encoding in apeEAC-CPF.
		 * See new_encoding_same_entity-20140508.doc for more details into #836
		 */
		private Relations buildCPFRelationPart(Relations relations, int currentIndex) {
			
			boolean hasChanged = false;
			
			CpfRelation cpfRelation = new CpfRelation();

			// Try to recover the language of the relation (@lang).
			TextFieldWithLanguage cpfRelationNameTf = cpfRelationNameTfs.get(currentIndex);
			String language = null;
			if (StringUtils.isNotEmpty(cpfRelationNameTf.getLanguage()) && !cpfRelationNameTf.getLanguage().equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
				language = cpfRelationNameTf.getLanguage();
			}

			// Try to recover the type of the relation.
			// /eacCpf/cpfDescription/relations/cpfRelation@cpfRelationType
			// /eacCpf/cpfDescription/relations/cpfRelation@type
			TextFieldWithComboBoxEacCpf cpfRelationHrefAndTypeTf = cpfRelationHrefAndTypeTfs.get(currentIndex);
			String type = cpfRelationHrefAndTypeTf.getComboBoxValue(TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION, entityType);
			if (StringUtils.isNotEmpty(type) && !type.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
				cpfRelation.setCpfRelationType(type);
				hasChanged = true;
			}

			// Try to recover the href of the relation.
			// /eacCpf/cpfDescription/relations/cpfRelation@xlink:href
			String link = cpfRelationHrefAndTypeTf.getTextFieldValue();
			if (StringUtils.isNotEmpty(link) && !link.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
				cpfRelation.setHref(link);
				cpfRelation.setType(SIMPLE);
				hasChanged = true;
			}

			// Try to recover the descriptive note of the relation.
			// /eacCpf/cpfDescription/relations/cpfRelation/descriptiveNote
			TextAreaWithLanguage cpfRelationDescriptionTa = cpfRelationDescriptionTas.get(currentIndex);
			String description = cpfRelationDescriptionTa.getTextValue();
			if (StringUtils.isNotEmpty(description)) {
				DescriptiveNote descriptiveNote = new DescriptiveNote();
				P p = new P();
				p.setContent(description);
				if (StringUtils.isNotEmpty(language)) {
					p.setLang(language);
				}
				descriptiveNote.getP().add(p);
				cpfRelation.setDescriptiveNote(descriptiveNote);
				hasChanged = true;
			}

			// Try to recover the name of the relation.
			// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='title'
			String name = cpfRelationNameTf.getTextValue();
			if (StringUtils.isNotEmpty(name)) {
				RelationEntry relationEntry = new RelationEntry();
				relationEntry.setContent(name);
				relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_TITLE);
				if (StringUtils.isNotEmpty(language)) {
					relationEntry.setLang(language);
				}
				cpfRelation.getRelationEntry().add(relationEntry);
				hasChanged = true;
			}

			// Try to recover the identifier of the relation.
			// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='id'
			String id = cpfRelationIdentifierTfs.get(currentIndex).getText();
			if (StringUtils.isNotEmpty(id)) {
				RelationEntry relationEntry = new RelationEntry();
				relationEntry.setContent(id);
				relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_ID);
				if (StringUtils.isNotEmpty(language)) {
					relationEntry.setLang(language);
				}
				cpfRelation.getRelationEntry().add(relationEntry);
				hasChanged = true;
			}

			// Try to recover the agency name and agency code of the relation.
			List<TextFieldWithLanguage> cpfRelationOrganisationNameAndIdTfsList = cpfRelationOrganisationNameAndIdTfs.get(currentIndex);
			if(cpfRelationOrganisationNameAndIdTfsList!=null){
				for (TextFieldWithLanguage cpfRelationOrganisationNameAndIdTf : cpfRelationOrganisationNameAndIdTfsList) {
					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='agencyName'
					String agencyName = cpfRelationOrganisationNameAndIdTf.getTextValue();
					if (StringUtils.isNotEmpty(agencyName)) {
						RelationEntry relationEntry = new RelationEntry();
						relationEntry.setContent(agencyName);
						relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME);
						if (StringUtils.isNotEmpty(language)) {
							relationEntry.setLang(language);
						}
						cpfRelation.getRelationEntry().add(relationEntry);
						hasChanged = true;
					}

					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='agencyCode'
					String agencyCode = cpfRelationOrganisationNameAndIdTf.getExtraValue();
					if (StringUtils.isNotEmpty(agencyCode)) {
						RelationEntry relationEntry = new RelationEntry();
						relationEntry.setContent(agencyCode);
						relationEntry.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE);
						if (StringUtils.isNotEmpty(language)) {
							relationEntry.setLang(language);
						}
						cpfRelation.getRelationEntry().add(relationEntry);
						hasChanged = true;
					}
				}
			}
			if (hasChanged || (!saveCPF && currentIndex+1 == cpfRelationOrganisationNameAndIdTfs.size())) {
				relations.getCpfRelation().add(cpfRelation);
			}
			
			return relations;
		}
	}

	/**
	 * Class to performs the actions when the user clicks in other tab.
	 */
	public class ChangeTabListener extends UpdateEacCpfObject implements ChangeListener {
		private int currentTab;
		ChangeTabListener(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model, int indexTab) {
			super (eaccpf, tabbedPane, model);
			this.currentTab = indexTab;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			int selectedIndex = this.tabbedPane.getSelectedIndex();
			// Checks if clicks in different tab.
			if (this.currentTab != selectedIndex) {
				try {
					super.updateJAXBObject(true);
					removeChangeListener();
					switch (selectedIndex) {
						case 0:
							String mainagencycode = eaccpf.getControl().getMaintenanceAgency().getAgencyCode().getValue();
							reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, false, labels, entityType, firstLanguage, firstScript, mainagencycode).buildEditorPanel(errors), 0);
							break;
						case 1:
							reloadTabbedPanel(new EacCpfDescriptionPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 1);
							break;
						case 3:
							reloadTabbedPanel(new EacCpfControlPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 3);
							break;
						default:
							reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
					}
				} catch (EacCpfFormException ex) {
					reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
				}
			}
		}
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			// Empty.
		}
	}
	
	/**
	 * Class to performs the action when the user clicks in the exit button
	 */
	protected class ExitBtnAction extends UpdateEacCpfObject {
		/**
		 * Constructor.
		 *
		 * @param eacCpf
		 * @param tabbedPane
		 * @param model
		 */   
		public ExitBtnAction(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model) {
			super(eaccpf, tabbedPane, model);
		}
		
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
        	int event = JOptionPane.showConfirmDialog(tabbedPane,labels.getString("eaccpf.commons.exitConfirm"),labels.getString("eaccpf.eacCpfItem"),JOptionPane.YES_NO_OPTION);
        	try{
	        	if(event == JOptionPane.YES_OPTION){
	        		super.updateJAXBObject(true);
	        		if(checkStartTabFields()){
	        			super.saveFile(eaccpf.getControl().getRecordId().getValue());
	        			closeFrame();
	        		}
	        	}else if (event == JOptionPane.NO_OPTION){	
	        		EacCpfFrame.inUse(false);
	                closeFrame();
	        	}
			} catch (EacCpfFormException e) {
				reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
			}
        }
    }
}