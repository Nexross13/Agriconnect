# Agriconnect

On souhaite mettre en œuvre un réseau de capteurs et d’actionneur pour faciliter le travail d’agriculteurs.
Ce système permet, par l’intermédiaire de capteurs, de surveiller deux paramètres essentiels des cultures : l’humidité du sol et la température de l’air.
Grace à l’utilisation d’actionneurs, le système pourra déclencher l’arrosage des cultures si besoin.
Le tout pourra être surveillé (et géré) par l’agriculteur sur une application cliente.

## Sommaire
- [Features](#features)
- [Update](#updates)
- [BDD](#bdd)

## Features
**Arroseur**

- Modifie la temperature et l'humidité des capteurs dans la même zone que lui,

**Capteurs**

- Enregistrement à la centrale,
- Simulation de data,
- Envoie de data.

**Centrale**

- Enregistrements des capteurs, arroseur, client,
- Récéption des datas,
- Enregistrements des datas dans un fichier + dans BDD,
- Activation des arroseurs si data en dessous des seuils,
- Enregistre des logs pour toutes activités du capteurs.

**Client**

- Ajout de capteur et arroseur
- Manipulation du menu (affichage de data, etc)

## Updates
V3 -> Intégration des arroseurs, ajout d'une BDD.

V2 -> Réorganisation des fichiers, ajouts de nouvelles fonctionnalités, ajout d'une interface client.

V1 -> Création des capteurs et de la centrale. Les capteurs peuvent se connecter à la centrale. La centrale va recevoir les data et les enregistrer dans un fichier .txt.

## BDD
Les procédure stockées utilisées sont dans le dossier [SQL](/SQL/).

![](/IMG/tables.png)
*MCD de la base de données*