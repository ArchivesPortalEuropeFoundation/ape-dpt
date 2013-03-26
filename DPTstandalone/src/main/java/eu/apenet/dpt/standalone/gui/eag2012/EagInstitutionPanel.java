package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 03/10/2012
 *
 * @author Yoann Moranville
 */
public class EagInstitutionPanel extends EagPanels {

    private JTextField personTf;
    private JTextField countryCodeTf;
    private JTextField idUsedInApeTf;
    private List<TextFieldWithCheckbox> otherRecordIdTfs;
    private List<TextFieldWithLanguage> nameInstitutionTfs;
    private List<TextFieldWithLanguage> parallelNameTfs;
    private TextFieldWithLanguage streetTf;
    private TextFieldWithLanguage cityTf;
    private TextFieldWithLanguage countryTf;
    private JTextField coordinatesLatTf;
    private JTextField coordinatesLongTf;
    private TextFieldWithLanguage streetPTf;
    private TextFieldWithLanguage cityPTf;
    private JTextField telephoneTf;
    private JTextField emailTf;
    private JTextField emailTitleTf;
    private JTextField webpageTf;
    private JTextField webpageTitleTf;
    private JTextField openingTimesTf;
    private JTextField closingTimesTf;
    private JTextField refInstitutionHoldingsGuideTf;
    private JTextField refInstitutionHoldingsGuideTitleTf;
    private JTextField workplacesSearchroomTf;

    private boolean isNew;
    private boolean isStartOfForm;

    public EagInstitutionPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame, ProfileListModel model, boolean isNew, boolean isStartOfForm, ResourceBundle labels) {
        super(eag, tabbedPane, eag2012Frame, model, labels);
        this.isNew = isNew;
        this.isStartOfForm = isStartOfForm;
    }

    /**
     * Builds and answer the editor's general tab for the given layout.
     * @return the editor's general panel
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
                "right:max(50dlu;p), 4dlu, 100dlu, 7dlu, right:p, 4dlu, 100dlu",
                EDITOR_ROW_SPEC);

        layout.setColumnGroups(new int[][] { { 1, 3, 5, 7 } });
        PanelBuilder builder = new PanelBuilder(layout);

        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        rowNb = 1;
        builder.addSeparator(labels.getString("eag2012.YourInstitution"), cc.xyw(1, rowNb, 7));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.personResponsibleLabel"),    cc.xy (1, rowNb));

        if(isStartOfForm) {    //todo: ALL OF THIS should actually be in the save function, here just a check on whether something exists or not.
            if(eag.getControl().getMaintenanceHistory() == null)
                eag.getControl().setMaintenanceHistory(new MaintenanceHistory());
            MaintenanceEvent maintenanceEvent = new MaintenanceEvent();
            AgentType agentType = new AgentType();
            agentType.setValue("human");
            maintenanceEvent.setAgentType(agentType);
            EventDateTime eventDateTime = new EventDateTime();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat formatStandard = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            eventDateTime.setContent(format.format(date));
            eventDateTime.setStandardDateTime(formatStandard.format(date));
            maintenanceEvent.setEventDateTime(eventDateTime);
            EventType eventType = new EventType();
            if(isNew)
                eventType.setValue("created");
            else
                eventType.setValue("updated");
            maintenanceEvent.setEventType(eventType);
            eag.getControl().getMaintenanceHistory().getMaintenanceEvent().add(maintenanceEvent);
            int sizeEvents = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size();
            MaintenanceEvent event = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(sizeEvents - 1);
            event.setAgent(new Agent());
            personTf = new JTextField(event.getAgent().getContent());
            isStartOfForm = false;
        } else {
            int sizeEvents = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size();
            MaintenanceEvent event = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(sizeEvents - 1);
            personTf = new JTextField(event.getAgent().getContent());
        }
        builder.add(personTf, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.countryCodeLabel") + "*",          cc.xy (1, rowNb));
        countryCodeTf = new JTextField(eag.getArchguide().getIdentity().getRepositorid().getCountrycode());
        builder.add(countryCodeTf, cc.xy(3, rowNb));
        setNextRow();
        if(errors.contains("countryCodeTf")) {
            builder.add(createErrorLabel(labels.getString("eag2012.errors.countryCode")),          cc.xy(1, rowNb));
            setNextRow();
        }

        otherRecordIdTfs = new ArrayList<TextFieldWithCheckbox>(eag.getControl().getOtherRecordId().size());
        int nbOtherRecordIds = 0;
        for(OtherRecordId otherRecordId : eag.getControl().getOtherRecordId()) {
            TextFieldWithCheckbox textFieldWithCheckbox = new TextFieldWithCheckbox(otherRecordId.getValue(), otherRecordId.getLocalType());
            otherRecordIdTfs.add(textFieldWithCheckbox);
            if(nbOtherRecordIds++ == 0)
                builder.addLabel(labels.getString("eag2012.identifierInstitutionLabel") + "*",      cc.xy (1, rowNb));
            else
                builder.addLabel(labels.getString("eag2012.identifierInstitutionLabel"), cc.xy(1, rowNb));
            builder.add(textFieldWithCheckbox.getTextField(),               cc.xy (3, rowNb));
            textFieldWithCheckbox.getTextField().addKeyListener(new CheckKeyListener(textFieldWithCheckbox));
            builder.add(textFieldWithCheckbox.getIsilOrNotCombo(),               cc.xy (7, rowNb));
            textFieldWithCheckbox.getIsilOrNotCombo().addActionListener(new ComboboxActionListener(textFieldWithCheckbox));
            setNextRow();
        }
        if(errors.contains("otherRecordIdTfs")) {
            builder.add(createErrorLabel(labels.getString("eag2012.errors.otherId")),          cc.xy (1, rowNb));
            setNextRow();
        }

        JButton addNewOtherIdentifierBtn = new ButtonEag(labels.getString("eag2012.addOtherIdentifier"));
        addNewOtherIdentifierBtn.addActionListener(new AddOtherIdentifierAction(eag, tabbedPane, model));
        builder.add(addNewOtherIdentifierBtn, cc.xy(3, rowNb));

        builder.addLabel(labels.getString("eag2012.idUsedInApeLabel"), cc.xy (5, rowNb));
        if(Eag2012ValidFields.isRepositoryCodeCorrect(eag.getControl().getRecordId().getValue()))
            idUsedInApeTf = new JTextField(eag.getControl().getRecordId().getValue());
        else
            idUsedInApeTf = new JTextField(TextChanger.getNewText(otherRecordIdTfs, countryCodeTf.getText()));
        idUsedInApeTf.setEnabled(false);
        idUsedInApeTf.setEditable(false);
        builder.add(idUsedInApeTf,                                           cc.xy (7, rowNb));
        setNextRow();

        if(eag.getArchguide().getIdentity().getAutform().size() == 0)
            eag.getArchguide().getIdentity().getAutform().add(new Autform());
        nameInstitutionTfs = new ArrayList<TextFieldWithLanguage>(eag.getArchguide().getIdentity().getAutform().size());
        for(Autform autform : eag.getArchguide().getIdentity().getAutform()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(autform.getContent(), autform.getLang());
            nameInstitutionTfs.add(textFieldWithLanguage);
            builder.addLabel(labels.getString("eag2012.nameOfInstitutionLabel") + "*",    cc.xy (1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy (7, rowNb));
            setNextRow();
        }
        if(errors.contains("nameInstitutionTfs")) {
            builder.add(createErrorLabel(labels.getString("eag2012.errors.nameInstitutionEmpty")),          cc.xy (1, rowNb));
            setNextRow();
        }

        if(eag.getArchguide().getIdentity().getParform().size() == 0)
            eag.getArchguide().getIdentity().getParform().add(new Parform());
        parallelNameTfs = new ArrayList<TextFieldWithLanguage>(eag.getArchguide().getIdentity().getParform().size());
        for(Parform parform : eag.getArchguide().getIdentity().getParform()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(parform.getContent(), parform.getLang());
            parallelNameTfs.add(textFieldWithLanguage);
            builder.addLabel(labels.getString("eag2012.parallelNameOfInstitutionLabel"),    cc.xy (1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy (7, rowNb));
            setNextRow();
        }

        if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) { //todo: BECAUSE FOR NOW ONLY ONE REPOSITORY!!!!
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

                String mandatoryStar = "*";
                if(isPostal)
                    mandatoryStar = "";

                builder.addLabel(labels.getString("eag2012.streetLabel") + mandatoryStar,    cc.xy (1, rowNb));
                if(StringUtils.isNotEmpty(location.getStreet().getContent())) {
                    if(!isPostal)
                        streetTf = new TextFieldWithLanguage(location.getStreet().getContent(), location.getStreet().getLang());
                    else
                        streetPTf = new TextFieldWithLanguage(location.getStreet().getContent(), location.getStreet().getLang());
                } else {
                    if(!isPostal)
                        streetTf = new TextFieldWithLanguage("", "");
                    else
                        streetPTf = new TextFieldWithLanguage("", "");
                }
                if(!isPostal) {
                    builder.add(streetTf.getTextField(),                               cc.xy (3, rowNb));
                    builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
                    builder.add(streetTf.getLanguageBox(),                               cc.xy (7, rowNb));
                    if(errors.contains("streetTf")) {
                        setNextRow();
                        builder.add(createErrorLabel(labels.getString("eag2012.errors.street")),          cc.xy (1, rowNb));
                    }
                } else {
                    builder.add(streetPTf.getTextField(),                               cc.xy (3, rowNb));
                    builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
                    builder.add(streetPTf.getLanguageBox(),                               cc.xy (7, rowNb));
                }
                setNextRow();

                builder.addLabel(labels.getString("eag2012.cityTownLabel") + mandatoryStar,    cc.xy (1, rowNb));
                if(StringUtils.isNotEmpty(location.getMunicipalityPostalcode().getContent())) {
                    if(!isPostal)
                        cityTf = new TextFieldWithLanguage(location.getMunicipalityPostalcode().getContent(), location.getMunicipalityPostalcode().getLang());
                    else
                        cityPTf = new TextFieldWithLanguage(location.getMunicipalityPostalcode().getContent(), location.getMunicipalityPostalcode().getLang());
                } else {
                    if(!isPostal)
                        cityTf = new TextFieldWithLanguage("" , "");
                    else
                        cityPTf = new TextFieldWithLanguage("", "");
                }
                if(!isPostal) {
                    builder.add(cityTf.getTextField(),                               cc.xy (3, rowNb));
                    builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
                    builder.add(cityTf.getLanguageBox(),                               cc.xy (7, rowNb));
                    if(errors.contains("cityTf")) {
                        setNextRow();
                        builder.add(createErrorLabel(labels.getString("eag2012.errors.city")),          cc.xy (1, rowNb));
                    }
                } else {
                    builder.add(cityPTf.getTextField(),                               cc.xy (3, rowNb));
                    builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
                    builder.add(cityPTf.getLanguageBox(),                               cc.xy (7, rowNb));
                }
                setNextRow();

                if(!isPostal) {
                    builder.addLabel(labels.getString("eag2012.countryLabel") + "*",    cc.xy (1, rowNb));
                    if(StringUtils.isNotEmpty(location.getCountry().getContent()))
                        countryTf = new TextFieldWithLanguage(location.getCountry().getContent(), location.getCountry().getLang());
                    else
                        countryTf = new TextFieldWithLanguage("", "");

                    builder.add(countryTf.getTextField(), cc.xy (3, rowNb));
                    builder.addLabel(labels.getString("eag2012.language"),    cc.xy (5, rowNb));
                    builder.add(countryTf.getLanguageBox(),                               cc.xy (7, rowNb));
                    if(errors.contains("countryTf")) {
                        setNextRow();
                        builder.add(createErrorLabel(labels.getString("eag2012.errors.country")),          cc.xy (1, rowNb));
                    }
                    setNextRow();

                    builder.addLabel(labels.getString("eag2012.coordinatesLatitudeLabel"),    cc.xy (1, rowNb));
                    coordinatesLatTf = new JTextField(location.getLatitude());
                    builder.add(coordinatesLatTf, cc.xy (3, rowNb));
                    builder.addLabel(labels.getString("eag2012.coordinatesLongitudeLabel"), cc.xy(5, rowNb));
                    coordinatesLongTf = new JTextField(location.getLongitude());
                    builder.add(coordinatesLongTf, cc.xy (7, rowNb));
                    setNextRow();
                }
            }

            if(repository.getLocation().size() < 2) { //If equal or more than 2, we already have visitors and postal addresses
                JButton addNewPostalAddressBtn = new ButtonEag(labels.getString("eag2012.addPostalAddress"));
                addNewPostalAddressBtn.addActionListener(new AddPostalAddressAction(eag, tabbedPane, model));
                builder.add(addNewPostalAddressBtn, cc.xy(3, rowNb));
                setNextRow();
            }

            builder.addLabel(labels.getString("eag2012.continentLabel") + "*",    cc.xy (1, rowNb));
            if(Arrays.asList(continents).contains(repository.getGeogarea().getValue())){
                continentCombo.setSelectedItem(repository.getGeogarea().getValue());
            } else {
                continentCombo.setSelectedItem("Europe");
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

            builder.addLabel(labels.getString("eag2012.webpageLabel") + "*",    cc.xy (1, rowNb));
            if(repository.getWebpage().size() > 0) {
                webpageTf = new JTextField(repository.getWebpage().get(0).getHref());
                if(!StringUtils.isBlank(repository.getWebpage().get(0).getContent()))
                    webpageTitleTf = new JTextField(repository.getWebpage().get(0).getContent());
                else
                    webpageTitleTf = new JTextField("Go to our homepage");
            } else {
                webpageTf = new JTextField();
                webpageTitleTf = new JTextField();
            }
            builder.add(webpageTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkTitleLabel"),             cc.xy (5, rowNb));
            builder.add(webpageTitleTf,                                            cc.xy (7, rowNb));
            if(errors.contains("webpageTf")) {
                setNextRow();
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpage")),          cc.xy (1, rowNb));
            }
            setNextRow();

            builder.addLabel(labels.getString("eag2012.openingTimesLabel") + "*",    cc.xy (1, rowNb));
            openingTimesTf = new JTextField(repository.getTimetable().getOpening().getContent());
            builder.add(openingTimesTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.closingTimesLabel"), cc.xy(5, rowNb));
            if(repository.getTimetable().getClosing() == null)
                repository.getTimetable().setClosing(new Closing());
            closingTimesTf = new JTextField(repository.getTimetable().getClosing().getContent());
            builder.add(closingTimesTf,                                            cc.xy (7, rowNb));
            if(errors.contains("openingTimesTf")) {
                setNextRow();
                builder.add(createErrorLabel(labels.getString("eag2012.errors.openingTimes")),          cc.xy (1, rowNb));
            }
            setNextRow();
            builder.addLabel(labels.getString("eag2012.accessiblePublicLabel") + "*",    cc.xy (1, rowNb));
            if(Arrays.asList(yesOrNo).contains(repository.getAccess().getQuestion())) {
                accessiblePublicCombo.setSelectedItem(repository.getAccess().getQuestion());
            }
            builder.add(accessiblePublicCombo, cc.xy(3, rowNb));
            setNextRow();
            builder.addLabel(labels.getString("eag2012.facilitiesForDisabledLabel") + "*", cc.xy(1, rowNb));
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

            if(repository.getServices() == null) {
                repository.setServices(new Services());
                repository.getServices().setSearchroom(new Searchroom());
            }
            Searchroom searchroom = repository.getServices().getSearchroom();
            if(searchroom.getWorkPlaces() == null)
                searchroom.setWorkPlaces(new WorkPlaces());
            builder.addLabel(labels.getString("eag2012.workplaces") + "*",    cc.xy (1, rowNb));
            try {
                workplacesSearchroomTf = new JTextField(searchroom.getWorkPlaces().getNum().getContent());
            } catch (NullPointerException npe) {
                workplacesSearchroomTf = new JTextField();
            }
            builder.add(workplacesSearchroomTf,    cc.xy (3, rowNb));
            if(errors.contains("workplacesSearchroomTf")) {
                setNextRow();
                builder.add(createErrorLabel(labels.getString("eag2012.errors.workplaces")),          cc.xy (1, rowNb));
            }
            setNextRow();
        }

        JButton exitBtn = new ButtonEag(labels.getString("eag2012.exitButton"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        JButton nextTabBtn = new ButtonEag(labels.getString("eag2012.nextTabButton"));
        builder.add(nextTabBtn, cc.xy (5, rowNb));
//        builder.add(new ButtonEag(labels.getString("eag2012.validateButton")), cc.xy (5, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());
        nextTabBtn.addActionListener(new NextTabBtnAction(eag, tabbedPane, model));

        if(Utilities.isDev) {
            setNextRow();
            JButton saveBtn = new ButtonEag(labels.getString("eag2012.saveButton"));
            builder.add(saveBtn, cc.xy (5, rowNb));
            saveBtn.addActionListener(new SaveBtnAction(eag, tabbedPane, model));
        }

        return builder.getPanel();
    }

    public class SaveBtnAction extends UpdateEagObject {
        SaveBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
                super.saveFile(eag.getControl().getRecordId().getValue());
                closeFrame();
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, eag2012Frame, model, isNew, isStartOfForm, labels).buildEditorPanel(errors), 0);
            }
        }
    }

    public class NextTabBtnAction extends UpdateEagObject {
        NextTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();

                if(model == null)
                    LOG.info("The model is null, we can not add the EAG to the list...");

                reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, eag2012Frame, model, labels).buildEditorPanel(errors), 1);
                tabbedPane.setEnabledAt(1, true);
                tabbedPane.setEnabledAt(0, false);
            } catch (Eag2012FormException e) {
                if(model == null)
                    LOG.info("The model is null, we can not add the EAG to the list...");

                reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, eag2012Frame, model, isNew, isStartOfForm, labels).buildEditorPanel(errors), 0);
            }
        }
    }

    public class AddOtherIdentifierAction extends UpdateEagObject {
        AddOtherIdentifierAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject();
            } catch (Eag2012FormException e) {
//                for(String error : getErrors().keySet()) {
//                    System.out.println(error);
//                }
            }
            eag.getControl().getOtherRecordId().add(new OtherRecordId());
            reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, eag2012Frame, model, isNew, isStartOfForm, labels).buildEditorPanel(errors), 0);
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
//                for(String error : getErrors().keySet()) {
//                    System.out.println(error);
//                }
            }

            Location location = new Location();
            location.setLocalType("postal address");
            location.setCountry(new Country());
            location.setStreet(new Street());
            location.setMunicipalityPostalcode(new MunicipalityPostalcode());

            eag.getArchguide().getDesc().getRepositories().getRepository().get(0).getLocation().add(location);
            reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, eag2012Frame, model, isNew, isStartOfForm, labels).buildEditorPanel(errors), 0);
        }

    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        UpdateEagObject(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        protected void updateEagObject() throws Eag2012FormException {
            errors = new ArrayList<String>();

            boolean hasChanged = false;

            if(StringUtils.isNotEmpty(personTf.getText())) {
                MaintenanceEvent event = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size() - 1);
                eag.getControl().getMaintenanceHistory().getMaintenanceEvent().remove(event);
                event.getAgent().setContent(personTf.getText());
                eag.getControl().getMaintenanceHistory().getMaintenanceEvent().add(event);
                isNew = false;
            }

            if(StringUtils.isEmpty(countryCodeTf.getText()) || !Eag2012ValidFields.isCountryCodeCorrect(countryCodeTf.getText())) {
                errors.add("countryCodeTf");
            } else if(notEqual(countryCodeTf.getText(), eag.getArchguide().getIdentity().getRepositorid().getCountrycode())) {
                eag.getArchguide().getIdentity().getRepositorid().setCountrycode(countryCodeTf.getText());
                hasChanged = true;
            }

            if(notEqual(idUsedInApeTf.getText(), eag.getControl().getRecordId().getValue())) {
                eag.getControl().getRecordId().setValue(idUsedInApeTf.getText());
                hasChanged = true;
            }
            if(otherRecordIdTfs.size() > 0) {
                eag.getControl().getOtherRecordId().clear();
                for(TextFieldWithCheckbox textFieldWithCheckbox : otherRecordIdTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithCheckbox.getTextFieldValue())) {
                        OtherRecordId otherRecordId = new OtherRecordId();
                        otherRecordId.setValue(textFieldWithCheckbox.getTextFieldValue());
                        otherRecordId.setLocalType(textFieldWithCheckbox.getisilOrNotComboValue());
                        eag.getControl().getOtherRecordId().add(otherRecordId);
                        hasChanged = true;
                    }
                }
            }

            boolean error = true;
            for(TextFieldWithLanguage textFieldWithLanguage : nameInstitutionTfs) {
                if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextField().getText())) {
                    error = false;
                    break;
                }
            }
            if(error || nameInstitutionTfs.size() == 0) {
                errors.add("nameInstitutionTfs");
            }
            if(!error) {
                eag.getArchguide().getIdentity().getAutform().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : nameInstitutionTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        Autform autform = new Autform();
                        autform.setContent(textFieldWithLanguage.getTextValue());
                        autform.setLang(textFieldWithLanguage.getLanguage());
                        eag.getArchguide().getIdentity().getAutform().add(autform);
                        hasChanged = true;
                    }
                }
            }

            eag.getArchguide().getIdentity().getParform().clear();
            for(TextFieldWithLanguage textFieldWithLanguage : parallelNameTfs) {
                if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                    Parform parform = new Parform();
                    parform.setContent(textFieldWithLanguage.getTextValue());
                    parform.setLang(textFieldWithLanguage.getLanguage());
                    eag.getArchguide().getIdentity().getParform().add(parform);
                    hasChanged = true;
                }
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) { //todo: BECAUSE FOR NOW ONLY ONE REPOSITORY!!!!
                Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
                if(repository.getLocation().size() > 0) { //todo: Same reason as earlier
                    Location location = repository.getLocation().get(0);
                    location.setLocalType("visitors address");
                    if(StringUtils.isNotEmpty(streetTf.getTextValue())) {
                        if(!streetTf.getTextValue().equals(location.getStreet().getContent())) {
                            location.getStreet().setContent(streetTf.getTextValue());
                            location.getStreet().setLang(streetTf.getLanguage());
                            hasChanged = true;
                        }
                    } else {
                        errors.add("streetTf");
                    }
                    if(StringUtils.isNotEmpty(cityTf.getTextValue())) {
                        if(!cityTf.getTextValue().equals(location.getMunicipalityPostalcode().getContent())) {
                            location.getMunicipalityPostalcode().setContent(cityTf.getTextValue());
                            location.getMunicipalityPostalcode().setLang(cityTf.getLanguage());
                            hasChanged = true;
                        }
                    } else {
                        errors.add("cityTf");
                    }
                    if(StringUtils.isNotEmpty(countryTf.getTextValue())) {
                        if(!countryTf.getTextValue().equals(location.getCountry().getContent())) {
                            location.getCountry().setContent(countryTf.getTextValue());
                            location.getCountry().setLang(countryTf.getLanguage());
                            hasChanged = true;
                        }
                    } else {
                        errors.add("countryTf");
                    }

                    if(StringUtils.isNotEmpty(coordinatesLatTf.getText()) && !coordinatesLongTf.getText().equals(location.getLongitude())) {
                        location.setLatitude(coordinatesLatTf.getText());
                        hasChanged = true;
                    }

                    if(StringUtils.isNotEmpty(coordinatesLongTf.getText()) && !coordinatesLongTf.getText().equals(location.getLongitude())) {
                        location.setLongitude(coordinatesLongTf.getText());
                        hasChanged = true;
                    }

                    if(cityPTf != null && StringUtils.isNotEmpty(cityPTf.getTextValue()) && repository.getLocation().size() < 2) {
                        location = new Location();
                        location.setStreet(new Street());
                        location.setMunicipalityPostalcode(new MunicipalityPostalcode());
                        location.setCountry(new Country());
                        location.setFirstdem(new Firstdem());
                        location.setSecondem(new Secondem());
                        location.setLocalentity(new Localentity());
                        repository.getLocation().add(new Location());
                    }

                    if(repository.getLocation().size() > 1) {
                        location = repository.getLocation().get(1);
                        location.setLocalType("postal address");
                        if(StringUtils.isNotEmpty(streetPTf.getTextValue())) {
                            if(!streetPTf.getTextValue().equals(location.getStreet().getContent())) {
                                location.getStreet().setContent(streetPTf.getTextValue());
                                location.getStreet().setLang(streetPTf.getLanguage());
                                hasChanged = true;
                            }
                        }
                        if(StringUtils.isNotEmpty(cityPTf.getTextValue())) {
                            if(!cityPTf.getTextValue().equals(location.getMunicipalityPostalcode().getContent())) {
                                location.getMunicipalityPostalcode().setContent(cityPTf.getTextValue());
                                location.getMunicipalityPostalcode().setLang(cityPTf.getLanguage());
                                hasChanged = true;
                            }
                        }

                        if(StringUtils.isNotEmpty(countryTf.getTextValue())) {
                            if(!countryTf.getTextValue().equals(location.getCountry().getContent())) {
                                location.getCountry().setContent(countryTf.getTextValue());
                                location.getCountry().setLang(countryTf.getLanguage());
                                hasChanged = true;
                            }
                        }
                    }

                }

                if(!continentCombo.getSelectedItem().equals(repository.getGeogarea().getValue())) {
                    repository.getGeogarea().setValue(continentCombo.getSelectedItem().toString());
                    hasChanged = true;
                }

                if(repository.getTelephone().size() > 0) {
                    if(StringUtils.isNotEmpty(telephoneTf.getText()) && !telephoneTf.getText().equals(repository.getTelephone().get(0).getContent())) {
                        repository.getTelephone().get(0).setContent(telephoneTf.getText());
                    } else if(StringUtils.isEmpty(telephoneTf.getText())) {
                        repository.getTelephone().remove(0);
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
                    if(StringUtils.isEmpty(emailTf.getText())) {
                        repository.getEmail().remove(0);
                    }
                }

//                if(repository.getWebpage().size() > 0) {
                    if(StringUtils.isNotEmpty(webpageTf.getText())) {
                        repository.getWebpage().get(0).setHref(webpageTf.getText());
                        hasChanged = true;
                        if(StringUtils.isNotEmpty(webpageTitleTf.getText()) && !webpageTitleTf.getText().equals(repository.getWebpage().get(0).getContent())) {
                            repository.getWebpage().get(0).setContent(webpageTitleTf.getText());
                        } else if(StringUtils.isBlank(repository.getWebpage().get(0).getContent())) {
                            repository.getWebpage().get(0).setContent("Go to our homepage");
                        }
                    } else {
                        errors.add("webpageTf");
                    }
//                } else {

//                }

                if(StringUtils.isNotEmpty(openingTimesTf.getText())) {
                    repository.getTimetable().getOpening().setContent(openingTimesTf.getText());
                    hasChanged = true;
                } else {
                    errors.add("openingTimesTf");
                }

                if(StringUtils.isNotEmpty(closingTimesTf.getText())) {
                    if(repository.getTimetable().getClosing() == null)
                        repository.getTimetable().setClosing(new Closing());
                    if(!closingTimesTf.getText().equals(repository.getTimetable().getClosing().getContent())) {
                        repository.getTimetable().getClosing().setContent(closingTimesTf.getText());
                        hasChanged = true;
                    }
                } else {
                    repository.getTimetable().setClosing(null);
                }

                if(!accessiblePublicCombo.getSelectedItem().equals(repository.getAccess().getQuestion())) {
                    repository.getAccess().setQuestion(accessiblePublicCombo.getSelectedItem().toString());
                    hasChanged = true;
                }

                if(repository.getAccessibility().size() == 0) {
                    repository.getAccessibility().add(new Accessibility());
                }
                repository.getAccessibility().get(0).setQuestion(facilitiesForDisabledCombo.getSelectedItem().toString());

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
                    if(StringUtils.isEmpty(refInstitutionHoldingsGuideTf.getText())) {
                        eag.getRelations().getResourceRelation().remove(0);
                    }
                }

                Searchroom searchroom = repository.getServices().getSearchroom();
                if(StringUtils.isNotEmpty(workplacesSearchroomTf.getText())) {
                    Num num = new Num();
                    num.setUnit("site");
                    num.setContent(workplacesSearchroomTf.getText());
                    searchroom.getWorkPlaces().setNum(num);
                } else {
                    errors.add("workplacesSearchroomTf");
                }

            }

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }

    public class ComboboxActionListener implements ActionListener {
        private TextFieldWithCheckbox textFieldWithCheckbox;

        public ComboboxActionListener(TextFieldWithCheckbox textFieldWithCheckbox) {
            this.textFieldWithCheckbox = textFieldWithCheckbox;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if(textFieldWithCheckbox.getisilOrNotComboValue().equals("ISIL")) {
                int counter = 0;
                for(TextFieldWithCheckbox textFieldWithCheckbox1 : otherRecordIdTfs) {
                    if(textFieldWithCheckbox1.getisilOrNotComboValue().equals("ISIL")) {
                        counter++;
                    }
                }
                if(counter > 1)
                    textFieldWithCheckbox.getIsilOrNotCombo().setSelectedItem("notISIL");
            }
            idUsedInApeTf.setText(TextChanger.getNewText(otherRecordIdTfs, countryCodeTf.getText()));
        }
    }

    public class CheckKeyListener implements KeyListener {
        private TextFieldWithCheckbox textFieldWithCheckbox;

        public CheckKeyListener(TextFieldWithCheckbox textFieldWithCheckbox) {
            this.textFieldWithCheckbox = textFieldWithCheckbox;
        }

        public void keyTyped(KeyEvent keyEvent) {
        }

        public void keyPressed(KeyEvent keyEvent) {
        }

        public void keyReleased(KeyEvent keyEvent) {
            idUsedInApeTf.setText(TextChanger.getNewText(otherRecordIdTfs, countryCodeTf.getText()));
        }
    }
}
