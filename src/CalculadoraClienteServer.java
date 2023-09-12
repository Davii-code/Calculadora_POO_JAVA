import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CalculadoraClienteServer {
    private Socket clienSocket;
    private final static String SERVER_ADD = "127.0.0.1";
    private Scanner scanner;

    public CalculadoraClienteServer(){
        scanner = new Scanner (System.in);
    }

    public void start () throws UnknownHostException, IOException{
        clienSocket = new Socket(SERVER_ADD, CalculadoraServer.PORT);
        calculo();
    }


    private void calculo(){
      String num;
      do{
        System.out.println("Digite um numero");
        num = scanner.nextLine();
      }while(!num.equalsIgnoreCase("0"));
    }
    public static void main(String[] args) {
        try {
            CalculadoraClienteServer CalculadoraCliente = new CalculadoraClienteServer();
            CalculadoraCliente.start();
            System.out.println("Servidor iniciado");
        } catch (IOException e) {
           System.out.println("Servidor não iniciado");
        }
    }

}
