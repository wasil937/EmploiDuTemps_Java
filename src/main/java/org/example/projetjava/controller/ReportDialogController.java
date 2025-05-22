package org.example.projetjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.example.projetjava.modele.Enseignant; // Pour savoir qui signale

public class ReportDialogController {

    @FXML
    private TextArea reportTextArea;

    @FXML
    private Label errorLabel;

    @FXML
    private Button sendButton;

    private Enseignant reportingTeacher;
    private String reportContent = null; // Pour stocker le signalement

    public void setReportingTeacher(Enseignant teacher) {
        this.reportingTeacher = teacher;
    }

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    @FXML
    private void handleSendReportAction(ActionEvent event) {
        String reportText = reportTextArea.getText().trim();
        if (reportText.isEmpty()) {
            showError("Le contenu du signalement ne peut pas être vide.");
            return;
        }

        if (reportText.length() < 10) { // Validation très simple de la longueur
            showError("Veuillez fournir une description plus détaillée (min 10 caractères).");
            return;
        }

        // Stocker le contenu du signalement
        this.reportContent = reportText;

        // Afficher dans la console qui a signalé et quoi (pour la démo)
        System.out.println("Signalement envoyé par: " + (reportingTeacher != null ? reportingTeacher.getNom() : "Enseignant inconnu"));
        System.out.println("Contenu du signalement: " + reportContent);

        // (Logique future: ajouter à SharedDataRepository.ALL_REPORTS)

        closeDialog();
    }

    @FXML
    private void handleCancelReportAction(ActionEvent event) {
        this.reportContent = null; // S'assurer qu'aucun signalement n'est retourné
        closeDialog();
    }

    public String getReportContent() {
        return reportContent;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void closeDialog() {
        Stage stage = (Stage) sendButton.getScene().getWindow();
        stage.close();
    }
}