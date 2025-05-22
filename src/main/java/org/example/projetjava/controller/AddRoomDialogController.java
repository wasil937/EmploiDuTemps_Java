package org.example.projetjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.projetjava.modele.Salle;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddRoomDialogController {

    @FXML
    private TextField numeroField;

    @FXML
    private TextField capaciteField;

    @FXML
    private TextField equipementsField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button addButton;

    private Salle nouvelleSalle = null;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    @FXML
    private void handleAddButtonAction(ActionEvent event) {
        String numero = numeroField.getText().trim();
        String capaciteStr = capaciteField.getText().trim();
        String equipementsStr = equipementsField.getText().trim();

        if (numero.isEmpty() || capaciteStr.isEmpty()) {
            showError("Le numéro et la capacité sont requis.");
            return;
        }

        int capacite;
        try {
            capacite = Integer.parseInt(capaciteStr);
            if (capacite <= 0) {
                showError("La capacité doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("La capacité doit être un nombre valide.");
            return;
        }

        List<String> equipementsList = Arrays.stream(equipementsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        // Vérification simple si la salle existe déjà (basée sur le numéro)
        this.nouvelleSalle = new Salle(numero, capacite, equipementsList);
        closeDialog();
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        this.nouvelleSalle = null;
        closeDialog();
    }

    public Salle getNouvelleSalle() {
        return nouvelleSalle;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void closeDialog() {
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
}