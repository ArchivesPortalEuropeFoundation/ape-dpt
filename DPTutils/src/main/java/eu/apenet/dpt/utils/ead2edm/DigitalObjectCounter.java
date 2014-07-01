package eu.apenet.dpt.utils.ead2edm;

/**
 * User: yoannmoranville
 * Date: 30/06/14
 *
 * @author yoannmoranville
 */
public class DigitalObjectCounter {
    private int numberOfProvidedCHO;
    private int numberOfWebResource;

    public DigitalObjectCounter(int numberOfProvidedCHO, int numberOfWebResource) {
        this.numberOfProvidedCHO = numberOfProvidedCHO;
        this.numberOfWebResource = numberOfWebResource;
    }

    public int getNumberOfProvidedCHO() {
        return numberOfProvidedCHO;
    }

    public int getNumberOfWebResource() {
        return numberOfWebResource;
    }
}