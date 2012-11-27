package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import eu.apenet.dpt.utils.service.TransformationTool;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

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
    private JTabbedPane tabbedPane;
    private EagInstitutionPanel eagInstitutionPanel;
    private EagIdentityPanel eagIdentityPanel;

    public Eag2012Frame(File eagFile, boolean isEag2012) {
        if(!isEag2012) {
            //todo: do conversion first
            //eagFile = convertInEag2012File(eagFile);
            try {
                InputStream is = FileUtils.openInputStream(eagFile);
                File tempEagFile = new File(Utilities.TEMP_DIR + "tmp_" + eagFile.getName());
                File xslFile = new File(Utilities.CONFIG_DIR + "eag2eag2012.xsl");
                TransformationTool.createTransformation(is, tempEagFile, xslFile, null, true, true, "", true, null);
                eagFile = tempEagFile;
            } catch (Exception e) {
            }
        }
        createFrame(eagFile);
    }

    public Eag2012Frame() {
        URL emptyEAGFileURL = getClass().getResource("/EAG_XML_XSL/Blank_EAG_2012.xml");
        File emptyEAGFile = new File(emptyEAGFileURL.getPath());

        createFrame(emptyEAGFile);
    }

    public void createFrame(File eagFile) {
//        JFrame frame = new JFrame();

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
        this.setSize(new Dimension(1000, 1000));
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
        tabbedPane.setEnabledAt(1, false); //disable identity panel

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
        eagInstitutionPanel = new EagInstitutionPanel(eag, tabbedPane, this);
        return new JScrollPane(eagInstitutionPanel.buildEditorPanel(null));
    }

    protected JComponent buildIdentityPanel(Eag eag) {
        eagIdentityPanel = new EagIdentityPanel(eag, tabbedPane, this);
        return new JScrollPane(eagIdentityPanel.buildEditorPanel(null));
    }

//    private JComponent buildContactPanel() {
//        FormLayout layout = new FormLayout(
//                "right:max(50dlu;pref), 4dlu, max(35dlu;min), 2dlu, min, 2dlu, min, 2dlu, min, ",
//                EDITOR_ROW_SPEC);
//        return buildEditorGeneralPanel(layout);
//    }
//
//    private JComponent buildAccessAndServicesPanel() {
//        FormLayout layout = new FormLayout(
//                "right:max(50dlu;pref), 4dlu, max(35dlu;min), 2dlu, min, 2dlu, min, 2dlu, min, ",
//                EDITOR_ROW_SPEC);
//        return buildEditorTransportPanel(layout);
//    }

//    private static final String DEFAULT_ROW_SPEC = "3dlu, p";

}
