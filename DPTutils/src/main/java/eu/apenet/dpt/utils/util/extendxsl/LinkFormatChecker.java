/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util.extendxsl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

/**
 *
 * @author papp
 */
public class LinkFormatChecker extends ExtensionFunctionDefinition {

    private static final StructuredQName FUNCTION_NAME = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "checkLink");
    private static final Pattern URL_PATTERN = Pattern.compile("^(http|https|ftp)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$\\:#\\=~\\[\\]\\(\\)@;!])*$");

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
            String out = normalizeLink(sequences[0].head().getStringValue());
            return StringValue.makeStringValue(out);
        }
    }

    public String normalizeLink(String link) {
        try {
            URL currentUrl = new URL(link);
            link = currentUrl.toString();
        } catch (MalformedURLException ex) {
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
