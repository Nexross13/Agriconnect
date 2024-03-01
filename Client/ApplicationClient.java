package Client;


import java.rmi.Naming;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import Capteur.CapteurImpl;
import Capteur.CapteurInfo;
import Capteur.CapteurInterface;
import Centrale.CentraleInterface;

import java.time.format.DateTimeFormatter;


/**
 * Application client pour interagir avec la centrale de gestion des capteurs.
 * Permet de lister les capteurs, d'obtenir leurs informations, de modifier leur intervalle de mesure,
 * d'ajouter, d'activer ou de retirer des capteurs, et de calculer la moyenne et la tendance de leurs mesures.
 */
public class ApplicationClient {
    // Référence à l'interface de la centrale, utilisée pour appeler les méthodes distantes.
    private static CentraleInterface centrale;

    public static void main(String[] args) {
        try {
             // Recherche de l'objet distant de la centrale par son nom dans le service de nommage RMI.
            centrale = (CentraleInterface) Naming.lookup("rmi://localhost/Centrale");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Lister les capteurs");
                System.out.println("2. Obtenir infos capteur");
                System.out.println("3. Modifier intervalle de mesure pour un capteur ou tous");
                System.out.println("4. Ajouter un capteur");
                System.out.println("5. Activer un capteur");
                System.out.println("6. Retirer un capteur");
                System.out.println("7. Calculer la moyenne et la tendance pour un capteur");
                System.out.println("8. Quitter");
                System.out.print("Choix: ");
                int choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        listerCapteurs();
                        break;
                    case 2:
                        obtenirInfosCapteur(scanner);
                        break;
                    case 3:
                        modifierIntervalleMesure(scanner);
                        break;
                    case 4:
                        ajouterCapteur(scanner);
                        break;
                    case 5:
                        activerCapteur(scanner);
                        break;
                    case 6:
                        retirerCapteur(scanner);
                        break;
                    case 7:
                        calculerMoyenneEtTendance(scanner); 
                        break;
                    case 8:
                        System.out.println("Au revoir !");
                        System.exit(0);
                        break;
                    
                    default:
                        System.out.println("Choix invalide.");
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthodes implémentées pour chaque action possible, appelant les méthodes distantes de la centrale.
    // Chaque méthode est documentée avec des commentaires explicatifs sur son fonctionnement.

     // Lister les capteurs disponibles et leur état.
    private static void listerCapteurs() throws Exception {
        List<CapteurInfo> capteurs = centrale.listerCapteurs();
        if (capteurs.isEmpty()) {
            System.out.println("Aucun capteur connecté.");
        } else {
            capteurs.forEach(capteur -> System.out.println(
                "Capteur ID: " + capteur.getId() + ", Latitude: " + capteur.getLatitude() + ", Longitude: " + capteur.getLongitude() +" - État: " + (capteur.EstActif() ? "Actif" : "Désactivé")
            ));
        }
    }
    
    // Obtenir et afficher les informations d'un capteur spécifique.
    private static void obtenirInfosCapteur(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du capteur: ");
        int id = scanner.nextInt();
        CapteurInfo info = centrale.obtenirInfosCapteur(id);

        // Formatez la date et l'heure de la dernière mise à jour.
        String formattedTimestamp = info.getTimestamp().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        // Formate la température et l'humidité pour afficher un chiffre après la virgule et ajoute les unités.
        String formattedTemperature = String.format("%.1f°C", info.getDerniereTemperature());
        String formattedHumidite = String.format("%.1f%%", info.getDerniereHumidite());
        
        System.out.println("Dernière mesure pour le capteur " + id + " à " + formattedTimestamp + ": Température = " + formattedTemperature + ", Humidité = " + formattedHumidite);
    }
    
    // Modifier l'intervalle de mesure d'un ou de tous les capteurs.
    private static void modifierIntervalleMesure(Scanner scanner) throws Exception {
        System.out.println("Entrez l'ID du capteur ou '-1' pour tous les capteurs:");
        int id = scanner.nextInt();
        System.out.print("Entrez le nouvel intervalle de mesure (en secondes): ");
        long intervalle = scanner.nextLong();
        centrale.modifierIntervalleMesure(id, intervalle);
        if (id == -1) {
            System.out.println("Intervalle de mesure mis à jour pour tous les capteurs.");
        } else {
            System.out.println("Intervalle de mesure mis à jour pour le capteur " + id);
        }
    }

    // Ajouter un nouveau capteur et l'activer.
    private static void ajouterCapteur(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du nouveau capteur: ");
        int id = scanner.nextInt();
        System.out.print("Entrez la latitude du nouveau capteur: ");
        double latitude = scanner.nextDouble();
        System.out.print("Entrez la longitude du nouveau capteur: ");
        double longitude = scanner.nextDouble();
        
        CapteurInterface nouveauCapteur = new CapteurImpl(id, latitude, longitude, centrale);
        nouveauCapteur.activer();
        System.out.println("Nouveau capteur avec ID " + id + " et coordonnées (" + latitude + ", " + longitude + ") a été ajouté et activé.");
    }

    // Activer un capteur spécifique.
    private static void activerCapteur(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du capteur à activer: ");
        int id = scanner.nextInt();
        centrale.activerCapteur(id);
        System.out.println("Capteur avec ID " + id + " a été activé.");
    }

    // Retirer un capteur du système.
    private static void retirerCapteur(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du capteur à retirer: ");
        int id = scanner.nextInt();
        centrale.retirerCapteur(id);
        System.out.println("Capteur avec ID " + id + " a été retiré.");
    }
  
    // Calculer la moyenne et la tendance des mesures d'un capteur pour une période donnée.
    private static void calculerMoyenneEtTendance(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du capteur: ");
        int id = scanner.nextInt();
        
        System.out.println("Choisissez la période pour le calcul :");
        System.out.println("1. Dernière heure");
        System.out.println("2. Dernière journée");
        System.out.print("Choix: ");
        int periodeChoix = scanner.nextInt();
        
        LocalDateTime fin = LocalDateTime.now();
        LocalDateTime debut;
        
        if (periodeChoix == 1) {
            debut = fin.minusHours(1);
        } else if (periodeChoix == 2) {
            debut = fin.minusDays(1);
        } else {
            System.out.println("Choix invalide.");
            return;
        }
    
        // Appelez les méthodes de la centrale pour obtenir les données et effectuer les calculs
        double[] moyennes = centrale.calculerMoyenne(id, debut, fin);
        String[] tendances = centrale.determinerTendance(id, debut, fin);
        
        System.out.println("Moyenne Température: " + String.format("%.1f", moyennes[0]) + ", Moyenne Humidité: " + String.format("%.1f", moyennes[1]));
        System.out.println("Tendance Température: " + tendances[0] + ", Tendance Humidité: " + tendances[1]);
    }
}


