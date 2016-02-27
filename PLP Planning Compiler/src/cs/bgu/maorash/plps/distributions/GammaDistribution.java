package cs.bgu.maorash.plps.distributions;

/**
 * Created by maorash
 * maorash@cs.bgu.ac.il
 */
public class GammaDistribution {
    private String k_shape;
    private String alpha_shape;

    public GammaDistribution(String k_shape, String alpha_shape) {
        this.k_shape = k_shape;
        this.alpha_shape = alpha_shape;
    }

    public String getK_shape() {
        return k_shape;
    }

    public String getAlpha_shape() {
        return alpha_shape;
    }
}