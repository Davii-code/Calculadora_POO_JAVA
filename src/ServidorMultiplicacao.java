import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorMultiplicacao {
    public static void main(String[] args) throws IOException {
        int porta = 12347;
        ServerSocket servidorSocket = new ServerSocket(porta);
        System.out.println("Servidor de Multiplicação está ouvindo na porta " + porta);

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

            int resultado = multiplicacao(numeros);

            saida.writeInt(resultado);

            socket.close();
            System.out.println("Cliente desconectado: " + socket);
        }
    }

    private static int multiplicacao(List<Integer> numeros) {
        int resultado = 1;
        for (int numero : numeros) {
            resultado *= numero;
        }
        return resultado;
    }
}
