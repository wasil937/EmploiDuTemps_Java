package org.example.projetjava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.projetjava.controller.LoginController;
import org.example.projetjava.modele.AuthService;
import org.example.projetjava.modele.SharedDataRepository;

// Supprimez ces imports s'ils ne sont plus utilisés directement ici après le déplacement des données
// import org.example.projetjava.modele.Etudiant;
// import org.example.projetjava.modele.Enseignant;
// import org.example.projetjava.modele.Administrateur;
// import java.util.Arrays;
// import java.util.List;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    private AuthService authService; // Cette instance sera utilisée par LoginController

    @Override
    public void init() throws Exception {
        super.init(); // Appel à la méthode init de la classe parente (bonne pratique)

        // 1. Initialiser les données partagées une seule fois au démarrage de l'application
        SharedDataRepository.initializeData();

        // 2. Initialiser AuthService avec la liste des utilisateurs depuis le dépôt partagé
        // Note : SharedDataRepository.ALL_USERS est déjà une List<Utilisateur>
        this.authService = new AuthService(SharedDataRepository.ALL_USERS);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/org/example/projetjava/view/LoginView.fxml")));
        Parent root = loader.load();

        // Passer l'instance de AuthService (maintenant initialisée avec les données partagées) au LoginController
        LoginController loginController = loader.getController();
        if (loginController != null) {
            loginController.setAuthService(this.authService);
        } else {
            System.err.println("ERREUR: LoginController n'a pas pu être chargé depuis FXML. Vérifiez fx:controller dans LoginView.fxml.");
            // Vous pourriez vouloir arrêter l'application ici ou afficher une alerte
            // car sans LoginController, la connexion ne fonctionnera pas.
        }

        primaryStage.setTitle("Système de Gestion des Emplois du Temps - Connexion"); // Titre mis à jour
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}