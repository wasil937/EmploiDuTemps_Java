package org.example.projetjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.projetjava.modele.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminTimetableManagementController {

    @FXML
    private ComboBox<String> userSelectionComboBox;

    @FXML
    private ListView<String> timetableDisplayListView;

    @FXML
    private Button createEntryButton;

    @FXML
    private Button deleteEntryButton;

    private Administrateur currentAdmin;
    private List<Utilisateur> allUsers = new ArrayList<>();
    private List<EmploiDuTemps.Creneau> allCreneaux = new ArrayList<>();
    private ObservableList<String> timetableEntries = FXCollections.observableArrayList();



    public AdminTimetableManagementController() {
    }


    @FXML
    public void initialize() {
        timetableDisplayListView.setItems(timetableEntries);

        ObservableList<String> userDisplayNames = FXCollections.observableArrayList();
        userDisplayNames.add("Tous les Emplois du Temps");
        // Utiliser SharedDataRepository.ALL_USERS
        for (Utilisateur user : SharedDataRepository.ALL_USERS) {
            String role = "";
            if (user instanceof Etudiant) role = " (Étudiant)";
            else if (user instanceof Enseignant) role = " (Enseignant)";
            if (!role.isEmpty()) {
                userDisplayNames.add(user.getNom() + role);
            }
        }
        userSelectionComboBox.setItems(userDisplayNames);
        userSelectionComboBox.getSelectionModel().selectFirst();
        userSelectionComboBox.setOnAction(event -> displayScheduleForSelection());
        timetableDisplayListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteEntryButton.setDisable(newSelection == null || newSelection.startsWith("--- ") || newSelection.trim().isEmpty());
        });
        displayScheduleForSelection();
    }

    private void displayScheduleForSelection() {
        timetableEntries.clear();
        String selection = userSelectionComboBox.getSelectionModel().getSelectedItem();
        if (selection == null) return;

        List<EmploiDuTemps.Creneau> creneauxToDisplay = new ArrayList<>();

        if (selection.equals("Tous les Emplois du Temps")) {
            creneauxToDisplay.addAll(SharedDataRepository.ALL_CRENEAUX);
        } else {
            String selectedUserName = selection.substring(0, selection.lastIndexOf(" (")).trim();

            Utilisateur selectedUser = SharedDataRepository.ALL_USERS.stream()
                    .filter(u -> u.getNom().equals(selectedUserName))
                    .findFirst()
                    .orElse(null);
            if (selectedUser != null) {
                if (selectedUser instanceof Enseignant) {

                    creneauxToDisplay = SharedDataRepository.ALL_CRENEAUX.stream()
                            .filter(c -> c.getCours().getEnseignant().getId() == selectedUser.getId())
                            .collect(Collectors.toList());
                } else if (selectedUser instanceof Etudiant) {
                    final int etudiantId = selectedUser.getId();

                    creneauxToDisplay = SharedDataRepository.ALL_CRENEAUX.stream()
                            .filter(c -> (c.getEtudiantConcerne() != null && c.getEtudiantConcerne().getId() == etudiantId) ||
                                    // Simulation d'inscription basée sur les données de démo
                                    (selectedUser.getNom().equals("Alice") && (c.getCours().getNom().equals("TP Java") || (c.getCours().getNom().equals("Maths Fondamentales") && c.getEtudiantConcerne() == null && c.getDebut().getHour() == 10 && c.getCours().getEnseignant().getNom().equals("Bob")))) ||
                                    (selectedUser.getNom().equals("Tom") && (c.getCours().getNom().equals("Maths Fondamentales") && c.getEtudiantConcerne() == null && c.getDebut().getHour() == 8 && c.getCours().getEnseignant().getNom().equals("Bob")))
                            )
                            .collect(Collectors.toList());
                }
            }
        }
        creneauxToDisplay.sort(Comparator.comparing(EmploiDuTemps.Creneau::getDebut));
        formatAndDisplayCreneaux(creneauxToDisplay);
    }


    @FXML
    private void handleCreateEntry(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/CreateCreneauDialog.fxml"));
            Parent dialogRoot = loader.load();

            CreateCreneauDialogController dialogController = loader.getController();
            // Passer les listes nécessaires, y compris la liste des créneaux existants
            dialogController.initializeData(
                    new ArrayList<>(SharedDataRepository.ALL_COURS),
                    new ArrayList<>(SharedDataRepository.ALL_SALLES),
                    new ArrayList<>(SharedDataRepository.ALL_USERS),
                    new ArrayList<>(SharedDataRepository.ALL_CRENEAUX)
            );

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Créer Nouveau Créneau");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(dialogRoot));
            dialogStage.showAndWait();

            EmploiDuTemps.Creneau nouveauCreneau = dialogController.getNouveauCreneau();
            if (nouveauCreneau != null) {
                SharedDataRepository.ALL_CRENEAUX.add(nouveauCreneau);
                displayScheduleForSelection();
                System.out.println("Nouveau créneau créé et ajouté globalement : " + nouveauCreneau);
            }

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du dialogue de création de créneau:");
            e.printStackTrace();

        }
    }
    @FXML
    private void handleDeleteEntry(ActionEvent event) {
        String selectedItemString = timetableDisplayListView.getSelectionModel().getSelectedItem();

        if (selectedItemString == null || selectedItemString.startsWith("--- ") || selectedItemString.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner un créneau valide à supprimer.");
            alert.showAndWait();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Êtes-vous sûr de vouloir supprimer le créneau suivant ?\n" + selectedItemString,
                ButtonType.YES, ButtonType.NO);


        Optional<ButtonType> result = confirmationAlert.showAndWait();


        if (result.isPresent() && result.get() == ButtonType.YES) {

            EmploiDuTemps.Creneau creneauASupprimer = null;

            for (EmploiDuTemps.Creneau c : SharedDataRepository.ALL_CRENEAUX) {
                String typeCours = (c.getCours().getType() != null) ? c.getCours().getType() : "N/A";
                String etudiantConcerneStr = "";
                if (c.getEtudiantConcerne() != null) {
                    etudiantConcerneStr = " - Pour Étudiant: " + c.getEtudiantConcerne().getNom();
                }
                String formattedCreneau = String.format("%s - %s : %s (%s) - Prof: %s - Salle: %s%s",
                        c.getDebut().format(DateTimeFormatter.ofPattern("HH:mm")),
                        c.getFin().format(DateTimeFormatter.ofPattern("HH:mm")),
                        c.getCours().getNom(),
                        typeCours,
                        c.getCours().getEnseignant().getNom(),
                        c.getSalle().getNumero(),
                        etudiantConcerneStr);

                if (formattedCreneau.equals(selectedItemString)) {
                    creneauASupprimer = c;
                    break;
                }
            }

            if (creneauASupprimer != null) {
                SharedDataRepository.ALL_CRENEAUX.remove(creneauASupprimer);
                System.out.println("Créneau supprimé : " + selectedItemString);
                displayScheduleForSelection();
            } else {
                System.out.println("Impossible de trouver le créneau à supprimer basé sur la chaîne sélectionnée.");
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Impossible de retrouver le créneau exact à supprimer.");
                errorAlert.showAndWait();
            }
        }
    }

    private void formatAndDisplayCreneaux(List<EmploiDuTemps.Creneau> creneaux) {
        timetableEntries.clear();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
        String currentDate = "";

        if (creneaux.isEmpty()) {
            timetableEntries.add("Aucun créneau à afficher pour cette sélection.");
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
            String etudiantConcerneStr = "";
            if (creneau.getEtudiantConcerne() != null) {
                etudiantConcerneStr = " - Pour Étudiant: " + creneau.getEtudiantConcerne().getNom();
            }

            String entry = String.format("%s - %s : %s (%s) - Prof: %s - Salle: %s%s",
                    creneau.getDebut().format(timeFormatter),
                    creneau.getFin().format(timeFormatter),
                    creneau.getCours().getNom(),
                    typeCours,
                    creneau.getCours().getEnseignant().getNom(),
                    creneau.getSalle().getNumero(),
                    etudiantConcerneStr
            );
            timetableEntries.add(entry);
        }
    }

    public void setAdministrateur(Administrateur admin) {
        this.currentAdmin = admin;
    }
}