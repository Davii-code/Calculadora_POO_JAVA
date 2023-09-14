import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraSwingClient extends JFrame {
    private final JTextField campoDeExibicao;
    private final JPanel painelDeBotoes;
    private final JButton[] botoesNumericos;
    private final JButton[] botoesOperacao;
    private final JButton botaoIgual;
    private final JButton botaoLimpar;
    private final JLabel rotuloResultado;

    private String entradaAtual = "";
    private List<String> operacoes = new ArrayList<>();

    public CalculadoraSwingClient() {
        setTitle("Calculadora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());

        campoDeExibicao = new JTextField(10);
        campoDeExibicao.setFont(new Font("Arial", Font.PLAIN, 24));
        campoDeExibicao.setEditable(false);

        painelDeBotoes = new JPanel();
        painelDeBotoes.setLayout(new GridLayout(5, 4));

        botoesNumericos = new JButton[10];
        for (int i = 0; i < 10; i++) {
            botoesNumericos[i] = new JButton(String.valueOf(i));
            botoesNumericos[i].setFont(new Font("Arial", Font.PLAIN, 18));
            botoesNumericos[i].addActionListener(new ListenerBotaoNumero());
            painelDeBotoes.add(botoesNumericos[i]);
        }

        botoesOperacao = new JButton[4];
        botoesOperacao[0] = criarBotaoOperacao("+");
        botoesOperacao[1] = criarBotaoOperacao("-");
        botoesOperacao[2] = criarBotaoOperacao("*");
        botoesOperacao[3] = criarBotaoOperacao("/");
        for (JButton botao : botoesOperacao) {
            painelDeBotoes.add(botao);
        }

        botaoIgual = new JButton("=");
        botaoIgual.setFont(new Font("Arial", Font.PLAIN, 18));
        botaoIgual.addActionListener(new ListenerBotaoIgual());
        botaoLimpar = new JButton("C");
        botaoLimpar.setFont(new Font("Arial", Font.PLAIN, 18));
        botaoLimpar.addActionListener(new ListenerBotaoLimpar());

        painelDeBotoes.add(botaoIgual);
        painelDeBotoes.add(botaoLimpar);

        rotuloResultado = new JLabel("");
        rotuloResultado.setFont(new Font("Arial", Font.PLAIN, 24));

        add(campoDeExibicao, BorderLayout.NORTH);
        add(painelDeBotoes, BorderLayout.CENTER);
        add(rotuloResultado, BorderLayout.SOUTH);
    }

    private JButton criarBotaoOperacao(String operacao) {
        JButton botao = new JButton(operacao);
        botao.setFont(new Font("Arial", Font.PLAIN, 18));
        botao.addActionListener(new ListenerBotaoOperacao(operacao));
        return botao;
    }

    private class ListenerBotaoNumero implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String numero = ((JButton) e.getSource()).getText();
            entradaAtual += numero;
            campoDeExibicao.setText(entradaAtual);
        }
    }

    private class ListenerBotaoOperacao implements ActionListener {
        private final String operacao;

        public ListenerBotaoOperacao(String operacao) {
            this.operacao = operacao;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!entradaAtual.isEmpty()) {
                operacoes.add(entradaAtual);
                operacoes.add(operacao);
                entradaAtual = "";
                campoDeExibicao.setText("");
            }
        }
    }

    private DecimalFormat formatoDecimal = new DecimalFormat("#.##");

    private class ListenerBotaoIgual implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!entradaAtual.isEmpty()) {
                operacoes.add(entradaAtual);
                entradaAtual = "";
                campoDeExibicao.setText("");
            }

            if (operacoes.isEmpty()) {
                return;
            }

            try {
                List<String> expressao = new ArrayList<>(operacoes);
                double resultado = avaliarExpressao(expressao);

                if (resultado == (int) resultado) {
                    campoDeExibicao.setText(String.valueOf((int) resultado));
                } else {
                    campoDeExibicao.setText(formatoDecimal.format(resultado));
                }

            } catch (ArithmeticException ex) {
                campoDeExibicao.setText("Erro: Divisão por zero");
            } catch (IllegalArgumentException ex) {
                campoDeExibicao.setText("Erro: Expressão inválida");
            }
        }
    }


    private class ListenerBotaoLimpar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            entradaAtual = "";
            operacoes.clear();
            campoDeExibicao.setText("");
        }
    }


    private double avaliarExpressao(List<String> expressao) {
        double resultado = Double.parseDouble(expressao.get(0));

        for (int i = 1; i < expressao.size(); i += 2) {
            String operacao = expressao.get(i);
            double numero = Double.parseDouble(expressao.get(i + 1));

            switch (operacao) {
                case "+":
                    resultado += numero;
                    break;
                case "-":
                    resultado -= numero;
                    break;
                case "*":
                    resultado *= numero;
                    break;
                case "/":
                    if (numero == 0) {
                        throw new ArithmeticException("Divisão por zero");
                    }
                    resultado /= numero;
                    break;
                default:
                    throw new IllegalArgumentException("Operação inválida: " + operacao);
            }
        }

        return resultado;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculadoraSwingClient calculadora = new CalculadoraSwingClient();
            calculadora.setVisible(true);
        });
    }
}
