package eu.apenet.dpt.standalone.gui.eaccpf;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.dpt.standalone.gui.DataPreparationToolGUI;
import eu.apenet.dpt.standalone.gui.ProfileListModel;
import eu.apenet.dpt.standalone.gui.commons.TextChanger;
import eu.apenet.dpt.standalone.gui.commons.swingstructures.CommonsPropertiesPanels;
import eu.apenet.dpt.standalone.gui.eaccpf.swingstructures.TextFieldWithComboBoxEacCpf;
import eu.apenet.dpt.utils.eaccpf.Abbreviation;
import eu.apenet.dpt.utils.eaccpf.AddressLine;
import eu.apenet.dpt.utils.eaccpf.Agent;
import eu.apenet.dpt.utils.eaccpf.AgentType;
import eu.apenet.dpt.utils.eaccpf.Citation;
import eu.apenet.dpt.utils.eaccpf.Control;
import eu.apenet.dpt.utils.eaccpf.ConventionDeclaration;
import eu.apenet.dpt.utils.eaccpf.CpfRelation;
import eu.apenet.dpt.utils.eaccpf.Date;
import eu.apenet.dpt.utils.eaccpf.DateRange;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.EntityId;
import eu.apenet.dpt.utils.eaccpf.EventDateTime;
import eu.apenet.dpt.utils.eaccpf.EventDescription;
import eu.apenet.dpt.utils.eaccpf.EventType;
import eu.apenet.dpt.utils.eaccpf.ExistDates;
import eu.apenet.dpt.utils.eaccpf.Function;
import eu.apenet.dpt.utils.eaccpf.FunctionRelation;
import eu.apenet.dpt.utils.eaccpf.Functions;
import eu.apenet.dpt.utils.eaccpf.Identity.NameEntry;
import eu.apenet.dpt.utils.eaccpf.Language;
import eu.apenet.dpt.utils.eaccpf.LanguageDeclaration;
import eu.apenet.dpt.utils.eaccpf.MaintenanceEvent;
import eu.apenet.dpt.utils.eaccpf.MaintenanceStatus;
import eu.apenet.dpt.utils.eaccpf.NameEntryParallel;
import eu.apenet.dpt.utils.eaccpf.Occupation;
import eu.apenet.dpt.utils.eaccpf.Occupations;
import eu.apenet.dpt.utils.eaccpf.OtherRecordId;
import eu.apenet.dpt.utils.eaccpf.Place;
import eu.apenet.dpt.utils.eaccpf.PlaceEntry;
import eu.apenet.dpt.utils.eaccpf.Places;
import eu.apenet.dpt.utils.eaccpf.PublicationStatus;
import eu.apenet.dpt.utils.eaccpf.ResourceRelation;
import eu.apenet.dpt.utils.eaccpf.Script;
import eu.apenet.dpt.utils.eaccpf.SetComponent;
import eu.apenet.dpt.utils.util.XmlTypeEacCpf;

/**
 * The container of the different EacCpf Panels
 */
public abstract class EacCpfPanel extends CommonsPropertiesPanels {
	
    protected static final Logger LOG = Logger.getLogger(EacCpfPanel.class);

    private static final String CONVERTED_STRING = "Converted_apeEAC-CPF_version_";
    
	protected static final String MAINTENANCE_AGENT_HUMAN = "human";
	private static final String MAINTENANCE_EVENT_CREATED = "created";
	private static final String MAINTENANCE_EVENT_NEW = "new";
	private static final String MAINTENANCE_EVENT_REVISED = "revised";
	
	//Constant to the value publicationStatus
	private static final String PUBLICATIONSTATUS_VALUE = "approved";
    
    protected JTabbedPane tabbedPane;
    protected JTabbedPane mainTabbedPane;
    protected EacCpf eaccpf;
	protected JFrame eacCpfFrame;
    protected ProfileListModel model;
    protected ResourceBundle labels;
    protected XmlTypeEacCpf entityType;
    protected static String firstLanguage;
    protected static String firstScript;

  
    public EacCpfPanel(EacCpf eaccpf, JTabbedPane tabbedPane, JTabbedPane mainTabbedPane, JFrame eacCpfFrame, ProfileListModel model, ResourceBundle labels, XmlTypeEacCpf entityType, String firstLanguage, String firstScript) {
        this.eacCpfFrame = eacCpfFrame;
        this.eaccpf = eaccpf;
        this.tabbedPane = tabbedPane;
        this.mainTabbedPane = mainTabbedPane;
        this.model = model;
        this.labels = labels;
        this.entityType = entityType;
        EacCpfPanel.firstLanguage = firstLanguage;
        EacCpfPanel.firstScript = firstScript;
    }
    
    protected void closeFrame() {
    	this.eacCpfFrame.dispose();
    	this.eacCpfFrame.setVisible(false);
    }

    protected void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }
    
    protected static EacCpf cleanIncompleteData(EacCpf eac) {
    	if(eac!=null){
    		//1. Identities tab part
    		if(eac.getCpfDescription()!=null && eac.getCpfDescription().getIdentity()!=null && eac.getCpfDescription().getIdentity().getEntityId()!=null){
    			int idIndex = eac.getCpfDescription().getIdentity().getEntityId().size();
    			//keeps indexes equality
    			List<EntityId> targetsToBeRemoved = new ArrayList<EntityId>();
    			for(int i=0;i<idIndex;i++){
    				EntityId targetEntityId = eac.getCpfDescription().getIdentity().getEntityId().get(i);
    				if(StringUtils.isEmpty(targetEntityId.getContent()) || StringUtils.isEmpty(targetEntityId.getLocalType())){
    					targetsToBeRemoved.add(targetEntityId);
    				}
    			}
    			if(targetsToBeRemoved.size()>0){
    				eac.getCpfDescription().getIdentity().getEntityId().removeAll(targetsToBeRemoved);
    			}
    			//dates part for identity
    			if(eac.getCpfDescription().getDescription()!=null && eac.getCpfDescription().getDescription().getExistDates()!=null){
    				ExistDates existDates = eac.getCpfDescription().getDescription().getExistDates();
    				if(existDates.getDateRange()!=null){
    					if(checkDateRange(existDates.getDateRange())){
    						existDates.setDateRange(null);
    					}
    				}
    				if(existDates.getDateSet()!=null){
    					if(existDates.getDateSet().getDateOrDateRange()!=null){
    						existDates.getDateSet().getDateOrDateRange().removeAll(getListDateOrDateSetToBeDeleted(existDates.getDateSet().getDateOrDateRange()));
    					}
    				}
    			}
    			List<NameEntry> listNameEntries = getAllNameEntries(eac);
    			if(listNameEntries!=null && listNameEntries.size()>0){
    				for(NameEntry nameEntry:listNameEntries){
    					if(nameEntry.getUseDates()!=null){
    						if(nameEntry.getUseDates().getDateRange()!=null){
            					if(checkDateRange(nameEntry.getUseDates().getDateRange())){
            						nameEntry.getUseDates().setDateRange(null);
            					}
            				}
            				if(nameEntry.getUseDates().getDateSet()!=null){
            					if(nameEntry.getUseDates().getDateSet().getDateOrDateRange()!=null){
            						nameEntry.getUseDates().getDateSet().getDateOrDateRange().removeAll(getListDateOrDateSetToBeDeleted(nameEntry.getUseDates().getDateSet().getDateOrDateRange()));
            					}
            				}
    					}
    				}
    			}
    		}
    		//2. Descriptions tab part
    		if(eac.getCpfDescription()!=null && eac.getCpfDescription().getDescription()!=null && eac.getCpfDescription().getDescription().getPlacesOrLocalDescriptionsOrLegalStatuses()!=null){
    			List<Object> emptyTargets = new ArrayList<Object>();
    			for(Object target: eac.getCpfDescription().getDescription().getPlacesOrLocalDescriptionsOrLegalStatuses()){
    				if(target instanceof Places){
    					Places places = (Places)target;
    					if(places.getPlace()!=null){
    						List<Place> placeToBeDeleted = new ArrayList<Place>();
    						for(Place place: places.getPlace()){
    							boolean markToBeDeleted = false;
        						if(place.getPlaceEntry().size()>0){
        							if(StringUtils.isEmpty(place.getPlaceEntry().get(0).getContent())){
        								markToBeDeleted = true;
        							}else if(place.getAddress()!=null && place.getAddress().getAddressLine()!=null){
        								List<AddressLine> listToBeDeleted = new ArrayList<AddressLine>();
        								for(AddressLine addressLine:place.getAddress().getAddressLine()){
        									if(StringUtils.isEmpty(addressLine.getContent())/* || StringUtils.isEmpty(addressLine.getLocalType())*/){
        										listToBeDeleted.add(addressLine);
        									}
        								}
        								place.getAddress().getAddressLine().removeAll(listToBeDeleted);
        								if(place.getAddress().getAddressLine().isEmpty()){
        									place.setAddress(null);
        								}
        							}
        						}else{
        							markToBeDeleted = true;
        						}
        						if(!markToBeDeleted){
        							if(checkDateRange(place.getDateRange())){
        								place.setDateRange(null);
        							}
        							if(place.getDateSet()!=null){
        								place.getDateSet().getDateOrDateRange().removeAll(getListDateOrDateSetToBeDeleted(place.getDateSet().getDateOrDateRange()));
        							}
        						}
        						if(markToBeDeleted){
        							placeToBeDeleted.add(place);
        						}
        					}
    						places.getPlace().removeAll(placeToBeDeleted);
    						if(places.getPlace()!=null && places.getPlace().isEmpty()){
    							emptyTargets.add(target);
    						}
    					}
    				}else if(target instanceof Functions){
    					Functions functions = (Functions)target;
    					if(functions.getFunction()!=null){
    						List<Function> functionsToBeDeleted = new ArrayList<Function>();
    						for(Function function:functions.getFunction()){
    							boolean markToBeDeleted = false;
    							if(function.getTerm()!=null){
    								if(StringUtils.isEmpty(function.getTerm().getContent())){
    									markToBeDeleted = true;
    								}else if(function.getPlaceEntry()!=null){
    									List<PlaceEntry> listToBeDeleted = new ArrayList<PlaceEntry>();
    									for(PlaceEntry placeEntry : function.getPlaceEntry()){
    										if(StringUtils.isEmpty(placeEntry.getContent())){
    											listToBeDeleted.add(placeEntry);
    										}
    									}
    									function.getPlaceEntry().removeAll(listToBeDeleted);
    								}
    							}
    							if(!markToBeDeleted){
        							if(checkDateRange(function.getDateRange())){
        								function.setDateRange(null);
        							}
        							if(function.getDateSet()!=null){
        								function.getDateSet().getDateOrDateRange().removeAll(getListDateOrDateSetToBeDeleted(function.getDateSet().getDateOrDateRange()));
        							}
        						}
    							if(markToBeDeleted){
    								functionsToBeDeleted.add(function);
    							}
    						}
    						functions.getFunction().removeAll(functionsToBeDeleted);
    						if(functions.getFunction()!=null && functions.getFunction().isEmpty()){
    							emptyTargets.add(target);
    						}
    					}
    				}else if(target instanceof Occupations){
    					Occupations occupations = (Occupations)target;
    					if(occupations.getOccupation()!=null){
    						List<Occupation> occupationsToBeDeleted = new ArrayList<Occupation>();
    						for(Occupation occupation:occupations.getOccupation()){
    							boolean markToBeDeleted = false;
    							if(occupation.getTerm()!=null){
    								if(StringUtils.isEmpty(occupation.getTerm().getContent())){
    									markToBeDeleted = true;
    								}else if(occupation.getPlaceEntry()!=null){
    									List<PlaceEntry> listToBeDeleted = new ArrayList<PlaceEntry>();
    									for(PlaceEntry placeEntry : occupation.getPlaceEntry()){
    										if(StringUtils.isEmpty(placeEntry.getContent())){
    											listToBeDeleted.add(placeEntry);
    										}
    									}
    									occupation.getPlaceEntry().removeAll(listToBeDeleted);
    								}
    							}
    							if(!markToBeDeleted){
        							if(checkDateRange(occupation.getDateRange())){
        								occupation.setDateRange(null);
        							}
        							if(occupation.getDateSet()!=null){
        								occupation.getDateSet().getDateOrDateRange().removeAll(getListDateOrDateSetToBeDeleted(occupation.getDateSet().getDateOrDateRange()));
        							}
        						}
    							if(markToBeDeleted){
    								occupationsToBeDeleted.add(occupation);
    							}
    						}
    						occupations.getOccupation().removeAll(occupationsToBeDeleted);
    						if(occupations.getOccupation()!=null && occupations.getOccupation().isEmpty()){
    							emptyTargets.add(target);
    						}
    					}
    				}
    			}
    			eac.getCpfDescription().getDescription().getPlacesOrLocalDescriptionsOrLegalStatuses().removeAll(emptyTargets);
    		}
    		//3. Relations tab part
    		if(eac.getCpfDescription()!=null){
    			if(eac.getCpfDescription().getRelations()!=null && eac.getCpfDescription().getRelations()!=null){
    				if(eac.getCpfDescription().getRelations().getCpfRelation()!=null){
    					List<CpfRelation> cpfRelationsToBeDeleted = new ArrayList<CpfRelation>();
    					for(CpfRelation cpfRelation: eac.getCpfDescription().getRelations().getCpfRelation()){
        					boolean removeCpfRelation = false;
        					if(cpfRelation.getRelationEntry()!=null && cpfRelation.getRelationEntry().size()>0){
        						if(StringUtils.isEmpty(cpfRelation.getRelationEntry().get(0).getContent())){
        							removeCpfRelation = true;
        						}else{
        							/*List<RelationEntry> listToBeDeleted = new ArrayList<RelationEntry>();
        							for(RelationEntry relationEntry : cpfRelation.getRelationEntry()){
        								boolean delete = false;
                						if(relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME) || relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE)){
            								if(StringUtils.isEmpty(relationEntry.getContent())){
            									delete = true;
            								}
            							}
            							if(delete){
            								listToBeDeleted.add(relationEntry);
            							}
                					}
        							cpfRelation.getRelationEntry().removeAll(listToBeDeleted);*/
        						}
        						if(removeCpfRelation){
        							cpfRelationsToBeDeleted.add(cpfRelation);
        						}
        					}
        				}
    					eac.getCpfDescription().getRelations().getCpfRelation().removeAll(cpfRelationsToBeDeleted);
    				}
    				if(eac.getCpfDescription().getRelations().getResourceRelation()!=null){
    					List<ResourceRelation> resourceRelationsToBeDeleted = new ArrayList<ResourceRelation>();
    					for(ResourceRelation resourceRelation:eac.getCpfDescription().getRelations().getResourceRelation()){
    						boolean removeCpfRelation = false;
        					if(resourceRelation.getRelationEntry()!=null && resourceRelation.getRelationEntry().size()>0){
        						if(StringUtils.isEmpty(resourceRelation.getRelationEntry().get(0).getContent())){
        							removeCpfRelation = true;
        						}else{
        							/*List<RelationEntry> listToBeDeleted = new ArrayList<RelationEntry>();
        							for(RelationEntry relationEntry : resourceRelation.getRelationEntry()){
        								boolean delete = false;
                						if(relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME) || relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE)){
            								if(StringUtils.isEmpty(relationEntry.getContent())){
            									delete = true;
            								}
            							}
            							if(delete){
            								listToBeDeleted.add(relationEntry);
            							}
                					}
        							resourceRelation.getRelationEntry().removeAll(listToBeDeleted);*/
        						}
        						if(removeCpfRelation){
        							resourceRelationsToBeDeleted.add(resourceRelation);
        						}
        					}
    					}
    					eac.getCpfDescription().getRelations().getResourceRelation().removeAll(resourceRelationsToBeDeleted);
    				}
    				if(eac.getCpfDescription().getRelations().getFunctionRelation()!=null){
    					List<FunctionRelation> functionRelationsToBeDeleted = new ArrayList<FunctionRelation>();
    					for(FunctionRelation functionRelation:eac.getCpfDescription().getRelations().getFunctionRelation()){
    						boolean removeCpfRelation = false;
        					if(functionRelation.getRelationEntry()!=null && functionRelation.getRelationEntry().size()>0){
        						if(StringUtils.isEmpty(functionRelation.getRelationEntry().get(0).getContent())){
        							removeCpfRelation = true;
        						}else{
        							/*List<RelationEntry> listToBeDeleted = new ArrayList<RelationEntry>();
        							for(RelationEntry relationEntry : functionRelation.getRelationEntry()){
        								boolean delete = false;
                						if(relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME) || relationEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE)){
            								if(StringUtils.isEmpty(relationEntry.getContent())){
            									delete = true;
            								}
            							}
            							if(delete){
            								listToBeDeleted.add(relationEntry);
            							}
                					}
        							functionRelation.getRelationEntry().removeAll(listToBeDeleted);*/
        						}
        						if(removeCpfRelation){
        							functionRelationsToBeDeleted.add(functionRelation);
        						}
        					}
    					}
    					eac.getCpfDescription().getRelations().getFunctionRelation().removeAll(functionRelationsToBeDeleted);
    				}
    				if(eac.getCpfDescription().getRelations().getCpfRelation().isEmpty()
    						&& eac.getCpfDescription().getRelations().getResourceRelation().isEmpty()
    						&& eac.getCpfDescription().getRelations().getFunctionRelation().isEmpty()){
    					eac.getCpfDescription().setRelations(null);
    				}
    			}
    			if(eac.getCpfDescription().getAlternativeSet()!=null && eac.getCpfDescription().getAlternativeSet().getSetComponent()!=null){
    				List<SetComponent> setComponentsToBeDeleted = new ArrayList<SetComponent>();
        			for(SetComponent setComponent: eac.getCpfDescription().getAlternativeSet().getSetComponent()){
        				boolean removeSetComponent = false;
        				if(setComponent.getComponentEntry()!=null && setComponent.getComponentEntry().size()>0){
        					if(StringUtils.isEmpty(setComponent.getComponentEntry().get(0).getContent())){
        						removeSetComponent = true;
        					}else{
        						/*List<ComponentEntry> listToBeDeleted = new ArrayList<ComponentEntry>();
        						for(ComponentEntry componentEntry:setComponent.getComponentEntry()){
        							boolean delete = false;
        							if(componentEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYNAME) || componentEntry.getLocalType().equals(EacCpfRelationsPanel.LOCALTYPE_AGENCYCODE)){
        								if(StringUtils.isEmpty(componentEntry.getContent())){
        									delete = true;
        								}
        							}
        							if(delete){
        								listToBeDeleted.add(componentEntry);
        							}
        						}
        						setComponent.getComponentEntry().removeAll(listToBeDeleted);*/
        					}
        				}
        				if(removeSetComponent){
        					setComponentsToBeDeleted.add(setComponent);
        				}
        			}
        			eac.getCpfDescription().getAlternativeSet().getSetComponent().removeAll(setComponentsToBeDeleted);
        			if(eac.getCpfDescription().getAlternativeSet().getSetComponent().isEmpty()){
        				eac.getCpfDescription().setAlternativeSet(null);
        			}
        		}
    		}
    		//4. Controls tab part
    		if(eac.getControl()!=null && eac.getControl().getOtherRecordId()!=null){
    			List<OtherRecordId> listToBeDeleted = new ArrayList<OtherRecordId>();
    			for(OtherRecordId otherRecordId:eac.getControl().getOtherRecordId()){
    				if(StringUtils.isEmpty(otherRecordId.getContent())/* || StringUtils.isEmpty(otherRecordId.getLocalType())*/){
    					listToBeDeleted.add(otherRecordId);
    				}
    			}
    			eac.getControl().getOtherRecordId().removeAll(listToBeDeleted);
    		}
    	}
		return eac;
	}
    	
    protected static EacCpf updatesControl(EacCpf eac){
    	if(eac.getControl().getPublicationStatus() == null){
			PublicationStatus publicationStatus = new PublicationStatus();
			publicationStatus.setValue(PUBLICATIONSTATUS_VALUE);
			eac.getControl().setPublicationStatus(publicationStatus);
		}
		// Update maintenance status.
		updateMaintenanceStatus(eac.getControl());
		// Update language declaration.
		updateLanguageDeclaration(eac.getControl());
		// Update convention declaration.
		updateConventionDeclaration(eac.getControl());
		// Update maintenance history.
		updateMaintenanceHistory(eac.getControl());
		return eac;
    }

	private static Collection<?> getListDateOrDateSetToBeDeleted(List<Object> dateOrDateRangeList) {
		List<Object> datesToBeDeleted = new ArrayList<Object>(); 
		for(Object dateOrDateRange : dateOrDateRangeList){
			if(dateOrDateRange instanceof Date){
				Date date = (Date)dateOrDateRange;
				if(StringUtils.isEmpty(date.getContent())){
					datesToBeDeleted.add(dateOrDateRange);
				}
			}
			if(dateOrDateRange instanceof DateRange){
				DateRange dateRange = (DateRange)dateOrDateRange;
				if(checkDateRange(dateRange)){
					datesToBeDeleted.add(dateOrDateRange);
				}
			}
		}
		return datesToBeDeleted;
	}

	private static boolean checkDateRange(DateRange dateRange) {
		return (dateRange==null || dateRange.getFromDate()==null || dateRange.getToDate()==null ||
				(StringUtils.isEmpty(dateRange.getFromDate().getContent()) || StringUtils.isEmpty(dateRange.getToDate().getContent())) );
	}

	/**
	 * Method to update the maintenance status of the apeEAC-CPF file.
	 *
	 * @param control
	 * @return boolean value.
	 */
	private static boolean updateMaintenanceStatus(Control control) {
		boolean hasChanged = false;

		// Maintenance status.
		if (control.getMaintenanceStatus() == null) {
            MaintenanceStatus maintenanceStatus = new MaintenanceStatus();
            maintenanceStatus.setValue(MAINTENANCE_EVENT_NEW);
            control.setMaintenanceStatus(maintenanceStatus);
            hasChanged = true;
        } else {
            control.getMaintenanceStatus().setValue(MAINTENANCE_EVENT_REVISED);
            hasChanged = true;
        }

		return hasChanged;
	}

	/**
	 * Method to update the language declaration of the apeEAC-CPF file.
	 *
	 * @param control
	 * @return boolean value.
	 */
	private static boolean updateLanguageDeclaration(Control control) {
		boolean hasChanged = false;
		
		if (control.getLanguageDeclaration()==null
				|| (control.getLanguageDeclaration()!=null && control.getLanguageDeclaration().getLanguage()==null && control.getLanguageDeclaration().getScript()==null && control.getLanguageDeclaration().getDescriptiveNote()==null)){
	        // Language declaration.
	        if ((StringUtils.isNotEmpty(firstLanguage)
	        		&& !firstLanguage.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE))
	        		|| (StringUtils.isNotEmpty(firstScript)
        				&& !firstScript.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE))) {
	        	LanguageDeclaration languageDeclaration = new LanguageDeclaration();

	        	if (!firstLanguage.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
	        		Language language = new Language();
	        		language.setLanguageCode(firstLanguage);
	        		languageDeclaration.setLanguage(language);
	        	}

	        	if (!firstScript.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
	        		Script script = new Script();
	        		script.setScriptCode(firstScript);
	        		languageDeclaration.setScript(script);
	        	}
	        	control.setLanguageDeclaration(languageDeclaration);
	        	hasChanged = true;
	        }
		}
		return hasChanged;
	}

	/**
	 * Method to update the convention declaration of the apeEAC-CPF file.
	 *
	 * @param control
	 * @return boolean value.
	 */
	private static boolean updateConventionDeclaration(Control control) {
		boolean hasChanged = false;
		
		if (control.getConventionDeclaration().isEmpty()){
	        // Convention declaration items.
	        String[] conventionValues = {"ISO 639-2b", "ISO 3166-1", "ISO 8601", "ISO 15511", "ISO 15924"};
	        for (String conventionValue : conventionValues) {
	            ConventionDeclaration conventionDeclaration = new ConventionDeclaration();
	            Abbreviation abbreviation = new Abbreviation();

	            abbreviation.setValue(conventionValue);
	            conventionDeclaration.setAbbreviation(abbreviation);
	            conventionDeclaration.setCitation(new Citation());
	            control.getConventionDeclaration().add(conventionDeclaration);
	            hasChanged = true;
	        }
		}
		return hasChanged;
	}

	/**
	 * Method to update the maintenance history of the apeEAC-CPF file.
	 *
	 * @param control
	 * @return boolean value.
	 */
	private static boolean updateMaintenanceHistory(Control control) {
		boolean hasChanged = false;

		// Maintenance time.
		if (EacCpfFrame.getTimeMaintenance() == null) {
			EacCpfFrame.setTimeMaintenance(new java.util.Date());
		}

		// Maintenance event size.
		int sizeMaintenanceEvents = control.getMaintenanceHistory().getMaintenanceEvent().size();
		String languagePerson = "";

		if (sizeMaintenanceEvents > 0) {
			MaintenanceEvent maintenanceEvent = control.getMaintenanceHistory().getMaintenanceEvent().get(sizeMaintenanceEvents - 1);
			languagePerson = maintenanceEvent.getAgent().getLang();
		} else if (firstLanguage!=null && !firstLanguage.equals(TextFieldWithComboBoxEacCpf.DEFAULT_VALUE)) {
			languagePerson = firstLanguage;
		}

		MaintenanceEvent maintenanceEvent = TextChanger.getEacCpfMaintenanceEventSaved(EacCpfFrame.getTimeMaintenance(), control.getMaintenanceHistory().getMaintenanceEvent());
		if (maintenanceEvent == null) {
			maintenanceEvent = new MaintenanceEvent();
			maintenanceEvent.setAgent(new Agent());
			maintenanceEvent.setEventType(new EventType());
			if (sizeMaintenanceEvents == 0) {
				maintenanceEvent.getEventType().setValue(MAINTENANCE_EVENT_CREATED);
			} else {
				maintenanceEvent.getEventType().setValue(MAINTENANCE_EVENT_REVISED);
			}
			maintenanceEvent.setAgentType(new AgentType());
			maintenanceEvent.getAgentType().setValue(MAINTENANCE_AGENT_HUMAN);
			maintenanceEvent.setEventDateTime(new EventDateTime());
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat formatStandard = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            maintenanceEvent.getEventDateTime().setContent(format.format(EacCpfFrame.getTimeMaintenance()));
            maintenanceEvent.getEventDateTime().setStandardDateTime(formatStandard.format(EacCpfFrame.getTimeMaintenance()));
            EventDescription eventDescription = new EventDescription();
            eventDescription.setContent(CONVERTED_STRING+DataPreparationToolGUI.VERSION_NB);
            maintenanceEvent.setEventDescription(eventDescription);
		} else {
			control.getMaintenanceHistory().getMaintenanceEvent().remove(maintenanceEvent);
		}

		maintenanceEvent.getAgent().setContent(MAINTENANCE_AGENT_HUMAN);

		if (StringUtils.isNotEmpty(languagePerson)) {
			maintenanceEvent.getAgent().setLang(languagePerson);
		}

		control.getMaintenanceHistory().getMaintenanceEvent().add(maintenanceEvent);
		hasChanged = true;

		return hasChanged;
	}

	/**
	 * Method to remove duplicate whitespaces and trim string.
	 *
	 * @param str
	 * @return the string without whitespaces.
	 */
	protected static String trimStringValue(String str) {
		String result = str;

		if (result!= null && !result.isEmpty()) {

			String strWith = result.trim().replaceAll("[\\s+]", " ");
			StringBuilder sb = new StringBuilder();
			boolean space = false;
			for (int i = 0; i < strWith.length() ; i ++) {
				if (!space && strWith.charAt(i) == ' ') {
					sb.append(' ');
					space = true;
				} else if (strWith.charAt(i) != ' ') {
					sb.append(strWith.charAt(i));
					space = false;
				}
			}
			result = sb.toString();
		}

		return result;
	}
	
	/**
	 * Method to recover all the elements NameEntry in the object.
	 *
	 * @return the list of NameEntry.
	 */
	protected static List<NameEntry> getAllNameEntries(EacCpf eaccpf) {
		List<Object> nameEntryParallelOrNameEntryList = eaccpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry();
		List<NameEntry> nameEntries = new ArrayList<NameEntry>();

		for (Object object : nameEntryParallelOrNameEntryList) {
			if (object instanceof NameEntryParallel) {
				NameEntryParallel nameEntryParallel = (NameEntryParallel) object;
				for (Object nameEntry : nameEntryParallel.getContent()) {
					if (nameEntry instanceof NameEntry) {
						nameEntries.add((NameEntry) nameEntry);
					}
				}
			}

			if (object instanceof NameEntry) {
				nameEntries.add((NameEntry) object);
			}
		}

		return nameEntries;
	}
}