package org.example.projetjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.projetjava.controller.LoginController;
import org.example.projetjava.modele.AuthService;
import org.example.projetjava.modele.SharedDataRepository;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    private AuthService authService;

    @Override
    public void init() throws Exception {
        super.init();

        // 1. Initialiser les données partagées une seule fois au démarrage de l'application
        SharedDataRepository.initializeData();

        // 2. Initialiser AuthService avec la liste des utilisateurs depuis le dépôt partagé
        this.authService = new AuthService(SharedDataRepository.ALL_USERS);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/projetjava/view/LoginView.fxml")));
        Parent root = loader.load();

        LoginController loginController = loader.getController();
        if (loginController != null) {
            loginController.setAuthService(this.authService);
        } else {
            System.err.println("ERREUR: LoginController n'a pas pu être chargé depuis FXML. Vérifiez fx:controller dans LoginView.fxml.");
        }

        primaryStage.setTitle("Système de Gestion des Emplois du Temps - Connexion");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}