package eu.apenet.dpt.standalone.gui;

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

import javax.swing.*;

/**
 * User: Yoann Moranville
 * Date: 17/01/2012
 *
 * @author Yoann Moranville
 */
public class SummaryWorking implements Runnable {
    private boolean continueRunning;
    private boolean isBatch;
    private JLabel resultArea;
    private JProgressBar batchProgressBar;
    private int numberFiles = 0;
    private int currentFileNumberBatch = 0;

    public SummaryWorking(JLabel resultArea, JProgressBar batchProgressBar){
        this.continueRunning = true;
        this.isBatch = true;
        this.resultArea = resultArea;
        this.batchProgressBar = batchProgressBar;
    }

    public SummaryWorking(JLabel resultArea) {
        this.isBatch = false;
        this.continueRunning = true;
        this.resultArea = resultArea;
    }

    public void setTotalNumberFiles(int numberFiles) {
        this.numberFiles = numberFiles;
    }

    public void setCurrentFileNumberBatch(int currentFileNumberBatch) {
        this.currentFileNumberBatch = currentFileNumberBatch;
    }

    public void run(){
        String append = "";
        while(continueRunning){
            resultArea.setText(resultArea.getText().replaceAll("\\.", ""));
            resultArea.setText(resultArea.getText()+append);
            if(append.length() == 3){
                append = "";
            } else {
                append += ".";
            }

            if(isBatch && batchProgressBar != null){
                if(batchProgressBar.isIndeterminate()){
                    batchProgressBar.setMaximum(numberFiles);
                    batchProgressBar.setValue(0);
                    batchProgressBar.setIndeterminate(false);
                }
                if(numberFiles != 0){
                    batchProgressBar.setValue(currentFileNumberBatch - 1);
                    batchProgressBar.setString(currentFileNumberBatch + "/" + numberFiles);
                }
            }

            try{
                Thread.sleep(500);
            } catch(InterruptedException e){
                //Nothing
            }
        }
    }
    public void stop(){
        continueRunning = false;
    }

}
