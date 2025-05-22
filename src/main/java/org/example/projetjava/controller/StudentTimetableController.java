package org.example.projetjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.projetjava.modele.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
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

    private void updateWelcomeMessage() {
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

                                (currentEtudiant.getNom().equals("Alice") && (c.getCours().getNom().equals("TP Java") || (c.getCours().getNom().equals("Maths Fondamentales") && c.getEtudiantConcerne() == null && c.getDebut().getHour()==10 && c.getCours().getEnseignant().getNom().equals("Bob")))) ||
                                (currentEtudiant.getNom().equals("Tom") && (c.getCours().getNom().equals("Maths Fondamentales") && c.getEtudiantConcerne() == null && c.getDebut().getHour()==8 && c.getCours().getEnseignant().getNom().equals("Bob")))
                )
                .sorted(Comparator.comparing(EmploiDuTemps.Creneau::getDebut))
                .collect(Collectors.toList());


        formatAndDisplayCreneauxForStudent(studentCreneaux);
    }


    private void formatAndDisplayCreneauxForStudent(List<EmploiDuTemps.Creneau> creneaux) {

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