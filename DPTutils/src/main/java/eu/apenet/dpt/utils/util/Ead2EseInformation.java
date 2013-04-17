package eu.apenet.dpt.utils.util;

import eu.apenet.dpt.utils.ead2ese.XMLUtil;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * User: Yoann Moranville Date: 15/04/2013
 *
 * @author Yoann Moranville
 */
public class Ead2EseInformation {

    private static final Logger LOG = Logger.getLogger(Ead2EseInformation.class);
    private String languageCode;
    private String repository;
    private String roleType;
    private String archdescRepository;
    private TreeSet<String> alternativeLanguages;

    public String getLanguageCode() {
        return languageCode;
    }

    public String getRepository() {
        return repository;
    }

    public String getRoleType() {
        return roleType;
    }

    public String getArchdescRepository() {
        return archdescRepository;
    }
    
    public TreeSet<String> getAlternativeLanguages() {
        return alternativeLanguages;
    }

    public Ead2EseInformation(File fileToRead, String databaseRoleType, String archdescRepository) throws IOException, SAXException, ParserConfigurationException {
        this.archdescRepository = archdescRepository;
        this.alternativeLanguages = new TreeSet<String>();
        determineDaoInformation(fileToRead, databaseRoleType);
    }

    public Ead2EseInformation() {
        languageCode = "";
        repository = "";
        roleType = "";
        archdescRepository = "";
        alternativeLanguages =  new TreeSet<String>();
    }

    private void determineDaoInformation(File fileToRead, String databaseRoleType) throws IOException, SAXException, ParserConfigurationException {
        Document doc = XMLUtil.convertXMLToDocument(new FileInputStream(fileToRead));
        NodeList nodelist = doc.getElementsByTagName("dao");
        if (nodelist.getLength() != 0) {
            int counter = 0;
            do {
                if (languageCode != null && repository != null && roleType != null) {
                    break;
                }
                Node daoNode = nodelist.item(counter);
                roleType = determineRoleType(daoNode, databaseRoleType);
                repository = determineRepository(daoNode, doc);
                languageCode = determineLanguageCode(daoNode, doc);
                counter++;
            } while ((languageCode == null || repository == null || roleType == null) && counter < nodelist.getLength());
        }
    }

    private String determineRoleType(Node daoNode, String databaseRoleType) {
        if (!databaseRoleType.equals("UNSPECIFIED") && databaseRoleType != null) {
            return databaseRoleType;
        }

        NamedNodeMap attributes = daoNode.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.getNamedItem("xlink:role");
            if (attribute != null) {
                return attribute.getTextContent();
            }
        }
        return "";
    }

    private String determineRepository(Node daoNode, Document doc) {
        String result = "";
        Node didNode = daoNode.getParentNode();
        NodeList repositories = doc.getElementsByTagName("repository");
        if (repositories.getLength() != 0) {
            int counter = 0;
            do {
                Node repositoryNode = repositories.item(counter);
                if (repositoryNode instanceof Element) {
                    if (((Element) repositoryNode).getParentNode().getParentNode().getNodeName().equals("c") && ((Element) repositoryNode).getParentNode() == didNode) {
                        int index = 0;
                        while (index < ((Element) repositoryNode).getTextContent().length()) {
                            if (((Element) repositoryNode).getTextContent().charAt(index) >= ' ') {
                                index++;
                            } else {
                                break;
                            }
                        }
                        return ((Element) repositoryNode).getTextContent().substring(0, index);
                    }

                    if (((Element) repositoryNode).getParentNode().getParentNode().getNodeName().equals("archdesc")) {
                        int index = 0;
                        while (index < ((Element) repositoryNode).getTextContent().length()) {
                            if (((Element) repositoryNode).getTextContent().charAt(index) >= ' ') {
                                index++;
                            } else {
                                break;
                            }
                        }
                        if (archdescRepository == null) {
                            archdescRepository = ((Element) repositoryNode).getTextContent().substring(0, index);
                        }
                    }
                }
                counter++;
            } while (counter < repositories.getLength());
        }
        return result;
    }

    private String determineLanguageCode(Node daoNode, Document doc) {
        String result = null;
        Node didNode = daoNode.getParentNode();
        NodeList languages = doc.getElementsByTagName("language");
        if (languages.getLength() != 0) {
            int counter = 0;
            do {
                Node languageNode = languages.item(counter);
                if (languageNode.getParentNode().getParentNode() == didNode) {
                    NamedNodeMap attributes = languageNode.getAttributes();
                    for (int i = 0; i < attributes.getLength(); i++) {
                        Node attribute = attributes.item(i);
                        if (attribute.getNodeName().equals("langcode")) {
                            result = attribute.getTextContent();
                        }
                    }
                }
                /*                if (languageNode.getParentNode().getParentNode().getNodeName().equals("c")) {
                 Node cNode = languageNode.getParentNode().getParentNode();
                 while (!cNode.getParentNode().getNodeName().equals("ead")) {
                       
                 }
                 }*/
                else {
                    NamedNodeMap attributes = languageNode.getAttributes();
                    for (int i = 0; i < attributes.getLength(); i++) {
                        Node attribute = attributes.item(i);
                        if (attribute.getNodeName().equals("langcode")) {
                            alternativeLanguages.add(attribute.getTextContent());
                        }
                    }
                }
                counter++;
            } while (result == null && counter < languages.getLength());
        }
        return result;
    }
}
