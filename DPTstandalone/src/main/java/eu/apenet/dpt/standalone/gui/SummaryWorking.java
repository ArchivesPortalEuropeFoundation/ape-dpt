package eu.apenet.dpt.standalone.gui;

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
