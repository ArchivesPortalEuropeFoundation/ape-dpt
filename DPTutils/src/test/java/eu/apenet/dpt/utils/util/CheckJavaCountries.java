package eu.apenet.dpt.utils.util;

import org.junit.Test;
import java.util.Locale;

/**
 * User: yoannmoranville
 * Date: 23/09/14
 *
 * @author yoannmoranville
 */
public class CheckJavaCountries {

    @Test
    public void testCheckJavaCountries() {
        Locale[] locales = Locale.getAvailableLocales();
        for(int i=0; i < locales.length; i++){
            if(!locales[i].getDisplayCountry().equals(""))
                System.out.println(locales[i].getDisplayCountry() + " - " + locales[i].getCountry());
        }
    }

}
