package eu.apenet.dpt.standalone.gui;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

/**
 * User: Yoann Moranville
 * Date: 06/04/2012
 *
 * @author Yoann Moranville
 */
public class ListTransferHandler extends TransferHandler {
    private static final long serialVersionUID = -5675798406258610531L;
    private static final Logger LOG = Logger.getLogger(ListTransferHandler.class);

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JList list = (JList)c;
        final Object value = list.getSelectedValue();
        return new Transferable(){
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{new DataFlavor(File.class,"File")};
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor.match(new DataFlavor(File.class,"File"));
            }

            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return value;
            }
        };
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        JList table = (JList) comp;

        Point p = comp.getMousePosition();

        int dstRow = table.locationToIndex(p);
        int srcRow;
        try {
            srcRow = Integer.parseInt((String) t.getTransferData(new DataFlavor(File.class, "File")));
        } catch (UnsupportedFlavorException e) {
            LOG.error("Error when parsing - moving file", e);
            return false;
        } catch (IOException e) {
            LOG.error("Error when parsing - moving file", e);
            return false;
        }

        ProfileListModel m = (ProfileListModel) table.getModel();

        if (dstRow < 0) {
            dstRow = 0;
        }
        if (dstRow > m.getSize() - 1) {
            dstRow = m.getSize() - 1;
        }

        m.insertElementAt((File)m.getElementAt(srcRow), dstRow);
        m.removeElementAt(srcRow);
        return true;
    }
}