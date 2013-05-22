package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.FormerlyUsedName;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.TextFieldWithDate;
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

    final private String[] typeInstitution = {"---", "National archives", "Regional archives", "County/local authority archives",
            "Municipal archives", "Specialised governmental archives", "Private persons and family archives", "Church and religious archives",
            "Business archives", "University and research archives", "Media archives", "Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations",
            "Specialised non-governmental archives and archives of other cultural (heritage) institutions"};
    private JComboBox typeInstitutionCombo = new JComboBox(typeInstitution);

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
        builder.addLabel(labels.getString("eag2012.countryCodeLabel") + "*",    cc.xy (1, rowNb));
        builder.addLabel(eag.getArchguide().getIdentity().getRepositorid().getCountrycode(), cc.xy(3, rowNb));

        builder.addLabel(labels.getString("eag2012.idUsedInApeLabel"),      cc.xy (5, rowNb));
        builder.addLabel(eag.getControl().getRecordId().getValue(), cc.xy(7, rowNb));
        setNextRow();

        for(OtherRecordId otherRecordId : eag.getControl().getOtherRecordId()) {
            builder.addLabel(labels.getString("eag2012.identifierInstitutionLabel"),    cc.xy (1, rowNb));
            builder.addLabel(otherRecordId.getValue(), cc.xy(3, rowNb));
            setNextRow();
        }

        nameInstitutionTfs = new ArrayList<TextFieldWithLanguage>(eag.getArchguide().getIdentity().getAutform().size());
        int loop = 0;
        for(Autform autform : eag.getArchguide().getIdentity().getAutform()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(autform.getContent(), autform.getLang());
            nameInstitutionTfs.add(textFieldWithLanguage);
            if(loop++ == 0) {
                builder.addLabel(labels.getString("eag2012.nameOfInstitutionLabel") + "*",    cc.xy (1, rowNb));
                textFieldWithLanguage.getTextField().setEnabled(false);
            } else {
                builder.addLabel(labels.getString("eag2012.nameOfInstitutionLabel"),    cc.xy (1, rowNb));
            }

            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addNewNameInstitutionBtn = new ButtonEag(labels.getString("eag2012.addOtherNameInstitution"));
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
            builder.addLabel(labels.getString("eag2012.parallelNameOfInstitutionLabel"),    cc.xy (1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addNewParallelNameInstitutionBtn = new ButtonEag(labels.getString("eag2012.addOtherParallelNameInstitution"));
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
            builder.addLabel(labels.getString("eag2012.previousNameOfArchiveLabel"),    cc.xy (1, rowNb));
            builder.add(formerlyUsedName.getNameTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
            builder.add(formerlyUsedName.getLanguageBox(), cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.yearsOfUsedNameLabel"),    cc.xy (1, rowNb));
            setNextRow();

            for (TextFieldWithDate textFieldWithDate : datesForFormerlyUsedName) {
                if(!textFieldWithDate.isDateRange()) {
                    builder.addLabel(labels.getString("eag2012.yearLabel"),    cc.xy (1, rowNb));
                    builder.add(textFieldWithDate.getDateField(), cc.xy (3, rowNb));
                    setNextRow();
                } else {
                    builder.addLabel(labels.getString("eag2012.yearLabel") + " " + labels.getString("eag2012.fromLabel"),    cc.xy (1, rowNb));
                    builder.add(textFieldWithDate.getFromDateField(), cc.xy (3, rowNb));
                    builder.addLabel(labels.getString("eag2012.toLabel"),             cc.xy (5, rowNb));
                    builder.add(textFieldWithDate.getToDateField(),                                            cc.xy (7, rowNb));
                    setNextRow();
                }
            }
            if(!formerlyUsedNameTfs.isEmpty()){
                JButton addSingleYearBtn = new ButtonEag(labels.getString("eag2012.addYearButton"));
                addSingleYearBtn.setName("formerName_addSingleBtn_"+formerNameCounter);
                addSingleYearBtn.addActionListener(new AddSingleYearAction(eag, tabbedPane, model));
                builder.add(addSingleYearBtn, cc.xy(1, rowNb));
                JButton addYearRangeBtn = new ButtonEag(labels.getString("eag2012.addYearRangeButton"));
                addYearRangeBtn.setName("formerName_addYearRangeBtn_"+formerNameCounter);
                addYearRangeBtn.addActionListener(new AddYearRangeAction(eag, tabbedPane, model));
                builder.add(addYearRangeBtn, cc.xy(3, rowNb));
                setNextRow();
            }
        }
        JButton addNewNonpreNameInstitutionBtn = new ButtonEag(labels.getString("eag2012.addOtherNonpreNameInstitution"));
        addNewNonpreNameInstitutionBtn.addActionListener(new AddNonpreNameInstitutionAction(eag, tabbedPane, model));
        builder.add(addNewNonpreNameInstitutionBtn, cc.xy(1, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.selectTypeInstitutionLabel"),    cc.xy (1, rowNb));
        if(eag.getArchguide().getIdentity().getRepositoryType() != null && eag.getArchguide().getIdentity().getRepositoryType().size() > 0) {
            if(Arrays.asList(typeInstitution).contains(eag.getArchguide().getIdentity().getRepositoryType().get(0).getValue())){
                typeInstitutionCombo.setSelectedItem(eag.getArchguide().getIdentity().getRepositoryType().get(0).getValue());
            } else {
                typeInstitutionCombo.setSelectedItem("---");
            }
        }
        builder.add(typeInstitutionCombo, cc.xy (3, rowNb));
        setNextRow();

        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();

        JButton exitBtn = new ButtonEag(labels.getString("eag2012.exitButton"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton previousTabBtn = new ButtonEag(labels.getString("eag2012.previousTabButton"));
        builder.add(previousTabBtn, cc.xy (3, rowNb));
        previousTabBtn.addActionListener(new ChangeTabBtnAction(false));

        JButton nextTabBtn = new ButtonEag(labels.getString("eag2012.nextTabButton"));
        builder.add(nextTabBtn, cc.xy (5, rowNb));
        nextTabBtn.addActionListener(new ChangeTabBtnAction(true));

        setNextRow();
        JButton saveBtn = new ButtonEag(labels.getString("eag2012.saveButton"));
        builder.add(saveBtn, cc.xy (5, rowNb));
        saveBtn.addActionListener(new SaveBtnAction(eag, tabbedPane, model));

        setNextRow();
        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();
        JButton nextInstitutionTabBtn = new ButtonEag(labels.getString("eag2012.nextInstitutionBtn"));
        nextInstitutionTabBtn.addActionListener(new NextInstitutionTabBtnAction(eag, tabbedPane, model));
        builder.add(nextInstitutionTabBtn, cc.xy(5, rowNb));

        tabbedPane.addChangeListener(new TabChangeListener(eag, tabbedPane, model));

        return builder.getPanel();
    }

    public class NextInstitutionTabBtnAction extends UpdateEagObject {
        NextInstitutionTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(true);

                int currentTabIndex = mainTabbedPane.getSelectedIndex();
                if(currentTabIndex+1 < mainTabbedPane.getTabCount()) {
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
                super.updateEagObject(false);
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
                super.updateEagObject(true);
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
                super.updateEagObject(true);
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
                super.updateEagObject(false);
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
                super.updateEagObject(false);
            } catch (Eag2012FormException e) {

            }
            eag.getArchguide().getIdentity().getParform().add(new Parform());
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
                super.updateEagObject(false);
            } catch (Eag2012FormException e) {

            }
            Nonpreform nonpreform = new Nonpreform();
            nonpreform.getContent().add(new UseDates());
            eag.getArchguide().getIdentity().getNonpreform().add(nonpreform);
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
        }
    }

    public class ChangeTabBtnAction implements ActionListener {
        private boolean isNextTab;

        ChangeTabBtnAction(boolean isNextTab) {
            this.isNextTab = isNextTab;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if(isNextTab)
                tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
            else
                tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
        }
    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        public UpdateEagObject(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        protected void updateEagObject(boolean save) throws Eag2012FormException {
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

            if(!(typeInstitutionCombo.getSelectedItem()).equals("---")) {
                if(eag.getArchguide().getIdentity().getRepositoryType().size() == 0)
                    eag.getArchguide().getIdentity().getRepositoryType().add(new RepositoryType());
                eag.getArchguide().getIdentity().getRepositoryType().get(0).setValue(typeInstitutionCombo.getSelectedItem().toString());
            } else if(eag.getArchguide().getIdentity().getRepositoryType().size() != 0) {
                eag.getArchguide().getIdentity().getRepositoryType().remove(0);
            }

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }

    public class TabChangeListener extends UpdateEagObject implements ChangeListener {
        private boolean click;
        public TabChangeListener(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
            click = true;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {}

        public void stateChanged(ChangeEvent changeEvent) {
            LOG.info("stateChanged");
            if(click && !Eag2012Frame.firstTimeInTab) {
                tabbedPane.removeChangeListener(this);
                try {
                    super.updateEagObject(false);
                    LOG.info("Ok");
                    Eag2012Frame.firstTimeInTab = true;
                    EagPanels eagPanels = getCorrectEagPanels(tabbedPane.getSelectedIndex(), mainTabbedPane, eag2012Frame, labels, repositoryNb);
                    reloadTabbedPanel(eagPanels.buildEditorPanel(errors), tabbedPane.getSelectedIndex());
                } catch (Eag2012FormException e) {
                    LOG.info("NOT Ok");
                    EagPanels eagPanels = getCorrectEagPanels(1, mainTabbedPane, eag2012Frame, labels, repositoryNb);
                    reloadTabbedPanel(eagPanels.buildEditorPanel(errors), 1);
                }
                click = false;
            }
            Eag2012Frame.firstTimeInTab = false;
        }
    }
}
