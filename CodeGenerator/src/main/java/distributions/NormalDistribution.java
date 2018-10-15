package distributions;

public class NormalDistribution implements Distribution {
    private String mean;
    private String variance;

    public NormalDistribution(String mean, String variance) {
        this.mean = mean;
        this.variance = variance;
    }

    public String getMean() {
        return this.mean;
    }

    public String getVariance() {
        return this.variance;
    }

    public String toString() {
        return "Normal(" + this.mean + "," + this.variance + ")";
    }
}
