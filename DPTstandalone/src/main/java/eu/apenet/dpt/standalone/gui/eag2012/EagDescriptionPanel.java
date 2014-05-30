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
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextAreaWithLanguage;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithDate;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithLanguage;
import eu.apenet.dpt.utils.eag2012.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville Date: 28/11/2012
 *
 * @author Yoann Moranville
 */
public class EagDescriptionPanel extends EagPanels {

    private List<TextAreaWithLanguage> repositoryHistoryTfs;
    private List<TextFieldWithLanguage> repositoryFoundationTfs;
    private List<TextFieldWithLanguage> repositorySuppressionTfs;
    private List<TextAreaWithLanguage> unitAdministrativeStructureTfs;
    private List<TextAreaWithLanguage> buildingTfs;
    private List<TextAreaWithLanguage> archivalAndOthersTfs;
    private List<TextFieldWithDate> holdingsYearsTfs;
    private JTextField repositoryFoundationDateTf;
    private JTextField repositorySuppressionDateTf;
    private JTextField repositoryAreaTf;
    private JTextField lengthShelfTf;
    private JTextField extentTf;

    public EagDescriptionPanel(Eag eag, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels, int repositoryNb) {
        super(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
    }

    @Override
    protected JComponent buildEditorPanel(List<String> errors) {
        if (errors == null) {
            errors = new ArrayList<String>(0);
        } else if (Utilities.isDev && errors.size() > 0) {
            LOG.info("Errors in form:");
            for (String error : errors) {
                LOG.info(error);
            }
        }

        FormLayout layout = new FormLayout(
                "right:max(50dlu;p), 4dlu, 100dlu, 7dlu, right:p, 4dlu, 100dlu",
                EDITOR_ROW_SPEC);

        layout.setColumnGroups(new int[][]{{1, 3, 5, 7}});
        PanelBuilder builder = new PanelBuilder(layout);

        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        rowNb = 1;

        //todo here

        Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);

        builder.addSeparator(labels.getString("eag2012.description.repositoryDescription"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if (repository.getRepositorhist() == null) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            Repositorhist repositorhist = new Repositorhist();
            repositorhist.setDescriptiveNote(descriptiveNote);
            repository.setRepositorhist(repositorhist);
        }
        repositoryHistoryTfs = new ArrayList<TextAreaWithLanguage>(repository.getRepositorhist().getDescriptiveNote().getP().size());
        for (P p : repository.getRepositorhist().getDescriptiveNote().getP()) {
            builder.addLabel(labels.getString("eag2012.description.epositoryHistory"), cc.xy(1, rowNb));
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(p.getContent(), p.getLang());
            repositoryHistoryTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addRepositorhistDescriptionBtn = new ButtonTab(labels.getString("eag2012.isil.addHistoryDescription"));
        builder.add(addRepositorhistDescriptionBtn, cc.xy(1, rowNb));
        addRepositorhistDescriptionBtn.addActionListener(new EagDescriptionPanel.AddRepositorhistDescriptionBtnAction(eag, tabbedPane, model));
        setNextRow();

        if (repository.getRepositorfound() == null) {
            repository.setRepositorfound(new Repositorfound());
        }
        if(repository.getRepositorfound().getDate() == null) {
            repository.getRepositorfound().setDate(new Date());
        }
        if(repository.getRepositorfound().getRule().size() == 0) {
            repository.getRepositorfound().getRule().add(new Rule());
        }

        builder.addLabel(labels.getString("eag2012.description.foundationDate"), cc.xy(1, rowNb));
        repositoryFoundationDateTf = new JTextField(repository.getRepositorfound().getDate().getContent());
        builder.add(repositoryFoundationDateTf, cc.xy(3, rowNb));
        setNextRow();

        repositoryFoundationTfs = new ArrayList<TextFieldWithLanguage>(repository.getRepositorfound().getRule().size());
        for (Rule rule : repository.getRepositorfound().getRule()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(rule.getContent(), rule.getLang());
            repositoryFoundationTfs.add(textFieldWithLanguage);
            builder.addLabel(labels.getString("eag2012.description.ruleOfRepositoryFoundation"), cc.xy(1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addRuleFoundationBtn = new ButtonTab(labels.getString("eag2012.control.addRule"));
        builder.add(addRuleFoundationBtn, cc.xy(1, rowNb));
        addRuleFoundationBtn.addActionListener(new AddRuleFoundationBtnAction(eag, tabbedPane, model));
        setNextRow();

        if (repository.getRepositorsup() == null) {
            repository.setRepositorsup(new Repositorsup());
        }
        if(repository.getRepositorsup().getDate() == null) {
            repository.getRepositorsup().setDate(new Date());
        }
        if(repository.getRepositorsup().getRule().size() == 0) {
            repository.getRepositorsup().getRule().add(new Rule());
        }

        builder.addLabel(labels.getString("eag2012.description.dateArchiveClosure"), cc.xy(1, rowNb));
        repositorySuppressionDateTf = new JTextField(repository.getRepositorsup().getDate().getContent());
        builder.add(repositorySuppressionDateTf, cc.xy(3, rowNb));
        setNextRow();

        repositorySuppressionTfs = new ArrayList<TextFieldWithLanguage>(repository.getRepositorsup().getRule().size());
        for (Rule rule : repository.getRepositorsup().getRule()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(rule.getContent(), rule.getLang());
            repositorySuppressionTfs.add(textFieldWithLanguage);
            builder.addLabel(labels.getString("eag2012.description.ruleOfRepositorySuppression"), cc.xy(1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addRuleSuppressionBtn = new ButtonTab(labels.getString("eag2012.control.addRule"));
        builder.add(addRuleSuppressionBtn, cc.xy(1, rowNb));
        addRuleSuppressionBtn.addActionListener(new AddRuleSuppressionBtnAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.description.administrativeStructure"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if (repository.getAdminhierarchy() == null) {
            repository.setAdminhierarchy(new Adminhierarchy());
        }
        if (repository.getAdminhierarchy().getAdminunit().size() == 0) {
            repository.getAdminhierarchy().getAdminunit().add(new Adminunit());
        }
        unitAdministrativeStructureTfs = new ArrayList<TextAreaWithLanguage>(repository.getAdminhierarchy().getAdminunit().size());
        for (Adminunit adminunit : repository.getAdminhierarchy().getAdminunit()) {
            builder.addLabel(labels.getString("eag2012.description.unitOfAdministrativeStructure"), cc.xy(1, rowNb));
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(adminunit.getContent(), adminunit.getLang());
            unitAdministrativeStructureTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addUnitAdministrativeStructureBtn = new ButtonTab(labels.getString("eag2012.description.addAdministrationUnits"));
        builder.add(addUnitAdministrativeStructureBtn, cc.xy(1, rowNb));
        addUnitAdministrativeStructureBtn.addActionListener(new EagDescriptionPanel.AddUnitAdministrativeStructureBtnAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.description.buildingDescription"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if (repository.getBuildinginfo() == null) {
            repository.setBuildinginfo(new Buildinginfo());
        }
        if (repository.getBuildinginfo().getBuilding() == null) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            Building building = new Building();
            building.setDescriptiveNote(descriptiveNote);
            repository.getBuildinginfo().setBuilding(building);
        }
        buildingTfs = new ArrayList<TextAreaWithLanguage>(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().size());
        for (P p : repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP()) {
            builder.addLabel(labels.getString("eag2012.description.building"), cc.xy(1, rowNb));
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(p.getContent(), p.getLang());
            buildingTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addBuildingDescriptionBtn = new ButtonTab(labels.getString("eag2012.isil.addBuildingDescription"));
        builder.add(addBuildingDescriptionBtn, cc.xy(1, rowNb));
        addBuildingDescriptionBtn.addActionListener(new EagDescriptionPanel.AddBuildingDescriptionBtnAction(eag, tabbedPane, model));
        setNextRow();

        if (repository.getBuildinginfo().getRepositorarea() == null) {
            Repositorarea repositorarea = new Repositorarea();
            Num num = new Num();
            num.setUnit("linearmetre");
            repositorarea.setNum(num);
            repository.getBuildinginfo().setRepositorarea(repositorarea);
        }
        builder.addLabel(labels.getString("eag2012.description.repositoryArea"), cc.xy(1, rowNb));
        repositoryAreaTf = new JTextField(repository.getBuildinginfo().getRepositorarea().getNum().getContent());
        builder.add(repositoryAreaTf, cc.xy(3, rowNb));
        setNextRow();

        if (repository.getBuildinginfo().getLengthshelf() == null) {
            Lengthshelf lengthshelf = new Lengthshelf();
            Num num = new Num();
            num.setUnit("linearmetre");
            lengthshelf.setNum(num);
            repository.getBuildinginfo().setLengthshelf(lengthshelf);
        }
        builder.addLabel(labels.getString("eag2012.description.lengthShelf"), cc.xy(1, rowNb));
        lengthShelfTf = new JTextField(repository.getBuildinginfo().getLengthshelf().getNum().getContent());
        builder.add(lengthShelfTf, cc.xy(3, rowNb));
        setNextRow();


        builder.addSeparator(labels.getString("eag2012.description.holdingDescription"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if (repository.getHoldings() == null) {
            Holdings holdings = new Holdings();
            repository.setHoldings(holdings);
        }
        if (repository.getHoldings().getDescriptiveNote() == null) {
            repository.getHoldings().setDescriptiveNote(new DescriptiveNote());
        }
        if(repository.getHoldings().getDescriptiveNote().getP().size() == 0) {
            repository.getHoldings().getDescriptiveNote().getP().add(new P());
        }
        if (repository.getHoldings().getExtent() == null) {
            Extent extent = new Extent();
            Num num = new Num();
            num.setUnit("linearmetre");
            extent.setNum(num);
            repository.getHoldings().setExtent(extent);
        }
        if (repository.getHoldings().getDate() == null) {
            repository.getHoldings().setDate(new Date());
        }
        archivalAndOthersTfs = new ArrayList<TextAreaWithLanguage>(repository.getHoldings().getDescriptiveNote().getP().size());
        for (P p : repository.getHoldings().getDescriptiveNote().getP()) {
            builder.addLabel(labels.getString("eag2012.description.archivalAndOtherHoldings"), cc.xy(1, rowNb));
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(p.getContent(), p.getLang());
            archivalAndOthersTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addArchivalAndOthersDescriptionBtn = new ButtonTab(labels.getString("eag2012.isil.addArchivalAndOtherHoldingsDescription"));
        builder.add(addArchivalAndOthersDescriptionBtn, cc.xy(1, rowNb));
        addArchivalAndOthersDescriptionBtn.addActionListener(new EagDescriptionPanel.AddArchivalAndOthersDescriptionBtnAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.description.yearsOfTheHoldings"), cc.xy(1, rowNb));
        setNextRow();

        if (repository.getHoldings().getDateSet() != null) {
//            if(repository.getHoldings().getDateSet().getDateOrDateRange().size() == 0) {
//                List<Object> dates = TextChanger.transformDatesToDateOrDateRange(repository.getHoldings().getDateSet());
//                repository.getHoldings().getDateSet().getDateOrDateRange().addAll(dates);
//                repository.getHoldings().getDateSet().setDate(null);
//                repository.getHoldings().getDateSet().setDateRange(null);
//            }
            holdingsYearsTfs = new ArrayList<TextFieldWithDate>(repository.getHoldings().getDateSet().getDateOrDateRange().size());
            for (Object dateObj : repository.getHoldings().getDateSet().getDateOrDateRange()) {
                if (dateObj instanceof Date) {
                    TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", "", "", ((Date)dateObj).getContent());
                    holdingsYearsTfs.add(textFieldWithDate);
                    builder.addLabel(labels.getString("eag2012.commons.year"), cc.xy(1, rowNb));
                    builder.add(textFieldWithDate.getDateField(), cc.xy(3, rowNb));
                }
                if (dateObj instanceof DateRange) {
                    TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", ((DateRange)dateObj).getFromDate().getContent(), ((DateRange)dateObj).getToDate().getContent(), "");
                    holdingsYearsTfs.add(textFieldWithDate);
                    builder.addLabel(labels.getString("eag2012.commons.year") + " " + labels.getString("eag2012.commons.from"), cc.xy(1, rowNb));
                    builder.add(textFieldWithDate.getFromDateField(), cc.xy(3, rowNb));
                    builder.addLabel(labels.getString("eag2012.commons.to"), cc.xy(5, rowNb));
                    builder.add(textFieldWithDate.getToDateField(), cc.xy(7, rowNb));
                }
                setNextRow();
            }
        } else {
            holdingsYearsTfs = new ArrayList<TextFieldWithDate>();
            if (repository.getHoldings().getDate() != null) {
                TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", "", "", repository.getHoldings().getDate().getContent());
                holdingsYearsTfs.add(textFieldWithDate);
                builder.addLabel(labels.getString("eag2012.commons.year"), cc.xy(1, rowNb));
                builder.add(textFieldWithDate.getDateField(), cc.xy(3, rowNb));
                setNextRow();
            }
            if (repository.getHoldings().getDateRange() != null) {
                TextFieldWithDate textFieldWithDate = new TextFieldWithDate("", "", repository.getHoldings().getDateRange().getFromDate().getContent(), repository.getHoldings().getDateRange().getToDate().getContent(), "");
                holdingsYearsTfs.add(textFieldWithDate);
                builder.addLabel(labels.getString("eag2012.commons.year") + " " + labels.getString("eag2012.commons.from"), cc.xy(1, rowNb));
                builder.add(textFieldWithDate.getFromDateField(), cc.xy(3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.to"), cc.xy(5, rowNb));
                builder.add(textFieldWithDate.getToDateField(), cc.xy(7, rowNb));
                setNextRow();
            }
        }
        JButton addSingleYearBtn = new ButtonTab(labels.getString("eag2012.commons.addYearButton"));
        addSingleYearBtn.addActionListener(new AddSingleYearAction(eag, tabbedPane, model));
        builder.add(addSingleYearBtn, cc.xy(1, rowNb));
        JButton addYearRangeBtn = new ButtonTab(labels.getString("eag2012.commons.addYearRangeButton"));
        addYearRangeBtn.addActionListener(new AddYearRangeAction(eag, tabbedPane, model));
        builder.add(addYearRangeBtn, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.description.extent"), cc.xy(1, rowNb));
        extentTf = new JTextField(repository.getHoldings().getExtent().getNum().getContent());
        builder.add(extentTf, cc.xy(3, rowNb));
        setNextRow();

        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();

        JButton exitBtn = new ButtonTab(labels.getString("eag2012.commons.exit"));
        builder.add(exitBtn, cc.xy(1, rowNb));
        exitBtn.addActionListener(new EagPanels.ExitBtnAction());

        JButton previousTabBtn = new ButtonTab(labels.getString("eag2012.commons.previousTab"));
        builder.add(previousTabBtn, cc.xy(3, rowNb));
        previousTabBtn.addActionListener(new EagDescriptionPanel.ChangeTabBtnAction(eag, tabbedPane, model, false));

        if(repositoryNb == 0) {
            JButton nextTabBtn = new ButtonTab(labels.getString("eag2012.commons.nextTab"));
            builder.add(nextTabBtn, cc.xy(5, rowNb));
            nextTabBtn.addActionListener(new EagDescriptionPanel.ChangeTabBtnAction(eag, tabbedPane, model, true));
        }

        setNextRow();
        JButton saveBtn = new ButtonTab(labels.getString("eag2012.commons.save"));
        builder.add(saveBtn, cc.xy(5, rowNb));
        saveBtn.addActionListener(new EagDescriptionPanel.SaveBtnAction(eag, tabbedPane, model));

        setNextRow();
        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();
        JButton previousInstitutionTabBtn = new ButtonTab(labels.getString("eag2012.controls.previousInstitution"));
        previousInstitutionTabBtn.addActionListener(new PreviousInstitutionTabBtnAction(eag, tabbedPane, model));
        builder.add(previousInstitutionTabBtn, cc.xy(1, rowNb));
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
                    reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
                    mainTabbedPane.setEnabledAt(currentTabIndex, false);
                    mainTabbedPane.setEnabledAt(currentTabIndex+1, true);
                    mainTabbedPane.setSelectedIndex(currentTabIndex+1);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
            }
        }
    }

    public class PreviousInstitutionTabBtnAction extends UpdateEagObject {
        PreviousInstitutionTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(true);

                int currentTabIndex = mainTabbedPane.getSelectedIndex();
                if(currentTabIndex > 0) {
                    reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
                    mainTabbedPane.setEnabledAt(currentTabIndex, false);
                    mainTabbedPane.setEnabledAt(currentTabIndex-1, true);
                    mainTabbedPane.setSelectedIndex(currentTabIndex-1);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
            }
        }
    }

    public class AddRepositorhistDescriptionBtnAction extends EagDescriptionPanel.UpdateEagObject {

        AddRepositorhistDescriptionBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getRepositorhist() == null) {
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).setRepositorhist(new Repositorhist());
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getRepositorhist().setDescriptiveNote(new DescriptiveNote());
            }

            boolean empty = false;
            int pos = repositoryHistoryTfs.size();
            for(int i=0; i<pos;i++){
                if( (repositoryHistoryTfs.get(i).getTextValue()==null || repositoryHistoryTfs.get(i).getTextValue().toString().trim().compareTo("") == 0))
                	empty = true;
            }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.epositoryHistory"));

			eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getRepositorhist().getDescriptiveNote().getP().add(new P());
			reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
        }
    }
    
    public class AddRuleFoundationBtnAction extends UpdateEagObject {
        AddRuleFoundationBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getRepositorfound() == null)
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).setRepositorfound(new Repositorfound());
              
            boolean empty = false;
            int pos = repositoryFoundationTfs.size();
            for(int i=0; i<pos;i++){
                if( (repositoryFoundationTfs.get(i).getTextValue()==null || repositoryFoundationTfs.get(i).getTextValue().toString().trim().compareTo("") == 0))
                	empty = true;
            }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.ruleOfRepositoryFoundation"));
 
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getRepositorfound().getRule().add(new Rule());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
        }
    }
    
    public class AddRuleSuppressionBtnAction extends UpdateEagObject {
        AddRuleSuppressionBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getRepositorsup() == null) 
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).setRepositorsup(new Repositorsup());
            
            boolean empty = false;
            int pos = repositorySuppressionTfs.size();
            for(int i=0; i<pos;i++){
                if( (repositorySuppressionTfs.get(i).getTextValue()==null || repositorySuppressionTfs.get(i).getTextValue().toString().trim().compareTo("") == 0))
                	empty = true;
            }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.ruleOfRepositorySuppression"));

            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getRepositorsup().getRule().add(new Rule());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
        }
    }

    public class AddUnitAdministrativeStructureBtnAction extends EagDescriptionPanel.UpdateEagObject {

        AddUnitAdministrativeStructureBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getAdminhierarchy() == null) 
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).setAdminhierarchy(new Adminhierarchy());
            
            boolean empty = false;
            int pos = unitAdministrativeStructureTfs.size();
            for(int i=0; i<pos;i++){
                if( (unitAdministrativeStructureTfs.get(i).getTextValue()==null || unitAdministrativeStructureTfs.get(i).getTextValue().toString().trim().compareTo("") == 0))
                	empty = true;
            }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.unitOfAdministrativeStructure"));
               
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getAdminhierarchy().getAdminunit().add(new Adminunit());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
        }
    }

    public class AddBuildingDescriptionBtnAction extends EagDescriptionPanel.UpdateEagObject {

        AddBuildingDescriptionBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getBuildinginfo() == null) {
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).setBuildinginfo(new Buildinginfo());
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getBuildinginfo().setBuilding(new Building());
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getBuildinginfo().getBuilding().setDescriptiveNote(new DescriptiveNote());
            }
            
            boolean empty = false;
            int pos = buildingTfs.size();
            for(int i=0; i<pos;i++){
                if( (buildingTfs.get(i).getTextValue()==null || buildingTfs.get(i).getTextValue().toString().trim().compareTo("") == 0))
                	empty = true;
            }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.building"));

            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getBuildinginfo().getBuilding().getDescriptiveNote().getP().add(new P());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
        }
    }

    public class AddArchivalAndOthersDescriptionBtnAction extends EagDescriptionPanel.UpdateEagObject {

        AddArchivalAndOthersDescriptionBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings() == null) {
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).setHoldings(new Holdings());
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().setDescriptiveNote(new DescriptiveNote());
            }
            
            boolean empty = false;
            int pos = archivalAndOthersTfs.size();
            for(int i=0; i<pos;i++){
                if( (archivalAndOthersTfs.get(i).getTextValue()==null || archivalAndOthersTfs.get(i).getTextValue().toString().trim().compareTo("") == 0))
                	empty = true;
            }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.archivalAndOtherHoldings"));
 
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDescriptiveNote().getP().add(new P());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
        }
    }

    public class AddSingleYearAction extends EagDescriptionPanel.UpdateEagObject {

        AddSingleYearAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings() == null) {
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).setHoldings(new Holdings());
            }
            
            boolean empty = false;
            int pos = holdingsYearsTfs.size();
            for(int i=0; i<pos;i++){
                if( (holdingsYearsTfs.get(i).getTextValue()==null || holdingsYearsTfs.get(i).getTextValue().toString().trim().compareTo("") == 0))
                	empty = true;
            }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.year"));


            if(TextChanger.isDateSetReady(holdingsYearsTfs, true, false)) {
                if (eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateSet() == null) {
                    eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().setDateSet(new DateSet());
                }
                if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDate() != null) {
                    eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateSet().getDateOrDateRange().add(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDate());
                }
                if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateRange() != null) {
                    eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateSet().getDateOrDateRange().add(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateRange());
                }

                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateSet().getDateOrDateRange().add(new Date());
            } else {
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().setDate(new Date());
            }
            
            
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
        }
    }

    public class AddYearRangeAction extends EagDescriptionPanel.UpdateEagObject {

        AddYearRangeAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(true);
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings() == null) {
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).setHoldings(new Holdings());
            }

            DateRange dateRange = new DateRange();
            dateRange.setFromDate(new FromDate());
            dateRange.setToDate(new ToDate());
                       
            boolean empty = false;
            int pos = holdingsYearsTfs.size();
            for(int i=0; i<pos;i++){
                if( (holdingsYearsTfs.get(i).getTextValue()==null || holdingsYearsTfs.get(i).getTextValue().toString().trim().compareTo("") == 0))
                	empty = true;
            }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.year"));
          
            if(TextChanger.isDateSetReady(holdingsYearsTfs, false, true)) {
                if (eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateSet() == null) {
                    eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().setDateSet(new DateSet());
                }
                if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDate() != null) {
                    eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateSet().getDateOrDateRange().add(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDate());
                }
                if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateRange() != null) {
                    eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateSet().getDateOrDateRange().add(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateRange());
                }
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().getDateSet().getDateOrDateRange().add(dateRange);
            } else {
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getHoldings().setDateRange(dateRange);
            }

            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
        }
    }

    public class SaveBtnAction extends EagDescriptionPanel.UpdateEagObject {

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
                reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
            }
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

                if (isNextTab) {
                    reloadTabbedPanel(new EagControlPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 5);
                    tabbedPane.setEnabledAt(5, true);
                    tabbedPane.setEnabledAt(4, false);
                } else {
                    reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
                    tabbedPane.setEnabledAt(3, true);
                    tabbedPane.setEnabledAt(4, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
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

            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);

            int counterRepositorhistoryTfs = 0;
            if (repositoryHistoryTfs.size() > 0) {
                if(repository.getRepositorhist() == null) {
                    repository.setRepositorhist(new Repositorhist());
                }
                if(repository.getRepositorhist().getDescriptiveNote() == null) {
                    repository.getRepositorhist().setDescriptiveNote(new DescriptiveNote());
                }
                repository.getRepositorhist().getDescriptiveNote().getP().clear();
                for (TextAreaWithLanguage textAreaWithLanguage : repositoryHistoryTfs) {
                    if (StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue().trim())) {
                        counterRepositorhistoryTfs++;
                        P p = new P();
                        p.setContent(textAreaWithLanguage.getTextValue().trim());
                        p.setLang(textAreaWithLanguage.getLanguage());
                        repository.getRepositorhist().getDescriptiveNote().getP().add(p);
                    }
                }
            }
            if (counterRepositorhistoryTfs == 0)
                repository.setRepositorhist(null);


            if (StringUtils.isNotEmpty(repositoryFoundationDateTf.getText())) {
                repository.getRepositorfound().getDate().setContent(repositoryFoundationDateTf.getText());
            }
            boolean repositoryFoundationExists = false;
            if (repositoryFoundationTfs.size() > 0) {
                if(repository.getRepositorfound() == null) {
                    repository.setRepositorfound(new Repositorfound());
                }
                repository.getRepositorfound().getRule().clear();
                for (TextFieldWithLanguage textFieldWithLanguage : repositoryFoundationTfs) {
                    if (StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        Rule rule = new Rule();
                        rule.setContent(textFieldWithLanguage.getTextValue());
                        rule.setLang(textFieldWithLanguage.getLanguage());
                        repository.getRepositorfound().getRule().add(rule);
                        repositoryFoundationExists = true;
                    }
                }
            }
            if (!repositoryFoundationExists) {
                repository.setRepositorfound(null);
            }

            if (StringUtils.isNotEmpty(repositorySuppressionDateTf.getText())) {
                repository.getRepositorsup().getDate().setContent(repositorySuppressionDateTf.getText());
            }
            boolean repositorySuppressionExists = false;
            if (repositorySuppressionTfs.size() > 0) {
                if(repository.getRepositorsup() == null) {
                    repository.setRepositorsup(new Repositorsup());
                }
                repository.getRepositorsup().getRule().clear();
                for (TextFieldWithLanguage textFieldWithLanguage : repositorySuppressionTfs) {
                    if (StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        Rule rule = new Rule();
                        rule.setContent(textFieldWithLanguage.getTextValue());
                        rule.setLang(textFieldWithLanguage.getLanguage());
                        repository.getRepositorsup().getRule().add(rule);
                        repositorySuppressionExists = true;
                    }
                }
            }
            if (!repositorySuppressionExists) {
                repository.setRepositorsup(null);
            }

            int counterUnitAdministrativeStructureTfs = 0;
            if (unitAdministrativeStructureTfs.size() > 0) {
                if(repository.getAdminhierarchy() == null)
                    repository.setAdminhierarchy(new Adminhierarchy());
                repository.getAdminhierarchy().getAdminunit().clear();
                for (TextAreaWithLanguage textAreaWithLanguage : unitAdministrativeStructureTfs) {
                    if (StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        counterUnitAdministrativeStructureTfs++;
                        Adminunit adminunit = new Adminunit();
                        adminunit.setContent(textAreaWithLanguage.getTextValue());
                        adminunit.setLang(textAreaWithLanguage.getLanguage());
                        repository.getAdminhierarchy().getAdminunit().add(adminunit);
                    }
                }
            }
            if(counterUnitAdministrativeStructureTfs == 0)
                repository.setAdminhierarchy(null);


            int counterBuildingTfs = 0;
            if (buildingTfs.size() > 0) {
                repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().clear();
                for (TextAreaWithLanguage textAreaWithLanguage : buildingTfs) {
                    if (StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        counterBuildingTfs++;
                        P p = new P();
                        p.setContent(textAreaWithLanguage.getTextValue());
                        p.setLang(textAreaWithLanguage.getLanguage());
                        repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().add(p);
                    }
                }
            }

            if (StringUtils.isNotEmpty(repositoryAreaTf.getText())) {
                counterBuildingTfs++;
                repository.getBuildinginfo().getRepositorarea().getNum().setContent(repositoryAreaTf.getText());
            } else {
                repository.getBuildinginfo().setRepositorarea(null);
            }

            if (StringUtils.isNotEmpty(lengthShelfTf.getText())) {
                counterBuildingTfs++;
                repository.getBuildinginfo().getLengthshelf().getNum().setContent(lengthShelfTf.getText());
            } else {
                repository.getBuildinginfo().setLengthshelf(null);
            }

            if (counterBuildingTfs == 0)
                repository.setBuildinginfo(null);


            int counterForHoldingsTfs = 0;
            int counterArchivalAndOthersTfs = 0;
            if (archivalAndOthersTfs.size() > 0) {
                repository.getHoldings().getDescriptiveNote().getP().clear();
                for (TextAreaWithLanguage textAreaWithLanguage : archivalAndOthersTfs) {
                    if (StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        counterForHoldingsTfs++;
                        counterArchivalAndOthersTfs++;
                        P p = new P();
                        p.setContent(textAreaWithLanguage.getTextValue());
                        p.setLang(textAreaWithLanguage.getLanguage());
                        repository.getHoldings().getDescriptiveNote().getP().add(p);
                    }
                }
                if(counterArchivalAndOthersTfs == 0) {
                    repository.getHoldings().setDescriptiveNote(null);
                }
                if (StringUtils.isNotEmpty(extentTf.getText())) {
                    counterForHoldingsTfs++;
                    repository.getHoldings().getExtent().getNum().setContent(extentTf.getText());
                } else {
                    repository.getHoldings().setExtent(null);
                }

                repository.getHoldings().setDate(null);
                repository.getHoldings().setDateRange(null);
                repository.getHoldings().setDateSet(null);
                int counterDate = 0;
                int counterDateRange = 0;
                boolean isDateSet = false;
                for(TextFieldWithDate holdingsYearTextWithDate : holdingsYearsTfs) {
                    if(StringUtils.isNotEmpty(holdingsYearTextWithDate.getDate())) {
                        counterDate++;
                    } else if(StringUtils.isNotEmpty(holdingsYearTextWithDate.getFromDate()) && StringUtils.isNotEmpty(holdingsYearTextWithDate.getToDate())) {
                        counterDateRange++;
                    }
                }
                if((counterDate > 1 || counterDateRange > 1) || (counterDate > 0 && counterDateRange > 0)) {
                    repository.getHoldings().setDateSet(new DateSet());
                    isDateSet = true;
                } else {
                    if(counterDate == 1) {
                        repository.getHoldings().setDate(new Date());
                        String dateStr = "";
                        for(TextFieldWithDate holdingsYearTextWithDate : holdingsYearsTfs) {
                            if(StringUtils.isNotEmpty(holdingsYearTextWithDate.getDate())) {
                                dateStr = holdingsYearTextWithDate.getDate();
                            }
                        }
                        repository.getHoldings().getDate().setContent(dateStr);
                        counterForHoldingsTfs++;
                    } else if(counterDateRange == 1) {
                        String dateFromStr = "";
                        String dateToStr = "";
                        for(TextFieldWithDate holdingsYearTextWithDate : holdingsYearsTfs) {
                            if(StringUtils.isNotEmpty(holdingsYearTextWithDate.getFromDate()) && StringUtils.isNotEmpty(holdingsYearTextWithDate.getToDate())) {
                                dateFromStr = holdingsYearTextWithDate.getFromDate();
                                dateToStr = holdingsYearTextWithDate.getToDate();
                            }
                        }
                        repository.getHoldings().setDateRange(new DateRange());
                        repository.getHoldings().getDateRange().setFromDate(new FromDate());
                        repository.getHoldings().getDateRange().getFromDate().setContent(dateFromStr);
                        repository.getHoldings().getDateRange().setToDate(new ToDate());
                        repository.getHoldings().getDateRange().getToDate().setContent(dateToStr);
                        counterForHoldingsTfs++;
                    }
                }

                if(isDateSet) {
                    for(TextFieldWithDate holdingsYearTextWithDate : holdingsYearsTfs) {
                        if(StringUtils.isNotEmpty(holdingsYearTextWithDate.getDate())) {
                            Date date = new Date();
                            date.setContent(holdingsYearTextWithDate.getDate());
                            repository.getHoldings().getDateSet().getDateOrDateRange().add(date);
                            counterForHoldingsTfs++;
                        } else if(StringUtils.isNotEmpty(holdingsYearTextWithDate.getToDate()) && StringUtils.isNotEmpty(holdingsYearTextWithDate.getFromDate())) {
                            FromDate fromDate = new FromDate();
                            fromDate.setContent(holdingsYearTextWithDate.getFromDate());
                            ToDate toDate = new ToDate();
                            toDate.setContent(holdingsYearTextWithDate.getToDate());
                            DateRange dateRange = new DateRange();
                            dateRange.setFromDate(fromDate);
                            dateRange.setToDate(toDate);
                            repository.getHoldings().getDateSet().getDateOrDateRange().add(dateRange);
                            counterForHoldingsTfs++;
                        }
                    }
                }
            }
            if (counterForHoldingsTfs == 0)
                repository.setHoldings(null);

            if (!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }

}
