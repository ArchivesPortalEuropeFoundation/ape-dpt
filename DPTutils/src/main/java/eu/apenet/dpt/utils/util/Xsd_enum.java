package eu.apenet.dpt.utils.util;

/**
 * User: Yoann Moranville
 * Date: Aug 10, 2010
 *
 * @author Yoann Moranville
 */
public enum Xsd_enum {
    XSD_EAD_SCHEMA("EAD 2002", "ead_2002.xsd"),
    XSD_APE_SCHEMA("APE EAD", "APE_EAD.xsd"),
    XSD1_0_APE_SCHEMA("APE EAD (XSD 1.0)", "APE_EAD_XSD1.0.xsd"),
    XSD_EAC_SCHEMA("EAC-CPF", "cpf.xsd"),
    XSD_EAG_SCHEMA("EAG", "eag.xsd"),
    XSD_APE_EAG_SCHEMA("APE EAG", "APE_EAG.xsd");

    private String path;
    private String readableName;

    Xsd_enum(String readableName, String path) {
        this.readableName = readableName;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getReadableName(){
        return readableName;
    }

    @Override
    public String toString(){
        return "Schema name: " + readableName + " and path: " + path;
    }
}
