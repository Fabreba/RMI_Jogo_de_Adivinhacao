// JogoAdivinhacao.java
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoAdivinhacao extends Remote {
    void registrarJogador(String nome, IClienteCallback callback) throws RemoteException;
    void desregistrarJogador(String nome) throws RemoteException;
    String enviarPalpite(String nome, int palpite) throws RemoteException;
    void novoJogo() throws RemoteException;
}