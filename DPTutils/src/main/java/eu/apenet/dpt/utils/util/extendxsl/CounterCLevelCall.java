package eu.apenet.dpt.utils.util.extendxsl;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;

/**
 * User: Yoann Moranville
 * Date: Feb 9, 2011
 *
 * @author Yoann Moranville
 */
public class CounterCLevelCall extends ExtensionFunctionCall {
        private int counter = 0;
        private int maxCounter;
        public SequenceIterator call(SequenceIterator[] arguments, XPathContext context) throws XPathException {
            count();
            return SingletonIterator.makeIterator(null);
        }

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
    }