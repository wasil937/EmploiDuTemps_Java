package org.example.projetjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.projetjava.modele.EmploiDuTemps;
import org.example.projetjava.modele.Enseignant;
import org.example.projetjava.modele.SharedDataRepository; // Si vous l'utilisez pour les créneaux

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherScheduleController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private ListView<String> scheduleListView;

    @FXML
    private Button reportButton;

    @FXML
    private Label actionStatusLabel;


    private Enseignant currentEnseignant;
    private ObservableList<String> scheduleEntries = FXCollections.observableArrayList();


    public TeacherScheduleController() {

    }

    public void setEnseignant(Enseignant enseignant) {
        this.currentEnseignant = enseignant;
        updateWelcomeMessage();
        populateSchedule();
    }

    @FXML
    public void initialize() {
        scheduleListView.setItems(scheduleEntries);
        if (actionStatusLabel != null) { // Bonne pratique de vérifier si le label est injecté
            actionStatusLabel.setText("");
        }
    }

    private void updateWelcomeMessage() {
        if (currentEnseignant != null) {
            welcomeLabel.setText("Emploi du Temps de Prof. " + currentEnseignant.getNom());
        }
    }

    private void populateSchedule() {
        if (currentEnseignant == null) {
            scheduleEntries.clear(); // Vider la liste si pas d'enseignant
            scheduleEntries.add("Aucun enseignant sélectionné.");
            return;
        }
        scheduleEntries.clear();

        // Filtrer les créneaux pour l'enseignant courant depuis SharedDataRepository
        List<EmploiDuTemps.Creneau> teacherCreneaux = SharedDataRepository.ALL_CRENEAUX.stream()
                .filter(creneau -> creneau.getCours().getEnseignant().getId() == currentEnseignant.getId())
                .sorted(Comparator.comparing(EmploiDuTemps.Creneau::getDebut))
                .collect(Collectors.toList());

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy"); // yyyy pour l'année

        if (teacherCreneaux.isEmpty()) {
            scheduleEntries.add("Aucun cours programmé pour vous.");
            return;
        }

        String currentDate = "";
        for (EmploiDuTemps.Creneau creneau : teacherCreneaux) {
            String creneauDateStr = creneau.getDebut().format(dateFormatter);
            if (!creneauDateStr.equals(currentDate)) {
                if (!scheduleEntries.isEmpty()) {
                    scheduleEntries.add("");
                }
                scheduleEntries.add("--- " + creneauDateStr.toUpperCase() + " ---");
                currentDate = creneauDateStr;
            }
            String typeCours = (creneau.getCours().getType() != null) ? creneau.getCours().getType() : "N/A";
            String entry = String.format("%s - %s : %s (%s) en salle %s",
                    creneau.getDebut().format(timeFormatter),
                    creneau.getFin().format(timeFormatter),
                    creneau.getCours().getNom(),
                    typeCours,
                    creneau.getSalle().getNumero()
            );
            scheduleEntries.add(entry);
        }
    }

    @FXML
    private void handleReportButtonAction(ActionEvent event) {
        if (currentEnseignant == null) {
            if (actionStatusLabel != null) actionStatusLabel.setText("Erreur: Enseignant non identifié.");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Impossible d'identifier l'enseignant pour le signalement.");
            errorAlert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/ReportDialog.fxml"));
            Parent dialogRoot = loader.load();

            ReportDialogController dialogController = loader.getController();
            dialogController.setReportingTeacher(this.currentEnseignant); // Passer l'enseignant actuel au dialogue

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Faire un Signalement");
            dialogStage.initModality(Modality.APPLICATION_MODAL); // Bloque la fenêtre parente
            // Stage ownerStage = (Stage) reportButton.getScene().getWindow(); // Optionnel: définir le propriétaire
            // dialogStage.initOwner(ownerStage);
            dialogStage.setScene(new Scene(dialogRoot));

            dialogStage.showAndWait(); // Attendre que le dialogue soit fermé

            String reportMade = dialogController.getReportContent();
            if (reportMade != null && !reportMade.isEmpty()) {
                // Pour la démo, on affiche juste un message.
                // Idéalement, on ajouterait ce signalement à une liste dans SharedDataRepository

                if (actionStatusLabel != null) {
                    actionStatusLabel.setText("Signalement envoyé (simulé). Contenu loggué.");
                }
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Votre signalement a été enregistré (simulation).\nContenu: " + reportMade);
                infoAlert.setHeaderText("Signalement Pris en Compte");
                infoAlert.showAndWait();
            } else {
                if (actionStatusLabel != null) {
                    actionStatusLabel.setText("Signalement annulé.");
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du dialogue de signalement:");
            e.printStackTrace();
            if (actionStatusLabel != null)
                actionStatusLabel.setText("Erreur: Impossible d'ouvrir le dialogue de signalement.");
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir le formulaire de signalement.");
            errorAlert.showAndWait();
        }
    }
}