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
import eu.apenet.dpt.standalone.gui.commons.TextChanger;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithDate;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.FormerlyUsedName;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.TextFieldWithLanguage;
import eu.apenet.dpt.utils.eag2012.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * User: Yoann Moranville
 * Date: 03/10/2012
 *
 * @author Yoann Moranville
 */
public class EagIdentityPanel extends EagPanels {

    private List<TextFieldWithLanguage> nameInstitutionTfs;
    private List<TextFieldWithLanguage> parallelNameTfs;
    private List<FormerlyUsedName> formerlyUsedNameTfs;
    private List<TextFieldWithDate> datesForFormerlyUsedName;

    protected static String[] typeInstitution = {"---", "National archives", "Regional archives", "County/local authority archives",
            "Municipal archives", "Specialised governmental archives", "Private persons and family archives", "Church and religious archives",
            "Business archives", "University and research archives", "Media archives", "Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations",
            "Specialised non-governmental archives and archives of other cultural (heritage) institutions"};
    
    private List<JComboBox> typeInstitutionComboList = new LinkedList<JComboBox>();

    public EagIdentityPanel(Eag eag, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels, int repositoryNb) {
        super(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
    }

    /**
     * Builds and answer the editor's tab for the given layout.
     * @return the editor's panel
     */
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
        builder.addLabel(labels.getString("eag2012.commons.countryCode") + "*",    cc.xy (1, rowNb));
        builder.addLabel(eag.getArchguide().getIdentity().getRepositorid().getCountrycode(), cc.xy(3, rowNb));

        builder.addLabel(labels.getString("eag2012.commons.idUsedInApe"),      cc.xy (5, rowNb));
        builder.addLabel(eag.getControl().getRecordId().getValue(), cc.xy(7, rowNb));
        setNextRow();

        for(OtherRecordId otherRecordId : eag.getControl().getOtherRecordId()) {
            builder.addLabel(labels.getString("eag2012.control.identifierInstitution"),    cc.xy (1, rowNb));
            builder.addLabel(otherRecordId.getValue(), cc.xy(3, rowNb));
            setNextRow();
        }

        nameInstitutionTfs = new ArrayList<TextFieldWithLanguage>(eag.getArchguide().getIdentity().getAutform().size());
        int loop = 0;
        for(Autform autform : eag.getArchguide().getIdentity().getAutform()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(autform.getContent(), autform.getLang());
            nameInstitutionTfs.add(textFieldWithLanguage);
            if(loop++ == 0) {
                builder.addLabel(labels.getString("eag2012.commons.nameOfInstitution") + "*",    cc.xy (1, rowNb));
                textFieldWithLanguage.getTextField().setEnabled(false);
            } else {
                builder.addLabel(labels.getString("eag2012.commons.nameOfInstitution"),    cc.xy (1, rowNb));
            }

            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addNewNameInstitutionBtn = new ButtonTab(labels.getString("eag2012.identity.addAnotherForm"));
        addNewNameInstitutionBtn.addActionListener(new AddNameInstitutionAction(eag, tabbedPane, model));
        builder.add(addNewNameInstitutionBtn, cc.xy(1, rowNb));
        setNextRow();

        parallelNameTfs = new ArrayList<TextFieldWithLanguage>(eag.getArchguide().getIdentity().getParform().size());
        loop = 0;
        for(Parform parform : eag.getArchguide().getIdentity().getParform()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(parform.getContent(), parform.getLang());
            parallelNameTfs.add(textFieldWithLanguage);
            if(loop++ == 0 && StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue()))
                textFieldWithLanguage.getTextField().setEnabled(false);
            builder.addLabel(labels.getString("eag2012.commons.parallelNameOfInstitution"),    cc.xy (1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addNewParallelNameInstitutionBtn = new ButtonTab(labels.getString("eag2012.identity.addAnotherParallelName"));
        addNewParallelNameInstitutionBtn.addActionListener(new AddParallelNameInstitutionAction(eag, tabbedPane, model));
        builder.add(addNewParallelNameInstitutionBtn, cc.xy(1, rowNb));
        setNextRow();

        formerlyUsedNameTfs = new ArrayList<FormerlyUsedName>(eag.getArchguide().getIdentity().getNonpreform().size());
        for(int formerNameCounter = 0; formerNameCounter < eag.getArchguide().getIdentity().getNonpreform().size(); formerNameCounter++) {
            Nonpreform nonpreform = eag.getArchguide().getIdentity().getNonpreform().get(formerNameCounter);
            String nameStr = "";
            for(int i = 0; i < nonpreform.getContent().size(); i++) {
                Object object = nonpreform.getContent().get(i);
                if(object instanceof String) {
                    nameStr += (String)object;
                }
                if (object instanceof UseDates) {
                    UseDates useDates = (UseDates)object;
                    if(useDates.getDateSet() != null) {
                        datesForFormerlyUsedName = new ArrayList<TextFieldWithDate>(useDates.getDateSet().getDateOrDateRange().size());
                        for (Object object1 : useDates.getDateSet().getDateOrDateRange()) {
                            if(object1 instanceof eu.apenet.dpt.utils.eag2012.Date){
                                TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", "", "", ((eu.apenet.dpt.utils.eag2012.Date)object1).getContent());
                                datesForFormerlyUsedName.add(textFieldWithDate);
                            }
                            if(object1 instanceof DateRange){
                                TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", ((DateRange)object1).getFromDate().getContent(), ((DateRange)object1).getToDate().getContent(), "");
                                textFieldWithDate.setDateRange(true);
                                datesForFormerlyUsedName.add(textFieldWithDate);
                            }
                        }
                    } else {
                        datesForFormerlyUsedName = new ArrayList<TextFieldWithDate>();
                        if(useDates.getDate() != null) {
                            TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", "", "", useDates.getDate().getContent());
                            datesForFormerlyUsedName.add(textFieldWithDate);
                        }
                        if(useDates.getDateRange() != null) {
                            TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", useDates.getDateRange().getFromDate().getContent(), useDates.getDateRange().getToDate().getContent(), "");
                            textFieldWithDate.setDateRange(true);
                            datesForFormerlyUsedName.add(textFieldWithDate);
                        }
                    }
                }
            }
            if(datesForFormerlyUsedName.isEmpty())
                datesForFormerlyUsedName.add(new TextFieldWithDate("", "", "", "", ""));

            FormerlyUsedName formerlyUsedName = new FormerlyUsedName(nameStr, nonpreform.getLang(), datesForFormerlyUsedName);
            formerlyUsedName.setOrderInXmlFile(formerNameCounter);
            formerlyUsedNameTfs.add(formerlyUsedName);
            builder.addLabel(labels.getString("eag2012.identity.previousNameOfArchive"),    cc.xy (1, rowNb));
            builder.add(formerlyUsedName.getNameTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(formerlyUsedName.getLanguageBox(), cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.identity.yearsOfUsedName"),    cc.xy (1, rowNb));
            setNextRow();

            for (TextFieldWithDate textFieldWithDate : datesForFormerlyUsedName) {
                if(!textFieldWithDate.isDateRange()) {
                    builder.addLabel(labels.getString("eag2012.commons.year"),    cc.xy (1, rowNb));
                    builder.add(textFieldWithDate.getDateField(), cc.xy (3, rowNb));
                    setNextRow();
                } else {
                    builder.addLabel(labels.getString("eag2012.commons.year") + " " + labels.getString("eag2012.commons.from"),    cc.xy (1, rowNb));
                    builder.add(textFieldWithDate.getFromDateField(), cc.xy (3, rowNb));
                    builder.addLabel(labels.getString("eag2012.commons.to"),             cc.xy (5, rowNb));
                    builder.add(textFieldWithDate.getToDateField(),                                            cc.xy (7, rowNb));
                    setNextRow();
                }
            }
            if(!formerlyUsedNameTfs.isEmpty()){
                JButton addSingleYearBtn = new ButtonTab(labels.getString("eag2012.commons.addYearButton"));
                addSingleYearBtn.setName("formerName_addSingleBtn_"+formerNameCounter);
                addSingleYearBtn.addActionListener(new AddSingleYearAction(eag, tabbedPane, model));
                builder.add(addSingleYearBtn, cc.xy(1, rowNb));
                JButton addYearRangeBtn = new ButtonTab(labels.getString("eag2012.commons.addYearRangeButton"));
                addYearRangeBtn.setName("formerName_addYearRangeBtn_"+formerNameCounter);
                addYearRangeBtn.addActionListener(new AddYearRangeAction(eag, tabbedPane, model));
                builder.add(addYearRangeBtn, cc.xy(3, rowNb));
                setNextRow();
            }
        }
        JButton addNewNonpreNameInstitutionBtn = new ButtonTab(labels.getString("eag2012.identity.addAnotherFormerlyUsedName"));
        addNewNonpreNameInstitutionBtn.addActionListener(new AddNonpreNameInstitutionAction(eag, tabbedPane, model));
        builder.add(addNewNonpreNameInstitutionBtn, cc.xy(1, rowNb));
        setNextRow();
      
        //print repositoryType combo
        if (eag.getArchguide().getIdentity().getRepositoryType()!=null && !eag.getArchguide().getIdentity().getRepositoryType().isEmpty()){
            for(RepositoryType repoType : eag.getArchguide().getIdentity().getRepositoryType()) {
                builder.addLabel(labels.getString("eag2012.identity.selectType"),    cc.xy (1, rowNb));
            	JComboBox comboBox = new JComboBox(typeInstitution);
                if (repoType.getValue() != null && !repoType.getValue().isEmpty()) {
                	comboBox.setSelectedItem(repoType.getValue());
                } else {
                	comboBox.setSelectedItem("---");
                }
            	typeInstitutionComboList.add(comboBox);
                builder.add(comboBox, cc.xy (3, rowNb));
                setNextRow();
            }
        }else{
            builder.addLabel(labels.getString("eag2012.identity.selectType"),    cc.xy (1, rowNb));
            JComboBox comboBox = new JComboBox(typeInstitution);
        	comboBox.setSelectedItem("---");
        	typeInstitutionComboList.add(comboBox);
            builder.add(comboBox, cc.xy (3, rowNb));
            setNextRow();
        }

        //add another repositoryType button
        JButton addNewTypeOfTheInstitution = new ButtonTab(labels.getString("eag2012.identity.addAnotherRepositoryType"));
        addNewTypeOfTheInstitution.addActionListener(new AddRepositoryTypeAction(eag, tabbedPane, model));
        builder.add(addNewTypeOfTheInstitution,cc.xy(1, rowNb));
        setNextRow();

        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();

        JButton exitBtn = new ButtonTab(labels.getString("eag2012.commons.exit"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton previousTabBtn = new ButtonTab(labels.getString("eag2012.commons.previousTab"));
        builder.add(previousTabBtn, cc.xy (3, rowNb));
        previousTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, false));

        JButton nextTabBtn = new ButtonTab(labels.getString("eag2012.commons.nextTab"));
        builder.add(nextTabBtn, cc.xy (5, rowNb));
        nextTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, true));

        setNextRow();
        JButton saveBtn = new ButtonTab(labels.getString("eag2012.commons.save"));
        builder.add(saveBtn, cc.xy (5, rowNb));
        saveBtn.addActionListener(new SaveBtnAction(eag, tabbedPane, model));

        setNextRow();
        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();
        JButton nextInstitutionTabBtn = new ButtonTab(labels.getString("eag2012.controls.nextInstitution"));
        nextInstitutionTabBtn.addActionListener(new NextInstitutionTabBtnAction(eag, tabbedPane, model));
        builder.add(nextInstitutionTabBtn, cc.xy(5, rowNb));

//        if(tabbedPane.getChangeListeners().length < 2) {
//            LOG.info("Add listener");
//            tabbedPane.addChangeListener(new TabChangeListener(eag, tabbedPane, model));
//        }

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
                    reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
                    mainTabbedPane.setEnabledAt(currentTabIndex, false);
                    mainTabbedPane.setEnabledAt(currentTabIndex+1, true);
                    mainTabbedPane.setSelectedIndex(currentTabIndex+1);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
            }
        }
    }

    public class AddSingleYearAction extends EagIdentityPanel.UpdateEagObject {

        AddSingleYearAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            String sourceString = ((JButton)actionEvent.getSource()).getName();
            int sourceOrderNo = Integer.parseInt(sourceString.substring(sourceString.lastIndexOf('_') + 1));
            List<TextFieldWithDate> yearsTfs = formerlyUsedNameTfs.get(sourceOrderNo).getDateList();

            UseDates currentUseDate = new UseDates();
            for(Object obj : eag.getArchguide().getIdentity().getNonpreform().get(sourceOrderNo).getContent()) {
                if(obj instanceof UseDates) {
                    currentUseDate = (UseDates)obj;
                }
            }
            if(TextChanger.isDateSetReady(yearsTfs, true, false)) {
                if (currentUseDate.getDateSet() == null) {
                    currentUseDate.setDateSet(new DateSet());
                }
                if(currentUseDate.getDate() != null) {
                    currentUseDate.getDateSet().getDateOrDateRange().add(currentUseDate.getDate());
                }
                if(currentUseDate.getDateRange() != null) {
                    currentUseDate.getDateSet().getDateOrDateRange().add(currentUseDate.getDateRange());
                }
                currentUseDate.getDateSet().getDateOrDateRange().add(new eu.apenet.dpt.utils.eag2012.Date());
            } else {
                currentUseDate.setDate(new eu.apenet.dpt.utils.eag2012.Date());
            }
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
        }
    }

    public class AddYearRangeAction extends EagIdentityPanel.UpdateEagObject {

        AddYearRangeAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(true);
            } catch (Eag2012FormException e) {
            }
            String sourceString = ((JButton)actionEvent.getSource()).getName();
            int sourceOrderNo = Integer.parseInt(sourceString.substring(sourceString.lastIndexOf('_') + 1));
            List<TextFieldWithDate> yearsTfs = formerlyUsedNameTfs.get(sourceOrderNo).getDateList();

            UseDates currentUseDate = new UseDates();
            for(Object obj : eag.getArchguide().getIdentity().getNonpreform().get(sourceOrderNo).getContent()) {
                if(obj instanceof UseDates) {
                    currentUseDate = (UseDates)obj;
                }
            }

            DateRange dateRange = new DateRange();
            dateRange.setFromDate(new FromDate());
            dateRange.setToDate(new ToDate());
            if(TextChanger.isDateSetReady(yearsTfs, false, true)) {
                if (currentUseDate.getDateSet() == null) {
                    currentUseDate.setDateSet(new DateSet());
                }
                if(currentUseDate.getDate() != null) {
                    currentUseDate.getDateSet().getDateOrDateRange().add(currentUseDate.getDate());
                }
                if(currentUseDate.getDateRange() != null) {
                    currentUseDate.getDateSet().getDateOrDateRange().add(currentUseDate.getDateRange());
                }
                currentUseDate.getDateSet().getDateOrDateRange().add(dateRange);
            } else {
                currentUseDate.setDateRange(dateRange);
            }
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
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
                reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 0);
            }
        }
    }

    public class AddNameInstitutionAction extends UpdateEagObject {
        public AddNameInstitutionAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {

            }
            eag.getArchguide().getIdentity().getAutform().add(new Autform());
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
        }
    }

    public class AddParallelNameInstitutionAction extends UpdateEagObject {
        public AddParallelNameInstitutionAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {

            }
            eag.getArchguide().getIdentity().getParform().add(new Parform());
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
        }
    }
    
    /***
     * This class adds another repositoryType only if there is a selected type, if no, it shows an error message
     * @author fernando
     *
     */
    public class AddRepositoryTypeAction extends UpdateEagObject {
        public AddRepositoryTypeAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {

            }

            boolean empty = false;
            for (int i = 0; !empty  && i < typeInstitutionComboList.size(); i++) {
            	if (typeInstitutionComboList.get(i).getSelectedItem().toString().equals("---")) {
            		empty = true;
            	}
            }

            if(!errors.contains("typeInstitutionComboList")){
	            if (!empty) {
	            	eag.getArchguide().getIdentity().getRepositoryType().add(new RepositoryType());
	            } else {
	            	JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.typeOfInstitution"));
	            	eag.getArchguide().getIdentity().getRepositoryType().add(new RepositoryType());
	            }
            }

            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
        }
    }
    
    public class AddNonpreNameInstitutionAction extends UpdateEagObject {
        public AddNonpreNameInstitutionAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {

            }
            Nonpreform nonpreform = new Nonpreform();
            nonpreform.getContent().add(new UseDates());
            eag.getArchguide().getIdentity().getNonpreform().add(nonpreform);
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
        }
    }

    public class ChangeTabBtnAction extends UpdateEagObject {
        private boolean isNextTab;
        ChangeTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model, boolean isNextTab) {
            super(eag, tabbedPane, model);
            this.isNextTab = isNextTab;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);

                if(isNextTab) {
                    reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, false, labels, repositoryNb).buildEditorPanel(errors), 2);
                    tabbedPane.setEnabledAt(2, true);
                    tabbedPane.setEnabledAt(1, false);
                } else {
                    reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, false, labels, repositoryNb).buildEditorPanel(errors), 0);
                    tabbedPane.setEnabledAt(0, true);
                    tabbedPane.setEnabledAt(1, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
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

            if(nameInstitutionTfs.size() > 0) {
                eag.getArchguide().getIdentity().getAutform().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : nameInstitutionTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        Autform autform = new Autform();
                        autform.setContent(textFieldWithLanguage.getTextValue());
                        autform.setLang(textFieldWithLanguage.getLanguage());
                        eag.getArchguide().getIdentity().getAutform().add(autform);
                        hasChanged = true;
                    }
                }
            }

            if(parallelNameTfs.size() > 0) {
                eag.getArchguide().getIdentity().getParform().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : parallelNameTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        Parform parform = new Parform();
                        parform.setContent(textFieldWithLanguage.getTextValue());
                        parform.setLang(textFieldWithLanguage.getLanguage());
                        eag.getArchguide().getIdentity().getParform().add(parform);
                        hasChanged = true;
                    }
                }
            }

            int counterForHoldingsTfs = 0;
            if(formerlyUsedNameTfs.size() > 0) {
                eag.getArchguide().getIdentity().getNonpreform().clear();
                for (FormerlyUsedName formerlyUsedName : formerlyUsedNameTfs) {
                    if (StringUtils.isNotEmpty(formerlyUsedName.getName())) {
                        Nonpreform nonpreform = new Nonpreform();
                        nonpreform.setLang(formerlyUsedName.getLanguage());
                        nonpreform.getContent().add(formerlyUsedName.getName());
                        List<TextFieldWithDate> yearsTfs = formerlyUsedName.getDateList();
                        UseDates useDates = new UseDates();

                        useDates.setDate(null);
                        useDates.setDateRange(null);
                        useDates.setDateSet(null);
                        int counterDate = 0;
                        int counterDateRange = 0;
                        boolean isDateSet = false;
                        for (TextFieldWithDate yearTextWithDate : yearsTfs) {
                            if (StringUtils.isNotEmpty(yearTextWithDate.getDate())) {
                                counterDate++;
                            } else if (StringUtils.isNotEmpty(yearTextWithDate.getFromDate()) && StringUtils.isNotEmpty(yearTextWithDate.getToDate())) {
                                counterDateRange++;
                            }
                        }
                        if ((counterDate > 1 || counterDateRange > 1) || (counterDate > 0 && counterDateRange > 0)) {
                            useDates.setDateSet(new DateSet());
                            isDateSet = true;
                        } else {
                            if (counterDate == 1) {
                                useDates.setDate(new eu.apenet.dpt.utils.eag2012.Date());
                                String dateStr = "";
                                for (TextFieldWithDate yearTextWithDate : yearsTfs) {
                                    if (StringUtils.isNotEmpty(yearTextWithDate.getDate())) {
                                        dateStr = yearTextWithDate.getDate();
                                    }
                                }
                                useDates.getDate().setContent(dateStr);
                                counterForHoldingsTfs++;
                            } else if (counterDateRange == 1) {
                                String dateFromStr = "";
                                String dateToStr = "";
                                for (TextFieldWithDate yearTextWithDate : yearsTfs) {
                                    if (StringUtils.isNotEmpty(yearTextWithDate.getFromDate()) && StringUtils.isNotEmpty(yearTextWithDate.getToDate())) {
                                        dateFromStr = yearTextWithDate.getFromDate();
                                        dateToStr = yearTextWithDate.getToDate();
                                    }
                                }
                                useDates.setDateRange(new DateRange());
                                useDates.getDateRange().setFromDate(new FromDate());
                                useDates.getDateRange().getFromDate().setContent(dateFromStr);
                                useDates.getDateRange().setToDate(new ToDate());
                                useDates.getDateRange().getToDate().setContent(dateToStr);
                                counterForHoldingsTfs++;
                            }
                        }

                        if(isDateSet) {
                            for(TextFieldWithDate yearTextWithDate : yearsTfs) {
                                if(StringUtils.isNotEmpty(yearTextWithDate.getDate())) {
                                    eu.apenet.dpt.utils.eag2012.Date date = new eu.apenet.dpt.utils.eag2012.Date();
                                    date.setContent(yearTextWithDate.getDate());
                                    useDates.getDateSet().getDateOrDateRange().add(date);
                                    counterForHoldingsTfs++;
                                } else if(StringUtils.isNotEmpty(yearTextWithDate.getToDate()) && StringUtils.isNotEmpty(yearTextWithDate.getFromDate())) {
                                    FromDate fromDate = new FromDate();
                                    fromDate.setContent(yearTextWithDate.getFromDate());
                                    ToDate toDate = new ToDate();
                                    toDate.setContent(yearTextWithDate.getToDate());
                                    DateRange dateRange = new DateRange();
                                    dateRange.setFromDate(fromDate);
                                    dateRange.setToDate(toDate);
                                    useDates.getDateSet().getDateOrDateRange().add(dateRange);
                                    counterForHoldingsTfs++;
                                }
                            }
                        }

                        nonpreform.getContent().add(useDates);
                        eag.getArchguide().getIdentity().getNonpreform().add(nonpreform);
                        hasChanged = true;
                    }
                }
            }

            
            // type of the institution
        	eag.getArchguide().getIdentity().getRepositoryType().clear();
            for (int i = 0; i < typeInstitutionComboList.size(); i++) {
            	String value = typeInstitutionComboList.get(i).getSelectedItem().toString();
            	if (!value.equals("---")) {
            		RepositoryType repositoryType = new RepositoryType();
            		repositoryType.setValue(value);
            		eag.getArchguide().getIdentity().getRepositoryType().add(repositoryType);
            	}
            }
            //Duplicated values	
            List<String> duplicated = new LinkedList<String>();
            Set<String> concurrences = new HashSet<String>();
            for (int i=0; i<typeInstitutionComboList.size(); i++){
            	if(duplicated.contains(typeInstitutionComboList.get(i).getSelectedItem().toString())){
            		errors.add("typeInstitutionComboList");
            		concurrences.add(typeInstitutionComboList.get(i).getSelectedItem().toString());
            	}
            	else
            		duplicated.add(typeInstitutionComboList.get(i).getSelectedItem().toString());
            }

            if(!errors.isEmpty()) {
            	if(errors.contains("typeInstitutionComboList"))
            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.typeOfInstitutionRepeated") + ": " + concurrences);

                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}
