package org.example.projetjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.projetjava.modele.Administrateur;

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
        updateWelcomeMessage();
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
    private void handleManageCourses(ActionEvent event) {
        String message = "Action: Gérer les Cours et Emplois du Temps (Implémentation à venir)";
        System.out.println(message);
        actionStatusLabel.setText(message);
        // Ici, vous chargeriez une nouvelle vue ou un dialogue pour la gestion des cours.
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
        String message = "Action: Gérer les Salles (Implémentation à venir)";
        System.out.println(message);
        actionStatusLabel.setText(message);
        // Ici, vous chargeriez une nouvelle vue pour la gestion des salles.
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
}