package org.example.projetjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projetjava.modele.Administrateur; // Si vous voulez passer l'admin
import org.example.projetjava.modele.Salle;
import org.example.projetjava.modele.SharedDataRepository; // Pour accéder aux données partagées

import java.util.List;
import java.util.stream.Collectors;

public class AdminManageRoomsController {

    @FXML
    private TableView<Salle> roomsTableView;

    @FXML
    private TableColumn<Salle, String> numeroColumn;

    @FXML
    private TableColumn<Salle, Integer> capaciteColumn;

    @FXML
    private TableColumn<Salle, String> equipementsColumn; // Affichera la liste des équipements comme une chaîne

    // private Administrateur currentAdmin; // Décommentez si vous passez l'admin

    // public void setAdministrateur(Administrateur admin) { // Décommentez si vous passez l'admin
    //    this.currentAdmin = admin;
    // }

    @FXML
    public void initialize() {
        // Configurer les colonnes pour qu'elles sachent comment obtenir les données de l'objet Salle
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capacite"));

        // Pour la liste des équipements, nous allons la convertir en une chaîne
        equipementsColumn.setCellValueFactory(cellData -> {
            List<String> equipements = cellData.getValue().getEquipements();

            // Convertit la liste en une chaîne séparée par des virgules
            String equipStr = equipements.stream().collect(Collectors.joining(", "));
            // PropertyValueFactory attend un ObservableValue, mais pour une chaîne simple,
            // on peut la wrapper. Cependant, pour TableColumn<Salle, String>,
            // il est plus simple de retourner une SimpleStringProperty ou similaire.
            // Ici, nous allons utiliser une astuce : retourner une ObservableList d'un seul élément
            // et la TableCell par défaut fera toString(). Pour une meilleure solution, utilisez setCellFactory.
            // MAIS pour PropertyValueFactory, le type doit correspondre.
            // Changeons cela pour utiliser une cell factory pour plus de flexibilité et de propreté.
            // Pour l'instant, une solution simple si Salle::getEquipements() retournait String :
            // Ou si vous avez une méthode dans Salle qui retourne la chaîne formatée :
            // equipementsColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEquipements")); // Si une telle méthode existait

            // Solution simple avec setCellValueFactory et conversion manuelle :
            // Note: le type de retour doit être ObservableValue<String>.
            // On va le faire plus simplement dans setCellFactory pour l'instant ou juste afficher le toString() de la liste.
            // Pour PropertyValueFactory, la propriété doit exister DANS l'objet Salle.
            // Nous allons donc ajouter une méthode getFormattedEquipements() dans Salle.java pour la propreté.
            // Ou, plus simple pour l'instant, laissons PropertyValueFactory et modifions Salle pour avoir une méthode qui retourne la chaîne.
            // Alternative (si vous ne voulez pas modifier Salle) :
            // equipementsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.join(", ", data.getValue().getEquipements())));
            // Pour l'instant, on va supposer que vous allez ajouter une méthode à Salle, c'est plus propre.
            // Si Salle a `public String getEquipementsAsString() { return String.join(", ", equipements); }`
            // alors vous pouvez faire :
            // equipementsColumn.setCellValueFactory(new PropertyValueFactory<>("equipementsAsString"));
            // Pour la démo immédiate, on va juste afficher le toString() de la liste (pas idéal mais rapide)
            // Pour cela, la colonne doit être TableColumn<Salle, List<String>> et utiliser une cellFactory
            // Pour garder TableColumn<Salle, String>, on se fie à PropertyValueFactory qui appelle un getter.
            // Créons une méthode getFormattedEquipements dans Salle.java (voir étape 4)
            return new javafx.beans.property.SimpleStringProperty(String.join(", ", cellData.getValue().getEquipements()));
        });


        // Charger les données depuis SharedDataRepository
        ObservableList<Salle> sallesList = FXCollections.observableArrayList(SharedDataRepository.ALL_SALLES);
        roomsTableView.setItems(sallesList);

        if (sallesList.isEmpty()) {
            System.out.println("Aucune salle à afficher depuis SharedDataRepository.");
        }

        equipementsColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEquipements"));
    }
}