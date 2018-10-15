package distributions;

public class GammaDistribution implements Distribution {
    private String k_shape;
    private String alpha_shape;

    public GammaDistribution(String k_shape, String alpha_shape) {
        this.k_shape = k_shape;
        this.alpha_shape = alpha_shape;
    }

    public String getK_shape() {
        return this.k_shape;
    }

    public String getAlpha_shape() {
        return this.alpha_shape;
    }

    public String toString() {
        return "Gamma(" + this.k_shape + "," + this.alpha_shape + ")";
    }
}
