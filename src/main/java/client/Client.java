package client;

import java.io.*;
import java.net.Socket;
import java.rmi.ConnectException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args){
        try{
            Socket clientSocket= new Socket("127.0.0.1", 80);

            ObjectOutputStream os = new ObjectOutputStream(
                    clientSocket.getOutputStream());
            
            //BufferedWriter bw = new BufferedWriter(os);

            Scanner sc = new Scanner(System.in);

            ObjectInputStream is = new ObjectInputStream(
                    clientSocket.getInputStream());

            //BufferedReader br = new BufferedReader(is);
            
            while(sc.hasNext()){
                String line=sc.nextLine();
                os.writeObject(line);
                os.flush();
                //bw.append(line);
                //bw.flush();

                if (line.equals("CHARGER")){
                    try {
                        System.out.println(is.readObject().toString());
                        } catch (ClassNotFoundException e) {
                        System.out.println("La classe est introuvable");
                        }
                    
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
