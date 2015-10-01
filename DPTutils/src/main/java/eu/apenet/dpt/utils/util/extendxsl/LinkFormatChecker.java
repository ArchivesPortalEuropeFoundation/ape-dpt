/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util.extendxsl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public class LinkFormatChecker extends ExtensionFunctionDefinition {
    private static final Logger LOG = Logger.getLogger(LinkFormatChecker.class);
    private static final StructuredQName FUNCTION_NAME = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "checkLink");
    
    //First row: prefix; second row: alphanumeric URL; third row: IP4 URL; fourth row: optional port and anything after the /
    private static final Pattern URL_PATTERN = Pattern.compile("^(http|https|ftp)\\://"
            + "(([a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3})?|"
            + "(^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.\n([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$)?)"
            + "(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$\\:#\\=~\\[\\]\\(\\)@;!])*$");
    
    private static final String[] FORBIDDEN_EXTENSIONS = {"bmp", "gif", "jpg", "png", "psd", "pspimage",
            "tif", "tiff", "dds", "indd", "pct", "pict", "tga", "yuv", "ai", "eps", "ps", "svg", "3dm",
            "3ds", "max", "obj", "doc", "docx", "log", "msg", "odt", "pages", "pdf", "rtf", "txt", "wpd",
            "wps", "tex", "csv", "dat", "ged", "key", "pps", "ppt", "pptx", "vcf", "xlr", "xls", "xlsx",
            "xml", "3g2", "3gp", "aif", "avi", "flv", "iff", "m3u", "m4a", "mid", "mov", "mp3", "mp4", "mpa",
            "mpg", "ra", "rm", "swf", "wav", "wma", "wmv", "asf", "asx", "m4v", "srt", "7z", "cbr", "deb",
            "gz", "pkg", "rar", "rpm", "tar", "zip", "zipx", "accdb", "db", "dbf", "mdb", "pdb", "sdf", "sql",
            "asp", "aspx", "css", "htm", "html", "js", "jsp", "php", "rss", "xhtm", "xhtml", "apk", "app",
            "bat", "exe", "jar", "bak", "tmp", "dot", "dotm", "dotx", "pot", "potm", "potx", "ots", "ott", "stc",
            "stw", "xlt", "c", "class", "cpp", "cs", "dtd", "fla", "h", "java", "m", "p", "py", "rng", "sh",
            "xsd", "xsl", "cur", "dll", "drv", "icns", "ico", "sys", ".cfg", "ini", "prf", "cgi", "gpx", "kml",
            "kmz", "crx", "plugin", "fnt", "fon", "otf", "ttf"};
    private static List<String> FORBIDDEN_EXTENSIONS_LIST;

    static {
        FORBIDDEN_EXTENSIONS_LIST = Arrays.asList(FORBIDDEN_EXTENSIONS);
    }

    public LinkFormatChecker() {
    }

    @Override
    public StructuredQName getFunctionQName() {
        return FUNCTION_NAME;
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
        return new LinkFormatCheckerCall();
    }

    public class LinkFormatCheckerCall extends ExtensionFunctionCall {
        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            if(sequences[0].head() == null)
                return StringValue.EMPTY_STRING;
            String out = normalizeLink(sequences[0].head().getStringValue());
            return StringValue.makeStringValue(out);
        }
    }

    public String normalizeLink(String link) {
        try {
            URL currentUrl = new URL(link);
            link = currentUrl.toString();
        } catch (MalformedURLException ex) {
            String part = link;
            if(!part.contains("://")) {
                if(part.contains("/"))
                    part = part.substring(0, part.indexOf("/"));
                if(part.contains("."))
                    part = part.substring(part.lastIndexOf(".") + 1, part.length());
                if(FORBIDDEN_EXTENSIONS_LIST.contains(part))
                    return null;
            }

            if (ex.getMessage().startsWith("no protocol") || ex.getMessage().startsWith("unknown protocol")) {
                link = "http://" + link;
            }
        }
        Matcher matcher = URL_PATTERN.matcher(link);
        try {
            if (matcher.matches()) {
                return link;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }
}
