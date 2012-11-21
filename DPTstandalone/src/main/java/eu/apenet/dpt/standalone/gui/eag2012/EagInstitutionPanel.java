package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 03/10/2012
 *
 * @author Yoann Moranville
 */
public class EagInstitutionPanel extends EagPanels {

    /**
     * Below will be all the different NON REPEATABLE form elements
     */
    private JTextField personTf;
    private JTextField countryCodeTf;
    private JTextField identifierTf;
    private List<JTextField> otherIdTfs;
    private List<JTextField> nameInstitutionTfs;
    private List<JTextField> parallelNameTfs;
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
    private JTextField emailTf;
    private JTextField emailTitleTf;
    private JTextField webpageTf;
    private JTextField webpageTitleTf;
    private JTextField openingTimesTf;
    private JTextField closingTimesTf;
    private JTextField refInstitutionHoldingsGuideTf;
    private JTextField refInstitutionHoldingsGuideTitleTf;

    private JComboBox languageBoxNameInstitution = new JComboBox(languages);
    private JComboBox languageBoxParallelName = new JComboBox(languages);
    private JComboBox languageBoxStreet = new JComboBox(languages);
    private JComboBox languageBoxCity = new JComboBox(languages);
    private JComboBox languageBoxCountry = new JComboBox(languages);

    private JComboBox continentCombo = new JComboBox(continents);

    private JComboBox accessiblePublicCombo = new JComboBox(yesOrNo);
    private JComboBox facilitiesForDisabledCombo = new JComboBox(yesOrNo);

    public EagInstitutionPanel(Eag eag, JTabbedPane tabbedPane) {
        super(eag, tabbedPane);
    }

    /**
     * Builds and answer the editor's general tab for the given layout.
     * @return the editor's general panel
     */

    protected JComponent buildEditorPanel() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;p), 4dlu, 100dlu, 7dlu, right:p, 4dlu, 100dlu",
                EDITOR_ROW_SPEC);

        layout.setColumnGroups(new int[][] { { 1, 3, 5, 7 } });
        PanelBuilder builder = new PanelBuilder(layout);

        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        rowNb = 1;
        builder.addSeparator("Your institution", cc.xyw(1, rowNb, 7));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.personResponsibleLabel"),    cc.xy (1, rowNb));
        personTf = new JTextField("WHAT FIELD IS THIS???");
        builder.add(personTf, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.countryCodeLabel"),          cc.xy (1, rowNb));
        countryCodeTf = new JTextField(eag.getArchguide().getIdentity().getRepositorid().getCountrycode());
        builder.add(countryCodeTf, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.identifierInstitutionLabel"),cc.xy (1, rowNb));
        identifierTf = new JTextField(eag.getControl().getRecordId().getValue());
        builder.add(identifierTf,                                           cc.xy (3, rowNb));

        otherIdTfs = new ArrayList<JTextField>(eag.getControl().getOtherRecordId().size());
        for(OtherRecordId otherRecordId : eag.getControl().getOtherRecordId()) {
            JTextField otherIdTf = new JTextField(otherRecordId.getValue());
            otherIdTfs.add(otherIdTf);
            builder.addLabel(labels.getString("eag2012.idUsedInApeLabel"),      cc.xy (5, rowNb));
            builder.add(otherIdTf,               cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addNewOtherIdentifierBtn = new JButton(labels.getString("eag2012.addOtherIdentifier"));
        addNewOtherIdentifierBtn.addActionListener(new AddOtherIdentifierAction(eag, tabbedPane));
        builder.add(addNewOtherIdentifierBtn, cc.xy(7, rowNb));
        setNextRow();

        nameInstitutionTfs = new ArrayList<JTextField>(eag.getArchguide().getIdentity().getAutform().size());
        for(Autform autform : eag.getArchguide().getIdentity().getAutform()) {
            JTextField nameInstitutionTf = new JTextField(autform.getContent());
            nameInstitutionTfs.add(nameInstitutionTf);
            builder.addLabel(labels.getString("eag2012.nameOfInstitutionLabel"),    cc.xy (1, rowNb));
            builder.add(nameInstitutionTf, cc.xy (3, rowNb));
            setNextRow();
        }
//        builder.addLabel(labels.getString("eag2012.languageLabel"),             cc.xy (5, rowNb));
//        builder.add(languageBoxNameInstitution,                                            cc.xy (7, rowNb));
//        setNextRow();
        parallelNameTfs = new ArrayList<JTextField>(eag.getArchguide().getIdentity().getParform().size());
        for(Parform parform : eag.getArchguide().getIdentity().getParform()) {
            JTextField parallelNameTf = new JTextField(parform.getContent());
            parallelNameTfs.add(parallelNameTf);
            builder.addLabel(labels.getString("eag2012.parallelNameOfInstitutionLabel"),    cc.xy (1, rowNb));
            builder.add(parallelNameTf, cc.xy (3, rowNb));
            setNextRow();
        }
//        builder.addLabel(labels.getString("eag2012.languageLabel"),             cc.xy (5, rowNb));
//        builder.add(languageBoxParallelName,                                                cc.xy (7, rowNb));
//        setNextRow();

        if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) { //todo: BECAUSE FOR NOW ONLY ONE REPOSITORY!!!!
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);

            if(repository.getLocation().size() > 0) {
                Location location = repository.getLocation().get(0);
                if(StringUtils.isEmpty(location.getLocalType()) || location.getLocalType().equals("visitors address")) {

                } else if (location.getLocalType().equals("postal address")) {

                }
            }

            for(int i = 0; i < repository.getLocation().size(); i++) {
                boolean isPostal = false;

                if(i == 2)
                    break;
                Location location = repository.getLocation().get(i);
                if(StringUtils.isEmpty(location.getLocalType())) {
                    location.setLocalType("visitors address");
                }
                if(location.getLocalType().equals("visitors address")) {
                    builder.addSeparator("Visitors address", cc.xyw(1, rowNb, 7));
                    isPostal = false;
                } else if (location.getLocalType().equals("postal address")) {
                    builder.addSeparator("Postal address", cc.xyw(1, rowNb, 7));
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

            if(repository.getLocation().size() < 2) { //If equal or more than 2, we already have visitors and postal addresses
                JButton addNewPostalAddressBtn = new JButton(labels.getString("eag2012.addPostalAddress"));
                addNewPostalAddressBtn.addActionListener(new AddPostalAddressAction(eag, tabbedPane));
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
            if(repository.getTelephone().size() > 0) {
                telephoneTf = new JTextField(repository.getTelephone().get(0).getContent());
            } else {
                telephoneTf = new JTextField();
            }
            builder.add(telephoneTf, cc.xy (3, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.emailLabel"),    cc.xy (1, rowNb));
            if(repository.getEmail().size() > 0) {
                emailTf = new JTextField(repository.getEmail().get(0).getHref());
                emailTitleTf = new JTextField(repository.getEmail().get(0).getContent());
            } else {
                emailTf = new JTextField();
                emailTitleTf = new JTextField();
            }
            builder.add(emailTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkTitleLabel"),             cc.xy (5, rowNb));
            builder.add(emailTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.webpageLabel"),    cc.xy (1, rowNb));
            if(repository.getWebpage().size() > 0) {
                webpageTf = new JTextField(repository.getWebpage().get(0).getHref());
                webpageTitleTf = new JTextField(repository.getWebpage().get(0).getContent());
            } else {
                webpageTf = new JTextField();
                webpageTitleTf = new JTextField();
            }
            builder.add(webpageTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkTitleLabel"),             cc.xy (5, rowNb));
            builder.add(webpageTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.openingTimesLabel"),    cc.xy (1, rowNb));
            openingTimesTf = new JTextField(repository.getTimetable().getOpening().getContent());
            builder.add(openingTimesTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.closingTimesLabel"), cc.xy(5, rowNb));
            if(repository.getTimetable().getClosing() != null) {
                closingTimesTf = new JTextField(repository.getTimetable().getClosing().getContent());
            } else {
                closingTimesTf = new JTextField();
            }
            builder.add(closingTimesTf,                                            cc.xy (7, rowNb));
            setNextRow();
            builder.addLabel(labels.getString("eag2012.accessiblePublicLabel"),    cc.xy (1, rowNb));
            if(Arrays.asList(yesOrNo).contains(repository.getAccess().getQuestion())) {
                accessiblePublicCombo.setSelectedItem(repository.getAccess().getQuestion());
            }
            builder.add(accessiblePublicCombo, cc.xy(3, rowNb));
            setNextRow();
            builder.addLabel(labels.getString("eag2012.facilitiesForDisabledLabel"), cc.xy(1, rowNb));
            if(repository.getAccessibility().size() > 0 && Arrays.asList(yesOrNo).contains(repository.getAccessibility().get(0).getQuestion())) {
                facilitiesForDisabledCombo.setSelectedItem(repository.getAccessibility().get(0).getQuestion());
            }
            builder.add(facilitiesForDisabledCombo, cc.xy(3, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.referenceToHoldingsGuideLabel"), cc.xy(1, rowNb));
            if(eag.getRelations().getResourceRelation().size() > 0) {
                refInstitutionHoldingsGuideTf = new JTextField(eag.getRelations().getResourceRelation().get(0).getHref());
                if(eag.getRelations().getResourceRelation().get(0).getDescriptiveNote() != null && eag.getRelations().getResourceRelation().get(0).getDescriptiveNote().getP().size() > 0)
                    refInstitutionHoldingsGuideTitleTf = new JTextField(eag.getRelations().getResourceRelation().get(0).getDescriptiveNote().getP().get(0).getContent());
                else
                    refInstitutionHoldingsGuideTitleTf = new JTextField();
            } else {
                refInstitutionHoldingsGuideTf = new JTextField();
                refInstitutionHoldingsGuideTitleTf = new JTextField();
            }
            builder.add(refInstitutionHoldingsGuideTf, cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkTitleLabel"),             cc.xy (5, rowNb));
            builder.add(refInstitutionHoldingsGuideTitleTf, cc.xy(7, rowNb));
            setNextRow();
        }

        builder.add(new JButton(labels.getString("eag2012.exitButton")), cc.xy (1, rowNb));
        JButton nextTabBtn = new JButton(labels.getString("eag2012.nextTabButton"));
        builder.add(nextTabBtn, cc.xy (3, rowNb));
//        builder.add(new JButton(labels.getString("eag2012.validateButton")), cc.xy (5, rowNb));
        nextTabBtn.addActionListener(new NextTabBtnAction(eag, tabbedPane));

        return builder.getPanel();
    }

    public class NextTabBtnAction extends UpdateEagObject {
        NextTabBtnAction(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            super.updateEagObject();

            tabbedPane.setEnabledAt(1, true);
            tabbedPane.setSelectedIndex(1);
            tabbedPane.setEnabledAt(0, false);
        }
    }

    public class AddOtherIdentifierAction extends UpdateEagObject {
        AddOtherIdentifierAction(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            super.updateEagObject();
            eag.getControl().getOtherRecordId().add(new OtherRecordId());
            reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane).buildEditorPanel(), 0);
        }

    }

    public class AddPostalAddressAction extends UpdateEagObject {
        AddPostalAddressAction(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            super.updateEagObject();

            Location location = new Location();
            location.setLocalType("postal address");
            location.setCountry(new Country());
            location.setStreet(new Street());
            location.setMunicipalityPostalcode(new MunicipalityPostalcode());

            eag.getArchguide().getDesc().getRepositories().getRepository().get(0).getLocation().add(location);
            reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane).buildEditorPanel(), 0);
        }

    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        UpdateEagObject(Eag eag, JTabbedPane tabbedPane) {
            super(eag, tabbedPane);
        }

        protected void updateEagObject() {
            boolean hasChanged = false;

            if(StringUtils.isNotEmpty(countryCodeTf.getText()) && !countryCodeTf.getText().equals(eag.getArchguide().getIdentity().getRepositorid().getCountrycode())) {
                if(countryCodeTf.getText().length() == 2) {
                    eag.getArchguide().getIdentity().getRepositorid().setCountrycode(countryCodeTf.getText());
                    hasChanged = true;
                } else {
                    //todo: ERROR TO USER
                }
            }
            if(StringUtils.isNotEmpty(identifierTf.getText()) && !identifierTf.getText().equals(eag.getControl().getRecordId().getValue())) {
                eag.getControl().getRecordId().setValue(identifierTf.getText());
                hasChanged = true;
            }
            if(otherIdTfs.size() > 0) {
                eag.getControl().getOtherRecordId().clear();
                for(JTextField field : otherIdTfs) {
                    if(StringUtils.isNotEmpty(field.getText())) {
                        OtherRecordId otherRecordId = new OtherRecordId();
                        otherRecordId.setValue(field.getText());
                        eag.getControl().getOtherRecordId().add(otherRecordId);
                        hasChanged = true;
                    }
                }
            }

            if(nameInstitutionTfs.size() > 0) {
                eag.getArchguide().getIdentity().getAutform().clear();
                for(JTextField field : nameInstitutionTfs) {
                    if(StringUtils.isNotEmpty(field.getText())) {
                        Autform autform = new Autform();
                        autform.setContent(field.getText());
                        eag.getArchguide().getIdentity().getAutform().add(autform);
                        hasChanged = true;
                    }
                }
            }

            if(parallelNameTfs.size() > 0) {
                eag.getArchguide().getIdentity().getParform().clear();
                for(JTextField field : parallelNameTfs) {
                    if(StringUtils.isNotEmpty(field.getText())) {
                        Parform parform = new Parform();
                        parform.setContent(field.getText());
                        eag.getArchguide().getIdentity().getParform().add(parform);
                        hasChanged = true;
                    }
                }
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) { //todo: BECAUSE FOR NOW ONLY ONE REPOSITORY!!!!
                Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
                if(repository.getLocation().size() > 0) { //todo: Same reason as earlier
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
                            System.out.println(languageBoxStreet.getSelectedItem().toString());
                            hasChanged = true;
                        }
                        if(!languageBoxStreet.getSelectedItem().toString().equals("---")) {
                            location.getStreet().setLang(languageBoxStreet.getSelectedItem().toString());
                            hasChanged = true;
                        }
                    }
                    if(StringUtils.isNotEmpty(cityTf.getText())) {
                        if(!cityTf.getText().equals(location.getMunicipalityPostalcode().getContent())) {
                            location.getMunicipalityPostalcode().setContent(cityTf.getText());
                            hasChanged = true;
                        }
                        if(!languageBoxCity.getSelectedItem().toString().equals("---")) {
                            location.getMunicipalityPostalcode().setLang(languageBoxCity.getSelectedItem().toString());
                            hasChanged = true;
                        }
                    }
                    if(StringUtils.isNotEmpty(countryTf.getText())) {
                        if(!countryTf.getText().equals(location.getCountry().getContent())) {
                            location.getCountry().setContent(countryTf.getText());
                            hasChanged = true;
                        }
                        if(!languageBoxCountry.getSelectedItem().toString().equals("---")) {
                            location.getCountry().setLang(languageBoxCountry.getSelectedItem().toString());
                            hasChanged = true;
                        }
                    }

                    if(StringUtils.isNotEmpty(coordinatesLatTf.getText()) && !coordinatesLatTf.getText().equals(location.getLatitude())) {
                        location.setLatitude(coordinatesLatTf.getText());
                        hasChanged = true;
                    }

                    if(StringUtils.isNotEmpty(coordinatesLongTf.getText()) && !coordinatesLongTf.getText().equals(location.getLongitude())) {
                        location.setLongitude(coordinatesLongTf.getText());
                        hasChanged = true;
                    }
                }

                if(!continentCombo.getSelectedItem().equals(repository.getGeogarea().getValue())) {
                    repository.getGeogarea().setValue(continentCombo.getSelectedItem().toString());
                    hasChanged = true;
                }

                if(repository.getTelephone().size() > 0) {
                    if(StringUtils.isNotEmpty(telephoneTf.getText()) && !telephoneTf.getText().equals(repository.getTelephone().get(0).getContent())) {
                        repository.getTelephone().get(0).setContent(telephoneTf.getText());
                    }
                }

                if(repository.getEmail().size() > 0) {
                    if(StringUtils.isNotEmpty(emailTf.getText()) && !emailTf.getText().equals(repository.getEmail().get(0).getHref())) {
                        repository.getEmail().get(0).setHref(emailTf.getText());
                        hasChanged = true;
                    }
                    if(StringUtils.isNotEmpty(emailTitleTf.getText()) && !emailTitleTf.getText().equals(repository.getEmail().get(0).getContent())) {
                        repository.getEmail().get(0).setContent(emailTitleTf.getText());
                        hasChanged = true;
                    } else if(StringUtils.isBlank(repository.getEmail().get(0).getContent())) {
                        repository.getEmail().get(0).setContent("Send an email");
                        hasChanged = true;
                    }
                }

                if(repository.getWebpage().size() > 0) {
                    if(StringUtils.isNotEmpty(webpageTf.getText()) && !webpageTf.getText().equals(repository.getWebpage().get(0).getHref())) {
                        repository.getWebpage().get(0).setHref(webpageTf.getText());
                        hasChanged = true;
                    }
                    if(StringUtils.isNotEmpty(webpageTitleTf.getText()) && !webpageTitleTf.getText().equals(repository.getWebpage().get(0).getContent())) {
                        repository.getWebpage().get(0).setContent(webpageTitleTf.getText());
                        hasChanged = true;
                    } else if(StringUtils.isBlank(repository.getWebpage().get(0).getContent())) {
                        repository.getWebpage().get(0).setContent("Visit website");
                        hasChanged = true;
                    }
                }

                if(StringUtils.isNotEmpty(openingTimesTf.getText()) && !openingTimesTf.getText().equals(repository.getTimetable().getOpening().getContent())) {
                    repository.getTimetable().getOpening().setContent(openingTimesTf.getText());
                    hasChanged = true;
                }

                if(StringUtils.isNotEmpty(closingTimesTf.getText())) {
                    if(repository.getTimetable().getClosing() == null)
                        repository.getTimetable().setClosing(new Closing());
                    if(!closingTimesTf.getText().equals(repository.getTimetable().getClosing().getContent())) {
                        repository.getTimetable().getClosing().setContent(closingTimesTf.getText());
                        hasChanged = true;
                    }
                }

                if(!accessiblePublicCombo.getSelectedItem().equals(repository.getAccess().getQuestion())) {
                    repository.getAccess().setQuestion(accessiblePublicCombo.getSelectedItem().toString());
                    hasChanged = true;
                }

                if(repository.getAccessibility().size() == 0) {
                    repository.setAccessibility(new ArrayList<Accessibility>(1));
                    repository.getAccessibility().add(new Accessibility());
                }
                if(!facilitiesForDisabledCombo.getSelectedItem().equals(repository.getAccessibility().get(0).getQuestion())) {
                    repository.getAccessibility().get(0).setQuestion(facilitiesForDisabledCombo.getSelectedItem().toString());
                    hasChanged = true;
                }

                if(eag.getRelations().getResourceRelation().size() > 0) {
                    if(StringUtils.isNotEmpty(refInstitutionHoldingsGuideTf.getText()) && !refInstitutionHoldingsGuideTf.getText().equals(eag.getRelations().getResourceRelation().get(0).getHref())) {
                        eag.getRelations().getResourceRelation().get(0).setHref(refInstitutionHoldingsGuideTf.getText());
                        hasChanged = true;
                    }

                    if(eag.getRelations().getResourceRelation().get(0).getDescriptiveNote() != null && eag.getRelations().getResourceRelation().get(0).getDescriptiveNote().getP().size() > 0) {
                        if(StringUtils.isNotEmpty(refInstitutionHoldingsGuideTitleTf.getText()) && !refInstitutionHoldingsGuideTitleTf.getText().equals(eag.getRelations().getResourceRelation().get(0).getDescriptiveNote().getP().get(0).getContent())) {
                            eag.getRelations().getResourceRelation().get(0).getDescriptiveNote().getP().get(0).setContent(refInstitutionHoldingsGuideTitleTf.getText());
                            hasChanged = true;
                        }
                    }
                }
            }
        }
    }
}
