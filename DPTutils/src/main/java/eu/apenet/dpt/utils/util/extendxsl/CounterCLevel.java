package eu.apenet.dpt.utils.util.extendxsl;

import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.value.SequenceType;
import org.apache.log4j.Logger;

/**
 * User: Yoann Moranville
 * Date: Feb 7, 2011
 *
 * @author Yoann Moranville
 */
public class CounterCLevel extends ExtensionFunctionDefinition {
    private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.net/functions", "counterclevel");
    private static final Logger LOG = Logger.getLogger(CounterCLevel.class);
    private CounterCLevelCall counterCLevelCall;

    public CounterCLevel(CounterCLevelCall counterCLevelCall){
        this.counterCLevelCall = counterCLevelCall;
    }

    @Override
    public StructuredQName getFunctionQName() {
        return funcname;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 0;
    }
    public int getMaximumNumberOfArguments() {
        return 0;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.EMPTY_SEQUENCE};
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return SequenceType.EMPTY_SEQUENCE;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return counterCLevelCall;
    }
}
