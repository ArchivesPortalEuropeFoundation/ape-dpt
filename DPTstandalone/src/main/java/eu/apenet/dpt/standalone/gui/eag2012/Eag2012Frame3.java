package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;

/**
 * User: Yoann Moranville
 * Date: 23/08/2012
 *
 * @author Yoann Moranville
 */
public class Eag2012Frame3 {
    private JTabbedPane tabbedPane;
    private EagInstitutionPanel eagInstitutionPanel;
    private EagIdentityPanel eagIdentityPanel;

    public Eag2012Frame3() {
        JFrame frame = new JFrame();

        Eag eag = null;
        try {
            File file = new File("/Users/yoannmoranville/Work/APEnet/Projects/dpt-project/NA/trunk/output/APE_EAD_eag_35.xml");

//            URL emptyEAGFileURL = getClass().getResource("/EAG_XML_XSL/Blank_EAG_2012.xml");
//            File emptyEAGFile = new File(emptyEAGFileURL.getPath());

            JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            eag = (Eag) jaxbUnmarshaller.unmarshal(file);

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
        frame.getContentPane().add(tabbedPane);

//        FormLayout layout = init2();
        frame.setSize(new Dimension(1000, 1000));
//        JPanel panel = new JPanel(layout);
//        frame.getContentPane().add(panel);
        
        frame.pack();
        frame.setVisible(true);
    }

    public void buildPanel(Eag eag) {
        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("eag2012.yourInstitutionTab",  buildInstitutionPanel(eag));

        tabbedPane.add("eag2012.identityTab",  buildIdentityPanel(eag));
        tabbedPane.setEnabledAt(1, false); //disable identity panel

//        tabbedPane.add(labels.getString("eag2012.contact"),   buildContactPanel());
//        tabbedPane.add(labels.getString("eag2012.accessAndServicesTab"),   buildAccessAndServicesPanel());
    }


    private JComponent buildInstitutionPanel(Eag eag) {
        eagInstitutionPanel = new EagInstitutionPanel(eag, tabbedPane);
        return new JScrollPane(eagInstitutionPanel.buildEditorPanel());
    }

    private JComponent buildIdentityPanel(Eag eag) {
        eagIdentityPanel = new EagIdentityPanel(eag, tabbedPane);
        return new JScrollPane(eagIdentityPanel.buildEditorPanel());
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
