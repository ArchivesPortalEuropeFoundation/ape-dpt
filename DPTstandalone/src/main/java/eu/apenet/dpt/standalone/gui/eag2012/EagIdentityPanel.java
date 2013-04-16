package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import static eu.apenet.dpt.standalone.gui.eag2012.EagPanels.LOG;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import eu.apenet.dpt.standalone.gui.eag2012.data.Date;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
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
    private List<TextFieldWithDate> formerNameTfs;
    private JTextField dateTf;
    private JTextField fromTf;
    private JTextField toTf;

    final private String[] typeInstitution = {"National archives", "Regional archives", "County/local authority archives",
    "Municipal archives", "Specialised governmental archives", "Private persons and family archives", "Church and religious archives",
    "Business archives", "University and research archives", "Media archives", "Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations",
    "Specialised non-governmental archives and archives of other cultural (heritage) institutions"};
    private JComboBox typeInstitutionCombo = new JComboBox(typeInstitution);

    public EagIdentityPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels) {
        super(eag, tabbedPane, eag2012Frame, model, labels);
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

        formerNameTfs = new ArrayList<TextFieldWithDate>(eag.getArchguide().getIdentity().getNonpreform().size());
//        fromTfs = new ArrayList<JTextField>(eag.getArchguide().getIdentity().getNonpreform().size());
//        toTfs = new ArrayList<JTextField>(eag.getArchguide().getIdentity().getNonpreform().size());
        for(Nonpreform nonpreform : eag.getArchguide().getIdentity().getNonpreform()) {
            String nameStr = "";
            String dateStr = "";
            String fromDateStr = "";
            String toDateStr = "";
            for(int i = 0; i < nonpreform.getContent().size(); i++) {
                Object object = nonpreform.getContent().get(i);
                if(object instanceof String) {
                    nameStr = (String)object;
                } else if (object instanceof UseDates) {
                    UseDates useDates = (UseDates)object;
                    if(useDates.getDate() != null) {
                        dateStr = useDates.getDate().getContent();
                    } else if(useDates.getDateRange() != null) {
                        fromDateStr = useDates.getDateRange().getFromDate().getContent();
                        toDateStr = useDates.getDateRange().getToDate().getContent();
                    }
//                    else if(useDates.getDateSet() != null) {
//                    }
                }
            }
            
            TextFieldWithDate textFieldWithDate = new TextFieldWithDate(nameStr, nonpreform.getLang(), fromDateStr, toDateStr, dateStr);
            formerNameTfs.add(textFieldWithDate);
            builder.addLabel(labels.getString("eag2012.previousNameOfArchiveLabel"),    cc.xy (1, rowNb));
            builder.add(textFieldWithDate.getTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithDate.getLanguageBox(), cc.xy (7, rowNb));
            setNextRow();
            
            builder.addLabel(labels.getString("eag2012.yearsOfUsedNameLabel"),    cc.xy (1, rowNb));
            setNextRow();
            if(StringUtils.isNotBlank(dateStr)) {
                builder.addLabel(labels.getString("eag2012.yearLabel"),    cc.xy (1, rowNb));
                builder.add(textFieldWithDate.getDateField(), cc.xy (3, rowNb));
                setNextRow();
            }
            if(StringUtils.isNotBlank(fromDateStr) && StringUtils.isNotBlank(toDateStr)) {
                builder.addLabel(labels.getString("eag2012.yearsOfUsedNameLabel"),    cc.xy (1, rowNb));
                setNextRow();
                builder.addLabel(labels.getString("eag2012.yearLabel") + " " + labels.getString("eag2012.fromLabel"),    cc.xy (1, rowNb));
                builder.add(textFieldWithDate.getFromDateField(), cc.xy (3, rowNb));
                builder.addLabel(labels.getString("eag2012.toLabel"),             cc.xy (5, rowNb));
                builder.add(textFieldWithDate.getToDateField(),                                            cc.xy (7, rowNb));
                setNextRow();
            }
            if(StringUtils.isBlank(dateStr) && StringUtils.isBlank(fromDateStr) && StringUtils.isBlank(toDateStr)) {
                builder.addLabel(labels.getString("eag2012.yearLabel"),    cc.xy (1, rowNb));
                builder.add(textFieldWithDate.getDateField(), cc.xy (3, rowNb));
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
            }
        }
        builder.add(typeInstitutionCombo, cc.xy (3, rowNb));
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
                reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 0);
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
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }
            eag.getArchguide().getIdentity().getAutform().add(new Autform());
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 1);
        }
    }
    public class AddParallelNameInstitutionAction extends UpdateEagObject {
        public AddParallelNameInstitutionAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }
            eag.getArchguide().getIdentity().getParform().add(new Parform());
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 1);
        }
    }
    public class AddNonpreNameInstitutionAction extends UpdateEagObject {
        public AddNonpreNameInstitutionAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }
            Nonpreform nonpreform = new Nonpreform();
            nonpreform.getContent().add(new UseDates());
            eag.getArchguide().getIdentity().getNonpreform().add(nonpreform);
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 1);
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
                    reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 2);
                    tabbedPane.setEnabledAt(2, true);
                    tabbedPane.setEnabledAt(1, false);
                } else {
                    reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, eag2012Frame, model, false, labels).buildEditorPanel(errors), 0);
                    tabbedPane.setEnabledAt(0, true);
                    tabbedPane.setEnabledAt(1, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 1);
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

            if(formerNameTfs.size() > 0) {
                eag.getArchguide().getIdentity().getNonpreform().clear();
                for(TextFieldWithDate textFieldWithDate : formerNameTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithDate.getTextValue())) {
                        Nonpreform nonpreform = new Nonpreform();
                        nonpreform.setLang(textFieldWithDate.getLanguage());
                        nonpreform.getContent().add(textFieldWithDate.getTextValue());
                        if(StringUtils.isNotEmpty(textFieldWithDate.getDate())) {
                            UseDates useDates = new UseDates();
                            Date date = new Date();
                            date.setContent(textFieldWithDate.getDate());
                            useDates.setDate(date);
                            nonpreform.getContent().add(useDates);
                        }
                        if(StringUtils.isNotEmpty(textFieldWithDate.getFromDate()) && StringUtils.isNotEmpty(textFieldWithDate.getToDate())) {
                            UseDates useDates = new UseDates();
                            DateRange dateRange = new DateRange();
                            FromDate fromDate = new FromDate();
                            fromDate.setContent(textFieldWithDate.getFromDate());
                            ToDate toDate = new ToDate();
                            toDate.setContent(textFieldWithDate.getToDate());
                            dateRange.setFromDate(fromDate);
                            useDates.setDateRange(dateRange);
                            nonpreform.getContent().add(useDates);
                        }
                        eag.getArchguide().getIdentity().getNonpreform().add(nonpreform);
                        hasChanged = true;
                    }
                }
            }

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}
