package eu.apenet.dpt.standalone.gui.eacCpf.SwingStructures;

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

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithDate;

/**
 * Class to create a JTextFields with a JCheckBoxs for the dates for an
 * apeEAC-CPF file.
 */
public class TextFieldsWithCheckBoxForDates {
	private TextFieldWithDate textFieldWithDate;
	private JCheckBox dateUndefinedCB;
	private JCheckBox dateFromUndefinedCB;
	private JCheckBox dateToUndefinedCB;
	private JTextField standardDateTF;
	private JTextField standardDateFromTF;
	private JTextField standardDateToTF;
	private boolean isDateRange;

	/**
	 * Constructor.
	 *
	 * @param cbText
	 * @param date
	 * @param dateUndefined
	 * @param standardDate
	 * @param dateFrom
	 * @param dateFromUndefined
	 * @param standardDateFrom
	 * @param dateToUndefined
	 * @param dateTo
	 * @param standardDateTo
	 */
	public TextFieldsWithCheckBoxForDates(String cbText, String date, boolean dateUndefined, String standardDate, String dateFrom, boolean dateFromUndefined, String standardDateFrom, String dateTo, boolean dateToUndefined, String standardDateTo, boolean isDateRange) {
		this.textFieldWithDate = new TextFieldWithDate("", "", dateFrom, dateTo, date);

		this.dateUndefinedCB = new JCheckBox(cbText);
		this.standardDateTF = new JTextField(standardDate);
		if (dateUndefined) {
			this.textFieldWithDate.getDateField().setText(this.dateUndefinedCB.getText());
			this.textFieldWithDate.getDateField().setEditable(false);
			this.dateUndefinedCB.setSelected(dateUndefined);
			this.standardDateTF.setEditable(false);
		}

		this.dateFromUndefinedCB = new JCheckBox(cbText);
		this.standardDateFromTF = new JTextField(standardDateFrom);
		if (dateFromUndefined) {
			this.textFieldWithDate.getFromDateField().setText(this.dateFromUndefinedCB.getText());
			this.textFieldWithDate.getFromDateField().setEditable(false);
			this.dateFromUndefinedCB.setSelected(dateFromUndefined);
			this.standardDateFromTF.setEditable(false);
		}

		this.dateToUndefinedCB = new JCheckBox(cbText);
		this.standardDateToTF = new JTextField(standardDateTo);
		if (dateToUndefined) {
			this.textFieldWithDate.getToDateField().setText(this.dateToUndefinedCB.getText());
			this.textFieldWithDate.getToDateField().setEditable(false);
			this.dateToUndefinedCB.setSelected(dateToUndefined);
			this.standardDateToTF.setEditable(false);
		}

		this.isDateRange = isDateRange;
	}

	// Getters for the components.
	/**
	 * Method to recover the JTextField for the date.
	 *
	 * @return the dateTextField.
	 */
	public JTextField getDateTextField() {
		return this.textFieldWithDate.getDateField();
	}

	/**
	 * Method to recover the JCheckBox for the date.
	 *
	 * @return the dateUndefinedCB.
	 */
	public JCheckBox getDateUndefinedCB() {
		return this.dateUndefinedCB;
	}

	/**
	 * Method to recover the JTextField for the standardDate.
	 *
	 * @return the standardDateTextField.
	 */
	public JTextField getStandardDateTextField() {
		return this.standardDateTF;
	}

	/**
	 * Method to recover the JTextField for the dateFrom.
	 *
	 * @return the dateFromTextField.
	 */
	public JTextField getDateFromTextField() {
		return this.textFieldWithDate.getFromDateField();
	}

	/**
	 * Method to recover the JCheckBox for the dateFrom.
	 *
	 * @return the dateFromUndefinedCB.
	 */
	public JCheckBox getDateFromUndefinedCB() {
		return this.dateFromUndefinedCB;
	}

	/**
	 * Method to recover the JTextField for the standardDateFrom.
	 *
	 * @return the standardDateFromTextField.
	 */
	public JTextField getStandardDateFromTextField() {
		return this.standardDateFromTF;
	}

	/**
	 * Method to recover the JTextField for the dateTo.
	 *
	 * @return the dateToTextField.
	 */
	public JTextField getDateToTextField() {
		return this.textFieldWithDate.getToDateField();
	}

	/**
	 * Method to recover the JCheckBox for the dateTo.
	 *
	 * @return the dateToUndefinedCB.
	 */
	public JCheckBox getDateToUndefinedCB() {
		return this.dateToUndefinedCB;
	}

	/**
	 * Method to recover the JTextField for the standardDateTo.
	 *
	 * @return the standardDateToTextField.
	 */
	public JTextField getStandardDateToTextField() {
		return this.standardDateToTF;
	}

	// Getters for the components values.
	/**
	 * Method to recover the value for the date.
	 *
	 * @return the dateTextField value.
	 */
	public String getDateValue() {
		return this.textFieldWithDate.getDate();
	}

	/**
	 * Method to recover the state for the date check box.
	 *
	 * @return the dateUndefinedCB state.
	 */
	public boolean isSelectedDateUndefinedCB() {
		return this.dateUndefinedCB.isSelected();
	}

	/**
	 * Method to recover the value for the standardDate.
	 *
	 * @return the standardDateTextField value.
	 */
	public String getStandardDateValue() {
		return this.standardDateTF.getText();
	}

	/**
	 * Method to recover the value for the dateFrom.
	 *
	 * @return the dateFromTextField value.
	 */
	public String getDateFromValue() {
		return this.textFieldWithDate.getFromDate();
	}

	/**
	 * Method to recover the state for the dateFrom check box.
	 *
	 * @return the dateFromUndefinedCB state.
	 */
	public boolean isSelectedDateFromUndefinedCB() {
		return this.dateFromUndefinedCB.isSelected();
	}

	/**
	 * Method to recover the value for the standardDateFrom.
	 *
	 * @return the standardDateFromTextField value.
	 */
	public String getStandardDateFromValue() {
		return this.standardDateFromTF.getText();
	}

	/**
	 * Method to recover the value for the dateTo.
	 *
	 * @return the dateToTextField value.
	 */
	public String getDateToValue() {
		return this.textFieldWithDate.getToDate();
	}

	/**
	 * Method to recover the state for the dateTo check box.
	 *
	 * @return the dateToUndefinedCB state.
	 */
	public boolean isSelectedDateToUndefinedCB() {
		return this.dateToUndefinedCB.isSelected();
	}

	/**
	 * Method to recover the value for the standardDateTo
	 *
	 * @return the standardDateToTextField value.
	 */
	public String getStandardDateToValue() {
		return this.standardDateToTF.getText();
	}

	/**
	 * @return the isDateRange
	 */
	public boolean isDateRange() {
		return this.isDateRange;
	}

}
