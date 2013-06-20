package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.utils.eag2012.*;

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
    public EagNewRepositoryPanel(Eag eag, JTabbedPane tabbedPane1, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels, int repositoryNb) {
        super(eag, tabbedPane1, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
        super.setInstitutionTabbedPane(tabbedPane);
        super.setRepositoryNb(repositoryNb);
    }

    @Override
    protected JComponent buildEditorPanel(List<String> errors) {
        return null;
    }

    protected JComponent buildInstitutionTabbedPane(boolean isNew, String countrycode, String mainagencycode) {
        Eag2012Frame.firstTimeInTab = true;
        if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == repositoryNb) {
            eag.getArchguide().getDesc().getRepositories().getRepository().add(new Repository());
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);
            repository.setGeogarea(new Geogarea());
            repository.getLocation().add(new Location());
            repository.getLocation().get(0).setCountry(new Country());
            repository.getLocation().get(0).setMunicipalityPostalcode(new MunicipalityPostalcode());
            repository.getLocation().get(0).setStreet(new Street());
            repository.getTelephone().add(new Telephone());
            repository.getEmail().add(new Email());
            repository.getWebpage().add(new Webpage());
            repository.setTimetable(new Timetable());
            repository.getTimetable().getOpening().add(new Opening());
            repository.setAccess(new Access());
            repository.getAccess().setQuestion("yes");
            repository.getAccessibility().add(new Accessibility());
            repository.getAccessibility().get(0).setQuestion("yes");
        }
        JScrollPane scrollPane;
        if(repositoryNb == 0) {
            scrollPane = new JScrollPane(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, countrycode, mainagencycode, repositoryNb).buildEditorPanel(null));
            scrollPane.getVerticalScrollBar().setUnitIncrement(20);
            tabbedPane.add(labels.getString("eag2012.yourInstitutionTab"), scrollPane);
            tabbedPane.add(labels.getString("eag2012.identityTab"), null);
            tabbedPane.add(labels.getString("eag2012.contactTab"), null);
            tabbedPane.add(labels.getString("eag2012.accessAndServicesTab"), null);
            tabbedPane.add(labels.getString("eag2012.descriptionTab"), null);
            tabbedPane.add(labels.getString("eag2012.controlTab"), null);
            tabbedPane.add(labels.getString("eag2012.relationsTab"), null);
            tabbedPane.setEnabledAt(0, true);
            tabbedPane.setEnabledAt(1, false);
            tabbedPane.setEnabledAt(2, false);
            tabbedPane.setEnabledAt(3, false);
            tabbedPane.setEnabledAt(4, false);
            tabbedPane.setEnabledAt(5, false);
            tabbedPane.setEnabledAt(6, false);
        } else {
            scrollPane = new JScrollPane(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(null));
            scrollPane.getVerticalScrollBar().setUnitIncrement(20);
            tabbedPane.add(labels.getString("eag2012.yourInstitutionTab"), null);
            tabbedPane.add(labels.getString("eag2012.identityTab"), null);
            tabbedPane.add(labels.getString("eag2012.contactTab"), scrollPane);
            tabbedPane.add(labels.getString("eag2012.accessAndServicesTab"), null);
            tabbedPane.add(labels.getString("eag2012.descriptionTab"), null);
            tabbedPane.add(labels.getString("eag2012.controlTab"), null);
            tabbedPane.add(labels.getString("eag2012.relationsTab"), null);
            tabbedPane.setEnabledAt(0, false);
            tabbedPane.setEnabledAt(1, false);
            tabbedPane.setEnabledAt(2, true);
            tabbedPane.setEnabledAt(3, false);
            tabbedPane.setEnabledAt(4, false);
            tabbedPane.setEnabledAt(5, false);
            tabbedPane.setEnabledAt(6, false);
            Eag2012Frame.firstTimeInTab = true;
            tabbedPane.setSelectedIndex(2);
            Eag2012Frame.firstTimeInTab = false;
        }

        return tabbedPane;
    }
}
