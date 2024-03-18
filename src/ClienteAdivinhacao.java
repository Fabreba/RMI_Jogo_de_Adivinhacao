// ClienteAdivinhacao.java
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClienteAdivinhacao extends UnicastRemoteObject implements IClienteCallback {

    protected ClienteAdivinhacao() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Digite seu nome:");
            String nome = scanner.next();

            JogoAdivinhacao servidor = (JogoAdivinhacao) Naming.lookup("//localhost/ServidorAdivinhacao");
            ClienteAdivinhacao cliente = new ClienteAdivinhacao();
            servidor.registrarJogador(nome, cliente);

            int palpite;
            while (true) {
                System.out.print("Digite seu palpite (1-" + ServidorAdivinhacao.NUMERO_MAXIMO + "): ");
                palpite = scanner.nextInt();
                if (palpite == -1) {
                    servidor.desregistrarJogador(nome);
                    System.out.println("Você saiu do jogo.");
                    break;
                }
                String resposta = servidor.enviarPalpite(nome, palpite);
                System.out.println(resposta);
            }
            scanner.close();
        } catch (Exception e) {
            System.err.println("Cliente falhou: " + e);
        }
    }

    @Override
    public void notificar(String mensagem) throws RemoteException {
        System.out.println("Notificação: " + mensagem);
    }
}