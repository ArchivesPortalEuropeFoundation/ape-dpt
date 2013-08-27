package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.namespace.EagNamespaceMapper;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.ReadXml;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
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
    private static String personResponsible;
    private String countrycode;
    private String mainagencycode;
    public static boolean firstTimeInTab = true;

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
        InputStream emptyEAGFileStream = DocumentValidation.class.getResourceAsStream("/eag2012/Blank_EAG_2012.xml");

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
        personResponsible = null;
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
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void buildPanel(Eag eag, boolean isNew) {
        mainTabbedPane = new JTabbedPane();
        mainTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainTabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
        mainTabbedPane.add(labels.getString("eag2012.yourinstitution.institution"), new EagNewRepositoryPanel(eag, null, mainTabbedPane, this, model, labels, 0).buildInstitutionTabbedPane(isNew, countrycode, mainagencycode));
        for(int i = 1; i < eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(i).getRepositoryName().size() >0 && StringUtils.isNotEmpty(eag.getArchguide().getDesc().getRepositories().getRepository().get(i).getRepositoryName().get(0).getContent())) {
                String name = eag.getArchguide().getDesc().getRepositories().getRepository().get(i).getRepositoryName().get(0).getContent();
                mainTabbedPane.add(name, new EagNewRepositoryPanel(eag, null, mainTabbedPane, this, model, labels, i).buildInstitutionTabbedPane(isNew, countrycode, mainagencycode));
            } else {
                mainTabbedPane.add(labels.getString("eag2012.tab.extraRepository") + " " + i, new EagNewRepositoryPanel(eag, null, mainTabbedPane, this, model, labels, i).buildInstitutionTabbedPane(isNew, countrycode, mainagencycode));
            }
            mainTabbedPane.setEnabledAt(i, false);
        }
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

    public static String getPersonResponsible() {
        return personResponsible;
    }

    public static void setPersonResponsible(String personResponsible) {
        Eag2012Frame.personResponsible = personResponsible;
    }
}
