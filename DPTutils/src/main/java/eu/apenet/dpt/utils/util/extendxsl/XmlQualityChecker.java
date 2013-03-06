package eu.apenet.dpt.utils.util.extendxsl;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;
import org.apache.log4j.Logger;

/**
 * User: Yoann Moranville
 * Date: 08/02/2013
 *
 * @author Yoann Moranville
 */
public class XmlQualityChecker extends ExtensionFunctionDefinition {
    private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "checkIfExist");
    private Logger log = Logger.getLogger(getClass());

    private XmlQualityCheckerCall xmlQualityCheckerCall;

    public XmlQualityChecker(XmlQualityCheckerCall xmlQualityCheckerCall) {
        this.xmlQualityCheckerCall = xmlQualityCheckerCall;
    }

    @Override
    public StructuredQName getFunctionQName() {
        return funcname;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 1;
    }
    public int getMaximumNumberOfArguments() {
        return 1;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.SINGLE_NODE};
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return SequenceType.OPTIONAL_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return xmlQualityCheckerCall;
    }
}
