import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OptionPricingGUI gui = new OptionPricingGUI();
            gui.setVisible(true);
        });
    }
}

class OptionPricingGUI extends JFrame {
    private JTextField spotPriceField, strikePriceField, timeField, volatilityField;
    private JButton calculateButton;
    private JTextArea resultArea;

    public OptionPricingGUI() {
        setTitle("Black-Scholes Option Pricing");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        add(new JLabel("Spot Price (S):"));
        spotPriceField = new JTextField("100");
        add(spotPriceField);

        add(new JLabel("Strike Price (K):"));
        strikePriceField = new JTextField("100");
        add(strikePriceField);

        add(new JLabel("Time to Maturity (T in years):"));
        timeField = new JTextField("1");
        add(timeField);

        add(new JLabel("Volatility (sigma):"));
        volatilityField = new JTextField("0.2");
        add(volatilityField);

        calculateButton = new JButton("Calculate");
        add(calculateButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea));

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateOptionPrice();
            }
        });
    }

    private void calculateOptionPrice() {
        try {
            double S = Double.parseDouble(spotPriceField.getText());
            double K = Double.parseDouble(strikePriceField.getText());
            double T = Double.parseDouble(timeField.getText());
            double sigma = Double.parseDouble(volatilityField.getText());

            BlackScholes bs = new BlackScholes();
            double callPrice = BlackScholes.blackScholesPrice(true, S, K, T, sigma);
            double putPrice = BlackScholes.blackScholesPrice(false, S, K, T, sigma);

            resultArea.setText("Call Option Price: " + callPrice + "\n" + "Put Option Price: " + putPrice);
        } catch (NumberFormatException ex) {
            resultArea.setText("Invalid input! Please enter numeric values.");
        }
    }
}
