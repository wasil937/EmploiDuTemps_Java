package org.example.projetjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.projetjava.modele.Administrateur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button manageCoursesButton;



    @FXML
    private Button manageRoomsButton;

    @FXML
    private Button viewStatisticsButton;



    @FXML
    private Label actionStatusLabel;

    private Administrateur currentAdmin;

    public void setAdministrateur(Administrateur admin) {
        this.currentAdmin = admin;
    }

    @FXML
    public void initialize() {
        actionStatusLabel.setText("");
    }

    private void updateWelcomeMessage() {
        if (currentAdmin != null) {
            welcomeLabel.setText("Panneau d'Administration - " + currentAdmin.getNom());
        }
    }



    @FXML
    private void handleManageRooms(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/AdminManageRoomsView.fxml"));
            Parent manageRoomsRoot = loader.load();



            Stage stage = new Stage();
            stage.setTitle("Gestion des Salles");
            stage.setScene(new Scene(manageRoomsRoot));

            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue de gestion des salles:");
            e.printStackTrace();
            if(actionStatusLabel != null) actionStatusLabel.setText("Erreur chargement vue Gestion Salles.");
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de l'ouverture de la gestion des salles:");
            e.printStackTrace();
            if(actionStatusLabel != null) actionStatusLabel.setText("Erreur inattendue. Consultez la console.");
        }
    }



    @FXML
    private void handleManageCourses(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/AdminTimetableManagementView.fxml"));
            Parent timetableManagementRoot = loader.load();

            AdminTimetableManagementController controller = loader.getController();
            if (controller != null) {
                controller.setAdministrateur(this.currentAdmin);
            } else {
                System.err.println("Erreur: AdminTimetableManagementController n'a pas pu être chargé.");
                actionStatusLabel.setText("Erreur interne (Contrôleur Gestion EDT).");
                return;
            }

            Stage stage = new Stage();
            stage.setTitle("Gestion des Emplois du Temps");
            stage.setScene(new Scene(timetableManagementRoot));



            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue de gestion des emplois du temps:");
            e.printStackTrace();
            actionStatusLabel.setText("Erreur chargement vue Gestion EDT.");
        } catch (Exception e) {
            System.err.println("Erreur inattendue :");
            e.printStackTrace();
            actionStatusLabel.setText("Erreur inattendue. Consultez la console.");
        }
    }

    @FXML
    private void handleViewStatistics(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/AdminStatisticsView.fxml"));
            Parent statsRoot = loader.load();



            Stage stage = new Stage();
            stage.setTitle("Statistiques d'Utilisation");
            stage.setScene(new Scene(statsRoot));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue des statistiques:");
            e.printStackTrace();
            if (actionStatusLabel != null) actionStatusLabel.setText("Erreur chargement vue Stats.");
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de l'ouverture des statistiques:");
            e.printStackTrace();
            if (actionStatusLabel != null) actionStatusLabel.setText("Erreur inattendue Stats. Consultez la console.");
        }
    }
}
