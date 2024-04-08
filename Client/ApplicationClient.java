package Client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Scanner;

import Arroseur.ArroseurInterface;
import Capteur.CapteurInterface;
import Centrale.Centrale;

public class ApplicationClient extends UnicastRemoteObject implements ClientInterface {
    // Référence à l'interface de la centrale, utilisée pour appeler les méthodes distantes.
    private static Centrale centrale;
    private int id;

    public ApplicationClient(int id) throws RemoteException {
        super();
        this.id = id;
    }

    @Override
    public void connecter() throws RemoteException, MalformedURLException, NotBoundException, IOException {
        centrale = (Centrale) Naming.lookup("rmi://localhost:4444/centrale");
        centrale.addClient(this);
    }

    @Override
    public void demarrage() {
        try {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                printMenu();
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
                    case 0:
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

    private static void printMenu() {
        String border = new String(new char[31]).replace("\0", "*");
        String title = "GESTION DU MATÉRIEL";
        
        // Bordure supérieure
        System.out.println("\033[0;33m" + border + "\033[0m");
        // Titre centré
        System.out.println("\033[0;33m*\033[0m" + "     " + "\033[1;34m" + title + "\033[0m" + "     \033[0;33m*\033[0m");
        // Bordure inférieure du titre
        System.out.println("\033[0;33m" + border + "\033[0m\n");
        
        // Options du menu
        System.out.println("\033[0;32m1.\033[0m Lister le matériel connecté");
        System.out.println("\033[0;32m2.\033[0m Obtenir infos capteur");
        System.out.println("\033[0;32m3.\033[0m Modifier intervalle de mesure pour un capteur ou tous");
        System.out.println("\033[0;32m4.\033[0m Ajouter du matériel");
        System.out.println("\033[0;32m5.\033[0m Activer du matériel");
        System.out.println("\033[0;32m6.\033[0m Désactiver du matériel");
        System.out.println("\033[0;32m7.\033[0m Calculer la moyenne et la tendance pour un capteur");
        System.out.println("\033[0;31m0.\033[0m Quitter");
        
        // Pied de page du menu
        System.out.println("\n\033[0;33m" + border + "\033[0m");
        System.out.print("Entrez votre choix : ");
    }

    private static void listerMateriel() throws Exception {
        System.out.println("Liste des capteurs:");
        HashMap<Integer, CapteurInterface> listeCapteur = centrale.getCapteurs();
        HashMap<Integer, ArroseurInterface> listeArroseur = centrale.getArroseurs();
        System.out.println("Capteurs:");
        listeCapteur.forEach((id, capteur) -> {
            try {
                String etat = capteur.getEstActif() ? "ACTIF" : "DESACTIVE";
                System.out.println("[" + etat +"] ID: " + id + ", " + capteur.getZone() +"(" + capteur.getLatitude() + "/" + capteur.getLongitude() + ")");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        System.out.println("--------------------");
        System.out.println("Arroseurs:");
        listeArroseur.forEach((id, arroseur) -> {
            try {
                String etat = arroseur.getEstActif() ? "ACTIF" : "DESACTIVE";
                System.out.println("[" + etat +"] ID: " + id + ", " + arroseur.getZone() +"(" + arroseur.getLatitude() + "/" + arroseur.getLongitude() + ")");
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
            System.out.println("ID: " + id + ", " + capteur.getZone() + "(" + capteur.getLatitude() + "/" + capteur.getLongitude() + ") | Temperature: " + capteur.getTemperature() + "°C, Humidité: " + capteur.getHumidite() + "%");
        }
    }

    private static void ajouterMateriel(Scanner scanner) throws Exception {
        System.out.print("Quel matériel vous voulez ajouter? (1: Capteur, 2: Arroseur): ");
        int choix = scanner.nextInt();
        if (choix == 1) {
            System.out.print("Entrez l'ID du nouveau capteur: ");
            int id = scanner.nextInt();
            centrale.registerCapteur(id);
            System.out.println("Nouveau capteur avec ID " + id + " a été ajouté et activé.");

        } else if (choix == 2) {
            System.out.print("Entrez l'ID du nouvel arroseur: ");
            int id = scanner.nextInt();
            centrale.registerArroseur(id);
            System.out.println("Nouvel arroseur avec ID " + id + " a été ajouté.");
            
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
    
    @Override
    public void receiveNotification(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public int getId() throws RemoteException{
        return id;
    }
}


