package eu.apenet.dpt.standalone.gui.eacCpf;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.namespace.EacCpfNamespaceMapper;
import eu.apenet.dpt.utils.eag2012.Eag;

import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.ReadXml;
import eu.apenet.dpt.utils.util.XmlTypeEacCpf;

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
 * Frame related to EAC-CPF
 */
public class EacCpfFrame extends JFrame {
    private static final Logger LOG = Logger.getLogger(EacCpfFrame.class);
    private JTabbedPane mainTabbedPane;
    private Dimension dimension;
    private ProfileListModel model;
    private ResourceBundle labels;
    private static boolean used;
    private static Date timeMaintenance;
    private static String personResponsible;
    private String countrycode;
    private String mainagencycode;
    private XmlTypeEacCpf eacType;
    private String firstLanguage;
    private String firstScript;
    public static boolean firstTimeInTab = true;

    public EacCpfFrame(File eacFile, boolean isEac, Dimension dimension, ProfileListModel model, ResourceBundle labels) throws Exception {
        String namespace = ReadXml.getXmlNamespace(eacFile);
        if(isEac && !namespace.equals(EacCpfNamespaceMapper.EAC_CPF_URI)) {
            throw new Exception("eaccpf.error.fileNotInEacCpfNamespace");
        } else if(!isEac && namespace.equals(EacCpfNamespaceMapper.EAC_CPF_URI)) {
            throw new Exception("eaccpf.error.notAnEacCpfFile");
        }
        if(!isEac) {
            try {
                InputStream is = FileUtils.openInputStream(eacFile);
                File tempEagFile = new File(Utilities.TEMP_DIR + "tmp_" + eacFile.getName());
                File xslFile = new File(Utilities.SYSTEM_DIR + "default-apeEAC-CPF.xsl");
                TransformationTool.createTransformation(is, tempEagFile, xslFile, null, true, true, "", true, null);
                eacFile = tempEagFile;
            } catch (Exception e) {
                LOG.error("Something went wrong...", e);
            }
        }
        this.dimension = dimension;
        this.model = model;
        this.labels = labels;
        createFrame(eacFile, false);
    }

    public EacCpfFrame(Dimension dimension, ProfileListModel model, ResourceBundle labels, String countrycode, String mainagencycode, XmlTypeEacCpf eacType, String firstLanguage, String firstScript) {
        InputStream emptyEACCPFileStream = DocumentValidation.class.getResourceAsStream("/eaccpf/Blank_EAC_CPF.xml");

        this.dimension = dimension;
        this.model = model;
        this.labels = labels;
        this.countrycode = countrycode;
        this.mainagencycode = mainagencycode;
        this.eacType = eacType;
        this.firstLanguage = firstLanguage;
        this.firstScript = firstScript;

        if (this.eacType == null) {
        	createFrame(emptyEACCPFileStream, true);
        } else {
        	createFrame(emptyEACCPFileStream, false);
        }
    }

    public void createFrame(File eacFile, boolean isNew) {
        try {
            createFrame(FileUtils.openInputStream(eacFile), isNew);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createFrame(InputStream eacCpfStream, boolean isNew) {
        timeMaintenance = null;
        personResponsible = null;
        inUse(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                inUse(false);
            }
        });
        EacCpf eacCpf = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EacCpf.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            eacCpf = (EacCpf) jaxbUnmarshaller.unmarshal(eacCpfStream);
            eacCpfStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.buildPanel(eacCpf, isNew);
        this.getContentPane().add(mainTabbedPane);

        Dimension frameDim = new Dimension(((Double)(dimension.getWidth() * 0.95)).intValue(), ((Double)(dimension.getHeight() * 0.95)).intValue());
        this.setSize(frameDim);
        this.setPreferredSize(frameDim);
        this.pack();
        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void buildPanel(EacCpf eacCpf, boolean isNew) {    
    	mainTabbedPane = new JTabbedPane();
        mainTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        mainTabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
    	if (isNew){
    		mainTabbedPane.add(labels.getString("eaccpf.tab.start"), new EacCpfStartPanel(eacCpf, null, mainTabbedPane, this, model, labels).buildEditorPanel(null));
    	}
    }

    public static void inUse(boolean used) {
    	EacCpfFrame.used = used;
    }

    public static boolean isUsed() {
        return used;
    }

    public static Date getTimeMaintenance() {
        return timeMaintenance;
    }

    public static void setTimeMaintenance(Date timeMaintenance) {
    	EacCpfFrame.timeMaintenance = timeMaintenance;
    }

    public static String getPersonResponsible() {
        return personResponsible;
    }

    public static void setPersonResponsible(String personResponsible) {
    	EacCpfFrame.personResponsible = personResponsible;
    }
}
