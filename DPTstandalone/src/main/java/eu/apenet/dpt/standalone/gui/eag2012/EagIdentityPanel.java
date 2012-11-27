package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
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

    private List<JTextField> nameInstitutionTfs;
    private List<JTextField> parallelNameTfs;
    private List<JTextField> formerNameTfs;
    private JTextField dateTf;
    private JTextField fromTf;
    private JTextField toTf;

    final private String[] languages = {"---", "eng", "fre"};
    private JComboBox languageBoxNameInstitution = new JComboBox(languages);
    private JComboBox languageBoxParallelName = new JComboBox(languages);

    final private String[] typeInstitution = {"National archives", "Regional archives", "County/local authority archives",
    "Municipal archives", "Specialised governmental archives", "Private persons and family archives", "Church and religious archives",
    "Business archives", "University and research archives", "Media archives", "Archives of political parties, of popular/labour movement and other non-governmental organisations, associations, agencies and foundations",
    "Specialised non-governmental archives and archives of other cultural (heritage) institutions"};
    private JComboBox typeInstitutionCombo = new JComboBox(typeInstitution);

    public EagIdentityPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame) {
        super(eag, tabbedPane, eag2012Frame);
    }

    /**
     * Builds and answer the editor's tab for the given layout.
     * @return the editor's panel
     */
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
        builder.addLabel(labels.getString("eag2012.countryCodeLabel"),    cc.xy (1, rowNb));
        builder.addLabel(eag.getArchguide().getIdentity().getRepositorid().getCountrycode(), cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.identifierInstitutionLabel"),    cc.xy (1, rowNb));
        builder.addLabel(eag.getControl().getRecordId().getValue(), cc.xy(3, rowNb));
        setNextRow();

        for(OtherRecordId otherRecordId : eag.getControl().getOtherRecordId()) {
            builder.addLabel(labels.getString("eag2012.idUsedInApeLabel"),      cc.xy (5, rowNb));
            builder.addLabel(otherRecordId.getValue(), cc.xy(7, rowNb));
            setNextRow();
        }

        nameInstitutionTfs = new ArrayList<JTextField>(eag.getArchguide().getIdentity().getAutform().size());
        int loop = 0;
        for(Autform autform : eag.getArchguide().getIdentity().getAutform()) {
            JTextField nameInstitutionTf = new JTextField(autform.getContent());
            nameInstitutionTfs.add(nameInstitutionTf);
            if(loop++ == 0)
                nameInstitutionTf.setEnabled(false);
            builder.addLabel(labels.getString("eag2012.nameOfInstitutionLabel"),    cc.xy (1, rowNb));
            builder.add(nameInstitutionTf, cc.xy(3, rowNb));
            setNextRow();
        }
        JButton addNewNameInstitutionBtn = new JButton(labels.getString("eag2012.addOtherNameInstitution"));
        addNewNameInstitutionBtn.addActionListener(new AddNameInstitutionAction(eag, tabbedPane));
        builder.add(addNewNameInstitutionBtn, cc.xy(1, rowNb));
        setNextRow();

        parallelNameTfs = new ArrayList<JTextField>(eag.getArchguide().getIdentity().getParform().size());
        loop = 0;
        for(Parform parform : eag.getArchguide().getIdentity().getParform()) {
            JTextField parallelNameTf = new JTextField(parform.getContent());
            parallelNameTfs.add(parallelNameTf);
            if(loop++ == 0)
                parallelNameTf.setEnabled(false);
            builder.addLabel(labels.getString("eag2012.parallelNameOfInstitutionLabel"),    cc.xy (1, rowNb));
            builder.add(parallelNameTf, cc.xy(3, rowNb));
            setNextRow();
        }
        JButton addNewParallelNameInstitutionBtn = new JButton(labels.getString("eag2012.addOtherParallelNameInstitution"));
        addNewParallelNameInstitutionBtn.addActionListener(new AddParallelNameInstitutionAction(eag, tabbedPane));
        builder.add(addNewParallelNameInstitutionBtn, cc.xy(1, rowNb));
        setNextRow();
//        builder.addLabel(labels.getString("eag2012.languageLabel"),             cc.xy (5, rowNb));
//        builder.add(new JComboBox(),                                            cc.xy (7, rowNb));
//        setNextRow();

        formerNameTfs = new ArrayList<JTextField>(eag.getArchguide().getIdentity().getNonpreform().size());
//        fromTfs = new ArrayList<JTextField>(eag.getArchguide().getIdentity().getNonpreform().size());
//        toTfs = new ArrayList<JTextField>(eag.getArchguide().getIdentity().getNonpreform().size());
        for(Nonpreform nonpreform : eag.getArchguide().getIdentity().getNonpreform()) {
            for(int i = 0; i < nonpreform.getContent().size(); i++) {
                Object object = nonpreform.getContent().get(i);
                if(object instanceof String) {
                    JTextField formerNameTf = new JTextField((String)object);
                    formerNameTfs.add(formerNameTf);
                    builder.addLabel(labels.getString("eag2012.formerlyUsedNameLabel"),    cc.xy (1, rowNb));
                    builder.add(formerNameTf, cc.xy (3, rowNb));
                    setNextRow();
                } else if(object instanceof UseDates) {
                    if(nonpreform.getContent().size() == 1) { //Only a useDates obj but no title, we need to add title too
                        JTextField formerNameTf = new JTextField();
                        formerNameTfs.add(formerNameTf);
                        builder.addLabel(labels.getString("eag2012.formerlyUsedNameLabel"),    cc.xy (1, rowNb));
                        builder.add(formerNameTf, cc.xy (3, rowNb));
                        setNextRow();
                    }

                    UseDates useDates = ((UseDates)object);
                    if(useDates.getDate() != null) {
                        builder.addLabel(labels.getString("eag2012.datesOfUsedNameLabel"),    cc.xy (1, rowNb));
                        setNextRow();
                        dateTf = new JTextField(useDates.getDate().getContent());
                        builder.addLabel(labels.getString("eag2012.dateLabel"),    cc.xy (1, rowNb));
                        builder.add(dateTf, cc.xy (3, rowNb));
                    } else if(useDates.getDateRange() != null) {
                        builder.addLabel(labels.getString("eag2012.datesOfUsedNameLabel"),    cc.xy (1, rowNb));
                        setNextRow();
                        fromTf = new JTextField(useDates.getDateRange().getFromDate().getContent());
                        toTf = new JTextField(useDates.getDateRange().getToDate().getContent());
                        builder.addLabel(labels.getString("eag2012.fromLabel"),    cc.xy (1, rowNb));
                        builder.add(fromTf, cc.xy (3, rowNb));
                        builder.addLabel(labels.getString("eag2012.toLabel"),             cc.xy (5, rowNb));
                        builder.add(toTf,                                            cc.xy (7, rowNb));
                        setNextRow();
                    } else if(useDates.getDateSet() != null) {
                        builder.addLabel(labels.getString("eag2012.datesOfUsedNameLabel"),    cc.xy (1, rowNb));
                        setNextRow();
                    }
                }
            }
        }
        JButton addNewNonpreNameInstitutionBtn = new JButton(labels.getString("eag2012.addOtherNonpreNameInstitution"));
        addNewNonpreNameInstitutionBtn.addActionListener(new AddNonpreNameInstitutionAction(eag, tabbedPane));
        builder.add(addNewNonpreNameInstitutionBtn, cc.xy(1, rowNb));
        setNextRow();
//        builder.addLabel(labels.getString("eag2012.languageLabel"),             cc.xy (5, rowNb));
//        builder.add(new JComboBox(),                                            cc.xy (7, rowNb));
//        setNextRow();

        builder.addLabel(labels.getString("eag2012.selectTypeInstitutionLabel"),    cc.xy (1, rowNb));
        if(eag.getArchguide().getIdentity().getRepositoryType() != null && eag.getArchguide().getIdentity().getRepositoryType().size() > 0) {
            if(Arrays.asList(typeInstitution).contains(eag.getArchguide().getIdentity().getRepositoryType().get(0).getValue())){
                typeInstitutionCombo.setSelectedItem(eag.getArchguide().getIdentity().getRepositoryType().get(0).getValue());
            }
        }
        builder.add(typeInstitutionCombo, cc.xy (3, rowNb));
        setNextRow();

        JButton exitBtn = new JButton(labels.getString("eag2012.exitButton"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton nextTabBtn = new JButton(labels.getString("eag2012.nextTabButton"));
        builder.add(nextTabBtn, cc.xy (3, rowNb));
        nextTabBtn.addActionListener(new NextTabBtnAction(eag, tabbedPane));

        return builder.getPanel();
    }

    public class AddNameInstitutionAction extends UpdateEagObject {
        public AddNameInstitutionAction(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }
            eag.getArchguide().getIdentity().getAutform().add(new Autform());
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame).buildEditorPanel(errors), 1);
        }
    }
    public class AddParallelNameInstitutionAction extends UpdateEagObject {
        public AddParallelNameInstitutionAction(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }
            eag.getArchguide().getIdentity().getParform().add(new Parform());
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame).buildEditorPanel(errors), 1);
        }
    }
    public class AddNonpreNameInstitutionAction extends UpdateEagObject {
        public AddNonpreNameInstitutionAction(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
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
            reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame).buildEditorPanel(errors), 1);
        }
    }

    public class NextTabBtnAction extends UpdateEagObject {
        NextTabBtnAction(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();

                reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame).buildEditorPanel(errors), 2);

                tabbedPane.setEnabledAt(2, true);
                tabbedPane.setEnabledAt(1, false);
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame).buildEditorPanel(errors), 1);
            }
        }
    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        public UpdateEagObject(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }

        @Override
        protected void updateEagObject() throws Eag2012FormException {
            errors = new ArrayList<String>();

            boolean hasChanged = false;

            if(nameInstitutionTfs.size() > 0) {
                eag.getArchguide().getIdentity().getAutform().clear();
                for(JTextField field : nameInstitutionTfs) {
                    if(StringUtils.isNotEmpty(field.getText())) {
                        Autform autform = new Autform();
                        autform.setContent(field.getText());
                        eag.getArchguide().getIdentity().getAutform().add(autform);
                        hasChanged = true;
                    }
                }
            }

            if(parallelNameTfs.size() > 0) {
                eag.getArchguide().getIdentity().getParform().clear();
                for(JTextField field : parallelNameTfs) {
                    if(StringUtils.isNotEmpty(field.getText())) {
                        Parform parform = new Parform();
                        parform.setContent(field.getText());
                        eag.getArchguide().getIdentity().getParform().add(parform);
                        hasChanged = true;
                    }
                }
            }

            if(formerNameTfs.size() > 0) {
                eag.getArchguide().getIdentity().getNonpreform().clear();
                for(JTextField field : formerNameTfs) {
                    if(StringUtils.isNotEmpty(field.getText())) {
                        Nonpreform nonpreform = new Nonpreform();
                        for(Object object : nonpreform.getContent()) {
                            if(object instanceof String) {
                                nonpreform.getContent().remove(object);
                                nonpreform.getContent().add(field);
                            }
                        }

                        nonpreform.getContent().add(field.getText());
                        eag.getArchguide().getIdentity().getNonpreform().add(nonpreform); //todo for dates too?
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
