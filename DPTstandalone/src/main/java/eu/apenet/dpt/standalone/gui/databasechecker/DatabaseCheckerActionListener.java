package eu.apenet.dpt.standalone.gui.databaseChecker;

import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Yoann Moranville
 * Date: 07/02/2013
 *
 * @author Yoann Moranville
 */
public class DatabaseCheckerActionListener implements ActionListener {
    private RetrieveFromDb retrieveFromDb;
    private Container contentPane;

    public DatabaseCheckerActionListener(RetrieveFromDb retrieveFromDb, Container contentPane) {
        this.retrieveFromDb = retrieveFromDb;
        this.contentPane = contentPane;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        JFrame databaseCheckerFrame = new DatabaseCheckerFrame(retrieveFromDb);
        Dimension contentPaneDimensions = contentPane.getSize();
        databaseCheckerFrame.setPreferredSize(new Dimension(new Double(contentPaneDimensions.getWidth() * 3 / 4).intValue(), new Double(contentPaneDimensions.getHeight() * 3 / 4).intValue()));
        databaseCheckerFrame.setLocation(new Double(contentPaneDimensions.getWidth() / 8).intValue(), new Double(contentPaneDimensions.getHeight() / 8).intValue());

        databaseCheckerFrame.pack();
        databaseCheckerFrame.setVisible(true);
    }
}
