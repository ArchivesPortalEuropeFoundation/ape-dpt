package eu.apenet.dpt.utils.util;

import java.nio.charset.UnsupportedCharsetException;

import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

/**
 * Created by IntelliJ IDEA.
 * User: Yoann
 * Date: Mar 25, 2010
 * Time: 10:51:53 AM
 * To change this template use File | Settings | File Templates.
 */

public class ChardetSniffer implements nsICharsetDetectionObserver {

    public String returnValue;

    public void Notify(String charsetName) {
        try {
            returnValue = charsetName;
        } catch (UnsupportedCharsetException e) {
            returnValue = null;
        }
    }

    public String getCharsetName(){
        return this.returnValue;
    }
}
