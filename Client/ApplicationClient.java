package Client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Scanner;

import Arroseur.ArroseurInterface;
import Capteur.CapteurInterface;
import Centrale.Centrale;

public class ApplicationClient {
    // Référence à l'interface de la centrale, utilisée pour appeler les méthodes distantes.
    private static Centrale centrale;

    public static void main(String[] args) {
        try {
             // Recherche de l'objet distant de la centrale par son nom dans le service de nommage RMI.
            centrale = (Centrale) Naming.lookup("rmi://localhost:4444/centrale");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Lister le matériel connecté");
                System.out.println("2. Obtenir infos capteur");
                System.out.println("3. Modifier intervalle de mesure pour un capteur ou tous");
                System.out.println("4. Ajouter du matériel");
                System.out.println("5. Activer du matériel");
                System.out.println("6. Désactiver du matériel");
                System.out.println("7. Calculer la moyenne et la tendance pour un capteur"); // via bdd
                System.out.println("8. Quitter");
                System.out.print("Choix: ");
                int choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        listerMateriel();
                        break;
                    case 2:
                        obtenirInfosCapteur(scanner);
                        break;
                    case 3:
                        modifierIntervalleMesure(scanner);
                        break;
                    case 4:
                        ajouterMateriel(scanner);
                        break;
                    case 5:
                        activerMateriel(scanner);
                        break;
                    case 6:
                        desactiverCapteur(scanner);
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

    private static void listerMateriel() throws Exception {
        System.out.println("Liste des capteurs:");
        HashMap<Integer, CapteurInterface> listeCapteur = centrale.getCapteurs();
        HashMap<Integer, ArroseurInterface> listeArroseur = centrale.getArroseurs();
        System.out.println("Capteurs:");
        listeCapteur.forEach((id, capteur) -> {
            try {
                String etat = capteur.getEstActif() ? "ACTIF" : "DESACTIVE";
                System.out.println("[" + etat +"] ID: " + id + ", Zone " + capteur.getZone() +"(" + capteur.getLatitude() + "/" + capteur.getLongitude() + ")");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        System.out.println("--------------------");
        System.out.println("Arroseurs:");
        listeArroseur.forEach((id, arroseur) -> {
            try {
                String etat = arroseur.getEstActif() ? "ACTIF" : "DESACTIVE";
                System.out.println("[" + etat +"] ID: " + id + ", Zone " + arroseur.getZone() +"(" + arroseur.getLatitude() + "/" + arroseur.getLongitude() + ")");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private static void obtenirInfosCapteur(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du capteur: ");
        int id = scanner.nextInt();
        CapteurInterface capteur = centrale.getLastInfoCapteur(id);
        if (capteur == null) {
            System.out.println("Capteur avec ID " + id + " n'existe pas ou n'est pas activé.");
        } else {
            System.out.println("ID: " + id + ", Zone: " + capteur.getZone() + "(" + capteur.getLatitude() + "/" + capteur.getLongitude() + ") | Temperature: " + capteur.getTemperature() + "°C, Humidité: " + capteur.getHumidite() + "%");
        }
    }

    private static void ajouterMateriel(Scanner scanner) throws Exception {
        System.out.print("Quel matériel vous voulez ajouter? (1: Capteur, 2: Arroseur): ");
        int choix = scanner.nextInt();
        if (choix == 1) {
            System.out.print("Entrez l'ID du nouveau capteur: ");
            int id = scanner.nextInt();
            System.out.print("Entrez la latitude du capteur: ");
            double latitude = scanner.nextDouble();
            System.out.print("Entrez la longitude du capteur: ");
            double longitude = scanner.nextDouble();
            centrale.registerCapteur(id, latitude, longitude);
            System.out.println("Nouveau capteur avec ID " + id + " a été ajouté.");
        } else if (choix == 2) {
            System.out.print("Entrez l'ID du nouvel arroseur: ");
            int id = scanner.nextInt();
            System.out.print("Entrez la latitude de l'arroseur: ");
            double latitude = scanner.nextDouble();
            System.out.print("Entrez la longitude de l'arroseur: ");
            double longitude = scanner.nextDouble();
            centrale.registerArroseur(id, latitude, longitude);
            System.out.println("Nouveau capteur avec ID " + id + " a été ajouté et activé.");
        } else {
            System.out.println("Choix invalide.");
        }
        
    }

    private static void activerMateriel(Scanner scanner) throws Exception {
        System.out.print("Quel matériel vous voulez ajouter? (1: Capteur, 2: Arroseur): ");
        int choix = scanner.nextInt();
        if (choix == 1) {
            System.out.print("Entrez l'ID du capteur: ");
            int id = scanner.nextInt();
            centrale.activerCapteur(id);
            System.out.println("Capteur " + id + " activé.");
        } else if (choix == 2) {
            System.out.print("Entrez l'ID de l'arroseur: ");
            int id = scanner.nextInt();
            centrale.activerArroseur(id);
            System.out.println("Arroseur " + id + " activé.");
        } else {
            System.out.println("Choix invalide.");
        }
    }

    private static void desactiverCapteur(Scanner scanner) throws Exception {
        System.out.print("Entrez le type de matériel que vous voulez désactiver (1: Capteur, 2: Arroseur): ");
        int choix = scanner.nextInt();
        if (choix == 1) {
            System.out.print("Entrez l'ID du capteur: ");
            int id = scanner.nextInt();
            centrale.desactiverCapteur(id);
            System.out.println("Capteur " + id + " désactivé.");
        } else if (choix == 2) {
            System.out.print("Entrez l'ID de l'arroseur: ");
            int id = scanner.nextInt();
            centrale.desactiverArroseur(id);
            System.out.println("Arroseur " + id + " désactivé.");
        } else {
            System.out.println("Choix invalide.");
        }
    }

    private static void modifierIntervalleMesure(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du capteur (ou 0 pour tous les capteurs): ");
        int id = scanner.nextInt();
        System.out.print("Entrez le nouvel intervalle de mesure: ");
        int intervalle = scanner.nextInt();
        centrale.modifInterval(id, intervalle);
    }

    private static void calculerMoyenneEtTendance(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du capteur: ");
        int id = scanner.nextInt();
        HashMap<String, Object> data = centrale.getMoyenne(id);
        System.out.println("Moyenne de température: " + data.get("moyenne_temperature") + "°C (" + data.get("tendance_temperature") + "), Moyenne d'humidité: " + data.get("moyenne_humidite") + "% (" + data.get("tendance_humidite") +")");
    }
}


