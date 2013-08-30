package eu.apenet.dpt.utils.service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Class which is based on Bastiaan solution for issue #497
 * to solve multi-properties problems into DPT.
 */
public class ResourceBundlesWrapper extends ResourceBundle {

	private Map<String,Map<Locale,ResourceBundle>> resourcesBundles = new HashMap<String,Map<Locale, ResourceBundle>>();
	private ResourceBundle resourceBundle = null;
	private Locale currentLocale = null;
	private String[] basenames;
	
	public ResourceBundlesWrapper (String[] basenames,Locale currentLocale){
	    this.basenames = basenames;
	    this.currentLocale = currentLocale;
	}
	
	@Override
	protected String handleGetObject(String key) {
		return (String) getString(key,currentLocale);
	}

	@Override
	public Enumeration<String> getKeys() {
		return (resourceBundle!=null)?resourceBundle.getKeys():null;
	}
	public String getString(String key,Locale locale){ //locale
        boolean found = false;
        Map<Locale,ResourceBundle> mapBundle = null;
        String value = key;
        for (int i = 0; i< this.basenames.length && !found;i++){
        	String basename = this.basenames[i];
        	if(!this.resourcesBundles.containsKey(basename)){ 
        		mapBundle = new HashMap<Locale,ResourceBundle>();
        		this.resourceBundle = ResourceBundle.getBundle(basename,locale);
        		mapBundle.put(locale,this.resourceBundle);
        		this.resourcesBundles.put(basename,mapBundle);
        	}else{
        		mapBundle = this.resourcesBundles.get(basename);
        		if(mapBundle.containsKey(locale)){
        			this.resourceBundle = mapBundle.get(locale);
        		}else{
        			this.resourceBundle = ResourceBundle.getBundle(basename,locale);
        		}
        		mapBundle.put(locale,this.resourceBundle);
        	}
        	if(this.resourceBundle!=null && !this.resourceBundle.containsKey(basename)){
//    			mapBundle.put(locale, this.resourceBundle);
    			this.resourcesBundles.put(basename,mapBundle);
    			i = this.basenames.length; //exit flag
    		}
        }
        if(this.resourceBundle!=null){
        	value = this.resourceBundle.getString(key);
        }
		return value;
    }
}
