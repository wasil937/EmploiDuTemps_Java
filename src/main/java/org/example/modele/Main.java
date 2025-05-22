package org.example.modele;

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

        Utilisateur u = authService.login("alice@etu.fr", "123");

        if (u != null) {
            System.out.println("Connexion réussie : " + u);
        } else {
            System.out.println("Échec de la connexion.");
        }
    }
}
