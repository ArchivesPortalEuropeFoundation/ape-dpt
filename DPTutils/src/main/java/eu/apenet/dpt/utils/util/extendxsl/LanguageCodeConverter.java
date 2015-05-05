/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util.extendxsl;

import eu.apenet.dpt.utils.util.LanguageConverter;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

/**
 *
 * @author apef
 */
public class LanguageCodeConverter extends ExtensionFunctionDefinition{

    private static final StructuredQName FUNCTION_NAME = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "convertLangCode");
    
    @Override
    public StructuredQName getFunctionQName() {
        return FUNCTION_NAME;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.OPTIONAL_STRING};
    }

    @Override
    public SequenceType getResultType(SequenceType[] sts) {
        return SequenceType.OPTIONAL_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new LanguageCodeConverterCall();
    }

    public static class LanguageCodeConverterCall extends ExtensionFunctionCall {

        public LanguageCodeConverterCall() {
        }

        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            String out = convertCode(sequences[0].head().getStringValue());
            return StringValue.makeStringValue(out);
        }

        private String convertCode(String stringValue) {
            return LanguageConverter.get639_1Code(stringValue);
        }
    }
}
