package eu.apenet.dpt.standalone.gui.conversion;

/*
 * #%L
 * Data Preparation Tool Standalone mapping tool
 * %%
 * Copyright (C) 2009 - 2014 Archives Portal Europe
 * %%
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * #L%
 */

import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;
import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * User: Yoann Moranville
 * Date: 17/01/2012
 *
 * @author Yoann Moranville
 */
public class CounterThread implements Runnable {
    private static final Logger LOG = Logger.getLogger(CounterThread.class);

    private CounterCLevelCall counterCLevelCall;
    private boolean continueCounter = true;
    private int counterMax;
    private JProgressBar progressBar;

    public CounterThread(CounterCLevelCall counterCLevelCall, JProgressBar progressBar, int counterMax){
        this.counterCLevelCall = counterCLevelCall;
        this.progressBar = progressBar;
        this.counterMax = counterMax;
    }

    public void run() {
        int cou = 0;
        while(continueCounter) {
            if(progressBar != null){
                if(progressBar.isIndeterminate()) {
                    progressBar.setMaximum(counterMax);
                    progressBar.setValue(0);
                    progressBar.setIndeterminate(false);
                }
                cou = counterCLevelCall.getCounter();
            }
            try {
                progressBar.setValue(cou);
                if(continueCounter)
                    Thread.sleep(500);
            } catch (NullPointerException ex){
                LOG.error("Error when trying to set the value in the progress bar", ex);
            } catch (InterruptedException e){
                //It means the thread has been closed before the end of the pause, we do not do anything
            }
        }
    }

    public void stop() {
        continueCounter = false;
    }
}
