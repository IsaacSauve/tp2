package client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.Server;
import server.models.Course;
import mvc.Controleur;

import java.io.IOException;
import java.util.ArrayList;

public class Client_fx extends Application{

    String[] sessions = {"Hiver","Ete","Automne"};
    ComboBox<String> combo_sessions = new ComboBox<String>(
            FXCollections.observableArrayList(sessions));

    private Button boutonCharger = new Button("charger");

    private Controleur controleur = new Controleur();


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

        // Table complétée:
        TableView<Course> table_cours = new TableView<Course>();
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
        
        //Combo Box:
        combo_sessions.setPrefWidth(110);
        combo_sessions.setValue("Hiver");
        
        //Buttons:

        Label envoyer_lab = new Label("");
        envoyer_lab.setPrefWidth(70);
        Button envoyer = new Button("envoyer");
        envoyer.setPrefWidth(100);
        HBox envoyer_hb = new HBox(envoyer_lab,envoyer);
        envoyer_hb.setAlignment(Pos.CENTER);


        HBox bas_gauche = new HBox(combo_sessions,boutonCharger);
        bas_gauche.setAlignment(Pos.CENTER);
        gauche.getChildren().add(bas_gauche);
        bas_gauche.setSpacing(50);
        
        // Formulaire:
        Label prenom_lab = new Label("Prénom");
        prenom_lab.setPrefWidth(50);
        TextField prenom_txt = new TextField();
        HBox prenom_hb = new HBox(prenom_lab, prenom_txt);
        prenom_hb.setAlignment(Pos.CENTER);
        prenom_hb.setSpacing(20);
        
        Label nom_lab = new Label("Nom");
        nom_lab.setPrefWidth(50);
        TextField nom_txt = new TextField();
        HBox nom_hb = new HBox(nom_lab, nom_txt);
        nom_hb.setAlignment(Pos.CENTER);
        nom_hb.setSpacing(20);

        Label email_lab = new Label("Email");
        email_lab.setPrefWidth(50);
        TextField email_txt = new TextField();
        HBox email_hb = new HBox(email_lab, email_txt);
        email_hb.setAlignment(Pos.CENTER);
        email_hb.setSpacing(20);

        Label mat_lab = new Label("Matricule");
        mat_lab.setPrefWidth(50);
        TextField mat_txt = new TextField();
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
    public ComboBox<String> getComboBox(){
        return combo_sessions;
    }



    public static void main(String[] args){
        Client_fx.launch(args);
        try {
            Server server = new Server(80);
            server.run();
        } catch(IOException e){
            System.out.println("Erreur à la connexion.");
        }
    }
}
