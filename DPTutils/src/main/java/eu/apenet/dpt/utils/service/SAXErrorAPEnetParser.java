package eu.apenet.dpt.utils.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Yoann Moranville
 * Date: Jan 20, 2011
 *
 * @author Yoann Moranville
 */
public class SAXErrorAPEnetParser {

    public static String doParser(String message){
        for(ValidationRuleHeaders validationRuleHeader : ValidationRuleHeaders.values()){
            if(message.startsWith(validationRuleHeader.getFilePrefix())){
                return changeMessage(message, validationRuleHeader.getRegexp(), validationRuleHeader.getNewMessage());
            }
        }
        return message; 
    }

    public static String changeMessage(String message, Pattern pattern, String newMessage){
        Matcher matcher = pattern.matcher(message);
        if(matcher.matches()){
            if(newMessage.contains("$3"))
                return newMessage.replace("$1", matcher.group(2)).replace("$2", matcher.group(3)).replace("$3", matcher.group(4));
            if(newMessage.contains("$2"))
                return newMessage.replace("$1", matcher.group(2)).replace("$2", matcher.group(3));
            else if(newMessage.contains("$1"))
                return newMessage.replace("$1", matcher.group(2));
        }
        return message;
    }

    public enum ValidationRuleHeaders {
//        A("cvc-accept", Pattern.compile("")),
//        B("cvc-assertion", Pattern.compile("")),
//        C("cvc-assertions-valid", Pattern.compile("")),
//        D("cvc-assess-attr", Pattern.compile("")),
//        E("cvc-assess-elt", ""),
        F("cvc-attribute.3", Pattern.compile("([^:]+): [^\\']+\\'([^\\']+)\\'[^\\']+\\'([^\\']+)\\'[^\\']+\\'([^\\']+)\\'.*"), "You are using the forbidden value '$1' in the attribute '$2' of the element '$3'."), //cvc-attribute.3: The value 'series' of attribute 'level' on element 'c' is not valid with respect to its type, '#AnonType_levelc.errorlevelitem'.
//        G("cvc-au", ""),
//        H("cvc-complex-content", ""),
        I("cvc-complex-type.2.4.b", Pattern.compile("([^:]+): [^\\']+\\'([^\\']+)\\'[^\\']+\\'([^\\']+)\\'.*"), "The element '$1' is missing at least one of the following sub-elements: '$2'."), //cvc-complex-type.2.4.b: The content of element 'did' is not complete. One of '{"urn:isbn:1-931666-22-9":container, "urn:isbn:1-931666-22-9":dao, "urn:isbn:1-931666-22-9":langmaterial, "urn:isbn:1-931666-22-9":materialspec, "urn:isbn:1-931666-22-9":note, "urn:isbn:1-931666-22-9":origination, "urn:isbn:1-931666-22-9":physdesc, "urn:isbn:1-931666-22-9":physloc, "urn:isbn:1-931666-22-9":repository, "urn:isbn:1-931666-22-9":unitdate, "urn:isbn:1-931666-22-9":unitid, "urn:isbn:1-931666-22-9":unittitle}' is expected.
        I2("cvc-complex-type.2.4.a", Pattern.compile("([^:]+): [^\\']+\\'([^\\']+)\\'[^\\']+\\'([^\\']+)\\'.*"), "The element '$1' has been found but should not appear here, only one (or more) of the following element(s) are accepted here: '$2'."), //cvc-complex-type.2.4.a: Invalid content was found starting with element 'odd'. One of '{"urn:isbn:1-931666-22-9":did}' is expected.
        I3("cvc-complex-type.2.4.d", Pattern.compile("([^:]+): [^\\']+\\'([^\\']+)\\'[^\\']+.*"), "The element '$1' has been found but should not appear here, no child element is allowed here."), //cvc-complex-type.2.4.d: Invalid content was found starting with element 'unittitle'. No child element is expected at this point.
//        J("cvc-datatype-valid", ""),
        K("cvc-elt.1.a", Pattern.compile("([^:]+): Cannot find the declaration of element \\'(.*)\\'\\."), "The declaration of '$1' was not found, your file might not contain the schema declaration corresponding (xmlns)."), //cvc-elt.1.a: Cannot find the declaration of element 'ead'.
        K2("cvc-elt.1", Pattern.compile("([^:]+): Cannot find the declaration of element \\'(.*)\\'\\."), "The declaration of '$1' was not found, your file might not contain the schema declaration corresponding (xmlns)."), //cvc-elt.1: Cannot find the declaration of element 'eac'.
        L("cvc-enumeration-valid", Pattern.compile("([^:]+): [^\\']+\\'([^\\']+)\\'[^\\']+\\'([^\\']+)\\'.*"), "You are using the value '$1' in an attribute that can only contain one of those values: '$2'."), //cvc-enumeration-valid: Value 'series' is not facet-valid with respect to enumeration '[file, item, subseries]'. It must be a value from the enumeration.
//        M("cvc-explicitTimezone-valid", ""),
//        N("cvc-fractionDigits-valid", ""),
//        O("cvc-id", ""),
//        P("cvc-identity-constraint", ""),
//        Q("cvc-length-valid", ""),
//        R("cvc-maxExclusive-valid", ""),
//        S("cvc-maxInclusive-valid", ""),
//        T("cvc-maxLength-valid", ""),
//        U("cvc-maxScale-valid", ""),
//        V("cvc-minExclusive-valid", ""),
//        W("cvc-minInclusive-valid", ""),
//        X("cvc-minLength-valid", ""),
//        Y("cvc-minScale-valid", ""),
//        Z("cvc-model-group", Pattern.compile(""), "")
        AA("cvc-pattern-valid", Pattern.compile("([^:]+): Value \\'(.*)\\' is not facet-valid with respect to pattern (.*) for type (.*)"), "The value '$1' is not following the correct pattern for such element, which should be '$2'"),
        ;

        private String filePrefix;
        private Pattern regexp;
        private String newMessage;
        ValidationRuleHeaders(String filePrefix, Pattern regexp, String newMessage){
            this.filePrefix = filePrefix;
            this.regexp = regexp;
            this.newMessage = newMessage;
        }
        public String getFilePrefix(){
            return filePrefix;
        }
        public Pattern getRegexp(){
            return regexp;
        }
        public String getNewMessage(){
            return newMessage;
        }
    }
}
