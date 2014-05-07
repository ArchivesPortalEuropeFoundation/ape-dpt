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
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
import eu.apenet.dpt.standalone.gui.commons.TextChanger;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.StructureWithLanguage;
import eu.apenet.dpt.standalone.gui.eacCpf.SwingStructures.TextFieldWithComboBoxEacCpf;
import eu.apenet.dpt.standalone.gui.eacCpf.SwingStructures.TextFieldsWithCheckBoxForDates;
import eu.apenet.dpt.utils.eaccpf.Abbreviation;
import eu.apenet.dpt.utils.eaccpf.AgencyCode;
import eu.apenet.dpt.utils.eaccpf.AgencyName;
import eu.apenet.dpt.utils.eaccpf.Agent;
import eu.apenet.dpt.utils.eaccpf.AgentType;
import eu.apenet.dpt.utils.eaccpf.Citation;
import eu.apenet.dpt.utils.eaccpf.Control;
import eu.apenet.dpt.utils.eaccpf.ConventionDeclaration;
import eu.apenet.dpt.utils.eaccpf.CpfDescription;
import eu.apenet.dpt.utils.eaccpf.Date;
import eu.apenet.dpt.utils.eaccpf.DateRange;
import eu.apenet.dpt.utils.eaccpf.DateSet;
import eu.apenet.dpt.utils.eaccpf.Description;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.EntityId;
import eu.apenet.dpt.utils.eaccpf.EntityType;
import eu.apenet.dpt.utils.eaccpf.EventDateTime;
import eu.apenet.dpt.utils.eaccpf.EventType;
import eu.apenet.dpt.utils.eaccpf.ExistDates;
import eu.apenet.dpt.utils.eaccpf.FromDate;
import eu.apenet.dpt.utils.eaccpf.Identity;
import eu.apenet.dpt.utils.eaccpf.Identity.NameEntry;
import eu.apenet.dpt.utils.eaccpf.Language;
import eu.apenet.dpt.utils.eaccpf.LanguageDeclaration;
import eu.apenet.dpt.utils.eaccpf.MaintenanceAgency;
import eu.apenet.dpt.utils.eaccpf.MaintenanceEvent;
import eu.apenet.dpt.utils.eaccpf.MaintenanceStatus;
import eu.apenet.dpt.utils.eaccpf.NameEntryParallel;
import eu.apenet.dpt.utils.eaccpf.Part;
import eu.apenet.dpt.utils.eaccpf.RecordId;
import eu.apenet.dpt.utils.eaccpf.Script;
import eu.apenet.dpt.utils.eaccpf.ToDate;
import eu.apenet.dpt.utils.eaccpf.UseDates;
import eu.apenet.dpt.utils.util.XmlTypeEacCpf;

/**
 * Class for the panel "identity" of the apeEAC-CPF creation form.
 */
public class EacCpfIdentityPanel extends EacCpfPanel {
	// Constansts for the errors.
	private static final String ERROR_NAME_PART = "namePartComponentTfsWCbs_";
	private static final String ERROR_EXISTENCE_DATES = "existenceDateTFs";

	// Constants for the unknown dates.
	private static final String UNKNOWN_DATE = "date";
	private static final String UNKNOWN_DATE_FROM = "from";
	private static final String UNKNOWN_DATE_TO = "to";
	private static final String UNKNOWN_END_DATE = "2099";
	private static final String UNKNOWN_INITIAL_DATE = "0001";

	// Constanrs for maintenance.
	private static final String MAINTENANCE_AGENCY_NAME = "European test archives";
	private static final String MAINTENANCE_AGENT_HUMAN = "human";
	private static final String MAINTENANCE_EVENT_CREATED = "created";
	private static final String MAINTENANCE_EVENT_NEW = "new";
	private static final String MAINTENANCE_EVENT_REVISED = "revised";

	// Initial values.
	private boolean isNew; // Indicates if is new file.
	private XmlTypeEacCpf entityType; // The type of the entity described.
	private String firstLanguage; // The current first language selected.
	private String firstScript; // The current first script selected.
    private String mainagencycode; // The code of the current agency.

	// Elements in the panel.
	// Name section.
	private Map<Integer, List<TextFieldWithComboBoxEacCpf>> namePartComponentTfsWCbs;
	private List<TextFieldWithComboBoxEacCpf> nameFormTfsWCbs;
	private List<StructureWithLanguage> nameLanguageCbs;
	private Map<Integer, List<TextFieldsWithCheckBoxForDates>> useDateTFs;
	// Identifiers section.
	private List<JTextField> identifierTfs;
	private List<JTextField> identifierTypeTfs;
	// Dates of existence section.
	private List<TextFieldsWithCheckBoxForDates> existenceDateTFs;

	/**
	 * Constructor.
	 *
	 * @param eaccpf
	 * @param tabbedPane
	 * @param mainTabbedPane
	 * @param eacCpfFrame
	 * @param model
	 * @param isNew
	 * @param labels
	 */
	public EacCpfIdentityPanel(EacCpf eaccpf, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eacCpfFrame, ProfileListModel model, boolean isNew, ResourceBundle labels) {
		this(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels, null);
	}

	/**
	 * Constructor.
	 *
	 * @param eaccpf
	 * @param tabbedPane
	 * @param mainTabbedPane
	 * @param eacCpfFrame
	 * @param model
	 * @param isNew
	 * @param labels
	 * @param entityType
	 */
	public EacCpfIdentityPanel(EacCpf eaccpf, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eacCpfFrame, ProfileListModel model, boolean isNew, ResourceBundle labels, XmlTypeEacCpf entityType) {
		super(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels);
		this.isNew = isNew;
		this.entityType = entityType;
	}

	/**
	 * Constructor.
	 *
	 * @param eaccpf
	 * @param tabbedPane
	 * @param mainTabbedPane
	 * @param eacCpfFrame
	 * @param model
	 * @param isNew
	 * @param labels
	 * @param entityType
	 * @param firstLanguage
	 * @param firstScript
	 * @param mainagencycode
	 */
	public EacCpfIdentityPanel(EacCpf eaccpf, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eacCpfFrame, ProfileListModel model, boolean isNew, ResourceBundle labels, XmlTypeEacCpf entityType, String firstLanguage, String firstScript, String mainagencycode) {
		super(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels);
		this.isNew = isNew;
		this.entityType = entityType;
		this.firstLanguage = firstLanguage;
		this.firstScript = firstScript;
		this.mainagencycode = mainagencycode;
	}

	/**
	 * Builds and answer the identity tab for the given layout.
	 *
	 * @param errors List of errors.
	 * @return the identity tab.
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
		if (this.eaccpf.getCpfDescription().getIdentity() == null) {
			this.eaccpf.getCpfDescription().setIdentity(new Identity());
		}

        this.setNextRow();
		builder.addSeparator("", cc.xyw(1, this.rowNb, 7));
        this.setNextRow();

		// Call method to build the text of the entity type.
		builder = this.buildEntityTypeText(builder, cc);

        // Call method to build the name section.
		builder = this.buildNameSection(builder, cc, errors);

        // Call method to build the identifier section.
		builder = this.buildIdentifierSection(builder, cc);

        // Call method to build the dates of existence section.
		builder = this.buildDatesOfExistenceSection(builder, cc, errors);

		// Call method to build the main buttons zone.
		this.buildButtons(builder, cc);

		// Define the change tab listener.
		this.removeChangeListener();
		this.tabbedPane.addChangeListener(new ChangeTabListener(this.eaccpf, this.tabbedPane, this.model, 0));

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
			builder.addLabel(this.labels.getString("eaccpf.identity.type") + " " + this.labels.getString("eaccpf.commons.corporateBody"), cc.xyw(1, this.rowNb, 3));
			this.entityType = XmlTypeEacCpf.EAC_CPF_CORPORATEBODY;
		} else if (XmlTypeEacCpf.EAC_CPF_FAMILY.getName().equalsIgnoreCase(type)) {
			builder.addLabel(this.labels.getString("eaccpf.identity.type") + " " + this.labels.getString("eaccpf.commons.family"), cc.xyw(1, this.rowNb, 3));
			this.entityType = XmlTypeEacCpf.EAC_CPF_FAMILY;
		} else if (XmlTypeEacCpf.EAC_CPF_PERSON.getName().equalsIgnoreCase(type)) {
			builder.addLabel(this.labels.getString("eaccpf.identity.type") + " " + this.labels.getString("eaccpf.commons.person"), cc.xyw(1, this.rowNb, 3));
			this.entityType = XmlTypeEacCpf.EAC_CPF_PERSON;
		} else {
			builder.addLabel(this.labels.getString("eaccpf.identity.unrecognized.type"), cc.xyw (1, this.rowNb, 3));
		}

		return builder;
	}

	/**
	 * Main method to build the name section.
	 *
	 * @param builder
	 * @param cc
	 * @param errors
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildNameSection(PanelBuilder builder, CellConstraints cc, List<String> errors) {
		// Call method to build full name or part of the name part.
		builder = this.buildFullNamePart(builder, cc, errors);

		return builder;
	}

	/**
	 * Method to build the full name or part of the name part of name section.
	 *
	 * @param builder
	 * @param cc
	 * @param errors
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildFullNamePart(PanelBuilder builder, CellConstraints cc, List<String> errors) {
		// Define values of the part.
		if (this.eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().isEmpty()) {
			this.eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().add(new NameEntry());
		}

		List<NameEntry> nameEntries = this.getAllNameEntries();

		this.namePartComponentTfsWCbs = new HashMap<Integer, List<TextFieldWithComboBoxEacCpf>>(nameEntries.size());
		this.nameFormTfsWCbs = new ArrayList<TextFieldWithComboBoxEacCpf>(nameEntries.size());
		this.nameLanguageCbs = new ArrayList<StructureWithLanguage>(nameEntries.size());
		this.useDateTFs = new HashMap<Integer, List<TextFieldsWithCheckBoxForDates>>(nameEntries.size());

		int index = 0;

		for (NameEntry nameEntry : nameEntries) {
			// Set section title.
			this.setNextRow();
			builder.addSeparator(this.labels.getString("eaccpf.identity.name"), cc.xyw(1, this.rowNb, 7));
			this.setNextRow();

			if (nameEntry.getPart().isEmpty()) {
				nameEntry.getPart().add(new Part());
			}

			// Create list of elements.
			List<TextFieldWithComboBoxEacCpf> namePartComponentTfWCbList = new ArrayList<TextFieldWithComboBoxEacCpf>();

			for (int i = 0; i < nameEntry.getPart().size(); i++) {
				Part part = nameEntry.getPart().get(i);
				// Create element.
				TextFieldWithComboBoxEacCpf namePartComponentTfWCb = new TextFieldWithComboBoxEacCpf(this.trimStringValue(part.getContent()), part.getLocalType(), TextFieldWithComboBoxEacCpf.TYPE_COMPONENT, this.entityType, this.labels);

				// Add elements to the list.
				namePartComponentTfWCbList.add(namePartComponentTfWCb);

				// Add elements to the panel.
				if (i == 0) {
					builder.addLabel(this.labels.getString("eaccpf.identity.name.full") + "*", cc.xy(1, this.rowNb));
				} else {
					builder.addLabel(this.labels.getString("eaccpf.identity.name.part"), cc.xy(1, this.rowNb));
				}
				builder.add(namePartComponentTfWCb.getTextField(), cc.xy(3, this.rowNb));
				builder.addLabel(this.labels.getString("eaccpf.identity.name.component"), cc.xy(5, this.rowNb));
				builder.add(namePartComponentTfWCb.getComboBox(), cc.xy(7, this.rowNb));
				this.setNextRow();

				// Add error mandatory field not filled when needed.
				if (errors.contains(EacCpfIdentityPanel.ERROR_NAME_PART + index)) {
					builder.add(createErrorLabel(this.labels.getString("eaccpf.identity.error.empty.name.label")), cc.xyw(1, this.rowNb, 3));
					this.setNextRow();
				}
			}

			// Add elements to the map.
			this.namePartComponentTfsWCbs.put(Integer.valueOf(index), namePartComponentTfWCbList);

			// Button to add new parts of the name.
			JButton addNamePartsBtn = new ButtonTab(this.labels.getString("eaccpf.identity.add.part"));
			addNamePartsBtn.addActionListener(new AddNamePartsAction(this.eaccpf, this.tabbedPane, this.model, index));
			builder.add(addNamePartsBtn, cc.xy(1, this.rowNb));
			this.setNextRow();

			// Call method to build form of the name part and language.
			builder = this.buildFormNameAndLanguage(builder, cc, nameEntry);

			// Call method to build use dates of the name part.
			builder = this.buildUseDates(builder, cc, nameEntry, index);

			index++;
		}

		// Button to add new forms of the name.
		this.setNextRow();
		JButton addFormNameBtn = new ButtonTab(this.labels.getString("eaccpf.identity.add.form"));
		addFormNameBtn.addActionListener(new AddFormNameAction(this.eaccpf, this.tabbedPane, this.model));
		builder.add(addFormNameBtn, cc.xy(1, this.rowNb));
		this.setNextRow();

		return builder;
	}

	/**
	 * Method to recover all the elements NameEntry in the object.
	 *
	 * @return the list of NameEntry.
	 */
	private List<NameEntry> getAllNameEntries() {
		List<Object> nameEntryParallelOrNameEntryList = this.eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry();
		List<NameEntry> nameEntries = new ArrayList<NameEntry>();

		for (Object object : nameEntryParallelOrNameEntryList) {
			if (object instanceof NameEntryParallel) {
				NameEntryParallel nameEntryParallel = (NameEntryParallel) object;
				for (Object nameEntry : nameEntryParallel.getContent()) {
					if (nameEntry instanceof NameEntry) {
						nameEntries.add((NameEntry) nameEntry);
					}
				}
			}

			if (object instanceof NameEntry) {
				nameEntries.add((NameEntry) object);
			}
		}

		return nameEntries;
	}

	/**
	 * Method to build the Form of the name part of name section.
	 *
	 * @param builder
	 * @param cc
	 * @param nameEntry
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildFormNameAndLanguage(PanelBuilder builder, CellConstraints cc, NameEntry nameEntry) {
		this.setNextRow();

		// Create elements.
		TextFieldWithComboBoxEacCpf nameFormTfWCb = new TextFieldWithComboBoxEacCpf(null, nameEntry.getLocalType(), TextFieldWithComboBoxEacCpf.TYPE_FORM, this.entityType, this.labels);
		StructureWithLanguage structureWithLanguageCb = null;
		String lang = "";
		for (Part part : nameEntry.getPart()) {
			if (part.getLang() != null
					&& !part.getLang().isEmpty()) {
				lang = part.getLang();
			}
		}

		if (lang != null && !lang.isEmpty()) {
			structureWithLanguageCb = new StructureWithLanguage(lang);
		} else {
			structureWithLanguageCb = new StructureWithLanguage(this.firstLanguage);
		}

		// Add elements to the list.
		this.nameFormTfsWCbs.add(nameFormTfWCb);
		this.nameLanguageCbs.add(structureWithLanguageCb);

		// Add elements to the panel.
		builder.addLabel(this.labels.getString("eaccpf.identity.name.form"), cc.xy(1, this.rowNb));
		builder.add(nameFormTfWCb.getComboBox(), cc.xy(3, this.rowNb));
		builder.addLabel(this.labels.getString("eaccpf.commons.select.language"), cc.xy(5, this.rowNb));
		builder.add(structureWithLanguageCb.getLanguageBox(), cc.xy(7, this.rowNb));

		return builder;
	}

	/**
	 * Method to build the use dates part of name section.
	 *
	 * @param builder
	 * @param cc
	 * @param nameEntry
	 * @param index
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildUseDates(PanelBuilder builder, CellConstraints cc, NameEntry nameEntry, int index) {
		this.setNextRow();

		// Define values of the section.
		if (nameEntry.getUseDates() == null) {
			nameEntry.setUseDates(new UseDates());
		}

		List<Object> useDates = this.getAllDates(nameEntry.getUseDates());
		List<TextFieldsWithCheckBoxForDates> tfwcfdList = new ArrayList<TextFieldsWithCheckBoxForDates>();

		for (Object object : useDates) {
			this.setNextRow();

			// Type of date.
			boolean isDateRange = false;

			// Create element.
			TextFieldsWithCheckBoxForDates useDateTF = null;
			if (object instanceof Date) {
				Date date = (Date) object;
				boolean isDateUndefined = this.isUndefinedDate(date.getStandardDate());
				useDateTF = new TextFieldsWithCheckBoxForDates(this.labels.getString("eaccpf.commons.unknown.date"),
												date.getContent(), isDateUndefined, date.getStandardDate(),
												"", false, "", "", false, "", false);
			} else if (object instanceof DateRange) {
				isDateRange = true;
				DateRange dateRange = (DateRange) object;
				String dateFrom = "";
				String dateFromStandard = "";
				String dateTo = "";
				String dateToStandard = "";

				if (dateRange.getFromDate() != null) {
					if (dateRange.getFromDate().getContent() != null
							&& !dateRange.getFromDate().getContent().isEmpty()) {
						dateFrom = dateRange.getFromDate().getContent();
					}
					if (dateRange.getFromDate().getStandardDate() != null
							&& !dateRange.getFromDate().getStandardDate().isEmpty()) {
						dateFromStandard = dateRange.getFromDate().getStandardDate();
					}
				}
				if (dateRange.getToDate() != null) {
					if (dateRange.getToDate().getContent() != null
							&& !dateRange.getToDate().getContent().isEmpty()) {
						dateTo = dateRange.getToDate().getContent();
					}
					if (dateRange.getToDate().getStandardDate() != null
							&& !dateRange.getToDate().getStandardDate().isEmpty()) {
						dateToStandard = dateRange.getToDate().getStandardDate();
					}
				}

				boolean isDateFromUndefined = this.isUndefinedDate(dateFromStandard);
				boolean isDateToUndefined = this.isUndefinedDate(dateToStandard);

				useDateTF = new TextFieldsWithCheckBoxForDates(this.labels.getString("eaccpf.commons.unknown.date"), "", false, "",
												dateFrom, isDateFromUndefined, dateFromStandard,
												dateTo, isDateToUndefined, dateToStandard, true);
			}

			// Add elements to the list.
			tfwcfdList.add(useDateTF);

			// Add elements to the panel.
			if (isDateRange) {
				// First date row. Normal date text fields.
				builder.addLabel(this.labels.getString("eaccpf.commons.from.date"), cc.xy(1, this.rowNb));
				useDateTF.getDateFromTextField().addFocusListener(new AddIsoText(useDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_FROM));
				builder.add(useDateTF.getDateFromTextField(), cc.xy(3, this.rowNb));
				builder.addLabel(this.labels.getString("eaccpf.commons.to.date"), cc.xy(5, this.rowNb));
				useDateTF.getDateToTextField().addFocusListener(new AddIsoText(useDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_TO));
				builder.add(useDateTF.getDateToTextField(), cc.xy(7, this.rowNb));

				// Second date row. Unknown check boxes.
				this.setNextRow();
				useDateTF.getDateFromUndefinedCB().addActionListener(new AddUndefinedTexts(useDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_FROM));
				useDateTF.getDateToUndefinedCB().addActionListener(new AddUndefinedTexts(useDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_TO));
				builder.add(useDateTF.getDateFromUndefinedCB(), cc.xy(3, this.rowNb));
				builder.add(useDateTF.getDateToUndefinedCB(), cc.xy(7, this.rowNb));

				// Third date row. Standard dates.
				this.setNextRow();
				builder.addLabel(this.labels.getString("eaccpf.commons.iso.date"), cc.xy(1, this.rowNb));
				useDateTF.getStandardDateFromTextField().addFocusListener(new CheckIsoText(useDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_FROM));
				builder.add(useDateTF.getStandardDateFromTextField(), cc.xy(3, this.rowNb));
				builder.addLabel(this.labels.getString("eaccpf.commons.iso.date"), cc.xy(5, this.rowNb));
				useDateTF.getStandardDateToTextField().addFocusListener(new CheckIsoText(useDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_TO));
				builder.add(useDateTF.getStandardDateToTextField(), cc.xy(7, this.rowNb));
			} else {
				// First date row. Normal date text fields.
				builder.addLabel(this.labels.getString("eaccpf.commons.date"), cc.xy(1, this.rowNb));
				useDateTF.getDateTextField().addFocusListener(new AddIsoText(useDateTF, EacCpfIdentityPanel.UNKNOWN_DATE));
				builder.add(useDateTF.getDateTextField(), cc.xy(3, this.rowNb));

				// Second date row. Unknown check boxes.
				this.setNextRow();
				useDateTF.getDateUndefinedCB().addActionListener(new AddUndefinedTexts(useDateTF, EacCpfIdentityPanel.UNKNOWN_DATE));
				builder.add(useDateTF.getDateUndefinedCB(), cc.xy(3, this.rowNb));

				// Third date row. Standard dates.
				this.setNextRow();
				builder.addLabel(this.labels.getString("eaccpf.commons.iso.date"), cc.xy(1, this.rowNb));
				useDateTF.getStandardDateTextField().addFocusListener(new CheckIsoText(useDateTF, EacCpfIdentityPanel.UNKNOWN_DATE));
				builder.add(useDateTF.getStandardDateTextField(), cc.xy(3, this.rowNb));
			}
		}

		// Buttons to add new entries.
		this.setNextRow();
		JButton addSingleDateBtn = new ButtonTab(this.labels.getString("eaccpf.commons.add.single.date"));
		addSingleDateBtn.addActionListener(new AddSingleOrRangeDateAction(this.eaccpf, this.tabbedPane, this.model, true, false, index));
		builder.add(addSingleDateBtn, cc.xy(1, this.rowNb));
		JButton addRangeDateBtn = new ButtonTab(this.labels.getString("eaccpf.commons.add.range.date"));
		addRangeDateBtn.addActionListener(new AddSingleOrRangeDateAction(this.eaccpf, this.tabbedPane, this.model, true, true, index));
		builder.add(addRangeDateBtn, cc.xy(3, this.rowNb));

		this.useDateTFs.put(Integer.valueOf(index), tfwcfdList);

		return builder;
	}

	/**
	 * Method to recover all the elements Date and DateRange in the object.
	 *
	 * @param useDates
	 * @return The list of dates
	 */
	private List<Object> getAllDates(UseDates useDates) {
		List<Object> datesList = new ArrayList<Object>();

		// Only Date element.
		if (useDates.getDate() != null) {
			datesList.add(useDates.getDate());
		}
		// Only DateRange element.
		if (useDates.getDateRange() != null) {
			datesList.add(useDates.getDateRange());
		}
		// Any combination of Date and DateRange elements.
		if (useDates.getDateSet() != null) {
			datesList.addAll(useDates.getDateSet().getDateOrDateRange());
		}

		return datesList;
	}

	/**
	 * Method to parse the value of the date to an ISO one if possible.
	 *
	 * @param text
	 * @return the ISO date.
	 */
	private String parseStandardDate(String text) {
		boolean pattern1 = Pattern.matches("\\d{4}", text); //yyyy
		boolean pattern2 = Pattern.matches("\\d{4}[\\-\\./:\\s]\\d{2}", text); //yyyy-MM
		boolean pattern3 = Pattern.matches("\\d{4}[\\-\\./:\\s]\\d{2}[\\-\\./:\\s]\\d{2}", text); //yyyy-MM-dd
		boolean pattern4 = Pattern.matches("\\d{2}[\\-\\./:\\s]\\d{2}[\\-\\./:\\s]\\d{4}", text); //dd-MM-yyyy
		if (pattern4){
			String yearStandardDate = text.substring(6);
			String monthStandardDate = text.substring(2,6);
			String dateStandardDate = text.substring(0,2);
			String reverseString =yearStandardDate+monthStandardDate+dateStandardDate;
			text = text.replaceAll(text, reverseString);
		}
		if (pattern1){
			return text;
		} else if (pattern2) {
			String monthStandardDate = text.substring(5,7);

			if (Integer.parseInt(monthStandardDate) <= 12) {
				text = text.replaceAll("[\\./:\\s]", "-");
				return text;
			}
		} else if (pattern3 || pattern4) {
			text = text.replaceAll("[\\./:\\s]", "-");
			return text;
		}

		return "";
	}

	/**
	 * Main method to build the identifier section.
	 *
	 * @param builder
	 * @param cc
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildIdentifierSection(PanelBuilder builder, CellConstraints cc) {
        this.setNextRow();
		builder.addSeparator("", cc.xyw(1, this.rowNb, 7));
        this.setNextRow();

		// Set section title.
		this.setNextRow();
		builder.addSeparator(this.labels.getString("eaccpf.identity.identifier"), cc.xyw(1, this.rowNb, 7));
		this.setNextRow();

		// Define values of the section.
		if (this.eaccpf.getCpfDescription().getIdentity().getEntityId().isEmpty()) {
			this.eaccpf.getCpfDescription().getIdentity().getEntityId().add(new EntityId());
		}

		List<EntityId> entityIds = this.eaccpf.getCpfDescription().getIdentity().getEntityId();

		this.identifierTfs = new ArrayList<JTextField>(entityIds.size());
		this.identifierTypeTfs = new ArrayList<JTextField>(entityIds.size());

		for (EntityId entityId : entityIds) {
			// Create element.
			JTextField identifierTf = new JTextField(entityId.getContent());
			JTextField identifierTypeTf = new JTextField(entityId.getLocalType());

			// Add elements to the list.
			this.identifierTfs.add(identifierTf);
			this.identifierTypeTfs.add(identifierTypeTf);

			// Add elements to the panel.
			if (this.entityType != null && XmlTypeEacCpf.EAC_CPF_CORPORATEBODY.getName().equalsIgnoreCase(this.entityType.getName())) {
				builder.addLabel(this.labels.getString("eaccpf.identity.identifier.corporateBody"), cc.xy(1, this.rowNb));
			} else if (this.entityType != null && XmlTypeEacCpf.EAC_CPF_FAMILY.getName().equalsIgnoreCase(this.entityType.getName())) {
				builder.addLabel(this.labels.getString("eaccpf.identity.identifier.family"), cc.xy(1, this.rowNb));
			} else if (this.entityType != null && XmlTypeEacCpf.EAC_CPF_PERSON.getName().equalsIgnoreCase(this.entityType.getName())) {
				builder.addLabel(this.labels.getString("eaccpf.identity.identifier.person"), cc.xy(1, this.rowNb));
			} else {
				builder.addLabel(this.labels.getString("eaccpf.identity.identifier"), cc.xy(1, this.rowNb));
			}

			builder.add(identifierTf, cc.xy(3, this.rowNb));
			builder.addLabel(this.labels.getString("eaccpf.identity.identifier.type"), cc.xy(5, this.rowNb));
			builder.add(identifierTypeTf, cc.xy(7, this.rowNb));
			this.setNextRow();
		}

		// Button to add new entries.
		JButton addIdentifierBtn = new ButtonTab(this.labels.getString("eaccpf.identity.add.identifier"));
		addIdentifierBtn.addActionListener(new AddIdentifierAction(this.eaccpf, this.tabbedPane, this.model));
		builder.add(addIdentifierBtn, cc.xy(1, this.rowNb));

		return builder;
	}

	/**
	 * Main method to build the dates of existence section.
	 *
	 * @param builder
	 * @param cc
	 * @param errors
	 * @return the PanelBuilder
	 */
	private PanelBuilder buildDatesOfExistenceSection(PanelBuilder builder, CellConstraints cc, List<String> errors) {
        this.setNextRow();
		builder.addSeparator("", cc.xyw(1, this.rowNb, 7));
        this.setNextRow();

		// Set section title.
		this.setNextRow();
		builder.addSeparator(this.labels.getString("eaccpf.identity.dates.existence") + "*", cc.xyw(1, this.rowNb, 7));

		// Define values of the section.
		if (this.eaccpf.getCpfDescription().getDescription() == null) {
			this.eaccpf.getCpfDescription().setDescription(new Description());
		}
		if (this.eaccpf.getCpfDescription().getDescription().getExistDates() == null) {
			this.eaccpf.getCpfDescription().getDescription().setExistDates(new ExistDates());
		}
		if (this.eaccpf.getCpfDescription().getDescription().getExistDates().getDate() == null
				&& this.eaccpf.getCpfDescription().getDescription().getExistDates().getDateRange() == null
				&& this.eaccpf.getCpfDescription().getDescription().getExistDates().getDateSet() == null) {
			this.eaccpf.getCpfDescription().getDescription().getExistDates().setDateRange(new DateRange());
		}

		List<Object> existingDates = this.getAllDates(this.eaccpf.getCpfDescription().getDescription().getExistDates());
		this.existenceDateTFs = new ArrayList<TextFieldsWithCheckBoxForDates>(existingDates.size());

		int position = 0;

		for (Object object : existingDates) {
			this.setNextRow();

			// Type of date.
			boolean isDateRange = false;
			// Create element.
			TextFieldsWithCheckBoxForDates existenceDateTF = null;
			if (object instanceof Date) {
				Date date = (Date) object;
				boolean isDateUndefined = this.isUndefinedDate(date.getStandardDate());
				existenceDateTF = new TextFieldsWithCheckBoxForDates(this.labels.getString("eaccpf.commons.unknown.date"),
												date.getContent(), isDateUndefined, date.getStandardDate(),
												"", false, "", "", false, "", false);
			} else if (object instanceof DateRange) {
				isDateRange = true;
				DateRange dateRange = (DateRange) object;
				String dateFrom = "";
				String dateFromStandard = "";
				String dateTo = "";
				String dateToStandard = "";

				if (dateRange.getFromDate() != null) {
					if (dateRange.getFromDate().getContent() != null
							&& !dateRange.getFromDate().getContent().isEmpty()) {
						dateFrom = dateRange.getFromDate().getContent();
					}
					if (dateRange.getFromDate().getStandardDate() != null
							&& !dateRange.getFromDate().getStandardDate().isEmpty()) {
						dateFromStandard = dateRange.getFromDate().getStandardDate();
					}
				}
				if (dateRange.getToDate() != null) {
					if (dateRange.getToDate().getContent() != null
							&& !dateRange.getToDate().getContent().isEmpty()) {
						dateTo = dateRange.getToDate().getContent();
					}
					if (dateRange.getToDate().getStandardDate() != null
							&& !dateRange.getToDate().getStandardDate().isEmpty()) {
						dateToStandard = dateRange.getToDate().getStandardDate();
					}
				}

				boolean isDateFromUndefined = this.isUndefinedDate(dateFromStandard);
				boolean isDateToUndefined = this.isUndefinedDate(dateToStandard);

				existenceDateTF = new TextFieldsWithCheckBoxForDates(this.labels.getString("eaccpf.commons.unknown.date"), "", false, "",
												dateFrom, isDateFromUndefined, dateFromStandard,
												dateTo, isDateToUndefined, dateToStandard, true);
			}

			// Add elements to the list.
			this.existenceDateTFs.add(existenceDateTF);

			// Add elements to the panel.
			if (isDateRange) {
				// First date row. Normal date text fields.
				builder.addLabel(this.labels.getString("eaccpf.commons.from.date"), cc.xy(1, this.rowNb));
				existenceDateTF.getDateFromTextField().addFocusListener(new AddIsoText(existenceDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_FROM));
				builder.add(existenceDateTF.getDateFromTextField(), cc.xy(3, this.rowNb));
				builder.addLabel(this.labels.getString("eaccpf.commons.to.date"), cc.xy(5, this.rowNb));
				existenceDateTF.getDateToTextField().addFocusListener(new AddIsoText(existenceDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_TO));
				builder.add(existenceDateTF.getDateToTextField(), cc.xy(7, this.rowNb));

				// Second date row. Unknown check boxes.
				this.setNextRow();
				existenceDateTF.getDateFromUndefinedCB().addActionListener(new AddUndefinedTexts(existenceDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_FROM));
				existenceDateTF.getDateToUndefinedCB().addActionListener(new AddUndefinedTexts(existenceDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_TO));
				builder.add(existenceDateTF.getDateFromUndefinedCB(), cc.xy(3, this.rowNb));
				builder.add(existenceDateTF.getDateToUndefinedCB(), cc.xy(7, this.rowNb));

				// Third date row. Standard dates.
				this.setNextRow();
				builder.addLabel(this.labels.getString("eaccpf.commons.iso.date"), cc.xy(1, this.rowNb));
				existenceDateTF.getStandardDateFromTextField().addFocusListener(new CheckIsoText(existenceDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_FROM));
				builder.add(existenceDateTF.getStandardDateFromTextField(), cc.xy(3, this.rowNb));
				builder.addLabel(this.labels.getString("eaccpf.commons.iso.date"), cc.xy(5, this.rowNb));
				existenceDateTF.getStandardDateToTextField().addFocusListener(new CheckIsoText(existenceDateTF, EacCpfIdentityPanel.UNKNOWN_DATE_TO));
				builder.add(existenceDateTF.getStandardDateToTextField(), cc.xy(7, this.rowNb));

				if (errors.contains(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES)
						&& position == 0) {
					this.setNextRow();
					builder.add(createErrorLabel(this.labels.getString("eaccpf.identity.error.empty.date.label")), cc.xyw(1, this.rowNb, 3));
				}
			} else {
				// First date row. Normal date text fields.
				builder.addLabel(this.labels.getString("eaccpf.commons.date"), cc.xy(1, this.rowNb));
				existenceDateTF.getDateTextField().addFocusListener(new AddIsoText(existenceDateTF, EacCpfIdentityPanel.UNKNOWN_DATE));
				builder.add(existenceDateTF.getDateTextField(), cc.xy(3, this.rowNb));

				// Second date row. Unknown check boxes.
				this.setNextRow();
				existenceDateTF.getDateUndefinedCB().addActionListener(new AddUndefinedTexts(existenceDateTF, EacCpfIdentityPanel.UNKNOWN_DATE));
				builder.add(existenceDateTF.getDateUndefinedCB(), cc.xy(3, this.rowNb));

				// Third date row. Standard dates.
				this.setNextRow();
				builder.addLabel(this.labels.getString("eaccpf.commons.iso.date"), cc.xy(1, this.rowNb));
				existenceDateTF.getStandardDateTextField().addFocusListener(new CheckIsoText(existenceDateTF, EacCpfIdentityPanel.UNKNOWN_DATE));
				builder.add(existenceDateTF.getStandardDateTextField(), cc.xy(3, this.rowNb));

				if (errors.contains(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES)
						&& position == 0) {
					this.setNextRow();
					builder.add(createErrorLabel(this.labels.getString("eaccpf.identity.error.empty.date.label")), cc.xyw(1, this.rowNb, 3));
				}
			}

			position++;
		}

		// Buttons to add new entries.
		this.setNextRow();
		JButton addSingleDateBtn = new ButtonTab(this.labels.getString("eaccpf.commons.add.single.date"));
		addSingleDateBtn.addActionListener(new AddSingleOrRangeDateAction(this.eaccpf, this.tabbedPane, this.model, false, false, 0));
		builder.add(addSingleDateBtn, cc.xy(1, this.rowNb));
		JButton addRangeDateBtn = new ButtonTab(this.labels.getString("eaccpf.commons.add.range.date"));
		addRangeDateBtn.addActionListener(new AddSingleOrRangeDateAction(this.eaccpf, this.tabbedPane, this.model, false, true, 0));
		builder.add(addRangeDateBtn, cc.xy(3, this.rowNb));

		return builder;
	}

	/**
	 * Method to recover all the elements Date and DateRange in the object.
	 *
	 * @param existDates
	 * @return The list of dates
	 */
	private List<Object> getAllDates(ExistDates existDates) {
		List<Object> datesList = new ArrayList<Object>();

		// Only Date element.
		if (existDates.getDate() != null) {
			datesList.add(existDates.getDate());
		}
		// Only DateRange element.
		if (existDates.getDateRange() != null) {
			datesList.add(existDates.getDateRange());
		}
		// Any combination of Date and DateRange elements.
		if (existDates.getDateSet() != null) {
			for (Object object : existDates.getDateSet().getDateOrDateRange()) {
				if (object != null) {
					datesList.add(object);
				}
			}
		}

		return datesList;
	}

	/**
	 * Method to check if the date is an undefined value.
	 *
	 * @param standardDate
	 * @return Is undefined or not.
	 */
	private boolean isUndefinedDate(String standardDate) {
		boolean result = false;

		if (standardDate != null
				&& !standardDate.isEmpty()
				&& (standardDate.equals(EacCpfIdentityPanel.UNKNOWN_INITIAL_DATE)
						|| standardDate.equals(EacCpfIdentityPanel.UNKNOWN_END_DATE))) {
			result = true;
		}
		
		return result;
	}

	/**
	 * Method to build the main buttons zone.
	 *
	 * @param builder the PanelBuilder to add the buttons.
	 * @param cc the constraints to use.
	 * @return the PanelBuilder with the buttons.
	 */
	private PanelBuilder buildButtons(PanelBuilder builder, CellConstraints cc) {
		// Row for the next tab button.
		this.setNextRow();
		builder.addSeparator("", cc.xyw(1, this.rowNb, 7));
		this.setNextRow();
		JButton nextTabBtn = new ButtonTab(this.labels.getString("eaccpf.commons.nextTab"));
		builder.add(nextTabBtn, cc.xy (5, this.rowNb));
		nextTabBtn.addActionListener(new NextTabBtnAction(this.eaccpf, this.tabbedPane, this.model));

		// Row for exit and save buttons.
		this.setNextRow();
		JButton exitBtn = new ButtonTab(this.labels.getString("eaccpf.commons.exit"));
		builder.add(exitBtn, cc.xy(1, this.rowNb));
		exitBtn.addActionListener(new ExitBtnAction());

		JButton saveBtn = new ButtonTab(labels.getString("eaccpf.commons.save"));
		builder.add(saveBtn, cc.xy (5, this.rowNb));
		saveBtn.addActionListener(new SaveBtnAction(this.eaccpf, this.tabbedPane, this.model));

		return builder;
	}

	/**
	 * Method to remove duplicate whitespaces and trim string.
	 *
	 * @param content
	 * @return the string without whitespaces.
	 */
	private String trimStringValue(String str) {
		String result = str;

		if (result!= null && !result.isEmpty()) {

			String strWith = result.trim().replaceAll("[\\s+]", " ");
			StringBuilder sb = new StringBuilder();
			boolean space = false;
			for (int i = 0; i < strWith.length() ; i ++) {
				if (!space && strWith.charAt(i) == ' ') {
					sb.append(' ');
					space = true;
				} else if (strWith.charAt(i) != ' ') {
					sb.append(strWith.charAt(i));
					space = false;
				}
			}
			result = sb.toString();
		}

		return result;
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
	 * Class to performs the addition of new row inside name part in name
	 * section if the previous values are filled.
	 */
	public class AddNamePartsAction extends UpdateEacCpfObject {
		private int currentNameEntry;

		/**
		 * Constructor.
		 *
		 * @param eacCpf
		 * @param tabbedPane
		 * @param model
		 */
		AddNamePartsAction(EacCpf eacCpf, JTabbedPane tabbedPane,
				ProfileListModel model, int counter) {
			super(eacCpf, tabbedPane, model);
			this.currentNameEntry = counter;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(false);
			} catch (EacCpfFormException e) {
//				reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
			}

			boolean empty = false;
			List<TextFieldWithComboBoxEacCpf> namePartComponentList = namePartComponentTfsWCbs.get(this.currentNameEntry);
			for (int i = 0; !empty && i < namePartComponentList.size(); i++) {
				if (StringUtils.isEmpty(trimStringValue(namePartComponentList.get(i).getTextFieldValue()))) {
					JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.identity.error.empty.name"));
					empty = true;
				}
			}

			List<NameEntry> nameEntries = getAllNameEntries();

			if (nameEntries.size() > this.currentNameEntry) {
				NameEntry nameEntry = nameEntries.get(this.currentNameEntry);
	
				nameEntry.getPart().add(new Part());
			}

			reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
		}
	}

	/**
	 * Class to performs the addition of new forms of the name in name section
	 * if the previous values are filled.
	 */
	public class AddFormNameAction extends UpdateEacCpfObject {
		/**
		 * Constructor.
		 *
		 * @param eacCpf
		 * @param tabbedPane
		 * @param model
		 */
		AddFormNameAction(EacCpf eacCpf, JTabbedPane tabbedPane,
				ProfileListModel model) {
			super(eacCpf, tabbedPane, model);
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(false);
			} catch (EacCpfFormException e) {
//				reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
			}

			boolean empty = false;
			for (int i = 0; !empty && i < namePartComponentTfsWCbs.size(); i++) {
				List<TextFieldWithComboBoxEacCpf> namePartComponentList = namePartComponentTfsWCbs.get(i);
				// Always recover the first part.
				if (StringUtils.isEmpty(trimStringValue(namePartComponentList.get(0).getTextFieldValue()))) {
					JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.identity.error.empty.name"));
					empty = true;
				}
			}

			// Add new NameEntry.
			eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().add(new NameEntry());

			reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
		}
	}

	/**
	 * Class to performs the addition of ISO values for dates or dateRanges
	 * in name section and existence section if possible.
	 */
	public class AddIsoText implements FocusListener {
		private TextFieldsWithCheckBoxForDates tfwcbfDates;
		private String dateType;

		/**
		 * Constructor.
		 *
		 * @param tfwcbfDates
		 * @param dateType
		 */
		AddIsoText(TextFieldsWithCheckBoxForDates tfwcbfDates, String dateType) {
			this.tfwcbfDates = tfwcbfDates;
			this.dateType = dateType;
		}

		@Override
		public void focusGained(FocusEvent e) {
			// No action
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (EacCpfIdentityPanel.UNKNOWN_DATE.equalsIgnoreCase(this.dateType)) {
				this.tfwcbfDates.getStandardDateTextField().setText(parseStandardDate(this.tfwcbfDates.getDateValue()));
			} else if (EacCpfIdentityPanel.UNKNOWN_DATE_FROM.equalsIgnoreCase(this.dateType)) {
				this.tfwcbfDates.getStandardDateFromTextField().setText(parseStandardDate(this.tfwcbfDates.getDateFromValue()));
			} else if (EacCpfIdentityPanel.UNKNOWN_DATE_TO.equalsIgnoreCase(this.dateType)) {
				this.tfwcbfDates.getStandardDateToTextField().setText(parseStandardDate(this.tfwcbfDates.getDateToValue()));
			}
		}
	}

	/**
	 * Class to performs the checks of ISO values for dates or dateRanges
	 * in name section and existence section if possible.
	 */
	public class CheckIsoText implements FocusListener {
		private TextFieldsWithCheckBoxForDates tfwcbfDates;
		private String dateType;

		/**
		 * Constructor.
		 *
		 * @param tfwcbfDates
		 * @param dateType
		 */
		CheckIsoText(TextFieldsWithCheckBoxForDates tfwcbfDates, String dateType) {
			this.tfwcbfDates = tfwcbfDates;
			this.dateType = dateType;
		}

		@Override
		public void focusGained(FocusEvent e) {
			// No action
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (EacCpfIdentityPanel.UNKNOWN_DATE.equalsIgnoreCase(this.dateType)) {
				if (!this.tfwcbfDates.getStandardDateValue().isEmpty()) {
					if (parseStandardDate(this.tfwcbfDates.getStandardDateValue()).isEmpty()) {
						JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.commons.error.no.standard.date"));
						this.tfwcbfDates.getStandardDateTextField().setText("");
					}
				}
			} else if (EacCpfIdentityPanel.UNKNOWN_DATE_FROM.equalsIgnoreCase(this.dateType)) {
				if (!this.tfwcbfDates.getStandardDateFromValue().isEmpty()) {
					if (parseStandardDate(this.tfwcbfDates.getStandardDateFromValue()).isEmpty()) {
						JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.commons.error.no.standard.date"));
						this.tfwcbfDates.getStandardDateFromTextField().setText("");
					}
				}
			} else if (EacCpfIdentityPanel.UNKNOWN_DATE_TO.equalsIgnoreCase(this.dateType)) {
				if (!this.tfwcbfDates.getStandardDateToValue().isEmpty()) {
					if (parseStandardDate(this.tfwcbfDates.getStandardDateToValue()).isEmpty()) {
						JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.commons.error.no.standard.date"));
						this.tfwcbfDates.getStandardDateToTextField().setText("");
					}
				}
			}
		}
	}

	/**
	 * Class to performs the addition of unknown values for dates or dateRanges
	 * in name section and existence section.
	 */
	public class AddUndefinedTexts implements ActionListener {
		private TextFieldsWithCheckBoxForDates tfwcbfDates;
		private String dateType;

		/**
		 * Constructor.
		 *
		 * @param tfwcbfDates
		 * @param dateType
		 */
		AddUndefinedTexts(TextFieldsWithCheckBoxForDates tfwcbfDates, String dateType) {
			this.tfwcbfDates = tfwcbfDates;
			this.dateType = dateType;
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (EacCpfIdentityPanel.UNKNOWN_DATE.equalsIgnoreCase(this.dateType)) {
				// Check if event is select or deselect for Date.
				if (this.tfwcbfDates.isSelectedDateUndefinedCB()) {
					// Date unknown.
					this.tfwcbfDates.getDateTextField().setText(labels.getString("eaccpf.commons.unknown.date"));
					this.tfwcbfDates.getDateTextField().setEditable(false);
					this.tfwcbfDates.getStandardDateTextField().setText(EacCpfIdentityPanel.UNKNOWN_INITIAL_DATE);
						this.tfwcbfDates.getStandardDateTextField().setEditable(false);
				} else {
					// Date known.
					this.tfwcbfDates.getDateTextField().setText("");
					this.tfwcbfDates.getDateTextField().setEditable(true);
					this.tfwcbfDates.getStandardDateTextField().setText("");
					this.tfwcbfDates.getStandardDateTextField().setEditable(true);
				}
			} else if (EacCpfIdentityPanel.UNKNOWN_DATE_FROM.equalsIgnoreCase(this.dateType)) {
				// Check if event is select or deselect for FromDate.
				if (this.tfwcbfDates.isSelectedDateFromUndefinedCB()) {
					// FromDate unknown.
					this.tfwcbfDates.getDateFromTextField().setText(labels.getString("eaccpf.commons.unknown.date"));
					this.tfwcbfDates.getDateFromTextField().setEditable(false);
					this.tfwcbfDates.getStandardDateFromTextField().setText(EacCpfIdentityPanel.UNKNOWN_INITIAL_DATE);
					this.tfwcbfDates.getStandardDateFromTextField().setEditable(false);
				} else {
					// FromDate known.
					this.tfwcbfDates.getDateFromTextField().setText("");
					this.tfwcbfDates.getDateFromTextField().setEditable(true);
					this.tfwcbfDates.getStandardDateFromTextField().setText("");
					this.tfwcbfDates.getStandardDateFromTextField().setEditable(true);
				}
			} else if (EacCpfIdentityPanel.UNKNOWN_DATE_TO.equalsIgnoreCase(this.dateType)) {
				// Check if event is select or deselect for ToDate.
				if (this.tfwcbfDates.isSelectedDateToUndefinedCB()) {
					// ToDate unknown.
					this.tfwcbfDates.getDateToTextField().setText(labels.getString("eaccpf.commons.unknown.date"));
					this.tfwcbfDates.getDateToTextField().setEditable(false);
					this.tfwcbfDates.getStandardDateToTextField().setText(EacCpfIdentityPanel.UNKNOWN_END_DATE);
					this.tfwcbfDates.getStandardDateToTextField().setEditable(false);
				} else {
					// ToDate known.
					this.tfwcbfDates.getDateToTextField().setText("");
					this.tfwcbfDates.getDateToTextField().setEditable(true);
					this.tfwcbfDates.getStandardDateToTextField().setText("");
					this.tfwcbfDates.getStandardDateToTextField().setEditable(true);
				}
			}
		}
	}

	/**
	 * Class to performs the addition of new dates or dateRanges in name
	 * section and existence section if the previous values are filled.
	 */
	public class AddSingleOrRangeDateAction extends UpdateEacCpfObject {
		private boolean isNameSection;
		private boolean isDateRange;
		private int currentNameEntry;

		/**
		 * Constructor.
		 *
		 * @param eacCpf
		 * @param tabbedPane
		 * @param model
		 * @param isNameSection
		 * @param isDateRange
		 */
		AddSingleOrRangeDateAction(EacCpf eacCpf, JTabbedPane tabbedPane,
				ProfileListModel model, boolean isNameSection, boolean isDateRange, int index) {
			super(eacCpf, tabbedPane, model);
			this.isNameSection = isNameSection;
			this.isDateRange = isDateRange;
			this.currentNameEntry = index;
		}

		// TODO: Simplify this class.
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(false);
			} catch (EacCpfFormException e) {
//				reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
			}

			boolean empty = false;
			boolean partial = false;
			List<TextFieldsWithCheckBoxForDates> textFieldsWithCheckBoxForDatesList = null;
			if (this.isNameSection) {
				textFieldsWithCheckBoxForDatesList = useDateTFs.get(this.currentNameEntry);
			} else {
				textFieldsWithCheckBoxForDatesList = existenceDateTFs;
			}

			for (int i = 0; !empty && i < textFieldsWithCheckBoxForDatesList.size(); i++) {
				TextFieldsWithCheckBoxForDates textFieldsWithCheckBoxForDates = textFieldsWithCheckBoxForDatesList.get(i);

				// Check if is date or dateRange
				if (!this.isDateRange && !textFieldsWithCheckBoxForDates.isDateRange()) {
					// Checks if some value is empty
					if (StringUtils.isEmpty(trimStringValue(textFieldsWithCheckBoxForDates.getDateValue()))) {
						JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.commons.error.empty.single.date"));
						empty = true;
					}
				} else  if (this.isDateRange && textFieldsWithCheckBoxForDates.isDateRange()) {
					// Checks if some value is empty
					if (StringUtils.isEmpty(trimStringValue(textFieldsWithCheckBoxForDates.getDateFromValue()))
							|| StringUtils.isEmpty(trimStringValue(textFieldsWithCheckBoxForDates.getDateToValue()))) {
						JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.commons.error.empty.range.date"));
						partial = !(StringUtils.isEmpty(trimStringValue(textFieldsWithCheckBoxForDates.getDateFromValue()))
								&& StringUtils.isEmpty(trimStringValue(textFieldsWithCheckBoxForDates.getDateToValue())));
						empty = true;
					}
				} else if (textFieldsWithCheckBoxForDates.isDateRange() && !partial) {
					partial = StringUtils.isEmpty(trimStringValue(textFieldsWithCheckBoxForDates.getDateFromValue()))
							|| StringUtils.isEmpty(trimStringValue(textFieldsWithCheckBoxForDates.getDateToValue()));
				}
			}

			// Add new Date.
			ExistDates existDates = null;
			UseDates useDates = null;
			List<NameEntry> nameEntryList = null;
			if (this.isNameSection) {
				nameEntryList = getAllNameEntries();
				if (nameEntryList.size() > this.currentNameEntry) {
					useDates = nameEntryList.get(this.currentNameEntry).getUseDates();
				}
			} else {
				existDates = eaccpf.getCpfDescription().getDescription().getExistDates();
			}

			if (!empty) {
				if (this.isNameSection) {
					if (useDates != null) {
						UseDates newUseDates = new UseDates();
						if (useDates.getDate() != null) {
							DateSet dateSet = new DateSet();
							dateSet.getDateOrDateRange().add(useDates.getDate());
							if (!this.isDateRange) {
								dateSet.getDateOrDateRange().add(new Date());
							} else if (!partial) {
								dateSet.getDateOrDateRange().add(new DateRange());
							}
		
							newUseDates.setDateSet(dateSet);
						} else if (useDates.getDateRange() != null) {
							DateSet dateSet = new DateSet();
							dateSet.getDateOrDateRange().add(useDates.getDateRange());
							if (!this.isDateRange) {
								dateSet.getDateOrDateRange().add(new Date());
							} else if (!partial) {
								dateSet.getDateOrDateRange().add(new DateRange());
							}
		
							newUseDates.setDateSet(dateSet);
						} else if (useDates.getDateSet() != null) {
							DateSet dateSet = useDates.getDateSet();
							if (!this.isDateRange) {
								dateSet.getDateOrDateRange().add(new Date());
							} else if (!partial) {
								dateSet.getDateOrDateRange().add(new DateRange());
							}
		
							newUseDates.setDateSet(dateSet);
						} else {
							if (!this.isDateRange) {
								newUseDates.setDate(new Date());
							} else {
								newUseDates.setDateRange(new DateRange());
							}
						}

						int position = eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().indexOf(nameEntryList.get(this.currentNameEntry));
						NameEntry nameEntry = (NameEntry) eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().get(position);
						nameEntry.setUseDates(newUseDates);
//						useDates = newUseDates;
					} else {
						useDates = new UseDates();
						if (!this.isDateRange) {
							useDates.setDate(new Date());
						} else {
							useDates.setDateRange(new DateRange());
						}
						if (eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().size() > this.currentNameEntry) {
							int position = eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().indexOf(nameEntryList.get(this.currentNameEntry));
							NameEntry nameEntry = (NameEntry) eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().get(position);
							nameEntry.setUseDates(useDates);	
						} else {
							NameEntry nameEntry = new NameEntry();
							nameEntry.setUseDates(useDates);
							eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().add(nameEntry);
						}
					}
				} else  {
					if (existDates != null) {
						ExistDates newExistDates = new ExistDates();
						if (existDates.getDate() != null && !partial) {
							DateSet dateSet = new DateSet();
							dateSet.getDateOrDateRange().add(existDates.getDate());
							if (!this.isDateRange) {
								dateSet.getDateOrDateRange().add(new Date());
							} else /*if (!partial)*/ {
								dateSet.getDateOrDateRange().add(new DateRange());
							}

							newExistDates.setDateSet(dateSet);
						} else if (existDates.getDateRange() != null && !partial) {
							if (existDates.getDateRange().getFromDate() != null
									&& StringUtils.isNotEmpty(existDates.getDateRange().getFromDate().getContent())
									&& existDates.getDateRange().getToDate() != null
									&& StringUtils.isNotEmpty(existDates.getDateRange().getToDate().getContent())) {
								DateSet dateSet = new DateSet();
								dateSet.getDateOrDateRange().add(existDates.getDateRange());
								if (!this.isDateRange) {
									dateSet.getDateOrDateRange().add(new Date());
								} else /*if (!partial)*/ {
									dateSet.getDateOrDateRange().add(new DateRange());
								}

								newExistDates.setDateSet(dateSet);
							} else {
								newExistDates = existDates;
							}
						} else if (existDates.getDateSet() != null && !partial) {
							DateSet dateSet = existDates.getDateSet();
							if (!this.isDateRange) {
								dateSet.getDateOrDateRange().add(new Date());
							} else /*if (!partial)*/ {
								dateSet.getDateOrDateRange().add(new DateRange());
							}

							newExistDates.setDateSet(dateSet);
						} else if (!partial) {
							if (!this.isDateRange) {
								newExistDates.setDate(new Date());
							} else {
								newExistDates.setDateRange(new DateRange());
							}
						} else {
							newExistDates = existDates;
						}
						eaccpf.getCpfDescription().getDescription().setExistDates(newExistDates);
					} else {
						existDates = new ExistDates();
						if (!this.isDateRange) {
							existDates.setDate(new Date());
						} else {
							existDates.setDateRange(new DateRange());
						}

						eaccpf.getCpfDescription().getDescription().setExistDates(existDates);
						if (this.errors.contains(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES)) {
							this.errors.remove(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES);
						}
					}
				}
			} else {
				if (this.isNameSection) {
					if (useDates != null) {
						if (useDates.getDateSet() != null && !partial) {
							if (!this.isDateRange) {
								useDates.getDateSet().getDateOrDateRange().add(new Date());
							} else /*if (!partial)*/ {
								useDates.getDateSet().getDateOrDateRange().add(new DateRange());
							}
						} else if (useDates.getDate() == null
								&& useDates.getDateRange() == null
								&& useDates.getDateSet() == null) {
							if (!this.isDateRange) {
								useDates.setDate(new Date());
							} else {
								useDates.setDateRange(new DateRange());
							}
						} else if (useDates.getDate() != null) {
							DateSet dateSet = new DateSet();
							dateSet.getDateOrDateRange().add(useDates.getDate());
							if (!this.isDateRange) {
								dateSet.getDateOrDateRange().add(new Date());
							} else {
								dateSet.getDateOrDateRange().add(new DateRange());
							}

							useDates.setDate(null);
							useDates.setDateSet(dateSet);
						} else if (useDates.getDateRange() != null && !partial) {
							DateSet dateSet = new DateSet();
							dateSet.getDateOrDateRange().add(useDates.getDateRange());
							if (!this.isDateRange) {
								dateSet.getDateOrDateRange().add(new Date());
							} else {
								dateSet.getDateOrDateRange().add(new DateRange());
							}

							useDates.setDateRange(null);
							useDates.setDateSet(dateSet);
						}
					} else {
						useDates = new UseDates();
						if (!this.isDateRange) {
							useDates.setDate(new Date());
						} else {
							useDates.setDateRange(new DateRange());
						}
						if (eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().size() > this.currentNameEntry) {
							int position = eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().indexOf(nameEntryList.get(this.currentNameEntry));
							NameEntry nameEntry = (NameEntry) eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().get(position);
							nameEntry.setUseDates(useDates);	
						} else {
							NameEntry nameEntry = new NameEntry();
							nameEntry.setUseDates(useDates);
							eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry().add(nameEntry);
						}
					}
				} else {
					if (existDates != null) {
						if (existDates.getDateSet() != null && !partial) {
							if (!this.isDateRange) {
								existDates.getDateSet().getDateOrDateRange().add(new Date());
							} else /*if (!partial)*/ {
								existDates.getDateSet().getDateOrDateRange().add(new DateRange());
							}
						} else if (existDates.getDate() == null
								&& existDates.getDateRange() == null
								&& existDates.getDateSet() == null) {
							if (!this.isDateRange) {
								existDates.setDate(new Date());
							} else {
								existDates.setDateRange(new DateRange());
							}
						} else if (existDates.getDate() != null) {
							DateSet dateSet = new DateSet();
							dateSet.getDateOrDateRange().add(existDates.getDate());
							if (!this.isDateRange) {
								dateSet.getDateOrDateRange().add(new Date());
							} else {
								dateSet.getDateOrDateRange().add(new DateRange());
							}

							existDates.setDate(null);
							existDates.setDateSet(dateSet);
						} else if (existDates.getDateRange() != null && !partial) {
							DateSet dateSet = new DateSet();
							dateSet.getDateOrDateRange().add(existDates.getDateRange());
							if (!this.isDateRange) {
								dateSet.getDateOrDateRange().add(new Date());
							} else {
								dateSet.getDateOrDateRange().add(new DateRange());
							}

							existDates.setDateRange(null);
							existDates.setDateSet(dateSet);
						}
					} else {
						existDates = new ExistDates();
						if (!this.isDateRange) {
							existDates.setDate(new Date());
						} else {
							existDates.setDateRange(new DateRange());
						}

						eaccpf.getCpfDescription().getDescription().setExistDates(existDates);
						if (this.errors.contains(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES)) {
							this.errors.remove(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES);
						}
					}
				}
			}

			reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
		}
	}

	/**
	 * Class to performs the addition of new row inside identifiers section if
	 * the previous values are filled.
	 */
	public class AddIdentifierAction extends UpdateEacCpfObject {
		/**
		 * Constructor.
		 *
		 * @param eacCpf
		 * @param tabbedPane
		 * @param model
		 */
		AddIdentifierAction(EacCpf eacCpf, JTabbedPane tabbedPane,
				ProfileListModel model) {
			super(eacCpf, tabbedPane, model);
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(false);
			} catch (EacCpfFormException e) {
				reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
			}

			boolean empty = false;
			for (int i = 0; !empty && i < identifierTfs.size(); i++) {
				if (StringUtils.isEmpty(trimStringValue(identifierTfs.get(i).getText()))
						/*|| StringUtils.isEmpty(trimStringValue(identifierTypeTfs.get(i).getText()))*/) {
					JOptionPane.showMessageDialog(eacCpfFrame, labels.getString("eaccpf.identity.error.empty.identifier"));
					empty = true;
				}
			}

			eaccpf.getCpfDescription().getIdentity().getEntityId().add(new EntityId());

			reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
		}
	}

	/**
	 * Class to performs the actions when the user clicks on button save.
	 */
	public class SaveBtnAction extends UpdateEacCpfObject {
		/**
		 * Constructor.
		 *
		 * @param eaccpf
		 * @param tabbedPane
		 * @param model
		 */
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
				reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
			}
		}
	}

	/**
	 * Class to performs the actions when the user clicks on button for next tab.
	 */
	public class NextTabBtnAction extends UpdateEacCpfObject {
		/**
		 * Constructor.
		 *
		 * @param eaccpf
		 * @param tabbedPane
		 * @param model
		 */
		NextTabBtnAction(EacCpf eaccpf, JTabbedPane tabbedPane, ProfileListModel model) {
			super(eaccpf, tabbedPane, model);
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			try {
				super.updateJAXBObject(true);
				removeChangeListener();

				reloadTabbedPanel(new EacCpfDescriptionPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels).buildEditorPanel(errors), 1);
			} catch (EacCpfFormException e) {
				reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
			}
		}
	}

	/**
	 * Class for update the JABX EAC-CPF object.
	 */
	public abstract class UpdateEacCpfObject extends DefaultBtnAction {
		/**
		 * Constructor.
		 *
		 * @param eacCpf
		 * @param tabbedPane
		 * @param model
		 */
		UpdateEacCpfObject(EacCpf eacCpf, JTabbedPane tabbedPane, ProfileListModel model) {
			super(eacCpf, tabbedPane, model);
		}

		protected void updateJAXBObject(boolean save) throws EacCpfFormException {
			errors = new ArrayList<String>();
//			boolean hasChanged = false;

			// Call method to save the maintenance information.
			if (this.eaccpf.getControl() == null) {
				this.eaccpf.setControl(new Control());
			}
			this.updateMaintenanceInformation(this.eaccpf.getControl(), save);

			// TODO: Temporary identifier.
			//eaccpf.getControl().getRecordId().getValue()
			if (this.eaccpf != null) {
				if (this.eaccpf.getControl() == null) {
					this.eaccpf.setControl(new Control());
				}

				if (this.eaccpf.getControl().getRecordId() == null
						|| this.eaccpf.getControl().getRecordId().getValue() == null
						|| this.eaccpf.getControl().getRecordId().getValue().isEmpty()) {
					RecordId recordId = new RecordId();
					recordId.setValue("Temporary_id");
	
					this.eaccpf.getControl().setRecordId(recordId);
				}
			}
			// End TODO: Temporary identifier.

			// Define values of the section "Identity".
			if (this.eaccpf.getCpfDescription() == null) {
				this.eaccpf.setCpfDescription(new CpfDescription());
			}
			CpfDescription cpfDescription = this.eaccpf.getCpfDescription();

			if (cpfDescription.getIdentity() == null) {
				cpfDescription.setIdentity(new Identity());
			}
			Identity identity = cpfDescription.getIdentity();


			// Save entity type.
			this.updateEntityType(identity);

			// Save full name or part of the name.
			this.updateFullName(identity);

			// Save identifier.
			this.updateIdentifier(identity);

			// Define values of the section "Description".
			if (this.eaccpf.getCpfDescription().getDescription() == null) {
				this.eaccpf.getCpfDescription().setDescription(new Description());
			}
			Description description = this.eaccpf.getCpfDescription().getDescription();

			// Save existence dates.
			this.updateExistenceDates(description);

			if(!errors.isEmpty()) {
				throw new EacCpfFormException("Errors in validation of EAC-CPF");
			}
		}

		/**
		 * Method to update the maintenance information of the apeEAC-CPF file.
		 *
		 * @param control
		 * @param save
		 * @return boolean value.
		 */
		private boolean updateMaintenanceInformation(Control control, boolean save) {
			boolean hasChanged = false;

			// Update maintenance agency.
			hasChanged = this.updateMaintenanceAgency(control);

			if (save) {
				// Update maintenance status.
				hasChanged = this.updateMaintenanceStatus(control);

				// Update language declaration.
				hasChanged = this.updateLanguageDeclaration(control);

				// Update convention declaration.
				hasChanged = this.updateConventionDeclaration(control);

				// Update maintenance history.
				hasChanged = this.updateMaintenanceHistory(control);
			}

			return hasChanged;
		}

		/**
		 * Method to update the maintenance status of the apeEAC-CPF file.
		 *
		 * @param control
		 * @return boolean value.
		 */
		private boolean updateMaintenanceStatus(Control control) {
			boolean hasChanged = false;

			// Maintenance status.
			if (control.getMaintenanceStatus() == null) {
	            MaintenanceStatus maintenanceStatus = new MaintenanceStatus();
	            maintenanceStatus.setValue(EacCpfIdentityPanel.MAINTENANCE_EVENT_NEW);
	            control.setMaintenanceStatus(maintenanceStatus);
	            hasChanged = true;
	        } else {
	            control.getMaintenanceStatus().setValue(EacCpfIdentityPanel.MAINTENANCE_EVENT_REVISED);
	            hasChanged = true;
	        }

			return hasChanged;
		}

		/**
		 * Method to update the maintenance agency of the apeEAC-CPF file.
		 *
		 * @param control
		 * @return boolean value.
		 */
		private boolean updateMaintenanceAgency(Control control) {
			boolean hasChanged = false;

			// Maintenance agency.
	        if (control.getMaintenanceAgency() == null) {
	            control.setMaintenanceAgency(new MaintenanceAgency());
	        }

	        // Maintenance agency code.
	        if (StringUtils.isNotEmpty(mainagencycode)) {
		        if (control.getMaintenanceAgency().getAgencyCode() == null) {
		            control.getMaintenanceAgency().setAgencyCode(new AgencyCode());
		        }
		        control.getMaintenanceAgency().getAgencyCode().setValue(mainagencycode);
	        }

	        // Maintenance agency name.
	        if (control.getMaintenanceAgency().getAgencyName() == null) {
                control.getMaintenanceAgency().setAgencyName(new AgencyName());
            }
	        control.getMaintenanceAgency().getAgencyName().setContent(EacCpfIdentityPanel.MAINTENANCE_AGENCY_NAME);
	        hasChanged = true;

			return hasChanged;
		}

		/**
		 * Method to update the language declaration of the apeEAC-CPF file.
		 *
		 * @param control
		 * @return boolean value.
		 */
		private boolean updateLanguageDeclaration(Control control) {
			boolean hasChanged = false;

	        // Language declaration.
	        if ((StringUtils.isNotEmpty(firstLanguage)
	        		&& !firstLanguage.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE))
	        		|| (StringUtils.isNotEmpty(firstScript)
        				&& !firstScript.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE))) {
	        	LanguageDeclaration languageDeclaration = new LanguageDeclaration();

	        	if (!firstLanguage.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
	        		Language language = new Language();
	        		language.setLanguageCode(firstLanguage);
	        		languageDeclaration.setLanguage(language);
	        	}

	        	if (!firstScript.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
	        		Script script = new Script();
	        		script.setScriptCode(firstScript);
	        		languageDeclaration.setScript(script);
	        	}
	        	control.setLanguageDeclaration(languageDeclaration);
	        	hasChanged = true;
	        }

			return hasChanged;
		}

		/**
		 * Method to update the convention declaration of the apeEAC-CPF file.
		 *
		 * @param control
		 * @return boolean value.
		 */
		private boolean updateConventionDeclaration(Control control) {
			boolean hasChanged = false;

	        // Convention declaration items.
	        String[] conventionValues = {"ISO 639-2b", "ISO 3166-1", "ISO 8601", "ISO 15511", "ISO 15924"};
	        for (String conventionValue : conventionValues) {
	            ConventionDeclaration conventionDeclaration = new ConventionDeclaration();
	            Abbreviation abbreviation = new Abbreviation();

	            abbreviation.setValue(conventionValue);
	            conventionDeclaration.setAbbreviation(abbreviation);
	            conventionDeclaration.setCitation(new Citation());
	            control.getConventionDeclaration().add(conventionDeclaration);
	            hasChanged = true;
	        }

			return hasChanged;
		}

		/**
		 * Method to update the maintenance history of the apeEAC-CPF file.
		 *
		 * @param control
		 * @return boolean value.
		 */
		private boolean updateMaintenanceHistory(Control control) {
			boolean hasChanged = false;

			// Maintenance time.
			if (EacCpfFrame.getTimeMaintenance() == null) {
				EacCpfFrame.setTimeMaintenance(new java.util.Date());
			}

			// Maintenance event size.
			int sizeMaintenanceEvents = control.getMaintenanceHistory().getMaintenanceEvent().size();
			String languagePerson = "";

			if (sizeMaintenanceEvents > 0) {
				MaintenanceEvent maintenanceEvent = control.getMaintenanceHistory().getMaintenanceEvent().get(sizeMaintenanceEvents - 1);
				languagePerson = maintenanceEvent.getAgent().getLang();
			} else if (!firstLanguage.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
				languagePerson = firstLanguage;
			}

			MaintenanceEvent maintenanceEvent = TextChanger.getEacCpfMaintenanceEventSaved(EacCpfFrame.getTimeMaintenance(), control.getMaintenanceHistory().getMaintenanceEvent());
			if (maintenanceEvent == null) {
				maintenanceEvent = new MaintenanceEvent();
				maintenanceEvent.setAgent(new Agent());
				maintenanceEvent.setEventType(new EventType());
				if (sizeMaintenanceEvents == 0) {
					maintenanceEvent.getEventType().setValue(EacCpfIdentityPanel.MAINTENANCE_EVENT_CREATED);
				} else {
					maintenanceEvent.getEventType().setValue(EacCpfIdentityPanel.MAINTENANCE_EVENT_REVISED);
				}
				maintenanceEvent.setAgentType(new AgentType());
				maintenanceEvent.getAgentType().setValue(EacCpfIdentityPanel.MAINTENANCE_AGENT_HUMAN);
				maintenanceEvent.setEventDateTime(new EventDateTime());
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat formatStandard = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                maintenanceEvent.getEventDateTime().setContent(format.format(EacCpfFrame.getTimeMaintenance()));
                maintenanceEvent.getEventDateTime().setStandardDateTime(formatStandard.format(EacCpfFrame.getTimeMaintenance()));
			} else {
				control.getMaintenanceHistory().getMaintenanceEvent().remove(maintenanceEvent);
			}

			maintenanceEvent.getAgent().setContent(EacCpfIdentityPanel.MAINTENANCE_AGENT_HUMAN);

			if (StringUtils.isNotEmpty(languagePerson)) {
				maintenanceEvent.getAgent().setLang(languagePerson);
			}

			control.getMaintenanceHistory().getMaintenanceEvent().add(maintenanceEvent);
			hasChanged = true;

			return hasChanged;
		}

		/**
		 * Method to update the entity type in the object.
		 *
		 * @param identity
		 * @return boolean value.
		 */
		private boolean updateEntityType(Identity identity) {
			boolean hasChanged = false;
			// Save entity type.
			if (entityType != null && this.eaccpf != null) {
				EntityType type = new EntityType();
				type.setValue(entityType.getName());

				identity.setEntityType(type);
				hasChanged = true;
			}

			return hasChanged;
		}

		/**
		 * Method to update the name in the object.
		 *
		 * @param identity
		 * @return boolean value.
		 */
		private boolean updateFullName(Identity identity) {
			boolean hasChanged = false;
			// Save full name or part of the name.
			identity.getNameEntryParallelOrNameEntry().clear();
			List<NameEntry> nameEntries = new ArrayList<NameEntry>();

			for (int i = 0; i < namePartComponentTfsWCbs.size(); i++) {
				String lang = nameLanguageCbs.get(i).getLanguage();

				hasChanged = false;
				List<TextFieldWithComboBoxEacCpf> namePartComponentTfsWCbList = namePartComponentTfsWCbs.get(i);
				NameEntry nameEntry = new NameEntry();
				for (TextFieldWithComboBoxEacCpf textFieldWithComboBoxEacCpf : namePartComponentTfsWCbList) {
					// Save part element.
					String namePart = trimStringValue(textFieldWithComboBoxEacCpf.getTextFieldValue());
					String nameComponent = trimStringValue(textFieldWithComboBoxEacCpf.getComboBoxValue(TextFieldWithComboBoxEacCpf.TYPE_COMPONENT, entityType));

					if (StringUtils.isNotEmpty(namePart)) {
						Part part = new Part();
						part.setContent(namePart);
						// @LocalType.
						if (StringUtils.isNotEmpty(nameComponent) && !nameComponent.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
							part.setLocalType(nameComponent);
						}

						// @Lang
						if (StringUtils.isNotEmpty(lang) && !lang.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
							part.setLang(lang);
						}

						nameEntry.getPart().add(part);
						hasChanged = true;
					}
				}

				// Save nameEntry attributes.
				// Form value.
				String formValue = nameFormTfsWCbs.get(i).getComboBoxValue(TextFieldWithComboBoxEacCpf.TYPE_FORM, entityType);
				if (StringUtils.isNotEmpty(formValue) && !formValue.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
					nameEntry.setLocalType(formValue);
				}

				// Lang.
//				String lang = nameLanguageCbs.get(i).getLanguage();
//				if (StringUtils.isNotEmpty(lang) && !lang.equalsIgnoreCase(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
//					nameEntry.setLang(lang);
//				}

				// Use dates.
				List<TextFieldsWithCheckBoxForDates> useDatesTfsWCbList = useDateTFs.get(i);
				/*hasChanged = */this.updateUseDates(nameEntry, useDatesTfsWCbList);

				if (hasChanged) {
					nameEntries.add(nameEntry);
				} else {
					this.errors.add(EacCpfIdentityPanel.ERROR_NAME_PART + i);
				}
			}

			// TODO: Check how to create nameEntryparallel.
//			if (nameEntries.size() > 1) {
//				NameEntryParallel nameEntryParallel = new NameEntryParallel();
//				nameEntryParallel.getContent().addAll(nameEntries);
//				identity.getNameEntryParallelOrNameEntry().add(nameEntryParallel);
//			} else {
				identity.getNameEntryParallelOrNameEntry().addAll(nameEntries);
//			}

			return hasChanged;
		}

		/**
		 * Method to update the use dates of the name in the object.
		 *
		 * @param nameEntry
		 * @param useDatesTfsWCbList
		 * @return boolean value.
		 */
		private boolean updateUseDates(NameEntry nameEntry, List<TextFieldsWithCheckBoxForDates> useDatesTfsWCbList) {
			boolean hasChanged = false;
			UseDates useDates = new UseDates();

			// Save use dates of the name.
			if (useDatesTfsWCbList != null
					&& !useDatesTfsWCbList.isEmpty()
					&& useDatesTfsWCbList.size() == 1
					&& !useDatesTfsWCbList.get(0).isDateRange()) {
				// Date.
				Date date = this.fillDateValues(useDatesTfsWCbList.get(0));
				if (date != null) {
					useDates.setDate(date);
				}
			} else if (useDatesTfsWCbList != null
					&& !useDatesTfsWCbList.isEmpty()
					&& useDatesTfsWCbList.size() == 1
					&& useDatesTfsWCbList.get(0).isDateRange()) {
				// DateRange.
				DateRange dateRange = this.fillDateRangeValues(useDatesTfsWCbList.get(0));

				if (dateRange != null) {
					useDates.setDateRange(dateRange);
				}
			} else if (useDatesTfsWCbList != null
					&& !useDatesTfsWCbList.isEmpty()
					&& useDatesTfsWCbList.size() > 1) {
				// DateSet.
				DateSet dateSet = new DateSet();

				for (TextFieldsWithCheckBoxForDates tfwcbfDates: useDatesTfsWCbList) {
					if (tfwcbfDates.isDateRange()) {
						// DateRange.
						DateRange dateRange = this.fillDateRangeValues(tfwcbfDates);

						if (dateRange != null) {
							dateSet.getDateOrDateRange().add(dateRange);
						}
					} else {
						// Date.
						Date date = this.fillDateValues(tfwcbfDates);

						if (date != null) {
							dateSet.getDateOrDateRange().add(date);
						}
					}
				}

				if (dateSet.getDateOrDateRange().size() == 1) {
					Object object = dateSet.getDateOrDateRange().get(0);

					// Date.
					if (object instanceof Date) {
						Date date = (Date) object;
						if (StringUtils.isNotEmpty(trimStringValue(date.getContent()))) {
							useDates.setDate(date);
							hasChanged = true;
						}
					}

					// DateRange.
					if (object instanceof DateRange) {
						DateRange dateRange = (DateRange) object;
						if (dateRange.getFromDate() != null
								&& StringUtils.isNotEmpty(trimStringValue(dateRange.getFromDate().getContent()))
								&& dateRange.getToDate() != null
								&& StringUtils.isNotEmpty(trimStringValue(dateRange.getToDate().getContent()))) {
							useDates.setDateRange(dateRange);
							hasChanged = true;
						}
					}
				} else {
					useDates.setDateSet(dateSet);
					hasChanged = true;
				}
			}

			// Add the new content.
			if (useDates.getDate() != null
					|| useDates.getDateRange() != null
					|| useDates.getDateSet() != null) {
				nameEntry.setUseDates(useDates);
				hasChanged = true;
			}

			return hasChanged;
		}

		/**
		 * Method to update the identifiers in the object.
		 *
		 * @param identity
		 * @return boolean value.
		 */
		private boolean updateIdentifier(Identity identity) {
			boolean hasChanged = false;
			// Save identifier.
			identity.getEntityId().clear();
			for (int i = 0; i < identifierTfs.size(); i++) {
				JTextField identifier = identifierTfs.get(i);
				JTextField identifierType = identifierTypeTfs.get(i);
				if (StringUtils.isNotEmpty(trimStringValue(identifier.getText()))) {
					EntityId entityId = new EntityId();
					entityId.setContent(trimStringValue(identifier.getText()));
					if (StringUtils.isNotEmpty(trimStringValue(identifierType.getText()))) {
						entityId.setLocalType(trimStringValue(identifierType.getText()));
					}

					identity.getEntityId().add(entityId);
					hasChanged = true;
				}
			}

			return hasChanged;
		}

		/**
		 * Method to update the dates of existence in the object.
		 *
		 * @param description
		 * @return boolean value.
		 */
		private boolean updateExistenceDates(Description description) {
			boolean hasChanged = false;
			ExistDates existDates = new ExistDates();

			// Save dates of existence.
			if (existenceDateTFs != null
					&& !existenceDateTFs.isEmpty()
					&& existenceDateTFs.size() == 1
					&& !existenceDateTFs.get(0).isDateRange()) {
				// Date.
				Date date = this.fillDateValues(existenceDateTFs.get(0));
				if (date != null) {
					existDates.setDate(date);
					hasChanged = true;
				} else {
					this.errors.add(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES);
				}
			} else if (existenceDateTFs != null
					&& !existenceDateTFs.isEmpty()
					&& existenceDateTFs.size() == 1
					&& existenceDateTFs.get(0).isDateRange()) {
				// DateRange.
				DateRange dateRange = this.fillDateRangeValues(existenceDateTFs.get(0));

				if (dateRange != null) {
					if (dateRange.getFromDate() == null
							|| StringUtils.isEmpty(trimStringValue(dateRange.getFromDate().getContent()))
							|| dateRange.getToDate() == null
							|| StringUtils.isEmpty(trimStringValue(dateRange.getToDate().getContent()))) {
						this.errors.add(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES);
					}

					existDates.setDateRange(dateRange);
					hasChanged = true;
				} else {
					this.errors.add(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES);
				}
			} else if (existenceDateTFs != null
					&& !existenceDateTFs.isEmpty()
					&& existenceDateTFs.size() > 1) {
				// DateSet.
				DateSet dateSet = new DateSet();

				for (TextFieldsWithCheckBoxForDates tfwcbfDates: existenceDateTFs) {
					if (tfwcbfDates.isDateRange()) {
						// DateRange.
						DateRange dateRange = this.fillDateRangeValues(tfwcbfDates);

						if (dateRange != null) {
							dateSet.getDateOrDateRange().add(dateRange);
						}
					} else {
						// Date.
						Date date = this.fillDateValues(tfwcbfDates);

						if (date != null) {
							dateSet.getDateOrDateRange().add(date);
						}
					}
				}

				if (dateSet.getDateOrDateRange().isEmpty()) {
					this.errors.add(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES);
				} else if (dateSet.getDateOrDateRange().size() == 1) {
					Object object = dateSet.getDateOrDateRange().get(0);

					// Date.
					if (object instanceof Date) {
						Date date = (Date) object;
						if (StringUtils.isEmpty(trimStringValue(date.getContent()))) {
							this.errors.add(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES);
						} else {
							existDates.setDate(date);
							hasChanged = true;
						}
					}

					// DateRange.
					if (object instanceof DateRange) {
						DateRange dateRange = (DateRange) object;
						if (dateRange.getFromDate() == null
								|| StringUtils.isEmpty(trimStringValue(dateRange.getFromDate().getContent()))
								|| dateRange.getToDate() == null
								|| StringUtils.isEmpty(trimStringValue(dateRange.getToDate().getContent()))) {
							this.errors.add(EacCpfIdentityPanel.ERROR_EXISTENCE_DATES);
						} else {
							existDates.setDateRange(dateRange);
							hasChanged = true;
						}
					}
				} else {
					existDates.setDateSet(dateSet);
					hasChanged = true;
				}
			}

			// Add the new content.
			if (existDates.getDate() == null
					&& existDates.getDateRange() == null
					&& existDates.getDateSet() == null) {
				description.setExistDates(null);
			} else {
				description.setExistDates(existDates);
			}

			return hasChanged;
		}

		/**
		 * Method to recover the date values from the TextFieldsWithCheckBoxForDates
		 *
		 * @param tfwcbfDates
		 * @return the Date.
		 */
		private Date fillDateValues(TextFieldsWithCheckBoxForDates tfwcbfDates) {
			Date date = null;
			if (StringUtils.isNotEmpty(trimStringValue(tfwcbfDates.getDateValue()))) {
				date = new Date();
				date.setContent(trimStringValue(tfwcbfDates.getDateValue()));
				if (StringUtils.isNotEmpty(parseStandardDate(trimStringValue(tfwcbfDates.getStandardDateValue())))) {
					date.setStandardDate(parseStandardDate(trimStringValue(tfwcbfDates.getStandardDateValue())));
				}
			}

			return date;
		}

		/**
		 * Method to recover the dateRange values from the TextFieldsWithCheckBoxForDates
		 *
		 * @param tfwcbfDates
		 * @return the tfwcbfDateRanges.
		 */
		private DateRange fillDateRangeValues(TextFieldsWithCheckBoxForDates tfwcbfDateRanges) {
			DateRange dateRange = null;
			FromDate fromDate = null;
			ToDate toDate = null;

			// From date.
			if (StringUtils.isNotEmpty(trimStringValue(tfwcbfDateRanges.getDateFromValue()))) {
				fromDate = new FromDate();
				fromDate.setContent(trimStringValue(tfwcbfDateRanges.getDateFromValue()));

				if (StringUtils.isNotEmpty(parseStandardDate(trimStringValue(tfwcbfDateRanges.getStandardDateFromValue())))) {
					fromDate.setStandardDate(parseStandardDate(trimStringValue(tfwcbfDateRanges.getStandardDateFromValue())));
				}
			}

			// To date.
			if (StringUtils.isNotEmpty(trimStringValue(tfwcbfDateRanges.getDateToValue()))) {
				toDate = new ToDate();
				toDate.setContent(trimStringValue(tfwcbfDateRanges.getDateToValue()));

				if (StringUtils.isNotEmpty(parseStandardDate(trimStringValue(tfwcbfDateRanges.getStandardDateToValue())))) {
					toDate.setStandardDate(parseStandardDate(trimStringValue(tfwcbfDateRanges.getStandardDateToValue())));
				}
			}

			if (fromDate != null || toDate != null) {
				dateRange = new DateRange();
				dateRange.setFromDate(fromDate);
				dateRange.setToDate(toDate);
			}

			return dateRange;
		}
	}

	/**
	 * Class to performs the actions when the user clicks in other tab.
	 */
	public class ChangeTabListener extends UpdateEacCpfObject implements ChangeListener {
		private int currentTab;
		/**
		 * Constructor.
		 *
		 * @param eaccpf
		 * @param tabbedPane
		 * @param model
		 * @param indexTab
		 */
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
						case 1:
							reloadTabbedPanel(new EacCpfDescriptionPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels).buildEditorPanel(errors), 1);
							break;
						case 2:
							reloadTabbedPanel(new EacCpfRelationsPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels).buildEditorPanel(errors), 2);
							break;
						case 3:
							reloadTabbedPanel(new EacCpfControlPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, labels).buildEditorPanel(errors), 3);
							break;
						default:
							reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
					}
				} catch (EacCpfFormException ex) {
					reloadTabbedPanel(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels).buildEditorPanel(errors), 0);
				}
			}
		}
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			// Empty.
		}
	}

}
