package eu.apenet.dpt.standalone.gui.eag2012;

import eu.apenet.dpt.standalone.gui.eag2012.data.Eag;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 03/10/2012
 *
 * @author Yoann Moranville
 */
public abstract class EagPanels {
    private static final int NB_ROWS = 25;
    private static final String EDITOR_ROW = "p, 3dlu, ";
    protected static String EDITOR_ROW_SPEC;
    static {
        String temp = "";
        for(int i = 0; i < NB_ROWS; i++) {
            temp += EDITOR_ROW;
        }
        temp += "p";
        EDITOR_ROW_SPEC = temp;
    }

    protected final String[] languages = {"---", "eng", "fre"};
    protected final String[] continents = {"Africa", "Antarctica", "Asia", "Australia", "Europe", "North America", "South America"};
    protected final String[] yesOrNo = {"yes", "no"};

    protected int rowNb;
    protected JTabbedPane tabbedPane;
    protected Eag eag;
    protected JFrame eag2012Frame;
    protected SpecialTemporaryResourceBundle labels = new SpecialTemporaryResourceBundle();

    public EagPanels(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame) {
        this.eag2012Frame = eag2012Frame;
        this.eag = eag;
        this.tabbedPane = tabbedPane;
    }

    protected void closeFrame() {
        eag2012Frame.dispose();
        eag2012Frame.setVisible(false);
    }

    protected void setNextRow() {
        rowNb += 2;
    }

    protected abstract JComponent buildEditorPanel(List<String> errors);

    public class SpecialTemporaryResourceBundle {
        public String getString(String s) {
            return s.replace("eag2012.", "");
        }
    }

    protected class ExitBtnAction implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            closeFrame();
        }
    }
}
