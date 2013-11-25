/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util.extendxsl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author papp
 */
public class LinkFormatCheckerTest {

    private Set<String> linkSet;

    @Before
    public void setUp() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        linkSet = new HashSet<String>();
        linkSet.add("https://redmine.archivesportaleurope.net/issues?assigned_to_id=me&set_filter=1&sort=assigned_to%3Adesc%2Cpriority%3Adesc%2Cupdated_on%3Adesc");
        DefaultHandler handler = new DefaultHandler() {
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("entry")) {
                    linkSet.add(attributes.getValue("value"));
                }
            }

            public void characters(char ch[], int start, int length) throws SAXException {
            }

            public void endElement(String uri, String localName, String qName) throws SAXException {
            }
        };
        try {
            SAXParser saxParser = factory.newSAXParser();
            InputStream is = getClass().getResource("/test_links.xml").openStream();
            saxParser.parse(is, handler);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_linkformatChecker() {
        System.out.println("Test for correct link formatting");
        LinkFormatChecker linkFormatChecker = new LinkFormatChecker();
        for (String link : linkSet) {
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
            String newLink = linkFormatChecker.normalizeLink(link);
            System.out.println("Input:  " + link);
            System.out.println("Output: " + newLink);
        }
    }
}
