package server.models;

import java.io.Serializable;
/**
 * La classe Course représente un cours auquel le client peut s'inscrire ou 
 * obtenir ses informations via le serveur. 
 */
public class Course implements Serializable {
    /**
     * Le nom du cours
     */
    private String name;
    /**
     * Le code du cours
     */
    private String code;
    /**
     * La session à laquelle le cours est offert
     */
    private String session;

    /**
     * Constructeur du cours qui demande le nom, le code et la session au moment
     * de se faire instancier.
     * 
     * @param name Le nom du cours
     * @param code Le code du cours
     * @param session La session à laquelle le cours est offert
     */
    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * Méthode pour obtenir le nom du cours
     * 
     * @return Le nom du cours
     */
    public String getName() {
        return name;
    }

    /**
     * Méthode pour modifier le nom du cours
     * 
     * @param name Le nouveau nom donné au cours
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Méthode pour obtenir le code du cours
     * 
     * @return Le code du cours
     */
    public String getCode() {
        return code;
    }

    /**
     * Méthode pour modifier le code du cours
     * 
     * @param code Le nouveau code donné au cours
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Méthode pour obtenir la session à laquelle le cours se donne
     * 
     * @return La session à laquelle le cours se donne
     */
    public String getSession() {
        return session;
    }

    /**
     * Méthode pour modifier la session à laquelle le cours se donne
     * 
     * @param session La nouvelle session à laquelle le cours a lieu
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Méthode pour afficher les informations du cours en chaîne de caractères
     * 
     * @return Les informations du cours en chaîne de caractères
     */
    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
