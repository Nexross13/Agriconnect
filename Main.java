public class Main {
    public static void main(String[] args) {
        // Création des capteurs
        Capteur capteur1 = new Capteur("1", 43.6000, 1.4333);
        Capteur capteur2 = new Capteur("2", 44.1000, 2.8333);
        Capteur capteur3 = new Capteur("3", 39.5000, 3.9373);
        Capteur capteur4 = new Capteur("4", 18.9000, 6.2663);
        Capteur capteur5 = new Capteur("5", 17.7000, 4.9333);

        // Connexion des capteurs
        capteur1.connexionCentrale();
        capteur2.connexionCentrale();
        capteur3.connexionCentrale();
        capteur4.connexionCentrale();
        capteur5.connexionCentrale();
    } 
}