1
II.1102 Projet Algorithmique et
Programmation JAVA
A.U. : 2024-2025
1. Informations générales
   Le module II.1102 propose aux étudiants de travailler sur un projet de développement Java.
   Cette année, le projet portera sur le développement d’une application de gestion des emplois
   du temps avec toutes les fonctionnalités requises. Plus d’informations sur ce sujet en section
2. Avant de décrire les besoins de ce projet, voici quelques consignes générales :
1. Vous devrez former des équipes de 2 ou 3 étudiants en saisissant le formulaire relatif
   à chaque groupe d’enseignement dans la section ‘Projet Java’ sur l’espace du cours
   Moodle (Les équipes se font au sein des mêmes groupes d’enseignement).
2. Les codes sources de vos projets vont être comparés afin de détecter toute tentative
   de partage de code. Si le moindre soupçon de triche est présent, vous passerez en
   audition devant vos enseignants pour prouver que vous êtes les auteurs de vos
   propres projets.
3. Il est absolument interdit d’utiliser les outils de la génération du code. Ceci sera
   considéré comme un plagiat flagrant.
4. Des bonus pourront être attribués pour des fonctionnalités originales de votre choix.
2. Présentation du projet : Système de Gestion des Emplois du
   Temps dans une université
   Une école d’ingénieurs souhaite moderniser son système d’information en introduisant de
   nouveaux logiciels dans les processus de gestion. Ainsi, le directeur des études propose de
   concevoir une application complète et évolutive pour gérer les emplois du temps scolaires, avec
   prise en compte des contraintes de disponibilité, de capacité de salle, de type de cours, et un
   accès différencié selon le profil (Administrateurs, enseignant et étudiant).
   L'application devra permettre aux administrateurs de créer et de modifier les emplois du temps
   des différentes facultés, de gérer les salles de cours, d'affecter des enseignants aux cours, et de
   consulter l'emploi du temps de chaque étudiant et enseignant.
   Une application de gestion des emplois du temps permettra à l’entreprise d’atteindre les
   objectifs suivants :
   • Optimiser la gestion du temps pour permettre à l'utilisateur de planifier et organiser
   efficacement ses activités ou ses cours,
   • Minimiser les conflits d'emploi du temps (par exemple, éviter que deux événements
   importants ne se chevauchent)
   • Fournir une interface facile à comprendre, permettant de visualiser rapidement les
   horaires. en permettant aux utilisateurs de partager des informations, des fichiers et des
   mises à jour en temps réel,
   2
   • Permettre une personnalisation avancée pour gérer les conflits d’emplois de temps.
3. Fonctionnalités du projet
   3.1. Rôle utilisateurs
   • Étudiants :
- Consulter leur emploi du temps (heures, matières, enseignants)
- Voir les informations de leurs salles de cours (numéro de salle, localisation,
  etc.)
- Recevoir des notifications en cas de changements dans l’emploi du temps
  (cours annulé, changement d’heure)
  • Enseignants :
- Consulter leur emploi du temps
- Accéder aux informations des cours qu'ils enseignent (salles, horaires,
  étudiants)
- Notifier les administrateurs de toute anomalie (ex : double réservation de
  salle)
  • Administrateurs :
- Créer et modifier les emplois du temps (cours, horaires, salles)
- Affecter des enseignants aux cours
- Gérer les salles (capacité, équipement disponible, etc.)
- Superviser la gestion des conflits (ex : doublons de salle ou horaires)
- Suivre les statistiques sur l'utilisation des salles et des enseignants
  3.2. Fonctionnalités attendues
  • Gestion des Cours et des Emplois du Temps :
- Création de cours avec horaires et salles associés.
- Gestion des créneaux horaires : possibilité de réorganiser les horaires en cas de
  besoin.
- Attribution automatique ou manuelle des enseignants aux cours.
- Visualisation de l'emploi du temps par jour, semaine, mois.
  • Gestion des Salles de Cours :
- Affectation des salles en fonction de la capacité et de la disponibilité.
- Gestion des équipements disponibles dans chaque salle.
- Contrôle des conflits d'affectation de salle (ex : double réservation).
  • Authentification et Sécurisation :
- Authentification des utilisateurs avec gestion des rôles (étudiants, enseignants,
  administrateurs).
  • Interface utilisateur conviviale :
- Interface graphique permettant une utilisation facile et intuitive de l'application.
- Navigation fluide entre les différentes fonctionnalités.
  3
  3.3. Fonctionnalités avancées
  • Recherche et filtre :
- Filtrer les emplois du temps par période, matière, enseignant, etc.
- Recherche intelligente de disponibilités (ex : trouver un créneau libre pour un TP)
- Possibilité d'imprimer ou d'exporter les emplois dans différents formats
  (PDF, CSV, etc.).
  • Statistique et ergonomie :
- Graphiques de visualisation des données,
- Nombre d’heures par prof/matière,
- Taux d’occupation des salles.
  • Notifications et Alertes :
- Notifications automatiques aux étudiants en cas de modification d’emploi du
  temps.
- Alertes pour les enseignants en cas de problème avec leurs horaires.
- Notifications en cas de conflit dans l’attribution des salles.
4. Contraintes techniques
   • L'application doit être développée en Java en utilisant des concepts de programmation
   orientée objet,
   • Utilisation de structures de données appropriées pour le stockage des informations
   (tableaux, listes, etc.),
   • Utilisation du JavaFX pour la création des interfaces de l’application,
   • Utilisation éventuelle de bases de données pour le stockage persistant des données
   (optionnel).
5. Livrables
   Plusieurs livrables seront à déposer sur Moodle durant le semestre :
   • Un premier concernant la modélisation UML de votre projet. Il est important de bien
   modéliser votre projet pour ensuite concevoir un programme plus robuste et plus
   facile à modifier si nécessaire (le vendredi 16 mai 2025).
   • En fin de semestre, il faudra rendre un document technique décrivant
   l’implémentation de votre projet ainsi que le code source du projet (Au plus tard,
   Jeudi 22 mai 2025).
   • Une soutenance sera planifiée dans la dernière séance du semestre (le vendredi 23
   mai 2025) au cours de laquelle vous devrez expliciter la conception de votre projet et
   en faire une démonstration.
   Plus d’informations vous seront communiquées au fur et à mesure sur Moodle. 