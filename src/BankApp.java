import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class BankApp extends JFrame {
    private final SimpleBankAccount account;
    private final JLabel balanceLabel;
    private final JTextField amountField;

    public BankApp() {
        setTitle("Bank Account Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 220);
        setLocationRelativeTo(null);

        double startBalance = promptForInitialBalance();
        account = new SimpleBankAccount(startBalance);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        JButton showButton = new JButton("Show Balance");
        balanceLabel = new JLabel(format(account.getBalance()), SwingConstants.LEFT);

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Balance:"), gbc);
        gbc.gridx = 1; panel.add(balanceLabel, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Amount:"), gbc);
        amountField = new JTextField();
        gbc.gridx = 1; panel.add(amountField, gbc);
        y++;

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton exitBtn = new JButton("Exit");
        btnRow.add(showButton);
        btnRow.add(depositBtn);
        btnRow.add(withdrawBtn);
        btnRow.add(exitBtn);
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = y; panel.add(btnRow, gbc);

        add(panel);

        showButton.addActionListener(e -> updateBalance());
        depositBtn.addActionListener(e -> {
            Double amount = parseAmount();
            if (amount == null) return;
            account.deposit(amount);
            updateBalance();
        });
        withdrawBtn.addActionListener(e -> {
            Double amount = parseAmount();
            if (amount == null) return;
            boolean ok = account.withdraw(amount);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Amount exceeds account balance.", "Withdraw",
                        JOptionPane.WARNING_MESSAGE);
            }
            updateBalance();
        });
        exitBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Final Balance: " + format(account.getBalance()));
            System.exit(0);
        });
    }

    private Double parseAmount() {
        String text = amountField.getText().trim();
        try {
            double a = Double.parseDouble(text);
            if (a <= 0) throw new NumberFormatException();
            return a;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a positive number.", "Invalid input",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private double promptForInitialBalance() {
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter initial balance:", "0.00");
            if (input == null) return 0.0; // user canceled -> default 0
            try {
                double v = Double.parseDouble(input.trim());
                if (v < 0) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a non-negative number.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String format(double amount) {
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        return df.format(amount);
    }

    private void updateBalance() {
        balanceLabel.setText(format(account.getBalance()));
        amountField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankApp().setVisible(true));
    }
}
