package org.example.projetjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.projetjava.modele.Administrateur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality; // Pour ouvrir dans une nouvelle fenêtre modale (optionnel)
import javafx.stage.Stage;
import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button manageCoursesButton;

    @FXML
    private Button manageTeachersButton;

    @FXML
    private Button manageRoomsButton;

    @FXML
    private Button manageConflictsButton;

    @FXML
    private Button viewStatisticsButton;

    @FXML
    private Button manageUsersButton;

    @FXML
    private Label actionStatusLabel;

    private Administrateur currentAdmin;

    public void setAdministrateur(Administrateur admin) {
        this.currentAdmin = admin;
        // Vous pouvez faire d'autres initialisations ici si la vue
        // dépend de l'administrateur connecté.
        // Par exemple, mettre à jour un label de bienvenue si vous en avez un.
        System.out.println("AdminTimetableManagementController initialisé pour l'admin: " + (admin != null ? admin.getNom() : "null"));
    }

    @FXML
    public void initialize() {
        // Peut être utilisé pour des configurations initiales si nécessaire
        actionStatusLabel.setText("");
    }

    private void updateWelcomeMessage() {
        if (currentAdmin != null) {
            welcomeLabel.setText("Panneau d'Administration - " + currentAdmin.getNom());
        }
    }


    @FXML
    private void handleManageTeachers(ActionEvent event) {
        String message = "Action: Gérer les Enseignants et Affectations (Implémentation à venir)";
        System.out.println(message);
        actionStatusLabel.setText(message);
        // Ici, vous chargeriez une nouvelle vue pour la gestion des enseignants.
    }

    @FXML
    private void handleManageRooms(ActionEvent event) {
        // String message = "Action: Gérer les Salles (Implémentation à venir)";
        // System.out.println(message);
        // actionStatusLabel.setText(message);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/AdminManageRoomsView.fxml"));
            Parent manageRoomsRoot = loader.load();

            // Si vous avez besoin de passer l'objet Admin au AdminManageRoomsController:
            // AdminManageRoomsController controller = loader.getController();
            // if (controller != null) {
            //     controller.setAdministrateur(this.currentAdmin); // Assurez-vous que currentAdmin est initialisé
            // } else {
            //     System.err.println("Erreur: AdminManageRoomsController n'a pas pu être chargé.");
            //     if(actionStatusLabel != null) actionStatusLabel.setText("Erreur interne (Contrôleur Gestion Salles).");
            //     return;
            // }

            Stage stage = new Stage();
            stage.setTitle("Gestion des Salles");
            stage.setScene(new Scene(manageRoomsRoot));
            // Optionnel: rendre modal
            // stage.initModality(Modality.APPLICATION_MODAL);
            // Stage ownerStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            // stage.initOwner(ownerStage);

            stage.showAndWait(); // Ou show() si vous ne voulez pas bloquer

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
    private void handleManageConflicts(ActionEvent event) {
        String message = "Action: Gérer les Conflits (Implémentation à venir)";
        System.out.println(message);
        actionStatusLabel.setText(message);
        // Ici, vous chargeriez une nouvelle vue pour la gestion des conflits.
    }

    @FXML
    private void handleViewStatistics(ActionEvent event) {
        String message = "Action: Voir les Statistiques (Implémentation à venir)";
        System.out.println(message);
        actionStatusLabel.setText(message);
        // Ici, vous chargeriez une nouvelle vue pour les statistiques.
    }

    @FXML
    private void handleManageUsers(ActionEvent event) {
        String message = "Action: Gérer les Utilisateurs (Étudiants) (Implémentation à venir)";
        System.out.println(message);
        actionStatusLabel.setText(message);
        // Ici, vous chargeriez une nouvelle vue pour la gestion des utilisateurs étudiants.
    }

    @FXML
    private void handleManageCourses(ActionEvent event) {
        // String message = "Action: Gérer les Cours et Emplois du Temps (Implémentation à venir)";
        // System.out.println(message);
        // actionStatusLabel.setText(message);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/AdminTimetableManagementView.fxml"));
            Parent timetableManagementRoot = loader.load();

            // Passer l'objet Administrateur au nouveau contrôleur si nécessaire
            AdminTimetableManagementController controller = loader.getController();
            if (controller != null) {
                controller.setAdministrateur(this.currentAdmin); // Assurez-vous que currentAdmin est bien initialisé
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
}