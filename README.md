# Java-Apis

La partie API de mon projet avorté de creation d'entreprise.
Pour ce projet j'avais besoin de creer une application web pour gerer un conservatoire. Gestion des etudiants, des notes, de la comptabilité, des occupations des salles, des spectacles, des diplomes etc.

Toutes ces applications sont des API RESTful et utilisent le framework Java Spring. 

Vous trouverez dans cette partie trois API decrite ci dessous.

## Authentification

Cette API est fortement inspiré d'OAuth 2. Elle permet de mettre en place un systeme de login via des token.chaques token est lié a un utilisateur ainsi qu'a une application. Ces tokens sont necessaires pour acceder a une ressource (voir les API suivantes)

## Conservatoire User

Cette api permet de creer et stocker les informations sur les utilisateurs concernant le conservatoire.
Exemple d'informations:
- Les informations administratives (Nom, adresses, parents si mineurs etc.)
- La classes ou section en fonction de l'etablissement
- Les notes

## Time management

Cette API a pour but de gerer les emplois du temps de toutes sortes.
Ca peut etre des emplois du temps de personnes (Eleves/Proffesseur ou tout autre personnels/Source exterrieure pour les spectacles), de salles pour en connaitres la disponibilité.

