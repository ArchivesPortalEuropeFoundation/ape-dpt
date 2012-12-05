package eu.apenet.dpt.standalone.gui.eag2012;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.eag2012.data.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 28/11/2012
 *
 * @author Yoann Moranville
 */
public class EagAccessAndServicesPanel extends EagPanels {
    private JTextField openingTimesTf;
    private JTextField closingTimesTf;
    private List<JTextField> travellingDirectionsTfs;
    private List<JTextField> linkTravellingDirectionsTfs;
    private List<JTextField> restaccessTfs;
    private List<JTextField> termsOfUseTfs;
    private List<JTextField> linkTermsOfUseTfs;
    private List<JTextField> accessibilityTfs;
    private JTextField telephoneSearchroomTf;
    private JTextField emailSearchroomTf;
    private JTextField emailTitleSearchroomTf;
    private JTextField webpageSearchroomTf;
    private JTextField webpageTitleSearchroomTf;
    private JTextField workplacesSearchroomTf;
    private JTextField computerplacesSearchroomTf;
    private JTextField microfilmplacesSearchroomTf;
    private List<JTextField> readersticketSearchroomTfs;
    private List<JTextField> readersticketTitleSearchroomTfs;
    private List<JTextField> advancedordersSearchroomTfs;
    private List<JTextField> advancedordersTitleSearchroomTfs;
    private List<JTextField> researchServicesSearchroomTfs;
    private JTextField telephoneLibraryTf;
    private JTextField emailLibraryTf;
    private JTextField emailTitleLibraryTf;
    private JTextField webpageLibraryTf;
    private JTextField webpageTitleLibraryTf;
    private JTextField monographicPubLibraryTf;
    private JTextField serialPubLibraryTf;
    private JTextField internetAccessDescTf;
    private JTextField telephoneRestorationlabTf;
    private JTextField emailRestorationlabTf;
    private JTextField emailTitleRestorationlabTf;
    private JTextField webpageRestorationlabTf;
    private JTextField webpageTitleRestorationlabTf;
    private JTextField telephoneReproductionServiceTf;
    private JTextField emailReproductionServiceTf;
    private JTextField emailTitleReproductionServiceTf;
    private JTextField webpageReproductionServiceTf;
    private JTextField webpageTitleReproductionServiceTf;
    private JComboBox microformServicesCombo = new JComboBox(yesOrNo);
    private JComboBox photographServicesCombo = new JComboBox(yesOrNo);
    private JComboBox digitalServicesCombo = new JComboBox(yesOrNo);
    private JComboBox photocopyServicesCombo = new JComboBox(yesOrNo);
    private JTextField refreshmentTf;

    private List<JTextField> exhibitionTfs;
    private List<JTextField> exhibitionWebpageTfs;
    private List<JTextField> exhibitionWebpageTitleTfs;
    private List<JTextField> toursAndSessionsTfs;
    private List<JTextField> toursAndSessionsWebpageTfs;
    private List<JTextField> toursAndSessionsWebpageTitleTfs;
    private List<JTextField> otherServicesTfs;
    private List<JTextField> otherServicesWebpageTfs;
    private List<JTextField> otherServicesWebpageTitleTfs;

    public EagAccessAndServicesPanel(Eag eag, JTabbedPane tabbedPane, JFrame eag2012Frame, ProfileListModel model) {
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

        builder.addLabel(labels.getString("eag2012.openingTimesLabel") + "*",    cc.xy (1, rowNb));
        openingTimesTf = new JTextField(repository.getTimetable().getOpening().getContent());
        builder.add(openingTimesTf, cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.closingTimesLabel"), cc.xy(5, rowNb));
        if(repository.getTimetable().getClosing() == null) {
            repository.getTimetable().setClosing(new Closing());
        }
        closingTimesTf = new JTextField(repository.getTimetable().getClosing().getContent());
        builder.add(closingTimesTf,                                            cc.xy (7, rowNb));
        setNextRow();

        if(repository.getDirections().size() == 0)
            repository.getDirections().add(new Directions());
        travellingDirectionsTfs = new ArrayList<JTextField>(repository.getDirections().size());
        linkTravellingDirectionsTfs = new ArrayList<JTextField>(repository.getDirections().size());
        for(Directions directions : repository.getDirections()) {
            builder.addLabel(labels.getString("eag2012.travellingDirections"),    cc.xy (1, rowNb));
            String str = "";
            String citation = "";
            for(Object obj : directions.getContent()) {
                if(obj instanceof String) {
                    str += (String)obj;
                } else if(obj instanceof Citation) {
                    citation += ((Citation) obj).getHref();
                }
            }
            JTextField travellingDirectionsTf = new JTextField(str);
            travellingDirectionsTfs.add(travellingDirectionsTf);
            builder.add(travellingDirectionsTf,                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkLabel"), cc.xy(5, rowNb));
            JTextField linkTf = new JTextField(citation);
            linkTravellingDirectionsTfs.add(linkTf);
            builder.add(linkTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }

        JButton addTravellingDirectionsBtn = new ButtonEag(labels.getString("eag2012.addTravallingDirectionsButton"));
        builder.add(addTravellingDirectionsBtn, cc.xy (1, rowNb));
//        addTravellingDirectionsBtn.addActionListener(new AddTravellingDirectionsBtnAction());
        setNextRow();

        builder.addLabel(labels.getString("eag2012.accessiblePublicLabel") + "*",    cc.xy (1, rowNb));
        if(Arrays.asList(yesOrNo).contains(repository.getAccess().getQuestion())) {
            accessiblePublicCombo.setSelectedItem(repository.getAccess().getQuestion());
        }
        builder.add(accessiblePublicCombo, cc.xy(3, rowNb));
        setNextRow();

        if(repository.getAccess().getRestaccess().size() == 0)
            repository.getAccess().getRestaccess().add(new Restaccess());
        restaccessTfs = new ArrayList<JTextField>(repository.getAccess().getRestaccess().size());
        int last = repository.getAccess().getRestaccess().size() - 1;
        for(Restaccess restaccess : repository.getAccess().getRestaccess()) {
            builder.addLabel(labels.getString("eag2012.accessRestrictions"),    cc.xy (1, rowNb));
            JTextField restaccessTf = new JTextField(restaccess.getContent());
            restaccessTfs.add(restaccessTf);
            builder.add(restaccessTf,                     cc.xy (3, rowNb));
//            builder.addLabel(labels.getString("eag2012.language"), cc.xy(5, rowNb));
//            linkTf = new JTextField(citation);
//            builder.add(linkTf,                                            cc.xy (7, rowNb));
            if(last-- == 0) {
                JButton addRestaccessBtn = new ButtonEag(labels.getString("eag2012.addRestaccessButton"));
                builder.add(addRestaccessBtn, cc.xy (7, rowNb));
//                addRestaccessBtn.addActionListener(new AddRestaccessBtnAction());
            }
            setNextRow();
        }


        if(repository.getAccess().getTermsOfUse().size() == 0)
            repository.getAccess().getTermsOfUse().add(new TermsOfUse());
        termsOfUseTfs = new ArrayList<JTextField>(repository.getAccess().getTermsOfUse().size());
        linkTermsOfUseTfs = new ArrayList<JTextField>(repository.getAccess().getTermsOfUse().size());
        for(TermsOfUse termsOfUse : repository.getAccess().getTermsOfUse()) {
            builder.addLabel(labels.getString("eag2012.termsOfUse"),    cc.xy (1, rowNb));
            JTextField termsOfUseTf = new JTextField(termsOfUse.getContent());
            termsOfUseTfs.add(termsOfUseTf);
            builder.add(termsOfUseTf,                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkLabel"), cc.xy(5, rowNb));
            JTextField linkTermsOfUseTf = new JTextField(termsOfUse.getHref());
            linkTermsOfUseTfs.add(linkTermsOfUseTf);
            builder.add(linkTermsOfUseTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }

        builder.addLabel(labels.getString("eag2012.facilitiesForDisabledLabel"), cc.xy(1, rowNb));
        if(repository.getAccessibility().size() > 0 && Arrays.asList(yesOrNo).contains(repository.getAccessibility().get(0).getQuestion())) {
            facilitiesForDisabledCombo.setSelectedItem(repository.getAccessibility().get(0).getQuestion());
        }
        builder.add(facilitiesForDisabledCombo, cc.xy(3, rowNb));
        setNextRow();

        if(repository.getAccessibility().size() == 0)
            repository.getAccessibility().add(new Accessibility());
        accessibilityTfs = new ArrayList<JTextField>(repository.getAccessibility().size());
        for(Accessibility accessibility : repository.getAccessibility()) {
            builder.addLabel(labels.getString("eag2012.accessibility"),    cc.xy (1, rowNb));
            JTextField accessibilityTf = new JTextField(accessibility.getContent());
            accessibilityTfs.add(accessibilityTf);
            builder.add(accessibilityTf,                     cc.xy (3, rowNb));
//            builder.addLabel(labels.getString("eag2012.linkLabel"), cc.xy(5, rowNb));
//            JTextField linkTermsOfUseTf = new JTextField(termsOfUse.getHref());
//            linkTermsOfUseTfs.add(linkTermsOfUseTf);
//            builder.add(linkTermsOfUseTf,                                            cc.xy (7, rowNb));
            if(last-- == 0) {
                JButton addAccessibilityBtn = new ButtonEag(labels.getString("eag2012.addAccessibilityButton"));
                builder.add(addAccessibilityBtn, cc.xy (7, rowNb));
//                addAccessibilityBtn.addActionListener(new AddAccessibilityBtnAction());
            }
            setNextRow();
        }

        builder.addSeparator(labels.getString("eag2012.searchroom"), cc.xyw(1, rowNb, 7));
        setNextRow();
        if(repository.getServices() == null)
            repository.setServices(new Services());
        if(repository.getServices().getSearchroom() == null)
            repository.getServices().setSearchroom(new Searchroom());
        Searchroom searchroom = repository.getServices().getSearchroom();

        if(searchroom.getContact() == null)
            searchroom.setContact(new Contact());

        if(searchroom.getContact().getTelephone().size() == 0)
            searchroom.getContact().getTelephone().add(new Telephone());
        builder.addLabel(labels.getString("eag2012.telephone"),    cc.xy (1, rowNb));
        telephoneSearchroomTf = new JTextField(searchroom.getContact().getTelephone().get(0).getContent());
        builder.add(telephoneSearchroomTf, cc.xy(3, rowNb));
        setNextRow();

        if(searchroom.getContact().getEmail().size() == 0)
            searchroom.getContact().getEmail().add(new Email());
        builder.addLabel(labels.getString("eag2012.email"),    cc.xy (1, rowNb));
        emailSearchroomTf = new JTextField(searchroom.getContact().getEmail().get(0).getHref());
        builder.add(emailSearchroomTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.emailTitle"),    cc.xy (5, rowNb));
        emailTitleSearchroomTf = new JTextField(searchroom.getContact().getEmail().get(0).getContent());
        builder.add(emailTitleSearchroomTf,    cc.xy (7, rowNb));
        setNextRow();

        if(searchroom.getWebpage().size() == 0)
            searchroom.getWebpage().add(new Webpage());
        builder.addLabel(labels.getString("eag2012.webpage"),    cc.xy (1, rowNb));
        webpageSearchroomTf = new JTextField(searchroom.getWebpage().get(0).getHref());
        builder.add(webpageSearchroomTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.webpageTitle"),    cc.xy (5, rowNb));
        webpageTitleSearchroomTf = new JTextField(searchroom.getWebpage().get(0).getContent());
        builder.add(webpageTitleSearchroomTf,    cc.xy (7, rowNb));
        setNextRow();

        if(searchroom.getWorkPlaces() == null)
            searchroom.setWorkPlaces(new WorkPlaces());
        builder.addLabel(labels.getString("eag2012.workplaces"),    cc.xy (1, rowNb));
        workplacesSearchroomTf = new JTextField(searchroom.getWorkPlaces().getNum().getContent());
        builder.add(workplacesSearchroomTf,    cc.xy (3, rowNb));
        setNextRow();

        if(searchroom.getComputerPlaces() == null) {
            ComputerPlaces computerPlaces = new ComputerPlaces();
            computerPlaces.setNum(new Num());
            searchroom.setComputerPlaces(computerPlaces);
        }
        builder.addLabel(labels.getString("eag2012.computerplaces"),    cc.xy (1, rowNb));
        computerplacesSearchroomTf = new JTextField(searchroom.getComputerPlaces().getNum().getContent());
        builder.add(computerplacesSearchroomTf,    cc.xy (3, rowNb));
        JButton addDescriptionBtn = new ButtonEag(labels.getString("eag2012.addDescriptionButton"));
        builder.add(addDescriptionBtn, cc.xy (5, rowNb));
//        addDescriptionBtn.addActionListener(new AddDescriptionBtnAction());
        setNextRow();

        if(searchroom.getMicrofilmPlaces() == null) {
            MicrofilmPlaces microfilmPlaces = new MicrofilmPlaces();
            microfilmPlaces.setNum(new Num());
            searchroom.setMicrofilmPlaces(microfilmPlaces);
        }
        if(searchroom.getPhotographAllowance() == null) {
            searchroom.setPhotographAllowance(new PhotographAllowance());
        }
        builder.addLabel(labels.getString("eag2012.microfilmplaces"),    cc.xy (1, rowNb));
        microfilmplacesSearchroomTf = new JTextField(searchroom.getMicrofilmPlaces().getNum().getContent());
        builder.add(microfilmplacesSearchroomTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.photographAllowance"),    cc.xy (5, rowNb));
        if(Arrays.asList(photographAllowance).contains(searchroom.getPhotographAllowance().getValue())) {
            photographAllowanceCombo.setSelectedItem(searchroom.getPhotographAllowance().getValue());
        }
        builder.add(photographAllowanceCombo, cc.xy(7, rowNb));
        setNextRow();

        if(searchroom.getReadersTicket().size() == 0)
            searchroom.getReadersTicket().add(new ReadersTicket());
        readersticketSearchroomTfs = new ArrayList<JTextField>(searchroom.getReadersTicket().size());
        readersticketTitleSearchroomTfs = new ArrayList<JTextField>(searchroom.getReadersTicket().size());
        for(ReadersTicket readersTicket : searchroom.getReadersTicket()) {
            builder.addLabel(labels.getString("eag2012.readersTicket"),    cc.xy (1, rowNb));
            JTextField readersticketTf = new JTextField(readersTicket.getContent());
            readersticketSearchroomTfs.add(readersticketTf);
            builder.add(readersticketTf,                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkLabel"), cc.xy(5, rowNb));
            JTextField readersticketTitleTf = new JTextField(readersTicket.getHref());
            readersticketTitleSearchroomTfs.add(readersticketTitleTf);
            builder.add(readersticketTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addReadersticketBtn = new ButtonEag(labels.getString("eag2012.addReadersticketButton"));
        builder.add(addReadersticketBtn, cc.xy (1, rowNb));
//        addReadersticketBtn.addActionListener(new AddReadersticketBtnAction());
        setNextRow();

        if(searchroom.getAdvancedOrders().size() == 0)
            searchroom.getAdvancedOrders().add(new AdvancedOrders());
        advancedordersSearchroomTfs = new ArrayList<JTextField>(searchroom.getAdvancedOrders().size());
        advancedordersTitleSearchroomTfs = new ArrayList<JTextField>(searchroom.getAdvancedOrders().size());
        for(AdvancedOrders advancedOrders : searchroom.getAdvancedOrders()) {
            builder.addLabel(labels.getString("eag2012.advancedOrders"),    cc.xy (1, rowNb));
            JTextField advancedordersTf = new JTextField(advancedOrders.getContent());
            advancedordersSearchroomTfs.add(advancedordersTf);
            builder.add(advancedordersTf,                     cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.linkLabel"), cc.xy(5, rowNb));
            JTextField advancedordersTitleTf = new JTextField(advancedOrders.getHref());
            advancedordersTitleSearchroomTfs.add(advancedordersTitleTf);
            builder.add(advancedordersTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addAdvancedordersBtn = new ButtonEag(labels.getString("eag2012.addAdvancedordersButton"));
        builder.add(addAdvancedordersBtn, cc.xy (1, rowNb));
//        addAdvancedordersBtn.addActionListener(new AddAdvancedordersBtnAction());
        setNextRow();

        if(searchroom.getResearchServices().size() == 0) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            ResearchServices researchServices = new ResearchServices();
            researchServices.setDescriptiveNote(descriptiveNote);
            searchroom.getResearchServices().add(researchServices);
        }
        researchServicesSearchroomTfs = new ArrayList<JTextField>(searchroom.getResearchServices().size());
        for(ResearchServices researchServices : searchroom.getResearchServices()) {
            builder.addLabel(labels.getString("eag2012.researchServices"),    cc.xy (1, rowNb));
            JTextField researchServicesTf = new JTextField(researchServices.getDescriptiveNote().getP().get(0).getContent());
            researchServicesSearchroomTfs.add(researchServicesTf);
            builder.add(researchServicesTf,                     cc.xy (3, rowNb));
            setNextRow();
        }
        JButton addResearchservicesBtn = new ButtonEag(labels.getString("eag2012.addResearchservicesButton"));
        builder.add(addResearchservicesBtn, cc.xy (1, rowNb));
//        addResearchservicesBtn.addActionListener(new AddResearchservicesBtnAction());
        setNextRow();





        builder.addSeparator(labels.getString("eag2012.library"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if(repository.getServices().getLibrary() == null)
            repository.getServices().setLibrary(new Library());
        Library library = repository.getServices().getLibrary();

        if(library.getContact() == null)
            library.setContact(new Contact());

        if(library.getContact().getTelephone().size() == 0)
            library.getContact().getTelephone().add(new Telephone());
        builder.addLabel(labels.getString("eag2012.telephone"),    cc.xy (1, rowNb));
        telephoneLibraryTf = new JTextField(library.getContact().getTelephone().get(0).getContent());
        builder.add(telephoneLibraryTf, cc.xy(3, rowNb));
        setNextRow();

        if(library.getContact().getEmail().size() == 0)
            library.getContact().getEmail().add(new Email());
        builder.addLabel(labels.getString("eag2012.email"),    cc.xy (1, rowNb));
        emailLibraryTf = new JTextField(library.getContact().getEmail().get(0).getHref());
        builder.add(emailLibraryTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.emailTitle"),    cc.xy (5, rowNb));
        emailTitleLibraryTf = new JTextField(library.getContact().getEmail().get(0).getContent());
        builder.add(emailTitleLibraryTf,    cc.xy (7, rowNb));
        setNextRow();

        if(library.getWebpage().size() == 0)
            library.getWebpage().add(new Webpage());
        builder.addLabel(labels.getString("eag2012.webpage"),    cc.xy (1, rowNb));
        webpageLibraryTf = new JTextField(library.getWebpage().get(0).getHref());
        builder.add(webpageLibraryTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.webpageTitle"),    cc.xy (5, rowNb));
        webpageTitleLibraryTf = new JTextField(library.getWebpage().get(0).getContent());
        builder.add(webpageTitleLibraryTf,    cc.xy (7, rowNb));
        setNextRow();

        if(library.getMonographicpub() == null) {
            Monographicpub monographicpub = new Monographicpub();
            monographicpub.setNum(new Num());
            library.setMonographicpub(monographicpub);
        }
        if(library.getSerialpub() == null) {
            Serialpub serialpub = new Serialpub();
            serialpub.setNum(new Num());
            library.setSerialpub(serialpub);
        }
        builder.addLabel(labels.getString("eag2012.monographicPub"),    cc.xy (1, rowNb));
        monographicPubLibraryTf = new JTextField(library.getMonographicpub().getNum().getContent());
        builder.add(monographicPubLibraryTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.serialPub"),    cc.xy (5, rowNb));
        serialPubLibraryTf = new JTextField(library.getSerialpub().getNum().getContent());
        builder.add(serialPubLibraryTf,    cc.xy (7, rowNb));
        setNextRow();



        builder.addSeparator(labels.getString("eag2012.internetAccess"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if(repository.getServices().getInternetAccess() == null) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            InternetAccess internetAccess = new InternetAccess();
            internetAccess.setDescriptiveNote(descriptiveNote);
            repository.getServices().setInternetAccess(internetAccess);
        }
        InternetAccess internetAccess = repository.getServices().getInternetAccess();
        builder.addLabel(labels.getString("eag2012.description"),    cc.xy (1, rowNb));
        internetAccessDescTf = new JTextField(internetAccess.getDescriptiveNote().getP().get(0).getContent());
        builder.add(internetAccessDescTf, cc.xy(3, rowNb));
        setNextRow();



//        builder.addSeparator(labels.getString("eag2012.technicalServices"), cc.xy(1, rowNb));
        builder.addSeparator(labels.getString("eag2012.technicalServices"), cc.xyw(1, rowNb, 7));
        setNextRow();
        builder.addSeparator(labels.getString("eag2012.restaurationLab"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if(repository.getServices().getTechservices() == null)
            repository.getServices().setTechservices(new Techservices());
        if(repository.getServices().getTechservices().getRestorationlab() == null)
            repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
        Restorationlab restorationlab = repository.getServices().getTechservices().getRestorationlab();

        if(restorationlab.getContact() == null)
            restorationlab.setContact(new Contact());

        if(restorationlab.getContact().getTelephone().size() == 0)
            restorationlab.getContact().getTelephone().add(new Telephone());
        builder.addLabel(labels.getString("eag2012.telephone"),    cc.xy (1, rowNb));
        telephoneRestorationlabTf = new JTextField(restorationlab.getContact().getTelephone().get(0).getContent());
        builder.add(telephoneRestorationlabTf, cc.xy(3, rowNb));
        setNextRow();

        if(restorationlab.getContact().getEmail().size() == 0)
            restorationlab.getContact().getEmail().add(new Email());
        builder.addLabel(labels.getString("eag2012.email"),    cc.xy (1, rowNb));
        emailRestorationlabTf = new JTextField(restorationlab.getContact().getEmail().get(0).getHref());
        builder.add(emailRestorationlabTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.emailTitle"),    cc.xy (5, rowNb));
        emailTitleRestorationlabTf = new JTextField(restorationlab.getContact().getEmail().get(0).getContent());
        builder.add(emailTitleRestorationlabTf,    cc.xy (7, rowNb));
        setNextRow();

        if(restorationlab.getWebpage().size() == 0)
            restorationlab.getWebpage().add(new Webpage());
        builder.addLabel(labels.getString("eag2012.webpage"),    cc.xy (1, rowNb));
        webpageRestorationlabTf = new JTextField(restorationlab.getWebpage().get(0).getHref());
        builder.add(webpageRestorationlabTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.webpageTitle"),    cc.xy (5, rowNb));
        webpageTitleRestorationlabTf = new JTextField(restorationlab.getWebpage().get(0).getContent());
        builder.add(webpageTitleRestorationlabTf,    cc.xy (7, rowNb));
        setNextRow();



        builder.addSeparator(labels.getString("eag2012.reproductionService"), cc.xyw(1, rowNb, 7));
        setNextRow();

        if(repository.getServices().getTechservices().getReproductionser() == null)
            repository.getServices().getTechservices().setReproductionser(new Reproductionser());
        Reproductionser reproductionser = repository.getServices().getTechservices().getReproductionser();

        if(reproductionser.getContact() == null)
            reproductionser.setContact(new Contact());

        if(reproductionser.getContact().getTelephone().size() == 0)
            reproductionser.getContact().getTelephone().add(new Telephone());
        builder.addLabel(labels.getString("eag2012.telephone"),    cc.xy (1, rowNb));
        telephoneReproductionServiceTf = new JTextField(reproductionser.getContact().getTelephone().get(0).getContent());
        builder.add(telephoneReproductionServiceTf, cc.xy(3, rowNb));
        setNextRow();

        if(reproductionser.getContact().getEmail().size() == 0)
            reproductionser.getContact().getEmail().add(new Email());
        builder.addLabel(labels.getString("eag2012.email"),    cc.xy (1, rowNb));
        emailReproductionServiceTf = new JTextField(reproductionser.getContact().getEmail().get(0).getHref());
        builder.add(emailReproductionServiceTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.emailTitle"),    cc.xy (5, rowNb));
        emailTitleReproductionServiceTf = new JTextField(reproductionser.getContact().getEmail().get(0).getContent());
        builder.add(emailTitleReproductionServiceTf,    cc.xy (7, rowNb));
        setNextRow();

        if(reproductionser.getWebpage().size() == 0)
            reproductionser.getWebpage().add(new Webpage());
        builder.addLabel(labels.getString("eag2012.webpage"),    cc.xy (1, rowNb));
        webpageReproductionServiceTf = new JTextField(reproductionser.getWebpage().get(0).getHref());
        builder.add(webpageReproductionServiceTf,    cc.xy (3, rowNb));
        builder.addLabel(labels.getString("eag2012.webpageTitle"),    cc.xy (5, rowNb));
        webpageTitleReproductionServiceTf = new JTextField(reproductionser.getWebpage().get(0).getContent());
        builder.add(webpageTitleReproductionServiceTf,    cc.xy (7, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.microformServices"), cc.xy(1, rowNb));
        if(Arrays.asList(yesOrNo).contains(reproductionser.getMicroformser().getQuestion())) {
            microformServicesCombo.setSelectedItem(reproductionser.getMicroformser().getQuestion());
        }
        builder.add(microformServicesCombo, cc.xy(3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.photographServices"), cc.xy(1, rowNb));
        if(Arrays.asList(yesOrNo).contains(reproductionser.getPhotographser().getQuestion())) {
            photographServicesCombo.setSelectedItem(reproductionser.getPhotographser().getQuestion());
        }
        builder.add(photographServicesCombo, cc.xy(3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.digitalServices"), cc.xy(1, rowNb));
        if(Arrays.asList(yesOrNo).contains(reproductionser.getDigitalser().getQuestion())) {
            digitalServicesCombo.setSelectedItem(reproductionser.getDigitalser().getQuestion());
        }
        builder.add(digitalServicesCombo, cc.xy(3, rowNb));
        setNextRow();

        builder.addLabel(labels.getString("eag2012.photocopyServices"), cc.xy(1, rowNb));
        if(Arrays.asList(yesOrNo).contains(reproductionser.getPhotocopyser().getQuestion())) {
            photocopyServicesCombo.setSelectedItem(reproductionser.getPhotocopyser().getQuestion());
        }
        builder.add(photocopyServicesCombo, cc.xy(3, rowNb));
        setNextRow();



        builder.addSeparator(labels.getString("eag2012.recreationalServices"), cc.xyw(1, rowNb, 7));
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
        builder.addLabel(labels.getString("eag2012.refreshment"), cc.xy(1, rowNb));
        refreshmentTf = new JTextField(recreationalServices.getRefreshment().getDescriptiveNote().getP().get(0).getContent());
        builder.add(refreshmentTf, cc.xy(3, rowNb));
        setNextRow();

        if(recreationalServices.getExhibition().size() == 0) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            Exhibition exhibition = new Exhibition();
            exhibition.setDescriptiveNote(descriptiveNote);
            exhibition.setWebpage(new Webpage());
            recreationalServices.getExhibition().add(exhibition);
        }
        exhibitionTfs = new ArrayList<JTextField>(recreationalServices.getExhibition().size());
        exhibitionWebpageTfs = new ArrayList<JTextField>(recreationalServices.getExhibition().size());
        exhibitionWebpageTitleTfs = new ArrayList<JTextField>(recreationalServices.getExhibition().size());
        for(Exhibition exhibition : recreationalServices.getExhibition()) {
            builder.addLabel(labels.getString("eag2012.exhibition"),    cc.xy (1, rowNb));
            JTextField exhibitionTf = new JTextField(exhibition.getDescriptiveNote().getP().get(0).getContent());
            exhibitionTfs.add(exhibitionTf);
            builder.add(exhibitionTf,                     cc.xy (3, rowNb));
            setNextRow();
            builder.addLabel(labels.getString("eag2012.webpage"), cc.xy(1, rowNb));
            JTextField exhibitionWebpageTf = new JTextField(exhibition.getWebpage().getHref());
            exhibitionWebpageTfs.add(exhibitionWebpageTf);
            builder.add(exhibitionWebpageTf,                                            cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.webpageTitle"), cc.xy(5, rowNb));
            JTextField exhibitionWebpageTitleTf = new JTextField(exhibition.getWebpage().getContent());
            exhibitionWebpageTitleTfs.add(exhibitionWebpageTitleTf);
            builder.add(exhibitionWebpageTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addExhibitionsBtn = new ButtonEag(labels.getString("eag2012.addExhibitionsButton"));
        builder.add(addExhibitionsBtn, cc.xy (1, rowNb));
//        addExhibitionsBtn.addActionListener(new AddExhibitionsBtnAction());
        setNextRow();

        if(recreationalServices.getToursSessions().size() == 0) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            ToursSessions toursSessions = new ToursSessions();
            toursSessions.setDescriptiveNote(descriptiveNote);
            toursSessions.setWebpage(new Webpage());
            recreationalServices.getToursSessions().add(toursSessions);
        }
        toursAndSessionsTfs = new ArrayList<JTextField>(recreationalServices.getToursSessions().size());
        toursAndSessionsWebpageTfs = new ArrayList<JTextField>(recreationalServices.getToursSessions().size());
        toursAndSessionsWebpageTitleTfs = new ArrayList<JTextField>(recreationalServices.getToursSessions().size());
        for(ToursSessions toursSessions : recreationalServices.getToursSessions()) {
            builder.addLabel(labels.getString("eag2012.toursAndSessions"),    cc.xy (1, rowNb));
            JTextField toursAndSessionsTf = new JTextField(toursSessions.getDescriptiveNote().getP().get(0).getContent());
            toursAndSessionsTfs.add(toursAndSessionsTf);
            builder.add(toursAndSessionsTf,                     cc.xy (3, rowNb));
            setNextRow();
            builder.addLabel(labels.getString("eag2012.webpage"), cc.xy(1, rowNb));
            JTextField toursAndSessionsWebpageTf = new JTextField(toursSessions.getWebpage().getHref());
            toursAndSessionsWebpageTfs.add(toursAndSessionsWebpageTf);
            builder.add(toursAndSessionsWebpageTf,                                            cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.webpageTitle"), cc.xy(5, rowNb));
            JTextField toursAndSessionsWebpageTitleTf = new JTextField(toursSessions.getWebpage().getContent());
            toursAndSessionsWebpageTitleTfs.add(toursAndSessionsWebpageTitleTf);
            builder.add(toursAndSessionsWebpageTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addToursSessionsBtn = new ButtonEag(labels.getString("eag2012.addToursSessionsButton"));
        builder.add(addToursSessionsBtn, cc.xy (1, rowNb));
//        addToursSessionsBtn.addActionListener(new AddToursSessionsBtnAction());
        setNextRow();

        if(recreationalServices.getOtherServices().size() == 0) {
            DescriptiveNote descriptiveNote = new DescriptiveNote();
            descriptiveNote.getP().add(new P());
            OtherServices otherServices = new OtherServices();
            otherServices.setDescriptiveNote(descriptiveNote);
            otherServices.setWebpage(new Webpage());
            recreationalServices.getOtherServices().add(otherServices);
        }
        otherServicesTfs = new ArrayList<JTextField>(recreationalServices.getOtherServices().size());
        otherServicesWebpageTfs = new ArrayList<JTextField>(recreationalServices.getOtherServices().size());
        otherServicesWebpageTitleTfs = new ArrayList<JTextField>(recreationalServices.getOtherServices().size());
        for(OtherServices otherServices : recreationalServices.getOtherServices()) {
            builder.addLabel(labels.getString("eag2012.otherServices"),    cc.xy (1, rowNb));
            JTextField otherServicesTf = new JTextField(otherServices.getDescriptiveNote().getP().get(0).getContent());
            otherServicesTfs.add(otherServicesTf);
            builder.add(otherServicesTf,                     cc.xy (3, rowNb));
            setNextRow();
            builder.addLabel(labels.getString("eag2012.webpage"), cc.xy(1, rowNb));
            JTextField otherServicesWebpageTf = new JTextField(otherServices.getWebpage().getHref());
            otherServicesWebpageTfs.add(otherServicesWebpageTf);
            builder.add(otherServicesWebpageTf,                                            cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.webpageTitle"), cc.xy(5, rowNb));
            JTextField otherServicesWebpageTitleTf = new JTextField(otherServices.getWebpage().getContent());
            otherServicesWebpageTitleTfs.add(otherServicesWebpageTitleTf);
            builder.add(otherServicesWebpageTitleTf,                                            cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addOtherServicesBtn = new ButtonEag(labels.getString("eag2012.addOtherServicesButton"));
        builder.add(addOtherServicesBtn, cc.xy (1, rowNb));
//        addOtherServicesBtn.addActionListener(new AddOtherServicesBtnAction());
        setNextRow();


        JButton exitBtn = new ButtonEag(labels.getString("eag2012.exitButton"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        JButton previousTabBtn = new ButtonEag(labels.getString("eag2012.previousTabButton"));
        builder.add(previousTabBtn, cc.xy (3, rowNb));
        previousTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, false));

        JButton nextTabBtn = new ButtonEag(labels.getString("eag2012.nextTabButton"));
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
                    reloadTabbedPanel(new EagDescriptionPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 4);
                    tabbedPane.setEnabledAt(4, true);
                    tabbedPane.setEnabledAt(3, false);
                } else {
                    reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 2);
                    tabbedPane.setEnabledAt(2, true);
                    tabbedPane.setEnabledAt(3, false);
                }
            } catch (Eag2012FormException e) {
                reloadTabbedPanel(new EagAccessAndServicesPanel(eag, tabbedPane, eag2012Frame, model).buildEditorPanel(errors), 3);
            }
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

            //todo here

            if(!errors.isEmpty()) {
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}
