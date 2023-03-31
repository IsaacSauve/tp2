package server;
/**
 * La classe ServerLauncher fait le lancement du serveur avec la méthode main.
 */
public class ServerLauncher {
    /**
     * Le numéro de port pour le serveur.
     */
    public final static int PORT = 1337;

    /**
     * La méthode crée un serveur et l'exécute. Elle attrape les exceptions
     * possibles à l'initialisation ou à l'exécution du serveur et 
     * 
     * @param args Les arguments reçus par la méthode principale (main)
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}