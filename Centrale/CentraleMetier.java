package Centrale;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Arroseur.ArroseurInterface;

import java.sql.*;

import Capteur.CapteurInknowException;
import Capteur.CapteurInterface;

public class CentraleMetier  {
    private HashMap<Integer, CapteurInterface> capteurs;
    private HashMap<Integer, ArroseurInterface> arroseurs;
    private ScheduledExecutorService executor;
    private Connection bdd;

    public CentraleMetier() {
        this.capteurs = new HashMap<Integer, CapteurInterface>();
        try {
            String urlDB = "jdbc:mysql://localhost:8889/agriconnect";
            this.bdd = DriverManager.getConnection(urlDB, "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Methodes privates
    private void receiveData(int id) throws CapteurInknowException, RemoteException{
        executor = Executors.newSingleThreadScheduledExecutor();
        if(this.capteurs.containsKey(id) && this.capteurs.get(id).getEstActif()){
            CapteurInterface capteur = this.capteurs.get(id);
            Runnable task = () -> {
                while (true) {
                    try {
                        double temperature = capteur.getTemperature();
                        double humidite = capteur.getHumidite();
                        LocalDateTime now = LocalDateTime.now();
                        String formatter = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

                        for (ArroseurInterface arroseur : this.arroseurs.values()) {
                            if (capteur.getZone() == arroseur.getZone() && humidite < arroseur.getSeuilHumi()){
                                arroseur.arroser(capteur);
                            }
                        }

                        //ecrire dans un fichier
                        FileWriter writer = new FileWriter("Data/data.txt", true);
                        writer.write("[" + formatter + "] Capteur " + id + " : " + temperature + "°C, " + humidite + "%\n");
                        writer.close();
                        
                        //ecrire les data dans bdd
                        writeDataInBdd(id, temperature, humidite);
                        break;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            };

            executor.scheduleAtFixedRate(task, 0, capteur.getInterval(), TimeUnit.SECONDS);

        } else {
            throw new CapteurInknowException();
        }
    }

    private void writeDataInBdd(int id, double temperature, double humidite) throws SQLException {
        CallableStatement ajoutDonnees = this.bdd.prepareCall("{call ajoutDonnees(?, ?, ?)}");
        ajoutDonnees.setInt(1, id);
        ajoutDonnees.setDouble(2, temperature);
        ajoutDonnees.setDouble(3, humidite);
        ajoutDonnees.execute();
    }

    private void ecrireLog(String message, String type) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String formatter = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        FileWriter writer = new FileWriter("Logs/" + type + ".log", true);
        writer.write("[" + formatter + "] : " + message);
        writer.close();
    }

    private String verifCoordonnees(double longitude, double latitude) throws SQLException {
        CallableStatement coordonnees = this.bdd.prepareCall("{call CheckPointInTerrains(?, ?, ?)}");
        coordonnees.setDouble("p_longitude", longitude);
        coordonnees.setDouble("p_latitude", latitude);
        coordonnees.registerOutParameter("terrain_name", Types.VARCHAR);
        coordonnees.executeQuery();
        String terrain = coordonnees.getString("terrain_name");
        return terrain;
    }

    //Methodes publiques

    public void registerCapteur(int id, double latitude, double longitude) throws MalformedURLException, RemoteException, NotBoundException, CapteurInknowException, IOException, SQLException {
        CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:4444/capteur" + id);
        String terrainName = verifCoordonnees(longitude, latitude);
        if (terrainName == null) {
            System.out.println("Coordonnées incorrectes");
        } else {
            this.capteurs.put(id, capteur);
            capteur.setLatitude(latitude);
            capteur.setLongitude(longitude);
            capteur.setZone(terrainName);
            String message = "Le capteur " + id + " a été ajouté à la centrale" + "\n";
            ecrireLog(message, "capteur");
            receiveData(id);
        }
        
    }

    public void registerArroseur(int id, double latitude, double longitude) throws MalformedURLException, RemoteException, NotBoundException, CapteurInknowException, IOException, SQLException {
        ArroseurInterface arroseur = (ArroseurInterface) Naming.lookup("rmi://localhost:4444/arroseur" + id);
        String terrainName = verifCoordonnees(longitude, latitude);
        if (terrainName == null) {
            System.out.println("Coordonnées incorrectes");
        } else {
            this.arroseurs.put(id, arroseur);
            arroseur.setLatitude(latitude);
            arroseur.setLongitude(longitude);
            arroseur.setZone(terrainName);
            String message = "L'arroseur " + id + " a été ajouté à la centrale" + "\n";
            ecrireLog(message, "arroseur");
        }
    }

    public void unregisterCapteur(int id) throws RemoteException {
        if (this.capteurs.containsKey(id)) {
            this.capteurs.remove(id);
        }
    }

    public void activerCapteur(int id) throws RemoteException, CapteurInknowException, IOException {
        if (this.capteurs.containsKey(id) && !this.capteurs.get(id).getEstActif()) {
            CapteurInterface capteur = this.capteurs.get(id);
            capteur.activer();
            String message = "Le capteur " + id + " a été activé" + "\n";
            ecrireLog(message, "capteur");
            receiveData(id);
        }
    }

    public void activerArroseur(int id) throws RemoteException, CapteurInknowException, IOException {
        if (this.arroseurs.containsKey(id) && !this.arroseurs.get(id).getEstActif()) {
            ArroseurInterface arroseur = this.arroseurs.get(id);
            arroseur.activer();
            String message = "L'arroseur " + id + " a été activé" + "\n";
            ecrireLog(message, "arroseur");
        }
    }

    public void desactiverCapteur(int id) throws RemoteException, CapteurInknowException, IOException {
        if (this.capteurs.containsKey(id) && this.capteurs.get(id).getEstActif()) {
            CapteurInterface capteur = this.capteurs.get(id);
            capteur.desactiver();
            String message = "Le capteur " + id + " a été désactivé" + "\n";
            ecrireLog(message, "capteur");
            executor.shutdown();
        }
    }

    public void desactiverArroseur(int id) throws RemoteException, CapteurInknowException, IOException {
        if (this.arroseurs.containsKey(id) && this.arroseurs.get(id).getEstActif()) {
            ArroseurInterface arroseur = this.arroseurs.get(id);
            arroseur.desactiver();
            String message = "L'arroseur " + id + " a été désactivé" + "\n";
            ecrireLog(message, "arroseur");
            executor.shutdown();
        }
    }

    public void modifInterval(int id, int intervalle) throws RemoteException, CapteurInknowException, IOException {
        if (id == 0) {
            capteurs.values().forEach(capteur -> {
                try {
                    capteur.setInterval(intervalle);
                    executor.shutdown();
                    String message = "L'intervalle de tous les capteurs a été modifié à " + intervalle + " secondes " + "\n";
                    ecrireLog(message, "capteur");
                    receiveData(capteur.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            CapteurInterface capteur = this.capteurs.get(id);
            capteur.setInterval(intervalle);
            executor.shutdown();
            String message = "L'intervalle du capteur " + id + " a été modifié à " + intervalle + " secondes " + "%\n";
            ecrireLog(message, "capteur");
            receiveData(id);
        }
    }

    public HashMap<Integer, CapteurInterface> getCapteurs() throws RemoteException {
        return this.capteurs;
    }

    public HashMap<Integer, ArroseurInterface> getArroseurs() throws RemoteException {
        return this.arroseurs;
    }

    public CapteurInterface getLastInfoCapteur(int id) throws RemoteException {
        if (!this.capteurs.containsKey(id) && this.capteurs.get(id).getEstActif()) {
            return this.capteurs.get(id);
        } else {
            return null;
        }
    }

    public HashMap<String, Object> getMoyenne(int id) throws SQLException {
        String tendance;
        HashMap<String, Object> resultats = new HashMap<String, Object>();

        CallableStatement moyenne = this.bdd.prepareCall("{call CalculerMoyenneParMinute(?, ?, ?, ?, ?, ?, ?)}");
        moyenne.setInt("ps_id_capteur", id);
        moyenne.registerOutParameter("pheure_n", Types.DATE);
        moyenne.registerOutParameter("pmoyenne_temperature_n", Types.DOUBLE);
        moyenne.registerOutParameter("pmoyenne_humidite_n", Types.DOUBLE);
        moyenne.registerOutParameter("pheure_n1", Types.DATE);
        moyenne.registerOutParameter("pmoyenne_temperature_n1", Types.DOUBLE);
        moyenne.registerOutParameter("pmoyenne_humidite_n1", Types.DOUBLE);
        moyenne.executeQuery();
        
        double moyenneTemperatureN = moyenne.getDouble("pmoyenne_temperature_n");
        resultats.put("moyenne_temperature", moyenneTemperatureN);
        double moyenneTemperatureN1 = moyenne.getDouble("pmoyenne_temperature_n1");

        if (moyenneTemperatureN > moyenneTemperatureN1) {
            tendance = "hausse";
        } else if (moyenneTemperatureN < moyenneTemperatureN1) {
            tendance = "baisse";
        } else {
            tendance = "stable";
        }

        resultats.put("tendance_temperature", tendance);

        double moyenneHumiditeN = moyenne.getDouble("pmoyenne_humidite_n");
        resultats.put("moyenne_humidite", moyenneHumiditeN);
        double moyenneHumiditeN1 = moyenne.getDouble("pmoyenne_humidite_n1");

        System.out.println(moyenneHumiditeN);
        System.out.println(moyenneTemperatureN);

        if (moyenneHumiditeN > moyenneHumiditeN1) {
            tendance = "hausse";
        } else if (moyenneHumiditeN < moyenneHumiditeN1) {
            tendance = "baisse";
        } else {
            tendance = "stable";
        }

        resultats.put("tendance_humidite", tendance);
        
        //Voir le contenu de la hashmap
        for (String name: resultats.keySet()) {
            String key = name.toString();
            String value = resultats.get(name).toString();
            System.out.println(key + " " + value);
        }

        return resultats;
    }
}