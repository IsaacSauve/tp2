package client;

import java.io.*;
import java.net.Socket;
import java.rmi.ConnectException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args){
        System.out.println("*** Bienvenue au portail d'inscription de cours " +
        "de l'UDEM ***");

        String messageConsultation = "Veuillez choisir la session pour " +
        "laquelle vous voulez consulter la liste des cours: \n 1.Automne \n " +
        "2.Hiver \n 3.Été \n > Choix: ";

        System.out.print(messageConsultation);
        //String REGISTER_COMMAND = "INSCRIRE ";
        String LOAD_COMMAND = "CHARGER ";

        try{
            Socket clientSocket= new Socket("127.0.0.1", 80);
            
            ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());

            ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
            
            Scanner sc = new Scanner(System.in);
            
            while(sc.hasNext()){

                String line = sc.nextLine();
                String command = LOAD_COMMAND + line;
                os.writeObject(command);
                os.flush();
                
                try {
                    System.out.println(is.readObject().toString());

                } catch (ClassNotFoundException e) {
                    System.out.println("La classe est introuvable");
                }

                if (line.equals("EXIT")){
                    break;
                }
            
            }
            os.close();
            is.close();
            sc.close();
            clientSocket.close();

        } catch (ConnectException c){
            System.out.println("Connexion impossible sur le port 80: pas de serveur. ");
        } catch(IOException e){
            System.out.println("Erreur à l'écriture de l'objet.");
        }
    }
}
