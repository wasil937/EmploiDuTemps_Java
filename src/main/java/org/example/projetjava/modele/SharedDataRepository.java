package org.example.projetjava.modele;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedDataRepository {

    // Listes statiques pour partager les données à travers l'application
    public static final List<Utilisateur> ALL_USERS = new ArrayList<>();
    public static final List<Cours> ALL_COURS = new ArrayList<>();
    public static final List<Salle> ALL_SALLES = new ArrayList<>();
    public static final List<EmploiDuTemps.Creneau> ALL_CRENEAUX = new ArrayList<>();

    // Drapeau pour s'assurer que les données ne sont initialisées qu'une seule fois
    private static boolean isDataInitialized = false;

    public static synchronized void initializeData() {
        if (isDataInitialized) {
            return; // Ne pas réinitialiser si déjà fait
        }

        // Vider les listes au cas où (pour des redémarrages propres pendant le dev)
        ALL_USERS.clear();
        ALL_COURS.clear();
        ALL_SALLES.clear();
        ALL_CRENEAUX.clear();

        // === Initialisation des Salles ===
        Salle salleC101 = new Salle("C101", 40, Arrays.asList("Vidéoprojecteur", "PC Enseignant"));
        Salle salleD202 = new Salle("D202", 30, Arrays.asList("Tableau Blanc"));
        Salle laboInfo3 = new Salle("INFO3", 20, Arrays.asList("PC", "Réseau"));
        ALL_SALLES.addAll(Arrays.asList(salleC101, salleD202, laboInfo3));

        // === Initialisation des Utilisateurs (Enseignants, Étudiants, Admin) ===
        Enseignant enseignantBob = new Enseignant(2, "Bob", "bob@ens.fr", "456", "Maths");
        Enseignant enseignantAliceProf = new Enseignant(4, "Alice Prof", "alice.prof@ens.fr", "prof123", "Physique");
        // S'assurer que les IDs sont uniques
        Etudiant etudiantAlice = new Etudiant(1, "Alice", "alice@etu.fr", "123", "E001");
        Etudiant etudiantTom = new Etudiant(5, "Tom", "tom@etu.fr", "789", "E002");
        Administrateur adminClaire = new Administrateur(3, "Claire", "admin@univ.fr", "admin");

        ALL_USERS.addAll(Arrays.asList(enseignantBob, enseignantAliceProf, etudiantAlice, etudiantTom, adminClaire));

        // === Initialisation des Cours ===
        Cours coursMathsBob = new Cours("Maths Fondamentales", "CM", 120, enseignantBob);
        Cours coursAlgebreBob = new Cours("Algèbre Avancée", "TD", 90, enseignantBob);
        Cours coursPhysiqueAliceP = new Cours("Physique Quantique", "CM", 120, enseignantAliceProf); // Renommé pour éviter confusion avec etudiantAlice
        Cours coursProgJava = new Cours("TP Java", "TP", 180, enseignantAliceProf);
        ALL_COURS.addAll(Arrays.asList(coursMathsBob, coursAlgebreBob, coursPhysiqueAliceP, coursProgJava));

        // === Initialisation des Créneaux ===
        // Pour Bob
        ALL_CRENEAUX.add(new EmploiDuTemps.Creneau(coursMathsBob, salleC101,
                LocalDateTime.of(2024, 5, 27, 10, 0), LocalDateTime.of(2024, 5, 27, 12, 0)));
        ALL_CRENEAUX.add(new EmploiDuTemps.Creneau(coursAlgebreBob, salleD202,
                LocalDateTime.of(2024, 5, 28, 14, 0), LocalDateTime.of(2024, 5, 28, 15, 30)));
        // Pour Alice Prof
        ALL_CRENEAUX.add(new EmploiDuTemps.Creneau(coursPhysiqueAliceP, salleC101,
                LocalDateTime.of(2024, 5, 27, 14, 0), LocalDateTime.of(2024, 5, 27, 16, 0)));
        // Un cours TP que l'étudiant Alice pourrait suivre (enseigné par Alice Prof)
        // Créneau spécifiquement pour Etudiant Alice
        ALL_CRENEAUX.add(new EmploiDuTemps.Creneau(coursProgJava, laboInfo3,
                LocalDateTime.of(2024, 5, 29, 9, 0), LocalDateTime.of(2024, 5, 29, 12, 0), etudiantAlice));
        // Pour l'étudiant Tom (un autre cours avec Bob)
        ALL_CRENEAUX.add(new EmploiDuTemps.Creneau(coursMathsBob, salleD202,
                LocalDateTime.of(2024, 5, 30, 8, 0), LocalDateTime.of(2024, 5, 30, 10, 0), etudiantTom));

        isDataInitialized = true;
        System.out.println("SharedDataRepository initialisé avec " + ALL_USERS.size() + " utilisateurs, " +
                ALL_COURS.size() + " cours, " + ALL_SALLES.size() + " salles, " +
                ALL_CRENEAUX.size() + " créneaux.");
    }
}