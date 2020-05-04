package eu.apenet.dpt.utils.ead2edm;

/**
 * User: yoannmoranville
 * Date: 30/06/14
 *
 * @author yoannmoranville
 */
public class DigitalObjectCounter {
    private final int numberOfProvidedCHO;
    private final int numberOfDigitalObjects;

    public DigitalObjectCounter(int numberOfProvidedCHO, int digitalObjects) {
        this.numberOfProvidedCHO = numberOfProvidedCHO;
        this.numberOfDigitalObjects = digitalObjects;
    }

    public int getNumberOfProvidedCHO() {
        return numberOfProvidedCHO;
    }

    public int getNumberOfDigitalObjects() {
        return numberOfDigitalObjects;
    }
}