package eu.apenet.dpt.standalone.gui.eag2012;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.Utilities;
import eu.apenet.dpt.standalone.gui.commons.ButtonTab;
import eu.apenet.dpt.standalone.gui.commons.DefaultBtnAction;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextAreaWithLanguage;
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithLanguage;
import eu.apenet.dpt.utils.eag2012.Accessibility;
import eu.apenet.dpt.utils.eag2012.AdvancedOrders;
import eu.apenet.dpt.utils.eag2012.Citation;
import eu.apenet.dpt.utils.eag2012.Closing;
import eu.apenet.dpt.utils.eag2012.ComputerPlaces;
import eu.apenet.dpt.utils.eag2012.Contact;
import eu.apenet.dpt.utils.eag2012.DescriptiveNote;
import eu.apenet.dpt.utils.eag2012.Digitalser;
import eu.apenet.dpt.utils.eag2012.Directions;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Email;
import eu.apenet.dpt.utils.eag2012.Exhibition;
import eu.apenet.dpt.utils.eag2012.InternetAccess;
import eu.apenet.dpt.utils.eag2012.Library;
import eu.apenet.dpt.utils.eag2012.MicrofilmPlaces;
import eu.apenet.dpt.utils.eag2012.Microformser;
import eu.apenet.dpt.utils.eag2012.Monographicpub;
import eu.apenet.dpt.utils.eag2012.Num;
import eu.apenet.dpt.utils.eag2012.Opening;
import eu.apenet.dpt.utils.eag2012.OtherServices;
import eu.apenet.dpt.utils.eag2012.P;
import eu.apenet.dpt.utils.eag2012.Photocopyser;
import eu.apenet.dpt.utils.eag2012.PhotographAllowance;
import eu.apenet.dpt.utils.eag2012.Photographser;
import eu.apenet.dpt.utils.eag2012.ReadersTicket;
import eu.apenet.dpt.utils.eag2012.RecreationalServices;
import eu.apenet.dpt.utils.eag2012.Refreshment;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.Reproductionser;
import eu.apenet.dpt.utils.eag2012.ResearchServices;
import eu.apenet.dpt.utils.eag2012.Restaccess;
import eu.apenet.dpt.utils.eag2012.Restorationlab;
import eu.apenet.dpt.utils.eag2012.Searchroom;
import eu.apenet.dpt.utils.eag2012.Serialpub;
import eu.apenet.dpt.utils.eag2012.Services;
import eu.apenet.dpt.utils.eag2012.Techservices;
import eu.apenet.dpt.utils.eag2012.Telephone;
import eu.apenet.dpt.utils.eag2012.TermsOfUse;
import eu.apenet.dpt.utils.eag2012.ToursSessions;
import eu.apenet.dpt.utils.eag2012.Webpage;
import eu.apenet.dpt.utils.eag2012.WorkPlaces;

/**
 * User: Yoann Moranville
 * Date: 28/11/2012
 *
 * @author Yoann Moranville
 */
public class EagAccessAndServicesPanel extends EagPanels {
    private List<TextAreaWithLanguage> openingHoursTfs;
    private List<TextAreaWithLanguage> closingDatesTfs;
    private List<TextAreaWithLanguage> travellingDirectionsTfs;
    private List<TextAreaWithLanguage> restaccessTfs;
    private List<TextAreaWithLanguage> termsOfUseTfs;
    private List<TextAreaWithLanguage> accessibilityTfs;
    private List<JTextField> telephoneSearchroomTf;
    private List<JTextField> emailSearchroomTf;
    private List<JTextField> emailTitleSearchroomTf;
    private List<JTextField> webpageSearchroomTf;
    private List<JTextField> webpageTitleSearchroomTf;
    private JTextField workplacesSearchroomTf;
    private JTextField computerplacesSearchroomTf;
    private List<TextFieldWithLanguage> computerplacesDescriptionTfs;
    private JTextField microfilmplacesSearchroomTf;
    private List<TextFieldWithLanguage> readersticketSearchroomTfs;
    private List<TextFieldWithLanguage> advancedordersSearchroomTfs;
    private List<TextFieldWithLanguage> researchServicesSearchroomTfs;
    private List<JTextField> telephoneLibraryTf;
    private List<JTextField> emailLibraryTf;
    private List<JTextField> emailTitleLibraryTf;
    private ArrayList<JTextField> webpageLibraryTf;
    private ArrayList<JTextField> webpageTitleLibraryTf;
    private JTextField monographicPubLibraryTf;
    private JTextField serialPubLibraryTf;
    private List<TextFieldWithLanguage> internetAccessDescTfs;
    private List<JTextField> telephoneRestorationlabTf;
    private List<JTextField> emailRestorationlabTf;
    private List<JTextField> emailTitleRestorationlabTf;
    private List<JTextField> webpageRestorationlabTf;
    private List<JTextField> webpageTitleRestorationlabTf;
    private List<TextFieldWithLanguage> descriptionRestorationServiceTfs;
    private List<JTextField> telephoneReproductionServiceTf;
    private List<JTextField> emailReproductionServiceTf;
    private List<JTextField> emailTitleReproductionServiceTf;
    private List<JTextField> webpageReproductionServiceTf;
    private List<JTextField> webpageTitleReproductionServiceTf;
    private List<TextFieldWithLanguage> descriptionReproductionServiceTfs;
    private JComboBox microformServicesCombo = new JComboBox(yesOrNoNotMandatory);
    private JComboBox photographServicesCombo = new JComboBox(yesOrNoNotMandatory);
    private JComboBox digitalServicesCombo = new JComboBox(yesOrNoNotMandatory);
    private JComboBox photocopyServicesCombo = new JComboBox(yesOrNoNotMandatory);
    private TextAreaWithLanguage refreshmentTf;

    private List<TextAreaWithLanguage> exhibitionTfs;
    private List<TextAreaWithLanguage> toursAndSessionsTfs;
    private List<TextAreaWithLanguage> otherServicesTfs;

    public EagAccessAndServicesPanel(Eag eag, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eag2012Frame, ProfileListModel model, ResourceBundle labels, int repositoryNb) {
        super(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb);
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

        //opening hours
        if(repository.getTimetable().getOpening().size() == 0) {
            repository.getTimetable().getOpening().add(new Opening());
        }
        openingHoursTfs = new ArrayList<TextAreaWithLanguage>(repository.getTimetable().getOpening().size());
        for(Opening opening : repository.getTimetable().getOpening()) {
            builder.addLabel(labels.getString("eag2012.commons.openingHours") + "*",    cc.xy (1, rowNb));
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(opening.getContent(), opening.getLang());
            openingHoursTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        if(errors.contains("openingHoursTfs")) {
            builder.add(createErrorLabel(labels.getString("eag2012.errors.openingHours")),          cc.xy (1, rowNb));
            setNextRow();
        }
        //add opening hours button
        JButton addOpeningHoursBtn = new ButtonTab(labels.getString("eag2012.commons.addOpeningHours"));
        builder.add(addOpeningHoursBtn, cc.xy (1, rowNb));
        addOpeningHoursBtn.addActionListener(new AddOpeningHoursBtnAction(eag, tabbedPane, model));
        setNextRow();

        if(repository.getTimetable().getClosing().size() == 0) {
            repository.getTimetable().getClosing().add(new Closing());
        }
        closingDatesTfs = new ArrayList<TextAreaWithLanguage>(repository.getTimetable().getClosing().size());
        for(Closing closing : repository.getTimetable().getClosing()) {
            builder.addLabel(labels.getString("eag2012.commons.closingDates"), cc.xy(1, rowNb));
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(closing.getContent(), closing.getLang());
            closingDatesTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"),    cc.xy (5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addClosingDatesBtn = new ButtonTab(labels.getString("eag2012.commons.addClosingDates"));
        builder.add(addClosingDatesBtn, cc.xy (1, rowNb));
        addClosingDatesBtn.addActionListener(new AddClosingDatesBtnAction(eag, tabbedPane, model));
        setNextRow();

        if(repository.getDirections().size() == 0)
            repository.getDirections().add(new Directions());
        travellingDirectionsTfs = new ArrayList<TextAreaWithLanguage>(repository.getDirections().size());
        for(Directions directions : repository.getDirections()) {
            builder.addLabel(labels.getString("eag2012.accessAndServices.travellingDirections"),    cc.xy (1, rowNb));
            String str = "";
            String citation = "";
            for(Object obj : directions.getContent()) {
                if(obj instanceof String) {
                    str += (String)obj;
                } else if(obj instanceof Citation) {
                    citation += ((Citation) obj).getHref();
                }
            }
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(str, directions.getLang(), citation);
            travellingDirectionsTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.accessAndServices.link"), cc.xy(1, rowNb));
            builder.add(textAreaWithLanguage.getExtraField(), cc.xy(3, rowNb));
            setNextRow();
            if(errors.contains("travellingDirectionsTfs")) {
                if(StringUtils.isNotBlank(textAreaWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textAreaWithLanguage.getExtraField().getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(textAreaWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textAreaWithLanguage.getExtraField().getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }
        }

        JButton addTravellingDirectionsBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addTravellingDirections"));
        builder.add(addTravellingDirectionsBtn, cc.xy (1, rowNb));
        addTravellingDirectionsBtn.addActionListener(new AddTravellingDirectionsBtnAction(eag, tabbedPane, model));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.commons.accessiblePublic") + "*",    cc.xy (1, rowNb));
        if(Arrays.asList(yesOrNo).contains(repository.getAccess().getQuestion())) {
            accessiblePublicCombo.setSelectedItem(repository.getAccess().getQuestion());
        }
        builder.add(accessiblePublicCombo, cc.xy(3, rowNb));
        setNextRow();

        if(repository.getAccess().getRestaccess().size() == 0)
            repository.getAccess().getRestaccess().add(new Restaccess());
        restaccessTfs = new ArrayList<TextAreaWithLanguage>(repository.getAccess().getRestaccess().size());
        int last = repository.getAccess().getRestaccess().size() - 1;
        for(Restaccess restaccess : repository.getAccess().getRestaccess()) {
            builder.addLabel(labels.getString("eag2012.accessAndServices.accessRestrictions"),    cc.xy (1, rowNb));
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(restaccess.getContent(), restaccess.getLang());
            restaccessTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
            if(last-- == 0) {
                JButton addRestaccessBtn = new ButtonTab(labels.getString("eag2012.commons.addFutherAccessInformation"));
                builder.add(addRestaccessBtn, cc.xy (1, rowNb));
                addRestaccessBtn.addActionListener(new AddRestaccessBtnAction(eag, tabbedPane, model));
                setNextRow();
            }
        }


        if(repository.getAccess().getTermsOfUse().size() == 0)
            repository.getAccess().getTermsOfUse().add(new TermsOfUse());
        termsOfUseTfs = new ArrayList<TextAreaWithLanguage>(repository.getAccess().getTermsOfUse().size());
        for(TermsOfUse termsOfUse : repository.getAccess().getTermsOfUse()) {
            builder.addLabel(labels.getString("eag2012.accessAndServices.termsOfUse"),    cc.xy (1, rowNb));
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(termsOfUse.getContent(), termsOfUse.getLang(), termsOfUse.getHref());
            termsOfUseTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.accessAndServices.link"), cc.xy(1, rowNb));
            builder.add(textAreaWithLanguage.getExtraField(), cc.xy(3, rowNb));
            setNextRow();
            if(errors.contains("termsOfUseTfs")) {
                if(StringUtils.isNotBlank(textAreaWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textAreaWithLanguage.getExtraField().getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(textAreaWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textAreaWithLanguage.getExtraField().getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }
        }
        
        //ad further button
        JButton addTermsOfUseBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addFurtherTermsOfUse"));
        builder.add(addTermsOfUseBtn, cc.xy (1, rowNb));
        addTermsOfUseBtn.addActionListener(new addTermsOfUseBtnAction(eag, tabbedPane, model));
        setNextRow();
        

        builder.addLabel(labels.getString("eag2012.commons.disabledAccess") + "*", cc.xy(1, rowNb));
        if(repository.getAccessibility().size() > 0 && Arrays.asList(yesOrNo).contains(repository.getAccessibility().get(0).getQuestion())) {
            facilitiesForDisabledCombo.setSelectedItem(repository.getAccessibility().get(0).getQuestion());
        }
        builder.add(facilitiesForDisabledCombo, cc.xy(3, rowNb));
        setNextRow();
        
//      //opening hours
        addOpeningHoursBtn.addActionListener(new AddOpeningHoursBtnAction(eag, tabbedPane, model));
        
        //facilities for disabled persons
        accessibilityTfs = new ArrayList<TextAreaWithLanguage>(repository.getAccessibility().size());
        for(Accessibility accessibility : repository.getAccessibility()) {
            builder.addLabel(labels.getString("eag2012.commons.disabledAccess.facilities"), cc.xy (1, rowNb));
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(accessibility.getContent(), accessibility.getLang());
            accessibilityTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(), cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            if(last-- == 0) {
                JButton addAccessibilityBtn = new ButtonTab(labels.getString("eag2012.yourinstitution.addInfoOnExistingFacilities"));
                builder.add(addAccessibilityBtn, cc.xy (7, rowNb));
                addAccessibilityBtn.addActionListener(new AddAccessibilityBtnAction(eag, tabbedPane, model));
            }
            setNextRow();
        }
        
        //add button
        JButton addFacilitiesForDisabledBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addFurtherFacilitiesForDisabled"));
        builder.add(addFacilitiesForDisabledBtn, cc.xy (1, rowNb));
        addFacilitiesForDisabledBtn.addActionListener(new addFacilitiesForDisabledBtnAction(eag, tabbedPane, model));
        setNextRow();
        
        builder.addSeparator(labels.getString("eag2012.accessAndServices.searchroom"), cc.xyw(1, rowNb, 7));
        setNextRow();

       if(repository.getServices() == null)
            repository.setServices(new Services());
        if(repository.getServices().getSearchroom() == null)
            repository.getServices().setSearchroom(new Searchroom());
        Searchroom searchroom = repository.getServices().getSearchroom();

        if(searchroom.getContact() == null)
            searchroom.setContact(new Contact());

        //(searchroom.getContact().getTelephone()
        builder.addLabel(labels.getString("eag2012.commons.telephone"), cc.xy(1, rowNb));
        int i = 0;
        telephoneSearchroomTf = new ArrayList<JTextField>(searchroom.getContact().getTelephone().size());
        for(Telephone telephone : searchroom.getContact().getTelephone()) {
            JTextField telephoneTf = new JTextField(telephone.getContent());
            telephoneSearchroomTf.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            if(i++ == 0) {
                JButton addtelephoneSearchroomTfBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
                addtelephoneSearchroomTfBtn.addActionListener(new AddTelephoneSearchroomBtnAction(eag, tabbedPane, model));
                builder.add(addtelephoneSearchroomTfBtn, cc.xy(5, rowNb));
            }
            setNextRow();
        }
        if(searchroom.getContact().getTelephone().size() == 0) {
            JTextField telephoneTf = new JTextField();
            telephoneSearchroomTf.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            JButton addtelephoneSearchroomTfBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
            addtelephoneSearchroomTfBtn.addActionListener(new AddTelephoneSearchroomBtnAction(eag, tabbedPane, model));
            builder.add(addtelephoneSearchroomTfBtn, cc.xy(5, rowNb));
            setNextRow();
        }

        //searchroom.getContact().getEmail()
        emailSearchroomTf = new ArrayList<JTextField>(searchroom.getContact().getEmail().size());
        emailTitleSearchroomTf = new ArrayList<JTextField>(searchroom.getContact().getEmail().size());
        if(searchroom.getContact().getEmail().size() == 0)
        	searchroom.getContact().getEmail().add(new Email());
        for(Email email : searchroom.getContact().getEmail()) {
            JTextField emailTf = new JTextField(email.getHref());
            JTextField emailTitleTf = new JTextField(email.getContent());
            emailSearchroomTf.add(emailTf);
            emailTitleSearchroomTf.add(emailTitleTf);
            builder.addLabel(labels.getString("eag2012.commons.email"),cc.xy (1, rowNb));
            builder.add(emailTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),cc.xy (5, rowNb));
            builder.add(emailTitleTf,cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addEmailSearchroomBtn = new ButtonTab(labels.getString("eag2012.commons.addEmail"));
        addEmailSearchroomBtn.addActionListener(new AddEmailSearchroomAction(eag, tabbedPane, model));
        builder.add(addEmailSearchroomBtn, cc.xy(1, rowNb));
        setNextRow();

		//searchroom.getWebpage()
        webpageSearchroomTf = new ArrayList<JTextField>(searchroom.getWebpage().size());
        webpageTitleSearchroomTf = new ArrayList<JTextField>(searchroom.getWebpage().size());
        if(searchroom.getWebpage().size() == 0)
        	searchroom.getWebpage().add(new Webpage());
        for(Webpage webpage : searchroom.getWebpage()) {
            JTextField webpageTf = new JTextField(webpage.getHref());
            JTextField webpageTitleTf = new JTextField(webpage.getContent());
            webpageTitleSearchroomTf.add(webpageTitleTf);
        	webpageSearchroomTf.add(webpageTf);
    		builder.addLabel(labels.getString("eag2012.commons.webpage"),    cc.xy (1, rowNb));
            builder.add(webpageTf,    cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),    cc.xy (5, rowNb));
            builder.add(webpageTitleTf,    cc.xy (7, rowNb));
            setNextRow();
            if(errors.contains("webpageSearchroomTf")) {
                if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")), cc.xyw(1, rowNb, 3));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),cc.xyw(1, rowNb, 3));
                setNextRow();
            }
        }
        JButton addWebpageSearchroomBtn = new ButtonTab(labels.getString("eag2012.commons.addWebpage"));
        addWebpageSearchroomBtn.addActionListener(new AddWebpageSearchroomAction(eag, tabbedPane, model));
        builder.add(addWebpageSearchroomBtn, cc.xy(1, rowNb));
        setNextRow();


        if(searchroom.getWorkPlaces() == null)
            searchroom.setWorkPlaces(new WorkPlaces());
        builder.addLabel(labels.getString("eag2012.commons.workPlaces"),    cc.xy (1, rowNb));
        try {
            workplacesSearchroomTf = new JTextField(searchroom.getWorkPlaces().getNum().getContent());
        } catch (NullPointerException npe) {
            workplacesSearchroomTf = new JTextField();
        }
        builder.add(workplacesSearchroomTf,    cc.xy (3, rowNb));
        setNextRow();

        if(searchroom.getComputerPlaces() == null) {
            ComputerPlaces computerPlaces = new ComputerPlaces();
            Num num = new Num();
            num.setUnit("site");
            computerPlaces.setNum(num);
            searchroom.setComputerPlaces(computerPlaces);
        }
        builder.addLabel(labels.getString("eag2012.accessAndServices.computerPlaces"),    cc.xy (1, rowNb));
        computerplacesSearchroomTf = new JTextField(searchroom.getComputerPlaces().getNum().getContent());
        builder.add(computerplacesSearchroomTf,    cc.xy (3, rowNb));
        if(searchroom.getComputerPlaces().getDescriptiveNote() == null){
            JButton addDescriptionBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addDescription"));
            builder.add(addDescriptionBtn, cc.xy (5, rowNb));
            addDescriptionBtn.addActionListener(new AddComputerplacesDescriptionBtnAction(eag, tabbedPane, model));
        }
        setNextRow();
        if(searchroom.getComputerPlaces().getDescriptiveNote() != null){
            computerplacesDescriptionTfs = new ArrayList<TextFieldWithLanguage>(searchroom.getComputerPlaces().getDescriptiveNote().getP().size());
            for (P p : searchroom.getComputerPlaces().getDescriptiveNote().getP()) {
                builder.addLabel(labels.getString("eag2012.commons.computerplacesDescription"), cc.xy(1, rowNb));
                TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(p.getContent(), p.getLang());
                computerplacesDescriptionTfs.add(textFieldWithLanguage);
                builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
                builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
                setNextRow();
            }
            JButton addDescriptionBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addDescription"));
            builder.add(addDescriptionBtn, cc.xy (5, rowNb));
            addDescriptionBtn.addActionListener(new AddComputerplacesDescriptionBtnAction(eag, tabbedPane, model));
            setNextRow();
        }

        if(searchroom.getMicrofilmPlaces() == null) {
            MicrofilmPlaces microfilmPlaces = new MicrofilmPlaces();
            Num num = new Num();
            num.setUnit("site");
            microfilmPlaces.setNum(num);
            searchroom.setMicrofilmPlaces(microfilmPlaces);
        }
        if(searchroom.getPhotographAllowance() == null) {
            searchroom.setPhotographAllowance(new PhotographAllowance());
        }
        builder.addLabel(labels.getString("eag2012.accessAndServices.microfilmPlaces"),    cc.xy (1, rowNb));
        microfilmplacesSearchroomTf = new JTextField(searchroom.getMicrofilmPlaces().getNum().getContent());
        builder.add(microfilmplacesSearchroomTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.accessAndServices.photographAllowance"),    cc.xy (5, rowNb));
        if(Arrays.asList(photographAllowance).contains(searchroom.getPhotographAllowance().getValue())) {
            photographAllowanceCombo.setSelectedItem(searchroom.getPhotographAllowance().getValue());
        } else {
            photographAllowanceCombo.setSelectedItem("---");
        }
        builder.add(photographAllowanceCombo, cc.xy(7, rowNb));
        setNextRow();

        if(searchroom.getReadersTicket().size() == 0)
            searchroom.getReadersTicket().add(new ReadersTicket());
        readersticketSearchroomTfs = new ArrayList<TextFieldWithLanguage>(searchroom.getReadersTicket().size());
        for(ReadersTicket readersTicket : searchroom.getReadersTicket()) {
            builder.addLabel(labels.getString("eag2012.accessAndServices.readersTicket"),    cc.xy (1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(readersTicket.getContent(), readersTicket.getLang(), readersTicket.getHref());
            readersticketSearchroomTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.accessAndServices.link"), cc.xy(1, rowNb));
            builder.add(textFieldWithLanguage.getExtraField(),                                            cc.xy (3, rowNb));
            setNextRow();
            if(errors.contains("readersticketSearchroomTfs")) {
                if(StringUtils.isNotBlank(textFieldWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textFieldWithLanguage.getExtraField().getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(textFieldWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textFieldWithLanguage.getExtraField().getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }
        }
        JButton addReadersticketBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addReadersTicket"));
        builder.add(addReadersticketBtn, cc.xy (1, rowNb));
        addReadersticketBtn.addActionListener(new AddReadersticketBtnAction(eag, tabbedPane, model));
        setNextRow();

        if(searchroom.getAdvancedOrders().size() == 0)
            searchroom.getAdvancedOrders().add(new AdvancedOrders());
        advancedordersSearchroomTfs = new ArrayList<TextFieldWithLanguage>(searchroom.getAdvancedOrders().size());
        for(AdvancedOrders advancedOrders : searchroom.getAdvancedOrders()) {
            builder.addLabel(labels.getString("eag2012.accessAndServices.advancedOrders"),    cc.xy (1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(advancedOrders.getContent(), advancedOrders.getLang(), advancedOrders.getHref());
            advancedordersSearchroomTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.accessAndServices.link"), cc.xy(1, rowNb));
            builder.add(textFieldWithLanguage.getExtraField(),                                            cc.xy (3, rowNb));
            setNextRow();
            if(errors.contains("advancedordersSearchroomTfs")) {
                if(StringUtils.isNotBlank(textFieldWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textFieldWithLanguage.getExtraField().getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(textFieldWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textFieldWithLanguage.getExtraField().getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }
        }
        JButton addAdvancedordersBtn = new ButtonTab(labels.getString("eag2012.control.advancedOrders"));
        builder.add(addAdvancedordersBtn, cc.xy (1, rowNb));
        addAdvancedordersBtn.addActionListener(new AddAdvancedordersBtnAction(eag, tabbedPane, model));
        setNextRow();

        if(searchroom.getResearchServices().size() == 0) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            ResearchServices researchServices = new ResearchServices();
            researchServices.setDescriptiveNote(descriptiveNote);
            searchroom.getResearchServices().add(researchServices);
        }
        researchServicesSearchroomTfs = new ArrayList<TextFieldWithLanguage>(searchroom.getResearchServices().size());
        for(ResearchServices researchServices : searchroom.getResearchServices()) {
            if(researchServices.getDescriptiveNote() == null) {
                DescriptiveNote descriptiveNote = new DescriptiveNote();
                descriptiveNote.setP(new ArrayList<P>(){{ add(new P()); }});
                researchServices.setDescriptiveNote(descriptiveNote);
            }
            builder.addLabel(labels.getString("eag2012.accessAndServices.researchServices"),    cc.xy (1, rowNb));
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(researchServices.getDescriptiveNote().getP().get(0).getContent(), researchServices.getDescriptiveNote().getP().get(0).getLang());
            researchServicesSearchroomTfs.add(textFieldWithLanguage);
            builder.add(textFieldWithLanguage.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(),                     cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addResearchservicesBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addResearchservices"));
        builder.add(addResearchservicesBtn, cc.xy (1, rowNb));
        addResearchservicesBtn.addActionListener(new AddResearchservicesBtnAction(eag, tabbedPane, model));
        setNextRow();


        builder.addSeparator(labels.getString("eag2012.accessAndServices.library"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if(repository.getServices().getLibrary() == null)
            repository.getServices().setLibrary(new Library());
        Library library = repository.getServices().getLibrary();

        if(library.getContact() == null)
            library.setContact(new Contact());

        //LibrarygetContact().getTelephone()
        builder.addLabel(labels.getString("eag2012.commons.telephone"), cc.xy(1, rowNb));
        i = 0;
        telephoneLibraryTf = new ArrayList<JTextField>(library.getContact().getTelephone().size());
        for(Telephone telephone : library.getContact().getTelephone()) {
            JTextField telephoneTf = new JTextField(telephone.getContent());
            telephoneLibraryTf.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            if(i++ == 0) {
                JButton addtelephoneLibraryTfBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
                addtelephoneLibraryTfBtn.addActionListener(new addTelephoneLibraryTfBtnAction(eag, tabbedPane, model));
                builder.add(addtelephoneLibraryTfBtn, cc.xy(5, rowNb));
            }
            setNextRow();
        }
        if(library.getContact().getTelephone().size() == 0) {
            JTextField telephoneTf = new JTextField();
            telephoneLibraryTf.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            JButton addtelephoneLibraryTfBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
            addtelephoneLibraryTfBtn.addActionListener(new addTelephoneLibraryTfBtnAction(eag, tabbedPane, model));
            builder.add(addtelephoneLibraryTfBtn, cc.xy(5, rowNb));
            setNextRow();
        }
       
      //library.getContact().getEmail()
        emailLibraryTf = new ArrayList<JTextField>(library.getContact().getEmail().size());
        emailTitleLibraryTf = new ArrayList<JTextField>(library.getContact().getEmail().size());
        if(library.getContact().getEmail().size() == 0)
        	library.getContact().getEmail().add(new Email());
        for(Email email : library.getContact().getEmail()) {
            JTextField emailTf = new JTextField(email.getHref());
            JTextField emailTitleTf = new JTextField(email.getContent());
            emailLibraryTf.add(emailTf);
            emailTitleLibraryTf.add(emailTitleTf);
            builder.addLabel(labels.getString("eag2012.commons.email"),cc.xy (1, rowNb));
            builder.add(emailTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),cc.xy (5, rowNb));
            builder.add(emailTitleTf,cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addEmailLibraryBtn = new ButtonTab(labels.getString("eag2012.commons.addEmail"));
        addEmailLibraryBtn.addActionListener(new AddEmailLibraryAction(eag, tabbedPane, model));
        builder.add(addEmailLibraryBtn, cc.xy(1, rowNb));
        setNextRow();

		//library.getWebpage()
        webpageLibraryTf = new ArrayList<JTextField>(library.getWebpage().size());
        webpageTitleLibraryTf = new ArrayList<JTextField>(library.getWebpage().size());
        if(library.getWebpage().size() == 0)
        	library.getWebpage().add(new Webpage());
        for(Webpage webpage : library.getWebpage()) {
            JTextField webpageTf = new JTextField(webpage.getHref());
            JTextField webpageTitleTf = new JTextField(webpage.getContent());
        	webpageTitleLibraryTf.add(webpageTitleTf);
    		webpageLibraryTf.add(webpageTf);
    		builder.addLabel(labels.getString("eag2012.commons.webpage"),    cc.xy (1, rowNb));
            builder.add(webpageTf,    cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),    cc.xy (5, rowNb));
            builder.add(webpageTitleTf,    cc.xy (7, rowNb));
            setNextRow();
            if(errors.contains("webpageLibraryTf")) {
                if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")), cc.xyw(1, rowNb, 3));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),cc.xyw(1, rowNb, 3));
                setNextRow();
            }
        }
        JButton addEbpageLibraryBtn = new ButtonTab(labels.getString("eag2012.commons.addWebpage"));
        addEbpageLibraryBtn.addActionListener(new AddWebpageLibraryAction(eag, tabbedPane, model));
        builder.add(addEbpageLibraryBtn, cc.xy(1, rowNb));
        setNextRow();

        if(library.getMonographicpub() == null) {
            Monographicpub monographicpub = new Monographicpub();
            Num num = new Num();
            num.setUnit("site");
            monographicpub.setNum(num);
            library.setMonographicpub(monographicpub);
        }
        if(library.getSerialpub() == null) {
            Serialpub serialpub = new Serialpub();
            Num num = new Num();
            num.setUnit("site");
            serialpub.setNum(num);
            library.setSerialpub(serialpub);
        }
        builder.addLabel(labels.getString("eag2012.accessAndServices.monographicPublication"),    cc.xy (1, rowNb));
        monographicPubLibraryTf = new JTextField(library.getMonographicpub().getNum().getContent());
        builder.add(monographicPubLibraryTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.accessAndServices.serialPublication"),    cc.xy (5, rowNb));
        serialPubLibraryTf = new JTextField(library.getSerialpub().getNum().getContent());
        builder.add(serialPubLibraryTf,    cc.xy (7, rowNb));
        setNextRow();



        builder.addSeparator(labels.getString("eag2012.accessAndServices.internetAccess"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if(repository.getServices().getInternetAccess() == null) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            InternetAccess internetAccess = new InternetAccess();
            internetAccess.setDescriptiveNote(descriptiveNote);
            repository.getServices().setInternetAccess(internetAccess);
        }
        InternetAccess internetAccess = repository.getServices().getInternetAccess();
        internetAccessDescTfs = new ArrayList<TextFieldWithLanguage>(internetAccess.getDescriptiveNote().getP().size());
        for(P p : internetAccess.getDescriptiveNote().getP()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(p.getContent(), p.getLang());
            internetAccessDescTfs.add(textFieldWithLanguage);
            builder.addLabel(labels.getString("eag2012.accessAndServices.description"),    cc.xy (1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addInternetAccessBtn = new ButtonTab(labels.getString("eag2012.isil.addInternetAccess"));
        builder.add(addInternetAccessBtn, cc.xy (1, rowNb));
        addInternetAccessBtn.addActionListener(new AddInternetAccessBtnAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator(labels.getString("eag2012.accessAndServices.technicalServices"), cc.xyw(1, rowNb, 7));
        setNextRow();
        builder.addSeparator(labels.getString("eag2012.accessAndServices.conservationLab"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if(repository.getServices().getTechservices() == null)
            repository.getServices().setTechservices(new Techservices());
        if(repository.getServices().getTechservices().getRestorationlab() == null)
            repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
        Restorationlab restorationlab = repository.getServices().getTechservices().getRestorationlab();


        if(restorationlab.getDescriptiveNote() == null)
            restorationlab.setDescriptiveNote(new DescriptiveNote());
        if(restorationlab.getDescriptiveNote().getP().size() == 0)
            restorationlab.getDescriptiveNote().getP().add(new P());

        descriptionRestorationServiceTfs = new ArrayList<TextFieldWithLanguage>(restorationlab.getDescriptiveNote().getP().size());
        for(P p : restorationlab.getDescriptiveNote().getP()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(p.getContent(), p.getLang());
            descriptionRestorationServiceTfs.add(textFieldWithLanguage);
            builder.addLabel(labels.getString("eag2012.accessAndServices.description"),    cc.xy (1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addDescriptionRestorationBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addDescriptionTranslation"), true);
        builder.add(addDescriptionRestorationBtn, cc.xy (1, rowNb));
        addDescriptionRestorationBtn.addActionListener(new AddDescriptionRestorationBtnAction(eag, tabbedPane, model));
        setNextRow();


        if(restorationlab.getContact() == null)
            restorationlab.setContact(new Contact());

       //restorationlab.getContact().getTelephone()
       builder.addLabel(labels.getString("eag2012.commons.telephone"), cc.xy(1, rowNb));
        i = 0;
        telephoneRestorationlabTf = new ArrayList<JTextField>(restorationlab.getContact().getTelephone().size());
        for(Telephone telephone : restorationlab.getContact().getTelephone()) {
            JTextField telephoneTf = new JTextField(telephone.getContent());
            telephoneRestorationlabTf.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            if(i++ == 0) {
                JButton addtelephoneRestorationlabTfBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
                addtelephoneRestorationlabTfBtn.addActionListener(new AddTelephoneRestorationlabTfBtnAction(eag, tabbedPane, model));
                builder.add(addtelephoneRestorationlabTfBtn, cc.xy(5, rowNb));
            }
            setNextRow();
        }
        if(restorationlab.getContact().getTelephone().size() == 0) {
            JTextField telephoneTf = new JTextField();
            telephoneRestorationlabTf.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            JButton addtelephoneRestorationlabTfBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
            addtelephoneRestorationlabTfBtn.addActionListener(new AddTelephoneRestorationlabTfBtnAction(eag, tabbedPane, model));
            builder.add(addtelephoneRestorationlabTfBtn, cc.xy(5, rowNb));
            setNextRow();
        }

        //Restoration.getContact().getEmail()
        emailRestorationlabTf = new ArrayList<JTextField>(restorationlab.getContact().getEmail().size());
        emailTitleRestorationlabTf = new ArrayList<JTextField>(restorationlab.getContact().getEmail().size());
        if(restorationlab.getContact().getEmail().size() == 0)
        	restorationlab.getContact().getEmail().add(new Email());
        for(Email email : restorationlab.getContact().getEmail()) {
            JTextField emailTf = new JTextField(email.getHref());
            JTextField emailTitleTf = new JTextField(email.getContent());
            emailRestorationlabTf.add(emailTf);
            emailTitleRestorationlabTf.add(emailTitleTf);
            builder.addLabel(labels.getString("eag2012.commons.email"),cc.xy (1, rowNb));
            builder.add(emailTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),cc.xy (5, rowNb));
            builder.add(emailTitleTf,cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addEmaiRestorationlabBtn = new ButtonTab(labels.getString("eag2012.commons.addEmail"));
        addEmaiRestorationlabBtn.addActionListener(new AddEmailRestorationAction(eag, tabbedPane, model));
        builder.add(addEmaiRestorationlabBtn, cc.xy(1, rowNb));
        setNextRow();

        //restorationlab.getWebpage()
        webpageRestorationlabTf = new ArrayList<JTextField>(restorationlab.getWebpage().size());
        webpageTitleRestorationlabTf = new ArrayList<JTextField>(restorationlab.getWebpage().size());
        if(restorationlab.getWebpage().size() == 0)
        	restorationlab.getWebpage().add(new Webpage());
        for(Webpage webpage : restorationlab.getWebpage()) {
            JTextField webpageTf = new JTextField(webpage.getHref());
            JTextField webpageTitleTf = new JTextField(webpage.getContent());
            webpageTitleRestorationlabTf.add(webpageTitleTf);
        	webpageRestorationlabTf.add(webpageTf);
    		builder.addLabel(labels.getString("eag2012.commons.webpage"),    cc.xy (1, rowNb));
            builder.add(webpageTf,    cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),    cc.xy (5, rowNb));
            builder.add(webpageTitleTf,    cc.xy (7, rowNb));
            setNextRow();
            if(errors.contains("webpageRestorationlabTf")) {
                if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")), cc.xyw(1, rowNb, 3));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),cc.xyw(1, rowNb, 3));
                setNextRow();
            }
        }
        JButton addWebpageRestorationlabBtn = new ButtonTab(labels.getString("eag2012.commons.addWebpage"));
        addWebpageRestorationlabBtn.addActionListener(new AddWebpageRestorationAction(eag, tabbedPane, model));
        builder.add(addWebpageRestorationlabBtn, cc.xy(1, rowNb));
        setNextRow();
        
        builder.addSeparator(labels.getString("eag2012.accessAndServices.reproductionService"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if(repository.getServices().getTechservices().getReproductionser() == null)
            repository.getServices().getTechservices().setReproductionser(new Reproductionser());
        Reproductionser reproductionser = repository.getServices().getTechservices().getReproductionser();

        if(reproductionser.getDescriptiveNote() == null)
            reproductionser.setDescriptiveNote(new DescriptiveNote());
        if(reproductionser.getDescriptiveNote().getP().size() == 0)
            reproductionser.getDescriptiveNote().getP().add(new P());

        descriptionReproductionServiceTfs = new ArrayList<TextFieldWithLanguage>(reproductionser.getDescriptiveNote().getP().size());
        for(P p : reproductionser.getDescriptiveNote().getP()) {
            TextFieldWithLanguage textFieldWithLanguage = new TextFieldWithLanguage(p.getContent(), p.getLang());
            descriptionReproductionServiceTfs.add(textFieldWithLanguage);
            builder.addLabel(labels.getString("eag2012.accessAndServices.description"),    cc.xy (1, rowNb));
            builder.add(textFieldWithLanguage.getTextField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textFieldWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
        }
        JButton addDescriptionReproductionBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addDescriptionTranslation"), true);
        builder.add(addDescriptionReproductionBtn, cc.xy (1, rowNb));
        addDescriptionReproductionBtn.addActionListener(new AddDescriptionReproductionBtnAction(eag, tabbedPane, model));
        setNextRow();

        if(reproductionser.getContact() == null)
            reproductionser.setContact(new Contact());

        builder.addLabel(labels.getString("eag2012.commons.telephone"), cc.xy(1, rowNb));
        i = 0;

        //reproductionser.getContact().getTelephone()
        telephoneReproductionServiceTf = new ArrayList<JTextField>(reproductionser.getContact().getTelephone().size());
        for(Telephone telephone : reproductionser.getContact().getTelephone()) {
            JTextField telephoneTf = new JTextField(telephone.getContent());
            telephoneReproductionServiceTf.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            if(i++ == 0) {
                JButton addtelephoneReproductionServiceTfBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
                addtelephoneReproductionServiceTfBtn.addActionListener(new AddTelephoneReproductionServiceTfBtnAction(eag, tabbedPane, model));
                builder.add(addtelephoneReproductionServiceTfBtn, cc.xy(5, rowNb));
            }
            setNextRow();
        }
        if(reproductionser.getContact().getTelephone().size() == 0) {
            JTextField telephoneTf = new JTextField();
            telephoneReproductionServiceTf.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            JButton addtelephoneRestorationlabTfBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
            addtelephoneRestorationlabTfBtn.addActionListener(new AddTelephoneReproductionServiceTfBtnAction(eag, tabbedPane, model));
            builder.add(addtelephoneRestorationlabTfBtn, cc.xy(5, rowNb));
            setNextRow();
        }
        
        //Reproductionser.getContact().getEmail()
        emailReproductionServiceTf = new ArrayList<JTextField>(reproductionser.getContact().getEmail().size());
        emailTitleReproductionServiceTf = new ArrayList<JTextField>(reproductionser.getContact().getEmail().size());
        if(reproductionser.getContact().getEmail().size() == 0)
        	reproductionser.getContact().getEmail().add(new Email());
        for(Email email : reproductionser.getContact().getEmail()) {
            JTextField emailTf = new JTextField(email.getHref());
            JTextField emailTitleTf = new JTextField(email.getContent());
            emailReproductionServiceTf.add(emailTf);
            emailTitleReproductionServiceTf.add(emailTitleTf);
            builder.addLabel(labels.getString("eag2012.commons.email"),cc.xy (1, rowNb));
            builder.add(emailTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),cc.xy (5, rowNb));
            builder.add(emailTitleTf,cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addEmaiReproductionServiceBtn = new ButtonTab(labels.getString("eag2012.commons.addEmail"));
        addEmaiReproductionServiceBtn.addActionListener(new AddEmailReproductionServiceBtnAction(eag, tabbedPane, model));
        builder.add(addEmaiReproductionServiceBtn, cc.xy(1, rowNb));
        setNextRow();

        //reproductionser.getWebpage()
        webpageReproductionServiceTf = new ArrayList<JTextField>(reproductionser.getWebpage().size());
        webpageTitleReproductionServiceTf = new ArrayList<JTextField>(reproductionser.getWebpage().size());
        if(reproductionser.getWebpage().size() == 0)
        	reproductionser.getWebpage().add(new Webpage());
        for(Webpage webpage : reproductionser.getWebpage()) {
            JTextField webpageTf = new JTextField(webpage.getHref());
            JTextField webpageTitleTf = new JTextField(webpage.getContent());
            webpageTitleReproductionServiceTf.add(webpageTitleTf);
            webpageReproductionServiceTf.add(webpageTf);
    		builder.addLabel(labels.getString("eag2012.commons.webpage"),cc.xy (1, rowNb));
            builder.add(webpageTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"), cc.xy (5, rowNb));
            builder.add(webpageTitleTf, cc.xy (7, rowNb));
            setNextRow();
            if(errors.contains("webpageReproductionServiceTf")) {
                if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")), cc.xyw(1, rowNb, 3));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),cc.xyw(1, rowNb, 3));
                setNextRow();
            }
        }
        JButton addWebpageReproductionserBtn = new ButtonTab(labels.getString("eag2012.commons.addWebpage"));
        addWebpageReproductionserBtn.addActionListener(new AddWebpageReproductionserAction(eag, tabbedPane, model));
        builder.add(addWebpageReproductionserBtn, cc.xy(1, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.accessAndServices.microformServices"), cc.xy(1, rowNb));
        if(reproductionser.getMicroformser() == null)
            reproductionser.setMicroformser(new Microformser());
        if(Arrays.asList(yesOrNo).contains(reproductionser.getMicroformser().getQuestion())) {
            microformServicesCombo.setSelectedItem(reproductionser.getMicroformser().getQuestion());
        } else {
            microformServicesCombo.setSelectedItem("---");
        }
        builder.add(microformServicesCombo, cc.xy(3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.accessAndServices.photographServices"), cc.xy(1, rowNb));
        if(reproductionser.getPhotographser() == null)
            reproductionser.setPhotographser(new Photographser());
        if(Arrays.asList(yesOrNo).contains(reproductionser.getPhotographser().getQuestion())) {
            photographServicesCombo.setSelectedItem(reproductionser.getPhotographser().getQuestion());
        } else {
            photographServicesCombo.setSelectedItem("---");
        }
        builder.add(photographServicesCombo, cc.xy(3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.accessAndServices.digitalServices"), cc.xy(1, rowNb));
        if(reproductionser.getDigitalser() == null)
            reproductionser.setDigitalser(new Digitalser());
        if(Arrays.asList(yesOrNo).contains(reproductionser.getDigitalser().getQuestion())) {
            digitalServicesCombo.setSelectedItem(reproductionser.getDigitalser().getQuestion());
        } else {
            digitalServicesCombo.setSelectedItem("---");
        }
        builder.add(digitalServicesCombo, cc.xy(3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.accessAndServices.photocopyServices"), cc.xy(1, rowNb));
        if(reproductionser.getPhotocopyser() == null)
            reproductionser.setPhotocopyser(new Photocopyser());
        if(Arrays.asList(yesOrNo).contains(reproductionser.getPhotocopyser().getQuestion())) {
            photocopyServicesCombo.setSelectedItem(reproductionser.getPhotocopyser().getQuestion());
        } else {
            photocopyServicesCombo.setSelectedItem("---");
        }
        builder.add(photocopyServicesCombo, cc.xy(3, rowNb));
        setNextRow();



        builder.addSeparator(labels.getString("eag2012.accessAndServices.recreationalServices"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if(repository.getServices().getRecreationalServices() == null)
            repository.getServices().setRecreationalServices(new RecreationalServices());
        RecreationalServices recreationalServices = repository.getServices().getRecreationalServices();

        if(recreationalServices.getRefreshment() == null) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            Refreshment refreshment = new Refreshment();
            refreshment.setDescriptiveNote(descriptiveNote);
            recreationalServices.setRefreshment(refreshment);
        }
        builder.addLabel(labels.getString("eag2012.accessAndServices.refreshment"), cc.xy(1, rowNb));
        refreshmentTf = new TextAreaWithLanguage(recreationalServices.getRefreshment().getDescriptiveNote().getP().get(0).getContent(), recreationalServices.getRefreshment().getDescriptiveNote().getP().get(0).getLang());
        builder.add(refreshmentTf.getTextField(), cc.xy(3, rowNb));
        builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
        builder.add(refreshmentTf.getLanguageBox(), cc.xy(7, rowNb));
        setNextRow();

        if(recreationalServices.getExhibition().size() == 0) {
            recreationalServices.getExhibition().add(new Exhibition());
        }
        exhibitionTfs = new ArrayList<TextAreaWithLanguage>(recreationalServices.getExhibition().size());
        for(Exhibition exhibition : recreationalServices.getExhibition()) {
            if(exhibition.getDescriptiveNote() == null) {
                DescriptiveNote descriptiveNote = new DescriptiveNote();
                descriptiveNote.getP().add(new P());
                exhibition.setDescriptiveNote(descriptiveNote);
                exhibition.setWebpage(new Webpage());
            }

            builder.addLabel(labels.getString("eag2012.accessAndServices.exhibition"),    cc.xy (1, rowNb));
            if(exhibition.getWebpage() == null){
                exhibition.setWebpage(new Webpage());
            }
            TextAreaWithLanguage exhibitionTf = new TextAreaWithLanguage(exhibition.getDescriptiveNote().getP().get(0).getContent(), exhibition.getDescriptiveNote().getP().get(0).getLang(), exhibition.getWebpage().getHref(), exhibition.getWebpage().getContent());
            exhibitionTfs.add(exhibitionTf);
            builder.add(exhibitionTf.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(exhibitionTf.getLanguageBox(),                     cc.xy (7, rowNb));
            setNextRow();

            builder.addLabel(labels.getString("eag2012.commons.webpage"), cc.xy(1, rowNb));
            builder.add(exhibitionTf.getExtraField(),                                            cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"), cc.xy(5, rowNb));
            builder.add(exhibitionTf.getSecondExtraField(),                                            cc.xy (7, rowNb));
            setNextRow();
            if(errors.contains("exhibitionTfs")) {
                if(StringUtils.isNotBlank(exhibitionTf.getExtraField().getText()) && !StringUtils.startsWithAny(exhibitionTf.getExtraField().getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(exhibitionTf.getExtraField().getText()) && !StringUtils.startsWithAny(exhibitionTf.getExtraField().getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }
        }
        JButton addExhibitionsBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addExhibitions"));
        builder.add(addExhibitionsBtn, cc.xy (1, rowNb));
        addExhibitionsBtn.addActionListener(new AddExhibitionsBtnAction(eag, tabbedPane, model));
        setNextRow();

        if(recreationalServices.getToursSessions().size() == 0) {
            recreationalServices.getToursSessions().add(new ToursSessions());
        }
        toursAndSessionsTfs = new ArrayList<TextAreaWithLanguage>(recreationalServices.getToursSessions().size());
        for(ToursSessions toursSessions : recreationalServices.getToursSessions()) {
            if(toursSessions.getDescriptiveNote() == null) {
                DescriptiveNote descriptiveNote = new DescriptiveNote();
                descriptiveNote.getP().add(new P());
                toursSessions.setDescriptiveNote(descriptiveNote);
                toursSessions.setWebpage(new Webpage());
            }
            builder.addLabel(labels.getString("eag2012.accessAndServices.toursAndSessions"),    cc.xy (1, rowNb));
            if(toursSessions.getWebpage() == null){
                toursSessions.setWebpage(new Webpage());
            }
            TextAreaWithLanguage textAreaWithLanguage = new TextAreaWithLanguage(toursSessions.getDescriptiveNote().getP().get(0).getContent(), toursSessions.getDescriptiveNote().getP().get(0).getLang(), toursSessions.getWebpage().getHref(), toursSessions.getWebpage().getContent());
            toursAndSessionsTfs.add(textAreaWithLanguage);
            builder.add(textAreaWithLanguage.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getLanguageBox(), cc.xy(7, rowNb));
            setNextRow();
            builder.addLabel(labels.getString("eag2012.commons.webpage"), cc.xy(1, rowNb));
            builder.add(textAreaWithLanguage.getExtraField(), cc.xy(3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"), cc.xy(5, rowNb));
            builder.add(textAreaWithLanguage.getSecondExtraField(),                                            cc.xy (7, rowNb));
            setNextRow();
            if(errors.contains("toursAndSessionsTfs")) {
                if(StringUtils.isNotBlank(textAreaWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textAreaWithLanguage.getExtraField().getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(textAreaWithLanguage.getExtraField().getText()) && !StringUtils.startsWithAny(textAreaWithLanguage.getExtraField().getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }
        }
        JButton addToursSessionsBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addToursSessions"));
        builder.add(addToursSessionsBtn, cc.xy (1, rowNb));
        addToursSessionsBtn.addActionListener(new AddToursSessionsBtnAction(eag, tabbedPane, model));
        setNextRow();

        if(recreationalServices.getOtherServices().size() == 0) {
            recreationalServices.getOtherServices().add(new OtherServices());
        }
        otherServicesTfs = new ArrayList<TextAreaWithLanguage>(recreationalServices.getOtherServices().size());
        for(OtherServices otherServices : recreationalServices.getOtherServices()) {
            if(otherServices.getDescriptiveNote() == null) {
                DescriptiveNote descriptiveNote = new DescriptiveNote();
                otherServices.setDescriptiveNote(descriptiveNote);
            }
            if(otherServices.getDescriptiveNote().getP().size() == 0) {
                otherServices.getDescriptiveNote().getP().add(new P());
            }
            if(otherServices.getWebpage() == null) {
                otherServices.setWebpage(new Webpage());
            }
            builder.addLabel(labels.getString("eag2012.accessAndServices.otherServices"),    cc.xy (1, rowNb));
            TextAreaWithLanguage otherServicesTf = new TextAreaWithLanguage(otherServices.getDescriptiveNote().getP().get(0).getContent(), otherServices.getDescriptiveNote().getP().get(0).getLang(), otherServices.getWebpage().getHref(), otherServices.getWebpage().getContent());
            otherServicesTfs.add(otherServicesTf);
            builder.add(otherServicesTf.getTextField(),                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy(5, rowNb));
            builder.add(otherServicesTf.getLanguageBox(),                     cc.xy (7, rowNb));
            setNextRow();
            builder.addLabel(labels.getString("eag2012.commons.webpage"), cc.xy(1, rowNb));
            builder.add(otherServicesTf.getExtraField(),                                            cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"), cc.xy(5, rowNb));
            builder.add(otherServicesTf.getSecondExtraField(),                                            cc.xy (7, rowNb));
            setNextRow();
            if(errors.contains("otherServicesTfs")) {
                if(StringUtils.isNotBlank(otherServicesTf.getExtraField().getText()) && !StringUtils.startsWithAny(otherServicesTf.getExtraField().getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(otherServicesTf.getExtraField().getText()) && !StringUtils.startsWithAny(otherServicesTf.getExtraField().getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),          cc.xy(1, rowNb));
                setNextRow();
            }
        }
        JButton addOtherServicesBtn = new ButtonTab(labels.getString("eag2012.accessAndServices.addOtherServices"));
        builder.add(addOtherServicesBtn, cc.xy (1, rowNb));
        addOtherServicesBtn.addActionListener(new AddOtherServicesBtnAction(eag, tabbedPane, model));
        setNextRow();

        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();

        JButton exitBtn = new ButtonTab(labels.getString("eag2012.commons.exit"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton previousTabBtn = new ButtonTab(labels.getString("eag2012.commons.previousTab"));
        builder.add(previousTabBtn, cc.xy (3, rowNb));
        previousTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, false));

        JButton nextTabBtn = new ButtonTab(labels.getString("eag2012.commons.nextTab"));
        builder.add(nextTabBtn, cc.xy (5, rowNb));
        nextTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, true));

        setNextRow();
        JButton saveBtn = new ButtonTab(labels.getString("eag2012.commons.save"));
        builder.add(saveBtn, cc.xy (5, rowNb));
        saveBtn.addActionListener(new SaveBtnAction(eag, tabbedPane, model));

        setNextRow();
        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();
        JButton previousInstitutionTabBtn = new ButtonTab(labels.getString("eag2012.controls.previousInstitution"));
        previousInstitutionTabBtn.addActionListener(new PreviousInstitutionTabBtnAction(eag, tabbedPane, model));
        builder.add(previousInstitutionTabBtn, cc.xy(1, rowNb));
        JButton nextInstitutionTabBtn = new ButtonTab(labels.getString("eag2012.controls.nextInstitution"));
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
                super.updateJAXBObject(true);

                int currentTabIndex = mainTabbedPane.getSelectedIndex();
                if(currentTabIndex+1 < mainTabbedPane.getTabCount()) {
                    reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
                    mainTabbedPane.setEnabledAt(currentTabIndex, false);
                    mainTabbedPane.setEnabledAt(currentTabIndex+1, true);
                    mainTabbedPane.setSelectedIndex(currentTabIndex+1);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
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
                super.updateJAXBObject(true);

                int currentTabIndex = mainTabbedPane.getSelectedIndex();
                if(currentTabIndex > 0) {
                    reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
                    mainTabbedPane.setEnabledAt(currentTabIndex, false);
                    mainTabbedPane.setEnabledAt(currentTabIndex-1, true);
                    mainTabbedPane.setSelectedIndex(currentTabIndex-1);
//                    reloadTabbedPanel(new EagInstitutionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, false, labels, repositoryNb-1).buildEditorPanel(errors), 0);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
            }
        }
    }

   // add opening hours action
    public class AddOpeningHoursBtnAction extends UpdateEagObject {
        AddOpeningHoursBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getTimetable().getOpening().add(new Opening());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    /***
     * Class for the button to add further terms of use
     * @author fernando
     *
     */
    public class addTermsOfUseBtnAction extends UpdateEagObject {
    	addTermsOfUseBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);
            boolean empty = false;         
            int pos = termsOfUseTfs.size();
            
            for(int i=0; i<pos;i++){
                if( (termsOfUseTfs.get(i).getTextValue()==null || termsOfUseTfs.get(i).getTextValue().trim().compareTo("") == 0)
                		&& (termsOfUseTfs.get(i).getExtraValue()==null || termsOfUseTfs.get(i).getExtraValue().trim().compareTo("") == 0))
                	empty = true;
            }

        	if (!errors.contains("termsOfUseTfs")){
        		if (empty)
            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTermsOfUse"));
            	
            }
            else{
            	if (empty)
            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTermsOfUse"));
            }
             repository.getAccess().getTermsOfUse().add(new TermsOfUse());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
     
    /***
     * Class for the button to add facilities for disabled persons
     * @author fernando
     *
     */
    public class addFacilitiesForDisabledBtnAction extends UpdateEagObject {
    	addFacilitiesForDisabledBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            
            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);
            boolean empty = false;         
            int pos = accessibilityTfs.size();
            
            for(int i=0; i<pos;i++){
                if( accessibilityTfs.get(i).getTextValue()==null || accessibilityTfs.get(i).getTextValue().trim().compareTo("") == 0)
                	empty = true;
            }

            if (empty)
            	JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorFacilitiesForDisabled"));

        	repository.getAccessibility().add(new Accessibility());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddClosingDatesBtnAction extends UpdateEagObject {
        AddClosingDatesBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getTimetable().getClosing().add(new Closing());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddTravellingDirectionsBtnAction extends UpdateEagObject {
        AddTravellingDirectionsBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getDirections().add(new Directions());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddRestaccessBtnAction extends UpdateEagObject {
        AddRestaccessBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getAccess().getRestaccess().add(new Restaccess());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddAccessibilityBtnAction extends UpdateEagObject {
        AddAccessibilityBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
//            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getAccessibility().isEmpty()){
//                LOG.info("create new accessibility");
//                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getAccessibility().add(new Accessibility());
//            }
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddComputerplacesDescriptionBtnAction extends UpdateEagObject {
        AddComputerplacesDescriptionBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }

            if(StringUtils.isNotEmpty(computerplacesSearchroomTf.getText())){
                if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom().getComputerPlaces().getDescriptiveNote() == null){
                    DescriptiveNote descriptiveNote = new DescriptiveNote();
                    descriptiveNote.setP(new ArrayList<P>());
                    descriptiveNote.getP().add(new P());
                    eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom().getComputerPlaces().setDescriptiveNote(descriptiveNote);
                } else {
                    eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom().getComputerPlaces().getDescriptiveNote().getP().add(new P());
                }
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.computerplacesNotBlank"));
            }
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddReadersticketBtnAction extends UpdateEagObject {
        AddReadersticketBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom().getReadersTicket().add(new ReadersTicket());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddAdvancedordersBtnAction extends UpdateEagObject {
        AddAdvancedordersBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom().getAdvancedOrders().add(new AdvancedOrders());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddResearchservicesBtnAction extends UpdateEagObject {
        AddResearchservicesBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom().getResearchServices().add(new ResearchServices());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddExhibitionsBtnAction extends UpdateEagObject {
        AddExhibitionsBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getRecreationalServices().getExhibition().add(new Exhibition());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.fillServiceBeforeAddingAnother"));
            }
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddToursSessionsBtnAction extends UpdateEagObject {
        AddToursSessionsBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getRecreationalServices().getToursSessions().add(new ToursSessions());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.fillServiceBeforeAddingAnother"));
            }
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddOtherServicesBtnAction extends UpdateEagObject {
        AddOtherServicesBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getRecreationalServices().getOtherServices().add(new OtherServices());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.fillServiceBeforeAddingAnother"));
            }
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddInternetAccessBtnAction extends UpdateEagObject {
        AddInternetAccessBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getInternetAccess() == null) {
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().setInternetAccess(new InternetAccess());
            }
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getInternetAccess().getDescriptiveNote() == null) {
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getInternetAccess().setDescriptiveNote(new DescriptiveNote());
            }
            eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getInternetAccess().getDescriptiveNote().getP().add(new P());
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }

    public class AddDescriptionReproductionBtnAction extends UpdateEagObject {
        AddDescriptionReproductionBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
                Reproductionser reproductionser = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getReproductionser();
                if(reproductionser.getDescriptiveNote() == null) {
                    reproductionser.setDescriptiveNote(new DescriptiveNote());
                }
                reproductionser.getDescriptiveNote().getP().add(new P());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.fillDescriptionBeforeAddingAnother"));
            }
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }

    /***
     * Adds a new tel. in SearchRoom 
     * @author fernando
     *
     */
    public class AddTelephoneSearchroomBtnAction extends UpdateEagObject {
    	AddTelephoneSearchroomBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Exception e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.toString());
            }
                        
			if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
				Searchroom searchroom = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom();

    			if(searchroom == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().setSearchroom(new Searchroom());
					searchroom = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom();
				}
    			
    			if(searchroom.getContact()==null)
    				searchroom.setContact(new Contact());	
    			
				boolean empty = false;         
			    int pos = telephoneSearchroomTf.size(); 
			    for(int i=0; i<pos;i++){
			        if( telephoneSearchroomTf.get(i).getText()==null || telephoneSearchroomTf.get(i).getText().trim().compareTo("") == 0)
			        	empty = true;
			    }
				if (empty)
					JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTelephone"));
				
				searchroom.getContact().getTelephone().add(new Telephone());
			} else {
			    JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTelephone"));
			}

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }

    /***
     * Adds a new tel. in Library
     * @author fernando
     *
     */
    public class addTelephoneLibraryTfBtnAction extends UpdateEagObject {
    	addTelephoneLibraryTfBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }
		            
			if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
	            Library library = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getLibrary();
    			
    			if(library == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().setLibrary(new Library());
					library = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getLibrary();
				}
    			
    			if(library.getContact()==null)
    				library.setContact(new Contact());	
    			
    			boolean empty = false;         
	            int pos = telephoneLibraryTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( telephoneLibraryTf.get(i).getText()==null || telephoneLibraryTf.get(i).getText().trim().compareTo("") == 0)
	                	empty = true;
	            }
				if (empty)
					JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTelephone"));
					
				library.getContact().getTelephone().add(new Telephone());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTelephone"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }

    /***
     * Adds a new tel. in RestorationLab
     * @author fernando
     *
     */
    public class AddTelephoneRestorationlabTfBtnAction extends UpdateEagObject {
    	AddTelephoneRestorationlabTfBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
            	
				Services services = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices();
				
				if(services.getTechservices() == null) 
					services.setTechservices(new Techservices());
				
				Restorationlab restorationlab = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getRestorationlab();
				
				if(restorationlab == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().setRestorationlab(new Restorationlab());
					restorationlab = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getRestorationlab();
				}

    			if(restorationlab.getContact()==null)
    				restorationlab.setContact(new Contact());	
				
    			boolean empty = false;         
	            int pos = telephoneRestorationlabTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( telephoneRestorationlabTf.get(i).getText()==null || telephoneRestorationlabTf.get(i).getText().trim().compareTo("") == 0)
	                	empty = true;
	            }
				if (empty)
					JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTelephone"));
					
				restorationlab.getContact().getTelephone().add(new Telephone());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTelephone"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }

    /***
     * Adds a new tel. in ReproductionServices
     * @author fernando
     *
     */
    public class AddTelephoneReproductionServiceTfBtnAction extends UpdateEagObject {
    	AddTelephoneReproductionServiceTfBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }
			
			if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
				
				Services services = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices();
				
				if(services.getTechservices() == null) 
					services.setTechservices(new Techservices());
				
				Reproductionser reproductionser = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getReproductionser();

				if(reproductionser == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().setReproductionser(new Reproductionser());
					reproductionser = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getReproductionser();
				}
				
    			if(reproductionser.getContact()==null){
    				reproductionser.setContact(new Contact());	
    			}
				
    			boolean empty = false;         
	            int pos = telephoneReproductionServiceTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( telephoneReproductionServiceTf.get(i).getText()==null || telephoneReproductionServiceTf.get(i).getText().trim().compareTo("") == 0)
	                	empty = true;
	            }
				if (empty)
					JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTelephone"));
				
				reproductionser.getContact().getTelephone().add(new Telephone());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTelephone"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
       
    /***
     * Adds a new email in SearchRoom
     * @author fernando
     *
     */
    public class AddEmailSearchroomAction extends UpdateEagObject {
    	AddEmailSearchroomAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
                Searchroom searchroom = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom();
    			
                if(searchroom == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().setSearchroom(new Searchroom());
					searchroom = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom();
				}
                
    			if(searchroom.getContact()==null)
    				searchroom.setContact(new Contact());	
    			
    			boolean empty = false;         
	            int pos = emailSearchroomTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( (emailSearchroomTf.get(i).getText()==null || emailSearchroomTf.get(i).getText().trim().compareTo("") == 0)
	                		&& (emailTitleSearchroomTf.get(i).getText().trim().compareTo("") == 0))
	                	empty = true;
	            }
				if (empty)
					JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorEmail"));
					
				searchroom.getContact().getEmail().add(new Email());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorEmail"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    /***
     * Adds a new email in Library
     * @author fernando
     *
     */
    public class AddEmailLibraryAction extends UpdateEagObject {
    	AddEmailLibraryAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
    			Library library = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getLibrary();

    			if(library == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().setLibrary(new Library());
					library = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getLibrary();
				}
    			
    			if(library.getContact()==null)
    				library.setContact(new Contact());	
    			
    			boolean empty = false;         
	            int pos = emailLibraryTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( (emailLibraryTf.get(i).getText()==null || emailLibraryTf.get(i).getText().trim().compareTo("") == 0)
	                	&& (emailTitleLibraryTf.get(i).getText().trim().compareTo("") == 0))
	                	empty = true;
	            }
				if (empty)
					JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorEmail"));
					
				library.getContact().getEmail().add(new Email());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorEmail"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    /***
     * Adds a new email in RestorationServices
     * @author fernando
     *
     */
    public class AddEmailRestorationAction extends UpdateEagObject {
    	AddEmailRestorationAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
    			
				Services services = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices();
				
				if(services.getTechservices() == null) 
					services.setTechservices(new Techservices());
				
            	Restorationlab restorationlab = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getRestorationlab();

				if(restorationlab == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().setRestorationlab(new Restorationlab());
					restorationlab = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getRestorationlab();
				}
				
    			if(restorationlab.getContact()==null)
    				restorationlab.setContact(new Contact());	
    			
    			boolean empty = false;         
	            int pos = emailRestorationlabTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( (emailRestorationlabTf.get(i).getText()==null || emailRestorationlabTf.get(i).getText().trim().compareTo("") == 0) 
	                		&& (emailTitleRestorationlabTf.get(i).getText().trim().compareTo("") == 0))
	                	empty = true;
	            }
				if (empty)
					JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorEmail"));
				
				restorationlab.getContact().getEmail().add(new Email());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorEmail"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    /***
     * Adds a new email in ReproductionService
     * @author fernando
     *
     */
    public class AddEmailReproductionServiceBtnAction extends UpdateEagObject {
    	AddEmailReproductionServiceBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }
    
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
    			
				Services services = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices();
				
				if(services.getTechservices() == null) 
					services.setTechservices(new Techservices());
				
            	Reproductionser reproductionser = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getReproductionser();

				if(reproductionser == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().setReproductionser(new Reproductionser());
					reproductionser = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getReproductionser();
				}
				
    			if(reproductionser.getContact()==null)
    				reproductionser.setContact(new Contact());	
				
    			boolean empty = false;         
	            int pos = emailReproductionServiceTf.size(); 
	            for(int i=0; i<pos;i++){
	                if(( emailReproductionServiceTf.get(i).getText()==null || emailReproductionServiceTf.get(i).getText().trim().compareTo("") == 0) 
	                		&& (emailTitleReproductionServiceTf.get(i).getText().trim().compareTo("") == 0))
	                	empty = true;
	            }
				if (empty)
					JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorEmail"));
				
				reproductionser.getContact().getEmail().add(new Email());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorEmail"));
            }
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
       }
    }
    
    /***
     * Adds a new web page in Search room
     * @author fernando
     *
     */
    public class AddWebpageSearchroomAction extends UpdateEagObject {
    	AddWebpageSearchroomAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
                Searchroom searchroom = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom();
    			
    			if(searchroom == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().setSearchroom(new Searchroom());
					searchroom = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getSearchroom();
				}
    			
    			boolean empty = false;         
	            int pos = webpageSearchroomTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( (webpageSearchroomTf.get(i).getText()==null || webpageSearchroomTf.get(i).getText().trim().compareTo("") == 0)
	                		&& (webpageTitleSearchroomTf.get(i).getText()==null || webpageTitleSearchroomTf.get(i).getText().trim().compareTo("") == 0))
	                	empty = true;
	            }
	            if(!errors.contains("webpageSearchroomTf")){
	            	if (empty)
	            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
	            	
	            }
	            else{
	            	if (empty)
	            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
	            }
	            searchroom.getWebpage().add(new Webpage());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
     
	/***
	 * Adds a new web page in Library
	 * @author fernando
	 *
	 */
    public class AddWebpageLibraryAction extends UpdateEagObject {
    	AddWebpageLibraryAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
                Library library = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getLibrary();
    			
				if(library == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().setLibrary(new Library());
					library = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getLibrary();
				}

    			boolean empty = false;         
	            int pos = webpageLibraryTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( (webpageLibraryTf.get(i).getText()==null || webpageLibraryTf.get(i).getText().trim().compareTo("") == 0)
	                		&& (webpageTitleLibraryTf.get(i).getText()==null || webpageTitleLibraryTf.get(i).getText().trim().compareTo("") == 0))
	                	empty = true;
	            }
	            if (!errors.contains("webpageLibraryTf")){
	            	if (empty)
	            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
	            	
	            }
	            else{
	            	if (empty)
	            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
	            }
	            library.getWebpage().add(new Webpage());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }

    /***
     * Adds a new web page in Conservation Lab.
     * @author fernando
     *
     */
    public class AddWebpageRestorationAction extends UpdateEagObject {
    	AddWebpageRestorationAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null
            		&& eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices() != null){
                Restorationlab restorationlab = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getRestorationlab();

				if(restorationlab == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().setRestorationlab(new Restorationlab());
					restorationlab = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getRestorationlab();
				}
				
    			boolean empty = false;         
	            int pos = webpageRestorationlabTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( (webpageRestorationlabTf.get(i).getText()==null || webpageRestorationlabTf.get(i).getText().trim().compareTo("") == 0)
	                		&& (webpageTitleRestorationlabTf.get(i).getText()==null || webpageTitleRestorationlabTf.get(i).getText().trim().compareTo("") == 0))
	                	empty = true;
	            }
	            if(!errors.contains("webpageRestorationlabTf")){
	            	if (empty)
	            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
	            	
	            }
	            else{
	            	if (empty)
	            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
	            }
	            restorationlab.getWebpage().add(new Webpage());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    /***
     * Adds a new web page in Reproduction services.
     * @author fernando
     *
     */
    public class AddWebpageReproductionserAction extends UpdateEagObject {
    	AddWebpageReproductionserAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }

            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null
            		&& eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices() != null){
                Reproductionser reproductionser = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getReproductionser();

				if(reproductionser == null) {
					eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().setReproductionser(new Reproductionser());
					reproductionser = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getReproductionser();
				}
				
    			boolean empty = false;         
	            int pos = webpageReproductionServiceTf.size(); 
	            for(int i=0; i<pos;i++){
	                if( (webpageReproductionServiceTf.get(i).getText()==null || webpageReproductionServiceTf.get(i).getText().trim().compareTo("") == 0)
	                		&& (webpageTitleReproductionServiceTf.get(i).getText()==null || webpageTitleReproductionServiceTf.get(i).getText().trim().compareTo("") == 0))
	                	empty = true;
	            }
	            if(!errors.contains("webpageReproductionServiceTf")){
	            	if (empty)
	            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
	            	
	            }
	            else{
	            	if (empty)
	            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
	            }
	            reproductionser.getWebpage().add(new Webpage());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
            }

            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }
    
    public class AddDescriptionRestorationBtnAction extends UpdateEagObject {
        AddDescriptionRestorationBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            
            if(eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices() != null){
                Restorationlab restorationlab = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getServices().getTechservices().getRestorationlab();
                if(restorationlab.getDescriptiveNote() == null) {
                    restorationlab.setDescriptiveNote(new DescriptiveNote());
                }
                restorationlab.getDescriptiveNote().getP().add(new P());
            } else {
                JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.fillDescriptionBeforeAddingAnother"));
            }
            reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
        }
    }

    public class SaveBtnAction extends UpdateEagObject {
        SaveBtnAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(true);
                super.saveFile(eag.getControl().getRecordId().getValue());
                closeFrame();
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
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
                super.updateJAXBObject(false);

                if(isNextTab) {
                    reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 4);
                    tabbedPane.setEnabledAt(4, true);
                    tabbedPane.setEnabledAt(3, false);
                } else {
                    reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, false, labels, repositoryNb).buildEditorPanel(errors), 2);
                    tabbedPane.setEnabledAt(2, true);
                    tabbedPane.setEnabledAt(3, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, labels, repositoryNb).buildEditorPanel(errors), 3);
            }
        }
    }
    
    public abstract class UpdateEagObject extends DefaultBtnAction {

        public UpdateEagObject(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        protected void updateJAXBObject(boolean save) throws Eag2012FormException {
            errors = new ArrayList<String>();

            Repository repository = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb);

            //updating opening hours
            boolean openingTimeExists = false;
            if(openingHoursTfs.size() > 0) {
                repository.getTimetable().getOpening().clear();
                for(TextAreaWithLanguage textAreaWithLanguage : openingHoursTfs) {
                    if(StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        Opening opening = new Opening();
                        opening.setContent(textAreaWithLanguage.getTextValue());
                        opening.setLang(textAreaWithLanguage.getLanguage());
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
                for(TextAreaWithLanguage textAreaWithLanguage : closingDatesTfs) {
                    if(StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        Closing closing = new Closing();
                        closing.setContent(textAreaWithLanguage.getTextValue());
                        closing.setLang(textAreaWithLanguage.getLanguage());
                        repository.getTimetable().getClosing().add(closing);
                    }
                }
            }

            if(travellingDirectionsTfs.size() > 0) {
                repository.getDirections().clear();
                for(TextAreaWithLanguage textAreaWithLanguage : travellingDirectionsTfs) {
                    if(StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        Directions directions = new Directions();
                        directions.setLang(textAreaWithLanguage.getLanguage());
                        directions.getContent().add(textAreaWithLanguage.getTextValue());
                        if(StringUtils.isNotEmpty(textAreaWithLanguage.getExtraValue())) {
                            Citation citation = new Citation();
                            citation.setHref(textAreaWithLanguage.getExtraValue());
                            if(!StringUtils.startsWithAny(textAreaWithLanguage.getExtraValue(), webPrefixes))
                                errors.add("travellingDirectionsTfs");
                            directions.getContent().add(citation);
                        }
                        repository.getDirections().add(directions);
                    }
                }
            }
            repository.getAccess().setQuestion((String)accessiblePublicCombo.getSelectedItem());

            if(restaccessTfs.size() > 0) {
                repository.getAccess().getRestaccess().clear();
                for(TextAreaWithLanguage textAreaWithLanguage : restaccessTfs) {
                    if(StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        Restaccess restaccess = new Restaccess();
                        restaccess.setContent(textAreaWithLanguage.getTextValue());
                        restaccess.setLang(textAreaWithLanguage.getLanguage());
                        repository.getAccess().getRestaccess().add(restaccess);
                    }
                }
            }

            // updating Terms of Use
            if(termsOfUseTfs.size() > 0) {
                repository.getAccess().getTermsOfUse().clear();
                for(TextAreaWithLanguage textAreaWithLanguage : termsOfUseTfs) {
                    if(StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue().trim()) || StringUtils.isNotEmpty(textAreaWithLanguage.getExtraValue().trim())) {
                        TermsOfUse termsOfUse = new TermsOfUse();
                        termsOfUse.setLang(textAreaWithLanguage.getLanguage());
                        termsOfUse.setContent(textAreaWithLanguage.getTextValue());
                        if(StringUtils.isNotEmpty(textAreaWithLanguage.getExtraValue())) {
                            termsOfUse.setHref(textAreaWithLanguage.getExtraValue());
                            if(!StringUtils.startsWithAny(textAreaWithLanguage.getExtraValue(), webPrefixes))
                                errors.add("termsOfUseTfs");
                        }
                        repository.getAccess().getTermsOfUse().add(termsOfUse);
                    }
                }
            }

          //updating accessibility
            if(accessibilityTfs.size() > 0) {
                repository.getAccessibility().clear();
                for(TextAreaWithLanguage textAreaWithLanguage : accessibilityTfs) {
                    if(StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue().trim())) {
                        Accessibility accessibility = new Accessibility();
                        accessibility.setLang(textAreaWithLanguage.getLanguage());
                        accessibility.setContent(textAreaWithLanguage.getTextValue());
                        accessibility.setQuestion((String) facilitiesForDisabledCombo.getSelectedItem());
                        repository.getAccessibility().add(accessibility);
                    }
                }
            }
            if (repository.getAccessibility().isEmpty()){
            	Accessibility accessibility = new Accessibility();
            	accessibility.setQuestion((String) facilitiesForDisabledCombo.getSelectedItem());
            	repository.getAccessibility().add(accessibility);
            }
            
            if(repository.getServices() == null)
                repository.setServices(new Services());
     
            // ***************************************************************************************
            // 		Search room
            // ***************************************************************************************

            if(repository.getServices().getSearchroom() == null)
                repository.getServices().setSearchroom(new Searchroom());

            Searchroom searchroom = repository.getServices().getSearchroom();
            boolean hasContactInfo = false;
            boolean hasSearchRoomInfo = false;
            boolean hasSearchroomWebpage = false;

            if (searchroom.getContact() == null) {
            	searchroom.setContact(new Contact());
            }

            //Telephone in Search room
            searchroom.getContact().getTelephone().clear();
            for(JTextField field : telephoneSearchroomTf) {
                if(StringUtils.isNotEmpty(field.getText().trim())) {
                    Telephone telephone = new Telephone();
                    telephone.setContent(field.getText());
                    searchroom.getContact().getTelephone().add(telephone);
                    hasContactInfo = true;
                }
            }

            //email in Search room
            searchroom.getContact().getEmail().clear();
            for(int i = 0; i < emailSearchroomTf.size(); i++) {
                JTextField field = emailSearchroomTf.get(i);
                JTextField fieldTitle = emailTitleSearchroomTf.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim())|| StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Email email = new Email();
                    email.setHref(field.getText());
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                        email.setContent(fieldTitle.getText());
//                    else
//                        email.setContent(field.getText());
                    searchroom.getContact().getEmail().add(email);
                    hasContactInfo = true;
                }
            }
            
            if(!hasContactInfo) {
                searchroom.setContact(null);
            }
            hasSearchRoomInfo = hasContactInfo;
            
            //web page in Search room
            searchroom.getWebpage().clear();
            for(int i = 0; i < webpageSearchroomTf.size(); i++) {
                JTextField field = webpageSearchroomTf.get(i);
                JTextField fieldTitle = webpageTitleSearchroomTf.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim()) || StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Webpage webpage = new Webpage();
                    webpage.setHref(field.getText());
                    if(StringUtils.isNotEmpty(field.getText().trim())){
	                    if(!StringUtils.startsWithAny(field.getText(), webPrefixes))
	                        errors.add("webpageSearchroomTf");
                    }
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                    	webpage.setContent(fieldTitle.getText());
//                    else
//                    	webpage.setContent(field.getText());
                    searchroom.getWebpage().add(webpage);
                    hasSearchroomWebpage = true;
                }
            }

            if(!hasSearchRoomInfo)
            	hasSearchRoomInfo = hasSearchroomWebpage;

            if(StringUtils.isNotEmpty(computerplacesSearchroomTf.getText())) {
                Num num = new Num();
                num.setUnit("site");
                num.setContent(computerplacesSearchroomTf.getText());
                searchroom.getComputerPlaces().setNum(num);
                hasSearchRoomInfo = true;
            } else {
                searchroom.setComputerPlaces(null);
            }

            if (computerplacesDescriptionTfs != null && computerplacesDescriptionTfs.size() > 0) {
                searchroom.getComputerPlaces().getDescriptiveNote().getP().clear();
                for (TextFieldWithLanguage textFieldWithLanguage : computerplacesDescriptionTfs) {
                    if (StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        P p = new P();
                        p.setContent(textFieldWithLanguage.getTextValue());
                        p.setLang(textFieldWithLanguage.getLanguage());
                        searchroom.getComputerPlaces().getDescriptiveNote().getP().add(p);
                        hasSearchRoomInfo = true;
                    }
                }
            }

            if(StringUtils.isNotEmpty(microfilmplacesSearchroomTf.getText())) {
                Num num = new Num();
                num.setUnit("site");
                num.setContent(microfilmplacesSearchroomTf.getText());
                searchroom.getMicrofilmPlaces().setNum(num);
                hasSearchRoomInfo = true;
            } else {
                searchroom.setMicrofilmPlaces(null);
            }

            if(!(photographAllowanceCombo.getSelectedItem()).equals("---")) {
                searchroom.getPhotographAllowance().setValue((String)photographAllowanceCombo.getSelectedItem());
                hasSearchRoomInfo = true;
            } else {
                searchroom.setPhotographAllowance(null);
            }

            if(readersticketSearchroomTfs.size() > 0) {
                searchroom.getReadersTicket().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : readersticketSearchroomTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        ReadersTicket readersTicket = new ReadersTicket();
                        readersTicket.setLang(textFieldWithLanguage.getLanguage());
                        readersTicket.setContent(textFieldWithLanguage.getTextValue());
                        readersTicket.setHref(textFieldWithLanguage.getExtraValue());
                        if(!StringUtils.startsWithAny(textFieldWithLanguage.getExtraValue(), webPrefixes))
                                errors.add("readersticketSearchroomTfs");
                        searchroom.getReadersTicket().add(readersTicket);
                        hasSearchRoomInfo = true;
                    }
                }
            }

            if(advancedordersSearchroomTfs.size() > 0) {
                searchroom.getAdvancedOrders().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : advancedordersSearchroomTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        AdvancedOrders advancedOrders = new AdvancedOrders();
                        advancedOrders.setLang(textFieldWithLanguage.getLanguage());
                        advancedOrders.setContent(textFieldWithLanguage.getTextValue());
                        advancedOrders.setHref(textFieldWithLanguage.getExtraValue());
                        if(!StringUtils.startsWithAny(textFieldWithLanguage.getExtraValue(), webPrefixes))
                                errors.add("advancedordersSearchroomTfs");
                        searchroom.getAdvancedOrders().add(advancedOrders);
                        hasSearchRoomInfo = true;
                    }
                }
            }

            if(researchServicesSearchroomTfs.size() > 0) {
                searchroom.getResearchServices().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : researchServicesSearchroomTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        ResearchServices researchServices = new ResearchServices();
                        DescriptiveNote descriptiveNote = new DescriptiveNote();
                        descriptiveNote.setP(new ArrayList<P>() {{
                            add(new P());
                        }});
                        descriptiveNote.getP().get(0).setContent(textFieldWithLanguage.getTextValue());
                        descriptiveNote.getP().get(0).setLang(textFieldWithLanguage.getLanguage());
                        researchServices.setDescriptiveNote(descriptiveNote);
                        searchroom.getResearchServices().add(researchServices);
                        hasSearchRoomInfo = true;
                    }
                }
            }

            if(StringUtils.isNotEmpty(workplacesSearchroomTf.getText())) {
                Num num = new Num();
                num.setUnit("site");
                num.setContent(workplacesSearchroomTf.getText());
                searchroom.getWorkPlaces().setNum(num);
                hasSearchRoomInfo = true;
            } else if(hasSearchRoomInfo) {
                repository.getServices().getSearchroom().setWorkPlaces(null);
            }
            
            if(!hasSearchRoomInfo) {
                repository.getServices().setSearchroom(null);
            }

            // ***************************************************************************************
            // 		Library
            // ***************************************************************************************

            if(repository.getServices().getLibrary() == null)
                repository.getServices().setLibrary(new Library());

			Library library = repository.getServices().getLibrary();
			boolean libraryExists = false;
			boolean hasLibraryContactInfo = false;
			boolean hasLibraryWebPage=false;

            if (library.getContact() == null) {
            	library.setContact(new Contact());
            }

            //Telephone in library
            library.getContact().getTelephone().clear();
            for(JTextField field : telephoneLibraryTf) {
                if(StringUtils.isNotEmpty(field.getText().trim())) {
                    Telephone telephone = new Telephone();
                    telephone.setContent(field.getText());
                    library.getContact().getTelephone().add(telephone);
                    hasLibraryContactInfo = true;
                }
            }

            //email in library
            library.getContact().getEmail().clear();
            for(int i = 0; i < emailLibraryTf.size(); i++) {
                JTextField field = emailLibraryTf.get(i);
                JTextField fieldTitle = emailTitleLibraryTf.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim())|| StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Email email = new Email();
                    email.setHref(field.getText());
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                        email.setContent(fieldTitle.getText());
//                    else
//                        email.setContent(field.getText());
                    library.getContact().getEmail().add(email);
                    hasLibraryContactInfo = true;
                }
            }

            if(!hasLibraryContactInfo) {
                library.setContact(null);
            }
            libraryExists = hasLibraryContactInfo;

            //web page in Library
            library.getWebpage().clear();
            for(int i = 0; i < webpageLibraryTf.size(); i++) {
                JTextField field = webpageLibraryTf.get(i);
                JTextField fieldTitle = webpageTitleLibraryTf.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim()) || StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Webpage webpage = new Webpage();
                    webpage.setHref(field.getText());
                    if(StringUtils.isNotEmpty(field.getText().trim())){
	                    if(!StringUtils.startsWithAny(field.getText(), webPrefixes))
	                        errors.add("webpageLibraryTf");
                    }
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                    	webpage.setContent(fieldTitle.getText());
//                    else
//                    	webpage.setContent(field.getText());
                    library.getWebpage().add(webpage);
                    hasLibraryWebPage = true;
                    libraryExists=true;
                }
            }

            if(!libraryExists)
            	libraryExists = hasLibraryWebPage;
            
            
            if(StringUtils.isNotEmpty(monographicPubLibraryTf.getText())) {
                Num num = new Num();
                num.setUnit("site");
                num.setContent(monographicPubLibraryTf.getText());
                library.getMonographicpub().setNum(num);
                libraryExists = true;
            } else {
                library.setMonographicpub(null);
            }
            if(StringUtils.isNotEmpty(serialPubLibraryTf.getText())) {
                Num num = new Num();
                num.setUnit("site");
                num.setContent(serialPubLibraryTf.getText());
                library.getSerialpub().setNum(num);
                libraryExists = true;
            } else {
                library.setSerialpub(null);
            }
            library.setQuestion("yes");
            
            InternetAccess internetAccess = repository.getServices().getInternetAccess();
            internetAccess.setQuestion("yes");
            boolean isInternetAccessFilled = false;
            if(internetAccessDescTfs.size() > 0) {
                internetAccess.getDescriptiveNote().getP().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : internetAccessDescTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        P p = new P();
                        p.setContent(textFieldWithLanguage.getTextValue());
                        p.setLang(textFieldWithLanguage.getLanguage());
                        internetAccess.getDescriptiveNote().getP().add(p);
                        isInternetAccessFilled = true;
                    }
                }
            }
            if(!isInternetAccessFilled) {
                repository.getServices().setInternetAccess(null);
            }
            
            if(!libraryExists) {
                repository.getServices().setLibrary(null);
            }
            
            // ***************************************************************************************
            // 		Technical services
            // ***************************************************************************************

            if(repository.getServices().getTechservices() == null)
                repository.getServices().setTechservices(new Techservices());
            
            Techservices techservices = repository.getServices().getTechservices();
            
            // ***************************************************************************************
            // 		Restoration lab
            // ***************************************************************************************

            if(repository.getServices().getTechservices().getRestorationlab() == null)
                repository.getServices().getTechservices().setRestorationlab(new Restorationlab());

            Restorationlab restorationlab = techservices.getRestorationlab();

            boolean isRestorationDescFilled = false;
            boolean restorationLabExists = false;
            boolean hasRestorationLabWebPage = false;
            boolean hasRestorationlabContactInfo = false;

            if(descriptionRestorationServiceTfs.size() > 0) {
                restorationlab.getDescriptiveNote().getP().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : descriptionRestorationServiceTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        P p = new P();
                        p.setContent(textFieldWithLanguage.getTextValue());
                        p.setLang(textFieldWithLanguage.getLanguage());
                        restorationlab.getDescriptiveNote().getP().add(p);
                        isRestorationDescFilled = true;
                        restorationLabExists = true;
                    }
                }
            }
            if(!isRestorationDescFilled) {
                restorationlab.setDescriptiveNote(null);
            }


            if (restorationlab.getContact() == null) {
            	restorationlab.setContact(new Contact());
            }

            //telephone in Restoration
            restorationlab.getContact().getTelephone().clear();
            for(JTextField field : telephoneRestorationlabTf) {
                if(StringUtils.isNotEmpty(field.getText().trim())) {
                    Telephone telephone = new Telephone();
                    telephone.setContent(field.getText());
                    restorationlab.getContact().getTelephone().add(telephone);
                    hasRestorationlabContactInfo = true;
                }
            }

            //email in Restoration
            restorationlab.getContact().getEmail().clear();
            for(int i = 0; i < emailRestorationlabTf.size(); i++) {
                JTextField field = emailRestorationlabTf.get(i);
                JTextField fieldTitle = emailTitleRestorationlabTf.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim())|| StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Email email = new Email();
                    email.setHref(field.getText());
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                        email.setContent(fieldTitle.getText());
//                    else
//                        email.setContent(field.getText());
                    restorationlab.getContact().getEmail().add(email);
                    hasRestorationlabContactInfo = true;
                }
            }
            if(!hasRestorationlabContactInfo) {
                restorationlab.setContact(null);
            }
            if (!restorationLabExists)
            	restorationLabExists = restorationLabExists || hasRestorationlabContactInfo;
            
            //web page in restoration laboratory
            restorationlab.getWebpage().clear();
            for(int i = 0; i < webpageRestorationlabTf.size(); i++) {
                JTextField field = webpageRestorationlabTf.get(i);
                JTextField fieldTitle = webpageTitleRestorationlabTf.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim()) || StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Webpage webpage = new Webpage();
                    webpage.setHref(field.getText());
                    if(StringUtils.isNotEmpty(field.getText().trim())){
	                    if(!StringUtils.startsWithAny(field.getText(), webPrefixes))
	                        errors.add("webpageRestorationlabTf");
                    }
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                    	webpage.setContent(fieldTitle.getText());
//                    else
//                    	webpage.setContent(field.getText());
                    restorationlab.getWebpage().add(webpage);
                    hasRestorationLabWebPage = true;
                    restorationLabExists=true;
                }
            }
            restorationlab.setQuestion("yes");
            
            if (!restorationLabExists)
            	restorationLabExists = restorationLabExists || hasRestorationLabWebPage;
            
            if(!restorationLabExists)
                techservices.setRestorationlab(null);
            
            // ***************************************************************************************
            // 		Reproduction services
            // ***************************************************************************************

            if(repository.getServices().getTechservices().getReproductionser() == null)
                repository.getServices().getTechservices().setReproductionser(new Reproductionser());

            Reproductionser reproductionser = techservices.getReproductionser();
            boolean hasReproductionInfo = false;
            boolean isReproductionDescFilled = false;
            boolean hasReproductionserContactInfo = false;
            boolean hasReproductionWebPage = false;

            if(descriptionReproductionServiceTfs.size() > 0) {
                reproductionser.getDescriptiveNote().getP().clear();
                for(TextFieldWithLanguage textFieldWithLanguage : descriptionReproductionServiceTfs) {
                    if(StringUtils.isNotEmpty(textFieldWithLanguage.getTextValue())) {
                        P p = new P();
                        p.setContent(textFieldWithLanguage.getTextValue());
                        p.setLang(textFieldWithLanguage.getLanguage());
                        reproductionser.getDescriptiveNote().getP().add(p);
                        isReproductionDescFilled = true;
                    }
                }
            }
            if(!isReproductionDescFilled) {
                reproductionser.setDescriptiveNote(null);
            }

            if (reproductionser.getContact() == null) {
            	reproductionser.setContact(new Contact());
            }
            
            //telephone in Reproduction
            reproductionser.getContact().getTelephone().clear();
            for(JTextField field : telephoneReproductionServiceTf) {
                if(StringUtils.isNotEmpty(field.getText().trim())) {
                    Telephone telephone = new Telephone();
                    telephone.setContent(field.getText());
                    reproductionser.getContact().getTelephone().add(telephone);
                    hasReproductionserContactInfo = true;
                }
            }

            //email in Reproduction
            reproductionser.getContact().getEmail().clear();
            for(int i = 0; i < emailReproductionServiceTf.size(); i++) {
                JTextField field = emailReproductionServiceTf.get(i);
                JTextField fieldTitle = emailTitleReproductionServiceTf.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim())|| StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Email email = new Email();
                    email.setHref(field.getText());
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                        email.setContent(fieldTitle.getText());
//                    else
//                        email.setContent(field.getText());
                    reproductionser.getContact().getEmail().add(email);
                    hasReproductionserContactInfo = true;
                }
            }

            if(!hasReproductionserContactInfo) {
                reproductionser.setContact(null);
            }
            if(isReproductionDescFilled || hasReproductionserContactInfo) {
                hasReproductionInfo = true;
            }

            //web page in reproduction services
            reproductionser.getWebpage().clear();
            for(int i = 0; i < webpageReproductionServiceTf.size(); i++) {
                JTextField field = webpageReproductionServiceTf.get(i);
                JTextField fieldTitle = webpageTitleReproductionServiceTf.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim()) || StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Webpage webpage = new Webpage();
                    webpage.setHref(field.getText());
                    if(StringUtils.isNotEmpty(field.getText().trim())){
	                    if(!StringUtils.startsWithAny(field.getText(), webPrefixes))
	                        errors.add("webpageReproductionServiceTf");
                    }
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                    	webpage.setContent(fieldTitle.getText());
//                    else
//                    	webpage.setContent(field.getText());
                    reproductionser.getWebpage().add(webpage);
                    hasReproductionWebPage = true;
                }
            }
            
            if(hasReproductionWebPage) {
                hasReproductionInfo = true;
            }
                       
            if(!microformServicesCombo.getSelectedItem().equals("---")) {
                reproductionser.getMicroformser().setQuestion((String)microformServicesCombo.getSelectedItem());
                hasReproductionInfo = true;
            } else {
                reproductionser.setMicroformser(null);
            }
            if(!photographServicesCombo.getSelectedItem().equals("---")) {
                reproductionser.getPhotographser().setQuestion((String)photographServicesCombo.getSelectedItem());
                hasReproductionInfo = true;
            } else {
                reproductionser.setPhotographser(null);
            }
            if(!digitalServicesCombo.getSelectedItem().equals("---")) {
                reproductionser.getDigitalser().setQuestion((String)digitalServicesCombo.getSelectedItem());
                hasReproductionInfo = true;
            } else {
                reproductionser.setDigitalser(null);
            }
            if(!photocopyServicesCombo.getSelectedItem().equals("---")) {
                reproductionser.getPhotocopyser().setQuestion((String)photocopyServicesCombo.getSelectedItem());
                hasReproductionInfo = true;
            } else {
                reproductionser.setPhotocopyser(null);
            }
            reproductionser.setQuestion("yes");

            if(!hasReproductionInfo) {
                techservices.setReproductionser(null);
            }

            if(techservices.getReproductionser() == null && techservices.getRestorationlab() == null) {
                repository.getServices().setTechservices(null);
            }
            
            // **************************************************************
            // Recreational Services
            // **************************************************************
            
            if(repository.getServices().getRecreationalServices() == null)
                repository.getServices().setRecreationalServices(new RecreationalServices());

            RecreationalServices recreationalServices = repository.getServices().getRecreationalServices();
            boolean hasRecreationalServices = false;
            if(StringUtils.isNotEmpty(refreshmentTf.getTextValue())) {
                recreationalServices.getRefreshment().getDescriptiveNote().getP().get(0).setContent(refreshmentTf.getTextValue());
                recreationalServices.getRefreshment().getDescriptiveNote().getP().get(0).setLang(refreshmentTf.getLanguage());
                hasRecreationalServices = true;
            } else {
                recreationalServices.setRefreshment(null);
            }

            if(exhibitionTfs.size() > 0) {
                recreationalServices.getExhibition().clear();
                for(TextAreaWithLanguage textAreaWithLanguage : exhibitionTfs) {
                    if(StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        Exhibition exhibition = new Exhibition();
                        DescriptiveNote descriptiveNote = new DescriptiveNote();
                        descriptiveNote.setP(new ArrayList<P>(){{ add(new P()); }});
                        exhibition.setDescriptiveNote(descriptiveNote);
                        exhibition.getDescriptiveNote().getP().get(0).setLang(textAreaWithLanguage.getLanguage());
                        exhibition.getDescriptiveNote().getP().get(0).setContent(textAreaWithLanguage.getTextValue());
                        if(StringUtils.isNotEmpty(textAreaWithLanguage.getExtraValue())) {
                            exhibition.setWebpage(new Webpage());
                            exhibition.getWebpage().setHref(textAreaWithLanguage.getExtraValue());
                            if(!StringUtils.startsWithAny(textAreaWithLanguage.getExtraValue(), webPrefixes))
                                errors.add("exhibitionTfs");
                            exhibition.getWebpage().setContent(textAreaWithLanguage.getSecondExtraValue());
                        }
                        recreationalServices.getExhibition().add(exhibition);
                        hasRecreationalServices = true;
                    }
                }
            }

            if(toursAndSessionsTfs.size() > 0) {
                recreationalServices.getToursSessions().clear();
                for(TextAreaWithLanguage textAreaWithLanguage : toursAndSessionsTfs) {
                    if(StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        ToursSessions toursSessions = new ToursSessions();
                        DescriptiveNote descriptiveNote = new DescriptiveNote();
                        descriptiveNote.setP(new ArrayList<P>(){{ add(new P()); }});
                        toursSessions.setDescriptiveNote(descriptiveNote);
                        toursSessions.getDescriptiveNote().getP().get(0).setLang(textAreaWithLanguage.getLanguage());
                        toursSessions.getDescriptiveNote().getP().get(0).setContent(textAreaWithLanguage.getTextValue());
                        if(StringUtils.isNotEmpty(textAreaWithLanguage.getExtraValue())) {
                            toursSessions.setWebpage(new Webpage());
                            toursSessions.getWebpage().setHref(textAreaWithLanguage.getExtraValue());
                            if(!StringUtils.startsWithAny(textAreaWithLanguage.getExtraValue(), webPrefixes))
                                errors.add("toursAndSessionsTfs");
                            toursSessions.getWebpage().setContent(textAreaWithLanguage.getSecondExtraValue());
                        }
                        recreationalServices.getToursSessions().add(toursSessions);
                        hasRecreationalServices = true;
                    }
                }
            }

            if(otherServicesTfs.size() > 0) {
                recreationalServices.getOtherServices().clear();
                for(TextAreaWithLanguage textAreaWithLanguage : otherServicesTfs) {
                    if(StringUtils.isNotEmpty(textAreaWithLanguage.getTextValue())) {
                        OtherServices otherServices = new OtherServices();
                        DescriptiveNote descriptiveNote = new DescriptiveNote();
                        descriptiveNote.setP(new ArrayList<P>(){{ add(new P()); }});
                        otherServices.setDescriptiveNote(descriptiveNote);
                        otherServices.getDescriptiveNote().getP().get(0).setLang(textAreaWithLanguage.getLanguage());
                        otherServices.getDescriptiveNote().getP().get(0).setContent(textAreaWithLanguage.getTextValue());
                        if(StringUtils.isNotEmpty(textAreaWithLanguage.getExtraValue())) {
                            otherServices.setWebpage(new Webpage());
                            otherServices.getWebpage().setHref(textAreaWithLanguage.getExtraValue());
                            if(!StringUtils.startsWithAny(textAreaWithLanguage.getExtraValue(), webPrefixes))
                                errors.add("otherServicesTfs");
                            otherServices.getWebpage().setContent(textAreaWithLanguage.getSecondExtraValue());
                        }
                        recreationalServices.getOtherServices().add(otherServices);
                        hasRecreationalServices = true;
                    }
                }
            }

            if(!hasRecreationalServices)
                repository.getServices().setRecreationalServices(null);

            if(repository.getServices().getInternetAccess() == null && 
    		repository.getServices().getLibrary() == null && 
    		repository.getServices().getRecreationalServices() == null && 
    		repository.getServices().getSearchroom() == null && 
    		repository.getServices().getTechservices() == null) {
                repository.setServices(null);
            }

            if(!errors.isEmpty()) {
//            	String strOut ="";
//            	
//            	if (errors.contains("termsOfUseTfs"))
//            		strOut+= labels.getString("eag2012.errors.errorTermsOfUse")+"\n";
//            	
//             	if (errors.contains("webpageSearchroomTf"))
//            		strOut+= labels.getString("eag2012.portal.searchroom") +":\n" + labels.getString("eag2012.errors.webpageProtocol")+ "\n";
//
//             	if (errors.contains("webpageLibraryTf"))
//            		strOut+= labels.getString("eag2012.portal.library") +":\n" + labels.getString("eag2012.errors.webpageProtocol") + "\n";
//
//            	if (errors.contains("webpageRestorationlabTf"))
//            		strOut+= labels.getString("eag2012.portal.restorationlabcontact") +":\n" + labels.getString("eag2012.errors.webpageProtocol")+ "\n";
//
//            	if (errors.contains("webpageReproductionServiceTf"))
//            		strOut+= labels.getString("eag2012.accessAndServices.reproductionService") +":\n" + labels.getString("eag2012.errors.webpageProtocol")+ "\n";
//
//        		JOptionPane.showMessageDialog(eag2012Frame, strOut);
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}
