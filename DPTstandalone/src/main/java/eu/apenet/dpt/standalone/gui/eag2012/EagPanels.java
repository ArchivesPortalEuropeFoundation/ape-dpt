package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.CommonsPropertiesPanels;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.util.LanguageIsoList;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * User: Yoann Moranville
 * Date: 03/10/2012
 *
 * @author Yoann Moranville
 */
public abstract class EagPanels extends CommonsPropertiesPanels{
    protected static final Logger LOG = Logger.getLogger(EagPanels.class);

    protected final String[] continents = {"Africa", "Antarctica", "Asia", "Australia", "Europe", "North America", "South America"};
    protected final String[] yesOrNoNotMandatory = {"---", "yes", "no"};
    protected final String[] yesOrNo = {"yes", "no"};
    protected final String[] photographAllowance = {"---", "depending on the material", "no", "yes", "yes (without flash)"};
    protected final String[] repositoryRoles = {"---", "Branch", "Head quarter", "Interim archive"};
    protected final String[] webPrefixes = {"http://", "https://", "ftp://"};

    protected JComboBox continentCombo = new JComboBox(continents);
    protected JComboBox accessiblePublicCombo = new JComboBox(yesOrNo);
    protected JComboBox facilitiesForDisabledCombo = new JComboBox(yesOrNo);
    protected JComboBox photographAllowanceCombo = new JComboBox(photographAllowance);

    protected Eag eag;
    protected JFrame eag2012Frame;
    protected int repositoryNb;
    protected boolean isDataValid;

    public EagPanels(Eag eag, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels, int repositoryNb) {
        this.eag2012Frame = eag2012Frame;
        this.eag = eag;
        this.tabbedPane = tabbedPane;
        this.mainTabbedPane = mainTabbedPane;
        this.model = model;
        this.labels = labels;
        this.repositoryNb = repositoryNb;
    }

    protected void closeFrame() {
        eag2012Frame.dispose();
        eag2012Frame.setVisible(false);
    }

    protected void setInstitutionTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public int getRepositoryNb() {
        return repositoryNb;
    }

    public void setRepositoryNb(int repositoryNb) {
        this.repositoryNb = repositoryNb;
    }

    protected class ExitBtnAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Eag2012Frame.inUse(false);
            closeFrame();
        }
    }
}
