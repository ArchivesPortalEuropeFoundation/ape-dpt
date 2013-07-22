package eu.apenet.dpt.standalone.gui.edition;

import eu.apenet.dpt.utils.util.LanguageIsoList;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: Oct 28, 2010
 *
 * @author Yoann Moranville
 */
public class CheckList {
    private List<String> langList = new ArrayList<String>();
    private List<String> levelList = new ArrayList<String>();

    public CheckList(){
        langList = LanguageIsoList.getLanguageIsoList();

        createLevelList();
    }

    private void createLevelList(){
        levelList.add("fonds");
        levelList.add("series");
        levelList.add("subseries");
        levelList.add("file");
        levelList.add("item");
    }

    public List<String> getLangList(){
        return langList;
    }

    public List<String> getLevelList(){
        return levelList;
    }
}
