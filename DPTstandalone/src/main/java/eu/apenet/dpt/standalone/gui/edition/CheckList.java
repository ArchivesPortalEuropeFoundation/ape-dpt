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
