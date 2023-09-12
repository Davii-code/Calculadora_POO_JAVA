package Interface_Calculadora;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorInterface {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Calculadora");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 400));

        JPanel panel = new JPanel(new BorderLayout());

        JTextField display = new JTextField();
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setEditable(false);
        panel.add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 10, 10));

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = display.getText();
                    if (label.equals("=")) {
                        try {
                            String result = evaluateExpression(text);
                            display.setText(result);
                        } catch (Exception ex) {
                            display.setText("Erro");
                        }
                    } else {
                        display.setText(text + label);
                    }
                }
            });
            buttonPanel.add(button);
        }

        panel.add(buttonPanel, BorderLayout.CENTER);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private static String evaluateExpression(String expression) throws Exception {
        // Implemente a lógica de avaliação da expressão aqui
        // Por exemplo, você pode usar a classe JavaScriptEngine para avaliar a expressão matemática.
        // Neste exemplo simples, vamos apenas usar o eval() do JavaScriptEngine.
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        return engine.eval(expression).toString();
    }
}
