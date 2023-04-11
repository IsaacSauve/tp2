package mvc;

//En attendant
import server.models.Course;
import server.models.RegistrationForm;
import client.Client_fx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Controleur {

    private Course modelCourse;
    private RegistrationForm modelRegistrationForm;

    private Client_fx vue;

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

    public ArrayList<Course> charger(){

        try {
            clientSocket = new Socket("127.0.0.1",80);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());

            String session = this.vue.getComboBox().getValue();

            oos.writeObject("CHARGER " + session );
            oos.flush();

            @SuppressWarnings("unchecked")
            ArrayList<Course> liste_cours = (ArrayList<Course>)ois.readObject();
            return liste_cours;


        }catch (IOException e){
            System.out.println("Erreur à la connexion.");

        }catch (ClassNotFoundException e){
            System.out.println("Erreur classe non trouvé.");
        }
        return null;
    }

}
