package org.example.projetjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.projetjava.modele.Cours;
import org.example.projetjava.modele.EmploiDuTemps;
import org.example.projetjava.modele.Salle;
import org.example.projetjava.modele.Etudiant;
import org.example.projetjava.modele.Utilisateur;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class CreateCreneauDialogController {


    @FXML private ComboBox<String> coursComboBox;
    @FXML private ComboBox<String> salleComboBox;
    @FXML private DatePicker dateDebutPicker;
    @FXML private TextField heureDebutField;
    @FXML private TextField minuteDebutField;
    @FXML private ComboBox<String> etudiantComboBox;
    @FXML private Label errorLabel;
    @FXML private Button creerButton;


    private List<Cours> availableCours;
    private List<Salle> availableSalles;
    private List<Etudiant> availableEtudiants;
    private List<EmploiDuTemps.Creneau> allExistingCreneaux; // NOUVEAU CHAMP pour stocker les créneaux existants

    private EmploiDuTemps.Creneau nouveauCreneau = null;


    public void initializeData(List<Cours> cours, List<Salle> salles, List<Utilisateur> tousLesUtilisateurs, List<EmploiDuTemps.Creneau> existingCreneaux) {
        this.availableCours = cours;
        this.availableSalles = salles;
        this.allExistingCreneaux = existingCreneaux; // Stocker les créneaux existants

        this.availableEtudiants = tousLesUtilisateurs.stream()
                .filter(u -> u instanceof Etudiant)
                .map(u -> (Etudiant) u)
                .collect(Collectors.toList());

        ObservableList<String> coursNoms = FXCollections.observableArrayList();
        for (Cours c : availableCours) {
            coursNoms.add(c.getNom() + " (Prof: " + c.getEnseignant().getNom() + ")");
        }
        coursComboBox.setItems(coursNoms);

        ObservableList<String> salleNumeros = FXCollections.observableArrayList();
        for (Salle s : availableSalles) {
            salleNumeros.add(s.getNumero());
        }
        salleComboBox.setItems(salleNumeros);

        ObservableList<String> etudiantNoms = FXCollections.observableArrayList();
        etudiantNoms.add("Aucun / Général");
        for (Etudiant e : availableEtudiants) {
            etudiantNoms.add(e.getNom());
        }
        etudiantComboBox.setItems(etudiantNoms);
        etudiantComboBox.getSelectionModel().selectFirst();


        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    @FXML
    private void handleCreerButtonAction(ActionEvent event) {
        if (coursComboBox.getSelectionModel().isEmpty() ||
                salleComboBox.getSelectionModel().isEmpty() ||
                dateDebutPicker.getValue() == null ||
                heureDebutField.getText().trim().isEmpty() ||
                minuteDebutField.getText().trim().isEmpty()) {
            showError("Champs cours, salle, date et heure/minute requis.");
            return;
        }

        try {
            String selectedCoursString = coursComboBox.getSelectionModel().getSelectedItem();
            Cours selectedCours = availableCours.stream()
                    .filter(c -> (c.getNom() + " (Prof: " + c.getEnseignant().getNom() + ")").equals(selectedCoursString))
                    .findFirst().orElse(null);

            String selectedSalleString = salleComboBox.getSelectionModel().getSelectedItem();
            Salle selectedSalle = availableSalles.stream()
                    .filter(s -> s.getNumero().equals(selectedSalleString))
                    .findFirst().orElse(null);

            if (selectedCours == null || selectedSalle == null) {
                showError("Sélection de cours ou salle invalide.");
                return;
            }

            LocalDate dateDebut = dateDebutPicker.getValue();
            int heureDebut = Integer.parseInt(heureDebutField.getText().trim());
            int minuteDebut = Integer.parseInt(minuteDebutField.getText().trim());

            if (heureDebut < 0 || heureDebut > 23 || minuteDebut < 0 || minuteDebut > 59) {
                showError("Heure ou minute invalide.");
                return;
            }

            LocalDateTime potentialNewDebut = LocalDateTime.of(dateDebut, LocalTime.of(heureDebut, minuteDebut));
            LocalDateTime potentialNewFin = potentialNewDebut.plusMinutes(selectedCours.getDuree());

            // Vérification des conflits de salles
            for (EmploiDuTemps.Creneau existingCreneau : allExistingCreneaux) {
                if (existingCreneau.getSalle().getNumero().equals(selectedSalle.getNumero())) {
                    // La salle est la même, vérifier si les horaires se chevauchent
                    // Chevauchement si: (StartA < EndB) and (EndA > StartB)
                    if (potentialNewDebut.isBefore(existingCreneau.getFin()) &&
                            potentialNewFin.isAfter(existingCreneau.getDebut())) {

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm 'le' dd/MM");
                        showError("Salle '" + selectedSalle.getNumero() + "' déjà occupée par '" +
                                existingCreneau.getCours().getNom() + "' de " +
                                existingCreneau.getDebut().format(formatter) + " à " +
                                existingCreneau.getFin().format(formatter) + ".");
                        return; // Arrêter la création
                    }
                }
            }



            Etudiant selectedEtudiant = null;
            String selectedEtudiantString = etudiantComboBox.getSelectionModel().getSelectedItem();
            if (selectedEtudiantString != null && !selectedEtudiantString.equals("Aucun / Général")) {
                selectedEtudiant = availableEtudiants.stream()
                        .filter(e -> e.getNom().equals(selectedEtudiantString))
                        .findFirst().orElse(null);
            }

            this.nouveauCreneau = new EmploiDuTemps.Creneau(selectedCours, selectedSalle, potentialNewDebut, potentialNewFin, selectedEtudiant);
            closeDialog();

        } catch (NumberFormatException e) {
            showError("Format heure/minute incorrect. Utilisez des nombres.");
        } catch (Exception e) {
            showError("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAnnulerButtonAction(ActionEvent event) {
        this.nouveauCreneau = null;
        closeDialog();
    }

    public EmploiDuTemps.Creneau getNouveauCreneau() {
        return nouveauCreneau;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void closeDialog() {
        Stage stage = (Stage) creerButton.getScene().getWindow();
        stage.close();
    }
}