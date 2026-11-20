package serverpackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import common.Operation;

public class ClientProcess extends Thread {
    private Socket socket;
    private int numero;

    public ClientProcess(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject("Bienvenue, vous êtes le client n°" + numero);
            oos.flush();

            while (true) {
                Object obj = ois.readObject();

                if (obj instanceof Operation) {
                    Operation op = (Operation) obj;
                    double result = 0;

                    switch (op.operator) {
                        case '+': result = op.num1 + op.num2; break;
                        case '-': result = op.num1 - op.num2; break;
                        case '*': result = op.num1 * op.num2; break;
                        case '/':
                            if(op.num2 != 0) result = op.num1 / op.num2;
                            else {
                                oos.writeObject("Erreur : Division par zéro !");
                                oos.flush();
                                continue;
                            }
                            break;
                    }

                    // SYNCHRONIZED COUNTER
                    synchronized (Server_MT.class) {
                        Server_MT.operationsCounter++;
                        System.out.println("[Serveur] Opération traitée par client n°" + numero
                                + ". Total global : " + Server_MT.operationsCounter);
                    }

                    oos.writeObject("Résultat : " + result);
                    oos.flush();

                } else if (obj instanceof String && obj.equals("quit")) {
                    break;
                }
            }
            System.out.println("Client n°" + numero + " déconnecté.");
        } catch (Exception e) {
            System.out.println("Erreur client n°" + numero + " : " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException e) { }
        }
    }
}