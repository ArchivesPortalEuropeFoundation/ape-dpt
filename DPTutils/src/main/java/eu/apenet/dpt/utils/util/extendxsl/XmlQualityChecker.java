package eu.apenet.dpt.utils.util.extendxsl;

import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.tree.tiny.TinyElementImpl;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * User: Yoann Moranville
 * Date: 08/02/2013
 *
 * @author Yoann Moranville
 */
public class XmlQualityChecker extends ExtensionFunctionDefinition {
    private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "checkIfExist");
    private Logger log = Logger.getLogger(getClass());

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
        return new XmlQualityCheckerCall();
    }


    public class XmlQualityCheckerCall extends ExtensionFunctionCall {

        public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
            NodeInfo node = (NodeInfo) arguments[0].next();
            NodeOverNodeInfo nodeOverNodeInfo = NodeOverNodeInfo.wrap(node);
            NodeList unittitles = nodeOverNodeInfo.getOwnerDocument().getElementsByTagName("unititle");
            if(unittitles == null ||  unittitles.getLength() == 0) {
                System.out.println("No unittitle");
            }
//            NodeList nodeList = nodeOverNodeInfo.getChildNodes();
//            for(int i = 0; i < nodeList.getLength(); i++) {
//                System.out.println(nodeList.item(i).getLocalName());
//            }
            return SingletonIterator.makeIterator(new StringValue(""));
        }
    }
}
