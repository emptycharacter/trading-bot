import java.util.Arrays;

public class BlackScholes {
    
    private static final double RISK_FREE_RATE = 0.05; // 5% annual risk-free rate

    public static double normCDF(double x) {
        return 0.5 * (1.0 + erf(x / Math.sqrt(2.0)));
    }

    public static double erf(double x) {
        // Approximation of the error function (erf)
        double t = 1.0 / (1.0 + 0.5 * Math.abs(x));
        double tau = t * Math.exp(-x*x - 1.26551223 + t * (1.00002368 + t * (0.37409196 + t * 
                     (0.09678418 + t * (-0.18628806 + t * (0.27886807 + t * 
                     (-1.13520398 + t * (1.48851587 + t * (-0.82215223 + t * 0.17087277)))))))));
        return (x >= 0) ? (1 - tau) : (tau - 1);
    }

    public static double blackScholesPrice(boolean isCall, double S, double K, double T, double sigma) {
        double d1 = (Math.log(S / K) + (RISK_FREE_RATE + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);
        
        if (isCall) {
            return S * normCDF(d1) - K * Math.exp(-RISK_FREE_RATE * T) * normCDF(d2);
        } else {
            return K * Math.exp(-RISK_FREE_RATE * T) * normCDF(-d2) - S * normCDF(-d1);
        }
    }

    public static double delta(boolean isCall, double S, double K, double T, double sigma) {
        double d1 = (Math.log(S / K) + (RISK_FREE_RATE + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        return isCall ? normCDF(d1) : normCDF(d1) - 1;
    }

    public static double gamma(double S, double K, double T, double sigma) {
        double d1 = (Math.log(S / K) + (RISK_FREE_RATE + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double pdf = Math.exp(-0.5 * d1 * d1) / Math.sqrt(2 * Math.PI);
        return pdf / (S * sigma * Math.sqrt(T));
    }

    public static double vega(double S, double K, double T, double sigma) {
        double d1 = (Math.log(S / K) + (RISK_FREE_RATE + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double pdf = Math.exp(-0.5 * d1 * d1) / Math.sqrt(2 * Math.PI);
        return S * pdf * Math.sqrt(T);
    }

    public static double theta(boolean isCall, double S, double K, double T, double sigma) {
        double d1 = (Math.log(S / K) + (RISK_FREE_RATE + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);
        double pdf = Math.exp(-0.5 * d1 * d1) / Math.sqrt(2 * Math.PI);
        
        double term1 = -(S * pdf * sigma) / (2 * Math.sqrt(T));
        double term2 = RISK_FREE_RATE * K * Math.exp(-RISK_FREE_RATE * T) * normCDF(isCall ? d2 : -d2);
        return isCall ? term1 - term2 : term1 + term2;
    }

    public static double rho(boolean isCall, double S, double K, double T, double sigma) {
        double d2 = (Math.log(S / K) + (RISK_FREE_RATE - 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        return isCall ? K * T * Math.exp(-RISK_FREE_RATE * T) * normCDF(d2) : -K * T * Math.exp(-RISK_FREE_RATE * T) * normCDF(-d2);
    }

    public static void main(String[] args) {
        double S = 100.0;  // Spot price
        double K = 100.0;  // Strike price
        double T = 1.0;    // Time to maturity in years
        double sigma = 0.2; // Volatility (20%)
        boolean isCall = true; // True for Call, False for Put

        System.out.println("Option Price: " + blackScholesPrice(isCall, S, K, T, sigma));
        System.out.println("Delta: " + delta(isCall, S, K, T, sigma));
        System.out.println("Gamma: " + gamma(S, K, T, sigma));
        System.out.println("Vega: " + vega(S, K, T, sigma));
        System.out.println("Theta: " + theta(isCall, S, K, T, sigma));
        System.out.println("Rho: " + rho(isCall, S, K, T, sigma));
    }
}
