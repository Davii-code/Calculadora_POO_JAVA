import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculadoraSwingClient extends JFrame {
    private final JTextField campoDeExibicao;
    private final JPanel painelDeBotoes;
    private final JButton[] botoesNumericos;
    private final JButton botaoSoma;
    private final JButton botaoSubtracao;
    private final JButton botaoMultiplicacao;
    private final JButton botaoDivisao;
    private final JButton botaoIgual;
    private final JButton botaoLimpar;
    private final JLabel rotuloResultado;
    private final JButton botaoUsarResultado;

    private final CalculadoraInterface calculadora;

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

        botaoSoma = criarBotaoOperacao("+");
        botaoSubtracao = criarBotaoOperacao("-");
        botaoMultiplicacao = criarBotaoOperacao("*");
        botaoDivisao = criarBotaoOperacao("/");
        botaoIgual = new JButton("=");
        botaoIgual.setFont(new Font("Arial", Font.PLAIN, 18));
        botaoIgual.addActionListener(new ListenerBotaoIgual());
        botaoLimpar = new JButton("C");
        botaoLimpar.setFont(new Font("Arial", Font.PLAIN, 18));
        botaoLimpar.addActionListener(new ListenerBotaoLimpar());
        // Adicione o botão ao painel de botões
        botaoUsarResultado = new JButton("Usar Resultado");
        botaoUsarResultado.setFont(new Font("Arial", Font.PLAIN, 18));
        botaoUsarResultado.addActionListener(new ListenerBotaoUsarResultado());

        painelDeBotoes.add(botaoUsarResultado);
        painelDeBotoes.add(botaoSoma);
        painelDeBotoes.add(botaoSubtracao);
        painelDeBotoes.add(botaoMultiplicacao);
        painelDeBotoes.add(botaoDivisao);
        painelDeBotoes.add(botaoIgual);
        painelDeBotoes.add(botaoLimpar);

        rotuloResultado = new JLabel("");
        rotuloResultado.setFont(new Font("Arial", Font.PLAIN, 24));

        // Crie uma instância de Calculadora e um adaptador
        Calculadora calculadoraOriginal = new Calculadora();
        calculadora = new CalculadoraAdapter(calculadoraOriginal);

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
            campoDeExibicao.setText(campoDeExibicao.getText() + numero);
        }
    }

    private class ListenerBotaoOperacao implements ActionListener {
        private final String operacao;

        public ListenerBotaoOperacao(String operacao) {
            this.operacao = operacao;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            calculadora.adicionarNumero(campoDeExibicao.getText());
            calculadora.adicionarOperacao(operacao);
            campoDeExibicao.setText("");
        }
    }

    private class ListenerBotaoUsarResultado implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int resultadoAnterior = calculadora.getResultadoAnterior();
            campoDeExibicao.setText(String.valueOf(resultadoAnterior));
            calculadora.limpar();
        }
    }


    private class ListenerBotaoIgual implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            calculadora.adicionarNumero(campoDeExibicao.getText());
            calculadora.calcularResultado();
            int resultado = calculadora.getResultado();
            campoDeExibicao.setText("Resultado: " + resultado);
        }
    }

    private class ListenerBotaoLimpar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            campoDeExibicao.setText("");
            rotuloResultado.setText("");
            calculadora.limpar();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculadoraSwingClient calculadora = new CalculadoraSwingClient();
            calculadora.setVisible(true);
        });
    }
}
