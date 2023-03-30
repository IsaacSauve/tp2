package server;

import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * La classe serveur nous permet d'établir la connexion avec un client, d'attendre les commandes de
 * celui-çi, de lister les différents cours disponibles selon la session désirée et
 * d'exécuter l'inscription d'un étudiant.
 */
public class Server {

    /**
     * Chaînes de caractères permettant  de reconnaître quelle commande
     * doit être exécuter.
     */

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";

    /**
     * L'objet Socket va nous permettre d'ouvrir une connexion
     * sur notre serveur.
     */
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     *Constructeur permettant l'initialisation de la connexion au serveur sur le port passé en paramètre, de créer le
     *tableau allant contenir les "EventHandler" et ajouter ceux-ci dans le tableau "handlers".
     *
     * @param port  Port sur lequel le serveur va être créé.
     * @throws IOException  Erreur d'entrée/sortie en générale.
     */

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Cette méthode ajoute l'EventHandler mis en paramètre à l'ArrayList nommé "handlers".
     *
     * @param h     L'EventHandler qui doit être ajouté à l'ArrayList "handlers".
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Cette méthode informe les "EventHandlers" que la commande reçu en paramètre doit être traîtée.
     *
     * @param cmd   Une chaîne de caractères représentant la commande du client.
     * @param arg   Une chaîne de caractères représentant pour quelle session le client souhaite que la commande
     *              soit exécutée.
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Méthode qui attend qu'une connexion soit établit avec un client, pour ensuite afficher des informations sur le
     * client, recevoir des données de celui-ci et en renvoyer en fonction des données reçues par le client.
     * Ce processus va continuer tant que le client est connecté.
     * Dès que le client se déconnecte, on affiche la chaîne de caractère "Client déconnecté" et le serveur se ferme.
     * De plus, si une exception survient, elle est attrapée.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode permettant le démarrage de la boucle d'évènement en attendant qu'une connexion avec un client soit
     * établit.
     *
     * @throws IOException  Erreur d'entrée/sortie en générale.
     * @throws ClassNotFoundException   La classe demandé n'a pas pu être trouvé. (+++)
     */

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Méthode qui va  créer le tableau "parts" avec la chaîne de caractères reçu en argument, où chaque case
     * va contenir un mot de cette chaîne, pour par la suite déclarer(?) la chaîne de caractère "cmd" par le mot à
     * l'index 0 du tableau "parts" et joindre toutes les cases sauf celle à l'index 0, avec des espaces entre chaque
     * mot qui va être utilisé pour déclarer la chaîne de caractère "args".
     * La méthode se termine en retournant une nouvelle paire(?) avec "cmd" et "args" en paramètre.
     *
     * @param line Chaîne de caractères reçu par le client que l'on doit interpréter.
     * @return  La commande qui doit être exécutée et la session pour laquelle la commande va etre exécutée.
     *
     * ??? Est-ce que args peut être autre chose que des session???
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        // TODO: implémenter cette méthode
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        // TODO: implémenter cette méthode
    }
}

