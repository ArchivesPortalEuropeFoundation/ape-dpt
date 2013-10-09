package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import static eu.apenet.dpt.standalone.gui.eag2012.EagPanels.createErrorLabel;

import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.LocationType;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.TextFieldWithLanguage;
import eu.apenet.dpt.utils.eag2012.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: Yoann Moranville
 * Date: 27/11/2012
 *
 * @author Yoann Moranville
 */
public class EagContactPanel extends EagPanels {
    private List<TextFieldWithLanguage> repositoryNameTfs;
    private JComboBox repositoryRoleTypeCombo = new JComboBox(repositoryRoles);

    private List<LocationType> locationFields;
    private List<JTextField> telephoneTfs;
    private List<JTextField> faxTfs;
    private List<JTextField> emailTfs;
    private List<JTextField> emailTitleTfs;
    private List<JTextField> webpageTfs;
    private List<JTextField> webpageTitleTfs;
    
    private boolean isNew;

    private JComboBox continentCombo = new JComboBox(continents);

    public EagContactPanel(Eag eag, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, boolean isNew, ResourceBundle labels, int repositoryNb) {
        super(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
        this.isNew = isNew;
    }

    @Override
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
                "right:max(50dlu;p), 4dlu, 100dlu, 7dlu, right:p, 4dlu, 100dlu",
                EDITOR_ROW_SPEC);

        layout.setColumnGroups(new int[][] { { 1, 3, 5, 7 } });
        PanelBuilder builder = new PanelBuilder(layout);

        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        rowNb = 1;

        Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);

        if(repositoryNb > 0) {
            if(repository.getRepositoryRole() == null)
                repository.setRepositoryRole(new RepositoryRole());
            if(Arrays.asList(repositoryRoles).contains(repository.getRepositoryRole().getValue()))
                repositoryRoleTypeCombo.setSelectedItem(repository.getRepositoryRole().getValue());
            builder.addLabel(labels.getString("eag2012.commons.roleOfRepository"), cc.xy(1, rowNb));
            builder.add(repositoryRoleTypeCombo, cc.xy(3, rowNb));

            setNextRow();

            if(repository.getRepositoryName().size() == 0)
                repository.getRepositoryName().add(new RepositoryName());
            repositoryNameTfs = new ArrayList<TextFieldWithLanguage>(repository.getRepositoryName().size());
            for(RepositoryName repositoryName : repository.getRepositoryName()) {
                builder.addLabel(labels.getString("eag2012.commons.nameOfRepository"), cc.xy(1, rowNb));
                TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(repositoryName.getContent(), repositoryName.getLang());
                repositoryNameTfs.add(textFieldWithLanguage);
                builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
                builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
                setNextRow();
            }
            JButton addNewRepositoryNameBtn = new ButtonEag(labels.getString("eag2012.contact.addNameOfRepository"));
            addNewRepositoryNameBtn.addActionListener(new AddRepositoryNameAction(eag, tabbedPane, model));
            builder.add(addNewRepositoryNameBtn, cc.xy(3, rowNb));
            setNextRow();
        }

        boolean hasMinimumOnePostalAddress = false;
        boolean hasMinimumOneVisitorAddress = false;
        locationFields = new ArrayList<LocationType>(repository.getLocation().size());
        for(Location location : repository.getLocation()) {
            boolean isPostal = false;
            LocationType locationType = new LocationType(location);
            locationFields.add(locationType);
            if(StringUtils.isEmpty(location.getLocalType()) || location.getLocalType().equals("visitors address")) {
                locationType.setLocalType("visitors address");
                builder.addSeparator(labels.getString("eag2012.commons.visitorsAddress"), cc.xyw(1, rowNb, 7));
                isPostal = false;
                hasMinimumOneVisitorAddress = true;
            } else if (location.getLocalType().equals("postal address")) {
                builder.addSeparator(labels.getString("eag2012.commons.postalAddress"), cc.xyw(1, rowNb, 7));
                isPostal = true;
                hasMinimumOnePostalAddress = true;
            }
            setNextRow();

            String mandatoryStar = "*";
            if(isPostal)
                mandatoryStar = "";

            builder.addLabel(labels.getString("eag2012.commons.street") + mandatoryStar,    cc.xy (1, rowNb));
            builder.add(locationType.getStreetTf().getTextField(),                               cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(locationType.getStreetTf().getLanguageBox(),                               cc.xy (7, rowNb));
            if(errors.contains("streetTf") && StringUtils.isEmpty(locationType.getStreetTfValue())) {
                setNextRow();
                builder.add(createErrorLabel(labels.getString("eag2012.errors.street")),          cc.xy (1, rowNb));
            }
            setNextRow();

            builder.addLabel(labels.getString("eag2012.commons.cityTownWithPostalcode") + mandatoryStar,    cc.xy (1, rowNb));

            builder.add(locationType.getCityTf().getTextField(),                               cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(locationType.getCityTf().getLanguageBox(),                               cc.xy (7, rowNb));
            if(errors.contains("cityTf") && StringUtils.isEmpty(locationType.getCityTfValue())) {
                setNextRow();
                builder.add(createErrorLabel(labels.getString("eag2012.errors.city")),          cc.xy (1, rowNb));
            }
            setNextRow();

            if(!isPostal) {
                builder.addLabel(labels.getString("eag2012.contact.district"),    cc.xy (1, rowNb));
                builder.add(locationType.getDistrictTf().getTextField(), cc.xy (3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
                builder.add(locationType.getDistrictTf().getLanguageBox(),                               cc.xy (7, rowNb));
                setNextRow();

                builder.addLabel(labels.getString("eag2012.contact.countyLocalAuthority"),    cc.xy (1, rowNb));
                builder.add(locationType.getCountyTf().getTextField(), cc.xy (3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
                builder.add(locationType.getCountyTf().getLanguageBox(),                               cc.xy (7, rowNb));
                setNextRow();

                builder.addLabel(labels.getString("eag2012.contact.region"),    cc.xy (1, rowNb));
                builder.add(locationType.getRegionTf().getTextField(), cc.xy (3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
                builder.add(locationType.getRegionTf().getLanguageBox(),                               cc.xy (7, rowNb));
                setNextRow();

                builder.addLabel(labels.getString("eag2012.commons.country") + "*",    cc.xy (1, rowNb));
                builder.add(locationType.getCountryTf().getTextField(), cc.xy (3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
                builder.add(locationType.getCountryTf().getLanguageBox(),                               cc.xy (7, rowNb));
                if(errors.contains("countryTf") && StringUtils.isEmpty(locationType.getCountryTfValue())) {
                    setNextRow();
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.country")),          cc.xy (1, rowNb));
                }
                setNextRow();

                builder.addLabel(labels.getString("eag2012.commons.latitude"),    cc.xy (1, rowNb));
                builder.add(locationType.getLatitudeTf(), cc.xy (3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.longitude"), cc.xy(5, rowNb));
                builder.add(locationType.getLongitudeTf(), cc.xy (7, rowNb));
                setNextRow();
            }
        }

        if(hasMinimumOneVisitorAddress) {
            JButton addNewVisitorTranslationAddressBtn = new ButtonEag(labels.getString("eag2012.commons.addVisitorTranslationAddress"), true);
            addNewVisitorTranslationAddressBtn.addActionListener(new AddAddressAction(eag, tabbedPane, model, false));
            builder.add(addNewVisitorTranslationAddressBtn, cc.xy(3, rowNb));
            setNextRow();
        } else {
            JButton addNewVisitorAddressBtn = new ButtonEag(labels.getString("eag2012.commons.addVisitorAddress"));
            addNewVisitorAddressBtn.addActionListener(new AddAddressAction(eag, tabbedPane, model, false));
            builder.add(addNewVisitorAddressBtn, cc.xy(3, rowNb));
            setNextRow();
        }

        if(hasMinimumOnePostalAddress) {
            JButton addNewPostalTranslationAddressBtn = new ButtonEag(labels.getString("eag2012.commons.addPostalTranslationAddress"), true);
            addNewPostalTranslationAddressBtn.addActionListener(new AddAddressAction(eag, tabbedPane, model, true));
            builder.add(addNewPostalTranslationAddressBtn, cc.xy(3, rowNb));
            setNextRow();
        } else {
            JButton addNewPostalAddressBtn = new ButtonEag(labels.getString("eag2012.commons.addPostalAddress"));
            addNewPostalAddressBtn.addActionListener(new AddAddressAction(eag, tabbedPane, model, true));
            builder.add(addNewPostalAddressBtn, cc.xy(3, rowNb));
            setNextRow();
        }

        builder.addLabel(labels.getString("eag2012.commons.continent") + "*",    cc.xy (1, rowNb));
        if(Arrays.asList(continents).contains(repository.getGeogarea().getValue())){
            continentCombo.setSelectedItem(repository.getGeogarea().getValue());
        } else {
            continentCombo.setSelectedItem("Europe");
        }
        builder.add(continentCombo, cc.xy (3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.commons.telephone"), cc.xy(1, rowNb));
        int i = 0;
        telephoneTfs = new ArrayList<JTextField>(repository.getTelephone().size());
        for(Telephone telephone : repository.getTelephone()) {
            JTextField telephoneTf = new JTextField(telephone.getContent());
            telephoneTfs.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            if(i++ == 0) {
                JButton addTelephoneBtn = new ButtonEag(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
                addTelephoneBtn.addActionListener(new AddTelephoneAction(eag, tabbedPane, model));
                builder.add(addTelephoneBtn, cc.xy(5, rowNb));
            }
            setNextRow();
        }
        if(repository.getTelephone().size() == 0) {
            JTextField telephoneTf = new JTextField();
            telephoneTfs.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            JButton addTelephoneBtn = new ButtonEag(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
            addTelephoneBtn.addActionListener(new AddTelephoneAction(eag, tabbedPane, model));
            builder.add(addTelephoneBtn, cc.xy(5, rowNb));
            setNextRow();
        }

        builder.addLabel(labels.getString("eag2012.contact.fax"), cc.xy(1, rowNb));
        i = 0;
        faxTfs = new ArrayList<JTextField>(repository.getFax().size());
        for(Fax fax : repository.getFax()) {
            JTextField faxTf = new JTextField(fax.getContent());
            faxTfs.add(faxTf);
            builder.add(faxTf, cc.xy (3, rowNb));
            if(i++ == 0) {
                JButton addFaxBtn = new ButtonEag(labels.getString("eag2012.contact.addFaxNumbers"));
                addFaxBtn.addActionListener(new AddFaxAction(eag, tabbedPane, model));
                builder.add(addFaxBtn, cc.xy(5, rowNb));
            }
            setNextRow();
        }
        if(repository.getFax().size() == 0) {
            JTextField faxTf = new JTextField();
            faxTfs.add(faxTf);
            builder.add(faxTf, cc.xy (3, rowNb));
            JButton addFaxBtn = new ButtonEag(labels.getString("eag2012.contact.addFaxNumbers"));
            addFaxBtn.addActionListener(new AddFaxAction(eag, tabbedPane, model));
            builder.add(addFaxBtn, cc.xy(5, rowNb));
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
            builder.addLabel(labels.getString("eag2012.commons.email"),    cc.xy (1, rowNb));
            builder.add(emailTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),             cc.xy (5, rowNb));
            builder.add(emailTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addEmailBtn = new ButtonEag(labels.getString("eag2012.commons.addEmail"));
        addEmailBtn.addActionListener(new AddEmailAction(eag, tabbedPane, model));
        builder.add(addEmailBtn, cc.xy(1, rowNb));
        setNextRow();

        webpageTfs = new ArrayList<JTextField>(repository.getWebpage().size());
        webpageTitleTfs = new ArrayList<JTextField>(repository.getWebpage().size());
        if(repository.getWebpage().size() == 0)
            repository.getWebpage().add(new Webpage());
        for(Webpage webpage : repository.getWebpage()) {
            JTextField webpageTf = new JTextField(webpage.getHref());
            JTextField webpageTitleTf;
            if(!StringUtils.isBlank(repository.getWebpage().get(0).getContent()))
                webpageTitleTf = new JTextField(webpage.getContent());
            else
                webpageTitleTf = new JTextField();
            webpageTfs.add(webpageTf);
            webpageTitleTfs.add(webpageTitleTf);
            builder.addLabel(labels.getString("eag2012.commons.webpage"),    cc.xy (1, rowNb));
            builder.add(webpageTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),             cc.xy (5, rowNb));
            builder.add(webpageTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();
            if(errors.contains("webpageTfs")) {
                if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }
        }
        JButton addWebpageBtn = new ButtonEag(labels.getString("eag2012.commons.addWebpage"));
        addWebpageBtn.addActionListener(new AddWebpageAction(eag, tabbedPane, model));
        builder.add(addWebpageBtn, cc.xy(1, rowNb));
        setNextRow();

        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();

        JButton exitBtn = new ButtonEag(labels.getString("eag2012.commons.exit"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        if(repositoryNb == 0) {
            JButton previousTabBtn = new ButtonEag(labels.getString("eag2012.commons.previousTab"));
            builder.add(previousTabBtn, cc.xy (3, rowNb));
            previousTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, false));
        }

        JButton nextTabBtn = new ButtonEag(labels.getString("eag2012.commons.nextTab"));
        builder.add(nextTabBtn, cc.xy (5, rowNb));
        nextTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, true));

        setNextRow();
        JButton saveBtn = new ButtonEag(labels.getString("eag2012.commons.save"));
        builder.add(saveBtn, cc.xy (5, rowNb));
        saveBtn.addActionListener(new SaveBtnAction(eag, tabbedPane, model));

        setNextRow();
        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();
        JButton previousInstitutionTabBtn = new ButtonEag(labels.getString("eag2012.controls.previousInstitution"));
        previousInstitutionTabBtn.addActionListener(new PreviousInstitutionTabBtnAction(eag, tabbedPane, model));
        builder.add(previousInstitutionTabBtn, cc.xy(1, rowNb));
        JButton nextInstitutionTabBtn = new ButtonEag(labels.getString("eag2012.controls.nextInstitution"));
        nextInstitutionTabBtn.addActionListener(new NextInstitutionTabBtnAction(eag, tabbedPane, model));
        builder.add(nextInstitutionTabBtn, cc.xy(5, rowNb));

//        if(tabbedPane.getChangeListeners().length < 2) {
//            LOG.info("Add listener");
//            tabbedPane.addChangeListener(new TabChangeListener(eag, tabbedPane, model));
//        }

        return builder.getPanel();
    }

    public class NextInstitutionTabBtnAction extends UpdateEagObject {
        NextInstitutionTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(true);

                int currentTabIndex = mainTabbedPane.getSelectedIndex();
                if(currentTabIndex+1 < mainTabbedPane.getTabCount()) {
                    reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
                    mainTabbedPane.setEnabledAt(currentTabIndex, false);
                    mainTabbedPane.setEnabledAt(currentTabIndex+1, true);
                    mainTabbedPane.setSelectedIndex(currentTabIndex+1);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
            }
        }
    }

    public class PreviousInstitutionTabBtnAction extends UpdateEagObject {
        PreviousInstitutionTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(true);

                int currentTabIndex = mainTabbedPane.getSelectedIndex();
                if(currentTabIndex > 0) {
                    reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
                    mainTabbedPane.setEnabledAt(currentTabIndex, false);
                    mainTabbedPane.setEnabledAt(currentTabIndex-1, true);
                    mainTabbedPane.setSelectedIndex(currentTabIndex-1);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
            }
        }
    }

    public class SaveBtnAction extends UpdateEagObject {
        SaveBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(true);
                super.saveFile(eag.getControl().getRecordId().getValue());
                closeFrame();
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
            }
        }
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
                super.updateEagObject(false);

                if(isNextTab) {
                    reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
                    tabbedPane.setEnabledAt(3, true);
                    tabbedPane.setEnabledAt(2, false);
                } else {
                    reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
                    tabbedPane.setEnabledAt(1, true);
                    tabbedPane.setEnabledAt(2, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
            }
        }
    }

    public class AddAddressAction extends UpdateEagObject {
        private boolean isPostal;
        AddAddressAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model, boolean isPostal) {
            super(eag, tabbedPane, model);
            this.isPostal = isPostal;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(false);
            } catch (Eag2012FormException e) {
//                for(String error : getErrors().keySet()) {
//                    System.out.println(error);
//                }
            }

            int counter = locationFields.size();
            if (counter > 1){
                if (isPostal && locationFields.get(counter - 1).getLocalType().equals("postal address")) {
                    reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 0);
                }
                if (!isPostal && locationFields.get(counter - 1).getLocalType().equals("visitors address")) {
                    if(StringUtils.isBlank(locationFields.get(counter - 1).getCountryTfValue())){
                        reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 0);
                    }
                }
            }
            if(locationFields.get(counter - 1).getErrors() == null || locationFields.get(counter - 1).getErrors().isEmpty()){
                Location location = new Location();
                if(isPostal) {
                    location.setLocalType("postal address");
                } else {
                    location.setLocalType("visitors address");
                }
                location.setCountry(new Country());
                location.setStreet(new Street());
                location.setMunicipalityPostalcode(new MunicipalityPostalcode());
                
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getLocation().add(location);
            }
            if(locationFields.get(counter - 1).getErrors().isEmpty() ){
                reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 0);
            }
//            Location location = new Location();
//            if(isPostal) {
//                location.setLocalType("postal address");
//            } else {
//                location.setLocalType("visitors address");
//            }
//            location.setCountry(new Country());
//            location.setStreet(new Street());
//            location.setMunicipalityPostalcode(new MunicipalityPostalcode());
//
//            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getLocation().add(location);
//            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 2);
        }
    }
    public class AddTelephoneAction extends UpdateEagObject {
        AddTelephoneAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(false);
            } catch (Eag2012FormException e) {

            }
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);
            repository.getTelephone().add(new Telephone());

            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
        }
    }
    public class AddFaxAction extends UpdateEagObject {
        AddFaxAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(false);
            } catch (Eag2012FormException e) {

            }
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);
            repository.getFax().add(new Fax());

            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
        }
    }
    public class AddEmailAction extends UpdateEagObject {
        AddEmailAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(false);
            } catch (Eag2012FormException e) {

            }
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);
            repository.getEmail().add(new Email());

            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
        }
    }
    public class AddWebpageAction extends UpdateEagObject {
        AddWebpageAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(false);
            } catch (Eag2012FormException e) {

            }
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);
            Webpage webpage = new Webpage();
            repository.getWebpage().add(webpage);

            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
        }
    }
    public class AddRepositoryNameAction extends UpdateEagObject {
        AddRepositoryNameAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(false);
            } catch (Eag2012FormException e) {

            }
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);
            RepositoryName repositoryName = new RepositoryName();
            repository.getRepositoryName().add(repositoryName);

            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
        }
    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        public UpdateEagObject(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        protected void updateEagObject(boolean save) throws Eag2012FormException {
            errors = new ArrayList<String>();

            boolean hasChanged = false;

//            if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) {
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);

            if(repositoryNb > 0) {
                boolean passedFirstName = false;
                if(repositoryNameTfs.size() > 0) {
                    repository.getRepositoryName().clear();
                    for(TextFieldWithLanguage textFieldWithLanguage : repositoryNameTfs) {
                        if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                            if(!passedFirstName) {
                                passedFirstName = true;
                                mainTabbedPane.setTitleAt(repositoryNb, textFieldWithLanguage.getTextValue());
                            }
                            RepositoryName repositoryName = new RepositoryName();
                            repositoryName.setContent(textFieldWithLanguage.getTextValue());
                            repositoryName.setLang(textFieldWithLanguage.getLanguage());
                            repository.getRepositoryName().add(repositoryName);
                        }
                    }
                }

                if(!repositoryRoleTypeCombo.getSelectedItem().equals("---")) {
                    repository.getRepositoryRole().setValue((String)repositoryRoleTypeCombo.getSelectedItem());
                } else {
                    repository.setRepositoryRole(null);
                }
            }

                repository.getLocation().clear();

                String defaultCountry = "";
                boolean isFirst = true;
                for(LocationType locationType : locationFields) {
                    if(StringUtils.isNotEmpty(locationType.getCountryTfValue())) {
                        defaultCountry = locationType.getCountryTfValue();
                    }
                    Location location = locationType.getLocation(defaultCountry, isFirst);
                    isFirst = false;
                    errors.addAll(locationType.getErrors());
                    if(location != null)
                        repository.getLocation().add(location);
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
                            email.setContent(field.getText());
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
                        if(!StringUtils.startsWithAny(field.getText(), webPrefixes))
                            errors.add("webpageTfs");
                        if(StringUtils.isNotEmpty(fieldTitle.getText()))
                            webpage.setContent(fieldTitle.getText());
                        repository.getWebpage().add(webpage);
                        hasChanged = true;
                    }
                }
//            }

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }

//    public class TabChangeListener extends UpdateEagObject implements ChangeListener {
//        private boolean click;
//        public TabChangeListener(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
//            super(eag, tabbedPane, model);
//            click = true;
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent actionEvent) {}
//
//        public void stateChanged(ChangeEvent changeEvent) {
//            LOG.info("stateChanged (mainTabbed index: " + mainTabbedPane.getSelectedIndex() + ")");
//            if(click && !Eag2012Frame.firstTimeInTab) {
//                tabbedPane.removeChangeListener(this);
//                try {
//                    super.updateEagObject(false);
//                    LOG.info("Ok");
//                    Eag2012Frame.firstTimeInTab = true;
//                    EagPanels eagPanels = getCorrectEagPanels(tabbedPane.getSelectedIndex(), mainTabbedPane, eag2012Frame, labels, repositoryNb);
//                    reloadTabbedPanel(eagPanels.buildEditorPanel(errors), tabbedPane.getSelectedIndex());
//                } catch (Eag2012FormException e) {
//                    LOG.info("NOT Ok");
//                    EagPanels eagPanels = getCorrectEagPanels(2, mainTabbedPane, eag2012Frame, labels, repositoryNb);
//                    reloadTabbedPanel(eagPanels.buildEditorPanel(errors), 2);
//                }
//                click = false;
//            }
//            Eag2012Frame.firstTimeInTab = false;
//        }
//    }
}
