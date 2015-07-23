/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util.extendxsl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import org.apache.commons.codec.binary.Base32;

/**
 *
 * @author apef
 */
public class DePortalIdCalculator extends ExtensionFunctionDefinition {

    private static final StructuredQName FUNCTION_NAME = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "calculateDePortalId");

    @Override
    public StructuredQName getFunctionQName() {
        return FUNCTION_NAME;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 2;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 2;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.OPTIONAL_STRING};
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return SequenceType.OPTIONAL_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new Base32EncoderCall();
    }

    public class Base32EncoderCall extends ExtensionFunctionCall {

        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            String result = "";
            if (sequences.length == 2) {
                try {
                    String providerID = sequences[0].head().getStringValue();
                    String providerItemId = sequences[1].head().getStringValue();
                    result = calculate(providerID, providerItemId);
                    
                } catch (Exception e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
            return StringValue.makeStringValue(result);
        }

        private String calculate(String providerID, String providerItemId) throws NoSuchAlgorithmException {
            byte[] input = (providerID + providerItemId).getBytes();
            return new String(base32Encode(sha1Hash(input)));
        }

        private byte[] base32Encode(byte[] input) {
            Base32 base32 = new Base32();
            return base32.encode(input);
        }

        private byte[] sha1Hash(byte[] input) throws NoSuchAlgorithmException {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            return mDigest.digest(input);
        }

    }

}
