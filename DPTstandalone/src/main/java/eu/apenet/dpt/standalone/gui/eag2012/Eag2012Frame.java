package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import eu.apenet.dpt.utils.service.TransformationTool;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

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

    public Eag2012Frame(File eagFile, boolean isEag2012, Dimension dimension, ProfileListModel model) {
        if(!isEag2012) {
            try {
                InputStream is = FileUtils.openInputStream(eagFile);
                File tempEagFile = new File(Utilities.TEMP_DIR + "tmp_" + eagFile.getName());
                LOG.info(tempEagFile.getAbsolutePath());
                File xslFile = new File(Utilities.CONFIG_DIR + "eag2eag2012.xsl");
                TransformationTool.createTransformation(is, tempEagFile, xslFile, null, true, true, "", true, null);
                eagFile = tempEagFile;
            } catch (Exception e) {
                LOG.error("Something went wrong...", e);
            }
        }
        this.dimension = dimension;
        this.model = model;
        createFrame(eagFile);
    }

    public Eag2012Frame(Dimension dimension, ProfileListModel model) {
        URL emptyEAGFileURL = getClass().getResource("/EAG_XML_XSL/Blank_EAG_2012.xml");
        File emptyEAGFile = new File(emptyEAGFileURL.getPath());

        this.dimension = dimension;
        this.model = model;
        createFrame(emptyEAGFile);
    }

    public void createFrame(File eagFile) {
//        JFrame frame = new JFrame();
        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
        }



        Eag eag = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            eag = (Eag) jaxbUnmarshaller.unmarshal(eagFile);

            //TESTS: CREATE AN EAG FILE FROM OBJECT
//            eag.getControl().getMaintenanceAgency().getAgencyCode().setContent("TEST MARSHELLING");
//            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EagNamespaceMapper());
//            jaxbMarshaller.marshal(eag, new File("/Users/yoannmoranville/Work/APEnet/Projects/dpt-project/NA/trunk/output/APE_EAD_eag_35_MARSHALL.xml"));

            //tests:
//            eag.getArchguide().getDesc().getRepositories().setRepository(new ArrayList<Repository>());
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        buildPanel(eag);
        this.getContentPane().add(tabbedPane);

//        FormLayout layout = init2();
        Dimension frameDim = new Dimension(((Double)(dimension.getWidth() * 0.95)).intValue(), ((Double)(dimension.getHeight() * 0.95)).intValue());
        this.setSize(frameDim);
        this.setPreferredSize(frameDim);
//        this.setSize(new Dimension(1000, 1000));
//        JPanel panel = new JPanel(layout);
//        frame.getContentPane().add(panel);
        
        this.pack();
        this.setVisible(true);
    }

    public void buildPanel(Eag eag) {
        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("eag2012.yourInstitutionTab",  buildInstitutionPanel(eag));

        tabbedPane.add("eag2012.identityTab",  null);
        tabbedPane.setEnabledAt(1, false);
        tabbedPane.add("eag2012.contactTab", null);
        tabbedPane.setEnabledAt(2, false);
        tabbedPane.add("eag2012.accessAndServicesTab", null);
        tabbedPane.setEnabledAt(3, false);
        tabbedPane.add("eag2012.descriptionTab", null);
        tabbedPane.setEnabledAt(4, false);
        tabbedPane.add("eag2012.controlTab", null);
        tabbedPane.setEnabledAt(5, false);
        tabbedPane.add("eag2012.relationsTab", null);
        tabbedPane.setEnabledAt(6, false);
    }


    protected JComponent buildInstitutionPanel(Eag eag) {
        return new JScrollPane(new EagInstitutionPanel(eag, tabbedPane, this, model).buildEditorPanel(null));
    }
}
