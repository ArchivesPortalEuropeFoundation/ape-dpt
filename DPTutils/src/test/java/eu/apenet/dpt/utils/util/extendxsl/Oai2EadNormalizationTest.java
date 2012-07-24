package eu.apenet.dpt.utils.util.extendxsl;

import org.junit.* ;

public class Oai2EadNormalizationTest {
    private final String unitidString = "http://portail2.ad15.mnesys.fr/?id=recherche_grandpublic_detail&amp;doc=accounts%2Fmnesys_ad15%2Fdatas%2Fir%2FArchives%20num%C3%A9ris%C3%A9es%2FFRAD015_9_NUM%2Exml%26page_ref%3D363%26unititle%3DR%C3%A9glementation%20du%20port%20des%20insignes%2C%201940-1941%20%3B%20renseignements%20g%C3%A9n%C3%A9raux%2C%2E%2E%2E%26unitid%3D9%20NUM%203%26unitdate%3D1940-1944";

    @Test
    public void test_unitidNormalization(){
        System.out.println("Test to check for unitid...");
        Oai2EadNormalization oai2EadNormalization = new Oai2EadNormalization();
        System.out.println("---");
        System.out.println("Input: " + unitidString);
        String newcode = oai2EadNormalization.normalizeId(unitidString);
        System.out.println("Output: " + newcode);
    }
}
