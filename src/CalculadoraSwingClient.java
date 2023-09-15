import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// Interface original que representa a calculadora
interface CalculadoraInterface {
    void adicionarNumero(String numero);
    void adicionarOperacao(String operacao);
    void calcularResultado();
    void limpar();
    int getResultado();
}

// Classe que representa a calculadora
class Calculadora implements CalculadoraInterface {
    private String entradaAtual = "";
    private String operacaoAtual = "";
    private int resultadoAtual = 0;

    public void adicionarNumero(String numero) {
        entradaAtual += numero;
    }

    public void adicionarOperacao(String operacao) {
        if (!entradaAtual.isEmpty()) {
            operacaoAtual = operacao;
            entradaAtual += operacao;
        }
    }

    public void calcularResultado() {
        if (!entradaAtual.isEmpty() && !operacaoAtual.isEmpty()) {
            List<Integer> numeros = parseEntrada(entradaAtual);

            if (numeros.size() < 2) {
                // Lidar com erro
                return;
            }

            int porta = getPortaParaOperacao(operacaoAtual);
            try (Socket socket = new Socket("127.0.0.1", porta)) {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                outputStream.writeInt(numeros.size());
                for (int num : numeros) {
                    outputStream.writeInt(num);
                }

                int resultado = inputStream.readInt();

                // Atualizar o resultado atual
                resultadoAtual = resultado;
            } catch (IOException ex) {
                
            }
        }
    }

    public void limpar() {
        entradaAtual = "";
        operacaoAtual = "";
    }

    public int getResultado() {
        return resultadoAtual;
    }

    private List<Integer> parseEntrada(String entrada) {
        String[] partes = entrada.split("[+\\-*/]");
        List<Integer> numeros = new ArrayList<>();

        for (String parte : partes) {
            try {
                int num = Integer.parseInt(parte.trim());
                numeros.add(num);
            } catch (NumberFormatException e) {
                // Ignorar entradas não numéricas
            }
        }

        return numeros;
    }

    private int getPortaParaOperacao(String operacao) {
        switch (operacao) {
            case "+":
                return 12345; // Porta do ServerMais
            case "-":
                return 12346; // Porta do ServerMenos
            case "*":
                return 12347; // Porta do ServerMulti
            case "/":
                return 12348; // Porta do ServerDivisao
            default:
                throw new IllegalArgumentException("Operação inválida");
        }
    }
}

// Adaptador que conecta a interface da calculadora com a Calculadora original
class CalculadoraAdapter implements CalculadoraInterface {
    private final Calculadora calculadora;

    public CalculadoraAdapter(Calculadora calculadora) {
        this.calculadora = calculadora;
    }

    public void adicionarNumero(String numero) {
        calculadora.adicionarNumero(numero);
    }

    public void adicionarOperacao(String operacao) {
        calculadora.adicionarOperacao(operacao);
    }

    public void calcularResultado() {
        calculadora.calcularResultado();
    }

    public void limpar() {
        calculadora.limpar();
    }

    public int getResultado() {
        return calculadora.getResultado();
    }
}

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
            int resultado = calculadora.getResultado();
            campoDeExibicao.setText(String.valueOf(resultado));
        }
    }

    private class ListenerBotaoIgual implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            calculadora.adicionarNumero(campoDeExibicao.getText());
            calculadora.calcularResultado();
            int resultado = calculadora.getResultado();
            rotuloResultado.setText("Resultado: " + resultado);
            campoDeExibicao.setText("");
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
