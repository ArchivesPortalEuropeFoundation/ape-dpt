package eu.apenet.dpt.utils.util.extendxsl;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.*;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Yoann Moranville
 * Date: 06/02/2013
 *
 * @author Yoann Moranville
 */
public class FlagSet extends ExtensionFunctionDefinition {
    private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "flagSet");
    private Logger log = Logger.getLogger(getClass());
    private int i = 0;

    @Override
    public StructuredQName getFunctionQName() {
        return funcname;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 0;
    }
    public int getMaximumNumberOfArguments() {
        return 0;
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
        return new FlagSetCall();
    }


    public class FlagSetCall extends ExtensionFunctionCall {
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            Item item = new StringValue(i==0?"true":"false");
            if(i == 0)
                i = 1;
            return item;
        }
    }
}
