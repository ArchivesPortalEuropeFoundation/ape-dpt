package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.eag2012.data.Eag;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.ReadXml;
import org.apache.commons.io.FileUtils;
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
    private JTabbedPane tabbedPane;
    private Dimension dimension;
    private ProfileListModel model;
    private ResourceBundle labels;
    private static boolean used;
    private static boolean startOfForm;
    private static Date timeMaintenance;
    private String countrycode;
    private String mainagencycode;

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
        this.getContentPane().add(tabbedPane);

        Dimension frameDim = new Dimension(((Double)(dimension.getWidth() * 0.95)).intValue(), ((Double)(dimension.getHeight() * 0.95)).intValue());
        this.setSize(frameDim);
        this.setPreferredSize(frameDim);
        this.pack();
        this.setVisible(true);
    }

    public void buildPanel(Eag eag, boolean isNew) {
        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add(labels.getString("eag2012.yourInstitutionTab"),  buildInstitutionPanel(eag, isNew));

        tabbedPane.add(labels.getString("eag2012.identityTab"),  null);
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
    }


    protected JComponent buildInstitutionPanel(Eag eag, boolean isNew) {
        Eag2012Frame.setStartOfForm(true);
        JScrollPane jScrollPane = new JScrollPane(new EagInstitutionPanel(eag, tabbedPane, this, model, isNew, labels, countrycode, mainagencycode).buildEditorPanel(null));
        jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        return jScrollPane;
    }

    public static void inUse(boolean used) {
        Eag2012Frame.used = used;
    }

    public static boolean isUsed() {
        return used;
    }

    public static void setStartOfForm(boolean startOfForm) {
        Eag2012Frame.startOfForm = startOfForm;
    }

    public static boolean isStartOfForm() {
        return startOfForm;
    }

    public static Date getTimeMaintenance() {
        return timeMaintenance;
    }

    public static void setTimeMaintenance(Date timeMaintenance) {
        Eag2012Frame.timeMaintenance = timeMaintenance;
    }
}
