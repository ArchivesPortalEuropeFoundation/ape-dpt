package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import static eu.apenet.dpt.standalone.gui.eag2012.EagPanels.LOG;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.text.AbstractDocument.Content;

/**
 * User: Yoann Moranville Date: 28/11/2012
 *
 * @author Yoann Moranville
 */
public class EagDescriptionPanel extends EagPanels {

    private List<TextFieldWithLanguage> repositoryHistoryTfs;
    private List<TextFieldWithLanguage> repositoryFoundationTfs;
    private JTextField repositoryFoundationDateTf;
    private List<TextFieldWithLanguage> repositorySuppressionTfs;
    private JTextField repositorySuppressionDateTf;
    private List<TextFieldWithLanguage> unitAdministrativeStructureTfs;
    private List<TextFieldWithLanguage> buildingTfs;
    private JTextField repositoryAreaTf;
    private JTextField lengthShelfTf;
    private List<TextFieldWithLanguage> archivalAndOthersTfs;
    private JTextField dateHoldingsTf;
    private JTextField rangeFromTf;
    private JTextField rangeToTf;
    private JTextField extentTf;

    public EagDescriptionPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels) {
        super(eag, tabbedPane, eag2012Frame, model, labels);
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

        Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

        builder.addSeparator(labels.getString("eag2012.repositoryDescription"), cc.xy(1, rowNb));
        setNextRow();

        if(repository.getRepositorhist() == null) {
         DescriptiveNote descriptiveNote = new DescriptiveNote();
         descriptiveNote.getP().add(new P());
         Repositorhist repositorhist = new Repositorhist();
         repositorhist.setDescriptiveNote(descriptiveNote);
         repository.setRepositorhist(repositorhist);
         }
        repositoryHistoryTfs = new ArrayList<TextFieldWithLanguage>(repository.getRepositorhist().getDescriptiveNote().getP().size());
        for (P p : repository.getRepositorhist().getDescriptiveNote().getP()) {
            builder.addLabel(labels.getString("eag2012.historyOfArchive"), cc.xy(1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(p.getContent(), p.getLang());
            repositoryHistoryTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addRepositorhistDescriptionBtn = new ButtonEag(labels.getString("eag2012.addHistoryDescriptionButton"));
        builder.add(addRepositorhistDescriptionBtn, cc.xy(1, rowNb));
        addRepositorhistDescriptionBtn.addActionListener(new EagDescriptionPanel.AddRepositorhistDescriptionBtnAction(eag, tabbedPane, model));
        setNextRow();
        
        if (repository.getRepositorfound() == null) {
            Repositorfound repositorfound = new Repositorfound();
            repositorfound.setDate(new Date());
            repositorfound.getRule().add(new Rule());
            repository.setRepositorfound(repositorfound);
        }

        builder.addLabel(labels.getString("eag2012.dateArchiveFoundation"), cc.xy(1, rowNb));
        repositoryFoundationDateTf = new JTextField(repository.getRepositorfound().getDate().getContent());
        builder.add(repositoryFoundationDateTf, cc.xy(3, rowNb));
        setNextRow();

        repositoryFoundationTfs = new ArrayList<TextFieldWithLanguage>(repository.getRepositorfound().getRule().size());
        for(Rule rule : repository.getRepositorfound().getRule()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(rule.getContent(), rule.getLang());
            repositoryFoundationTfs.add(textFieldWithLanguage);
            builder.addLabel(labels.getString("eag2012.archiveFoundingAct"), cc.xy(1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }

        if (repository.getRepositorsup() == null) {
            Repositorsup repositorsup = new Repositorsup();
            repositorsup.setDate(new Date());
            repositorsup.getRule().add(new Rule());
            repository.setRepositorsup(repositorsup);
        }

        builder.addLabel(labels.getString("eag2012.dateArchiveClosure"), cc.xy(1, rowNb));
        repositorySuppressionDateTf = new JTextField(repository.getRepositorsup().getDate().getContent());
        builder.add(repositorySuppressionDateTf, cc.xy(3, rowNb));
        setNextRow();

        repositorySuppressionTfs = new ArrayList<TextFieldWithLanguage>(repository.getRepositorsup().getRule().size());
        for(Rule rule : repository.getRepositorsup().getRule()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(rule.getContent(), rule.getLang());
            repositorySuppressionTfs.add(textFieldWithLanguage);
            builder.addLabel(labels.getString("eag2012.archiveClosureAct"), cc.xy(1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }

        builder.addSeparator(labels.getString("eag2012.administrativeStructure"), cc.xy(1, rowNb));
        setNextRow();

        if (repository.getAdminhierarchy() == null) {
            repository.setAdminhierarchy(new Adminhierarchy());
        }
        if (repository.getAdminhierarchy().getAdminunit().size() == 0) {
            repository.getAdminhierarchy().getAdminunit().add(new Adminunit());
        }
        unitAdministrativeStructureTfs = new ArrayList<TextFieldWithLanguage>(repository.getAdminhierarchy().getAdminunit().size());
        for (Adminunit adminunit : repository.getAdminhierarchy().getAdminunit()) {
            builder.addLabel(labels.getString("eag2012.unitAdministrativeStructure"), cc.xy(1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(adminunit.getContent(), adminunit.getLang());
            unitAdministrativeStructureTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addUnitAdministrativeStructureBtn = new ButtonEag(labels.getString("eag2012.addUnitAdministrativeStructureButton"));
        builder.add(addUnitAdministrativeStructureBtn, cc.xy(1, rowNb));
        addUnitAdministrativeStructureBtn.addActionListener(new EagDescriptionPanel.AddUnitAdministrativeStructureBtnAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.buildingDescription"), cc.xy(1, rowNb));
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
        buildingTfs = new ArrayList<TextFieldWithLanguage>(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().size());
        for (P p : repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP()) {
            builder.addLabel(labels.getString("eag2012.building"), cc.xy(1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(p.getContent(), p.getLang());
            buildingTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addBuildingDescriptionBtn = new ButtonEag(labels.getString("eag2012.addBuildingDescriptionButton"));
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
        builder.addLabel(labels.getString("eag2012.repositoryArea"), cc.xy(1, rowNb));
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
        builder.addLabel(labels.getString("eag2012.lengthShelf"), cc.xy(1, rowNb));
        lengthShelfTf = new JTextField(repository.getBuildinginfo().getLengthshelf().getNum().getContent());
        builder.add(lengthShelfTf, cc.xy(3, rowNb));
        setNextRow();


        builder.addSeparator(labels.getString("eag2012.holdingDescription"), cc.xy(1, rowNb));
        setNextRow();

        if (repository.getHoldings() == null) {
            Holdings holdings = new Holdings();
            repository.setHoldings(holdings);
        }
        if (repository.getHoldings().getDescriptiveNote() == null) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            repository.getHoldings().setDescriptiveNote(descriptiveNote);
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
        if (repository.getHoldings().getDateRange() == null) {
            repository.getHoldings().setDateRange(new DateRange());
            repository.getHoldings().getDateRange().setFromDate(new FromDate());
            repository.getHoldings().getDateRange().setToDate(new ToDate());
        }
        archivalAndOthersTfs = new ArrayList<TextFieldWithLanguage>(repository.getHoldings().getDescriptiveNote().getP().size());
        for (P p : repository.getHoldings().getDescriptiveNote().getP()) {
            builder.addLabel(labels.getString("eag2012.archivalAndOtherHoldings"), cc.xy(1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(p.getContent(), p.getLang());
            archivalAndOthersTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addArchivalAndOthersDescriptionBtn = new ButtonEag(labels.getString("eag2012.addArchivalAndOtherHoldingsDescriptionButton"));
        builder.add(addArchivalAndOthersDescriptionBtn, cc.xy(1, rowNb));
        addArchivalAndOthersDescriptionBtn.addActionListener(new EagDescriptionPanel.AddArchivalAndOthersDescriptionBtnAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.yearsOfHoldings"), cc.xy(1, rowNb));
        setNextRow();
        
        builder.addLabel(labels.getString("eag2012.yearLabel"), cc.xy(1, rowNb));
        dateHoldingsTf = new JTextField(repository.getHoldings().getDate().getContent());
        builder.add(dateHoldingsTf, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.yearLabel") + " " + labels.getString("eag2012.fromLabel"), cc.xy(1, rowNb));
        rangeFromTf = new JTextField(repository.getHoldings().getDateRange().getFromDate().getContent());
        builder.add(rangeFromTf, cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.toLabel"), cc.xy(5, rowNb));
        rangeToTf = new JTextField(repository.getHoldings().getDateRange().getToDate().getContent());
        builder.add(rangeToTf, cc.xy(7, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.extent"), cc.xy(1, rowNb));
        extentTf = new JTextField(repository.getHoldings().getExtent().getNum().getContent());
        builder.add(extentTf, cc.xy(3, rowNb));
        setNextRow();


        JButton exitBtn = new ButtonEag(labels.getString("eag2012.exitButton"));
        builder.add(exitBtn, cc.xy(1, rowNb));
        exitBtn.addActionListener(new EagPanels.ExitBtnAction());

        JButton previousTabBtn = new ButtonEag(labels.getString("eag2012.previousTabButton"));
        builder.add(previousTabBtn, cc.xy(3, rowNb));
        previousTabBtn.addActionListener(new EagDescriptionPanel.ChangeTabBtnAction(eag, tabbedPane, model, false));

        JButton nextTabBtn = new ButtonEag(labels.getString("eag2012.nextTabButton"));
        builder.add(nextTabBtn, cc.xy(5, rowNb));
        nextTabBtn.addActionListener(new EagDescriptionPanel.ChangeTabBtnAction(eag, tabbedPane, model, true));

        if (Utilities.isDev) {
            setNextRow();
            JButton saveBtn = new ButtonEag(labels.getString("eag2012.saveButton"));
            builder.add(saveBtn, cc.xy(5, rowNb));
            saveBtn.addActionListener(new EagDescriptionPanel.SaveBtnAction(eag, tabbedPane, model));
        }

        return builder.getPanel();
    }

    public class AddRepositorhistDescriptionBtnAction extends EagDescriptionPanel.UpdateEagObject {

        AddRepositorhistDescriptionBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(0).getRepositorhist().getDescriptiveNote().getP().add(new P());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 0);
        }
    }

    public class AddUnitAdministrativeStructureBtnAction extends EagDescriptionPanel.UpdateEagObject {

        AddUnitAdministrativeStructureBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(0).getAdminhierarchy() == null)
                return;
            eag.getArchguide().getDesc().getRepositories().getRepository().get(0).getAdminhierarchy().getAdminunit().add(new Adminunit());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 0);
        }
    }

    public class AddBuildingDescriptionBtnAction extends EagDescriptionPanel.UpdateEagObject {

        AddBuildingDescriptionBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(0).getBuildinginfo().getBuilding().getDescriptiveNote().getP().add(new P());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 0);
        }
    }

    public class AddArchivalAndOthersDescriptionBtnAction extends EagDescriptionPanel.UpdateEagObject {

        AddArchivalAndOthersDescriptionBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(0).getHoldings().getDescriptiveNote().getP().add(new P());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 0);
        }
    }

    public class SaveBtnAction extends EagDescriptionPanel.UpdateEagObject {

        SaveBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
                super.saveFile(eag.getControl().getRecordId().getValue());
                closeFrame();
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 0);
            }
        }
    }

    public class ChangeTabBtnAction extends EagDescriptionPanel.UpdateEagObject {

        private boolean isNextTab;

        ChangeTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model, boolean isNextTab) {
            super(eag, tabbedPane, model);
            this.isNextTab = isNextTab;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();

                if (isNextTab) {
                    reloadTabbedPanel(new EagControlPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 5);
                    tabbedPane.setEnabledAt(5, true);
                    tabbedPane.setEnabledAt(4, false);
                } else {
                    reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 3);
                    tabbedPane.setEnabledAt(3, true);
                    tabbedPane.setEnabledAt(4, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 4);
            }
        }
    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        public UpdateEagObject(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        protected void updateEagObject() throws Eag2012FormException {
            errors = new ArrayList<String>();

            boolean hasChanged = false;

            if (eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) { //todo: BECAUSE FOR NOW ONLY ONE REPOSITORY!!!!
                Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

                int counterRepositorhistoryTfs = 0;
                if (repositoryHistoryTfs.size() > 0) {
                    repository.getRepositorhist().getDescriptiveNote().getP().clear();
                    for (TextFieldWithLanguage textFieldWithLanguage : repositoryHistoryTfs) {
                        if (StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                            counterRepositorhistoryTfs++;
                            P p = new P();
                            p.setContent(textFieldWithLanguage.getTextValue());
                            p.setLang(textFieldWithLanguage.getLanguage());
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
                if(repositoryFoundationTfs.size() > 0) {
                    repository.getRepositorfound().getRule().clear();
                    for(TextFieldWithLanguage textFieldWithLanguage : repositoryFoundationTfs) {
                        if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                            Rule rule = new Rule();
                            rule.setContent(textFieldWithLanguage.getTextValue());
                            rule.setLang(textFieldWithLanguage.getLanguage());
                            repository.getRepositorfound().getRule().add(rule);
                            repositoryFoundationExists = true;
                        }
                    }
                }
                if(!repositoryFoundationExists) {
                    repository.setRepositorfound(null);
                }

                if (StringUtils.isNotEmpty(repositorySuppressionDateTf.getText())) {
                    repository.getRepositorsup().getDate().setContent(repositorySuppressionDateTf.getText());
                }
                boolean repositorySuppressionExists = false;
                if(repositorySuppressionTfs.size() > 0) {
                    repository.getRepositorsup().getRule().clear();
                    for(TextFieldWithLanguage textFieldWithLanguage : repositorySuppressionTfs) {
                        if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                            Rule rule = new Rule();
                            rule.setContent(textFieldWithLanguage.getTextValue());
                            rule.setLang(textFieldWithLanguage.getLanguage());
                            repository.getRepositorsup().getRule().add(rule);
                            repositorySuppressionExists = true;
                        }
                    }
                }
                if(!repositorySuppressionExists) {
                    repository.setRepositorsup(null);
                }

                int counterUnitAdministrativeStructureTfs = 0;
                if (unitAdministrativeStructureTfs.size() > 0) {
                    if(repository.getAdminhierarchy() == null)
                        repository.setAdminhierarchy(new Adminhierarchy());
                    repository.getAdminhierarchy().getAdminunit().clear();
                    for (TextFieldWithLanguage textFieldWithLanguage : unitAdministrativeStructureTfs) {
                        if (StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                            counterUnitAdministrativeStructureTfs++;
                            Adminunit adminunit = new Adminunit();
                            adminunit.setContent(textFieldWithLanguage.getTextValue());
                            adminunit.setLang(textFieldWithLanguage.getLanguage());
                            repository.getAdminhierarchy().getAdminunit().add(adminunit);
                        }
                    }
                }
                if(counterUnitAdministrativeStructureTfs == 0)
                    repository.setAdminhierarchy(null);
                

                int counterBuildingTfs = 0;
                if (buildingTfs.size() > 0) {
                    repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().clear();
                    for (TextFieldWithLanguage textFieldWithLanguage : buildingTfs) {
                        if (StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                            counterBuildingTfs++;
                            P p = new P();
                            p.setContent(textFieldWithLanguage.getTextValue());
                            p.setLang(textFieldWithLanguage.getLanguage());
                            repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().add(p);
                        }
                    }
                }
                if (counterBuildingTfs == 0) 
                    repository.setBuildinginfo(null);
                

                if (StringUtils.isNotEmpty(repositoryAreaTf.getText())) 
                    repository.getBuildinginfo().getRepositorarea().getNum().setContent(repositoryAreaTf.getText());
                

                if (StringUtils.isNotEmpty(lengthShelfTf.getText())) 
                    repository.getBuildinginfo().getLengthshelf().getNum().setContent(lengthShelfTf.getText());
                

                int counterArchivalAndOthersTfs = 0;
                if (archivalAndOthersTfs.size() > 0) {
                    repository.getHoldings().getDescriptiveNote().getP().clear();
                    for (TextFieldWithLanguage textFieldWithLanguage : archivalAndOthersTfs) {
                        if (StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                            counterArchivalAndOthersTfs++;
                            P p = new P();
                            p.setContent(textFieldWithLanguage.getTextValue());
                            p.setLang(textFieldWithLanguage.getLanguage());
                            repository.getHoldings().getDescriptiveNote().getP().add(p);
                        }
                    }
                    if (StringUtils.isNotEmpty(extentTf.getText())) {
                        repository.getHoldings().getExtent().getNum().setContent(extentTf.getText());
                    } else {
                        repository.getHoldings().setExtent(null);
                    }
                    if(StringUtils.isNotEmpty(dateHoldingsTf.getText()) && StringUtils.isNotEmpty(rangeFromTf.getText()) && StringUtils.isNotEmpty(rangeToTf.getText())) {
                        if(repository.getHoldings().getDateSet() == null) {
                            repository.getHoldings().setDateSet(new DateSet());
                            repository.getHoldings().getDateSet().setDate(new Date());
                            repository.getHoldings().getDateSet().setDateRange(new DateRange());
                            repository.getHoldings().getDateSet().getDateRange().setFromDate(new FromDate());
                            repository.getHoldings().getDateSet().getDateRange().setToDate(new ToDate());
                        }
                        repository.getHoldings().getDateSet().getDate().setContent(dateHoldingsTf.getText());
                        repository.getHoldings().getDateSet().getDateRange().getFromDate().setContent(rangeFromTf.getText());
                        repository.getHoldings().getDateSet().getDateRange().getToDate().setContent(rangeToTf.getText());
                        repository.getHoldings().setDate(null);
                        repository.getHoldings().setDateRange(null);
                    } else {
                        if (StringUtils.isNotEmpty(dateHoldingsTf.getText())) {
                            repository.getHoldings().getDate().setContent(dateHoldingsTf.getText());
                        } else {
                            repository.getHoldings().setDate(null);
                        }
                        if (StringUtils.isNotEmpty(rangeFromTf.getText()) && StringUtils.isNotEmpty(rangeToTf.getText())) {
                            repository.getHoldings().getDateRange().getFromDate().setContent(rangeFromTf.getText());
                            repository.getHoldings().getDateRange().getToDate().setContent(rangeToTf.getText());
                        } else {
                            repository.getHoldings().setDateRange(null);
                        }
                    }
                }
                if (counterArchivalAndOthersTfs == 0) 
                    repository.setHoldings(null);
                
/*                Accessibility accessibility = new Accessibility();
                accessibility.setQuestion("yes");
                repository.getAccessibility().add(accessibility);*/
            }

            if (!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}
