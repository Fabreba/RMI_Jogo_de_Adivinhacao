// ServidorAdivinhacao.java
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class ServidorAdivinhacao extends UnicastRemoteObject implements JogoAdivinhacao {

    public static final int NUMERO_MAXIMO = 100;
    private Map<String, Jogador> jogadores;
    private List<IClienteCallback> callbacks;
    private int numeroSecreto;

    protected ServidorAdivinhacao() throws RemoteException {
        super();
        jogadores = new HashMap<>();
        callbacks = new ArrayList<>();
        numeroSecreto = new Random().nextInt(NUMERO_MAXIMO) + 1;
        System.out.println("Servidor rodando...");
    }

    @Override
    public void registrarJogador(String nome, IClienteCallback callback) throws RemoteException {
        jogadores.put(nome, new Jogador(nome));
        callbacks.add(callback);
        System.out.println(nome + " registrado no jogo.");
    }

    @Override
    public void desregistrarJogador(String nome) throws RemoteException {
        jogadores.remove(nome);
        System.out.println(nome + " saiu do jogo");
    }

    @Override
    public String enviarPalpite(String nome, int palpite) throws RemoteException {
        Jogador jogador = jogadores.get(nome);
        System.out.println(nome + " palpitou o número: " + palpite);
        jogador.incrementarTentativas();

        if (palpite == numeroSecreto) {
            String mensagem = "Parabéns, " + nome + "! Você adivinhou o número em " + jogador.getTentativas() + " tentativas.";
            enviarMensagemParaTodos("O jogador " + nome + " venceu!");
            novoJogo();
            return mensagem;
        } else if (palpite > numeroSecreto) {
            return "O número secreto é menor que " + palpite;
        } else {
            return "O número secreto é maior que " + palpite;
        }
    }

    @Override
    public void novoJogo() throws RemoteException {
        numeroSecreto = new Random().nextInt(NUMERO_MAXIMO) + 1;
        for (Jogador jogador : jogadores.values()) {
            jogador.setTentativas(0);
        }
    }

    public void enviarMensagemParaTodos(String mensagem) throws RemoteException {
        for (IClienteCallback callback : callbacks) {
            callback.notificar(mensagem);
        }
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ServidorAdivinhacao servidor = new ServidorAdivinhacao();
            Naming.rebind("ServidorAdivinhacao", servidor);
            System.out.println("Servidor de Adivinhação pronto.");
        } catch (Exception e) {
            System.out.println("Servidor de Adivinhação falhou: " + e);
        }
    }
}

class Jogador {

    private String nome;
    private int tentativas;

    public Jogador(String nome) {
        this.nome = nome;
        this.tentativas = 0;
    }

    public int getTentativas() {
        return tentativas;
    }

    public String getNome(){
        return nome;
    }

    public void incrementarTentativas() {
        tentativas++;
    }

    public void setTentativas(int tentativas) {
        this.tentativas = tentativas;
    }

    public void receberMensagem(String mensagem) {
        System.out.println("Mensagem para " + nome + ": " + mensagem);
    }
}
