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
