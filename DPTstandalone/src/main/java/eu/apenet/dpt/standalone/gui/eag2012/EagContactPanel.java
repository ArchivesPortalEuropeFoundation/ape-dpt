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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithLanguage;
import eu.apenet.dpt.standalone.gui.eacCpf.EacCpfIdentityPanel;
import eu.apenet.dpt.standalone.gui.eacCpf.EacCpfIdentityPanel.AddIsoText;
import eu.apenet.dpt.standalone.gui.eacCpf.SwingStructures.TextFieldsWithCheckBoxForDates;
import eu.apenet.dpt.standalone.gui.eag2012.SwingStructures.LocationType;
import eu.apenet.dpt.utils.eag2012.Country;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Email;
import eu.apenet.dpt.utils.eag2012.Fax;
import eu.apenet.dpt.utils.eag2012.Location;
import eu.apenet.dpt.utils.eag2012.MunicipalityPostalcode;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.RepositoryName;
import eu.apenet.dpt.utils.eag2012.RepositoryRole;
import eu.apenet.dpt.utils.eag2012.Services;
import eu.apenet.dpt.utils.eag2012.Street;
import eu.apenet.dpt.utils.eag2012.Telephone;
import eu.apenet.dpt.utils.eag2012.Webpage;

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
    private List<JTextField> faxTfs; 
    private List<JTextField> telephoneTfs;
    private List<JTextField> emailTfs;
    private List<JTextField> emailTitleTfs;
    private List<JTextField> webpageTfs;
    private List<JTextField> webpageTitleTfs;
    
    private boolean isNew;

    private JComboBox continentCombo = new JComboBox(continents);

    //constants for the coordinates
	protected static final String LATITUDE = "latitude";
	protected static final String LONGITUDE = "longitude";
	
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
            JButton addNewRepositoryNameBtn = new ButtonTab(labels.getString("eag2012.contact.addNameOfRepository"));
            addNewRepositoryNameBtn.addActionListener(new AddRepositoryNameAction(eag, tabbedPane, model));
            builder.add(addNewRepositoryNameBtn, cc.xy(3, rowNb));
            setNextRow();
        }

        //visitors address and postal address
        boolean hasMinimumOnePostalAddress = false;
        boolean hasMinimumOneVisitorAddress = false;
        locationFields = new ArrayList<LocationType>(repository.getLocation().size());
        
        //loop to load Visitors address
        for(Location location : repository.getLocation()) 
        {
            if(location.getLocalType()!= null && location.getLocalType().equals("visitors address")) 
            {
                LocationType locationType = new LocationType(location);
                locationFields.add(locationType);
                if(hasMinimumOneVisitorAddress){
                    locationType.getLatitudeTf().setEnabled(false);
                    locationType.getLongitudeTf().setEnabled(false);
                }
                builder.addSeparator(labels.getString("eag2012.commons.visitorsAddress"), cc.xyw(1, rowNb, 7));
                hasMinimumOneVisitorAddress = true;
	            setNextRow();

	            String mandatoryStar = "*";
	
	            builder.addLabel(labels.getString("eag2012.commons.street") + mandatoryStar, cc.xy (1, rowNb));
	            builder.add(locationType.getStreetTf().getTextField(), cc.xy (3, rowNb));
	            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
	            builder.add(locationType.getStreetTf().getLanguageBox(), cc.xy (7, rowNb));
	            if(errors.contains("streetTf") && StringUtils.isEmpty(locationType.getStreetTfValue())) 
	            {
	                setNextRow();
	                builder.add(createErrorLabel(labels.getString("eag2012.errors.street")), cc.xy (1, rowNb));
	            }
	            setNextRow();
	
	            builder.addLabel(labels.getString("eag2012.commons.cityTownWithPostalcode") + mandatoryStar, cc.xy (1, rowNb));
	            builder.add(locationType.getCityTf().getTextField(), cc.xy (3, rowNb));
	            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
	            builder.add(locationType.getCityTf().getLanguageBox(), cc.xy (7, rowNb));
	            if(errors.contains("cityTf") && StringUtils.isEmpty(locationType.getCityTfValue())) 
	            {
	                setNextRow();
	                builder.add(createErrorLabel(labels.getString("eag2012.errors.city")), cc.xy (1, rowNb));
	            }
	            setNextRow();
	
	            builder.addLabel(labels.getString("eag2012.contact.district"),cc.xy (1, rowNb));
	            builder.add(locationType.getDistrictTf().getTextField(), cc.xy (3, rowNb));
	            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
	            builder.add(locationType.getDistrictTf().getLanguageBox(), cc.xy (7, rowNb));
	            setNextRow();
	
	            builder.addLabel(labels.getString("eag2012.contact.countyLocalAuthority"), cc.xy (1, rowNb));
	            builder.add(locationType.getCountyTf().getTextField(), cc.xy (3, rowNb));
	            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
	            builder.add(locationType.getCountyTf().getLanguageBox(), cc.xy (7, rowNb));
	            setNextRow();
	
	            builder.addLabel(labels.getString("eag2012.contact.region"), cc.xy (1, rowNb));
	            builder.add(locationType.getRegionTf().getTextField(), cc.xy (3, rowNb));
	            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
	            builder.add(locationType.getRegionTf().getLanguageBox(), cc.xy (7, rowNb));
	            setNextRow();
	
	            builder.addLabel(labels.getString("eag2012.commons.country") + "*", cc.xy (1, rowNb));
	            builder.add(locationType.getCountryTf().getTextField(), cc.xy (3, rowNb));
	            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
	            builder.add(locationType.getCountryTf().getLanguageBox(), cc.xy (7, rowNb));
	            if(errors.contains("countryTf") && StringUtils.isEmpty(locationType.getCountryTfValue())) 
	            {
	                setNextRow();
	                builder.add(createErrorLabel(labels.getString("eag2012.errors.country")), cc.xy (1, rowNb));
	            }
	            setNextRow();
	
	            builder.addLabel(labels.getString("eag2012.commons.latitude"), cc.xy (1, rowNb));
	            locationType.getLatitudeTf().addFocusListener(new UpdateCoordsText(locationType, EagContactPanel.LATITUDE));
                builder.add(locationType.getLatitudeTf(), cc.xy (3, rowNb));
                builder.addLabel(labels.getString("eag2012.commons.longitude"), cc.xy(5, rowNb));
	            locationType.getLongitudeTf().addFocusListener(new UpdateCoordsText(locationType, EagContactPanel.LONGITUDE));
                builder.add(locationType.getLongitudeTf(), cc.xy (7, rowNb));
                setNextRow();
            }// end if visitors address
        }//end loop to load Visitors address

        //add visitors address button
        if(hasMinimumOneVisitorAddress) 
        {
            JButton addNewVisitorTranslationAddressBtn = new ButtonTab(labels.getString("eag2012.commons.addVisitorTranslationAddress"), true);
            addNewVisitorTranslationAddressBtn.addActionListener(new AddAddressAction(eag, tabbedPane, model, false));
            builder.add(addNewVisitorTranslationAddressBtn, cc.xy(3, rowNb));
            setNextRow();
        } 
        else 
        {
            JButton addNewVisitorAddressBtn = new ButtonTab(labels.getString("eag2012.commons.addVisitorAddress"));
            addNewVisitorAddressBtn.addActionListener(new AddAddressAction(eag, tabbedPane, model, false));
            builder.add(addNewVisitorAddressBtn, cc.xy(3, rowNb));
            setNextRow();
        }
    	//end add visitors address button

        // loop to load Postal address
        for(Location location : repository.getLocation()) 
        {
        	if (location.getLocalType()!= null && location.getLocalType().equals("postal address")) 
        	{     
                LocationType locationType = new LocationType(location);
                locationFields.add(locationType);
                builder.addSeparator(labels.getString("eag2012.commons.postalAddress"), cc.xyw(1, rowNb, 7));
                hasMinimumOnePostalAddress = true;
	            setNextRow();

	            String mandatoryStar = "";
	
	            builder.addLabel(labels.getString("eag2012.commons.street") + mandatoryStar, cc.xy (1, rowNb));
	            builder.add(locationType.getStreetTf().getTextField(), cc.xy (3, rowNb));
	            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
	            builder.add(locationType.getStreetTf().getLanguageBox(), cc.xy (7, rowNb));
	            if(errors.contains("streetTf") && StringUtils.isEmpty(locationType.getStreetTfValue())) 
	            {
	                setNextRow();
	                builder.add(createErrorLabel(labels.getString("eag2012.errors.street")), cc.xy (1, rowNb));
	            }
	            setNextRow();
	
	            builder.addLabel(labels.getString("eag2012.commons.cityTownWithPostalcode") + mandatoryStar, cc.xy (1, rowNb));
	            builder.add(locationType.getCityTf().getTextField(), cc.xy (3, rowNb));
	            builder.addLabel(labels.getString("eag2012.commons.language"), cc.xy (5, rowNb));
	            builder.add(locationType.getCityTf().getLanguageBox(), cc.xy (7, rowNb));
	            if(errors.contains("cityTf") && StringUtils.isEmpty(locationType.getCityTfValue())) 
	            {
	                setNextRow();
	                builder.add(createErrorLabel(labels.getString("eag2012.errors.city")), cc.xy (1, rowNb));
	            }
	            setNextRow();
        	}//end if postal address
        }// end loop to load Postal address
        
        //add postal address button
        if(hasMinimumOnePostalAddress) 
        {
            JButton addNewPostalTranslationAddressBtn = new ButtonTab(labels.getString("eag2012.commons.addPostalTranslationAddress"), true);
            addNewPostalTranslationAddressBtn.addActionListener(new AddAddressAction(eag, tabbedPane, model, true));
            builder.add(addNewPostalTranslationAddressBtn, cc.xy(3, rowNb));
            setNextRow();
        } 
        else 
        {
            JButton addNewPostalAddressBtn = new ButtonTab(labels.getString("eag2012.commons.addPostalAddress"));
            addNewPostalAddressBtn.addActionListener(new AddAddressAction(eag, tabbedPane, model, true));
            builder.add(addNewPostalAddressBtn, cc.xy(3, rowNb));
            setNextRow();
        }
        //end add postal address button

        builder.addLabel(labels.getString("eag2012.commons.continent") + "*", cc.xy (1, rowNb));
        if(Arrays.asList(continents).contains(repository.getGeogarea().getValue())){
            continentCombo.setSelectedItem(repository.getGeogarea().getValue());
        } else {
            continentCombo.setSelectedItem("Europe");
        }
        builder.add(continentCombo, cc.xy (3, rowNb));
        setNextRow();

        //getTelephone()
        builder.addLabel(labels.getString("eag2012.commons.telephone"), cc.xy(1, rowNb));
        int i = 0;
        telephoneTfs = new ArrayList<JTextField>(repository.getTelephone().size());
        for(Telephone telephone : repository.getTelephone()) {
            JTextField telephoneTf = new JTextField(telephone.getContent());
            telephoneTfs.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            if(i++ == 0) {
            	JButton addTelephoneBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
                addTelephoneBtn.addActionListener(new AddTelephoneAction(eag, tabbedPane, model));
                builder.add(addTelephoneBtn, cc.xy(5, rowNb));            
            }
            setNextRow();
        }
        if(repository.getTelephone().size() == 0) {
            JTextField telephoneTf = new JTextField();
            telephoneTfs.add(telephoneTf);
            builder.add(telephoneTf, cc.xy (3, rowNb));
            JButton addTelephoneBtn = new ButtonTab(labels.getString("eag2012.contact.addFurtherTelephoneNumbers"));
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
                JButton addFaxBtn = new ButtonTab(labels.getString("eag2012.contact.addFaxNumbers"));
                addFaxBtn.addActionListener(new AddFaxAction(eag, tabbedPane, model));
                builder.add(addFaxBtn, cc.xy(5, rowNb));
            }
            setNextRow();
        }
        if(repository.getFax().size() == 0) {
            JTextField faxTf = new JTextField();
            faxTfs.add(faxTf);
            builder.add(faxTf, cc.xy (3, rowNb));
            JButton addFaxBtn = new ButtonTab(labels.getString("eag2012.contact.addFaxNumbers"));
            addFaxBtn.addActionListener(new AddFaxAction(eag, tabbedPane, model));
            builder.add(addFaxBtn, cc.xy(5, rowNb));
            setNextRow();
        }

        //getEmail()
        emailTfs = new ArrayList<JTextField>(repository.getEmail().size());
        emailTitleTfs = new ArrayList<JTextField>(repository.getEmail().size());
        if(repository.getEmail().size() == 0)
        	repository.getEmail().add(new Email());
        for(Email email : repository.getEmail()) {
            JTextField emailTf = new JTextField(email.getHref());
            JTextField emailTitleTf = new JTextField(email.getContent());
            emailTfs.add(emailTf);
            emailTitleTfs.add(emailTitleTf);
            builder.addLabel(labels.getString("eag2012.commons.email"),cc.xy (1, rowNb));
            builder.add(emailTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"),cc.xy (5, rowNb));
            builder.add(emailTitleTf,cc.xy (7, rowNb));
            setNextRow();
        }
        JButton addEmailBtn = new ButtonTab(labels.getString("eag2012.commons.addEmail"));
        addEmailBtn.addActionListener(new AddEmailAction(eag, tabbedPane, model));
        builder.add(addEmailBtn, cc.xy(1, rowNb));
        setNextRow();

        //getWebpage()
        webpageTfs = new ArrayList<JTextField>(repository.getWebpage().size());
        webpageTitleTfs = new ArrayList<JTextField>(repository.getWebpage().size());
        if(repository.getWebpage().size() == 0)
        	repository.getWebpage().add(new Webpage());
        for(Webpage webpage : repository.getWebpage()) {
            JTextField webpageTf = new JTextField(webpage.getHref());
            JTextField webpageTitleTf = new JTextField(webpage.getContent());
            webpageTitleTfs.add(webpageTitleTf);
        	webpageTfs.add(webpageTf);
    		builder.addLabel(labels.getString("eag2012.commons.webpage"), cc.xy (1, rowNb));
            builder.add(webpageTf, cc.xy (3, rowNb));
            builder.addLabel(labels.getString("eag2012.commons.linkTitle"), cc.xy (5, rowNb));
            builder.add(webpageTitleTf, cc.xy (7, rowNb));
            setNextRow();
            if(errors.contains("webpageTfs")) {
                if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                    builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")), cc.xyw(1, rowNb, 3));
                    setNextRow();
                }
            } else if(StringUtils.isNotBlank(webpageTf.getText()) && !StringUtils.startsWithAny(webpageTf.getText(), webPrefixes)){
                builder.add(createErrorLabel(labels.getString("eag2012.errors.webpageProtocol")),cc.xyw(1, rowNb, 3));
                setNextRow();
            }
        }
        JButton addWebpageBtn = new ButtonTab(labels.getString("eag2012.commons.addWebpage"));
        addWebpageBtn.addActionListener(new AddWebpageAction(eag, tabbedPane, model));
        builder.add(addWebpageBtn, cc.xy(1, rowNb));
        setNextRow();

        builder.addSeparator("", cc.xyw(1, rowNb, 7));
        setNextRow();

        JButton exitBtn = new ButtonTab(labels.getString("eag2012.commons.exit"));
        builder.add(exitBtn, cc.xy (1, rowNb));
        exitBtn.addActionListener(new ExitBtnAction());

        if(repositoryNb == 0) {
            JButton previousTabBtn = new ButtonTab(labels.getString("eag2012.commons.previousTab"));
            builder.add(previousTabBtn, cc.xy (3, rowNb));
            previousTabBtn.addActionListener(new ChangeTabBtnAction(eag, tabbedPane, model, false));
        }

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
                super.updateJAXBObject(true);

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
                super.updateJAXBObject(true);
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
                super.updateJAXBObject(false);

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
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }

            int counter = locationFields.size();
            boolean empty = false;
            boolean hasErrors = false;
            
			for(int i=0; i<counter;i++)
			{
				if (locationFields.get(i).getLocalType().compareToIgnoreCase("postal address")==0)
				{
					 if((StringUtils.isEmpty(locationFields.get(i).getStreetTfValue()) && locationFields.get(i).getStreetTfValue().trim().isEmpty()) || 
						(StringUtils.isEmpty(locationFields.get(i).getCityTfValue()) && locationFields.get(i).getCityTfValue().trim().isEmpty()))
			        		empty = true;
				}
				else
				{
				 if((StringUtils.isEmpty(locationFields.get(i).getStreetTfValue()) && locationFields.get(i).getStreetTfValue().trim().isEmpty()) || 
					(StringUtils.isEmpty(locationFields.get(i).getCityTfValue()) && locationFields.get(i).getCityTfValue().trim().isEmpty()) ||
					(StringUtils.isEmpty(locationFields.get(i).getCountryTfValue()) && locationFields.get(i).getCountryTfValue().trim().isEmpty()))
			   			empty = true;
				}

			    if(locationFields.get(i).getErrors().size()>0)
			    	hasErrors=true;
			}
			
			if (empty || hasErrors)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.commons.pleaseFillDataAddress")); 
			else
			{
				Location location = new Location();
                if(isPostal) 
                    location.setLocalType("postal address");
                 else 
                    location.setLocalType("visitors address");
                
                location.setCountry(new Country());
                location.setStreet(new Street());
                location.setMunicipalityPostalcode(new MunicipalityPostalcode());
                
                LocationType lastVisitorAddr = null;
                int index = counter - 1;
                while(lastVisitorAddr == null && index >= 0){
                    if(locationFields.get(index).getLocalType().equals("visitors address"))
                        lastVisitorAddr = locationFields.get(index);
                    index--;
                }
                if(lastVisitorAddr != null){
                    location.setLatitude(lastVisitorAddr.getLatitudeTfValue());
                    location.setLongitude(lastVisitorAddr.getLongitudeTfValue());
                }
                
                eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getLocation().add(location);
                reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors),2);
			}
            //reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors),2);
        }
    }
    
    /***
     * update coordinates in all fields
     * @author fernando
     *
     */
	public class UpdateCoordsText implements FocusListener {
		private String coordsType;
		private LocationType locationType;

		/**
		 * Constructor.
		 *
		 * @param locationType the fields that contains visitors address with
		 * @param dateType to check if it is latitude or longitude
		 */
		public UpdateCoordsText(LocationType locationType, String dateType) {
			this.locationType = locationType;
			this.coordsType = dateType;
		}

		@Override
		public void focusGained(FocusEvent e) {
			// No action
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (coordsType.equalsIgnoreCase(EagContactPanel.LATITUDE)) 
			{
				String value = this.locationType.getLatitudeTfValue();
				for (LocationType locationField: locationFields) {
					locationField.getLatitudeTf().setText(value);
				}
			} 
			else if (coordsType.equalsIgnoreCase(EagContactPanel.LONGITUDE)) 
			{
				String value = this.locationType.getLongitudeTfValue();
				for (LocationType locationField: locationFields) {
					locationField.getLongitudeTf().setText(value);
				}
			}
		}
	}
    
    /***
     * Adds a new telephone in Contact tab
     * @author fernando
     *
     */
    public class AddTelephoneAction extends UpdateEagObject {
        AddTelephoneAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }

            List<Telephone> telephone= eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getTelephone();
			boolean empty = false;         
		    int pos = telephoneTfs.size(); 
		    for(int i=0; i<pos;i++){
		        if( telephoneTfs.get(i).getText()==null || telephoneTfs.get(i).getText().trim().compareTo("") == 0)
		        	empty = true;
		    }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorTelephone"));

			telephone.add(new Telephone());
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
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
            
            boolean empty = false;         
		    int pos = faxTfs.size(); 
            
            List <Fax> fax = eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getFax();
            for(int i=0; i<pos;i++){
		        if( faxTfs.get(i).getText()==null || faxTfs.get(i).getText().trim().compareTo("") == 0)
		        	empty = true;
		    }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorFax"));
            
            fax.add(new Fax());
            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
        }
    }

    /***
     * Adds a new email in Contact tab
     * @author fernando
     *
     */
    public class AddEmailAction extends UpdateEagObject {
        AddEmailAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
            }
   			
            List<Email> email= eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getEmail();
			boolean empty = false;         
            int pos = emailTfs.size(); 
            for(int i=0; i<pos;i++){
                if( (emailTfs.get(i).getText()==null || emailTfs.get(i).getText().trim().compareTo("") == 0)
                		&& (emailTitleTfs.get(i).getText().trim().compareTo("") == 0))
                	empty = true;
            }
			if (empty)
				JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.errorEmail"));
			
			email.add(new Email());
            reloadTabbedPanel(new EagContactPanel(eag, tabbedPane, mainTabbedPane, eag2012Frame, model, isNew, labels, repositoryNb).buildEditorPanel(errors), 2);
        }
    }
    
    /***
     * Adds a new Web page in Contact tab
     * @author fernando
     *
     */
    public class AddWebpageAction extends UpdateEagObject {
        AddWebpageAction(Eag eag, JTabbedPane tabbedPane, ProfileListModel model) {
            super(eag, tabbedPane, model);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                super.updateJAXBObject(false);
            } catch (Eag2012FormException e) {
//            	JOptionPane.showMessageDialog(eag2012Frame, e.getCause() + "\n" + e.toString());
            }
            
            List<Webpage> webpage= eag.getArchguide().getDesc().getRepositories().getRepository().get(repositoryNb).getWebpage();
			boolean empty = false;         
            int pos = webpageTfs.size(); 
            for(int i=0; i<pos;i++){
                if( (webpageTfs.get(i).getText()==null || webpageTfs.get(i).getText().trim().compareTo("") == 0)
                		&& (webpageTitleTfs.get(i).getText()==null || webpageTitleTfs.get(i).getText().trim().compareTo("") == 0))
                	empty = true;
            }
            if(!errors.contains("webpageTfs")){
            	if (empty)
            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
            	
            }
            else{
            	if (empty)
            		JOptionPane.showMessageDialog(eag2012Frame, labels.getString("eag2012.errors.webpage"));
            }
            webpage.add(new Webpage());
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
                super.updateJAXBObject(false);
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
        protected void updateJAXBObject(boolean save) throws Eag2012FormException {
            errors = new ArrayList<String>();

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
            }
           
            
            
            if(repository.getServices() == null)
                repository.setServices(new Services());

            //Telephone 
            repository.getTelephone().clear();
            for(JTextField field : telephoneTfs) {
                if(StringUtils.isNotEmpty(field.getText().trim())) {
                    Telephone telephone = new Telephone();
                    telephone.setContent(field.getText());
                    repository.getTelephone().add(telephone);
                }
            }

            //email
            repository.getEmail().clear();
            for(int i = 0; i < emailTfs.size(); i++) {
                JTextField field = emailTfs.get(i);
                JTextField fieldTitle = emailTitleTfs.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim())|| StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Email email = new Email();
                    email.setHref(field.getText());
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                        email.setContent(fieldTitle.getText());
//                    else
//                        email.setContent(field.getText());
                    repository.getEmail().add(email);
                }
            }
            
            //web page
            repository.getWebpage().clear();
            for(int i = 0; i < webpageTfs.size(); i++) {
                JTextField field = webpageTfs.get(i);
                JTextField fieldTitle = webpageTitleTfs.get(i);
                if(StringUtils.isNotEmpty(field.getText().trim()) || StringUtils.isNotEmpty(fieldTitle.getText().trim())) {
                    Webpage webpage = new Webpage();
                    webpage.setHref(field.getText());
                    if(StringUtils.isNotEmpty(field.getText().trim())){
	                    if(!StringUtils.startsWithAny(field.getText(), webPrefixes))
	                        errors.add("webpageTfs");
                    }
                    if(StringUtils.isNotEmpty(fieldTitle.getText()))
                    	webpage.setContent(fieldTitle.getText());
//                    else
//                    	webpage.setContent(field.getText());
                    repository.getWebpage().add(webpage);
                }
            }
           
            repository.getFax().clear();
            for(JTextField field : faxTfs) {
                if(StringUtils.isNotEmpty(field.getText())) {
                    Fax fax = new Fax();
                    fax.setContent(field.getText());
                    repository.getFax().add(fax);
                }
            }   

            if(!errors.isEmpty()) {
//            	String strOut ="";
//            	
//             	if (errors.contains("webpageTfs"))
//            		strOut+= labels.getString("eag2012.errors.webpageProtocol")+ "\n";
//
//        		JOptionPane.showMessageDialog(eag2012Frame, strOut);
                throw new Eag2012FormException("Errors in validation of EAG 2012");
            }
        }
    }
}