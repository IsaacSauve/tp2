package server;

import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

import server.models.Course;
import server.models.RegistrationForm;
/**
 * La classe serveur nous permet d'établir la connexion avec un client, d'attendre les commandes de
 * celui-ci, de lister les différents cours disponibles selon la session désirée et
 * d'exécuter l'inscription d'un étudiant.
 */
public class Server {

    /**
     * Chaîne de caractères pour l'inscription
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * Chaîne de caractères pour le chargement des cours
     */
    public final static String LOAD_COMMAND = "CHARGER";
    /**
     * Le Socket pour ouvrir une connexion au serveur
     */
    private final ServerSocket server;
    /**
     * Le Socket pour la connexion avec le client
     */
    private Socket client;
    /**
     * Objet pour lire des informations d'un fichier de cours ou d'inscriptions
     */
    private ObjectInputStream objectInputStream;
    /**
     * Objet pour écrire dans le fichier d'inscriptions
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * La liste de manipulateurs d'événements
     */
    private final ArrayList<EventHandler> handlers;

    /**
     *Constructeur permettant l'initialisation de la connexion au serveur sur le 
     *port passé en paramètre, de créer le tableau allant contenir les 
     *"EventHandler" et ajouter ceux-ci dans le tableau "handlers".
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
     * @param arg   Une chaîne de caractères représentant pour quelle session le
     *  client souhaite que la commande soit exécutée.
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Méthode qui attend qu'une connexion soit établit avec un client, pour 
     * ensuite afficher des informations sur le client, recevoir des données de 
     * celui-ci et en renvoyer en fonction des données reçues par le client. Ce 
     * processus va continuer tant que le client est connecté. Dès que le client 
     * se déconnecte, on affiche la chaîne de caractère "Client déconnecté" et 
     * le serveur se ferme. De plus, si une exception survient, elle est 
     * attrapée.
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
     * Méthode permettant le démarrage de la boucle d'évènement en attendant 
     * qu'une connexion avec un client soit établie.
     *
     * @throws IOException  Erreur d'entrée/sortie en général.
     * @throws ClassNotFoundException   La classe demandée (RegistrationForm) 
     * n'a pas pu être trouvée.
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
     * La méthode reçoit une ligne de commande du client et sépare la commande
     * et les arguments pour créer une paire contenant ces deux éléments.
     *
     * @param line Chaîne de caractères reçu par le client que l'on doit 
     * interpréter.
     * @return  La commande qui doit être exécutée et la session pour 
     * laquelle la commande va etre exécutée.
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }
    /**
     * Méthode pour fermer toutes les connexions, c'est-à-dire celle du client
     * et celles des "streams". 
     * 
     * @throws IOException Une erreur en cas de fermeture des connexions
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }
    /**
     * La méthode fait l'appel à la fonction correspondante, soit pour inscrire
     * un client ou pour charger la liste de cours.
     * 
     * @param cmd Correspond à la demande faite par le client (Inscrire ou 
     * charger)
     * @param arg Correspond à la session dans le cas du chargement de cours
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transformer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        // TODO: implémenter cette méthode
        try {
            Scanner scan = new Scanner(new File("server\\data\\cours.txt"));
            ArrayList<Course> liste_cours = new ArrayList<Course>();
            while (scan.hasNextLine()){
                String ligne = scan.nextLine();

                String[] data_cours = ligne.split("\t",3);
                liste_cours.add(new Course(data_cours[1], data_cours[0],data_cours[2]));
            }

 
            for (Course cours : liste_cours){
                if (cours.getSession().equals(arg)){
                    try {
                        this.objectOutputStream.writeObject(cours);
                    } catch (IOException e){
                        System.out.println("Erreur à l'écriture de l'objet.");
                    }
                }
            }
            scan.close();
        } catch  (FileNotFoundException e) {
            System.out.println("Erreur. Fichier non-trouvé.");
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        // TODO: implémenter cette méthode
        try {
            RegistrationForm rf = (RegistrationForm) this.objectInputStream.readObject();
            
            try {
                FileOutputStream fw = new FileOutputStream("server\\data\\inscription.txt",true);
                
                String inscription = rf.getCourse().getSession() + "\t" 
                + rf.getCourse().getCode() + "\t" + rf.getMatricule() + "\t" 
                + rf.getPrenom() + "\t" + rf.getNom() + "\t" + rf.getEmail() + "\n";

                fw.write(inscription.getBytes());
                fw.close();

            } catch (IOException e) {
                System.out.println("Erreur à l'écriture du fichier");
            }

        } catch (IOException e) {
            System.out.println("Erreur à la lecture de l'objet");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur. La classe n'a pas été trouvée.");
        }
        
    }
    public static void main(String[] args) throws IOException{
        Server server = new Server(80);
        server.run();
    }
}

