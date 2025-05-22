package org.example.projetjava.modele;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Création d'un enseignant
        Enseignant enseignant = new Enseignant(1, "Durand", "durand@ecole.fr", "azerty", "Maths");

        // Création d'un cours
        Cours cours = new Cours("Algèbre", "CM", 90, enseignant);

        // Création d'une salle
        Salle salle = new Salle("B204", 30, Arrays.asList("Vidéoprojecteur", "Tableau"));

        // Création d'un emploi du temps
        EmploiDuTemps edt = new EmploiDuTemps();

        // Ajout d'un créneau
        LocalDateTime debut = LocalDateTime.of(2024, 6, 3, 10, 0);
        LocalDateTime fin = debut.plusMinutes(cours.getDuree());
        EmploiDuTemps.Creneau c = new EmploiDuTemps.Creneau(cours, salle, debut, fin);

        edt.ajouterCreneau(c);

        // Affichage
        edt.afficher();

        Etudiant etu = new Etudiant(1, "Alice", "alice@etu.fr", "123", "E001");
        Enseignant ens = new Enseignant(2, "Bob", "bob@ens.fr", "456", "Maths");
        Administrateur admin = new Administrateur(3, "Claire", "admin@univ.fr", "admin");


        AuthService authService = new AuthService(Arrays.asList(etu, ens, admin));

        // Interface console pour la connexion
        Scanner scanner = new Scanner(System.in); // Créer un Scanner

        System.out.println("--- Connexion ---");
        System.out.print("Email : ");
        String email = scanner.nextLine(); // Lire l'email entré par l'utilisateur

        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine(); // Lire le mot de passe

        Utilisateur u = authService.login(email, motDePasse);


        if (u != null) {
            System.out.println("Connexion réussie : " + u);

            switch (u.getRole()) {
                case "Etudiant":
                    menuEtudiant((Etudiant) u, edt, scanner);
                    break;
                case "Enseignant":
                    menuEnseignant((Enseignant) u, edt, scanner);
                    break;
                case "Administrateur":
                    menuAdministrateur((Administrateur) u, edt, scanner);
                    break;

            }


        } else {
            System.out.println("Échec de la connexion.");
        }
    }

    public static void menuEnseignant(Enseignant enseignant, EmploiDuTemps edt, Scanner scanner) {
        int choix;

        do {
            System.out.println("\n--- Menu Enseignant ---");
            System.out.println("Bienvenue " + enseignant.getNom());
            System.out.println("1. Voir mes cours");
            System.out.println("2. Signaler un conflit");
            System.out.println("3. Quitter");

            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); // vider le buffer

            switch (choix) {
                case 1:
                    System.out.println("\nVos cours dans l'emploi du temps :");
                    for (EmploiDuTemps.Creneau c : edt.getCreneaux()) {
                        if (c.getCours().getEnseignant().equals(enseignant)) {
                            System.out.println("- " + c.getCours().getNom() +
                                    " le " + c.getDebut() + " en salle " + c.getSalle().getNom());
                        }
                    }
                    break;

                case 2:
                    System.out.println("Conflit signalé à l'administrateur (simulation).");
                    // Tu peux plus tard ajouter une vraie gestion de conflit ici
                    break;

                case 3:
                    System.out.println("Déconnexion...");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }

        } while (choix != 3);
    }


    public static void menuEtudiant(Etudiant etudiant, EmploiDuTemps edt, Scanner scanner) {
        int choix;

        do {
            System.out.println("\n--- Menu Étudiant ---");
            System.out.println("Bienvenue " + etudiant.getNom());
            System.out.println("1. Consulter mon emploi du temps");
            System.out.println("2. Voir mes salles de cours");
            System.out.println("3. Quitter");

            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); // vider le buffer

            switch (choix) {
                case 1:
                    System.out.println("\nVotre emploi du temps :");
                    edt.afficher();
                    break;

                case 2:
                    System.out.println("\nVos salles de cours :");
                    for (EmploiDuTemps.Creneau c : edt.getCreneaux()) {
                        System.out.println("Cours : " + c.getCours().getNom() + " → Salle : " + c.getSalle().getNom());
                    }
                    break;

                case 3:
                    System.out.println("Déconnexion...");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }
        } while (choix != 3);
    }
    public static void menuAdministrateur(Administrateur admin, EmploiDuTemps edt, Scanner scanner) {
        int choix;
        Enseignant profTemp = null;
        do {
            System.out.println("\n--- Menu Administrateur ---");
            System.out.println("Bienvenue " + admin.getNom());
            System.out.println("1. Ajouter un cours");
            System.out.println("2. Ajouter un créneau");
            System.out.println("3. Afficher les statistiques");
            System.out.println("4. Quitter");

            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); // vider le buffer

            switch (choix) {
                case 1:
                    System.out.println("Nom du cours : ");
                    String nomCours = scanner.nextLine();

                    System.out.println("Type (CM, TD, TP...) : ");
                    String type = scanner.nextLine();

                    System.out.println("Durée (en minutes) : ");
                    int duree = scanner.nextInt();
                    scanner.nextLine();

                 profTemp = new Enseignant(
                            admin.getId(),
                            admin.getNom(),
                            admin.getEmail(),
                            admin.getMotDePasse(),
                            "Administration"
                    );
                    Cours nouveauCours = new Cours(nomCours, type, duree, profTemp);

                    System.out.println("✅ Cours ajouté : " + nouveauCours.getNom());
                    break;

                case 2:
                    System.out.println("Nom de la salle : ");
                    String numeroSalle = scanner.nextLine();

                    System.out.println("Capacité : ");
                    int capacite = scanner.nextInt();
                    scanner.nextLine();

                    Salle nouvelleSalle = new Salle(numeroSalle, capacite, Arrays.asList("Tableau", "Projecteur"));

                    System.out.println("Date de début (AAAA-MM-JJ HH:mm) : ");
                    String dateStr = scanner.nextLine();
                    LocalDateTime debut = LocalDateTime.parse(dateStr.replace(" ", "T"));

                    System.out.println("Durée du créneau (en minutes) : ");
                    int dureeCreneau = scanner.nextInt();
                    scanner.nextLine();

                    LocalDateTime fin = debut.plusMinutes(dureeCreneau);
                    Cours coursTemp = new Cours("CoursAdmin", "CM", dureeCreneau, profTemp);
                    EmploiDuTemps.Creneau c = new EmploiDuTemps.Creneau(coursTemp, nouvelleSalle, debut, fin);
                    edt.ajouterCreneau(c);

                    System.out.println("✅ Créneau ajouté.");
                    break;

                case 3:
                    System.out.println("--- Statistiques ---");
                    System.out.println("Nombre total de créneaux : " + edt.getCreneaux().size());
                    break;

                case 4:
                    System.out.println("Déconnexion...");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }

        } while (choix != 4);
    }

}


