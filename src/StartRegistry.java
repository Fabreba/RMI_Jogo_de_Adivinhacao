import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StartRegistry {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("Registro RMI criado na porta 1099");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}