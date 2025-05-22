package org.example.projetjava.controller; // Assurez-vous que le package est correct

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.projetjava.modele.Cours;
import org.example.projetjava.modele.EmploiDuTemps;
import org.example.projetjava.modele.Enseignant;
import org.example.projetjava.modele.Etudiant;
import org.example.projetjava.modele.Salle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List; // Importez java.util.List
import java.util.ArrayList; // Importez java.util.ArrayList
import java.util.Comparator; // Importez java.util.Comparator

public class StudentTimetableController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private ListView<String> timetableListView; // Utilisation du ListView

    private Etudiant currentEtudiant;
    private ObservableList<String> timetableEntries = FXCollections.observableArrayList();

    public StudentTimetableController() {
        // Constructeur par défaut
    }

    public void setEtudiant(Etudiant etudiant) {
        this.currentEtudiant = etudiant;
        updateWelcomeMessage();
        populateTimetable(); // Peupler après que l'étudiant soit défini
    }

    @FXML
    public void initialize() {
        timetableListView.setItems(timetableEntries); // Lier la liste observable au ListView
    }

    private void updateWelcomeMessage() {
        if (currentEtudiant != null) {
            welcomeLabel.setText("Emploi du Temps de " + currentEtudiant.getNom());
        }
    }

    private void populateTimetable() {
        if (currentEtudiant == null) {
            return;
        }

        timetableEntries.clear();

        // Exemple de données
        Enseignant profMaths = new Enseignant(10, "Prof. Alpha", "alpha@ecole.fr", "pass", "Mathématiques");
        Enseignant profPhysique = new Enseignant(11, "Prof. Beta", "beta@ecole.fr", "pass", "Physique");
        Enseignant profInfo = new Enseignant(12, "Prof. Gamma", "gamma@ecole.fr", "pass", "Informatique");

        Salle salleA101 = new Salle("A101", 30, Arrays.asList("Vidéoprojecteur"));
        Salle salleB203 = new Salle("B203", 25, Arrays.asList("Tableau interactif"));
        Salle laboInfo3 = new Salle("INFO3", 20, Arrays.asList("PC", "Réseau"));

        Cours coursAlgebre = new Cours("Algèbre Linéaire", "CM", 90, profMaths);
        Cours coursOptique = new Cours("Optique Ondulatoire", "TD", 120, profPhysique);
        Cours coursJavaTP = new Cours("Programmation Java", "TP", 180, profInfo);
        Cours coursAnalyse = new Cours("Analyse Numérique", "CM", 90, profMaths);

        // Création d'une liste de créneaux d'exemple pour l'étudiant
        // Idéalement, cette liste proviendrait de votre classe EmploiDuTemps et serait filtrée.
        List<EmploiDuTemps.Creneau> studentCreneaux = new ArrayList<>(Arrays.asList(
                new EmploiDuTemps.Creneau(coursAlgebre, salleA101, LocalDateTime.of(2024, 5, 27, 8, 30), LocalDateTime.of(2024, 5, 27, 10, 0)),
                new EmploiDuTemps.Creneau(coursOptique, salleB203, LocalDateTime.of(2024, 5, 27, 10, 30), LocalDateTime.of(2024, 5, 27, 12, 30)),
                new EmploiDuTemps.Creneau(coursJavaTP, laboInfo3, LocalDateTime.of(2024, 5, 28, 14, 0), LocalDateTime.of(2024, 5, 28, 17, 0)),
                new EmploiDuTemps.Creneau(coursAnalyse, salleA101, LocalDateTime.of(2024, 5, 29, 9, 0), LocalDateTime.of(2024, 5, 29, 10, 30))
        ));

        // Trier les créneaux par date de début pour un affichage chronologique
        studentCreneaux.sort(Comparator.comparing(EmploiDuTemps.Creneau::getDebut));

        // Formatter pour l'affichage
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy"); // Ajout de l'année pour plus de clarté

        String currentDate = "";
        for (EmploiDuTemps.Creneau creneau : studentCreneaux) {
            String creneauDateStr = creneau.getDebut().format(dateFormatter);
            if (!creneauDateStr.equals(currentDate)) {
                if (!timetableEntries.isEmpty()) {
                    timetableEntries.add(""); // Ligne vide pour séparer les jours
                }
                timetableEntries.add("--- " + creneauDateStr.toUpperCase() + " ---");
                currentDate = creneauDateStr;
            }
            // Assurez-vous que la classe Cours a une méthode getType()
            // Si Cours.getType() n'existe pas, vous pouvez l'ajouter ou retirer cette partie de la chaîne.
            String typeCours = (creneau.getCours().getType() != null) ? creneau.getCours().getType() : "N/A";

            String entry = String.format("%s - %s : %s (%s) avec Prof. %s en salle %s",
                    creneau.getDebut().format(timeFormatter),
                    creneau.getFin().format(timeFormatter),
                    creneau.getCours().getNom(),
                    typeCours,
                    creneau.getCours().getEnseignant().getNom(),
                    creneau.getSalle().getNumero()
            );
            timetableEntries.add(entry);
        }
    }
}