package eu.apenet.dpt.standalone.gui.xsdaddition;

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
