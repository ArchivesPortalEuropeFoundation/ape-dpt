/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Stefan Papp
 * @date 2013/04/18
 */
public class LanguageConverter {

    public static String get639_1Code(String lang639_2Code) {
        String[] languages = Locale.getISOLanguages();
        Map<String, Locale> localeMap = new HashMap<String, Locale>(languages.length);
        // Add 639-2/T variants
        for (String language : languages) {
            Locale locale = new Locale(language);
            localeMap.put(locale.getISO3Language(), locale);
        }
        // Add 639-2/B variants below this line
        localeMap.put("alb", new Locale("sq"));
        localeMap.put("arm", new Locale("hy"));
        localeMap.put("baq", new Locale("eu"));
        localeMap.put("bur", new Locale("my"));
        localeMap.put("chi", new Locale("zh"));
        localeMap.put("cze", new Locale("cs"));
        localeMap.put("dut", new Locale("nl"));
        localeMap.put("fre", new Locale("fr"));
        localeMap.put("geo", new Locale("ka"));
        localeMap.put("ger", new Locale("de"));
        localeMap.put("gre", new Locale("el"));
        localeMap.put("ice", new Locale("is"));
        localeMap.put("mac", new Locale("mk"));
        localeMap.put("mao", new Locale("mi"));
        localeMap.put("may", new Locale("ms"));
        localeMap.put("per", new Locale("fa"));
        localeMap.put("rum", new Locale("ro"));
        localeMap.put("slo", new Locale("sk"));
        localeMap.put("tib", new Locale("bo"));
        localeMap.put("wel", new Locale("cy"));
        return localeMap.get(lang639_2Code).toString();
    }
}
