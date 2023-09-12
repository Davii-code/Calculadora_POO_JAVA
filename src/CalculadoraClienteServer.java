import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CalculadoraClienteServer {
    public static Socket clienSocket;
    private final static String SERVER_ADD = "127.0.0.1";
    private Scanner scanner;

    public CalculadoraClienteServer(){
        scanner = new Scanner (System.in);
    }

    public void start () throws UnknownHostException, IOException{
        clienSocket = new Socket(SERVER_ADD, CalculadoraServer.PORT);
        calculo();
    }


    private void calculo() throws IOException{
        int num;
      do{
        System.out.println("Digite um numero");
        num = scanner.nextInt();
        DataOutputStream numero = new DataOutputStream(clienSocket.getOutputStream());
        numero.writeInt(num);
      }while(num != 0);
      DataInputStream entradaNum = new DataInputStream(clienSocket.getInputStream());
      int prNum = entradaNum.readInt();
      System.out.println("valor da operacão: " + prNum );
    }


    public static void main(String[] args) {
        try {
            CalculadoraClienteServer CalculadoraCliente = new CalculadoraClienteServer();
            CalculadoraCliente.start();
        } catch (IOException e) {
           System.out.println("Servidor não iniciado");
        }

    }

}
