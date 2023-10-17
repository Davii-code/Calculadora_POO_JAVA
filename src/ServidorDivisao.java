import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorDivisao {
    public static void main(String[] args) throws IOException {
        int porta = 12348;
        ServerSocket servidorSocket = new ServerSocket(porta);
        System.out.println("Servidor de Divisão está ouvindo na porta " + porta);

        while (true) {
            Socket socket = servidorSocket.accept();
            System.out.println("Cliente conectado: " + socket);

            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());

            int quantidadeNumeros = entrada.readInt();
            List<Integer> numeros = new ArrayList<>();

            for (int i = 0; i < quantidadeNumeros; i++) {
                int numero = entrada.readInt();
                numeros.add(numero);
            }

            int resultado = divisao(numeros);

            saida.writeInt(resultado);

            socket.close();
            System.out.println("Cliente desconectado: " + socket);
        }
    }

    private static int divisao(List<Integer> numeros) {
        if (numeros.size() < 2) {
            throw new IllegalArgumentException("Pelo menos dois números são necessários para a divisão.");
        }

        int resultado = numeros.get(0);
        for (int i = 1; i < numeros.size(); i++) {
            if (numeros.get(i) == 0) {
                throw new ArithmeticException("Divisão por zero encontrada. Não é possível dividir por zero.");
            }
            if (resultado % numeros.get(i) != 0) {
                throw new ArithmeticException("O resultado da divisão não é um número inteiro.");
            }
            resultado /= numeros.get(i);
        }
        return resultado;
    }
}

