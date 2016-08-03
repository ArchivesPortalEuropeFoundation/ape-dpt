package eu.apenet.dpt.utils.util.extendxsl;

import eu.apenet.dpt.utils.util.DateConversionXMLFilehandler;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Yoann Date: Apr 22, 2010
 */
public class DateNormalization extends ExtensionFunctionDefinition {

    private static final StructuredQName FUNCTION_NAME = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "normalizeDate");
    //Normal ISO pattern
    private static final Pattern PATTERN_CORRECT_SIMPLE = Pattern.compile("(\\-?(0|1|2)([0-9]{3})(((01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1])))|\\-((01|02|03|04|05|06|07|08|09|10|11|12)(\\-((0[1-9])|((1|2)[0-9])|(3[0-1])))?))?)(/\\-?(0|1|2)([0-9]{3})(((01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1])))|\\-((01|02|03|04|05|06|07|08|09|10|11|12)(\\-((0[1-9])|((1|2)[0-9])|(3[0-1])))?))?)?");
    //01.01.1985
    private static final Pattern PATTERN_DD_MM_YYYY = Pattern.compile("([0-9]{2})\\.([0-9]{2})\\.([0-9]{4})");
    private static final Pattern PATTERN_YYYYMM = Pattern.compile("(0|1|2)([0-9]{3})([0-9]{2})");
    //1985.01.01
    private static final Pattern PATTERN_YYYY_MM_DD = Pattern.compile("([0-9]{4})\\.([0-9]{2})\\.([0-9]{2})");
    //01.01.1985/02.01.1985
    private static final Pattern PATTERN_DD_MM_YYYY_TIMESPAN = Pattern.compile("([0-9]{2})\\.([0-9]{2})\\.([0-9]{4})\\s?(/|\\?|-)?\\s?([0-9]{2})\\.([0-9]{2})\\.([0-9]{4})");
    //1.1.1985 - 2.1.1985
    private static final Pattern PATTERN_D_M_YYYY_TIMESPAN = Pattern.compile("([1-9]{1})\\.([1-9]{1})\\.([0-9]{4})\\s?(/|\\?|-)?\\s?([1-9]{1})\\.([1-9]{1})\\.([0-9]{4})");
    //1985.01.01/1985.01.02
    private static final Pattern PATTERN_YYYY_MM_DD_TIMESPAN = Pattern.compile("([0-9]{4})\\.([0-9]{2})\\.([0-9]{2})\\s?(/|\\?|-)?\\s?([0-9]{4})\\.([0-9]{2})\\.([0-9]{2})");
    //01011985
    private static final Pattern PATTERN_DDMMYYYY = Pattern.compile("((0[1-9])|((1|2)[0-9])|(3[0-1]))(01|02|03|04|05|06|07|08|09|10|11|12)((0|1|2)([0-9]{3}))");
    //01011985/02011985
    private static final Pattern PATTERN_DDMMYYYY_TIMESPAN = Pattern.compile("((0[1-9])|((1|2)[0-9])|(3[0-1]))(01|02|03|04|05|06|07|08|09|10|11|12)((0|1|2)([0-9]{3}))/((0[1-9])|((1|2)[0-9])|(3[0-1]))(01|02|03|04|05|06|07|08|09|10|11|12)((0|1|2)([0-9]{3}))");
    //19850101
    private static final Pattern PATTERN_YYYYMMDD = Pattern.compile("((0|1|2)([0-9]{3}))(01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1]))");
    //1985.01.01/1985.01.02
    private static final Pattern PATTERN_YYYYMMDD_TIMESPAN = Pattern.compile("((0|1|2)([0-9]{3}))(01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1]))/((0|1|2)([0-9]{3}))(01|02|03|04|05|06|07|08|09|10|11|12)((0[1-9])|((1|2)[0-9])|(3[0-1]))");
    //1985--1986
    private static final Pattern PATTERN_YYYY_YYYY = Pattern.compile("((0|1|2)([0-9]{3}))--((0|1|2)([0-9]{3}))");
    //1985 - 1987, 1990
    private static final Pattern PATTERN_YYYY_YYYY_YYYY = Pattern.compile("\\(?\\[?((0|1|2)([0-9]{3}))\\)?\\]?\\s*-*,?\\s*((0|1|2)([0-9]{3}))\\s*-*,?\\s*\\(?\\[?((0|1|2)([0-9]{3}))\\]?\\)?\\s*");
    //03.07.1985 bis (to, à, etc.) 06.07.1985
    private static final Pattern PATTERN_1 = Pattern.compile("([0-9]{2})\\.([0-9]{2})\\.([0-9]{4})\\D+([0-9]{2})\\.([0-9]{2})\\.([0-9]{4})");
    //3.7.1985 bis (to, à, etc.) 6.7.1985
    private static final Pattern PATTERN_3 = Pattern.compile("([1-9]{1})\\.([1-9]{1})\\.([0-9]{4})\\D+([1-9]{1})\\.([1-9]{1})\\.([0-9]{4})");
    //1985 bis (to, à, etc.) 1988
    private static final Pattern PATTERN_2 = Pattern.compile("([0-9]{4})\\D*(\\s\\?\\s)?([0-9]{4})");
    //avant 1985
    private static final Pattern PATTERN_4 = Pattern.compile("(avant|vor|before)*\\s*([0-9]{4})");
    //apres 1985
    private static final Pattern PATTERN_5 = Pattern.compile("(apres|après|nach|after)*\\s*([0-9]{4})");
    //around 1985
    private static final Pattern PATTERN_6 = Pattern.compile("(environ|around|ca\\.?|env\\.?|etwa\\.?|um\\.?)*\\s*([0-9]{4})");
    //century
    private static final Pattern PATTERN_7 = Pattern.compile("(1[0-9]{1})(ieme|\\.|th|de|e|tes)* (siècle|siecle|Jhd|century|Century|Jahrhundert|Jh|eeuw)*\\s*\\.*");
    //century timespan
    private static final Pattern PATTERN_8 = Pattern.compile("(1[0-9]{1})(ieme|\\.|th|de|e|tes)* (bis|a|to|bis|à)* (1[0-9]{1})(ieme|\\.|th|de|e|tes)* (siècle|siecle|Jhd|century|Century|Jahrhundert|Jh|eeuw)*\\s*\\.*");
    //([a-zA-Z]*[0-9]{0,3}\\s*\\.*)*([0-9]{4}){1}([a-zA-Z]*[0-9]{0,3}\\s*\\.*)*([0-9]{4}){1}
//    private final Pattern PATTERN_7 = Pattern.compile("([a-zA-Z]*[0-9]{0,3}\\s*\\.*\\?*)*([0-9]{4}){1}([a-zA-Z]*[0-9]{0,3}\\s*\\.*\\?*)*([0-9]{4}){1}");
//    private final Pattern PATTERN_8 = Pattern.compile("([a-zA-Z]*[0-9]{0,3}\\s*\\.*\\?*)*([0-9]{4}){1}");
    //For mainagencycode
    public static final Pattern PATTERN_MAINAGENCYCODE = Pattern.compile("((AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BA|BW|BV|BR|IO|BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|IL|IT|JM|JP|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|FM|MD|MC|MN|MS|MA|MZ|MM|NA|NR|NP|NL|AN|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|RO|RU|RW|SH|KN|LC|PM|VC|WS|SM|ST|SA|SN|CS|SC|SL|SG|SK|SI|SB|SO|ZA|GS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW|RS|ME|EU|IM)|([a-zA-Z]{1})|([a-zA-Z]{3,4}))(-[a-zA-Z0-9:/\\-]{1,11})");
    public static final Pattern PATTERN_COUNTRYCODE = Pattern.compile("(AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BA|BW|BV|BR|IO|BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|IL|IT|JM|JP|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|FM|MD|MC|MN|MS|MA|MZ|MM|NA|NR|NP|NL|AN|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|RO|RU|RW|SH|KN|LC|PM|VC|WS|SM|ST|SA|SN|CS|SC|SL|SG|SK|SI|SB|SO|ZA|GS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW|RS|ME|EU|IM)");

    private String baseURI = "";
    public DateNormalization(String... baseURI) {
        if(baseURI.length == 1)
            this.baseURI = baseURI[0];
    }

    @Override
    public StructuredQName getFunctionQName() {
        return FUNCTION_NAME;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 1;
    }

    public int getMaximumNumberOfArguments() {
        return 2;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.OPTIONAL_STRING};
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return SequenceType.OPTIONAL_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new DateNormalizationCall(baseURI);
    }

    public class DateNormalizationCall extends ExtensionFunctionCall {

        private static final long serialVersionUID = 6761914863093344493L;

        private String baseURI;
        public DateNormalizationCall(String baseURI) {
            this.baseURI = baseURI;
        }

        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            if (sequences.length == 2) {
                if (!sequences[1].head().getStringValue().equals("pl_unitid")) {
                    String out = checkForMainagencycode(sequences[0].head().getStringValue());
                    return StringValue.makeStringValue(out);
                } else {
                    String out = changePLunitid(sequences[0].head().getStringValue());
                    return StringValue.makeStringValue(out);
                }
            } else {
                String out = printNumberTest(sequences[0].head().getStringValue());
                return StringValue.makeStringValue(out);
            }
        }

        public String printNumberTest(String input) {
            input = normalizeDate(input, baseURI);
            return input;
        }
    }


    /*Here is going to be the normalization itself*/
    public String normalizeDate(String date, String baseURI) {
        DateConversionXMLFilehandler fileHandler = new DateConversionXMLFilehandler();
        String fromXmlDateFile = fileHandler.findsEntry(date, baseURI);
        if (fromXmlDateFile != null)
            return fromXmlDateFile;

        if (date.contains("9999"))
            date = date.replace("9999", "2099");
        if (date.contains("3000"))
            date = date.replace("3000", "2099");

        //log.info("Trying to normalize: " + date);
        try {
            Matcher matcher_correct_simple = PATTERN_CORRECT_SIMPLE.matcher(date);
            if (matcher_correct_simple.matches()) {
                //We need a second check for YYYYMMDD and YYYYMMDD/YYYYMMDD which passes above
                if (!Pattern.compile("([0-9]{8})").matcher(date).matches() && !Pattern.compile("([0-9]{8})/([0-9]{8})").matcher(date).matches()) {
                    return date;
                }
            }

            Matcher matcher_dd_mm_yyyy = PATTERN_DD_MM_YYYY.matcher(date);
            if (matcher_dd_mm_yyyy.matches())
                return matcher_dd_mm_yyyy.group(3) + "-" + matcher_dd_mm_yyyy.group(2) + "-" + matcher_dd_mm_yyyy.group(1);
  
            Matcher matcher_yyyy_mm = PATTERN_YYYYMM.matcher(date);
            if (matcher_yyyy_mm.matches()) 
                return matcher_yyyy_mm.group(1) + matcher_yyyy_mm.group(2) + "-" + matcher_yyyy_mm.group(3);
            
            Matcher matcher_yyyy_mm_dd = PATTERN_YYYY_MM_DD.matcher(date);
            if (matcher_yyyy_mm_dd.matches()) 
                return matcher_yyyy_mm_dd.group(1) + "-" + matcher_yyyy_mm_dd.group(2) + "-" + matcher_yyyy_mm_dd.group(3);
            
            Matcher matcher_dd_mm_yyyy_timespan = PATTERN_DD_MM_YYYY_TIMESPAN.matcher(date);
            if (matcher_dd_mm_yyyy_timespan.matches()) 
                return matcher_dd_mm_yyyy_timespan.group(3) + "-" + matcher_dd_mm_yyyy_timespan.group(2) + "-" + matcher_dd_mm_yyyy_timespan.group(1) + "/" + matcher_dd_mm_yyyy_timespan.group(7) + "-" + matcher_dd_mm_yyyy_timespan.group(6) + "-" + matcher_dd_mm_yyyy_timespan.group(5);
            
            Matcher matcher_d_m_yyyy_timespan = PATTERN_D_M_YYYY_TIMESPAN.matcher(date);
            if (matcher_d_m_yyyy_timespan.matches()) 
                return matcher_d_m_yyyy_timespan.group(3) + "-" + "0" + matcher_d_m_yyyy_timespan.group(2) + "-" + "0" + matcher_d_m_yyyy_timespan.group(1) + "/" + matcher_d_m_yyyy_timespan.group(7) + "-" + "0" + matcher_d_m_yyyy_timespan.group(6) + "-" + "0" + matcher_d_m_yyyy_timespan.group(5);
            
            Matcher matcher_yyyy_mm_dd_timespan = PATTERN_YYYY_MM_DD_TIMESPAN.matcher(date);
            if (matcher_yyyy_mm_dd_timespan.matches()) 
                return matcher_yyyy_mm_dd_timespan.group(1) + "-" + matcher_yyyy_mm_dd_timespan.group(2) + "-" + matcher_yyyy_mm_dd_timespan.group(3) + "/" + matcher_yyyy_mm_dd_timespan.group(5) + "-" + matcher_yyyy_mm_dd_timespan.group(6) + "-" + matcher_yyyy_mm_dd_timespan.group(7);
            
            Matcher matcher_yyyymmdd = PATTERN_YYYYMMDD.matcher(date);
            if (matcher_yyyymmdd.matches()) {
                if (Integer.parseInt(matcher_yyyymmdd.group(1)) > 1000) { //If it is smaller than 1000, it probably means it is wrong
                    return matcher_yyyymmdd.group(1) + "-" + matcher_yyyymmdd.group(4) + "-" + matcher_yyyymmdd.group(5);
                }
            }

            Matcher matcher_yyyymmdd_timespan = PATTERN_YYYYMMDD_TIMESPAN.matcher(date);
            if (matcher_yyyymmdd_timespan.matches()) {
                if (Integer.parseInt(matcher_yyyymmdd_timespan.group(1)) > 1000) {
                    return matcher_yyyymmdd_timespan.group(1) + "-" + matcher_yyyymmdd_timespan.group(4) + "-" + matcher_yyyymmdd_timespan.group(5) + "/" + matcher_yyyymmdd_timespan.group(10) + "-" + matcher_yyyymmdd_timespan.group(13) + "-" + matcher_yyyymmdd_timespan.group(14);
                }
            }

            Matcher matcher_ddmmyyyy = PATTERN_DDMMYYYY.matcher(date);
            if (matcher_ddmmyyyy.matches()) 
                return matcher_ddmmyyyy.group(7) + "-" + matcher_ddmmyyyy.group(6) + "-" + matcher_ddmmyyyy.group(1);
            
            Matcher matcher_ddmmyyyy_timespan = PATTERN_DDMMYYYY_TIMESPAN.matcher(date);
            if (matcher_ddmmyyyy_timespan.matches()) 
                return matcher_ddmmyyyy_timespan.group(7) + "-" + matcher_ddmmyyyy_timespan.group(6) + "-" + matcher_ddmmyyyy_timespan.group(1) + "/" + matcher_ddmmyyyy_timespan.group(16) + "-" + matcher_ddmmyyyy_timespan.group(15) + "-" + matcher_ddmmyyyy_timespan.group(10);
            
            Matcher matcher_yyyy_yyyy = PATTERN_YYYY_YYYY.matcher(date);
            if (matcher_yyyy_yyyy.matches()) 
                return matcher_yyyy_yyyy.group(1) + "/" + matcher_yyyy_yyyy.group(4);
            
            Matcher matcher_yyyy_yyyy_yyyy = PATTERN_YYYY_YYYY_YYYY.matcher(date);
            if (matcher_yyyy_yyyy_yyyy.matches())
                return matcher_yyyy_yyyy_yyyy.group(1) + "/" + matcher_yyyy_yyyy_yyyy.group(7);
            
            return findNormalizationProcess(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String findNormalizationProcess(String date) {
        try {
            Matcher matcher_1 = PATTERN_1.matcher(date);
            if (matcher_1.matches()) 
                return matcher_1.group(3) + "-" + matcher_1.group(2) + "-" + matcher_1.group(1) + "/" + matcher_1.group(6) + "-" + matcher_1.group(5) + "-" + matcher_1.group(4);
            
            Matcher matcher_3 = PATTERN_3.matcher(date);
            if (matcher_3.matches()) 
                return matcher_3.group(3) + "-" + "0" + matcher_3.group(2) + "-" + "0" + matcher_3.group(1) + "/" + matcher_3.group(6) + "-" + "0" + matcher_3.group(5) + "-" + "0" + matcher_3.group(4);
            
            Matcher matcher_2 = PATTERN_2.matcher(date);
            if (matcher_2.matches()) 
                return matcher_2.group(1) + "/" + matcher_2.group(3);
            
            Matcher matcher_4 = PATTERN_4.matcher(date);
            if (matcher_4.matches()) 
                return "0001/" + matcher_4.group(2);
            
            Matcher matcher_5 = PATTERN_5.matcher(date);
            if (matcher_5.matches()) 
                return matcher_5.group(2) + "/2099";
            
            Matcher matcher_6 = PATTERN_6.matcher(date);
            if (matcher_6.matches()) 
                return matcher_6.group(2);
            
            Matcher matcher_7 = PATTERN_7.matcher(date);
            if (matcher_7.matches()) 
                return (Integer.parseInt(matcher_7.group(1)) - 1) + "00/" + matcher_7.group(1) + "00";
            
            Matcher matcher_8 = PATTERN_8.matcher(date);
            if (matcher_8.matches()) 
                return (Integer.parseInt(matcher_8.group(1)) - 1) + "00/" + matcher_8.group(4) + "00";
            
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String checkForMainagencycode(String mainagencycode) {
        if(mainagencycode == null)
            return null;
        if (mainagencycode.contains("Ö")) 
            mainagencycode = mainagencycode.replace("Ö", "O");
        Matcher matcher = PATTERN_MAINAGENCYCODE.matcher(mainagencycode);
        if (matcher.matches()) 
            return mainagencycode;
        return null;
    }

    public String checkForCountrycode(String countrycode) {
        if(countrycode == null)
            return null;
        Matcher matcher = PATTERN_COUNTRYCODE.matcher(countrycode);
        if (matcher.matches()) 
            return countrycode;
        return null;
    }

    public String checkForNormalAttribute(String date) {
        Matcher matcher_correct_simple = PATTERN_CORRECT_SIMPLE.matcher(date);
        if (matcher_correct_simple.matches()) {
            if (!Pattern.compile("([0-9]{8})").matcher(date).matches() && !Pattern.compile("([0-9]{8})/([0-9]{8})").matcher(date).matches()) {
                return date;
            }
        }
        return null;
    }

    public String changePLunitid(String unitid) {
        String out = unitid.replace(" ", "-");
        out = out.replaceFirst("/", "-");
        return out;
    }
}
