package mvc;

import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Controleur {

    private Client_fx vue;

    public Controleur(Client_fx vue){
        this.vue = vue;
    }
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

            String session = vue.getComboBox().getValue();

            oos.writeObject("CHARGER " + session );
            oos.flush();

            @SuppressWarnings("unchecked")
            ArrayList<Course> liste_cours = (ArrayList<Course>)ois.readObject();
            
            clientSocket.close();
            oos.close();
            ois.close();

            return liste_cours;
        }catch (IOException e){
            System.out.println("Erreur à la connexion.");

        }catch (ClassNotFoundException e){
            System.out.println("Erreur classe non trouvé.");
        }
        return null;
    }

    public boolean validerEmail(String email){

        String[] emailVerif1 = email.split("@");
        if (emailVerif1.length !=2){
            return false;
        }
        String[] emailVerif2 = emailVerif1[1].split("\\.");
        
        if (emailVerif2.length !=2){
            return false;
        }

        String[] emailVerif = {emailVerif1[0], emailVerif2[0], emailVerif2[1]};
        
        for (String partieEmail : emailVerif){
            if (partieEmail == ""){
                return false;
            }
        }
        return true;
    }

    public boolean validerMatricule(String matricule){

        try{
            Integer.parseInt(matricule);
        } catch(NumberFormatException e){
            return false;
        }

        if (matricule.length() != 8){
            return false;
        }

        return true;
    }
    public String inscrire(){

        try {
            clientSocket = new Socket("127.0.0.1",80);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());

            oos.writeObject("INSCRIRE");
            oos.flush();

            String messageAlert = "";
            String prenom = vue.getPrenomTxt().getText();

            if (prenom.equals("")){
                messageAlert += "- Vous devez entrer un prénom. \n";
                vue.getPrenomTxt().setStyle("-fx-border-color: red");
            }
            else{
                vue.getPrenomTxt().setStyle(null);
            }

            String nom = vue.getNomTxt().getText();

            if (nom.equals("")){
                messageAlert += "- Vous devez entrer un nom. \n";
                vue.getNomTxt().setStyle("-fx-border-color: red");
            }
            else{
                vue.getNomTxt().setStyle(null);
            }

            String email = vue.getEmailTxt().getText();

            if (!validerEmail(email)){
                messageAlert += "- Vous devez entrer une adresse courriel " + 
                "valide. \n";
                vue.getEmailTxt().setStyle("-fx-border-color: red");  
            }
            else{
                vue.getEmailTxt().setStyle(null);
            }

            String matricule = vue.getMatriculeTxt().getText();

            if (!validerMatricule(matricule)){
                messageAlert += "- Vous devez entrer un matricule valide. \n";
                vue.getMatriculeTxt().setStyle("-fx-border-color: red");
            }
            else{
                vue.getMatriculeTxt().setStyle(null);
            }

            Course coursChoisi = vue.getSelectionCours();

            if (coursChoisi == null){
                messageAlert +="- Vous devez choisir un cours dans la liste. \n";
                vue.getTableCours().setStyle("-fx-border-color: red");
            }
            else{
                vue.getTableCours().setStyle(null);
            }


            if (messageAlert.equals("")){
                RegistrationForm rf = new RegistrationForm(prenom, nom, email, 
                    matricule, coursChoisi);

                messageAlert += "Félicitations! Inscription réussie de " + 
                prenom + " au cours " + coursChoisi.getCode() + " .";
                
                oos.writeObject(rf);
                oos.flush();

                vue.getPrenomTxt().setText("");
                vue.getNomTxt().setText("");
                vue.getEmailTxt().setText("");
                vue.getMatriculeTxt().setText("");
                vue.resetColor();
            }

            clientSocket.close();
            oos.close();
            ois.close();
            return messageAlert;


        } catch (IOException e) {
            System.out.println("Erreur à la connexion");
        }
        return null;
    }

}
