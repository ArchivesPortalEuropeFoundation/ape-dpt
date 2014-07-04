package eu.apenet.dpt.standalone.gui.eaccpf.swingstructures;

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

import javax.swing.JRadioButton;
import javax.swing.JTextField;

import eu.apenet.dpt.standalone.gui.commons.swingstructures.TextFieldWithDate;

/**
 * Class to create a JTextFields with a JCheckBoxs for the dates for an
 * apeEAC-CPF file.
 */
public class TextFieldsWithRadioButtonForDates {
	private TextFieldWithDate textFieldWithDate;
	private JRadioButton dateUndefinedRB;
	private JRadioButton dateDefinedRB;
	private JRadioButton dateStillRB;
	
	private JRadioButton dateFromUndefinedRB;
	private JRadioButton dateFromDefinedRB;
	private JRadioButton dateFromStillRB;
	
	private JRadioButton dateToUndefinedRB;
	private JRadioButton dateToDefinedRB;
	private JRadioButton dateToStillRB;
	
	private JTextField standardDateTF;
	private JTextField standardDateFromTF;
	private JTextField standardDateToTF;
	private boolean isDateRange;

	/**
	 * Constructor.
	 *
	 * @param rbUndefinedText
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
	public TextFieldsWithRadioButtonForDates(
			String rbUndefinedText, String rbDefinedText, String rbStillText, String date, boolean dateUndefined, boolean stillIfKnown, 
			String standardDate, String dateFrom, boolean dateFromUndefined, String standardDateFrom, 
			String dateTo, boolean dateToUndefined, String standardDateTo, boolean isDateRange) {
		this.textFieldWithDate = new TextFieldWithDate("", "", dateFrom, dateTo, date);

		this.dateUndefinedRB = new JRadioButton(rbUndefinedText);
		this.dateDefinedRB = new JRadioButton(rbDefinedText);
		this.dateStillRB = new JRadioButton(rbStillText);
		this.standardDateTF = new JTextField(standardDate);
		if (dateUndefined) {
			this.textFieldWithDate.getDateField().setText(this.dateUndefinedRB.getText());
			this.textFieldWithDate.getDateField().setEditable(false);
			this.dateUndefinedRB.setSelected(true);
			this.standardDateTF.setEditable(false);
		}else{
			if(stillIfKnown){
				this.dateStillRB.setSelected(true);
			}else{
				this.dateDefinedRB.setSelected(true);
			}
		}

		this.dateFromUndefinedRB = new JRadioButton(rbUndefinedText);
		this.dateFromDefinedRB = new JRadioButton(rbDefinedText);
		this.dateFromStillRB = new JRadioButton(rbStillText);
		this.standardDateFromTF = new JTextField(standardDateFrom);
		if (dateFromUndefined) {
			this.textFieldWithDate.getFromDateField().setText(this.dateFromUndefinedRB.getText());
			this.textFieldWithDate.getFromDateField().setEditable(false);
			this.dateFromUndefinedRB.setSelected(true);
			this.standardDateFromTF.setEditable(false);
		}else{
			if(stillIfKnown){
				this.dateFromStillRB.setSelected(true);
			}else{
				this.dateFromDefinedRB.setSelected(true);
			}
		}

		this.dateToUndefinedRB = new JRadioButton(rbUndefinedText);
		this.dateToDefinedRB = new JRadioButton(rbDefinedText);
		this.dateToStillRB = new JRadioButton(rbStillText);
		this.standardDateToTF = new JTextField(standardDateTo);
		if (dateToUndefined) {
			this.textFieldWithDate.getToDateField().setText(this.dateToUndefinedRB.getText());
			this.textFieldWithDate.getToDateField().setEditable(false);
			this.dateToUndefinedRB.setSelected(true);
			this.standardDateToTF.setEditable(false);
		}else{
			if(stillIfKnown){
				this.dateToStillRB.setSelected(true);
			}else{
				this.dateToDefinedRB.setSelected(true);
			}
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
	 * Method to recover the JRadioButton for the date.
	 *
	 * @return the dateUndefinedCB.
	 */
	public JRadioButton getDateUndefinedRB() {
		return this.dateUndefinedRB;
	}
	
	public JRadioButton getDateDefinedRB() {
		return this.dateDefinedRB;
	}
	
	public JRadioButton getDateStillRB() {
		return this.dateStillRB;
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
	 * Method to recover the JRadioButton for the dateFrom.
	 *
	 * @return the dateFromUndefinedCB.
	 */
	public JRadioButton getDateFromUndefinedRB() {
		return this.dateFromUndefinedRB;
	}
	public JRadioButton getDateFromDefinedRB() {
		return this.dateFromDefinedRB;
	}
	public JRadioButton getDateFromStillRB() {
		return this.dateFromStillRB;
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
	 * Method to recover the JRadioButton for the dateTo.
	 *
	 * @return the dateToUndefinedCB.
	 */
	public JRadioButton getDateToUndefinedRB() {
		return this.dateToUndefinedRB;
	}
	
	public JRadioButton getDateToDefinedRB() {
		return this.dateToDefinedRB;
	}
	
	public JRadioButton getDateToStillRB() {
		return this.dateToStillRB;
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
	public boolean isSelectedDateUndefinedRB() {
		return this.dateUndefinedRB.isSelected();
	}
	
	public boolean isSelectedDateDefinedRB() {
		return this.dateDefinedRB.isSelected();
	}
	
	public boolean isSelectedDateFromDefinedRB(){
		return this.dateFromDefinedRB.isSelected();
	}
	
	public boolean isSelectedDateToDefinedRB(){
		return this.dateToDefinedRB.isSelected();
	}
	
	public boolean isSelectedDateStillRB() {
		return this.dateStillRB.isSelected();
	}
	
	public boolean isSelectedDateFromStillRB(){
		return this.dateFromStillRB.isSelected();
	}
	
	public boolean isSelectedDateToStillRB(){
		return this.dateToStillRB.isSelected();
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
	public boolean isSelectedDateFromUndefinedRB() {
		return this.dateFromUndefinedRB.isSelected();
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
	public boolean isSelectedDateToUndefinedRB() {
		return this.dateToUndefinedRB.isSelected();
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
