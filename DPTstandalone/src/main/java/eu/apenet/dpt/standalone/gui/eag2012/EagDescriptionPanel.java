package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 28/11/2012
 *
 * @author Yoann Moranville
 */
public class EagDescriptionPanel extends EagPanels {

    private JTextField repositoryHistoryTf;
    private JTextField dateRepositoryFoundationTf;
    private JTextField ruleRepositoryFoundationTf;
    private JTextField dateRepositorySuppressionTf;
    private JTextField ruleRepositorySuppressionTf;
    private List<JTextField> unitAdministrativeStructureTfs;
    private JTextField buildingTf;
    private JTextField repositoryAreaTf;
    private JTextField lengthShelfTf;
    private JTextField archivalAndOthersTf;
    private JTextField dateHoldingsTf;
    private JTextField rangeFromTf;
    private JTextField rangeToTf;
    private JTextField extentTf;

    public EagDescriptionPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame, ProfileListModel model) {
        super(eag, tabbedPane, eag2012Frame, model);
    }

    @Override
    protected JComponent buildEditorPanel(List<String> errors) {
        if(errors == null)
            errors = new ArrayList<String>(0);

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
        repositoryHistoryTf = new JTextField(repository.getRepositorhist().getDescriptiveNote().getP().get(0).getContent());
        builder.add(repositoryHistoryTf, cc.xy(3, rowNb));
        setNextRow();

        if(repository.getRepositorfound() == null) {
            Repositorfound repositorfound = new Repositorfound();
            repositorfound.setDate(new Date());
            repositorfound.setRule(new Rule());
            repository.setRepositorfound(repositorfound);
        }
        builder.addLabel(labels.getString("eag2012.dateRepositoryFoundation"), cc.xy(1, rowNb));
        dateRepositoryFoundationTf = new JTextField(repository.getRepositorfound().getDate().getContent());
        builder.add(dateRepositoryFoundationTf, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.ruleRepositoryFoundation"), cc.xy(1, rowNb));
        ruleRepositoryFoundationTf = new JTextField(repository.getRepositorfound().getRule().getContent());
        builder.add(ruleRepositoryFoundationTf, cc.xy(3, rowNb));
        setNextRow();

        if(repository.getRepositorsup() == null) {
            Repositorsup repositorsup = new Repositorsup();
            repositorsup.setDate(new Date());
            repositorsup.setRule(new Rule());
            repository.setRepositorsup(repositorsup);
        }
        builder.addLabel(labels.getString("eag2012.dateRepositorySuppression"), cc.xy(1, rowNb));
        dateRepositorySuppressionTf = new JTextField(repository.getRepositorsup().getDate().getContent());
        builder.add(dateRepositorySuppressionTf, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.ruleRepositorySuppression"), cc.xy(1, rowNb));
        ruleRepositorySuppressionTf = new JTextField(repository.getRepositorsup().getRule().getContent());
        builder.add(ruleRepositorySuppressionTf, cc.xy(3, rowNb));
        setNextRow();


        builder.addSeparator(labels.getString("eag2012.administrativeStructure"), cc.xy(1, rowNb));
        setNextRow();


        if(repository.getAdminhierarchy().getAdminunit().size() == 0)
            repository.getAdminhierarchy().getAdminunit().add(new Adminunit());
        unitAdministrativeStructureTfs = new ArrayList<JTextField>(repository.getAdminhierarchy().getAdminunit().size());
        for(Adminunit adminunit : repository.getAdminhierarchy().getAdminunit()) {
            builder.addLabel(labels.getString("eag2012.unitAdministrativeStructure"),    cc.xy (1, rowNb));
            JTextField unitAdministrativeStructureTf = new JTextField(adminunit.getContent());
            unitAdministrativeStructureTfs.add(unitAdministrativeStructureTf);
            builder.add(unitAdministrativeStructureTf,                     cc.xy (3, rowNb));
            setNextRow();
        }
        JButton addUnitAdministrativeStructureBtn = new ButtonEag(labels.getString("eag2012.addUnitAdministrativeStructureButton"));
        builder.add(addUnitAdministrativeStructureBtn, cc.xy (1, rowNb));
//        addUnitAdministrativeStructureBtn.addActionListener(new AddUnitAdministrativeStructureBtnAction());
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.buildingDescription"), cc.xy(1, rowNb));
        setNextRow();

        if(repository.getBuildinginfo().getBuilding() == null) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            Building building = new Building();
            building.setDescriptiveNote(descriptiveNote);
            repository.getBuildinginfo().setBuilding(building);
        }
        builder.addLabel(labels.getString("eag2012.building"), cc.xy(1, rowNb));
        buildingTf = new JTextField(repository.getBuildinginfo().getBuilding().getDescriptiveNote().getP().get(0).getContent());
        builder.add(buildingTf, cc.xy(3, rowNb));
        setNextRow();

        if(repository.getBuildinginfo().getRepositorarea() == null) {
            Repositorarea repositorarea = new Repositorarea();
            repositorarea.setNum(new Num());
            repository.getBuildinginfo().setRepositorarea(repositorarea);
        }
        builder.addLabel(labels.getString("eag2012.repositoryArea"), cc.xy(1, rowNb));
        repositoryAreaTf = new JTextField(repository.getBuildinginfo().getRepositorarea().getNum().getContent());
        builder.add(repositoryAreaTf, cc.xy(3, rowNb));
        setNextRow();

        if(repository.getBuildinginfo().getLengthshelf() == null) {
            Lengthshelf lengthshelf = new Lengthshelf();
            lengthshelf.setNum(new Num());
            repository.getBuildinginfo().setLengthshelf(lengthshelf);
        }
        builder.addLabel(labels.getString("eag2012.lengthShelf"), cc.xy(1, rowNb));
        lengthShelfTf = new JTextField(repository.getBuildinginfo().getLengthshelf().getNum().getContent());
        builder.add(lengthShelfTf, cc.xy(3, rowNb));
        setNextRow();


        builder.addSeparator(labels.getString("eag2012.holdingDescription"));
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
            extent.setNum(new Num());
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
        archivalAndOthersTf = new JTextField(repository.getHoldings().getDescriptiveNote().getP().get(0).getContent());
        builder.add(archivalAndOthersTf, cc.xy(3, rowNb));
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

        return builder.getPanel();
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
                    reloadTabbedPanel(new EagControlPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 5);
                    tabbedPane.setEnabledAt(5, true);
                    tabbedPane.setEnabledAt(4, false);
                } else {
                    reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 3);
                    tabbedPane.setEnabledAt(3, true);
                    tabbedPane.setEnabledAt(4, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 4);
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

            //todo here

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}
