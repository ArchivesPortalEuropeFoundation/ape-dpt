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

import eu.apenet.dpt.standalone.gui.commons.SwingStructures.TextFieldWithLanguage;
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
        if(location.getStreet() == null)
            location.setStreet(new Street());
        this.streetTf = new TextFieldWithLanguage(location.getStreet().getContent(), location.getStreet().getLang());
        if(location.getMunicipalityPostalcode() == null)
            location.setMunicipalityPostalcode(new MunicipalityPostalcode());
        this.cityTf = new TextFieldWithLanguage(location.getMunicipalityPostalcode().getContent(), location.getMunicipalityPostalcode().getLang());
        if(location.getLocalentity() == null)
            location.setLocalentity(new Localentity());
        this.districtTf = new TextFieldWithLanguage(location.getLocalentity().getContent(), location.getLocalentity().getLang());
        if(location.getSecondem() == null)
            location.setSecondem(new Secondem());
        this.countyTf = new TextFieldWithLanguage(location.getSecondem().getContent(), location.getSecondem().getLang());
        if(location.getFirstdem() == null)
            location.setFirstdem(new Firstdem());
        this.regionTf = new TextFieldWithLanguage(location.getFirstdem().getContent(), location.getFirstdem().getLang());
        if(location.getCountry() == null)
            location.setCountry(new Country());
        this.countryTf = new TextFieldWithLanguage(location.getCountry().getContent(), location.getCountry().getLang());
        this.latitudeTf = new JTextField(location.getLatitude());
        this.longitudeTf = new JTextField(location.getLongitude());
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
            location.getStreet().setContent(getStreetTfValue());
            location.getStreet().setLang(getStreetTfLanguage());
        } else {
            //if(!isPostal)
                errors.add("streetTf");//_" + locationNb);
            //else
            //    location.setStreet(null); //wrong
        }
        
        if(StringUtils.isNotEmpty(getCityTfValue()) && !getCityTfValue().trim().isEmpty()) {
            location.setMunicipalityPostalcode(new MunicipalityPostalcode());
            location.getMunicipalityPostalcode().setContent(getCityTfValue());
            location.getMunicipalityPostalcode().setLang(getCityTfLanguage());
        } else {
            //if(!isPostal)
                errors.add("cityTf");//_" + locationNb);
            //else
            //    location.setMunicipalityPostalcode(null); //wrong
        }
        
        if(StringUtils.isNotEmpty(getCountryTfValue()) && !getCountryTfValue().trim().isEmpty()) {
            location.setCountry(new Country());
            location.getCountry().setContent(getCountryTfValue());
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
            location.getLocalentity().setContent(getDistrictTfValue());
            location.getLocalentity().setLang(getDistrictTfLanguage());
        }

        if(StringUtils.isNotEmpty(getDistrictTfValue()) && !getDistrictTfValue().trim().isEmpty()) {
            location.setSecondem(new Secondem());
            location.getSecondem().setContent(getCountyTfValue());
            location.getSecondem().setLang(getCountyTfLanguage());
        }

        if(StringUtils.isNotEmpty(getRegionTfValue()) && !getRegionTfValue().trim().isEmpty()) {
            location.setFirstdem(new Firstdem());
            location.getFirstdem().setContent(getRegionTfValue());
            location.getFirstdem().setLang(getRegionTfLanguage());
        }

        if(StringUtils.isNotEmpty(getLatitudeTfValue()) && !getLatitudeTfValue().trim().isEmpty()) {
            location.setLatitude(getLatitudeTfValue());
        }

        if(StringUtils.isNotEmpty(getLongitudeTfValue()) && !getLongitudeTfValue().trim().isEmpty()) {
            location.setLongitude(getLongitudeTfValue());
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
