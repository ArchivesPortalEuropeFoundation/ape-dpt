package eu.apenet.dpt.standalone.gui.edition;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: Sep 24, 2010
 *
 * @author Yoann Moranville
 */
public class NodeEditable {
    private List<String> nodeEditable;

    public NodeEditable(){
        createList();
    }

    public List<String> getList(){
        return nodeEditable;
    }

    private void createList(){
        nodeEditable = new ArrayList<String>();
        nodeEditable.add("titleproper");
        nodeEditable.add("eadid");
        nodeEditable.add("level");
        nodeEditable.add("countrycode");
        nodeEditable.add("mainagencycode");
        nodeEditable.add("language");
        nodeEditable.add("normal");
    }
}
