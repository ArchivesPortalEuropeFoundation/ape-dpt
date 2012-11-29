package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<JTextField> telephoneTfs;
    private List<JTextField> faxTfs;
    private List<JTextField> emailTfs;
    private List<JTextField> emailTitleTfs;
    private List<JTextField> webpageTfs;
    private List<JTextField> webpageTitleTfs;

    private JComboBox continentCombo = new JComboBox(continents);

    public EagContactPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame, ProfileListModel model) {
        super(eag, tabbedPane, eag2012Frame, model);
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

//                builder.addLabel(labels.getString("eag2012.languageLabel"), cc.xy (5, rowNb));
//                if(StringUtils.isNotEmpty(location.getStreet().getLang())) {
//                    languageBoxStreet.setSelectedItem(location.getStreet().getLang());
//                }
//                builder.add(languageBoxStreet,                                    cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.cityTownLabel"),    cc.xy (1, rowNb));
            if(StringUtils.isNotEmpty(location.getMunicipalityPostalcode().getContent())) {
                cityTf = new JTextField(location.getMunicipalityPostalcode().getContent());
                cityPTf = new JTextField(location.getMunicipalityPostalcode().getContent());
            } else {
                cityTf = new JTextField();
                cityPTf = new JTextField();
            }
            if(!isPostal)
                builder.add(cityTf,                               cc.xy (3, rowNb));
            else
                builder.add(cityPTf,                               cc.xy (3, rowNb));

//                builder.addLabel(labels.getString("eag2012.languageLabel"),             cc.xy (5, rowNb));
//                if(StringUtils.isNotEmpty(location.getMunicipalityPostalcode().getLang())) {
//                    languageBoxCity.setSelectedItem(location.getMunicipalityPostalcode().getLang());
//                }
//                builder.add(languageBoxCity,                                            cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.countryLabel"),    cc.xy (1, rowNb));
            if(StringUtils.isNotEmpty(location.getCountry().getContent())) {
                countryTf = new JTextField(location.getCountry().getContent());
                countryPTf = new JTextField(location.getCountry().getContent());
            } else {
                countryTf = new JTextField();
                countryPTf = new JTextField();
            }
            if(!isPostal)
                builder.add(countryTf, cc.xy (3, rowNb));
            else
                builder.add(countryPTf, cc.xy(3, rowNb));
//                builder.addLabel(labels.getString("eag2012.languageLabel"),             cc.xy (5, rowNb));
//                if(StringUtils.isNotEmpty(location.getCountry().getLang())) {
//                    languageBoxCountry.setSelectedItem(location.getCountry().getLang());
//                }
//                builder.add(languageBoxCountry,                                            cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.coordinatesLatitudeLabel"),    cc.xy (1, rowNb));
            coordinatesLatTf = new JTextField(location.getLatitude());
            coordinatesLatPTf = new JTextField(location.getLatitude());
            if(isPostal)
                builder.add(coordinatesLatTf, cc.xy (3, rowNb));
            else
                builder.add(coordinatesLatPTf, cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.coordinatesLongitudeLabel"), cc.xy(5, rowNb));
            coordinatesLongTf = new JTextField(location.getLongitude());
            coordinatesLongPTf = new JTextField(location.getLongitude());
            if(isPostal)
                builder.add(coordinatesLongTf, cc.xy (7, rowNb));
            else
                builder.add(coordinatesLongPTf, cc.xy (7, rowNb));
            setNextRow();
        }

        if(repository.getLocation().size() < 2) { //If equal or more than 2, we already have visitors and postal addresses
            JButton addNewPostalAddressBtn = new JButton(labels.getString("eag2012.addPostalAddress"));
            addNewPostalAddressBtn.addActionListener(new AddPostalAddressAction(eag, tabbedPane, model));
            builder.add(addNewPostalAddressBtn, cc.xy(3, rowNb));
            setNextRow();
        }

        builder.addLabel(labels.getString("eag2012.continentLabel"),    cc.xy (1, rowNb));
        if(Arrays.asList(continents).contains(repository.getGeogarea().getValue())){
            continentCombo.setSelectedItem(repository.getGeogarea().getValue());
        }
        builder.add(continentCombo, cc.xy (3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.telephoneLabel"), cc.xy(1, rowNb));
        int i = 0;
        telephoneTfs = new ArrayList<JTextField>(repository.getTelephone().size());
        for(Telephone telephone : repository.getTelephone()) {
            JTextField telephoneTf = new JTextField(telephone.getContent());
            telephoneTfs.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            if(i++ == 0) {
                JButton addTelephoneBtn = new JButton(labels.getString("eag2012.addTelephoneNumbers"));
                addTelephoneBtn.addActionListener(new AddTelephoneAction(eag, tabbedPane, model));
                builder.add(addTelephoneBtn, cc.xy(5, rowNb));
            }
            setNextRow();
        }
        if(repository.getTelephone().size() == 0) {
            JTextField telephoneTf = new JTextField();
            telephoneTfs.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            setNextRow();
        }

        builder.addLabel(labels.getString("eag2012.faxLabel"), cc.xy(1, rowNb));
        i = 0;
        faxTfs = new ArrayList<JTextField>(repository.getFax().size());
        for(Fax fax : repository.getFax()) {
            JTextField faxTf = new JTextField(fax.getContent());
            faxTfs.add(faxTf);
            builder.add(faxTf, cc.xy (3, rowNb));
            if(i++ == 0) {
                JButton addFaxBtn = new JButton(labels.getString("eag2012.addFaxNumbers"));
                addFaxBtn.addActionListener(new AddFaxAction(eag, tabbedPane, model));
                builder.add(addFaxBtn, cc.xy(5, rowNb));
            }
            setNextRow();
        }
        if(repository.getFax().size() == 0) {
            JTextField faxTf = new JTextField();
            faxTfs.add(faxTf);
            builder.add(faxTf, cc.xy (3, rowNb));
            setNextRow();
        }

        emailTfs = new ArrayList<JTextField>(repository.getEmail().size());
        emailTitleTfs = new ArrayList<JTextField>(repository.getEmail().size());
        if(repository.getEmail().size() == 0)
            repository.getEmail().add(new Email());
        for(Email email : repository.getEmail()) {
            JTextField emailTf = new JTextField(email.getHref());
            JTextField emailTitleTf = new JTextField(email.getContent());
            emailTfs.add(emailTf);
            emailTitleTfs.add(emailTitleTf);
            builder.addLabel(labels.getString("eag2012.emailLabel"),    cc.xy (1, rowNb));
            builder.add(emailTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkTitleLabel"),             cc.xy (5, rowNb));
            builder.add(emailTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addEmailBtn = new JButton(labels.getString("eag2012.addEmail"));
        addEmailBtn.addActionListener(new AddEmailAction(eag, tabbedPane, model));
        builder.add(addEmailBtn, cc.xy(1, rowNb));
        setNextRow();

        webpageTfs = new ArrayList<JTextField>(repository.getWebpage().size());
        webpageTitleTfs = new ArrayList<JTextField>(repository.getWebpage().size());
        if(repository.getWebpage().size() == 0)
            repository.getWebpage().add(new Webpage());
        for(Webpage webpage : repository.getWebpage()) {
            JTextField webpageTf = new JTextField(webpage.getHref());
            JTextField webpageTitleTf = new JTextField(webpage.getContent());
            webpageTfs.add(webpageTf);
            webpageTitleTfs.add(webpageTitleTf);
            builder.addLabel(labels.getString("eag2012.webpageLabel"),    cc.xy (1, rowNb));
            builder.add(webpageTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkTitleLabel"),             cc.xy (5, rowNb));
            builder.add(webpageTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addWebpageBtn = new JButton(labels.getString("eag2012.addWebpage"));
        addWebpageBtn.addActionListener(new AddWebpageAction(eag, tabbedPane, model));
        builder.add(addWebpageBtn, cc.xy(1, rowNb));
        setNextRow();

        JButton exitBtn = new JButton(labels.getString("eag2012.exitButton"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton previousTabBtn = new JButton(labels.getString("eag2012.previousTabButton"));
        builder.add(previousTabBtn, cc.xy (3, rowNb));
        previousTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, false));

        JButton nextTabBtn = new JButton(labels.getString("eag2012.nextTabButton"));
        builder.add(nextTabBtn, cc.xy (5, rowNb));
        nextTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, true));

        return builder.getPanel();
    }

    public class ChangeTabBtnAction extends UpdateEagObject {
        private boolean isNextTab;
        ChangeTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model, boolean isNextTab) {
            super(eag, tabbedPane, model);
            this.isNextTab = isNextTab;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();

                if(isNextTab) {
                    reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 3);
                    tabbedPane.setEnabledAt(3, true);
                    tabbedPane.setEnabledAt(2, false);
                } else {
                    reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 1);
                    tabbedPane.setEnabledAt(1, true);
                    tabbedPane.setEnabledAt(2, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 2);
            }
        }
    }

    public class AddPostalAddressAction extends UpdateEagObject {
        AddPostalAddressAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }

            Location location = new Location();
            location.setLocalType("postal address");
            location.setCountry(new Country());
            location.setStreet(new Street());
            location.setMunicipalityPostalcode(new MunicipalityPostalcode());

            eag.getArchguide().getDesc().getRepositories().getRepository().get(0).getLocation().add(location);
            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 2);
        }
    }
    public class AddTelephoneAction extends UpdateEagObject {
        AddTelephoneAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
            repository.getTelephone().add(new Telephone());

            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 2);
        }
    }
    public class AddFaxAction extends UpdateEagObject {
        AddFaxAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
            repository.getFax().add(new Fax());

            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 2);
        }
    }
    public class AddEmailAction extends UpdateEagObject {
        AddEmailAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
            repository.getEmail().add(new Email());

            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 2);
        }
    }
    public class AddWebpageAction extends UpdateEagObject {
        AddWebpageAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {

            }
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
            repository.getWebpage().add(new Webpage());

            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 2);
        }
    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        public UpdateEagObject(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        protected void updateEagObject() throws Eag2012FormException {
            errors = new ArrayList<String>();

            boolean hasChanged = false;

            if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) {
                Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
                if(repository.getLocation().size() > 0) {
                    Location location = repository.getLocation().get(0);
//                    if(!StringUtils.isEmpty(location.getLocalType()) && !location.getLocalType().equals("visitors address")) {
//                        if(repository.getLocation().size() > 1) {
//                            location = repository.getLocation().get(1);
//                        }
//                    }
                    location.setLocalType("visitors address");
                    if(StringUtils.isNotEmpty(streetTf.getText())) {
                        if(!streetTf.getText().equals(location.getStreet().getContent())) {
                            location.getStreet().setContent(streetTf.getText());
//                            System.out.println(languageBoxStreet.getSelectedItem().toString());
                            hasChanged = true;
                        }
//                        if(!languageBoxStreet.getSelectedItem().toString().equals("---")) {
//                            location.getStreet().setLang(languageBoxStreet.getSelectedItem().toString());
//                            hasChanged = true;
//                        }
                    }
                    if(StringUtils.isNotEmpty(cityTf.getText())) {
                        if(!cityTf.getText().equals(location.getMunicipalityPostalcode().getContent())) {
                            location.getMunicipalityPostalcode().setContent(cityTf.getText());
                            hasChanged = true;
                        }
//                        if(!languageBoxCity.getSelectedItem().toString().equals("---")) {
//                            location.getMunicipalityPostalcode().setLang(languageBoxCity.getSelectedItem().toString());
//                            hasChanged = true;
//                        }
                    }
                    if(StringUtils.isNotEmpty(countryTf.getText())) {
                        if(!countryTf.getText().equals(location.getCountry().getContent())) {
                            location.getCountry().setContent(countryTf.getText());
                            hasChanged = true;
                        }
//                        if(!languageBoxCountry.getSelectedItem().toString().equals("---")) {
//                            location.getCountry().setLang(languageBoxCountry.getSelectedItem().toString());
//                            hasChanged = true;
//                        }
                    }

                    if(StringUtils.isNotEmpty(coordinatesLatTf.getText()) && !coordinatesLatTf.getText().equals(location.getLatitude())) {
                        location.setLatitude(coordinatesLatTf.getText());
                        hasChanged = true;
                    }

                    if(StringUtils.isNotEmpty(coordinatesLongTf.getText()) && !coordinatesLongTf.getText().equals(location.getLongitude())) {
                        location.setLongitude(coordinatesLongTf.getText());
                        hasChanged = true;
                    }


                    if(repository.getLocation().size() > 1) {
                        location = repository.getLocation().get(1);
                        location.setLocalType("postal address");
                        if(StringUtils.isNotEmpty(streetPTf.getText())) {
                            if(!streetPTf.getText().equals(location.getStreet().getContent())) {
                                location.getStreet().setContent(streetPTf.getText());
                                hasChanged = true;
                            }
//                            if(!languageBoxStreet.getSelectedItem().toString().equals("---")) {
//                                location.getStreet().setLang(languageBoxStreet.getSelectedItem().toString());
//                                hasChanged = true;
//                            }
                        }
                        if(StringUtils.isNotEmpty(cityPTf.getText())) {
                            if(!cityPTf.getText().equals(location.getMunicipalityPostalcode().getContent())) {
                                location.getMunicipalityPostalcode().setContent(cityPTf.getText());
                                hasChanged = true;
                            }
//                            if(!languageBoxCity.getSelectedItem().toString().equals("---")) {
//                                location.getMunicipalityPostalcode().setLang(languageBoxCity.getSelectedItem().toString());
//                                hasChanged = true;
//                            }
                        }
                        if(StringUtils.isNotEmpty(countryPTf.getText())) {
                            if(!countryPTf.getText().equals(location.getCountry().getContent())) {
                                location.getCountry().setContent(countryPTf.getText());
                                hasChanged = true;
                            }
//                            if(!languageBoxCountry.getSelectedItem().toString().equals("---")) {
//                                location.getCountry().setLang(languageBoxCountry.getSelectedItem().toString());
//                                hasChanged = true;
//                            }
                        }

                        if(StringUtils.isNotEmpty(coordinatesLatPTf.getText()) && !coordinatesLatPTf.getText().equals(location.getLatitude())) {
                            location.setLatitude(coordinatesLatPTf.getText());
                            hasChanged = true;
                        }

                        if(StringUtils.isNotEmpty(coordinatesLongPTf.getText()) && !coordinatesLongPTf.getText().equals(location.getLongitude())) {
                            location.setLongitude(coordinatesLongPTf.getText());
                            hasChanged = true;
                        }
                    }

                }

                if(!continentCombo.getSelectedItem().equals(repository.getGeogarea().getValue())) {
                    repository.getGeogarea().setValue(continentCombo.getSelectedItem().toString());
                    hasChanged = true;
                }



                repository.getTelephone().clear();
                for(JTextField field : telephoneTfs) {
                    if(StringUtils.isNotEmpty(field.getText())) {
                        Telephone telephone = new Telephone();
                        telephone.setContent(field.getText());
                        repository.getTelephone().add(telephone);
                        hasChanged = true;
                    }
                }
                repository.getFax().clear();
                for(JTextField field : faxTfs) {
                    if(StringUtils.isNotEmpty(field.getText())) {
                        Fax fax = new Fax();
                        fax.setContent(field.getText());
                        repository.getFax().add(fax);
                        hasChanged = true;
                    }
                }
                repository.getEmail().clear();
                for(int i = 0; i < emailTfs.size(); i++) {
                    JTextField field = emailTfs.get(i);
                    JTextField fieldTitle = emailTitleTfs.get(i);
                    if(StringUtils.isNotEmpty(field.getText())) {
                        Email email = new Email();
                        email.setHref(field.getText());
                        if(StringUtils.isNotEmpty(fieldTitle.getText()))
                            email.setContent(fieldTitle.getText());
                        else
                            email.setContent("Send an email");
                        repository.getEmail().add(email);
                        hasChanged = true;
                    }
                }
                repository.getWebpage().clear();
                for(int i = 0; i < webpageTfs.size(); i++) {
                    JTextField field = webpageTfs.get(i);
                    JTextField fieldTitle = webpageTitleTfs.get(i);
                    if(StringUtils.isNotEmpty(field.getText())) {
                        Webpage webpage = new Webpage();
                        webpage.setHref(field.getText());
                        if(StringUtils.isNotEmpty(fieldTitle.getText()))
                            webpage.setContent(fieldTitle.getText());
                        else
                            webpage.setContent("Visit website");
                        repository.getWebpage().add(webpage);
                        hasChanged = true;
                    }
                }
            }

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}
