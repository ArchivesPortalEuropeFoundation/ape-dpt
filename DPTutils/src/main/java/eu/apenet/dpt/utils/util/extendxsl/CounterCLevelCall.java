package eu.apenet.dpt.utils.util.extendxsl;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.EmptySequence;
import net.sf.saxon.value.StringValue;

/**
 * User: Yoann Moranville
 * Date: Feb 9, 2011
 *
 * @author Yoann Moranville
 */
public class CounterCLevelCall extends ExtensionFunctionCall {
        private int counter = 0;
        private int maxCounter;

        public void count(){
            counter++;
        }

        public int getCounter(){
            return counter;
        }

        public int getMaxCounter(){
            return maxCounter;
        }

        public void initializeCounter(int max) {
            maxCounter = max;
            counter = 0;
        }

    @Override
    public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
        count();
        return EmptySequence.getInstance();
    }
}