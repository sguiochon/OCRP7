# OCR - Projet 7 - Batch et Webapp

## Introduction
Ce projet Maven est un projet multimodules contenant le batch et la webapp.
Ces deux applications partagent deux librairies communes constituant chacune un module spécifique:
1. la modélisation du domaine métier (module model-library)
2. les web client consommant l'API REST.


# Le traitement Batch

Le module de cette application est dans le répertoire `batchprocessor`.

## Description
### Mode d'exécution
L'application dispose d'un scheduler interne qui permet de lancer le traitement à la fréquence souhaitée. Son fonctionnement normal est donc celui d'un daemon (fonctionnant en permanence).

La configuration du scheduler est réalisée via la ligne #51 du fichier `application.properties` via la propriété `application.cron.pattern`. La valeur fournie par défaut permet d'exécuter le traitement toutes les 30s. Un exemple est donné pour un traitement une fois par jour. Si nécessaire, se référer à la [documentation Spring](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html).

## Configuration

### Activation/désactivation de l'envoi d'emails
Deux profils spring sont utilisables:
1. le profil `dev`, dans lequel la liste des mails est affichée à l'écran, aucun mail n'étant effectivement envoyé,
2. le profil `prod`, dans lequel les mails sont effectivement envoyés à un serveur SMTP.

Le choix du mode est réalisé dans le fichier `applications.properties`, ligne #1

Si le profil `prod` est choisi, il convient d'indiquer les informations permettant la connexion à un serveur SMTP dans les lignes 58 à 68 du fichier `application.properties`. 

### Choix de la date de retour des prêts
Le fichier `application.properties` permet de spécifier une date limite de retour des prêts (ligne #4, propriété `batch.findByEndDateBefore.forcedValue`). Si aucune date n'est spécifiée, la date courante est utilisée (c'est le fonctionnement nominal de l'application).
En revanche, si une date est renseignée (ce qui est le cas dans le fichier), il est alors possible de lancer un traitement dans le cas d'un 'rattrapage' ou pour effectuer des tests.

### Paramètrage des web clients
Les informations nécessaires à l'utilisation de l'API REST sont indiquées dans le fichier `application.properties`.

### Log
La configuration des logs (niveau et appenders) est réalisée dans le fichier `logback.xml`. Par défaut, l'enregistrement dans un fichier est désactivé (ligne #50).

## Exécution
Pre-requis: l'API doit être en cours d'exécution.
1. Depuis le répertoire `Batch-Webapp`, lancer la commande `mvn install`
2. A la fin de la compilation, se placer dans le répertoire `batchprocessor` puis lancer la commande `mvn spring-boot:run` 


# L'application web

## Configuration

#### Port d'écoute
Il est indiqué ligne #2, dans le fichier `application.properties`. Sa valeur par défaut est 9991.


### Paramètrage des web clients
Les informations nécessaires à l'utilisation de l'API REST sont indiquées dans le fichier `application.properties`.

### Log
La configuration des logs (niveau et appenders) est réalisée dans le fichier `logback.xml`. Par défaut, l'enregistrment dans un fichier est désactivé (ligne #49).

## Exécution
Pre-requis: l'API doit être en cours d'exécution.
1. Depuis le répertoire `Batch-Webapp`, lancer la commande `mvn install`
2. A la fin de la compilation, se placer dans le répertoire `webapplication` puis lancer la commande `mvn spring-boot:run` 

L'application est alors exposée à l'adresse [http://localhost:9991](http://localhost:9991)

## Fonctionnalités

Le mode non connecté permet d'accéder aux fonctionnalités suivantes:
- Recherche d'ouvrages par mot clé
- Connexion 

Un compte de test est disponible disposant de l'identifiant **harry@hell.com** et du mot de passe **1234**.

En mode connecté, les fonctionnalités suivantes sont accessibles:
- visualisation du nombre d'exemplaires disponibles au prêt pour un document donné
- consultation des prêts en cours
- extension d'un prêt (1 seule fois)