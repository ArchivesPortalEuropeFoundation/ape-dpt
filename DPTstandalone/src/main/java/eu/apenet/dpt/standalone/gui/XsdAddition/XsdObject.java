package eu.apenet.dpt.standalone.gui.XsdAddition;

import eu.apenet.dpt.standalone.gui.FileInstance;

/**
 * User: Yoann Moranville
 * Date: 07/02/2013
 *
 * @author Yoann Moranville
 */
public class XsdObject {
    private int id;
    private String name;
    private String path;
    private boolean isXsd11;
    private boolean isSystem;
    private FileInstance.FileType fileType;

    public XsdObject(int id, String name, String path, int isXsd11, int isSystem, String fileType) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isXsd11 = isXsd11 != 0;
        this.isSystem = isSystem != 0;
        this.fileType = FileInstance.FileType.getCorrectFileType(fileType);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isXsd11() {
        return isXsd11;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public FileInstance.FileType getFileType() {
        return fileType;
    }
}
