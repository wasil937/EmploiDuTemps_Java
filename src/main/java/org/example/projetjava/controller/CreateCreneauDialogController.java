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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class CreateCreneauDialogController {

    @FXML
    private ComboBox<String> coursComboBox; // Affichera les noms des cours

    @FXML
    private ComboBox<String> salleComboBox; // Affichera les numéros des salles

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private TextField heureDebutField;

    @FXML
    private TextField minuteDebutField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button creerButton;

    private List<Cours> availableCours;
    private List<Salle> availableSalles;
    private EmploiDuTemps.Creneau nouveauCreneau = null;

    public void initializeData(List<Cours> cours, List<Salle> salles) {
        this.availableCours = cours;
        this.availableSalles = salles;

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
            showError("Tous les champs sont requis.");
            return;
        }

        try {
            String selectedCoursString = coursComboBox.getSelectionModel().getSelectedItem();
            // Retrouver l'objet Cours correspondant (simplifié, suppose que le nom est unique pour cet exemple)
            Cours selectedCours = availableCours.stream()
                    .filter(c -> (c.getNom() + " (Prof: " + c.getEnseignant().getNom() + ")").equals(selectedCoursString))
                    .findFirst().orElse(null);

            String selectedSalleString = salleComboBox.getSelectionModel().getSelectedItem();
            // Retrouver l'objet Salle correspondant
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

            LocalDateTime debut = LocalDateTime.of(dateDebut, LocalTime.of(heureDebut, minuteDebut));
            LocalDateTime fin = debut.plusMinutes(selectedCours.getDuree()); // Utilise la durée du cours

            this.nouveauCreneau = new EmploiDuTemps.Creneau(selectedCours, selectedSalle, debut, fin);
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
        this.nouveauCreneau = null; // S'assurer qu'aucun créneau n'est retourné
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