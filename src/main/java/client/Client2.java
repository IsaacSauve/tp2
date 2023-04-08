package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import server.models.Course;

public class Client2 {

    ObjectOutputStream oos; 
    ObjectInputStream ois; 
    Socket clientSocket;
    
    
    public void charger(Scanner scan) throws IOException{
        String messageConsultation = "Veuillez choisir la session pour " +
        "laquelle vous voulez consulter la liste des cours: \n 1.Automne \n " +
        "2.Hiver \n 3.Été \n > Choix: ";

        System.out.print(messageConsultation);

        clientSocket = new Socket("127.0.0.1",80);
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());

        String LOAD_COMMAND = "CHARGER ";

        while(scan.hasNext()){
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
                
            }catch(ClassNotFoundException e){
                System.out.println("Erreur classe non-trouvée.");
            }

        }
        
        
        
    }
    public void inscrire(){
        // TODO faire l'inscription!
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
        System.out.println("> Choix: ");
        System.out.print("1. Consulter les cours offerts pour une " +
        "autre session \n 2. Inscription à un cours: \n > Choix: ");

        String choix = scan.nextLine();
        
        
        try {

            if (choix.equals("1")){
                client.charger(scan);
            }
            if (choix.equals("2")){
                client.inscrire();
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
