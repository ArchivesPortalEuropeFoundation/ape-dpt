package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 28/11/2012
 *
 * @author Yoann Moranville
 */
public class EagControlPanel extends EagPanels {

    public EagControlPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame, ProfileListModel model) {
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

        JTextField temp;
        rowNb = 1;

        //todo here
        builder.addLabel(labels.getString("eag2012.descriptionIdentifier"), cc.xy(1, rowNb));
        builder.addLabel(eag.getControl().getRecordId().getValue(), cc.xy(3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.personInstitutionResponsible"), cc.xy(1, rowNb));
        int sizeEvents = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size();
        MaintenanceEvent event = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(sizeEvents - 1);
        temp = new JTextField(event.getAgent().getContent());
        temp.setEnabled(false);
        builder.add(temp, cc.xy(3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.identifierInstitution"), cc.xy(1, rowNb));
        temp = new JTextField(eag.getControl().getMaintenanceAgency().getAgencyCode().getContent());
        temp.setEnabled(false);
        builder.add(temp, cc.xy(3, rowNb));
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.usedLanguages"), cc.xyw(1, rowNb, 3));
        JButton addLanguagesBtn = new ButtonEag(labels.getString("eag2012.addLanguagesButton"));
//        addLanguagesBtn.setBackground(new Color(0, 162, 222));
        addLanguagesBtn.setBackground(new Color(97, 201, 237));
//        addLanguagesBtn.setOpaque(true);
//        addLanguagesBtn.setBorderPainted(false);
        builder.add(addLanguagesBtn, cc.xy(5, rowNb));
        setNextRow();


        if(eag.getControl().getLanguageDeclarations().getLanguageDeclaration().size() == 0)
            eag.getControl().getLanguageDeclarations().getLanguageDeclaration().add(new LanguageDeclaration());
        for(LanguageDeclaration languageDeclaration : eag.getControl().getLanguageDeclarations().getLanguageDeclaration()) {
            builder.addLabel(labels.getString("eag2012.language"),    cc.xy (1, rowNb));
            temp = new JTextField(languageDeclaration.getLanguage().getContent());
            builder.add(temp,                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.script"),    cc.xy (5, rowNb));
            temp = new JTextField(languageDeclaration.getScript().getContent());
            builder.add(temp,                     cc.xy (7, rowNb));
            setNextRow();
        }
         //97, 201, 237
        //0, 162, 222

        builder.addLabel(labels.getString("eag2012.identifierInstitution"), cc.xy(1, rowNb));
        temp = new JTextField(eag.getControl().getMaintenanceAgency().getAgencyCode().getContent());
        temp.setEnabled(false);
        builder.add(temp, cc.xy(3, rowNb));
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
                    reloadTabbedPanel(new EagRelationsPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 6);
                    tabbedPane.setEnabledAt(6, true);
                    tabbedPane.setEnabledAt(5, false);
                } else {
                    reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 4);
                    tabbedPane.setEnabledAt(4, true);
                    tabbedPane.setEnabledAt(5, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagControlPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 5);
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
