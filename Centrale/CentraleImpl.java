package Centrale;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import Capteur.CapteurInfo;
import Capteur.CapteurInterface;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Classe implémentant la centrale de gestion des capteurs via RMI.
public class CentraleImpl extends UnicastRemoteObject implements CentraleInterface {
    // Stockage des capteurs actifs avec leur ID comme clé.
    private Map<Integer, CapteurInterface> capteurs = new HashMap<>();
    // Stockage des informations actuelles des capteurs.
    private Map<Integer, CapteurInfo> capteursInfos = new HashMap<>();
    // Chemin du fichier utilisé pour enregistrer les données des capteurs.
    private static final String DATA_FILE_PATH = "capteursData.txt";

    // Constructeur de la classe CentraleImpl.
    public CentraleImpl() throws RemoteException {
        super();
    }

    // Ajoute un capteur à la centrale, ou active un capteur existant.
    public void ajouterCapteur(int id, CapteurInterface capteur, double latitude, double longitude) throws RemoteException {
        if (!capteurs.containsKey(id)) {
            System.out.println("Capteur " + id + " ajouté.");
            capteurs.put(id, capteur);
            capteursInfos.put(id, new CapteurInfo(id, 0.0, 0.0, latitude, longitude, true, null));
        } else {
            System.out.println("Capteur " + id + " est activé.");
        }
    }

    // Désactive un capteur spécifique et met à jour son état dans les informations du capteur.
    @Override
    public void retirerCapteur(int id) throws RemoteException {
        capteurs.get(id).desactiver();
        capteursInfos.get(id).setEstActif(false);
        System.out.println("Capteur " + id + " désactivé.");
    }

    // Retourne une liste contenant les informations de tous les capteurs.
    @Override
    public List<CapteurInfo> listerCapteurs() throws RemoteException {
        return new ArrayList<>(capteursInfos.values());
    }

    // Obtient les informations actuelles d'un capteur spécifique par son ID.
    @Override
    public CapteurInfo obtenirInfosCapteur(int id) throws RemoteException {
        return capteursInfos.get(id);
    }

    // Reçoit les données d'un capteur et met à jour ses informations.
    @Override
    public void recevoirDonnees(int id, double temperature, double humidite) throws RemoteException {
        CapteurInfo c = capteursInfos.get(id);
        LocalDateTime now = LocalDateTime.now(); // Obtient le timestamp actuel.
        
        // Formate la date pour l'affichage.
        String formattedDate = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        // Formate la température et l'humidité pour afficher un chiffre après la virgule.
        String formattedTemperature = String.format("%.1f°C", temperature);
        String formattedHumidite = String.format("%.1f%%", humidite);
        
        // Mise à jour et affichage des informations du capteur avec les formats appliqués.
        capteursInfos.put(id, new CapteurInfo(id, temperature, humidite, c.getLatitude(), c.getLongitude(), c.EstActif(), now));
        System.out.println("Données reçues du capteur " + id + " à " + formattedDate + ": Température = " + formattedTemperature + ", Humidité = " + formattedHumidite);
        
        enregistrerDonneesDansFichier(id, temperature, humidite, now);
}


    // Modifie l'intervalle de mesure pour un ou tous les capteurs.
    @Override
    public void modifierIntervalleMesure(int capteurId, long nouvelIntervalle) throws RemoteException {
        if (capteurId == -1) {
            // Modifie l'intervalle pour tous les capteurs.
            capteurs.values().forEach(capteur -> {
                try {
                    capteur.setIntervalleMesure(nouvelIntervalle);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Intervalle de mesure modifié pour tous les capteurs à " + nouvelIntervalle + " secondes.");
        } else {
            // Modifie l'intervalle pour un capteur spécifique.
            if (capteurs.containsKey(capteurId)) {
                capteurs.get(capteurId).setIntervalleMesure(nouvelIntervalle);
                System.out.println("Intervalle de mesure du capteur " + capteurId + " modifié à " + nouvelIntervalle + " secondes.");
            } else {
                System.out.println("Capteur " + capteurId + " introuvable.");
            }
        }
    }

    // Active un capteur spécifique.
    public void activerCapteur(int id) throws RemoteException {
        if (capteurs.containsKey(id)) {
            capteurs.get(id).activer();
            System.out.println("Capteur avec ID " + id + " activé.");
        } else {
            System.out.println("Capteur avec ID " + id + " introuvable.");
        }
    }  

    // Enregistre les données des capteurs dans un fichier pour une persistance simple.
    private void enregistrerDonneesDansFichier(int id, double temperature, double humidite, LocalDateTime timestamp) {
        String formattedDateTime = timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(DATA_FILE_PATH, true)))) {
            // Utilise Locale.ENGLISH pour forcer l'utilisation d'un point comme séparateur décimal.
            String formattedOutput = String.format(Locale.ENGLISH, "%d;%s;%.1f;%.1f%n", id, formattedDateTime, temperature, humidite);
            out.print(formattedOutput);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture des données de capteur dans le fichier: " + e.getMessage());
        }
    }
    

    // Lit et filtre les données des capteurs depuis un fichier en fonction de l'intervalle de temps et de l'ID du capteur.
    public List<String[]> lireDonneesFichier(LocalDateTime debut, LocalDateTime fin, int idCapteur) {
        try {
            return Files.lines(Paths.get(DATA_FILE_PATH))
                    // Utilisez le point-virgule comme délimiteur lors de la séparation des champs.
                    .map(line -> line.split(";"))
                    .filter(data -> LocalDateTime.parse(data[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME).isAfter(debut) &&
                                    LocalDateTime.parse(data[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME).isBefore(fin) &&
                                    Integer.parseInt(data[0]) == idCapteur)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
            return Collections.emptyList();
        }
    } 


    // Calcule la moyenne des mesures de température et d'humidité pour un capteur spécifié sur un intervalle donné.
    public double[] calculerMoyenne(int idCapteur, LocalDateTime debut, LocalDateTime fin) {
        List<String[]> donnees = lireDonneesFichier(debut, fin, idCapteur);
        double sommeTemperature = 0.0, sommeHumidite = 0.0;
        // Accumule les valeurs pour calculer la moyenne.
        for (String[] data : donnees) {
            sommeTemperature += Double.parseDouble(data[2]);
            sommeHumidite += Double.parseDouble(data[3]);
        }
        // Calcule les moyennes de température et d'humidité.
        double moyenneTemperature = donnees.size() > 0 ? sommeTemperature / donnees.size() : 0;
        double moyenneHumidite = donnees.size() > 0 ? sommeHumidite / donnees.size() : 0;
        return new double[]{moyenneTemperature, moyenneHumidite};
    }

    // Détermine la tendance des mesures (à la hausse, à la baisse, stable) pour un capteur sur un intervalle donné.
    public String[] determinerTendance(int idCapteur, LocalDateTime debut, LocalDateTime fin) {
        List<String[]> donnees = lireDonneesFichier(debut, fin, idCapteur);
        // Nécessite au moins deux points de données pour déterminer une tendance.
        if (donnees.size() < 2) return new String[]{"Stable", "Stable"};

        // Compare la première et la dernière mesure pour déterminer la tendance.
        double premiereTemperature = Double.parseDouble(donnees.get(0)[2]);
        double derniereTemperature = Double.parseDouble(donnees.get(donnees.size() - 1)[2]);
        double premiereHumidite = Double.parseDouble(donnees.get(0)[3]);
        double derniereHumidite = Double.parseDouble(donnees.get(donnees.size() - 1)[3]);

        // Détermine la tendance pour la température et l'humidité.
        String tendanceTemperature = derniereTemperature > premiereTemperature ? "À la hausse" :
                                     derniereTemperature < premiereTemperature ? "À la baisse" : "Stable";
        String tendanceHumidite = derniereHumidite > premiereHumidite ? "À la hausse" :
                                  derniereHumidite < premiereHumidite ? "À la baisse" : "Stable";

        return new String[]{tendanceTemperature, tendanceHumidite};
    }
}






