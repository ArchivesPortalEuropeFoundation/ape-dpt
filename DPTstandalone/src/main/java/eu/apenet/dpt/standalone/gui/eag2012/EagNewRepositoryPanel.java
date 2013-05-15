package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.utils.eag2012.Eag;

import javax.swing.*;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 14/05/2013
 *
 * @author Yoann Moranville
 */
public class EagNewRepositoryPanel extends EagPanels {
    public EagNewRepositoryPanel(Eag eag, JTabbedPane tabbedPane1, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, boolean isNew, ResourceBundle labels) {
        super(eag, tabbedPane1, mainTabbedPane, eag2012Frame, model, labels);
        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
        super.setInstitutionTabbedPane(tabbedPane);
    }

    @Override
    protected JComponent buildEditorPanel(List<String> errors) {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;p), 4dlu, 100dlu, 7dlu, right:p, 4dlu, 100dlu",
                EDITOR_ROW_SPEC);

        layout.setColumnGroups(new int[][] { { 1, 3, 5, 7 } });
        PanelBuilder builder = new PanelBuilder(layout);

        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        rowNb = 1;

        return builder.getPanel();
    }

    protected JComponent buildInstitutionTabbedPane(boolean isNew, String countrycode, String mainagencycode) {
        JScrollPane institutionPane = new JScrollPane(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, countrycode, mainagencycode).buildEditorPanel(null));
        institutionPane.getVerticalScrollBar().setUnitIncrement(20);
        tabbedPane.add(labels.getString("eag2012.yourInstitutionTab"), institutionPane);
        tabbedPane.setEnabledAt(0, true);
        tabbedPane.add(labels.getString("eag2012.identityTab"), null);
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.add(labels.getString("eag2012.contactTab"), null);
        tabbedPane.setEnabledAt(2, false);
        tabbedPane.add(labels.getString("eag2012.accessAndServicesTab"), null);
        tabbedPane.setEnabledAt(3, false);
        tabbedPane.add(labels.getString("eag2012.descriptionTab"), null);
        tabbedPane.setEnabledAt(4, false);
        tabbedPane.add(labels.getString("eag2012.controlTab"), null);
        tabbedPane.setEnabledAt(5, false);
        tabbedPane.add(labels.getString("eag2012.relationsTab"), null);
        tabbedPane.setEnabledAt(6, false);

        return tabbedPane;
    }
}
