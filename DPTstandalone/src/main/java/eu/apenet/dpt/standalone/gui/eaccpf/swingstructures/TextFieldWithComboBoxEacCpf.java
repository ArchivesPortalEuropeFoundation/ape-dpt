package eu.apenet.dpt.standalone.gui.eaccpf.swingstructures;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import eu.apenet.dpt.utils.util.XmlTypeEacCpf;

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

/**
 * Class to create a JTextField with a JComboBox with all the possibilities
 * for an apeEAC-CPF file.
 */
public class TextFieldWithComboBoxEacCpf {
	// Define elements.
	private JTextField textField;
	private JComboBox<String> comboBox;

	// All possible lists of values.
	private static final String[] CPF_RELATION = { "identity", "hierarchical", "hierarchical-parent", "hierarchical-child", "temporal", "temporal-earlier", "temporal-later", "family", "associative" };
	private static final String[] RESOURCE_RELATION = {"creatorOf","subjectOf","other" };
	private static final String[] FUNCTION_RELATION = {"controls","owns","performs" }; 
	private static final String[] NAME_COMPONENT_CORPORATE_BODY = { "corpname", "suffix", "alias", "legalform" };
	private static final String[] NAME_COMPONENT_FAMILY = { "famname", "surname", "prefix", "suffix", "alias" };
	private static final String[] NAME_COMPONENT_PERSON = { "persname", "surname", "firstname", "birthname", "title", "prefix", "suffix", "alias", "patronymic" };
	private static final String[] NAME_FORM = { "authorized", "alternative", "preferred" , "abbreviation" , "other" };
	private static final String[] PLACE_ADDRESS = { "street", "postalcode", "localentity", "firstdem", "secondem", "country", "other" };
	private static final String[] PLACE_ROLE_CORPORATE_BODY = { "foundation", "business-residence", "suppression", "other" };
	private static final String[] PLACE_ROLE_FAMILY = { "private-residence", "business-residence", "other" };
	private static final String[] PLACE_ROLE_PERSON = { "birth", "private-residence", "business-residence", "death", "other" };

	// Constants.
	public static final String DEFAULT_VALUE = "---";
	public static final String SELECTED_ALTERNATIVE_SET_VALUE = "identity";
	// Constants for types.
	public static final String TYPE_ADDRESS_COMPONENT = "address";
	public static final String TYPE_COMPONENT = "coponent";
	public static final String TYPE_CPF_RELATION = "cpf";
	public static final String TYPE_FORM = "form";
	public static final String TYPE_PLACE_ROLE = "place";
	public static final String TYPE_RESOURCE_RELATION = "resource_relation";
	public static final String TYPE_FUNCTION_RELATION = "function_relation";

	/**
	 * Constructor.
	 *
	 * @param textFieldValue
	 * @param comboBoxValue
	 * @param type
	 * @param entityType
	 * @param labels 
	 */
	public TextFieldWithComboBoxEacCpf(String textFieldValue, String comboBoxValue, String type, XmlTypeEacCpf entityType, ResourceBundle labels) { 
		// Fill the value of JComboBox and set the selected index.
		if (TextFieldWithComboBoxEacCpf.TYPE_COMPONENT.equalsIgnoreCase(type)) {
			// Fill the value of JTextField.
			this.textField = new JTextField(textFieldValue);
			if (XmlTypeEacCpf.EAC_CPF_CORPORATEBODY.getName().equalsIgnoreCase(entityType.getName())){
				this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.NAME_COMPONENT_CORPORATE_BODY, labels));
				this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.NAME_COMPONENT_CORPORATE_BODY));
			} else if (XmlTypeEacCpf.EAC_CPF_FAMILY.getName().equalsIgnoreCase(entityType.getName())) {
				this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.NAME_COMPONENT_FAMILY, labels));
				this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.NAME_COMPONENT_FAMILY));
			} else if (XmlTypeEacCpf.EAC_CPF_PERSON.getName().equalsIgnoreCase(entityType.getName())) {
				this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.NAME_COMPONENT_PERSON, labels));
				this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.NAME_COMPONENT_PERSON));
			}
		} else if (TextFieldWithComboBoxEacCpf.TYPE_FORM.equalsIgnoreCase(type)) {
			this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.NAME_FORM, labels));
			this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.NAME_FORM));
		} else if (TextFieldWithComboBoxEacCpf.TYPE_ADDRESS_COMPONENT.equalsIgnoreCase(type)) {
			// Fill the value of JTextField.
			this.textField = new JTextField(textFieldValue);
			this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.PLACE_ADDRESS, labels));
			this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.PLACE_ADDRESS));
		} else if (TextFieldWithComboBoxEacCpf.TYPE_PLACE_ROLE.equalsIgnoreCase(type)) {
			// Fill the value of JTextField.
//			this.textField = new JTextField(textFieldValue);
			if (XmlTypeEacCpf.EAC_CPF_CORPORATEBODY.getName().equalsIgnoreCase(entityType.getName())){
				this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.PLACE_ROLE_CORPORATE_BODY, labels));
				this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.PLACE_ROLE_CORPORATE_BODY));
			} else if (XmlTypeEacCpf.EAC_CPF_FAMILY.getName().equalsIgnoreCase(entityType.getName())) {
				this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.PLACE_ROLE_FAMILY, labels));
				this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.PLACE_ROLE_FAMILY));
			} else if (XmlTypeEacCpf.EAC_CPF_PERSON.getName().equalsIgnoreCase(entityType.getName())) {
				this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.PLACE_ROLE_PERSON, labels));
				this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.PLACE_ROLE_PERSON));
			}
		} else if (TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION.equalsIgnoreCase(type)) {
			// Fill the value of JTextField.
			this.textField = new JTextField(textFieldValue);
			this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.CPF_RELATION, labels));
			this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.CPF_RELATION));
		} else if (TextFieldWithComboBoxEacCpf.TYPE_RESOURCE_RELATION.equalsIgnoreCase(type)) {
			// Fill the value of JTextField.
			this.textField = new JTextField(textFieldValue);
			this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.RESOURCE_RELATION, labels));
			this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.RESOURCE_RELATION));
		} else if (TextFieldWithComboBoxEacCpf.TYPE_FUNCTION_RELATION.equalsIgnoreCase(type)) {
			// Fill the value of JTextField.
			this.textField = new JTextField(textFieldValue);
			this.comboBox = new JComboBox<String>(fillTranslationComboBoxValues(type, TextFieldWithComboBoxEacCpf.FUNCTION_RELATION, labels));
			this.comboBox.setSelectedIndex(this.getIndex(comboBoxValue, TextFieldWithComboBoxEacCpf.FUNCTION_RELATION));
		}
	}

	/**
	 * Method to fill the combo box with translated labels.
	 *
	 * @param arrayValues
	 * @param labels
	 * @return the array of translated labels.
	 */
	private String[] fillTranslationComboBoxValues(String type, String[] arrayValues, ResourceBundle labels) {
		List<String> partComponents = new LinkedList<String>();
		partComponents.add(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE);

		if (TextFieldWithComboBoxEacCpf.TYPE_COMPONENT.equalsIgnoreCase(type)) {
			for (int i =0; i < arrayValues.length; i++) {
				partComponents.add(labels.getString("eaccpf.identity.name.component." + arrayValues[i]));
			}
		} else if (TextFieldWithComboBoxEacCpf.TYPE_FORM.equalsIgnoreCase(type)) {
			for (int i =0; i < arrayValues.length; i++) {
				partComponents.add(labels.getString("eaccpf.identity.name.form." + arrayValues[i]));
			}
		} else if (TextFieldWithComboBoxEacCpf.TYPE_ADDRESS_COMPONENT.equalsIgnoreCase(type)) {
			for (int i =0; i < arrayValues.length; i++) {
				partComponents.add(labels.getString("eaccpf.description.combo.address.component." + arrayValues[i]));
			}
		} else if (TextFieldWithComboBoxEacCpf.TYPE_PLACE_ROLE.equalsIgnoreCase(type)) {
			for (int i =0; i < arrayValues.length; i++) {
				partComponents.add(labels.getString("eaccpf.description.combo.place.role." + arrayValues[i]));
			}
		} else if (TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION.equalsIgnoreCase(type)) {
			for (int i =0; i < arrayValues.length; i++) {
				partComponents.add(labels.getString("eaccpf.relations.cpf.relation.type." + arrayValues[i]));
			}
		} else if (TextFieldWithComboBoxEacCpf.TYPE_RESOURCE_RELATION.equalsIgnoreCase(type)) {
			for (int i =0; i < arrayValues.length; i++) {
				partComponents.add(labels.getString("eaccpf.relations.resource.relation.type." + arrayValues[i]));
			}
		} else if (TextFieldWithComboBoxEacCpf.TYPE_FUNCTION_RELATION.equalsIgnoreCase(type)) {
			for (int i =0; i < arrayValues.length; i++) {
				partComponents.add(labels.getString("eaccpf.relations.function.relation.type." + arrayValues[i]));
			}
		}

		return partComponents.toArray(new String[partComponents.size()]);
	}

	/**
	 * Method to recover the index to select.
	 *
	 * @param comboBoxValue
	 * @param arrayValues
	 * @return the index to select.
	 */
	private int getIndex(String comboBoxValue, String[] arrayValues) {
		int result = 0;
		boolean found = false;
		if (comboBoxValue != null && !comboBoxValue.isEmpty()) {	
			for (int i =0; !found && i < arrayValues.length; i++) {
				if (comboBoxValue.equalsIgnoreCase(arrayValues[i])) {
					result = i+1;
					found = true;
				}
			}
		}
		return result;
	}

	/**
	 * @return the textField.
	 */
	public JTextField getTextField() {
		return this.textField;
	}

	/**
	 * @return the textField value.
	 */
	public String getTextFieldValue() {
		return this.textField.getText();
	}

	/**
	 * @return the comboBox.
	 */
	public JComboBox<String> getComboBox() {
		return this.comboBox;
	}

	/**
	 * @param entityType 
	 * @param type 
	 * @return the comboBox selected value.
	 */
	public String getComboBoxValue(String type, XmlTypeEacCpf entityType) {
		String value = "";

		// Get the selected element index.
		int index = this.comboBox.getSelectedIndex();

		// Recover the value.
		if (index > 0) {
			if (TextFieldWithComboBoxEacCpf.TYPE_COMPONENT.equalsIgnoreCase(type)) {
				if (XmlTypeEacCpf.EAC_CPF_CORPORATEBODY.getName().equalsIgnoreCase(entityType.getName())){
					value = TextFieldWithComboBoxEacCpf.NAME_COMPONENT_CORPORATE_BODY[index - 1];
				} else if (XmlTypeEacCpf.EAC_CPF_FAMILY.getName().equalsIgnoreCase(entityType.getName())) {
					value = TextFieldWithComboBoxEacCpf.NAME_COMPONENT_FAMILY[index - 1];
				} else if (XmlTypeEacCpf.EAC_CPF_PERSON.getName().equalsIgnoreCase(entityType.getName())) {
					value = TextFieldWithComboBoxEacCpf.NAME_COMPONENT_PERSON[index - 1];
				}
			} else if (TextFieldWithComboBoxEacCpf.TYPE_FORM.equalsIgnoreCase(type)) {
				value = TextFieldWithComboBoxEacCpf.NAME_FORM[index - 1];
			} else if (TextFieldWithComboBoxEacCpf.TYPE_ADDRESS_COMPONENT.equalsIgnoreCase(type)) {
				value = TextFieldWithComboBoxEacCpf.PLACE_ADDRESS[index - 1];
			} else if (TextFieldWithComboBoxEacCpf.TYPE_PLACE_ROLE.equalsIgnoreCase(type)) {
				if (XmlTypeEacCpf.EAC_CPF_CORPORATEBODY.getName().equalsIgnoreCase(entityType.getName())){
					value = TextFieldWithComboBoxEacCpf.PLACE_ROLE_CORPORATE_BODY[index - 1];
				} else if (XmlTypeEacCpf.EAC_CPF_FAMILY.getName().equalsIgnoreCase(entityType.getName())) {
					value = TextFieldWithComboBoxEacCpf.PLACE_ROLE_FAMILY[index - 1];
				} else if (XmlTypeEacCpf.EAC_CPF_PERSON.getName().equalsIgnoreCase(entityType.getName())) {
					value = TextFieldWithComboBoxEacCpf.PLACE_ROLE_PERSON[index - 1];
				}
			} else if (TextFieldWithComboBoxEacCpf.TYPE_CPF_RELATION.equalsIgnoreCase(type)) {
				value = TextFieldWithComboBoxEacCpf.CPF_RELATION[index - 1];
			} else if (TextFieldWithComboBoxEacCpf.TYPE_RESOURCE_RELATION.equalsIgnoreCase(type)) {
				value = TextFieldWithComboBoxEacCpf.RESOURCE_RELATION[index - 1];
			} else if (TextFieldWithComboBoxEacCpf.TYPE_FUNCTION_RELATION.equalsIgnoreCase(type)) {
				value = TextFieldWithComboBoxEacCpf.FUNCTION_RELATION[index - 1];
			}
		}

		return value;
	}

}
