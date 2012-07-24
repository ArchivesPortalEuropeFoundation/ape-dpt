package eu.apenet.dpt.standalone.gui.hgcreation;

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
