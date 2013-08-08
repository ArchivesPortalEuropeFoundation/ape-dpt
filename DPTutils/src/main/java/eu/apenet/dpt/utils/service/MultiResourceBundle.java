package eu.apenet.dpt.utils.service;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class MultiResourceBundle extends ResourceBundle {

    private Properties properties;

    public MultiResourceBundle(String mainBaseName,String additionalBaseNames) {
        setParent(ResourceBundle.getBundle(mainBaseName, new MultiResourceBundleControl(additionalBaseNames)));
    }
    
    public MultiResourceBundle(String mainBaseName,String additionalBaseNames,Locale locale) {
        setParent(ResourceBundle.getBundle(mainBaseName, locale, new MultiResourceBundleControl(additionalBaseNames)));
    }
    
    protected MultiResourceBundle(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected Object handleGetObject(String key) {
        return properties != null ? properties.get(key) : parent.getObject(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration<String> getKeys() {
        return properties != null ? (Enumeration<String>) properties.propertyNames() : parent.getKeys();
    }

    protected static class MultiResourceBundleControl extends Control {
        private static final String SUBFIX = ".properties";
        private String additionalBaseNames = "";
        
        public MultiResourceBundleControl(String additionalBaseNames) {
        	this.additionalBaseNames = additionalBaseNames;
		}
        
        public MultiResourceBundleControl(String additionalBaseNames,Locale locale) {
        	this.additionalBaseNames = additionalBaseNames;
		}
        
		@Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            Properties properties = load(baseName, loader);
            if (additionalBaseNames != null && additionalBaseNames.length()>0){
                for (String includeBaseName : additionalBaseNames.split("\\s*,\\s*")){
                    properties.putAll(load(includeBaseName,loader));
                }
            }
            return new MultiResourceBundle(properties);
        }

        private Properties load(String baseName, ClassLoader loader) throws IOException{
            Properties properties = new Properties();
            properties.load(loader.getResourceAsStream(baseName + SUBFIX));
            return properties;
        }
    }

}
