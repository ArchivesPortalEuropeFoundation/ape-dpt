package eu.apenet.dpt.standalone.gui.eacCpf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.commons.ButtonTab;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.LanguageWithScript;
import eu.apenet.dpt.standalone.gui.db.RetrieveFromDb;

import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.Language;
import eu.apenet.dpt.utils.eaccpf.LanguageDeclaration;
import eu.apenet.dpt.utils.eaccpf.Script;

import eu.apenet.dpt.utils.util.XmlTypeEacCpf;



/**
 * Start tab Panel
 */

public class EacCpfStartPanel extends EacCpfPanel{
    protected static final Logger LOG = Logger.getLogger(EacCpfStartPanel.class);
    private LanguageWithScript languageWithScriptFirst;
    private JPanel radioBtnPanel;

  
    public EacCpfStartPanel(EacCpf eacCpf, JTabbedPane tabbedPane1, JTabbedPane mainTabbedPane, JFrame eacCpfFrame, ProfileListModel model, ResourceBundle labels) {
    	 super(eacCpf, tabbedPane1, mainTabbedPane, eacCpfFrame, model, labels);
    	 tabbedPane = new JTabbedPane();
         tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
         super.setTabbedPane(tabbedPane);
    }

    protected void closeFrame() {
    	this.eacCpfFrame.dispose();
    	this.eacCpfFrame.setVisible(false);
    	this.eacCpfFrame.setEnabled(false);
    }

    /**
     * Builds and answer the editor's tab for the given layout.
     * @return the editor's panel
     */
    protected JComponent buildEditorPanel(List<String> errors) {
        if(errors == null)
            errors = new ArrayList<String>(0);
        else if(Utilities.isDev && errors.size() > 0) {
            LOG.info("Errors in form:");
            for(String error : errors) {
                LOG.info(error);
            }
        }
        FormLayout layout = new FormLayout(
                "right:max(50dlu;p), 4dlu, 100dlu, 7dlu, right:p, 4dlu, right:max(100dlu;p)",
                EDITOR_ROW_SPEC);

        layout.setColumnGroups(new int[][] { { 1, 3, 5, 7 } });
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        LanguageDeclaration languageDeclaration = null;
        if(this.eaccpf.getControl().getLanguageDeclaration() == null) {
            languageDeclaration = new LanguageDeclaration();
            languageDeclaration.setLanguage(new Language());
            languageDeclaration.setScript(new Script());
            this.eaccpf.getControl().setLanguageDeclaration(languageDeclaration);
        }
        rowNb = 1;
        builder.addSeparator(labels.getString("eaccpf.start.title"), cc.xyw(1, rowNb, 7));
        this.setNextRow();
        // the radio button Panel 
        builder.addLabel(labels.getString("eaccpf.start.createInstance") + "*" + ":", cc.xy (1, rowNb));
        this.radioBtnPanel= new JPanel();
        builder.add(makeCheckboxesType(), cc.xyw(3, rowNb, 5));
        //language
        this.setNextRow();
        builder.addLabel(labels.getString("eaccpf.start.language"), cc.xy(1, rowNb));
        LanguageWithScript languageWithScript = new LanguageWithScript(languageDeclaration.getLanguage().getLanguageCode(), languageDeclaration.getScript().getScriptCode());
        this.languageWithScriptFirst = languageWithScript;
        builder.add(languageWithScript.getLanguageBox(), cc.xy (3, rowNb));
        this.setNextRow();
        builder.addLabel(labels.getString("eaccpf.start.text"), cc.xy(1, rowNb));
        //script
        this.setNextRow();
        builder.addLabel(labels.getString("eaccpf.start.script"), cc.xy(1, rowNb));
        builder.add(languageWithScript.getScriptBox(), cc.xy(3, rowNb));
        this.setNextRow();
        builder.addLabel(labels.getString("eaccpf.start.text"), cc.xy(1, rowNb));
        this.setNextRow();
        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        this.setNextRow();
        //exit button
        JButton exitBtn = new ButtonTab(labels.getString("eag2012.commons.exit"));
        builder.add(exitBtn, cc.xy(5, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());
        //Go button
        JButton goBtn = new ButtonTab(labels.getString("eaccpf.start.go"));
        goBtn.addActionListener(new GoBtnAction());
        builder.add(goBtn, cc.xy(7, rowNb));
        JScrollPane scrollPane;
        scrollPane = new JScrollPane(builder.getPanel());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        return scrollPane;
    }
    
    /**
     * Make the different radio button in the Panel: a person, a family, a corporate body
     * @return radioBtnPanel
     */
    private JComponent makeCheckboxesType() {
    	this.radioBtnPanel.setLayout(new BoxLayout(radioBtnPanel, BoxLayout.LINE_AXIS));
        JRadioButton radioButton;
        ButtonGroup groupRadio = new ButtonGroup();
        for(XmlTypeEacCpf xEnum : XmlTypeEacCpf.values()){
        	if (xEnum.getName().equals("person")){
        		radioButton = new JRadioButton(labels.getString("eaccpf.start.person"));
        	}else if (xEnum.getName().equals("family")){
        		radioButton = new JRadioButton(labels.getString("eaccpf.start.family"));
        	}else{
        		radioButton = new JRadioButton(labels.getString("eaccpf.start.corporateBody"));
        	}
            if(XmlTypeEacCpf.EAC_CPF_PERSON != null){
                radioButton.setSelected(true);
            }
    		radioButton.setName(xEnum.getName());
            radioButton.setVisible(true);
            groupRadio.add(radioButton);
           this.radioBtnPanel.add(radioButton);
           this.radioBtnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        }
        
        return this.radioBtnPanel;
    }

    /**
     * Action over the button Go in the panel
     */
     public class GoBtnAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// Recover the selected type in the radio button.
				XmlTypeEacCpf eacType = null; 
				Component[] radioComponents = radioBtnPanel.getComponents();
				if (radioComponents != null && radioComponents.length > 0) {
					boolean found = false;
					for (int i = 0; (i < radioComponents.length) && !found; i++) {
						Component component = radioComponents[i];
						if (component instanceof JRadioButton) {
							JRadioButton radioButton = (JRadioButton) component;
							if (radioButton.isSelected()) {
								found = true;
								String name = radioButton.getName();
								if (name.equalsIgnoreCase("person")){
									eacType = XmlTypeEacCpf.EAC_CPF_PERSON;
								}else if (name.equalsIgnoreCase("family")){
									eacType = XmlTypeEacCpf.EAC_CPF_FAMILY;
								}else{
									eacType = XmlTypeEacCpf.EAC_CPF_CORPORATEBODY;
								}
							}
						}
					}
				}

				// Recover the selected first language.
				String firstLanguage = (String) languageWithScriptFirst.getLanguageBox().getSelectedItem();
				// Recover the selected first script.
				String firstScript = (String) languageWithScriptFirst.getScriptBox().getSelectedItem();
				RetrieveFromDb retrieveFromDb = new RetrieveFromDb();
				new EacCpfFrame(tabbedPane.getSize(), model, labels, retrieveFromDb.retrieveCountryCode(), retrieveFromDb.retrieveRepositoryCode(), eacType, firstLanguage, firstScript);
			} catch (Exception e1) {
				 JOptionPane.showMessageDialog(tabbedPane, "Error...");
			}
		}
    	 
     }
}
