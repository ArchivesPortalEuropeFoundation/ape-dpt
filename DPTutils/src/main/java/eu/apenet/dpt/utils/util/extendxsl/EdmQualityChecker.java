/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dpt.utils.util.extendxsl;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public class EdmQualityChecker extends ExtensionFunctionDefinition {

    private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "checkIfExist");
    private Logger log = Logger.getLogger(getClass());

    private EdmQualityCheckerCall edmQualityCheckerCall;

    public EdmQualityChecker(EdmQualityCheckerCall edmQualityCheckerCall) {
        this.edmQualityCheckerCall = edmQualityCheckerCall;
    }

    @Override
    public StructuredQName getFunctionQName() {
        return funcname;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.SINGLE_NODE};
    }

    @Override
    public SequenceType getResultType(SequenceType[] sts) {
        return SequenceType.OPTIONAL_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return edmQualityCheckerCall;
    }
}
