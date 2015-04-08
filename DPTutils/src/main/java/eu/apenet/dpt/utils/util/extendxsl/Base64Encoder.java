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

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;

/**
 * Created by yoannmoranville on 23/02/15.
 */
public class Base64Encoder extends ExtensionFunctionDefinition {
    private static final StructuredQName FUNCTION_NAME = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "base64encoder");

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
        return new Base64EncoderCall();
    }

    public class Base64EncoderCall extends ExtensionFunctionCall {

        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            String result = "";
            if (sequences.length == 1) {
                try {
                    String arg = sequences[0].head().getStringValue();
                    result = DatatypeConverter.printBase64Binary(arg.getBytes("UTF-8"));
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
            return StringValue.makeStringValue(result);
        }
    }
}
