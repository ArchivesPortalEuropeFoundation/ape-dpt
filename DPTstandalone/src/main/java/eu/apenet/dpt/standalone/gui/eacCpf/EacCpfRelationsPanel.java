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
import eu.apenet.dpt.utils.eaccpf.CpfDescription;
import eu.apenet.dpt.utils.eaccpf.CpfRelation;
import eu.apenet.dpt.utils.eaccpf.DescriptiveNote;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.P;
import eu.apenet.dpt.utils.eaccpf.RelationEntry;
import eu.apenet.dpt.utils.eaccpf.Relations;
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
	// CPF relations section.
	private List<TextFieldWithLanguage> cpfRelationNameTfs;
	private List<JTextField> cpfRelationIdentifierTfs;
	private List<TextFieldWithComboBoxEacCpf> cpfRelationHrefAndTypeTfs;
	private List<TextAreaWithLanguage> cpfRelationDescriptionTas;
	private Map<Integer, List<TextFieldWithLanguage>> cpfRelationOrganisationNameAndIdTfs;

	// Resource relations section.

	// Function relations section.
	

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

		// Call method to build cpf relations section.
		builder = this.buildCPFRelationsSection(builder, cc);

		// Call method to build resource relations section.
		builder = this.buildResourceRelationsSection(builder, cc);

		// Call method to build functions relations section.
		builder = this.buildFunctionsRelationsSection(builder, cc);

		// Call method to build the main buttons zone.
		this.buildButtons(builder, cc);

		// Define the change tab listener.
		this.removeChangeListener();
		this.tabbedPane.addChangeListener(new ChangeTabListener (this.eaccpf, this.tabbedPane, this.model, 2));

		return builder.getPanel();
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
		if (this.eaccpf != null
				&& this.eaccpf.getCpfDescription() != null
				&& this.eaccpf.getCpfDescription().getIdentity() != null
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
		// Set section title.
		this.setNextRow();
		builder.addLabel(this.labels.getString("eaccpf.relations.cpf.section"), cc.xyw(1, this.rowNb, 7));

		// Call method to build cpf relations part.
		builder = this.buildCPFRelation(builder, cc);

		return builder;
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

		this.cpfRelationNameTfs = new ArrayList<TextFieldWithLanguage>(cpfRelations.size());
		this.cpfRelationIdentifierTfs = new ArrayList<JTextField>(cpfRelations.size());
		this.cpfRelationHrefAndTypeTfs = new ArrayList<TextFieldWithComboBoxEacCpf>(cpfRelations.size());
		this.cpfRelationDescriptionTas = new ArrayList<TextAreaWithLanguage>(cpfRelations.size());
		this.cpfRelationOrganisationNameAndIdTfs = new HashMap<Integer, List<TextFieldWithLanguage>>(cpfRelations.size());

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
					if(StringUtils.isNotEmpty(relationEntry.getLocalType())
							&& relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_TITLE)
							&& StringUtils.isNotEmpty(relationEntry.getContent())) {
						title = relationEntry.getContent();
					}
					// Id of the relation.
		            // /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='id'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType())
							&& relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_ID)
							&& StringUtils.isNotEmpty(relationEntry.getContent())) {
						id = relationEntry.getContent();
					}

					// Agency name of the relation.
					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='agencyName'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType())
							&& relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME)) {
						agencyNameList.add(relationEntry.getContent());
					}

					// Agency code of the relation.
					// /eacCpf/cpfDescription/relations/cpfRelation/relationEntry@localType='agencyCode'
					if(StringUtils.isNotEmpty(relationEntry.getLocalType())
							&& relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE)) {
						agencyCodeList.add(relationEntry.getContent());
					}

					// Texts of relation entries without @localType.
					// TODO: This texts will be loaded in field "name"
					if(StringUtils.isNotEmpty(relationEntry.getContent())
							&& StringUtils.isEmpty(relationEntry.getLocalType())) {
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
			this.cpfRelationOrganisationNameAndIdTfs.put(Integer.valueOf(i), cpfRelationOrganisationNameAndIdTfList);

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
			addFurtherAgencyBtn.addActionListener(new AddFurtherAgency(this.eaccpf, this.tabbedPane, this.model, i));
			builder.add(addFurtherAgencyBtn, cc.xy(1, this.rowNb));
		}

		// Button to add new CPF relation.
		this.setNextRow();
		JButton addFurtherCPFBtn = new ButtonTab(this.labels.getString("eaccpf.relations.add.further.cpf"));
		addFurtherCPFBtn.addActionListener(new AddFurtherCPF(this.eaccpf, this.tabbedPane, this.model));
		builder.add(addFurtherCPFBtn, cc.xy(1, this.rowNb));

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

		//TODO: Pending implementation.

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

		//TODO: Pending implementation.

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
		exitBtn.addActionListener(new ExitBtnAction());

		JButton saveBtn = new ButtonTab(labels.getString("eaccpf.commons.save"));
		builder.add(saveBtn, cc.xy (5, this.rowNb));
		saveBtn.addActionListener(new SaveBtnAction(this.eaccpf, this.tabbedPane, this.model));
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
		private int currentCPFRelation;

		/**
		 * Constructor.
		 *
		 * @param eaccpf
		 * @param tabbedPane
		 * @param model
		 * @param isNextTab
		 * @param counter
		 */
		AddFurtherAgency(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model, int counter) {
			super(eaccpf, tabbedPane, model);
			this.currentCPFRelation = counter;
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
			List<TextFieldWithLanguage> cpfRelationOrganisationNameAndIdTf = cpfRelationOrganisationNameAndIdTfs.get(this.currentCPFRelation);
			for (int i = 0; !emptyName && !emptyCode && i < cpfRelationOrganisationNameAndIdTf.size(); i++) {
				if (StringUtils.isEmpty(cpfRelationOrganisationNameAndIdTf.get(i).getTextValue())
						|| StringUtils.isEmpty(cpfRelationOrganisationNameAndIdTf.get(i).getExtraValue())) {
					JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.relations.error.empty.agency"));
				}
				if (StringUtils.isEmpty(cpfRelationOrganisationNameAndIdTf.get(i).getTextValue())) {
					emptyName = true;
				}
				if (StringUtils.isEmpty(cpfRelationOrganisationNameAndIdTf.get(i).getExtraValue())) {
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
					&& eaccpf.getCpfDescription().getRelations().getCpfRelation().size() > this.currentCPFRelation) {
				RelationEntry relationEntryName = new RelationEntry();
				relationEntryName.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME);
				eaccpf.getCpfDescription().getRelations().getCpfRelation().get(this.currentCPFRelation).getRelationEntry().add(relationEntryName);

				RelationEntry relationEntryCode = new RelationEntry();
				relationEntryCode.setLocalType(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE);
				eaccpf.getCpfDescription().getRelations().getCpfRelation().get(this.currentCPFRelation).getRelationEntry().add(relationEntryCode);
			}

			reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
		}
	}

	/**
	 * Class to performs the addition of new CPF relation in the CPF relations
	 * section if the previous values are filled.
	 */
	public class AddFurtherCPF extends UpdateEacCpfObject {
		AddFurtherCPF(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model) {
			super(eaccpf, tabbedPane, model);
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
					JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.relations.error.empty.name"));
					empty = true;
				}
			}

			// Check the content and add the elements.
			if (eaccpf.getCpfDescription() == null) {
				eaccpf.setCpfDescription(new CpfDescription());
			}
			if (eaccpf.getCpfDescription().getRelations() == null) {
				eaccpf.getCpfDescription().setRelations(new Relations());
			}

			if (!empty) {
				eaccpf.getCpfDescription().getRelations().getCpfRelation().add(new CpfRelation());
			}

			reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels, entityType, firstLanguage, firstScript).buildEditorPanel(errors), 2);
		}
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
				super.saveFile(eaccpf.getControl().getRecordId().getValue());
                closeFrame();
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
//			this.updateResourceRelations(relations);	// TODO: Implement

			// Call method to save the information about Function relations.
//			this.updateFunctionRelations(relations);	// TODO: Implement

			if(!errors.isEmpty()) {
				throw new EacCpfFormException("Errors in validation of EAC-CPF");
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

			// Check the number of relations.
			for (int i = 0; i < cpfRelationNameTfs.size(); i++) {
				CpfRelation cpfRelation = new CpfRelation();

				// Try to recover the language of the relation (@lang).
				TextFieldWithLanguage cpfRelationNameTf = cpfRelationNameTfs.get(i);
				String language = null;
				if (StringUtils.isNotEmpty(cpfRelationNameTf.getLanguage())
						&& !cpfRelationNameTf.getLanguage().equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					language = cpfRelationNameTf.getLanguage();
				}

				// Try to recover the type of the relation.
				// /eacCpf/cpfDescription/relations/cpfRelation@cpfRelationType
				// /eacCpf/cpfDescription/relations/cpfRelation@type
				TextFieldWithComboBoxEacCpf cpfRelationHrefAndTypeTf = cpfRelationHrefAndTypeTfs.get(i);
				String type = cpfRelationHrefAndTypeTf.getComboBoxValue(TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION, entityType);
				if (StringUtils.isNotEmpty(type)
						&& !type.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					cpfRelation.setCpfRelationType(type);
					cpfRelation.setType(type);
					hasChanged = true;
				}

				// Try to recover the href of the relation.
				// /eacCpf/cpfDescription/relations/cpfRelation@xlink:href
				String link = cpfRelationHrefAndTypeTf.getTextFieldValue();
				if (StringUtils.isNotEmpty(link)
						&& !link.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					cpfRelation.setHref(link);
					hasChanged = true;
				}

				// Try to recover the descriptive note of the relation.
				// /eacCpf/cpfDescription/relations/cpfRelation/descriptiveNote
				TextAreaWithLanguage cpfRelationDescriptionTa = cpfRelationDescriptionTas.get(i);
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
				String id = cpfRelationIdentifierTfs.get(i).getText();
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
				List<TextFieldWithLanguage> cpfRelationOrganisationNameAndIdTfsList = cpfRelationOrganisationNameAndIdTfs.get(i);
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

				if (hasChanged) {
					relations.getCpfRelation().add(cpfRelation);
				}
			}

			return hasChanged;
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

}
