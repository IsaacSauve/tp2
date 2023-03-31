package server.models;

import java.io.Serializable;
/**
 * La classe RegistrationForm représente un formulaire d'inscription fourni par
 * le client qui est envoyé au serveur.
 */
public class RegistrationForm implements Serializable {
    /**
     * Le prénom du client
     */
    private String prenom;
    /**
     * Le nom de famille du client
     */
    private String nom;
    /**
     * L'adresse courriel du client
     */
    private String email;
    /**
     * Le matricule du client
     */
    private String matricule;
    /**
     * Le cours auquel le client souhaite s'inscrire
     */
    private Course course;

    /**
     * Constructeur du formulaire d'inscription qui demande le prénom, le nom de 
     * famille, l'adresse courriel, le matricule et le cours auquel le client 
     * veut s'inscrire au moment de se faire instancier.
     * 
     * @param prenom Le prénom du client
     * @param nom Le nom de famille du client
     * @param email L'adresse courriel du client
     * @param matricule Le matricule du client
     * @param course Le cours auquel le client veut s'inscrire
     */
    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    /**
     * Méthode pour obtenir le prénom du client
     * 
     * @return Le prénom du client
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Méthode pour modifier le prénom du client 
     * 
     * @param prenom Le nouveau prénom du client 
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Méthode pour obtenir le nom de famille du client
     * 
     * @return Le nom de famille du client
     */
    public String getNom() {
        return nom;
    }

    /**
     * Méthode pour modifier le nom de famille du client
     * 
     * @param nom Le nouveau nom de famille du client
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Méthode pour obtenir l'adresse courriel du client
     * 
     * @return L'adresse courriel du client
     */
    public String getEmail() {
        return email;
    }

    /**
     * Méthode pour modifier l'adresse courriel du client
     * 
     * @param email La nouvelle adresse courriel du client
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Méthode pour obtenir le matricule du client
     * 
     * @return Le matricule du client
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * Méthode pour modifier le matricule du client
     * 
     * @param matricule Le nouveau matricule du client
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * Méthode pour obtenir le cours auquel le client veut s'inscrire
     * 
     * @return Le cours auquel le client veut s'inscrire
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Méthode pour modifier le cours auquel le client veut s'inscrire
     * 
     * @param course Le nouveau cours auquel le client veut s'inscrire
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Méthode pour afficher l'information du formulaire en chaîne de caractères
     * 
     * @return Toute l'information du formulaire en chaîne de caractères
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
