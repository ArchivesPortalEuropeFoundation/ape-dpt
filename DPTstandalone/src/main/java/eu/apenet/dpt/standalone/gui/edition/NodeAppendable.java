package eu.apenet.dpt.standalone.gui.edition;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: Oct 26, 2010
 *
 * @author Yoann Moranville
 */
public class NodeAppendable {
    public static final String XMLNS_EAD = "urn:isbn:1-931666-22-9";

    public static List<String> getActions(Node node, ResourceBundle labels){
        List<String> actions = new ArrayList<String>();
        String nodename = node.getNodeName();
        if(nodename.equals("eadid")) {
            NamedNodeMap map = node.getAttributes();
            if(map.getNamedItem("countrycode") == null)
                actions.add(labels.getString(ActionNamesEnum.INSERT_COUNTRYCODE.getBundleCode()));
            if(map.getNamedItem("mainagencycode") == null)
                actions.add(labels.getString(ActionNamesEnum.INSERT_MAINAGENCYCODE.getBundleCode()));
            if(node.getTextContent() == null || node.getTextContent().equals(""))
                actions.add(labels.getString(ActionNamesEnum.INSERT_TEXT.getBundleCode()));
        } else if(nodename.equals("langusage") || nodename.equals("langmaterial")){
            actions.add(labels.getString(ActionNamesEnum.INSERT_LANGUAGETAG.getBundleCode()));
        } else if(nodename.equals("unitdate") || nodename.equals("date")){
            NamedNodeMap map = node.getAttributes();
            if(map.getNamedItem("normal") == null)
                actions.add(labels.getString(ActionNamesEnum.INSERT_NORMALATTR.getBundleCode()));
        } else if(nodename.equals("eadheader")) {
            NodeList list = node.getChildNodes();
            boolean hasEadid = false;
            for(int i = 0; i < list.getLength(); i++) {
                Node child = list.item(i);
                if(child.getNodeName().equals("eadid")) {
                    hasEadid = true;
                }
            }
            if(!hasEadid) {
                actions.add(labels.getString(ActionNamesEnum.INSERT_EADID.getBundleCode()));
            }
        }
        return actions;
    }

}
