package eu.apenet.dpt.utils.util.extendxsl;

import eu.apenet.dpt.utils.service.ResourceBundlesWrapper;
import java.util.Locale;
import java.util.ResourceBundle;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
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
        public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) {
            if (arguments.length == 2) {
                try {
                    ResourceBundle resourceBundle = new ResourceBundlesWrapper(basenames, new Locale(arguments[1].next().getStringValue()));
                    String value = arguments[0].next().getStringValue();
                    value = resourceBundle.getString(value);
                    return SingletonIterator.makeIterator(new StringValue(value));
                } catch (XPathException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                return SingletonIterator.makeIterator(new StringValue("ERROR"));
            }
        }
    }
}
