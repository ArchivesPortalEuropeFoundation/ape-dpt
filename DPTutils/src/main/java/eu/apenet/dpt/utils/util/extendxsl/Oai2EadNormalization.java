package eu.apenet.dpt.utils.util.extendxsl;

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
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * User: Yoann
 * Date: Apr 22, 2010
 */
public class Oai2EadNormalization extends ExtensionFunctionDefinition {
    private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "oai2EadNormalization");
    private Logger log = Logger.getLogger(getClass());

    private final Pattern pattern_unitid = Pattern.compile("(.*)unitid%3D(.*)%26unitdate(.*)");

    @Override
    public StructuredQName getFunctionQName() {
        return funcname;
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
        return new DateNormalizationCall();
    }


    public class DateNormalizationCall extends ExtensionFunctionCall {
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            String out = normalizeId(sequences[0].head().getStringValue());
            return StringValue.makeStringValue(out);
        }
    }


    /*Here is going to be the normalization itself*/
    public String normalizeId(String input){
        try{
            Matcher matcher_unitid = pattern_unitid.matcher(input);
            if(matcher_unitid.matches())
                return URLDecoder.decode(matcher_unitid.group(2), "UTF-8");
            return null;
        } catch (UnsupportedEncodingException e){
            log.error("Error of decode from OAI - " + e);
            return null;
        }
    }
}
