package clientpackage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import common.Operation;

public class Client2 {
    static final String IP_SERVEUR = "localhost";
    static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByName(IP_SERVEUR), PORT), 5000);
            System.out.println("--- CLIENT 2 (Calculatrice) ---");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner clavier = new Scanner(System.in);

            System.out.println("Serveur : " + ois.readObject());

            while (true) {
                System.out.print("Entrez num1 : ");
                double n1 = Double.parseDouble(clavier.nextLine());
                System.out.print("Entrez l'opérateur (+,-,*,/) : ");
                char op = clavier.nextLine().charAt(0);
                System.out.print("Entrez num2 : ");
                double n2 = Double.parseDouble(clavier.nextLine());

                oos.writeObject(new Operation(n1, n2, op));
                oos.flush();

                System.out.println("Serveur : " + ois.readObject());

                System.out.print("Continuer ? (quit pour quitter) : ");
                if (clavier.nextLine().equals("quit")) {
                    oos.writeObject("quit");
                    oos.flush();
                    break;
                }
            }
            clavier.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}