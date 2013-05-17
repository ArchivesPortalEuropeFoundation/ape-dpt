package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.ReadXml;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 23/08/2012
 *
 * @author Yoann Moranville
 */
public class Eag2012Frame extends JFrame {
    private static final Logger LOG = Logger.getLogger(Eag2012Frame.class);
    private JTabbedPane mainTabbedPane;
    private Dimension dimension;
    private ProfileListModel model;
    private ResourceBundle labels;
    private static boolean used;
    private static Date timeMaintenance;
    private String countrycode;
    private String mainagencycode;
    private int currentTabIndex = 0;
    private int previousTabIndex = 0;
    private boolean clicked = true;

    private static EagPanels eagInstitutionPanel;

    public Eag2012Frame(File eagFile, boolean isEag2012, Dimension dimension, ProfileListModel model, ResourceBundle labels) throws Exception {
        String namespace = ReadXml.getXmlNamespace(eagFile);
        if(isEag2012 && !namespace.equals(EagNamespaceMapper.EAG_URI)) {
            throw new Exception("eag2012.error.fileNotInEag2012Namespace");
        } else if(!isEag2012 && namespace.equals(EagNamespaceMapper.EAG_URI)) {
            throw new Exception("eag2012.error.fileInEag2012Namespace");
        }
        if(!isEag2012) {
            try {
                InputStream is = FileUtils.openInputStream(eagFile);
                File tempEagFile = new File(Utilities.TEMP_DIR + "tmp_" + eagFile.getName());
                File xslFile = new File(Utilities.SYSTEM_DIR + "eag2eag2012.xsl");
                TransformationTool.createTransformation(is, tempEagFile, xslFile, null, true, true, "", true, null);
                eagFile = tempEagFile;
            } catch (Exception e) {
                LOG.error("Something went wrong...", e);
            }
        }
        this.dimension = dimension;
        this.model = model;
        this.labels = labels;
        createFrame(eagFile, false);
    }

    public Eag2012Frame(Dimension dimension, ProfileListModel model, ResourceBundle labels, String countrycode, String mainagencycode) {
//        URL emptyEAGFileURL = getClass().getResource("/EAG_XML_XSL/Blank_EAG_2012.xml");
        InputStream emptyEAGFileStream = getClass().getResourceAsStream("/EAG_XML_XSL/Blank_EAG_2012.xml");

//        File emptyEAGFile = new File(emptyEAGFileURL.getPath());

        this.dimension = dimension;
        this.model = model;
        this.labels = labels;
        this.countrycode = countrycode;
        this.mainagencycode = mainagencycode;
        createFrame(emptyEAGFileStream, true);
    }

    public void createFrame(File eagFile, boolean isNew) {
        try {
            createFrame(FileUtils.openInputStream(eagFile), isNew);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createFrame(InputStream eagStream, boolean isNew) {
        timeMaintenance = null;
        inUse(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                inUse(false);
            }
        });
        Eag eag = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            eag = (Eag) jaxbUnmarshaller.unmarshal(eagStream);
            eagStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        buildPanel(eag, isNew);
        this.getContentPane().add(mainTabbedPane);

        Dimension frameDim = new Dimension(((Double)(dimension.getWidth() * 0.95)).intValue(), ((Double)(dimension.getHeight() * 0.95)).intValue());
        this.setSize(frameDim);
        this.setPreferredSize(frameDim);
        this.pack();
        this.setVisible(true);
    }

    public void buildPanel(Eag eag, boolean isNew) {
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
        mainTabbedPane.add("eag2012.mainInstitution", new EagNewRepositoryPanel(eag, null, mainTabbedPane, this, model, labels, 0).buildInstitutionTabbedPane(isNew, countrycode, mainagencycode));
        mainTabbedPane.addMouseListener(new TabClickerListener());
        mainTabbedPane.addChangeListener(new TabChangeListener());
    }




    public static void inUse(boolean used) {
        Eag2012Frame.used = used;
    }

    public static boolean isUsed() {
        return used;
    }

    public static Date getTimeMaintenance() {
        return timeMaintenance;
    }

    public static void setTimeMaintenance(Date timeMaintenance) {
        Eag2012Frame.timeMaintenance = timeMaintenance;
    }

    public static EagPanels getEagInstitutionPanel() {
        return eagInstitutionPanel;
    }

    public static void setEagInstitutionPanel(EagPanels eagInstitutionPanel) {
        Eag2012Frame.eagInstitutionPanel = eagInstitutionPanel;
    }

    public class TabChangeListener implements ChangeListener {
        public void stateChanged(ChangeEvent changeEvent) {
            previousTabIndex = currentTabIndex;
            currentTabIndex = mainTabbedPane.getSelectedIndex();
            if(clicked) {
                JTabbedPane tabbedPane = (JTabbedPane)mainTabbedPane.getComponentAt(previousTabIndex);
                LOG.info(tabbedPane.getSelectedIndex()); //small tab system number

                EagPanels eagPanels = null;
                if(tabbedPane.getSelectedIndex() == 0) {
                    eagPanels = Eag2012Frame.getEagInstitutionPanel();
                }
                eagPanels.getHiddenSaveButton().doClick();
                if(!eagPanels.isDataValid()) {
                    clicked = false;
                    mainTabbedPane.setSelectedIndex(previousTabIndex);
                }
            }
        }
    }
    public class TabClickerListener implements MouseListener {

        public void mouseClicked(MouseEvent mouseEvent) {
            LOG.info("cli");
            clicked = true;
        }

        public void mousePressed(MouseEvent mouseEvent) {
        }

        public void mouseReleased(MouseEvent mouseEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void mouseEntered(MouseEvent mouseEvent) {
        }

        public void mouseExited(MouseEvent mouseEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
