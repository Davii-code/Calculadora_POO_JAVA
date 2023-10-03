import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Calculadora implements CalculadoraInterface {
    private String entradaAtual = "";
    private String operacaoAtual = "";
    private int resultadoAtual = 0;
    private int resultadoAnterior = 0;



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
                resultadoAtual = resultado;
                // Atualizar o resultado anterior e o resultado atual
                resultadoAnterior = resultadoAtual;

            } catch (IOException ex) {
                // Lidar com exceções
            }
        }
    }


    public void limpar() {
        entradaAtual = "";
        operacaoAtual = "";
        resultadoAnterior = 0; // Limpar o resultado anterior
    }

    public int getResultado() {
        return resultadoAtual;
    }

    public int getResultadoAnterior() {
        return resultadoAnterior;
    }

    private List<Integer> parseEntrada(String entrada) throws NumberFormatException {
        String[] partes = entrada.split("[+\\-*/]");
        List<Integer> numeros = new ArrayList<>();

        for (String parte : partes) {
            int num = Integer.parseInt(parte.trim());
            numeros.add(num);
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
