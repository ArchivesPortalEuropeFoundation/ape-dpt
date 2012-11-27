package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 27/11/2012
 *
 * @author Yoann Moranville
 */
public class EagContactPanel extends EagPanels {
    private JTextField streetTf;
    private JTextField cityTf;
    private JTextField countryTf;
    private JTextField coordinatesLatTf;
    private JTextField coordinatesLongTf;

    private JTextField streetPTf;
    private JTextField cityPTf;
    private JTextField countryPTf;
    private JTextField coordinatesLatPTf;
    private JTextField coordinatesLongPTf;
    private JTextField telephoneTf;

    private JComboBox languageBoxStreet = new JComboBox(languages);
    private JComboBox languageBoxCity = new JComboBox(languages);
    private JComboBox languageBoxCountry = new JComboBox(languages);

    public EagContactPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame) {
        super(eag, tabbedPane, eag2012Frame);
    }

    @Override
    protected JComponent buildEditorPanel(List<String> errors) {
        if(errors == null)
            errors = new ArrayList<String>(0);

        FormLayout layout = new FormLayout(
                "right:max(50dlu;p), 4dlu, 100dlu, 7dlu, right:p, 4dlu, 100dlu",
                EDITOR_ROW_SPEC);

        layout.setColumnGroups(new int[][] { { 1, 3, 5, 7 } });
        PanelBuilder builder = new PanelBuilder(layout);

        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        rowNb = 1;

        Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
        for(int i = 0; i < repository.getLocation().size(); i++) {
            boolean isPostal = false;

            if(i == 2)
                break;
            Location location = repository.getLocation().get(i);
            if(StringUtils.isEmpty(location.getLocalType())) {
                location.setLocalType("visitors address");
            }
            if(location.getLocalType().equals("visitors address")) {
                builder.addSeparator(labels.getString("eag2012.visitorsAddress"), cc.xyw(1, rowNb, 7));
                isPostal = false;
            } else if (location.getLocalType().equals("postal address")) {
                builder.addSeparator(labels.getString("eag2012.postalAddress"), cc.xyw(1, rowNb, 7));
                isPostal = true;
            }
            setNextRow();

            builder.addLabel(labels.getString("eag2012.streetLabel"),    cc.xy (1, rowNb));
            if(StringUtils.isNotEmpty(location.getStreet().getContent())) {
                streetTf = new JTextField(location.getStreet().getContent());
                streetPTf = new JTextField(location.getStreet().getContent());
            } else {
                streetTf = new JTextField();
                streetPTf = new JTextField();
            }
            if(!isPostal)
                builder.add(streetTf,                               cc.xy (3, rowNb));
            else
                builder.add(streetPTf,                               cc.xy (3, rowNb));

            builder.addLabel(labels.getString("eag2012.languageLabel"), cc.xy (5, rowNb));
            if(StringUtils.isNotEmpty(location.getStreet().getLang())) {
                languageBoxStreet.setSelectedItem(location.getStreet().getLang());
            }
            builder.add(languageBoxStreet,                                    cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.cityTownLabel"),    cc.xy (1, rowNb));
            if(StringUtils.isNotEmpty(location.getMunicipalityPostalcode().getContent())) {
                cityTf = new JTextField(location.getMunicipalityPostalcode().getContent());
            } else {
                cityTf = new JTextField();
            }
            builder.add(cityTf,                               cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.languageLabel"),             cc.xy (5, rowNb));
            if(StringUtils.isNotEmpty(location.getMunicipalityPostalcode().getLang())) {
                languageBoxCity.setSelectedItem(location.getMunicipalityPostalcode().getLang());
            }
            builder.add(languageBoxCity,                                            cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.countryLabel"),    cc.xy (1, rowNb));
            if(StringUtils.isNotEmpty(location.getCountry().getContent())) {
                countryTf = new JTextField(location.getCountry().getContent());
            } else {
                countryTf = new JTextField();
            }
            builder.add(countryTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.languageLabel"),             cc.xy (5, rowNb));
            if(StringUtils.isNotEmpty(location.getCountry().getLang())) {
                languageBoxCountry.setSelectedItem(location.getCountry().getLang());
            }
            builder.add(languageBoxCountry,                                            cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.coordinatesLatitudeLabel"),    cc.xy (1, rowNb));
            coordinatesLatTf = new JTextField(location.getLatitude());
            builder.add(coordinatesLatTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.coordinatesLongitudeLabel"), cc.xy(5, rowNb));
            coordinatesLongTf = new JTextField(location.getLongitude());
            builder.add(coordinatesLongTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }

        JButton exitBtn = new JButton(labels.getString("eag2012.exitButton"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton nextTabBtn = new JButton(labels.getString("eag2012.nextTabButton"));
        builder.add(nextTabBtn, cc.xy (3, rowNb));
        nextTabBtn.addActionListener(new NextTabBtnAction(eag, tabbedPane));

        return builder.getPanel();
    }

    public class NextTabBtnAction extends UpdateEagObject {
        NextTabBtnAction(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();

//                reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, eag2012Frame).buildEditorPanel(errors), 3);

                tabbedPane.setEnabledAt(3, true);
                tabbedPane.setEnabledAt(2, false);
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame).buildEditorPanel(errors), 2);
            }
        }
    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        public UpdateEagObject(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }

        @Override
        protected void updateEagObject() throws Eag2012FormException {
            errors = new ArrayList<String>();

            boolean hasChanged = false;

            //todo

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}
