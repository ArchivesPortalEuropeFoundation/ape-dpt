package eu.apenet.dpt.utils.util.extendxsl;

import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.*;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: 28/02/2013
 *
 * @author Yoann Moranville
 */
public class XmlQualityCheckerCall extends ExtensionFunctionCall {
    private static final Logger LOG = Logger.getLogger(XmlQualityCheckerCall.class);
    private int counterUnittitle = 0;
    private int counterUnitdate = 0;
    private int counterDao = 0;

    private List<String> idsUnittitle = new ArrayList<String>();
    private List<String> idsUnitdate = new ArrayList<String>();
    private List<String> idsDao = new ArrayList<String>();

    public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
        NodeInfo nodeInfo = (NodeInfo) arguments[0].next();
        NodeOverNodeInfo nodeOverNodeInfo = NodeOverNodeInfo.wrap(nodeInfo);
        boolean hasError;
        boolean wasInside;

        String id = "";
        NamedNodeMap cAttributes = nodeOverNodeInfo.getParentNode().getAttributes();
        for(int i = 0; i < cAttributes.getLength(); i++) {
            if(cAttributes.item(i).getLocalName() != null && cAttributes.item(i).getLocalName().equals("id")) {
                id = cAttributes.item(i).getNodeValue();
            }
        }

        NodeList nodes = nodeOverNodeInfo.getChildNodes();
        hasError = true;
        for(int i = 0; i < nodes.getLength(); i++) {
            if(nodes.item(i).getLocalName() != null && nodes.item(i).getLocalName().equals("unittitle")) {
                if(!nodes.item(i).getTextContent().equals(""))
                    hasError = false;
            }
        }
        if(hasError) {
            counterUnittitle++;
            idsUnittitle.add(id);
        }

        nodes = nodeOverNodeInfo.getChildNodes();
        hasError = true;
        wasInside = false;
        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if(node.getLocalName() != null && node.getLocalName().equals("unitdate")) {
                wasInside = true;
                NamedNodeMap nodeMap = node.getAttributes();
                for(int j = 0; j < nodeMap.getLength(); j++) {
                    Node attributes = nodeMap.item(j);
                    if(attributes.getLocalName().equals("normal")) {
                        hasError = false;
                    }
                }
            }
        }
        if(wasInside && hasError) {
            counterUnitdate++;
            idsUnitdate.add(id);
        }

        nodes = nodeOverNodeInfo.getChildNodes();
        hasError = true;
        wasInside = false;
        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if(node.getLocalName() != null && node.getLocalName().equals("dao")) {
                wasInside = true;
                NamedNodeMap nodeMap = node.getAttributes();
                for(int j = 0; j < nodeMap.getLength(); j++) {
                    Node attributes = nodeMap.item(j);
                    if(attributes.getLocalName().equals("role")) {
                        hasError = false;
                    }
                }
            }
        }
        if(wasInside && hasError) {
            counterDao++;
            idsDao.add(id);
        }

        return SingletonIterator.makeIterator(new StringValue(""));
    }

    public int getCounterUnittitle() {
        return counterUnittitle;
    }

    public int getCounterUnitdate() {
        return counterUnitdate;
    }

    public int getCounterDao() {
        return counterDao;
    }

    public List<String> getIdsUnittitle() {
        return idsUnittitle;
    }

    public List<String> getIdsUnitdate() {
        return idsUnitdate;
    }

    public List<String> getIdsDao() {
        return idsDao;
    }
}