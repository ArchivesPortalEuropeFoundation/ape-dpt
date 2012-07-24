package eu.apenet.dpt.standalone.gui;

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Yoann Moranville
 * Date: 04/04/2012
 *
 * @author Yoann Moranville
 */
public class TabItemActionListener implements ActionListener {
    private static final Logger LOG = Logger.getLogger(TabItemActionListener.class);
    private APEPanel apePanel;
    private int tab;

    public TabItemActionListener(APEPanel apePanel, int tab) {
        this.apePanel = apePanel;
        this.tab = tab;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        LOG.info(actionEvent.getActionCommand());
        apePanel.getApeTabbedPane().setSelectedIndex(tab);
        apePanel.getApeTabbedPane().changeBackgroundColor(tab, Utilities.TAB_COLOR);
    }
}
