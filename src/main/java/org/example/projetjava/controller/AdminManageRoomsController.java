package org.example.projetjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.projetjava.modele.Salle;
import org.example.projetjava.modele.SharedDataRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminManageRoomsController {

    @FXML
    private TableView<Salle> roomsTableView;

    @FXML
    private TableColumn<Salle, String> numeroColumn;

    @FXML
    private TableColumn<Salle, Integer> capaciteColumn;

    @FXML
    private TableColumn<Salle, String> equipementsColumn;

    @FXML
    private Button deleteRoomButton;

    @FXML
    private Button addRoomButton;

    @FXML
    public void initialize() {
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capacite"));

        equipementsColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        String.join(", ", cellData.getValue().getEquipements() != null ? cellData.getValue().getEquipements() : List.of("N/A"))
                )
        );

        loadSallesData();


        roomsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            deleteRoomButton.setDisable(newSelection == null);
        });
    }

    private void loadSallesData() {
        ObservableList<Salle> sallesList = FXCollections.observableArrayList(SharedDataRepository.ALL_SALLES);
        roomsTableView.setItems(sallesList);
        if (sallesList.isEmpty()) {
            System.out.println("Aucune salle à afficher depuis SharedDataRepository.");
        }
    }

    @FXML
    private void handleDeleteRoom(ActionEvent event) {
        Salle selectedSalle = roomsTableView.getSelectionModel().getSelectedItem();

        if (selectedSalle == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une salle à supprimer.");
            alert.showAndWait();
            return;
        }

        // Vérification : La salle est-elle utilisée dans des créneaux ?
        boolean isSalleUtilisee = SharedDataRepository.ALL_CRENEAUX.stream()
                .anyMatch(creneau -> creneau.getSalle().getNumero().equals(selectedSalle.getNumero()));

        if (isSalleUtilisee) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Cette salle est actuellement utilisée dans un ou plusieurs créneaux et ne peut pas être supprimée.\n" +
                            "Veuillez d'abord modifier ou supprimer les créneaux concernés.");
            alert.setHeaderText("Suppression Impossible");
            alert.showAndWait();
            return;
        }

        // Confirmation de suppression
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Êtes-vous sûr de vouloir supprimer la salle : " + selectedSalle.getNumero() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmationAlert.setHeaderText("Confirmation de Suppression");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            boolean removed = SharedDataRepository.ALL_SALLES.remove(selectedSalle);
            if (removed) {
                System.out.println("Salle supprimée : " + selectedSalle.getNumero());
                loadSallesData(); // Recharger les données pour mettre à jour le TableView

                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "La salle a été supprimée avec succès.");
                infoAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression de la salle.");
                errorAlert.showAndWait();
            }
        }
    }
    @FXML
    private void handleAddRoom(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/AddRoomDialog.fxml"));
            Parent dialogRoot = loader.load();

            AddRoomDialogController dialogController = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter une Nouvelle Salle");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            dialogStage.setScene(new Scene(dialogRoot));

            dialogStage.showAndWait(); // Attendre que le dialogue soit fermé

            Salle nouvelleSalle = dialogController.getNouvelleSalle();
            if (nouvelleSalle != null) {
                // Vérifier si une salle avec le même numéro existe déjà (simple vérification)
                boolean salleExisteDeja = SharedDataRepository.ALL_SALLES.stream()
                        .anyMatch(salle -> salle.getNumero().equalsIgnoreCase(nouvelleSalle.getNumero()));

                if (salleExisteDeja) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Une salle avec le numéro '" + nouvelleSalle.getNumero() + "' existe déjà.");
                    alert.showAndWait();
                } else {
                    SharedDataRepository.ALL_SALLES.add(nouvelleSalle); // Ajouter à la liste partagée
                    loadSallesData(); // Rafraîchir le TableView
                    System.out.println("Nouvelle salle ajoutée : " + nouvelleSalle.getNumero());

                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "La salle '" + nouvelleSalle.getNumero() + "' a été ajoutée avec succès.");
                    infoAlert.showAndWait();
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du dialogue d'ajout de salle:");
            e.printStackTrace();
            // Afficher une alerte à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible d'ouvrir le formulaire d'ajout de salle.");
            alert.showAndWait();
        }
    }
}
