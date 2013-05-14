package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.FileInstance;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.util.Xsd_enum;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 03/10/2012
 *
 * @author Yoann Moranville
 */
public abstract class DefaultBtnAction implements ActionListener {
    protected Eag eag;
    protected JTabbedPane tabbedPane;
    protected List<String> errors;
    protected ProfileListModel model;

    DefaultBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
        this.eag = eag;
        this.tabbedPane = tabbedPane;
        this.model = model;
    }

    public abstract void actionPerformed(ActionEvent actionEvent);
    protected abstract void updateEagObject(boolean save) throws Eag2012FormException;

    protected boolean notEqual(String newValue, String originalValue) {
        return !newValue.equals(originalValue);
    }

    protected List<String> getErrors() {
        return errors;
    }

    protected void saveFile(String id) {
        Eag2012Frame.inUse(false);
        try {
            if(model == null)
                throw new Eag2012FormException("The model is null, we can not add the EAG to the list...");
            JAXBContext jaxbContext = JAXBContext.newInstance(Eag.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EagNamespaceMapper());
            File eagFile = new File(Utilities.TEMP_DIR + "EAG2012_" + id + ".xml");
            if(model.existsFile(eagFile)) {
                model.removeFile(eagFile);
                eagFile.delete();
            }
            jaxbMarshaller.marshal(eag, eagFile);

            model.addFile(eagFile, Utilities.getXsdObjectFromPath(Xsd_enum.XSD_EAG_2012_SCHEMA.getPath()), FileInstance.FileType.EAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void reloadTabbedPanel(JComponent panel, int tabPosition) {
        String labelTitle = tabbedPane.getTitleAt(tabPosition);
        tabbedPane.remove(tabPosition);
        JScrollPane jScrollPane = new JScrollPane(panel);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        tabbedPane.insertTab(labelTitle, null, jScrollPane, "", tabPosition);
        tabbedPane.setSelectedIndex(tabPosition);
    }
}
