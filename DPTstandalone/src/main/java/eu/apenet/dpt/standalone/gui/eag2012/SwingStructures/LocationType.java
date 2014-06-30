package eu.apenet.dpt.standalone.gui.eag2012.SwingStructures;

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

import eu.apenet.dpt.standalone.gui.commons.swingstructures.TextFieldWithLanguage;
import eu.apenet.dpt.utils.eag2012.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 16/04/2013
 *
 * @author Yoann Moranville
 */
public class LocationType {
    private static final Logger LOG = Logger.getLogger(LocationType.class);
    private TextFieldWithLanguage streetTf;
    private TextFieldWithLanguage cityTf;
    private TextFieldWithLanguage districtTf;
    private TextFieldWithLanguage countyTf;
    private TextFieldWithLanguage regionTf;
    private TextFieldWithLanguage countryTf;
    private JTextField latitudeTf;
    private JTextField longitudeTf;
    private String localType;

    private List<String> errors;

    public LocationType(Location location) {
        
    	// street
    	if(location.getStreet() == null)
            location.setStreet(new Street());

    	if(StringUtils.isNotEmpty(location.getStreet().getContent()) && !location.getStreet().getContent().trim().isEmpty()) 
    		this.streetTf = new TextFieldWithLanguage(location.getStreet().getContent().trim(), location.getStreet().getLang());
    	 else 
    		this.streetTf = new TextFieldWithLanguage("", location.getStreet().getLang());
        
    	// city/town with postal code
        if(location.getMunicipalityPostalcode() == null)
            location.setMunicipalityPostalcode(new MunicipalityPostalcode());
        
        if(StringUtils.isNotEmpty(location.getMunicipalityPostalcode().getContent()) && !location.getMunicipalityPostalcode().getContent().trim().isEmpty()) 
        	this.cityTf = new TextFieldWithLanguage(location.getMunicipalityPostalcode().getContent().trim(), location.getMunicipalityPostalcode().getLang());
        else
        	this.cityTf = new TextFieldWithLanguage("", location.getMunicipalityPostalcode().getLang());

        // local entity
        if(location.getLocalentity() == null)
            location.setLocalentity(new Localentity());
        
        if(StringUtils.isNotEmpty(location.getLocalentity().getContent()) && !location.getLocalentity().getContent().trim().isEmpty())
        	this.districtTf = new TextFieldWithLanguage(location.getLocalentity().getContent().trim(), location.getLocalentity().getLang());
        else
        	this.districtTf = new TextFieldWithLanguage("", location.getLocalentity().getLang());
        
        // county
        if(location.getSecondem() == null)
            location.setSecondem(new Secondem());
     
        if(StringUtils.isNotEmpty(location.getSecondem().getContent()) && !location.getSecondem().getContent().trim().isEmpty())
        	this.countyTf = new TextFieldWithLanguage(location.getSecondem().getContent().trim(), location.getSecondem().getLang());
        else
        	this.countyTf = new TextFieldWithLanguage("", location.getSecondem().getLang());
       
        // district
        if(location.getFirstdem() == null)
            location.setFirstdem(new Firstdem());
        
        if(StringUtils.isNotEmpty(location.getFirstdem().getContent()) && !location.getFirstdem().getContent().trim().isEmpty())
        	this.regionTf = new TextFieldWithLanguage(location.getFirstdem().getContent().trim(), location.getFirstdem().getLang());
        else
        	this.regionTf = new TextFieldWithLanguage("", location.getFirstdem().getLang());
        
        // country
        if(location.getCountry() == null)
            location.setCountry(new Country());
        
        if(StringUtils.isNotEmpty(location.getCountry().getContent()) && !location.getCountry().getContent().trim().isEmpty())
        	this.countryTf = new TextFieldWithLanguage(location.getCountry().getContent().trim(), location.getCountry().getLang());
        else
        	this.countryTf = new TextFieldWithLanguage("", location.getCountry().getLang());
        
        // latitude
        if(StringUtils.isNotEmpty(location.getLatitude()) && !location.getLatitude().trim().isEmpty())
        	this.latitudeTf = new JTextField(location.getLatitude().trim());
        else
        	this.latitudeTf = new JTextField("");

        //longitude
        if(StringUtils.isNotEmpty(location.getLongitude()) && !location.getLongitude().trim().isEmpty())
        	this.longitudeTf = new JTextField(location.getLongitude().trim());
        else
        	this.longitudeTf = new JTextField("");
        
        //local type
        this.localType = location.getLocalType();
    }

    public TextFieldWithLanguage getStreetTf() {
        return streetTf;
    }
    public String getStreetTfValue() {
        return streetTf.getTextValue();
    }
    public String getStreetTfLanguage() {
        return streetTf.getLanguage();
    }

    public TextFieldWithLanguage getCityTf() {
        return cityTf;
    }
    public String getCityTfValue() {
        return cityTf.getTextValue();
    }
    public String getCityTfLanguage() {
        return cityTf.getLanguage();
    }

    public TextFieldWithLanguage getDistrictTf() {
        return districtTf;
    }
    public String getDistrictTfValue() {
        return districtTf.getTextValue();
    }
    public String getDistrictTfLanguage() {
        return districtTf.getLanguage();
    }

    public TextFieldWithLanguage getCountyTf() {
        return countyTf;
    }
    public String getCountyTfValue() {
        return countyTf.getTextValue();
    }
    public String getCountyTfLanguage() {
        return countyTf.getLanguage();
    }
    public TextFieldWithLanguage getRegionTf() {
        return regionTf;
    }
    public String getRegionTfValue() {
        return regionTf.getTextValue();
    }
    public String getRegionTfLanguage() {
        return regionTf.getLanguage();
    }

    public TextFieldWithLanguage getCountryTf() {
        return countryTf;
    }
    public String getCountryTfValue() {
        return countryTf.getTextValue();
    }
    public String getCountryTfLanguage() {
        return countryTf.getLanguage();
    }

    public JTextField getLatitudeTf() {
        return latitudeTf;
    }
    public void setLatitudeTf(JTextField textField) {
        this.latitudeTf = textField;
    }
    public String getLatitudeTfValue() {
        return latitudeTf.getText();
    }
    public void setLatitudeTfValue(String value) {
        this.getLatitudeTf().setText(value);
    }

    public JTextField getLongitudeTf() {
        return longitudeTf;
    }
    public void setLongitudeTf(JTextField textField) {
        this.longitudeTf = textField;
    }
    public String getLongitudeTfValue() {
        return longitudeTf.getText();
    }
    public void setLongitudeTfValue(String value) {
        this.getLongitudeTf().setText(value);
    }

    public String getLocalType() {
        return localType;
    }

    public void setLocalType(String localType) {
        this.localType = localType;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Location getLocation(String defaultCountry, boolean isFirst) {
        boolean isPostal = localType.equals("postal address");
        errors = new ArrayList<String>();
        Location location = new Location();
        location.setLocalType(localType);
        if(StringUtils.isNotEmpty(getStreetTfValue()) && !getStreetTfValue().trim().isEmpty()) {
            location.setStreet(new Street());
            location.getStreet().setContent(getStreetTfValue().trim());
            location.getStreet().setLang(getStreetTfLanguage());
        } else {
            //if(!isPostal)
                errors.add("streetTf");//_" + locationNb);
            //else
            //    location.setStreet(null); //wrong
        }
        
        if(StringUtils.isNotEmpty(getCityTfValue()) && !getCityTfValue().trim().isEmpty()) {
            location.setMunicipalityPostalcode(new MunicipalityPostalcode());
            location.getMunicipalityPostalcode().setContent(getCityTfValue().trim());
            location.getMunicipalityPostalcode().setLang(getCityTfLanguage());
        } else {
            //if(!isPostal)
                errors.add("cityTf");//_" + locationNb);
            //else
            //    location.setMunicipalityPostalcode(null); //wrong
        }
        
        if(StringUtils.isNotEmpty(getCountryTfValue()) && !getCountryTfValue().trim().isEmpty()) {
            location.setCountry(new Country());
            location.getCountry().setContent(getCountryTfValue().trim());
            location.getCountry().setLang(getCountryTfLanguage());
        } else {
            if(!isPostal)
                errors.add("countryTf");//_" + locationNb);
            else {
                Country country = new Country();
                country.setContent(defaultCountry);
                location.setCountry(country);
            }
        }

        if(StringUtils.isNotEmpty(getDistrictTfValue()) && !getDistrictTfValue().trim().isEmpty()) {
            location.setLocalentity(new Localentity());
            location.getLocalentity().setContent(getDistrictTfValue().trim());
            location.getLocalentity().setLang(getDistrictTfLanguage());
        }

        if(StringUtils.isNotEmpty(getDistrictTfValue()) && !getDistrictTfValue().trim().isEmpty()) {
            location.setSecondem(new Secondem());
            location.getSecondem().setContent(getCountyTfValue().trim());
            location.getSecondem().setLang(getCountyTfLanguage());
        }

        if(StringUtils.isNotEmpty(getRegionTfValue()) && !getRegionTfValue().trim().isEmpty()) {
            location.setFirstdem(new Firstdem());
            location.getFirstdem().setContent(getRegionTfValue().trim());
            location.getFirstdem().setLang(getRegionTfLanguage());
        }

        if(StringUtils.isNotEmpty(getLatitudeTfValue()) && !getLatitudeTfValue().trim().isEmpty()) {
            location.setLatitude(getLatitudeTfValue().trim());
        }

        if(StringUtils.isNotEmpty(getLongitudeTfValue()) && !getLongitudeTfValue().trim().isEmpty()) {
            location.setLongitude(getLongitudeTfValue().trim());
        }

        if(!isFirst && getLocalType().equals("visitors address") && StringUtils.isEmpty(getCityTfValue()) && StringUtils.isEmpty(getStreetTfValue()) && StringUtils.isEmpty(getCountryTfValue())) {
            errors = new ArrayList<String>();
            return null;
        }
        if(getLocalType().equals("postal address") && StringUtils.isEmpty(getCityTfValue()) && StringUtils.isEmpty(getStreetTfValue())) {
            errors = new ArrayList<String>();
            return null;
        }
        return location;
    }
}
