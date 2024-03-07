package Client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Scanner;

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
                        //obtenirInfosCapteur(scanner);
                        System.out.println("Obtenir infos capteur");
                        break;
                    case 3:
                        modifierIntervalleMesure(scanner);
                        break;
                    case 4:
                        ajouterCapteur(scanner);
                        break;
                    case 5:
                        //activerCapteur(scanner);
                        System.out.println("Activer un capteur");
                        break;
                    case 6:
                        //retirerCapteur(scanner);
                        System.out.println("Retirer un capteur");
                        break;
                    case 7:
                        //calculerMoyenneEtTendance(scanner); 
                        System.out.println("Calculer la moyenne et la tendance pour un capteur");
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

    private static void listerCapteurs() throws Exception {
        System.out.println("Liste des capteurs:");
        HashMap<Integer, CapteurInterface> listeCapteur = centrale.getCapteurs();
        listeCapteur.forEach((id, capteur) -> {
            try {
                System.out.println("ID: " + id + ", Latitude: " + capteur.getLatitude() + ", Longitude: " + capteur.getLongitude());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    // Ajouter un nouveau capteur et l'activer.
    private static void ajouterCapteur(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du nouveau capteur: ");
        int id = scanner.nextInt();
        
        centrale.registerCapteur(id);
        System.out.println("Nouveau capteur avec ID " + id + " a été ajouté et activé.");
    }

    private static void modifierIntervalleMesure(Scanner scanner) throws Exception {
        System.out.print("Entrez l'ID du capteur (ou 0 pour tous les capteurs): ");
        int id = scanner.nextInt();
        System.out.print("Entrez le nouvel intervalle de mesure: ");
        int intervalle = scanner.nextInt();
        
        centrale.modifInterval(id, intervalle);
    }
}


