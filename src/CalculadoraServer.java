import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculadoraServer {
    public final static int PORT = 4000;
    private ServerSocket serverSocket;
    
    public  void start() throws IOException{
        serverSocket = new ServerSocket(PORT);
        LoopServer();
    }

    private void LoopServer() throws IOException{
        while (true){
           Socket calcclienteSocket = serverSocket.accept();
           System.out.println("Cliente conectou" + calcclienteSocket.getRemoteSocketAddress());
        }

    }
    public static void main(String[] args) {
       
        try {
             CalculadoraServer server = new CalculadoraServer();
             server.start();
             System.out.println("Servidor iniciado com sucesso");
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o seridor");
        }
          System.out.println("Servidor Finalizado");
    }
  
}

