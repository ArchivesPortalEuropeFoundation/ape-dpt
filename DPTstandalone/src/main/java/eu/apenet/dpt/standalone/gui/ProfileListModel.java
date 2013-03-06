package eu.apenet.dpt.standalone.gui;

import eu.apenet.dpt.standalone.gui.xsdAddition.XsdObject;

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
        FileInstance fileInstance = new FileInstance(file);
        fileInstances.put(file.getName(), fileInstance);
        listFiles.add(file);
        fireIntervalAdded(this, 1, 1);
    }

    public void addFile(File file, XsdObject xsdObject, FileInstance.FileType fileType){
        FileInstance fileInstance = new FileInstance(file);
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
}