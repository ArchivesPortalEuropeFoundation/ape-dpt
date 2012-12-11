package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 28/11/2012
 *
 * @author Yoann Moranville
 */
public class EagRelationsPanel extends EagPanels {
    private List<ResourceRelationType> resourceRelationTypes;

    public EagRelationsPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame, ProfileListModel model) {
        super(eag, tabbedPane, eag2012Frame, model);
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
        Relations relations = eag.getRelations();

        builder.addSeparator(labels.getString("eag2012.resourceRelations"), cc.xyw(1, rowNb, 7));
        setNextRow();

        resourceRelationTypes = new ArrayList<ResourceRelationType>(relations.getResourceRelation().size());
        for(ResourceRelation resourceRelation : relations.getResourceRelation()) {
            if(resourceRelation.getRelationEntry() == null)
                resourceRelation.setRelationEntry(new RelationEntry());
            if(resourceRelation.getDescriptiveNote() == null) {
                DescriptiveNote descriptiveNote = new DescriptiveNote();
                descriptiveNote.setP(new ArrayList<P>(){{add(new P());}});
                resourceRelation.setDescriptiveNote(descriptiveNote);
            }
            ResourceRelationType resourceRelationType = new ResourceRelationType(resourceRelation.getResourceRelationType(), resourceRelation.getHref(), resourceRelation.getRelationEntry().getContent(), resourceRelation.getDescriptiveNote().getP().get(0).getContent(), resourceRelation.getDescriptiveNote().getLang(), true);
            resourceRelationTypes.add(resourceRelationType);

            builder.addLabel(labels.getString("eag2012.websiteForResources"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getWebsite(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.typeRelation"), cc.xy(5, rowNb));
            builder.add(resourceRelationType.getTypeRelations(), cc.xy(7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.titleIdRelatedMaterial"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getTitleAndId(), cc.xy(3, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.descriptionOfRelation"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getDescription().getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
            builder.add(resourceRelationType.getDescription().getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addResourceRelation = new ButtonEag(labels.getString("eag2012.addResourceRelationBtn"));
        builder.add(addResourceRelation, cc.xy (1, rowNb));
//        addResourceRelation.addActionListener();
        setNextRow();

        for(EagRelation eagRelation : relations.getEagRelation()) {
            ResourceRelationType resourceRelationType = new ResourceRelationType(eagRelation.getEagRelationType(), eagRelation.getHref(), eagRelation.getRelationEntry().get(0).getContent(), eagRelation.getDescriptiveNote().getP().get(0).getContent(), eagRelation.getDescriptiveNote().getLang(), false);
            resourceRelationTypes.add(resourceRelationType);

            builder.addLabel(labels.getString("eag2012.websiteForResources"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getWebsite(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.typeRelation"), cc.xy(5, rowNb));
            builder.add(resourceRelationType.getTypeRelations(), cc.xy(7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.titleIdRelatedInstitution"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getTitleAndId(), cc.xy(3, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.descriptionOfRelation"), cc.xy(1, rowNb));
            builder.add(resourceRelationType.getDescription().getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
            builder.add(resourceRelationType.getDescription().getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addInstitutionRelation = new ButtonEag(labels.getString("eag2012.addInstitutionRelationBtn"));
        builder.add(addInstitutionRelation, cc.xy (1, rowNb));
//        addInstitutionRelation.addActionListener();
        setNextRow();


        JButton exitBtn = new ButtonEag(labels.getString("eag2012.exitButton"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton previousTabBtn = new ButtonEag(labels.getString("eag2012.previousTabButton"));
        builder.add(previousTabBtn, cc.xy (3, rowNb));
        previousTabBtn.addActionListener(new PreviousTabBtnAction(eag, tabbedPane, model));

        JButton saveBtn = new ButtonEag(labels.getString("eag2012.saveButton"));
        builder.add(saveBtn, cc.xy (5, rowNb));
        saveBtn.addActionListener(new SaveBtnAction(eag, tabbedPane, model));

        return builder.getPanel();
    }

    public class PreviousTabBtnAction extends UpdateEagObject {
        PreviousTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();

                reloadTabbedPanel(new EagControlPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 5);
                tabbedPane.setEnabledAt(5, true);
                tabbedPane.setEnabledAt(6, false);
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagRelationsPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 6);
            }
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
                reloadTabbedPanel(new EagRelationsPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 6);
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
