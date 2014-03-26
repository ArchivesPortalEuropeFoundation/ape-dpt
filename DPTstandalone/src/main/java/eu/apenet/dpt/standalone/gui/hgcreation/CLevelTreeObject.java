package eu.apenet.dpt.standalone.gui.hgcreation;

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

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

/**
 * User: Yoann Moranville
 * Date: Feb 14, 2011
 *
 * @author Yoann Moranville
 */
public class CLevelTreeObject extends DefaultMutableTreeNode {
    private String id;
    private String name;
    private String description;
    private File file;

    public CLevelTreeObject(){
        super("");
        this.name = "";
        this.id = "";
        this.description = "";
    }

    public CLevelTreeObject(String name){
        super("[" + name + "]");
        this.name = name;
        this.id = "";
        this.description = "";
    }

    public CLevelTreeObject(String id, String name){
        super("[" + name + "]");
        this.name = name;
        this.id = id;
        this.description = "";
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFile(File file){
        this.file = file;
    }

    public boolean isFile(){
        return file != null;
    }

    public File getFile(){
        if(isFile())
            return file;
        return null;
    }

    public String toString(){
        String idString = "";
        if(id != null && !id.equals(""))
            idString += "[" + id + "] ";

        if(description != null && !description.equals("")){
            if(description.length() > 15)
                return idString + name + " - " + description.substring(0, 14) + "...";
            return idString + name + " - " + description;
        }
        return idString + name;
    }

    public boolean equals(Object obj){
        return obj instanceof CLevelTreeObject && this == obj;
    }
}
