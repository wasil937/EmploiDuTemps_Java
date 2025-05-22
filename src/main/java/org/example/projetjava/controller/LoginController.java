package org.example.projetjava.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.projetjava.modele.Administrateur;
import org.example.projetjava.modele.AuthService;
import org.example.projetjava.modele.Enseignant;
import org.example.projetjava.modele.Etudiant;
import org.example.projetjava.modele.Utilisateur;

import java.io.IOException;


public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorMessageLabel;

    private AuthService authService;

    public LoginController() {}


    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        if (authService == null) {
            errorMessageLabel.setText("Erreur : AuthService non initialisé.");
            return;
        }

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Veuillez entrer un email et un mot de passe.");
            return;
        }

        Utilisateur utilisateurConnecte = authService.login(email, password);

        if (utilisateurConnecte != null) {
            errorMessageLabel.setText("Connexion réussie : " + utilisateurConnecte.getNom());
            System.out.println("Utilisateur connecté: " + utilisateurConnecte.getNom() + " | Role: " + getRole(utilisateurConnecte));



            if (utilisateurConnecte != null) {

                System.out.println("Utilisateur connecté: " + utilisateurConnecte.getNom() + " | Role: " + getRole(utilisateurConnecte));

                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                String newTitle = "Application"; // Titre par défaut

                try {
                    Parent newRoot = null;
                    FXMLLoader loader;

                    if (utilisateurConnecte instanceof Etudiant) {
                        loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/StudentTimetableView.fxml"));
                        newRoot = loader.load();
                        StudentTimetableController controller = loader.getController();
                        if (controller != null) {
                            controller.setEtudiant((Etudiant) utilisateurConnecte);
                        } else {
                            System.err.println("Erreur: StudentTimetableController n'a pas pu être chargé.");
                            errorMessageLabel.setText("Erreur interne (Contrôleur Étudiant).");
                            return; // Arrêter ici si le contrôleur est null
                        }
                        newTitle = "Emploi du Temps - " + utilisateurConnecte.getNom();

                    } else if (utilisateurConnecte instanceof Administrateur) {
                        loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/AdminDashboardView.fxml"));
                        newRoot = loader.load();
                        AdminDashboardController controller = loader.getController();
                        if (controller != null) {
                            controller.setAdministrateur((Administrateur) utilisateurConnecte);
                        } else {
                            System.err.println("Erreur: AdminDashboardController n'a pas pu être chargé.");
                            errorMessageLabel.setText("Erreur interne (Contrôleur Admin).");
                            return;
                        }
                        newTitle = "Tableau de Bord Administrateur - " + utilisateurConnecte.getNom();

                    } else if (utilisateurConnecte instanceof Enseignant) {
                        loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/TeacherScheduleView.fxml"));
                        newRoot = loader.load();
                        TeacherScheduleController controller = loader.getController();
                        if (controller != null) {
                            controller.setEnseignant((Enseignant) utilisateurConnecte);
                        } else {
                            System.err.println("Erreur: TeacherScheduleController n'a pas pu être chargé.");
                            errorMessageLabel.setText("Erreur interne (Contrôleur Enseignant).");
                            return;
                        }
                        newTitle = "Emploi du Temps - Prof. " + utilisateurConnecte.getNom();

                    } else {
                        System.err.println("Rôle utilisateur inconnu ou non géré pour la navigation.");
                        errorMessageLabel.setText("Rôle utilisateur non géré.");
                        return; // Ne pas changer de scène
                    }


                    if (newRoot != null) {
                        Scene newScene = new Scene(newRoot);
                        currentStage.setScene(newScene);
                        currentStage.setTitle(newTitle);
                        currentStage.centerOnScreen(); // Ré-centrer la fenêtre après changement de taille potentielle
                    }

                } catch (IOException e) {
                    System.err.println("Erreur d'E/S lors du chargement de la vue suivante :");
                    e.printStackTrace();
                    if (errorMessageLabel != null) {
                        errorMessageLabel.setText("Erreur de chargement de la vue. Consultez la console.");
                    }
                } catch (NullPointerException e) {
                    System.err.println("Erreur NullPointerException (souvent chemin FXML incorrect ou composant FXML non trouvé) :");
                    e.printStackTrace();
                    if (errorMessageLabel != null) {
                        errorMessageLabel.setText("Erreur interne (NPE). Consultez la console.");
                    }
                } catch (Exception e) { // Attrape toute autre exception non prévue
                    System.err.println("Erreur inattendue lors de la tentative de navigation :");
                    e.printStackTrace();
                    if (errorMessageLabel != null) {
                        errorMessageLabel.setText("Erreur inattendue. Consultez la console.");
                    }
                }

            } else {
                errorMessageLabel.setText("Email ou mot de passe incorrect.");
            }
        } else {
            errorMessageLabel.setText("Email ou mot de passe incorrect.");
        }
    }


    private String getRole(Utilisateur utilisateur) {
        if (utilisateur instanceof Administrateur) {
            return "Administrateur";
        } else if (utilisateur instanceof Enseignant) {
            return "Enseignant";
        } else if (utilisateur instanceof Etudiant) {
            return "Étudiant";
        }
        return "Utilisateur";
    }
}