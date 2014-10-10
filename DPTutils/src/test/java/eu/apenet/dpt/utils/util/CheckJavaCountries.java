package eu.apenet.dpt.utils.util;

import com.neovisionaries.i18n.CountryCode;
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

    @Test
    public void testCheckNvi18Countries() {
        for (CountryCode code : CountryCode.values()) {
            System.out.format("[%s] %s\n", code, code.getName());
        }
    }

    @Test
    public void testCountryCodeToLocale() {
        CountryCode countryCode = CountryCode.getByCodeIgnoreCase("GE");
        System.out.println(countryCode);
        System.out.println(countryCode.toLocale().getDisplayCountry());
    }

}
