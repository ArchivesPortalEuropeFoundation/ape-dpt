package eu.apenet.dpt.utils.util.extendxsl;

import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.HashMap;
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

    private Map<String, Boolean> idsUnittitle = new HashMap<String, Boolean>();
    private Map<String, Boolean> idsUnitdate = new HashMap<String, Boolean>();
    private Map<String, Boolean> idsDao = new HashMap<String, Boolean>();

    public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
        NodeInfo nodeInfo = (NodeInfo) arguments[0].next();
        NodeOverNodeInfo nodeOverNodeInfo = NodeOverNodeInfo.wrap(nodeInfo);
        boolean hasError;
        boolean wasInside;

        String id = "";
        String unitid = "";
        NamedNodeMap cAttributes = nodeOverNodeInfo.getParentNode().getAttributes();
        for(int i = 0; i < cAttributes.getLength(); i++) {
            if(cAttributes.item(i).getLocalName() != null && cAttributes.item(i).getLocalName().equals("id")) {
                id = cAttributes.item(i).getNodeValue();
            }
        }

        NodeList nodes = nodeOverNodeInfo.getChildNodes();
        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if(node.getLocalName() != null && node.getLocalName().equals("unitid")) {
                NamedNodeMap nodeMap = node.getAttributes();
                for(int j = 0; j < nodeMap.getLength(); j++) {
                    Node attributes = nodeMap.item(j);
                    if(attributes.getLocalName().equals("type") && attributes.getTextContent().equals("call number")) {
                        unitid = node.getTextContent();
                    }
                }
            }
        }

        nodes = nodeOverNodeInfo.getChildNodes();
        hasError = true;
        for(int i = 0; i < nodes.getLength(); i++) {
            if(nodes.item(i).getLocalName() != null && nodes.item(i).getLocalName().equals("unittitle")) {
                if(!nodes.item(i).getTextContent().equals(""))
                    hasError = false;
            }
        }
        if(hasError) {
            counterUnittitle++;
            if(StringUtils.isNotBlank(unitid))
                idsUnittitle.put(unitid, true);
            else
                idsUnittitle.put(id, false);
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
            if(StringUtils.isNotBlank(unitid))
                idsUnitdate.put(unitid, true);
            else
                idsUnitdate.put(id, false);
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
            if(StringUtils.isNotBlank(unitid))
                idsDao.put(unitid, true);
            else
                idsDao.put(id, false);
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

    public Map<String, Boolean> getIdsUnittitle() {
        return idsUnittitle;
    }

    public Map<String, Boolean> getIdsUnitdate() {
        return idsUnitdate;
    }

    public Map<String, Boolean> getIdsDao() {
        return idsDao;
    }
}