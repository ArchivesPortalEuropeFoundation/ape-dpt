package eu.apenet.dpt.standalone.gui;

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

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;
import eu.apenet.dpt.standalone.gui.xsdaddition.XsdObject;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: 15/03/2012
 *
 * @author Yoann Moranville
 */
public class ProfileListModel extends AbstractListModel {
    private static final long serialVersionUID = 939393939;
    private List<File> listFiles;
    private Map<String, FileInstance> fileInstances;
    private JList list;
    private DataPreparationToolGUI dataPreparationToolGUI;

    public ProfileListModel(Map<String, FileInstance> fileInstances, DataPreparationToolGUI dataPreparationToolGUI) {
        listFiles = new ArrayList<File>();
        this.fileInstances = fileInstances;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
    }

    public void setList(JList list) {
        this.list = list;
    }

    public int getSize() {
        return listFiles.size();
    }

    public Object getElementAt(int index) {
        return listFiles.get(index);
    }

    public void addFile(File file){
        RetrieveFromDb retrieveFromDb = new RetrieveFromDb();
        FileInstance fileInstance = new FileInstance(file, retrieveFromDb.retrieveDefaultXsl(), retrieveFromDb.retrieveDefaultXsd());
        fileInstances.put(file.getName(), fileInstance);
        listFiles.add(file);
        fireIntervalAdded(this, 1, 1);
    }

    public void addFile(File file, FileInstance fileInstanceToCopy){
        FileInstance fileInstance = fileInstanceToCopy;
        fileInstances.put(file.getName(), fileInstance);
        listFiles.add(file);
        fireIntervalAdded(this, 1, 1);
    }

    public void addFileInstance(FileInstance fileInstanceToAdd, File fileToAdd) {
        fileInstances.put(fileInstanceToAdd.getName(), fileInstanceToAdd);
        listFiles.add(fileToAdd);
        fireIntervalAdded(this, 1, 1);
    }

    public void addFile(File file, XsdObject xsdObject, FileInstance.FileType fileType){
        RetrieveFromDb retrieveFromDb = new RetrieveFromDb();
        FileInstance fileInstance = new FileInstance(file, retrieveFromDb.retrieveDefaultXsl(), retrieveFromDb.retrieveDefaultXsd());
        fileInstance.setValidationSchema(xsdObject);
        fileInstance.setFileType(fileType);
        fileInstances.put(file.getName(), fileInstance);
        listFiles.add(file);
        fireIntervalAdded(this, 1, 1);
    }

    public void removeFile(File file) {
        if(list == null)
            throw new NullPointerException("list is null, list can not be null when using the model for a JList");
        if(file != null) {
            fileInstances.remove(file.getName());
            listFiles.remove(file);
            list.clearSelection();
            dataPreparationToolGUI.disableAllBtnAndItems();
            dataPreparationToolGUI.getAPEPanel().setFilename("");
            fireIntervalAdded(this, 1, 1);
        }
    }

    public boolean existsFile(File file) {
        if(list == null)
            throw new NullPointerException("list is null, list can not be null when using the model for a JList");
        return file != null && fileInstances.containsKey(file.getName());
    }

    public void removeFiles(Object[] selectedObjects){
        for(Object object : selectedObjects)
            removeFile((File)object);
    }

    public void removeElementAt(int index){
        listFiles.remove(index);
        fireIntervalAdded(this, index, index);
    }

    public void insertElementAt(File element, int index){
        listFiles.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    public void fireContentsChanged(Object o, int i, int j) {
        super.fireContentsChanged(o, i, j);
    }
}
