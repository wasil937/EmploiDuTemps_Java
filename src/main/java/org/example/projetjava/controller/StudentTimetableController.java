package org.example.projetjava.controller; // Assurez-vous que le package est correct

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.projetjava.modele.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List; // Importez java.util.List
import java.util.ArrayList; // Importez java.util.ArrayList
import java.util.Comparator; // Importez java.util.Comparator
import java.util.stream.Collectors;

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
        populateTimetable();
    }

    @FXML
    public void initialize() {
        timetableListView.setItems(timetableEntries);
    }

    private void updateWelcomeMessage() { // identique
        if (currentEtudiant != null) {
            welcomeLabel.setText("Emploi du Temps de " + currentEtudiant.getNom());
        }
    }

    private void populateTimetable() {
        if (currentEtudiant == null) return;
        timetableEntries.clear();

        final int etudiantId = currentEtudiant.getId();
        // Filtrer depuis SharedDataRepository.ALL_CRENEAUX
        List<EmploiDuTemps.Creneau> studentCreneaux = SharedDataRepository.ALL_CRENEAUX.stream()
                .filter(c ->
                        // Créneaux spécifiquement assignés à cet étudiant
                        (c.getEtudiantConcerne() != null && c.getEtudiantConcerne().getId() == etudiantId) ||
                                // OU créneaux de cours généraux auxquels cet étudiant est "inscrit" (logique de démo)
                                // La logique d'inscription ici est une simulation basée sur le nom de l'étudiant et le nom du cours.
                                // Une vraie application aurait une table d'inscription Etudiant-Cours.
                                (currentEtudiant.getNom().equals("Alice") && (c.getCours().getNom().equals("TP Java") || (c.getCours().getNom().equals("Maths Fondamentales") && c.getEtudiantConcerne() == null && c.getDebut().getHour()==10 && c.getCours().getEnseignant().getNom().equals("Bob")))) ||
                                (currentEtudiant.getNom().equals("Tom") && (c.getCours().getNom().equals("Maths Fondamentales") && c.getEtudiantConcerne() == null && c.getDebut().getHour()==8 && c.getCours().getEnseignant().getNom().equals("Bob")))
                )
                .sorted(Comparator.comparing(EmploiDuTemps.Creneau::getDebut))
                .collect(Collectors.toList());

        // La méthode formatAndDisplayCreneaux (ou une version adaptée) peut être utilisée ici
        // pour peupler timetableEntries, comme dans AdminTimetableManagementController
        formatAndDisplayCreneauxForStudent(studentCreneaux);
    }

    // Vous pouvez réutiliser une version de formatAndDisplayCreneaux ici
    private void formatAndDisplayCreneauxForStudent(List<EmploiDuTemps.Creneau> creneaux) {
        // Copiez la logique de formatage de AdminTimetableManagementController.formatAndDisplayCreneaux
        // ou rendez cette méthode statique/utilitaire si vous voulez la partager.
        // Pour l'instant, je la recopie pour la clarté :
        timetableEntries.clear();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
        String currentDate = "";

        if (creneaux.isEmpty()) {
            timetableEntries.add("Aucun cours programmé pour vous.");
            return;
        }

        for (EmploiDuTemps.Creneau creneau : creneaux) {
            String creneauDateStr = creneau.getDebut().format(dateFormatter);
            if (!creneauDateStr.equals(currentDate)) {
                if (!timetableEntries.isEmpty()) timetableEntries.add("");
                timetableEntries.add("--- " + creneauDateStr.toUpperCase() + " ---");
                currentDate = creneauDateStr;
            }
            String typeCours = (creneau.getCours().getType() != null) ? creneau.getCours().getType() : "N/A";
            // Pour la vue étudiant, on n'a pas besoin d'afficher "Pour Etudiant: X" si c'est leur propre EDT.
            // On peut afficher des détails différents si besoin.
            String entry = String.format("%s - %s : %s (%s) - Prof: %s - Salle: %s",
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