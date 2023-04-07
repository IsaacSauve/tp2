package client;

import java.io.*;
import java.net.Socket;
import java.rmi.ConnectException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args){
        try{
            Socket clientSocket= new Socket("127.0.0.1", 80);

            OutputStreamWriter os = new OutputStreamWriter(
                    clientSocket.getOutputStream());

            BufferedWriter bw = new BufferedWriter(os);

            Scanner sc = new Scanner(System.in);

            InputStreamReader is = new InputStreamReader(
                    clientSocket.getInputStream());

            while(sc.hasNext()){
                String line=sc.nextLine();
                bw.append(line + "\n");

                if (line.equals("CHARGER")){
                    System.out.println(is.getEncoding());
                }

                if (line.equals("EXIT")){
                    break;
                }

            }
            bw.close();
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
