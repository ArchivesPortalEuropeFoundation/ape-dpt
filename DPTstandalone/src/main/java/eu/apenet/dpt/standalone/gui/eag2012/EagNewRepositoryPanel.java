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
        }

        JScrollPane institutionPane = new JScrollPane(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, countrycode, mainagencycode, repositoryNb).buildEditorPanel(null));
        institutionPane.getVerticalScrollBar().setUnitIncrement(20);
        tabbedPane.add(labels.getString("eag2012.yourInstitutionTab"), institutionPane);
        tabbedPane.setEnabledAt(0, true);
//        tabbedPane.setTitleAt(); //todo use for renaming the repository tabs with their real names
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
