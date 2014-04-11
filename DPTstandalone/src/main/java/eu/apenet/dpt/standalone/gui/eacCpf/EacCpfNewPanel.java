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


import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.utils.eaccpf.EacCpf;

/**
 * Class for construct all the panels of the apeEAC-CPF creation form.
 */
public class EacCpfNewPanel extends EacCpfPanel {

	/**
	 * Constructor.
	 *
	 * @param eaccpf
	 * @param tabbedPanel
	 * @param mainTabbedPane
	 * @param eacCpfFrame
	 * @param model
	 * @param labels
	 */
	public EacCpfNewPanel(EacCpf eaccpf, JTabbedPane tabbedPanel, JTabbedPane mainTabbedPane, JFrame eacCpfFrame, ProfileListModel model, ResourceBundle labels) {
		super(eaccpf, tabbedPanel, mainTabbedPane, eacCpfFrame, model, labels);
		tabbedPane = new JTabbedPane();
		tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
		super.setTabbedPane(tabbedPane);
	}

	@Override
	protected JComponent buildEditorPanel(List<String> errors) {
		return null;
	}

	/**
	 * Method to construct the form with all the tabs.
	 *
	 * @param isNew
	 * @param eacType
	 * @param firstLanguage
	 * @param firstScript
	 * @return JConmponent with the form.
	 */
	protected JComponent buildInstitutionTabbedPane(boolean isNew, String eacType, String firstLanguage, String firstScript) {
		EacCpfFrame.firstTimeInTab = true;

		JScrollPane scrollPane = new JScrollPane(new EacCpfIdentityPanel(eaccpf, tabbedPane, mainTabbedPane, eacCpfFrame, model, isNew, labels, eacType).buildEditorPanel(null));
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		tabbedPane.add(labels.getString("eaccpf.tab.identity"), scrollPane);
		tabbedPane.add(labels.getString("eaccpf.tab.description"), null);
		tabbedPane.add(labels.getString("eaccpf.tab.relations"), null);
		tabbedPane.add(labels.getString("eaccpf.tab.control"), null);
		// TODO: Delete?
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		tabbedPane.setEnabledAt(3, false);

		return tabbedPane;
	}

}
