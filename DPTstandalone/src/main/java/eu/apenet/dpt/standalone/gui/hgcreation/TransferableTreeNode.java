package eu.apenet.dpt.standalone.gui.hgcreation;

import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * User: Yoann Moranville
 * Date: Feb 15, 2011
 *
 * @author Yoann Moranville
 */
public class TransferableTreeNode implements Transferable {

    public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class, "Tree Path");

    DataFlavor flavors[] = { TREE_PATH_FLAVOR };

    TreePath path;

    public TransferableTreeNode(TreePath tp) {
        path = tp;
    }

    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return (flavor.getRepresentationClass() == TreePath.class);
    }

    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(flavor)) {
            return (Object) path;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}