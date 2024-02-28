import java.io.FileWriter;
import java.util.HashMap;

public class CentraleMetier {
    private HashMap<String, Capteur> capteurs;

    public CentraleMetier() {
        this.capteurs = new HashMap<String, Capteur>();
    }

    //Methodes
    public void registerCapteur(String name, double longitude, double latitude) {
        this.capteurs.put(name, new Capteur(name, longitude, latitude));
    }

    public void unregisterCapteur(String name) throws CapteurInknowException{
        if(this.capteurs.containsKey(name)){
            this.capteurs.remove(name);
        } else {
            throw new CapteurInknowException();
        }
    }

    public HashMap receiveData(String name) throws CapteurInknowException{
        
        HashMap<String, Double> data = new HashMap<String, Double>();
        if(this.capteurs.containsKey(name)){
            Capteur capteur = this.capteurs.get(name);
            double temperature = capteur.getTemperature();
            double humidity = capteur.getHumidity();

            try (FileWriter writer = new FileWriter("donnees.txt", true)) {
                writer.write(capteur.getName() + " id : " + capteur.getId() + ": Température = " + temperature + ", Humidité = " + humidity + ", Latitude = " + capteur.getLatitude() + ", Longitude = " + capteur.getLongitude() + "\n");
            } catch (Exception e) {
                System.err.println("Erreur lors de l'écriture dans le fichier : " + e.toString());
            }
            System.out.print("Données reçues : " + capteur.getName() + " id : " + capteur.getId() + ": Température = " + temperature + ", Humidité = " + humidity + ", Latitude = " + capteur.getLatitude() + ", Longitude = " + capteur.getLongitude() + "\n");
            data.put("temperature", temperature);
            data.put("humidity", humidity);

            return data;

        } else {
            throw new CapteurInknowException();
        }
    }
}
