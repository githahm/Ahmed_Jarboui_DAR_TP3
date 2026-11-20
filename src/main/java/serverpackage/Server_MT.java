package serverpackage;


import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServeurMultiThread {

    public static final int PORT = 1234;

    // Compteur global thread-safe du nombre de clients connectés
    private static final AtomicInteger compteurClients = new AtomicInteger(0);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur multi-thread démarré sur le port " + PORT);
            System.out.println("En attente de clients...\n");

            // Boucle infinie : accepte autant de clients que nécessaire
            while (true) {
                Socket socketClient = serverSocket.accept();

                // Numéro d'ordre de connexion
                int numero = compteurClients.incrementAndGet();

                // (b) Récupération de l'adresse IP du client
                String adresseIP = socketClient.getRemoteSocketAddress().toString();

                // (b) Affichage côté serveur
                System.out.println("Client n°" + numero + " connecté depuis " + adresseIP);

                // Création d'un thread dédié à ce client
                Thread t = new Thread(new ClientProcess(socketClient, numero));
                t.start();
            }

        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
        }
    }
}
