package eu.apenet.dpt.utils.util.extendxsl;

import eu.apenet.dpt.utils.service.ResourceBundlesWrapper;
import java.util.Locale;
import java.util.ResourceBundle;
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

public class ResourcebundleExtension extends ExtensionFunctionDefinition {

    private static final StructuredQName FUNCTION_NAME = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions",
            "resource");
    private static final Logger LOG = Logger.getLogger(ResourcebundleExtension.class);

    public ResourcebundleExtension() {
    }

    @Override
    public StructuredQName getFunctionQName() {
        return FUNCTION_NAME;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 2;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 2;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.OPTIONAL_STRING, SequenceType.OPTIONAL_STRING};
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return SequenceType.OPTIONAL_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new ResourcebundleExtensionCall();
    }

    class ResourcebundleExtensionCall extends ExtensionFunctionCall {

        private final String[] basenames = {"i18n/eac-cpf/eac-cpf"};

        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            if (sequences.length == 2) {
                ResourceBundle resourceBundle = new ResourceBundlesWrapper(basenames, new Locale(sequences[1].toString()));
                String value = sequences[0].toString();
                value = resourceBundle.getString(value);
                return StringValue.makeStringValue(value);
            } else {
                return StringValue.makeStringValue("ERROR");
            }
        }
    }
}
