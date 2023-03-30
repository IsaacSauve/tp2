package server;

/**
 *L'interface fonctionnelle "EventHandler" est dédié à traîter la réaction du serveur à un évènement donné,
 * soit handleLoadCourses et handleRegistration.
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * La méthode handle nous permet de manipuler la commande reçu.
     *
     * @param cmd   Une chaîne de caractères représentant la commande du client.
     * @param arg   Une chaîne de caractères représentant pour quelle session le client souhaite que la commande
     *              soit exécutée.
     */
    void handle(String cmd, String arg);
}
