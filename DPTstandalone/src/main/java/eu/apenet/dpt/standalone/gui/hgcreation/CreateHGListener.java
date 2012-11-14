package eu.apenet.dpt.standalone.gui.hgcreation;

import eu.apenet.dpt.standalone.gui.*;
import eu.apenet.dpt.standalone.gui.adhoc.QueryComponent;
import eu.apenet.dpt.standalone.gui.db.DBUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 15/03/2012
 *
 * @author Yoann Moranville
 */
public class CreateHGListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(CreateHGListener.class);
    
    private DBUtil dbUtil;
    private ResourceBundle labels;
    private Component parent;
    private JTree holdingsGuideTree;
    private JList listFilesForHG;
    private Map<String, FileInstance> fileInstances;
    private JList list;
    private DataPreparationToolGUI dataPreparationToolGUI;
    private JButton buttonGoDown;
    private JButton buttonGoUp;
    private JFrame createHGFrame;

    public CreateHGListener(DBUtil dbUtil, ResourceBundle labels, Component parent, Map<String, FileInstance> fileInstances, JList list, DataPreparationToolGUI dataPreparationToolGUI) {
        this.dbUtil = dbUtil;
        this.labels = labels;
        this.parent = parent;
        this.fileInstances = fileInstances;
        this.list = list;
        this.dataPreparationToolGUI = dataPreparationToolGUI;
        holdingsGuideTree = new JTree();
    }

    public void changeLanguage(ResourceBundle labels) {
        this.labels = labels;
    }
    
    public void actionPerformed(ActionEvent e){
        QueryComponent queryComponent = new QueryComponent(dbUtil, labels.getString("titleHg"), labels.getString("identifier"), labels.getString("idOfHGTooltip"));
        String title = "";
        String id = "";
        int result = JOptionPane.showConfirmDialog(parent, queryComponent.getMainPanel(), labels.getString("addInfo"), JOptionPane.INFORMATION_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            title = queryComponent.getEntryTitle();
            id = queryComponent.getEntryId();
        }
        final String title_f = title;
        final String id_f = id;

        createHGFrame = new JFrame(labels.getString("hgCreationFrame"));

        createHGFrame.setPreferredSize(new Dimension(parent.getWidth(), parent.getHeight())); //getContentPane()???

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new CLevelTreeObject(id_f, title_f));

        final LevelTreeActions levelTreeActions = new LevelTreeActions();
        LevelTreeModel levelTreeModel = new LevelTreeModel(rootNode);

        holdingsGuideTree.setModel(levelTreeModel);
        holdingsGuideTree.setEditable(false);

        JScrollPane hgScrollPane = new JScrollPane(holdingsGuideTree);
        hgScrollPane.setMinimumSize(new Dimension(200, parent.getHeight()));

        JPanel buttonsPanel = new JPanel();
        buttonGoUp = new JButton(labels.getString("upBtn"));
        buttonsPanel.add(buttonGoUp);
        buttonGoDown = new JButton(labels.getString("downBtn"));
        buttonsPanel.add(buttonGoDown);
        buttonsPanel.setPreferredSize(new Dimension(100, parent.getHeight()));

        listFilesForHG = new JList(new ProfileListModel(fileInstances, dataPreparationToolGUI));
        JScrollPane paneListFilesHG = new JScrollPane(listFilesForHG);
        paneListFilesHG.setPreferredSize(new Dimension(parent.getWidth()/4, parent.getHeight())); //getContentPane

        for(Object selectedValue : list.getSelectedValues()){
            ((ProfileListModel)listFilesForHG.getModel()).addFile((File)selectedValue);
        }

        listFilesForHG.setCellRenderer(new IconListCellRenderer(fileInstances));

        createHGFrame.add(paneListFilesHG, BorderLayout.EAST);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buttonsPanel, hgScrollPane);
        createHGFrame.add(splitPane, BorderLayout.CENTER);

        TreeDragSource treeDragSource = new TreeDragSource(holdingsGuideTree, DnDConstants.ACTION_COPY_OR_MOVE);
        TreeDropTarget treeDropTarget = new TreeDropTarget(holdingsGuideTree, listFilesForHG);

        holdingsGuideTree.setDragEnabled(false);
        listFilesForHG.setDragEnabled(true);

        JPanel buttonsSouthPanel = new JPanel();
        JButton buttonSaveHg = new JButton(labels.getString("saveBtn"));
        buttonsSouthPanel.add(buttonSaveHg, BorderLayout.EAST);
        final JButton buttonLoadHg = new JButton(labels.getString("loadBtn"));
        buttonsSouthPanel.add(buttonLoadHg, BorderLayout.WEST);

        File hgSavedFile = new File(Utilities.HG_TREE_SER_FILE_PATH);
        if(!hgSavedFile.exists()){
            buttonLoadHg.setEnabled(false);
        }

        createHGFrame.add(buttonsSouthPanel, BorderLayout.SOUTH);

        createHGFrame.pack();
        createHGFrame.setVisible(true);

        buttonGoUp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                MutableTreeNode node = (MutableTreeNode) holdingsGuideTree.getSelectionPath().getLastPathComponent();
                int index;
                if(node.getParent() != null){
                    MutableTreeNode parentNode = (MutableTreeNode) node.getParent();
                    if((index = parentNode.getIndex(node)) != 0) {
                        TreePath pp = holdingsGuideTree.getSelectionPath();
                        ((LevelTreeModel) holdingsGuideTree.getModel()).removeNodeFromParent(node);
                        ((LevelTreeModel) holdingsGuideTree.getModel()).insertNodeInto(node, parentNode, index -1);
                        holdingsGuideTree.setSelectionPath(pp);
                    }
                }
            }
        });
        buttonGoDown.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                MutableTreeNode node = (MutableTreeNode) holdingsGuideTree.getSelectionPath().getLastPathComponent();
                int index;
                if(node.getParent() != null){
                    MutableTreeNode parentNode = (MutableTreeNode) node.getParent();
                    if((index = parentNode.getIndex(node)) != (parentNode.getChildCount() - 1)) {
                        TreePath pp = holdingsGuideTree.getSelectionPath();
                        ((LevelTreeModel) holdingsGuideTree.getModel()).removeNodeFromParent(node);
                        ((LevelTreeModel) holdingsGuideTree.getModel()).insertNodeInto(node, parentNode, index +1);
                        holdingsGuideTree.setSelectionPath(pp);
                    }
                }
            }
        });
        buttonSaveHg.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Utilities.HG_TREE_SER_FILE_PATH));
                    oos.writeObject(holdingsGuideTree.getModel());
                    oos.close();
                } catch (Exception ex) {
                    LOG.error("oups", ex);}

                dataPreparationToolGUI.setResultAreaText(labels.getString("creatingHG"));

                createHGFrame.setVisible(false);
                createHGFrame.dispose();

                new Thread(new Runnable(){
                    public void run(){
                        SummaryWorking summaryWorking = new SummaryWorking(dataPreparationToolGUI.getResultArea());
                        Thread summaryThread = new Thread(summaryWorking);
                        summaryThread.setName(SummaryWorking.class.getName());
                        summaryThread.start();

                        File hgFile = levelTreeActions.createXML(holdingsGuideTree.getModel(), title_f, id_f, dataPreparationToolGUI.getParams(), dataPreparationToolGUI.getCountryCode(), dataPreparationToolGUI.getRepositoryCodeIdentifier());
                        ProfileListModel model = ((ProfileListModel)list.getModel());
                        if(model.existsFile(hgFile))
                            model.removeFile(hgFile);
                        model.addFile(hgFile);
                        fileInstances.get(hgFile.getName()).setCurrentLocation(hgFile.getPath());
                        fileInstances.get(hgFile.getName()).setLastOperation(FileInstance.Operation.CONVERT);
                        fileInstances.get(hgFile.getName()).setIsConverted();
                        fileInstances.get(hgFile.getName()).setFileType(FileInstance.FileType.EAD);
//                        list.clearSelection();

                        summaryWorking.stop();
                        dataPreparationToolGUI.setResultAreaText(labels.getString("createHGDone"));
                    }
                }).start();
            }
        });
        buttonLoadHg.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    File hgSavedFile = new File(Utilities.HG_TREE_SER_FILE_PATH);
                    if(hgSavedFile.exists()){
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(hgSavedFile));
                        holdingsGuideTree.setModel((LevelTreeModel)ois.readObject());
                        ois.close();
                    }
                } catch (Exception ex){LOG.error("oups", ex);}
            }
        });

        HoldingsGuideTreeMouseListener holdingsGuideTreeMouseListener = new HoldingsGuideTreeMouseListener();
        holdingsGuideTree.addMouseListener(holdingsGuideTreeMouseListener);
        holdingsGuideTreeMouseListener.doActionEditLevel(rootNode);
    }

    public class HoldingsGuideTreeMouseListener implements MouseListener {
        public void mousePressed(MouseEvent e) {
            linkClicks(e);
        }

        public void mouseReleased(MouseEvent e) {
            linkClicks(e);
        }
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}

        private void linkClicks(MouseEvent e){
            if(e.isPopupTrigger()){
                TreePath selPath = holdingsGuideTree.getPathForLocation(e.getX(), e.getY());
                if(selPath != null){
                    holdingsGuideTree.getSelectionModel().clearSelection();
                    holdingsGuideTree.getSelectionModel().addSelectionPath(selPath);
                }

                JPopupMenu popupMenu = createPopupHGActions(holdingsGuideTree.getSelectionModel().getSelectionPath());

                // Only show popup if we have any menu items in it
                if(popupMenu.getComponentCount() > 0)
                    popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
            } else {
                TreePath selPath = holdingsGuideTree.getSelectionPath();
                if(selPath == null || selPath.getLastPathComponent() == null || ((DefaultMutableTreeNode)selPath.getLastPathComponent()).getParent() == null){
                    buttonGoUp.setEnabled(false);
                    buttonGoDown.setEnabled(false);
                } else {
                    if( ((DefaultMutableTreeNode) ( (DefaultMutableTreeNode)selPath.getLastPathComponent() ).getParent()).getFirstChild() == (DefaultMutableTreeNode)selPath.getLastPathComponent()){
                        buttonGoUp.setEnabled(false);
                    } else {
                        buttonGoUp.setEnabled(true);
                    }
                    if( ((DefaultMutableTreeNode) ( (DefaultMutableTreeNode)selPath.getLastPathComponent() ).getParent()).getLastChild() == (DefaultMutableTreeNode)selPath.getLastPathComponent()){
                        buttonGoDown.setEnabled(false);
                    } else {
                        buttonGoDown.setEnabled(true);
                    }
                }
            }

        }

        private JPopupMenu createPopupHGActions(final TreePath path) {
            JPopupMenu pm = new JPopupMenu();
            Object node = path.getLastPathComponent();
            if(node instanceof DefaultMutableTreeNode){
                final DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) node;

                JMenuItem j1 = new JMenuItem(node.toString());
                j1.setEnabled(false);
                pm.add(j1);
                pm.addSeparator();

                JMenuItem menuItem;
                if(!((CLevelTreeObject)defaultMutableTreeNode.getUserObject()).isFile()){
                    menuItem = new JMenuItem(labels.getString("addLevel"));
                    menuItem.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            doActionAddLevel(defaultMutableTreeNode, path);
                        }
                    });
                    pm.add(menuItem);

                    menuItem = new JMenuItem(labels.getString("editLevel"));
                    menuItem.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            doActionEditLevel(defaultMutableTreeNode);
                        }
                    });
                    pm.add(menuItem);
                }

                menuItem = new JMenuItem(labels.getString("removeLevel"));
                menuItem.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        doActionRemoveLevel(defaultMutableTreeNode);
                    }
                });
                pm.add(menuItem);
            }
            return pm;
        }
        public void doActionAddLevel(final DefaultMutableTreeNode cLevelTreeObject, TreePath path){
            try {
                if(!((CLevelTreeObject)cLevelTreeObject.getUserObject()).isFile()){
                    DefaultMutableTreeNode treenode = new DefaultMutableTreeNode(new CLevelTreeObject("[" + labels.getString("changeTitle") + "]"));
                    ((LevelTreeModel) holdingsGuideTree.getModel()).insertNodeInto(treenode, cLevelTreeObject, cLevelTreeObject.getChildCount());
                    holdingsGuideTree.fireTreeExpanded(path);
                    doActionEditLevel(treenode);
                }
            } catch (Exception e){
                LOG.error("Error", e);
            }
        }
        public void doActionEditLevel(final DefaultMutableTreeNode cLevelTreeObject){
            final CLevelTreeObject obj = (CLevelTreeObject)cLevelTreeObject.getUserObject();

            final JFrame editLevelFrame = new JFrame(labels.getString("edit"));
            editLevelFrame.setAlwaysOnTop(true);
            editLevelFrame.setPreferredSize(new Dimension(parent.getWidth() * 3/4, parent.getHeight() * 3/4)); //getContentPane
            editLevelFrame.setLocation((parent.getWidth() - parent.getWidth() * 3/4)/2, (parent.getHeight() - parent.getHeight() * 3/4)/2);

            JPanel editLevelAllPanel = (JPanel)editLevelFrame.getContentPane();
            editLevelAllPanel.setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel(new BorderLayout());
            JPanel editLevelMainPanel = new JPanel(new GridLayout(3, 2));

            editLevelAllPanel.add(buttonPanel, BorderLayout.SOUTH);
            editLevelAllPanel.add(editLevelMainPanel, BorderLayout.CENTER);

            JButton saveButton = new JButton(labels.getString("saveBtn"));
            buttonPanel.add(saveButton, BorderLayout.EAST);

            JLabel idLevel = new JLabel(labels.getString("identifier") + " [unitid]: ");

            final JTextField idLevelText = new JTextField(obj.getId());

            final JLabel nameLevel = new JLabel(labels.getString("name") + " [unittitle]: ");

            final JTextField nameLevelText = new JTextField(obj.getName());

            JLabel descLevel = new JLabel(labels.getString("description") + " [scopecontent]: ");
            descLevel.setHorizontalAlignment(SwingConstants.LEFT);

            final JTextField descLevelText = new JTextField(obj.getDescription());

            editLevelMainPanel.add(idLevel);
            editLevelMainPanel.add(idLevelText);
            editLevelMainPanel.add(nameLevel);
            editLevelMainPanel.add(nameLevelText);
            editLevelMainPanel.add(descLevel);
            editLevelMainPanel.add(descLevelText);

            saveButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    if(!nameLevelText.getText().equals("") && !nameLevelText.getText().equals("[" + labels.getString("changeTitle") + "]")){
                        obj.setId(idLevelText.getText());
                        obj.setName(nameLevelText.getText());
                        obj.setDescription(descLevelText.getText());
                        editLevelFrame.dispose();
                        ((LevelTreeModel) holdingsGuideTree.getModel()).reload(cLevelTreeObject);
                    } else {
                        nameLevelText.setText("[" + labels.getString("changeTitle") + "]");
                        if(!nameLevel.getText().endsWith("(" + labels.getString("mandatory") + ")"))
                            nameLevel.setText(nameLevel.getText() + " (" + labels.getString("mandatory") + ")");
                    }
                }
            });

            editLevelFrame.pack();
            editLevelFrame.setVisible(true);
        }
        public void doActionRemoveLevel(final DefaultMutableTreeNode defaultMutableTreeNode){
            LOG.trace("try remove child");
            if(defaultMutableTreeNode.getParent() != null){
                if(((CLevelTreeObject)defaultMutableTreeNode.getUserObject()).getFile() != null)
                    ((ProfileListModel)listFilesForHG.getModel()).addFile(((CLevelTreeObject)defaultMutableTreeNode.getUserObject()).getFile());
                ((LevelTreeModel) holdingsGuideTree.getModel()).removeNodeFromParent(defaultMutableTreeNode);
                LOG.trace("child removed");
            }
        }
    }
}