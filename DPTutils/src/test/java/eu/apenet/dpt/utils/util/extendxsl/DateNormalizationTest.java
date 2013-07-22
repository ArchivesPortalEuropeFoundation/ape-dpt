package eu.apenet.dpt.utils.util.extendxsl;

import org.junit.* ;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Yoann
 * Date: Apr 23, 2010
 * Time: 12:06:39 PM
 * Output should be:
 *  - YYYY-MM-DD
 *  - YYYY
 *  - YYYY-MM-DD/YYYY-MM-DD
 *  - YYYY/YYYY
 */
public class DateNormalizationTest {
    private Map<String, String> dateMap;

    @Test
    public void test_dateNormalization() {
        System.out.println("Test to check for normalization of dates...");
        DateNormalization dateNormalization = new DateNormalization();
        int errors = 0;
        for (String date : dateMap.keySet()){
            String newdate = dateNormalization.normalizeDate(date, "");
            if(!dateMap.get(date).equals(newdate)){
                errors++;
                System.out.println("--> failed");
                System.out.println("Input: " + date);
                System.out.println("Output:" + newdate);
            }
        }
        System.out.println("-----------");
        System.out.println(dateMap.size() + " dates");
        System.out.println(errors + " errors");
    }

    private final String code1 = "NL-HaNA";
    private final String code2 = "RA";
    private final String code3 = "SE-";

    private final String[] codes = {code1, code2, code3};

    @Test
    public void test_mainagencycode(){
        System.out.println("Test to check for mainagencycode...");
        DateNormalization dateNormalization = new DateNormalization();
        for (String code : codes){
            System.out.println("---");
            System.out.println("Input: " + code);
            String newcode = dateNormalization.checkForMainagencycode(code);
            System.out.println("Output: " + newcode);
        }
    }

    @Before
    public void startTest(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        dateMap = new HashMap<String, String>();
        DefaultHandler handler = new DefaultHandler() {
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equalsIgnoreCase("entry")) {
                    dateMap.put(attributes.getValue("key"), attributes.getValue("value"));
                }
            }
            public void characters(char ch[], int start, int length) throws SAXException {
            }
            public void endElement(String uri, String localName, String qName) throws SAXException {
            }
        };
        try {
            SAXParser saxParser = factory.newSAXParser();
            InputStream is = getClass().getResource("/test_dates.xml").openStream();
            saxParser.parse(is, handler);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
