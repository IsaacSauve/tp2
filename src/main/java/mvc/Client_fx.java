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

public class Client_fx extends Application{

    String[] sessions = {"Hiver","Ete","Automne"};
    ComboBox<String> combo_sessions = new ComboBox<String>(
            FXCollections.observableArrayList(sessions));

    private Button boutonCharger = new Button("charger");
    private Button boutonEnvoyer = new Button("envoyer");
    
    private TextField prenom_txt = new TextField();
    private TextField nom_txt = new TextField();
    private TextField email_txt = new TextField();
    private TextField mat_txt = new TextField();

    private TableView<Course> table_cours = new TableView<Course>();

    private Controleur controleur = new Controleur(this);

    private Alert formInvalide = new Alert(AlertType.ERROR);
    private Alert formValide = new Alert(AlertType.INFORMATION);


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
    public ComboBox<String> getComboBox(){
        return combo_sessions;
    }
    public TextField getPrenomTxt(){
        return prenom_txt;     
    }
    public TextField getNomTxt(){
        return nom_txt;
    }
    public TextField getEmailTxt(){
        return email_txt;
    }
    public TextField getMatriculeTxt(){
        return mat_txt;
    }
    public TableView<Course> getTableCours(){
        return table_cours;
    }
    public Course getSelectionCours(){
        return table_cours.getSelectionModel().getSelectedItem();
    }

    public void resetColor(){
        
        prenom_txt.setStyle(null);
        nom_txt.setStyle(null);
        email_txt.setStyle(null);
        mat_txt.setStyle(null);
        table_cours.setStyle(null);
    }

    public static void main(String[] args){
        Client_fx.launch(args);
    }
}
