package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import server.models.Course;
import server.models.RegistrationForm;

/**
 * La classe Client permet de se connecter au serveur, de recevoir des
 * données du serveur et d'envoyer des données à celui-ci, pour obtenir la liste de cours disponibles
 * pour une session désirée et de s'inscrire à un de ces cours.
 */
public class Client {

    /**
     * Pour envoyer des données au serveur.
     */
    ObjectOutputStream oos;
    /**
     * Pour recevoir des données du serveur.
     */
    ObjectInputStream ois;
    /**
     * Le socket pour la connexion avec le serveur.
     */
    Socket clientSocket;

    /**
     * Cette méthode permet de recevoir et d'afficher la liste de cours du fichier cours.txt pour une
     * session donnée en envoyant une requête au serveur.
     * Le client interagit en choisissant la session, pour y voir les cours disponibles.
     * @param scan  Pour lire les informations entrées par le client.
     * @return un ArrayList de Course contenant les différents cours disponibles pour la session désirée
     * @throws IOException  Erreur d'entrée/sortie en général.
     */
    public ArrayList<Course> charger(Scanner scan) throws IOException{
        String messageConsultation = "Veuillez choisir la session pour " +
        "laquelle vous voulez consulter la liste des cours: \n 1.Automne \n " +
        "2.Hiver \n 3.Été \n > Choix: ";

        System.out.print(messageConsultation);

        clientSocket = new Socket("127.0.0.1",80);
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());

        String LOAD_COMMAND = "CHARGER ";

        
            String line = scan.nextLine();


            oos.writeObject(LOAD_COMMAND + line);
            oos.flush();
            switch(line){
                
                case "1": line = "automne"; break;

                case "2": line = "hiver"; break;

                case "3": line = "été"; break;
            }

            System.out.println("Les cours offerts pour la session " +
                "d'" + line + " sont:");

            try{
                @SuppressWarnings("unchecked") 
                ArrayList<Course> liste_cours = (ArrayList<Course>)ois.readObject();


                for (Course cours : liste_cours){
                    System.out.print(cours.getCode() + "\t" + cours.getName() +"\t");
                }
                System.out.print("\n");

                return liste_cours;
                
            }catch(ClassNotFoundException e){
                System.out.println("Erreur classe non-trouvée.");
            }

        return null;
    }

    /**
     * La méthode permet de faire l'inscription d'un individu à un cours désiré en envoyant une
     * requête au serveur. La méthode permet également de valider
     * l'adresse courriel et le matricule de l'individu ainsi que de vérifier que le cours sélectionné
     * est disponible.
     * @param scan  Pour lire les informations entrée par le client.
     * @param liste_cours   Un ArrayList de Course pour la session désirée.
     * @throws IOException  Erreur d'entrée/sortie en général.
     */
    public void inscrire(Scanner scan, ArrayList<Course> liste_cours)throws IOException{
        clientSocket = new Socket("127.0.0.1",80);
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());

        String REGISTER_COMMAND = "INSCRIRE";

        oos.writeObject(REGISTER_COMMAND );
        oos.flush();

        System.out.print("Veuillez saisir votre prénom: ");

        String prenom = scan.nextLine();

        System.out.print("Veuillez saisir votre nom: ");

        String nom = scan.nextLine();



        //On assume que le email n'est pas valide jusqu'à preuve du contraire.

        boolean emailValide = false;

        String email = "temporaire";


        while (emailValide==false){

            System.out.print("Veuillez saisir votre email: ");

            String emailPossible = scan.nextLine();

            String[] emailVerif = emailPossible.split("@");

            if (emailVerif.length==2){

                String[] emailVerif2=emailVerif[1].split("\\.");

                if (emailVerif2.length==2 && emailVerif2[0] != null && emailVerif2[1] != null){
                    email=emailPossible;
                    emailValide = true;
                }
                else {
                    System.out.println("Email invalide.");
                }
            }
            else {
                System.out.println("Email invalide.");
            }
        }



        //On assume que le matricule n'est pas valide jusqu'à preuve du contraire.

        boolean matriculeValide = false;

        String matricule = "Temporaire";

        while (matriculeValide==false){

            System.out.print("Veuillez saisir votre matricule: ");

            String matriculePossible = scan.nextLine();

            // Vérification que c'est bien un entier de 8 caractères:
            try {
                Integer.parseInt(matriculePossible);
            } catch (NumberFormatException e) {
                System.out.println("Matricule invalide.");
                continue;
            }

            if (matriculePossible.length()==8){

                matricule=matriculePossible;
                matriculeValide=true;
            }
            else {
                System.out.println("Matricule invalide.");
            }
        }

        //On assume que le code du cours est invalide jusqu'à preuve du contraire.

        boolean codeValide =false;

        while (codeValide==false){

            System.out.print("Veuillez saisir le code du cours: ");

            String codePossible = scan.nextLine();

            //Verifier si c'est la liste de tous les cours ou ceux de la session seulement
            for(Course cours: liste_cours){
                if (cours.getCode().equals(codePossible)){

                    String code=codePossible;
                    String nomCours= cours.getName();
                    String session = cours.getSession();
                    Course course = new Course(nomCours, code, session);
                    RegistrationForm rf=new RegistrationForm(prenom, nom, email, matricule, course);
                    oos.writeObject(rf);
                    oos.flush();
                    System.out.println("Félicitations! Inscription réussie de " + prenom + " au cours " + code + " .");
                    codeValide=true;
                }

            }
            if (codeValide==false){
                System.out.println("Le cours ne fait pas partie de liste de cours disponible.");
            }
        }

    }

    /**
     * Méthode pour fermer la connexion avec le serveur et celles des streams.
     * @throws IOException  Erreur d'entrée/sortie en général.
     */
    public void deconnecter() throws IOException{
        oos.close();
        ois.close();
        clientSocket.close();
    }

    /**
     * Initialisation du client et permet de consulter la liste de cours de la session désirée pour
     * par la suite donner le choix entre une nouvelle consultation ou une inscription.
     * @param args Arguments passés en lignes de commandes.
     */

    public static void main(String[] args){
        Client client = new Client();
        Scanner scan = new Scanner(System.in);

        System.out.println("*** Bienvenue au portail d'inscription de cours " +
        "de l'UDEM ***");
        
        try {

            ArrayList<Course> listeCours = client.charger(scan);
            System.out.println("> Choix: ");
            System.out.print("1. Consulter les cours offerts pour une " +
            "autre session \n2. Inscription à un cours: \n > Choix: ");

            while (scan.hasNext()){

                String choix = scan.nextLine();


                if (choix.equals("1")){
                    //réassigner nouvelle valeur
                    listeCours = client.charger(scan);
                }
                if (choix.equals("2")){
                    client.inscrire(scan, listeCours);
                    break;
                }

                System.out.println("> Choix: ");
                System.out.print("1. Consulter les cours offerts pour une " +
                "autre session \n2. Inscription à un cours: \n > Choix: ");
                
            }
            scan.close();
            client.deconnecter();
            

        } catch(ConnectException e){
            System.out.println("Erreur à la connexion.");
        } catch (IOException e) {
            System.out.println("Erreur à l'écriture de l'objet");
        } 

    }
}
