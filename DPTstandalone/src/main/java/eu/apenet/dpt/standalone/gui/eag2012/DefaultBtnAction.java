package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.eag2012.data.Eag;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * User: Yoann Moranville
 * Date: 03/10/2012
 *
 * @author Yoann Moranville
 */
public abstract class DefaultBtnAction implements ActionListener {
    protected Eag eag;
    protected JTabbedPane tabbedPane;

    DefaultBtnAction(Eag eag, JTabbedPane tabbedPane) {
        this.eag = eag;
        this.tabbedPane = tabbedPane;
    }

    public abstract void actionPerformed(ActionEvent actionEvent);
    protected abstract void updateEagObject();

    protected void saveFile() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EagNamespaceMapper());
            jaxbMarshaller.marshal(eag, new File("/Users/yoannmoranville/Work/APEnet/Projects/dpt-project/NA/trunk/output/APE_EAD_eag_35_MARSHALL.xml"));

        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected void reloadTabbedPanel(JComponent panel, int tabPosition) {
        String labelTitle = tabbedPane.getTitleAt(tabPosition);
        tabbedPane.remove(tabPosition);
        tabbedPane.insertTab(labelTitle, null, new JScrollPane(panel), "", tabPosition);
        tabbedPane.setSelectedIndex(tabPosition);
    }
}
