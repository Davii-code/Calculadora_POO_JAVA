import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorSoma {
    public static void main(String[] args) throws IOException {
        int porta = 12345;
        ServerSocket servidorSocket = new ServerSocket(porta);
        System.out.println("Servidor de Soma está ouvindo na porta " + porta);

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

            int resultado = numeros.stream().mapToInt(Integer::intValue).sum();

            saida.writeInt(resultado);

            socket.close();
            System.out.println("Cliente desconectado: " + socket);
        }
    }
}
