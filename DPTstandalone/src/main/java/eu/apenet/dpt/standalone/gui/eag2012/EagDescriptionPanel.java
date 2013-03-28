package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 28/11/2012
 *
 * @author Yoann Moranville
 */
public class EagDescriptionPanel extends EagPanels {

    private TextFieldWithLanguage repositoryHistoryTf;
    private TextFieldWithLanguage repositoryFoundationTf;
    private TextFieldWithLanguage repositorySuppressionTf;
    private List<TextFieldWithLanguage> unitAdministrativeStructureTfs;
    private TextFieldWithLanguage buildingTf;
    private JTextField repositoryAreaTf;
    private JTextField lengthShelfTf;
    private TextFieldWithLanguage archivalAndOthersTf;
    private JTextField dateHoldingsTf;
    private JTextField rangeFromTf;
    private JTextField rangeToTf;
    private JTextField extentTf;

    public EagDescriptionPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels) {
        super(eag, tabbedPane, eag2012Frame, model, labels);
    }

    @Override
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
        builder.addLabel(labels.getString("eag2012.repositoryHistory"), cc.xy(1, rowNb));
        repositoryHistoryTf = new TextFieldWithLanguage(repository.getRepositorhist().getDescriptiveNote().getP().get(0).getContent(), repository.getRepositorhist().getDescriptiveNote().getLang());
        builder.add(repositoryHistoryTf.getTextField(), cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
        builder.add(repositoryHistoryTf.getLanguageBox(), cc.xy(7, rowNb));
        setNextRow();

        if(repository.getRepositorfound() == null) {
            Repositorfound repositorfound = new Repositorfound();
            repositorfound.setDate(new Date());
            repositorfound.setRule(new Rule());
            repository.setRepositorfound(repositorfound);
        }
        builder.addLabel(labels.getString("eag2012.dateRepositoryFoundation"), cc.xy(1, rowNb));
        repositoryFoundationTf = new TextFieldWithLanguage(repository.getRepositorfound().getRule().getContent(), repository.getRepositorfound().getRule().getLang(), repository.getRepositorfound().getDate().getContent());
        builder.add(repositoryFoundationTf.getExtraField(), cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.ruleRepositoryFoundation"), cc.xy(1, rowNb));
        builder.add(repositoryFoundationTf.getTextField(), cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
        builder.add(repositoryFoundationTf.getLanguageBox(), cc.xy(7, rowNb));
        setNextRow();

        if(repository.getRepositorsup() == null) {
            Repositorsup repositorsup = new Repositorsup();
            repositorsup.setDate(new Date());
            repositorsup.setRule(new Rule());
            repository.setRepositorsup(repositorsup);
        }
        builder.addLabel(labels.getString("eag2012.dateRepositorySuppression"), cc.xy(1, rowNb));
        repositorySuppressionTf = new TextFieldWithLanguage(repository.getRepositorsup().getRule().getContent(), repository.getRepositorsup().getRule().getLang(), repository.getRepositorsup().getDate().getContent());
        builder.add(repositorySuppressionTf.getExtraField(), cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.ruleRepositorySuppression"), cc.xy(1, rowNb));
        builder.add(repositorySuppressionTf.getTextField(), cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
        builder.add(repositorySuppressionTf.getLanguageBox(), cc.xy(7, rowNb));
        setNextRow();


        builder.addSeparator(labels.getString("eag2012.administrativeStructure"), cc.xy(1, rowNb));
        setNextRow();

        if(repository.getAdminhierarchy() == null)
            repository.setAdminhierarchy(new Adminhierarchy());
        if(repository.getAdminhierarchy().getAdminunit().size() == 0)
            repository.getAdminhierarchy().getAdminunit().add(new Adminunit());
        unitAdministrativeStructureTfs = new ArrayList<TextFieldWithLanguage>(repository.getAdminhierarchy().getAdminunit().size());
        for(Adminunit adminunit : repository.getAdminhierarchy().getAdminunit()) {
            builder.addLabel(labels.getString("eag2012.unitAdministrativeStructure"),    cc.xy (1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(adminunit.getContent(), adminunit.getLang());
            unitAdministrativeStructureTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(),                     cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addUnitAdministrativeStructureBtn = new ButtonEag(labels.getString("eag2012.addUnitAdministrativeStructureButton"));
        builder.add(addUnitAdministrativeStructureBtn, cc.xy (1, rowNb));
        addUnitAdministrativeStructureBtn.addActionListener(new AddUnitAdministrativeStructureBtnAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.buildingDescription"), cc.xy(1, rowNb));
        setNextRow();

        if(repository.getBuildinginfo() == null) {
            repository.setBuildinginfo(new Buildinginfo());
        }
        if(repository.getBuildinginfo().getBuilding() == null) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            Building building = new Building();
            building.setDescriptiveNote(descriptiveNote);
            repository.getBuildinginfo().setBuilding(building);
        }
        builder.addLabel(labels.getString("eag2012.building"), cc.xy(1, rowNb));
        buildingTf = new TextFieldWithLanguage(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(0).getContent(), repository.getBuildinginfo().getBuilding().getDescriptiveNote().getLang());
        builder.add(buildingTf.getTextField(), cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
        builder.add(buildingTf.getLanguageBox(), cc.xy(7, rowNb));
        setNextRow();

        if(repository.getBuildinginfo().getRepositorarea() == null) {
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

        if(repository.getBuildinginfo().getLengthshelf() == null) {
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

        if(repository.getHoldings() == null) {
            Holdings holdings = new Holdings();
            repository.setHoldings(holdings);
        }
        if(repository.getHoldings().getDescriptiveNote() == null) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            repository.getHoldings().setDescriptiveNote(descriptiveNote);
        }
        if(repository.getHoldings().getExtent() == null) {
            Extent extent = new Extent();
            Num num = new Num();
            num.setUnit("linearmetre");
            extent.setNum(num);
            repository.getHoldings().setExtent(extent);
        }
        if(repository.getHoldings().getDate() == null) {
            repository.getHoldings().setDate(new Date());
        }
        if(repository.getHoldings().getDateRange() == null) {
            repository.getHoldings().setDateRange(new DateRange());
            repository.getHoldings().getDateRange().setFromDate(new FromDate());
            repository.getHoldings().getDateRange().setToDate(new ToDate());
        }
        builder.addLabel(labels.getString("eag2012.archivalAndOtherHoldings"), cc.xy(1, rowNb));
        archivalAndOthersTf = new TextFieldWithLanguage(repository.getHoldings().getDescriptiveNote().getP().get(0).getContent(), repository.getHoldings().getDescriptiveNote().getLang());
        builder.add(archivalAndOthersTf.getTextField(), cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
        builder.add(archivalAndOthersTf.getLanguageBox(), cc.xy(7, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.dateOfHoldings"), cc.xy(1, rowNb));
        dateHoldingsTf = new JTextField(repository.getHoldings().getDate().getContent());
        builder.add(dateHoldingsTf, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.rangeFrom"), cc.xy(1, rowNb));
        rangeFromTf = new JTextField(repository.getHoldings().getDateRange().getFromDate().getContent());
        builder.add(rangeFromTf, cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.rangeTo"), cc.xy(5, rowNb));
        rangeToTf = new JTextField(repository.getHoldings().getDateRange().getToDate().getContent());
        builder.add(rangeToTf, cc.xy(7, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.extent"), cc.xy(1, rowNb));
        extentTf = new JTextField(repository.getHoldings().getExtent().getNum().getContent());
        builder.add(extentTf, cc.xy(3, rowNb));
        setNextRow();


        JButton exitBtn = new ButtonEag(labels.getString("eag2012.exitButton"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton previousTabBtn = new ButtonEag(labels.getString("eag2012.previousTabButton"));
        builder.add(previousTabBtn, cc.xy (3, rowNb));
        previousTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, false));

        JButton nextTabBtn = new ButtonEag(labels.getString("eag2012.nextTabButton"));
        builder.add(nextTabBtn, cc.xy (5, rowNb));
        nextTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, true));

        if(Utilities.isDev) {
            setNextRow();
            JButton saveBtn = new ButtonEag(labels.getString("eag2012.saveButton"));
            builder.add(saveBtn, cc.xy (5, rowNb));
            saveBtn.addActionListener(new SaveBtnAction(eag, tabbedPane, model));
        }

        return builder.getPanel();
    }

    public class AddUnitAdministrativeStructureBtnAction extends UpdateEagObject {
        AddUnitAdministrativeStructureBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(0).getAdminhierarchy().getAdminunit().add(new Adminunit());
            reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 0);
        }
    }

    public class SaveBtnAction extends UpdateEagObject {
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

    public class ChangeTabBtnAction extends UpdateEagObject {
        private boolean isNextTab;
        ChangeTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model, boolean isNextTab) {
            super(eag, tabbedPane, model);
            this.isNextTab = isNextTab;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();

                if(isNextTab) {
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

            if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) { //todo: BECAUSE FOR NOW ONLY ONE REPOSITORY!!!!
                Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

                if(StringUtils.isNotEmpty(repositoryHistoryTf.getTextValue())) {
                    repository.getRepositorhist().getDescriptiveNote().getP().get(0).setContent(repositoryHistoryTf.getTextValue());
                    repository.getRepositorhist().getDescriptiveNote().setLang(repositoryHistoryTf.getLanguage());
                }

                if(StringUtils.isNotEmpty(repositoryFoundationTf.getTextValue())) {
                    repository.getRepositorfound().getRule().setContent(repositoryFoundationTf.getTextValue());
                    repository.getRepositorfound().getRule().setLang(repositoryFoundationTf.getLanguage());
                    repository.getRepositorfound().getDate().setContent(repositoryFoundationTf.getExtraValue());
                }

                if(StringUtils.isNotEmpty(repositorySuppressionTf.getTextValue())) {
                    repository.getRepositorsup().getRule().setContent(repositorySuppressionTf.getTextValue());
                    repository.getRepositorsup().getRule().setLang(repositorySuppressionTf.getLanguage());
                    repository.getRepositorsup().getDate().setContent(repositorySuppressionTf.getExtraValue());
                }

                int counterUnitAdministrativeStructureTfs = 0;
                if(unitAdministrativeStructureTfs.size() > 0) {
                    repository.getAdminhierarchy().getAdminunit().clear();
                    for(TextFieldWithLanguage textFieldWithLanguage : unitAdministrativeStructureTfs) {
                        if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                            counterUnitAdministrativeStructureTfs++;
                            Adminunit adminunit = new Adminunit();
                            adminunit.setContent(textFieldWithLanguage.getTextValue());
                            adminunit.setLang(textFieldWithLanguage.getLanguage());
                            repository.getAdminhierarchy().getAdminunit().add(adminunit);
                        }
                    }
                }
                if(counterUnitAdministrativeStructureTfs == 0 && repository.getAdminhierarchy() != null)
                    repository.setAdminhierarchy(null);

                if(StringUtils.isNotEmpty(buildingTf.getTextValue())) {
                    repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(0).setContent(buildingTf.getTextValue());
                    repository.getBuildinginfo().getBuilding().getDescriptiveNote().setLang(buildingTf.getLanguage());
                }

                if(StringUtils.isNotEmpty(repositoryAreaTf.getText())) {
                    repository.getBuildinginfo().getRepositorarea().getNum().setContent(repositoryAreaTf.getText());
                }

                if(StringUtils.isNotEmpty(lengthShelfTf.getText())) {
                    repository.getBuildinginfo().getLengthshelf().getNum().setContent(lengthShelfTf.getText());
                }

                if(StringUtils.isNotEmpty(archivalAndOthersTf.getTextValue())) {
                    repository.getHoldings().getDescriptiveNote().getP().get(0).setContent(archivalAndOthersTf.getTextValue());
                    repository.getHoldings().getDescriptiveNote().setLang(archivalAndOthersTf.getLanguage());
                    if(StringUtils.isNotEmpty(extentTf.getText())) {
                        repository.getHoldings().getExtent().getNum().setContent(extentTf.getText());
                    } else {
                        repository.getHoldings().setExtent(null);
                    }
                    if(StringUtils.isNotEmpty(dateHoldingsTf.getText())) {
                        repository.getHoldings().getDate().setContent(dateHoldingsTf.getText());
                    } else {
                        repository.getHoldings().setDate(null);
                    }
                    if(StringUtils.isNotEmpty(rangeFromTf.getText()) && StringUtils.isNotEmpty(rangeToTf.getText())) {
                        repository.getHoldings().getDateRange().getFromDate().setContent(rangeFromTf.getText());
                        repository.getHoldings().getDateRange().getToDate().setContent(rangeToTf.getText());
                    } else {
                        repository.getHoldings().setDateRange(null);
                    }
                }

            }

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}
