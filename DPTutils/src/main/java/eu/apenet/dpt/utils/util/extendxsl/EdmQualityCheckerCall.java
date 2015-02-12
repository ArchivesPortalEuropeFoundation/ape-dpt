/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util.extendxsl;

import java.util.HashMap;
import java.util.Map;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.StringValue;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author papp
 */
public class EdmQualityCheckerCall extends ExtensionFunctionCall {

    private static final Logger LOG = Logger.getLogger(EdmQualityCheckerCall.class);
    private int counterNoUnitid = 0;
    private final Map<String, Integer> identifiers = new HashMap<String, Integer>();

    public int getCounterNoUnitid() {
        return counterNoUnitid;
    }

    public Map<String, Integer> getIdentifiers() {
        return identifiers;
    }

    @Override
    public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
        NodeInfo nodeInfo = (NodeInfo) sequences[0].head();
        NodeOverNodeInfo nodeOverNodeInfo = NodeOverNodeInfo.wrap(nodeInfo);
        boolean hasNoIdentifier;

        //get dc:identifier information from edm:ProvidedCHO
        String identifier = "";
        NodeList childNodes = nodeOverNodeInfo.getChildNodes();

        //if dc:identifier exists, set variable, else increment counter
        hasNoIdentifier = true;
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getLocalName() != null && node.getLocalName().equals("identifier") && node.getTextContent() != null && StringUtils.isNotBlank(node.getTextContent())) {
                hasNoIdentifier = false;
                identifier = node.getTextContent();
            }
        }
        if (hasNoIdentifier) {
            counterNoUnitid++;
        }

        //collect all dc:identifier values in Map for detection of duplicates
        if (StringUtils.isNotBlank(identifier)) {
            Integer numOccurrence = identifiers.get(identifier);
            if (numOccurrence == null) {
                //first count
                identifiers.put(identifier, 1);
            } else {
                identifiers.put(identifier, numOccurrence + 1);
            }
        }

        return StringValue.makeStringValue("");
    }
}
