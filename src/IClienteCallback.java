// IClienteCallback.java
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClienteCallback extends Remote {
    void notificar(String mensagem) throws RemoteException;
}