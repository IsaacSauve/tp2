package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import server.models.Course;
import server.models.RegistrationForm;

public class Client2 {

    ObjectOutputStream oos; 
    ObjectInputStream ois; 
    Socket clientSocket;
    
    
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

                //*********************
                return liste_cours;
                
            }catch(ClassNotFoundException e){
                System.out.println("Erreur classe non-trouvée.");
            }

        //*********************
        return null;
    }
    public void inscrire(Scanner scan, ArrayList<Course> liste_cours)throws IOException{
        clientSocket = new Socket("127.0.0.1",80);
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());

        String REGISTER_COMMAND = "INSCRIRE";

        oos.writeObject(REGISTER_COMMAND );
        oos.flush();

        System.out.println("Veuillez saisir votre prénom: ");

        String prenom = scan.nextLine();

        System.out.println("Veuillez saisir votre nom: ");

        String nom = scan.nextLine();



        //On assume que le email n'est pas valide jusqu'à preuve du contraire.

        boolean emailValide = false;

        String email = "temporaire";


        while (emailValide==false){

            System.out.println("Veuillez saisir votre email: ");

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

            System.out.println("Veuillez saisir votre matricule: ");

            String matriculePossible = scan.nextLine();

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

        //String code = "Temporaire";

        while (codeValide==false){

            System.out.println("Veuillez saisir le code du cours: ");

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

    public void deconnecter() throws IOException{
        oos.close();
        ois.close();
        clientSocket.close();
    }

    public static void main(String[] args){
        Client2 client = new Client2();
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
                }

                if (choix.equals("exit")){
                    break;    
                }
                System.out.println("> Choix: ");
                System.out.print("1. Consulter les cours offerts pour une " +
                "autre session \n 2. Inscription à un cours: \n > Choix: ");
                
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
