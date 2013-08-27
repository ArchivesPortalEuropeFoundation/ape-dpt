package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import static eu.apenet.dpt.standalone.gui.eag2012.EagPanels.createErrorLabel;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.LocationType;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.TextFieldWithCheckbox;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.TextFieldWithLanguage;
import eu.apenet.dpt.utils.eag2012.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    private List<LocationType> locationFields;
    private JTextField telephoneTf;
    private JTextField emailTf;
    private JTextField emailTitleTf;
    private JTextField webpageTf;
    private JTextField webpageTitleTf;
    private List<TextFieldWithLanguage> openingHoursTfs;
    private List<TextFieldWithLanguage> closingDatesTfs;
    private JTextField refInstitutionHoldingsGuideTf;
    private JTextField refInstitutionHoldingsGuideTitleTf;

    private boolean isNew;
    private String countrycode;
    private String mainagencycode;

    public EagInstitutionPanel(Eag eag, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, boolean isNew, ResourceBundle labels, int repositoryNb) {
        this(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, "", "", repositoryNb);
    }
    public EagInstitutionPanel(Eag eag, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, boolean isNew, ResourceBundle labels, String countrycode, String mainagencycode, int repositoryNb) {
        super(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
        this.isNew = isNew;
        this.countrycode = countrycode;
        this.mainagencycode = mainagencycode;
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
        builder.addSeparator(labels.getString("eag2012.tab.yourInstitution"), cc.xyw(1, rowNb, 7));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.commons.personResponsible"),    cc.xy (1, rowNb));

        int sizeEvents = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size();
        if(sizeEvents == 0) {
            if(StringUtils.isNotEmpty(Eag2012Frame.getPersonResponsible())) {
                personTf = new JTextField(Eag2012Frame.getPersonResponsible());
            } else {
                personTf = new JTextField("");
            }
        } else {
            MaintenanceEvent event = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(sizeEvents - 1);
            personTf = new JTextField(event.getAgent().getContent());
        }

        builder.add(personTf, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.commons.countryCode") + "*",          cc.xy (1, rowNb));
        if(isNew && StringUtils.isEmpty(eag.getArchguide().getIdentity().getRepositorid().getCountrycode()))
            countryCodeTf = new JTextField(countrycode);
        else
            countryCodeTf = new JTextField(eag.getArchguide().getIdentity().getRepositorid().getCountrycode());
        countryCodeTf.addKeyListener(new CheckKeyListener());
        builder.add(countryCodeTf, cc.xy(3, rowNb));
        setNextRow();
        if(errors.contains("countryCodeTf")) {
            builder.add(createErrorLabel(labels.getString("eag2012.errors.countryCode")),          cc.xy(1, rowNb));
            setNextRow();
        }

        if(eag.getControl().getOtherRecordId().size() == 0) {
            eag.getControl().getOtherRecordId().add(new OtherRecordId());
            if(isNew && StringUtils.isNotEmpty(mainagencycode)) {
                eag.getControl().getOtherRecordId().get(0).setLocalType("yes");
                eag.getControl().getOtherRecordId().get(0).setValue(mainagencycode);
            }
        }

        otherRecordIdTfs = new ArrayList<TextFieldWithCheckbox>(eag.getControl().getOtherRecordId().size());
        for(OtherRecordId otherRecordId : eag.getControl().getOtherRecordId()) {
            TextFieldWithCheckbox textFieldWithCheckbox = new TextFieldWithCheckbox(otherRecordId.getValue(), otherRecordId.getLocalType());
            otherRecordIdTfs.add(textFieldWithCheckbox);
            builder.addLabel(labels.getString("eag2012.control.identifierInstitution"), cc.xy(1, rowNb));
            builder.add(textFieldWithCheckbox.getTextField(),               cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.isil.isThisISIL"), cc.xy(5, rowNb));
            textFieldWithCheckbox.getTextField().addKeyListener(new CheckKeyListener());
            builder.add(textFieldWithCheckbox.getIsilOrNotCombo(),               cc.xy (7, rowNb));
            textFieldWithCheckbox.getIsilOrNotCombo().addActionListener(new ComboboxActionListener(textFieldWithCheckbox));
            setNextRow();
        }

        JButton addNewOtherIdentifierBtn = new ButtonEag(labels.getString("eag2012.yourinstitution.addOtherIdentifier"));
        addNewOtherIdentifierBtn.addActionListener(new AddOtherIdentifierAction(eag, tabbedPane, model));
        builder.add(addNewOtherIdentifierBtn, cc.xy(3, rowNb));

        builder.addLabel(labels.getString("eag2012.commons.idUsedInApe"), cc.xy (5, rowNb));
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
            builder.addLabel(labels.getString("eag2012.commons.nameOfInstitution") + "*",    cc.xy (1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
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
            builder.addLabel(labels.getString("eag2012.commons.parallelNameOfInstitution"),    cc.xy (1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy (7, rowNb));
            setNextRow();
        }

//        if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) { //todo: BECAUSE FOR NOW ONLY ONE REPOSITORY!!!!
        Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);

        boolean hasMinimumOnePostalAddress = false;
        boolean hasMinimumOneVisitorAddress = false;
        locationFields = new ArrayList<LocationType>(repository.getLocation().size());
//        int locationNb = 0;
        for(Location location : repository.getLocation()) {
//            locationNb++;
            boolean isPostal = false;
            LocationType locationType = new LocationType(location);
            if(StringUtils.isEmpty(location.getLocalType()) || location.getLocalType().equals("visitors address")) {
                locationType.setLocalType("visitors address");
                builder.addSeparator(labels.getString("eag2012.commons.visitorsAddress"), cc.xyw(1, rowNb, 7));
                isPostal = false;
                hasMinimumOneVisitorAddress = true;
            } else if (location.getLocalType().equals("postal address")) {
                builder.addSeparator(labels.getString("eag2012.yourinstitution.postalAddress"), cc.xyw(1, rowNb, 7));
                isPostal = true;
                hasMinimumOnePostalAddress = true;
            }
            locationFields.add(locationType);
            setNextRow();

            String mandatoryStar = "*";
            if(isPostal)
                mandatoryStar = "";

            builder.addLabel(labels.getString("eag2012.commons.street") + mandatoryStar,    cc.xy (1, rowNb));
            builder.add(locationType.getStreetTf().getTextField(),                               cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(locationType.getStreetTf().getLanguageBox(),                               cc.xy (7, rowNb));
            if(errors.contains("streetTf")/*_" + locationNb)*/ && StringUtils.isEmpty(locationType.getStreetTfValue())) {
                setNextRow();
                builder.add(createErrorLabel(labels.getString("eag2012.errors.street")),          cc.xy (1, rowNb));
            }
            setNextRow();

            builder.addLabel(labels.getString("eag2012.commons.cityTownWithPostalcode") + mandatoryStar,    cc.xy (1, rowNb));

            builder.add(locationType.getCityTf().getTextField(),                               cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(locationType.getCityTf().getLanguageBox(),                               cc.xy (7, rowNb));
            if(errors.contains("cityTf")/*_" + locationNb)*/ && StringUtils.isEmpty(locationType.getCityTfValue())) {
                setNextRow();
                builder.add(createErrorLabel(labels.getString("eag2012.errors.city")),          cc.xy (1, rowNb));
            }
            setNextRow();

            if(!isPostal) {
                builder.addLabel(labels.getString("eag2012.commons.country") + "*",    cc.xy (1, rowNb));
                builder.add(locationType.getCountryTf().getTextField(), cc.xy (3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
                builder.add(locationType.getCountryTf().getLanguageBox(),                               cc.xy (7, rowNb));
                if(errors.contains("countryTf")/*_" + locationNb)*/ && StringUtils.isEmpty(locationType.getCountryTfValue())) {
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
        if(repository.getTelephone().size() > 0) {
            telephoneTf = new JTextField(repository.getTelephone().get(0).getContent());
        } else {
            telephoneTf = new JTextField();
        }
        builder.add(telephoneTf, cc.xy (3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.commons.email"), cc.xy(1, rowNb));
        if(repository.getEmail().size() > 0) {
            emailTf = new JTextField(repository.getEmail().get(0).getHref());
            emailTitleTf = new JTextField(repository.getEmail().get(0).getContent());
        } else {
            emailTf = new JTextField();
            emailTitleTf = new JTextField();
        }
        builder.add(emailTf, cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.commons.linkTitle"),             cc.xy (5, rowNb));
        builder.add(emailTitleTf,                                            cc.xy (7, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.commons.webpage"), cc.xy(1, rowNb));
        if(repository.getWebpage().size() > 0) {
            webpageTf = new JTextField(repository.getWebpage().get(0).getHref());
            if(!StringUtils.isBlank(repository.getWebpage().get(0).getContent()))
                webpageTitleTf = new JTextField(repository.getWebpage().get(0).getContent());
            else
                webpageTitleTf = new JTextField();
        } else {
            webpageTf = new JTextField();
            webpageTitleTf = new JTextField();
        }
        builder.add(webpageTf, cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.commons.linkTitle"),             cc.xy (5, rowNb));
        builder.add(webpageTitleTf,                                            cc.xy (7, rowNb));
        setNextRow();
        if((StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)) || errors.contains("webpageTf")) {
            builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
            setNextRow();
        }
        
        if(repository.getTimetable().getOpening().size() == 0) {
            repository.getTimetable().getOpening().add(new Opening());
        }
        openingHoursTfs = new ArrayList<TextFieldWithLanguage>(repository.getTimetable().getOpening().size());
        for(Opening opening : repository.getTimetable().getOpening()) {
            builder.addLabel(labels.getString("eag2012.commons.openingHours") + "*",    cc.xy (1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(opening.getContent(), opening.getLang());
            openingHoursTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        if(errors.contains("openingHoursTfs")) {
            builder.add(createErrorLabel(labels.getString("eag2012.errors.openingHours")), cc.xy(1, rowNb));
            setNextRow();
        }

        if(repository.getTimetable().getClosing().size() == 0) {
            repository.getTimetable().getClosing().add(new Closing());
        }
        closingDatesTfs = new ArrayList<TextFieldWithLanguage>(repository.getTimetable().getClosing().size());
        for(Closing closing : repository.getTimetable().getClosing()) {
            builder.addLabel(labels.getString("eag2012.commons.closingDates"), cc.xy(1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(closing.getContent(), closing.getLang());
            closingDatesTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }

        builder.addLabel(labels.getString("eag2012.commons.accessiblePublic") + "*", cc.xy(1, rowNb));
        if(repository.getAccess() != null && Arrays.asList(yesOrNo).contains(repository.getAccess().getQuestion())) {
            accessiblePublicCombo.setSelectedItem(repository.getAccess().getQuestion());
        }
        builder.add(accessiblePublicCombo, cc.xy(3, rowNb));
        setNextRow();
        builder.addLabel(labels.getString("eag2012.commons.disabledAccess") + "*", cc.xy(1, rowNb));
        if(repository.getAccessibility().size() > 0 && Arrays.asList(yesOrNo).contains(repository.getAccessibility().get(0).getQuestion())) {
            facilitiesForDisabledCombo.setSelectedItem(repository.getAccessibility().get(0).getQuestion());
        }
        builder.add(facilitiesForDisabledCombo, cc.xy(3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.yourinstitution.linkToHoldingsGuide"), cc.xy(1, rowNb));
        if(eag.getRelations() == null) {
            eag.setRelations(new Relations());
        }
        if(eag.getRelations().getResourceRelation().size() > 0) {
            refInstitutionHoldingsGuideTf = new JTextField(eag.getRelations().getResourceRelation().get(0).getHref());
            if(eag.getRelations().getResourceRelation().get(0).getRelationEntry() != null)
                refInstitutionHoldingsGuideTitleTf = new JTextField(eag.getRelations().getResourceRelation().get(0).getRelationEntry().getContent());
            else
                refInstitutionHoldingsGuideTitleTf = new JTextField();
        } else {
            refInstitutionHoldingsGuideTf = new JTextField();
            refInstitutionHoldingsGuideTitleTf = new JTextField();
        }
        builder.add(refInstitutionHoldingsGuideTf, cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.commons.linkTitle"),             cc.xy (5, rowNb));
        builder.add(refInstitutionHoldingsGuideTitleTf, cc.xy(7, rowNb));
        setNextRow();
        if((StringUtils.isNotBlank(refInstitutionHoldingsGuideTf.getText()) && !StringUtils.startsWithAny(refInstitutionHoldingsGuideTf.getText(), webPrefixes)) || errors.contains("refInstitutionHoldingsGuideTf")) {
            builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
            setNextRow();
        }
//        }

        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();

        JButton addRepositoryBtn = new ButtonEag(labels.getString("eag2012.yourinstitution.addRepositoryTabButton"));
        builder.add(addRepositoryBtn, cc.xy (1, rowNb));
        addRepositoryBtn.addActionListener(new AddRepositoryTabButton());

        JButton nextTabBtn = new ButtonEag(labels.getString("eag2012.commons.nextTab"));
        builder.add(nextTabBtn, cc.xy (5, rowNb));
        nextTabBtn.addActionListener(new NextTabBtnAction(eag, tabbedPane, model));

        setNextRow();
        JButton exitBtn = new ButtonEag(labels.getString("eag2012.commons.exit"));
        builder.add(exitBtn, cc.xy(1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());
        JButton saveBtn = new ButtonEag(labels.getString("eag2012.commons.save"));
        builder.add(saveBtn, cc.xy (5, rowNb));
        saveBtn.addActionListener(new SaveBtnAction(eag, tabbedPane, model));

        setNextRow();
        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();
        JButton nextInstitutionTabBtn = new ButtonEag(labels.getString("eag2012.controls.nextInstitution"));
        nextInstitutionTabBtn.addActionListener(new NextInstitutionTabBtnAction(eag, tabbedPane, model));
        builder.add(nextInstitutionTabBtn, cc.xy(5, rowNb));

//        if(tabbedPane.getChangeListeners().length < 2) {
//            LOG.info("Add listener");
//            tabbedPane.addChangeListener(new TabChangeListener(eag, tabbedPane, model));
//        }

        return builder.getPanel();
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
                reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 0);
            }
        }
    }

    public class AddRepositoryTabButton implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
//            mainTabbedPane.add(labels.getString("eag2012.tab.extraRepository") + " " + mainTabbedPane.getTabCount(), null);
            mainTabbedPane.add(labels.getString("eag2012.tab.extraRepository") + " " + mainTabbedPane.getTabCount(), new EagNewRepositoryPanel(eag, null, mainTabbedPane, eag2012Frame, model, labels, mainTabbedPane.getTabCount()).buildInstitutionTabbedPane(isNew, countrycode, mainagencycode));
            mainTabbedPane.setEnabledAt(mainTabbedPane.getTabCount() - 1, false);
        }
    }

    public class NextTabBtnAction extends UpdateEagObject {
        NextTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(true);
                reloadTabbedPanel(new EagIdentityPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 1);
                tabbedPane.setEnabledAt(1, true);
                tabbedPane.setEnabledAt(0, false);
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 0);
            }
        }

//        public void actionPerformed(ActionEvent actionEvent) {
//            LOG.info("NextTabBtn: " + locationFields.size());
//            tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
//        }
    }

    public class NextInstitutionTabBtnAction extends UpdateEagObject {
        NextInstitutionTabBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateEagObject(true);

                int currentTabIndex = mainTabbedPane.getSelectedIndex();
                if(currentTabIndex+1 < mainTabbedPane.getTabCount()) {
                    reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 0);
                    mainTabbedPane.setEnabledAt(currentTabIndex, false);
                    mainTabbedPane.setEnabledAt(currentTabIndex+1, true);
                    mainTabbedPane.setSelectedIndex(currentTabIndex+1);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 0);
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
                super.updateEagObject(false);
            } catch (Eag2012FormException e) {
//                for(String error : getErrors().keySet()) {
//                    System.out.println(error);
//                }
            }
            eag.getControl().getOtherRecordId().add(new OtherRecordId());
            reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 0);
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
            reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 0);
        }
    }

    public abstract class UpdateEagObject extends DefaultBtnAction {

        UpdateEagObject(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        protected void updateEagObject(boolean save) throws Eag2012FormException {
            errors = new ArrayList<String>();

            boolean hasChanged = false;
            if(StringUtils.isNotEmpty(personTf.getText())) {
                Eag2012Frame.setPersonResponsible(personTf.getText());
            }
            if(save) {
                if(Eag2012Frame.getTimeMaintenance() == null)
                    Eag2012Frame.setTimeMaintenance(new Date());
                int sizeEvents = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size();
                String languagePerson = "";
                if(sizeEvents > 0) {
                    MaintenanceEvent event = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(sizeEvents - 1);
                    languagePerson = event.getAgent().getLang();
                }
                MaintenanceEvent event = TextChanger.getMaintenanceEventSaved(Eag2012Frame.getTimeMaintenance(), eag.getControl().getMaintenanceHistory().getMaintenanceEvent());
                if(event == null) {
                    event = new MaintenanceEvent();
                    event.setAgent(new Agent());
                    event.setEventType(new EventType());
                    if(isNew)
                        event.getEventType().setValue("created");
                    else
                        event.getEventType().setValue("updated");
                    event.setAgentType(new AgentType());
                    event.getAgentType().setValue("human");
                    event.setEventDateTime(new EventDateTime());
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                    SimpleDateFormat formatStandard = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    event.getEventDateTime().setContent(format.format(Eag2012Frame.getTimeMaintenance()));
                    event.getEventDateTime().setStandardDateTime(formatStandard.format(Eag2012Frame.getTimeMaintenance()));
                } else {
                    eag.getControl().getMaintenanceHistory().getMaintenanceEvent().remove(event);
                }
                if(StringUtils.isNotEmpty(personTf.getText())) {
                    event.getAgentType().setValue("human");
                    event.getAgent().setContent(personTf.getText());
                } else {
                    event.getAgentType().setValue("human");
                    event.getAgent().setContent("automatically created agent");
                }
                if(StringUtils.isNotEmpty(languagePerson))
                    event.getAgent().setLang(languagePerson);
                eag.getControl().getMaintenanceHistory().getMaintenanceEvent().add(event);
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

//            if(eag.getArchguide().getDesc().getRepositories().getRepository().size() == 1) { //todo: BECAUSE FOR NOW ONLY ONE REPOSITORY!!!!
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);
            repository.getLocation().clear();

            String defaultCountry = "";
//            int locationNb = 0;
            for(LocationType locationType : locationFields) {
//                locationNb++;
                if(StringUtils.isNotEmpty(locationType.getCountryTfValue())) {
                    defaultCountry = locationType.getCountryTfValue();
                }
                Location location = locationType.getLocation(defaultCountry);//, locationNb);
                errors.addAll(locationType.getErrors());
                if(location != null) {
                    repository.getLocation().add(location);
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
            } else if(StringUtils.isNotEmpty(telephoneTf.getText())) {
                repository.getTelephone().add(new Telephone());
                repository.getTelephone().get(0).setContent(telephoneTf.getText());
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
                    repository.getEmail().get(0).setContent(emailTf.getText());
                    hasChanged = true;
                }
                if(StringUtils.isEmpty(emailTf.getText())) {
                    repository.getEmail().remove(0);
                }
            }  else if(StringUtils.isNotEmpty(emailTf.getText())) {
                repository.getEmail().add(new Email());
                repository.getEmail().get(0).setHref(emailTf.getText());
                repository.getEmail().get(0).setContent(emailTitleTf.getText());
            }

            if(StringUtils.isNotEmpty(webpageTf.getText())) {
                if(repository.getWebpage().size() == 0)
                    repository.getWebpage().add(new Webpage());
                repository.getWebpage().get(0).setHref(webpageTf.getText());
                if(!StringUtils.startsWithAny(webpageTf.getText(), webPrefixes))
                    errors.add("webpageTf");
                hasChanged = true;
                if(StringUtils.isNotEmpty(webpageTitleTf.getText()) && !webpageTitleTf.getText().equals(repository.getWebpage().get(0).getContent())) {
                    repository.getWebpage().get(0).setContent(webpageTitleTf.getText());
                }
            } else if(repository.getWebpage().size() > 0) {
                repository.getWebpage().remove(0);
            }

            boolean openingTimeExists = false;
            if(openingHoursTfs.size() > 0) {
                repository.getTimetable().getOpening().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : openingHoursTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        Opening opening = new Opening();
                        opening.setContent(textFieldWithLanguage.getTextValue());
                        opening.setLang(textFieldWithLanguage.getLanguage());
                        repository.getTimetable().getOpening().add(opening);
                        openingTimeExists = true;
                    }
                }
            }
            if(!openingTimeExists) {
                errors.add("openingHoursTfs");
            }

            if(closingDatesTfs.size() > 0) {
                repository.getTimetable().getClosing().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : closingDatesTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        Closing closing = new Closing();
                        closing.setContent(textFieldWithLanguage.getTextValue());
                        closing.setLang(textFieldWithLanguage.getLanguage());
                        repository.getTimetable().getClosing().add(closing);
                    }
                }
            }

            if(repository.getAccess() == null) {
                repository.setAccess(new Access());
            }
            if(!accessiblePublicCombo.getSelectedItem().equals(repository.getAccess().getQuestion())) {
                repository.getAccess().setQuestion(accessiblePublicCombo.getSelectedItem().toString());
                hasChanged = true;
            }

            if(repository.getAccessibility().size() == 0) {
                repository.getAccessibility().add(new Accessibility());
            }
            repository.getAccessibility().get(0).setQuestion(facilitiesForDisabledCombo.getSelectedItem().toString());

            if(eag.getRelations() == null)
                eag.setRelations(new Relations());
            if(eag.getRelations().getResourceRelation().size() > 0) {
                if(StringUtils.isNotEmpty(refInstitutionHoldingsGuideTf.getText()) && !refInstitutionHoldingsGuideTf.getText().equals(eag.getRelations().getResourceRelation().get(0).getHref())) {
                    eag.getRelations().getResourceRelation().get(0).setHref(refInstitutionHoldingsGuideTf.getText());
                    if(!StringUtils.startsWithAny(refInstitutionHoldingsGuideTf.getText(), webPrefixes))
                        errors.add("refInstitutionHoldingsGuideTf");
                    hasChanged = true;
                }

                if(eag.getRelations().getResourceRelation().get(0).getRelationEntry() != null) {
                    if(StringUtils.isNotEmpty(refInstitutionHoldingsGuideTitleTf.getText())) {
                        eag.getRelations().getResourceRelation().get(0).getRelationEntry().setContent(refInstitutionHoldingsGuideTitleTf.getText());
                        hasChanged = true;
                    }
                }
                if(StringUtils.isEmpty(refInstitutionHoldingsGuideTf.getText())) {
                    eag.getRelations().getResourceRelation().remove(0);
                }
            } else if(StringUtils.isNotEmpty(refInstitutionHoldingsGuideTf.getText())) {
                eag.getRelations().getResourceRelation().add(new ResourceRelation());
                eag.getRelations().getResourceRelation().get(0).setResourceRelationType("creatorOf");
                eag.getRelations().getResourceRelation().get(0).setHref(refInstitutionHoldingsGuideTf.getText());
                eag.getRelations().getResourceRelation().get(0).setRelationEntry(new RelationEntry());
                eag.getRelations().getResourceRelation().get(0).getRelationEntry().setContent(refInstitutionHoldingsGuideTitleTf.getText());
            } else if(eag.getRelations().getEagRelation().size() == 0) {
                eag.setRelations(null);
            }
//            }

            for(int i = 0; i < eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size(); i ++) {
                MaintenanceEvent maintenanceEvent = eag.getControl().getMaintenanceHistory().getMaintenanceEvent().get(i);
                if(StringUtils.isEmpty(maintenanceEvent.getAgent().getContent()) && StringUtils.isEmpty(maintenanceEvent.getEventDateTime().getStandardDateTime()) && save) {
                    if(eag.getControl().getMaintenanceHistory().getMaintenanceEvent().size() == 1) {
                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                        SimpleDateFormat formatStandard = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        maintenanceEvent.getEventDateTime().setContent(format.format(date));
                        maintenanceEvent.getEventDateTime().setStandardDateTime(formatStandard.format(date));
                    } else {
                        eag.getControl().getMaintenanceHistory().getMaintenanceEvent().remove(maintenanceEvent);
                    }
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
            if(textFieldWithCheckbox.getisilOrNotComboValue().equals("yes")) {
                int counter = 0;
                for(TextFieldWithCheckbox textFieldWithCheckbox1 : otherRecordIdTfs) {
                    if(textFieldWithCheckbox1.getisilOrNotComboValue().equals("yes")) {
                        counter++;
                    }
                }
                if(counter > 1)
                    textFieldWithCheckbox.getIsilOrNotCombo().setSelectedItem("no");
            }
            idUsedInApeTf.setText(TextChanger.getNewText(otherRecordIdTfs, countryCodeTf.getText()));
        }
    }

    public class CheckKeyListener implements KeyListener {

        public void keyTyped(KeyEvent keyEvent) {
        }

        public void keyPressed(KeyEvent keyEvent) {
        }

        public void keyReleased(KeyEvent keyEvent) {
            idUsedInApeTf.setText(TextChanger.getNewText(otherRecordIdTfs, countryCodeTf.getText()));
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
//            LOG.info("Tab listener: " + locationFields.size());
//            LOG.debug("stateChanged (mainTabbed index: " + mainTabbedPane.getSelectedIndex() + ") + (changeListener NB: " + tabbedPane.getChangeListeners().length + ")");
//            LOG.debug(click + " - " + Eag2012Frame.firstTimeInTab);
//            if(click && !Eag2012Frame.firstTimeInTab) {
//                LOG.debug("Remove listener");
//                tabbedPane.removeChangeListener(this);
//                try {
//                    super.updateEagObject(true);
//                    LOG.debug("Ok");
//                    Eag2012Frame.firstTimeInTab = true;
//                    EagPanels eagPanels = getCorrectEagPanels(tabbedPane.getSelectedIndex(), mainTabbedPane, eag2012Frame, labels, repositoryNb);
//                    reloadTabbedPanel(eagPanels.buildEditorPanel(errors), tabbedPane.getSelectedIndex());
//                } catch (Eag2012FormException e) {
//                    LOG.debug("NOT Ok");
//                    EagPanels eagPanels = getCorrectEagPanels(0, mainTabbedPane, eag2012Frame, labels, repositoryNb);
//                    reloadTabbedPanel(eagPanels.buildEditorPanel(errors), 0);
//                }
//                click = false;
//            }
//            Eag2012Frame.firstTimeInTab = false;
//        }
//    }
}
