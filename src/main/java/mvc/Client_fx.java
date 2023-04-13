package mvc;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import server.models.Course;

import java.util.ArrayList;

/**
 * La classe Client_fx permet de définir l'affichage du formulaire.
 */
public class Client_fx extends Application{

    /**
     * Les sessions contenues dans le comboBox.
     */
    private final String[] sessions = {"Hiver","Ete","Automne"};

    /**
     *Le comboBox contenant les sessions disponibles.
     */
    private final ComboBox<String> combo_sessions = new ComboBox<>(
            FXCollections.observableArrayList(sessions));

    /**
     *Bouton ayant comme fonction de charger la liste de cours.
     */
    private final Button boutonCharger = new Button("charger");
    /**
     * Bouton ayant comme fonction d'envoyer le formulaire.
     */
    private final Button boutonEnvoyer = new Button("envoyer");
    /**
     * Champ de texte du prénom.
     */
    private final TextField prenom_txt = new TextField();
    /**
     * Champ de texte du nom.
     */
    private final TextField nom_txt = new TextField();
    /**
     * Champ de texte de l'adresse courriel.
     */
    private final TextField email_txt = new TextField();
    /**
     * Champ de texte du matricule.
     */
    private final TextField mat_txt = new TextField();
    /**
     * Tableau contenant les cours de la session sélectionnée.
     */
    private final TableView<Course> table_cours = new TableView<>();
    /**
     * Controleur permettant l'interaction des boutons.
     */
    private final Controleur controleur = new Controleur(this);
    /**
     * Alerte lorsqu'il y a une erreur dans le formulaire.
     */
    private final Alert formInvalide = new Alert(AlertType.ERROR);
    /**
     * Message confirmant l'inscription du client à un cours.
     */
    private final Alert formValide = new Alert(AlertType.INFORMATION);

    /**
     * Méthode permettant de créer l'affichage de l'interface et d'ajouter de l'interaction avec les boutons.
     * @param primaryStage  La fenêtre d'affichage
     */
    public void start(Stage primaryStage){
        HBox root = new HBox();
        VBox gauche = new VBox();
        VBox droite = new VBox();

        root.getChildren().add(gauche);
        root.getChildren().add(new Separator(Orientation.VERTICAL));
        root.getChildren().add(droite);
        
        gauche.setAlignment(Pos.TOP_CENTER);
        droite.setAlignment(Pos.TOP_CENTER);

        gauche.setPrefWidth(350);
        droite.setPrefWidth(300);

        gauche.setSpacing(10);
        droite.setSpacing(20);
        Scene scene = new Scene(root,650,450);

        // Textes:
        Text txt_liste_cours = new Text("Liste des cours");
        txt_liste_cours.setFont(Font.font("arial",20));
        gauche.getChildren().add(txt_liste_cours);

        Text txt_form = new Text("Formulaire d'inscription");
        txt_form.setFont(Font.font("arial",20));
        droite.getChildren().add(txt_form);

        //Combo Box:
        combo_sessions.setPrefWidth(110);
        combo_sessions.setValue("Hiver");

        // Table complétée:
        
        table_cours.setMaxWidth(300);
        table_cours.setMaxHeight(350);
        TableColumn<Course,String> col_1 = new TableColumn<>("Code");
        col_1.setCellValueFactory(new PropertyValueFactory<>("code"));
        col_1.setPrefWidth(125);
        TableColumn<Course,String> col_2 = new TableColumn<>("Cours");
        col_2.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_2.setPrefWidth(175);
        table_cours.getColumns().add(col_1);
        table_cours.getColumns().add(col_2);

        boutonCharger.setOnAction((action) -> {
            ArrayList<Course> liste_cours = controleur.charger();
            ObservableList<Course> liste_cours_visibles = FXCollections.observableArrayList(liste_cours);
            table_cours.setItems(liste_cours_visibles);
        });
        
        VBox table_vb = new VBox(table_cours);
        table_vb.setAlignment(Pos.CENTER);
        gauche.getChildren().add(table_vb);
        gauche.getChildren().add(new Separator(Orientation.HORIZONTAL));
        
        

        Label envoyer_lab = new Label("");
        envoyer_lab.setPrefWidth(70);
        
        boutonEnvoyer.setOnAction((action) -> {
            String messageAlert = controleur.inscrire();

            if (messageAlert.charAt(0) == '-'){
                formInvalide.setContentText(messageAlert);
                formInvalide.show();
            }
            else {
                formValide.setContentText(messageAlert);
                formValide.show();
            }

        });

        boutonEnvoyer.setPrefWidth(100);
        HBox envoyer_hb = new HBox(envoyer_lab,boutonEnvoyer);
        envoyer_hb.setAlignment(Pos.CENTER);

        HBox bas_gauche = new HBox(combo_sessions,boutonCharger);
        bas_gauche.setAlignment(Pos.CENTER);
        gauche.getChildren().add(bas_gauche);
        bas_gauche.setSpacing(50);
        
        // Formulaire:
        Label prenom_lab = new Label("Prénom");
        prenom_lab.setPrefWidth(50);
        HBox prenom_hb = new HBox(prenom_lab, prenom_txt);
        prenom_hb.setAlignment(Pos.CENTER);
        prenom_hb.setSpacing(20);

        Label nom_lab = new Label("Nom");
        nom_lab.setPrefWidth(50);
        HBox nom_hb = new HBox(nom_lab, nom_txt);
        nom_hb.setAlignment(Pos.CENTER);
        nom_hb.setSpacing(20);

        Label email_lab = new Label("Email");
        email_lab.setPrefWidth(50);
        HBox email_hb = new HBox(email_lab, email_txt);
        email_hb.setAlignment(Pos.CENTER);
        email_hb.setSpacing(20);

        Label mat_lab = new Label("Matricule");
        mat_lab.setPrefWidth(50);
        HBox mat_hb = new HBox(mat_lab, mat_txt);
        mat_hb.setAlignment(Pos.CENTER);
        mat_hb.setSpacing(20);

        VBox form = new VBox(prenom_hb,nom_hb,email_hb,mat_hb);
        form.setSpacing(10);
        
        
        droite.getChildren().addAll(form,envoyer_hb);


        primaryStage.setTitle("Inscription UdeM");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Getter pour le ComboBox contenant les sessions.
     * @return  ComboBox contenant les sessions
     */
    public ComboBox<String> getComboBox(){
        return combo_sessions;
    }

    /**
     * Getter pour le champ de texte contenant le prénom.
     * @return  Champ de texte contenant le prénom
     */
    public TextField getPrenomTxt(){
        return prenom_txt;     
    }
    /**
     * Getter pour le champ de texte contenant le nom.
     * @return  Champ de texte contenant le nom
     */
    public TextField getNomTxt(){
        return nom_txt;
    }
    /**
     * Getter pour le champ de texte contenant l'adresse courriel.
     * @return  Champ de texte contenant l'adresse courriel
     */
    public TextField getEmailTxt(){
        return email_txt;
    }
    /**
     * Getter pour le champ de texte contenant le matricule.
     * @return  Champ de texte contenant le matricule
     */
    public TextField getMatriculeTxt(){
        return mat_txt;
    }

    /**
     * Getter pour le tableau contenant les cours de la session sélectionnée.
     * @return Tableau contenant les cours de la session sélectionnée.
     */
    public TableView<Course> getTableCours(){
        return table_cours;
    }

    /**
     * Getter pour le cours sélectionné dans le tableau.
     * @return  Le cours sélectionné dans le tableau
     */
    public Course getSelectionCours(){
        return table_cours.getSelectionModel().getSelectedItem();
    }

    /**
     * Méthode permettant de réinitialiser la couleur des champs de texte et du tableau
     * contenant les cours de la session sélectionnée.
     */
    public void resetColor(){
        
        prenom_txt.setStyle(null);
        nom_txt.setStyle(null);
        email_txt.setStyle(null);
        mat_txt.setStyle(null);
        table_cours.setStyle(null);
    }

    /**
     * Méthode principale pour exécuter l'interface graphique Client_fx.
     * @param args Arguments passés en lignes de commandes
     */
    public static void main(String[] args){
        Client_fx.launch(args);
    }
}
