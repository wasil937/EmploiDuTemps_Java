package org.example.projetjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.projetjava.controller.LoginController; // Assurez-vous d'importer votre LoginController
import org.example.projetjava.modele.*; // Importer vos modèles

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {

    private AuthService authService;

    @Override
    public void init() throws Exception {
        // Initialiser les utilisateurs et AuthService ici
        // C'est la même logique que dans votre Main.java actuel
        Etudiant etu = new Etudiant(1, "Alice", "alice@etu.fr", "123", "E001");
        Enseignant ens = new Enseignant(2, "Bob", "bob@ens.fr", "456", "Maths");
        Administrateur admin = new Administrateur(3, "Claire", "admin@univ.fr", "admin");
        this.authService = new AuthService(Arrays.asList(etu, ens, admin));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/projetjava/view/LoginView.fxml")));
        Parent root = loader.load();

        // Passer l'instance de AuthService au LoginController
        LoginController loginController = loader.getController();
        loginController.setAuthService(this.authService);

        primaryStage.setTitle("Connexion");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}