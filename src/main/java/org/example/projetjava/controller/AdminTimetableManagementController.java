package org.example.projetjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.projetjava.modele.*; // Importer tous vos modèles

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminTimetableManagementController {

    @FXML
    private ComboBox<String> userSelectionComboBox;

    @FXML
    private ListView<String> timetableDisplayListView;

    @FXML
    private Button editEntryButton;

    private Administrateur currentAdmin;

    // Simuler une source de données globale pour l'exemple
    private List<Utilisateur> allUsers = new ArrayList<>();
    private List<EmploiDuTemps.Creneau> allCreneaux = new ArrayList<>();
    private ObservableList<String> timetableEntries = FXCollections.observableArrayList();

    public AdminTimetableManagementController() {
        // Constructeur - initialiser les données de démonstration ici
        initializeDemoData();
    }

    public void setAdministrateur(Administrateur admin) {
        this.currentAdmin = admin;
        // Logique supplémentaire si nécessaire avec l'objet admin
    }

    @FXML
    public void initialize() {
        timetableDisplayListView.setItems(timetableEntries);

        // Peupler le ComboBox
        ObservableList<String> userDisplayNames = FXCollections.observableArrayList();
        userDisplayNames.add("Tous les Emplois du Temps");
        for (Utilisateur user : allUsers) {
            String role = "";
            if (user instanceof Etudiant) role = " (Étudiant)";
            else if (user instanceof Enseignant) role = " (Enseignant)";
            // Ne pas ajouter les admins à la liste des emplois du temps consultables de cette manière
            if (!role.isEmpty()) {
                userDisplayNames.add(user.getNom() + role);
            }
        }
        userSelectionComboBox.setItems(userDisplayNames);
        userSelectionComboBox.getSelectionModel().selectFirst(); // Sélectionner "Tous" par défaut

        // Action lors du changement de sélection dans le ComboBox
        userSelectionComboBox.setOnAction(event -> displayScheduleForSelection());

        // Gérer l'activation du bouton "Modifier" (très basique)
        timetableDisplayListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editEntryButton.setDisable(newSelection == null);
        });

        // Afficher tous les emplois du temps par défaut
        displayScheduleForSelection();
    }

    private List<Cours> allCoursDemo = new ArrayList<>();
    private List<Salle> allSallesDemo = new ArrayList<>();

    private void initializeDemoData() {

        Salle salleC101 = new Salle("C101", 40, Arrays.asList("Vidéoprojecteur", "PC Enseignant"));
        Salle salleD202 = new Salle("D202", 30, Arrays.asList("Tableau Blanc"));
        Salle laboInfo3 = new Salle("INFO3", 20, Arrays.asList("PC", "Réseau"));
        allSallesDemo.addAll(Arrays.asList(salleC101, salleD202, laboInfo3));

        // Cours (reprise pour la liste) - Assurez-vous que les enseignants sont déjà dans allUsers ou créez-les ici
        Enseignant enseignantBob = (Enseignant) allUsers.stream().filter(u -> u.getNom().equals("Bob")).findFirst().orElse(new Enseignant(2, "Bob", "bob@ens.fr", "456", "Maths"));
        Enseignant enseignantAliceProf = (Enseignant) allUsers.stream().filter(u -> u.getNom().equals("Alice Prof")).findFirst().orElse(new Enseignant(4, "Alice Prof", "alice.prof@ens.fr", "prof123", "Physique"));

        Cours coursMathsBob = new Cours("Maths Fondamentales", "CM", 120, enseignantBob);
        Cours coursAlgebreBob = new Cours("Algèbre Avancée", "TD", 90, enseignantBob);
        Cours coursPhysiqueAlice = new Cours("Physique Quantique", "CM", 120, enseignantAliceProf);
        Cours coursProgJavaEtudiant = new Cours("TP Java", "TP", 180, enseignantAliceProf);
        allCoursDemo.addAll(Arrays.asList(coursMathsBob, coursAlgebreBob, coursPhysiqueAlice, coursProgJavaEtudiant));

        // Enseignants
        allUsers.add(enseignantBob);
        allUsers.add(enseignantAliceProf);

        // Étudiants
        Etudiant etudiantAlice = new Etudiant(1, "Alice", "alice@etu.fr", "123", "E001");
        Etudiant etudiantTom = new Etudiant(5, "Tom", "tom@etu.fr", "789", "E002");
        allUsers.add(etudiantAlice);
        allUsers.add(etudiantTom);

        // Administrateur (pas pour affichage EDT direct, mais pour la liste des utilisateurs)
        allUsers.add(new Administrateur(3, "Claire", "admin@univ.fr", "admin"));




        // Créneaux (liste globale)
        // Pour Bob
        allCreneaux.add(new EmploiDuTemps.Creneau(coursMathsBob, salleC101,
                LocalDateTime.of(2024, 5, 27, 10, 0), LocalDateTime.of(2024, 5, 27, 12, 0)));
        allCreneaux.add(new EmploiDuTemps.Creneau(coursAlgebreBob, salleD202,
                LocalDateTime.of(2024, 5, 28, 14, 0), LocalDateTime.of(2024, 5, 28, 15, 30)));
        // Pour Alice Prof
        allCreneaux.add(new EmploiDuTemps.Creneau(coursPhysiqueAlice, salleC101,
                LocalDateTime.of(2024, 5, 27, 14, 0), LocalDateTime.of(2024, 5, 27, 16, 0)));
        // Un cours TP que l'étudiant Alice pourrait suivre (enseigné par Alice Prof)
        allCreneaux.add(new EmploiDuTemps.Creneau(coursProgJavaEtudiant, laboInfo3,
                LocalDateTime.of(2024, 5, 29, 9, 0), LocalDateTime.of(2024, 5, 29, 12, 0)));
        // Pour l'étudiant Tom (un autre cours avec Bob)
        allCreneaux.add(new EmploiDuTemps.Creneau(coursMathsBob, salleD202, // Tom suit aussi Maths avec Bob mais dans une autre salle/horaire
                LocalDateTime.of(2024, 5, 30, 8, 0), LocalDateTime.of(2024, 5, 30, 10, 0)));
    }

    private void displayScheduleForSelection() {
        timetableEntries.clear();
        String selection = userSelectionComboBox.getSelectionModel().getSelectedItem();
        if (selection == null) return;

        List<EmploiDuTemps.Creneau> creneauxToDisplay = new ArrayList<>();

        if (selection.equals("Tous les Emplois du Temps")) {
            creneauxToDisplay.addAll(allCreneaux);
        } else {
            // Extraire le nom de l'utilisateur de la sélection
            String selectedUserName = selection.substring(0, selection.lastIndexOf(" (")).trim();
            Utilisateur selectedUser = allUsers.stream()
                    .filter(u -> u.getNom().equals(selectedUserName))
                    .findFirst()
                    .orElse(null);

            if (selectedUser != null) {
                if (selectedUser instanceof Enseignant) {
                    creneauxToDisplay = allCreneaux.stream()
                            .filter(c -> c.getCours().getEnseignant().getId() == selectedUser.getId())
                            .collect(Collectors.toList());
                } else if (selectedUser instanceof Etudiant) {
                    // Logique de filtrage pour étudiant :
                    // Pour l'instant, on va supposer qu'un étudiant voit certains cours spécifiques.
                    // Par exemple, on va dire que l'étudiant Alice suit le cours de TP Java et Maths
                    // Et Tom suit Maths avec Bob.
                    // Ceci est une simplification majeure. Normalement, les étudiants seraient inscrits à des cours.
                    if (selectedUser.getNom().equals("Alice")) {
                        creneauxToDisplay = allCreneaux.stream()
                                .filter(c -> c.getCours().getNom().equals("TP Java") || (c.getCours().getNom().equals("Maths Fondamentales") && c.getDebut().getHour()==10) ) // Alice suit TP Java et le premier cours de Maths
                                .collect(Collectors.toList());
                    } else if (selectedUser.getNom().equals("Tom")) {
                        creneauxToDisplay = allCreneaux.stream()
                                .filter(c -> c.getCours().getNom().equals("Maths Fondamentales") && c.getDebut().getHour()==8) // Tom suit le deuxième cours de Maths
                                .collect(Collectors.toList());
                    } else {
                        timetableEntries.add("Logique d'affichage pour cet étudiant non définie.");
                    }
                }
            }
        }

        // Trier et afficher
        creneauxToDisplay.sort(Comparator.comparing(EmploiDuTemps.Creneau::getDebut));
        formatAndDisplayCreneaux(creneauxToDisplay);
    }

    private void formatAndDisplayCreneaux(List<EmploiDuTemps.Creneau> creneaux) {
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
            String entry = String.format("%s - %s : %s (%s) - Prof: %s - Salle: %s",
                    creneau.getDebut().format(timeFormatter),
                    creneau.getFin().format(timeFormatter),
                    creneau.getCours().getNom(),
                    typeCours,
                    creneau.getCours().getEnseignant().getNom(),
                    creneau.getSalle().getNumero()
            );
            timetableEntries.add(entry);
        }
    }


    @FXML
    private void handleCreateEntry(ActionEvent event) {
        // System.out.println("Action: Créer un nouveau créneau (Implémentation à venir)");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projetjava/view/CreateCreneauDialog.fxml"));
            Parent dialogRoot = loader.load();

            CreateCreneauDialogController dialogController = loader.getController();
            dialogController.initializeData(new ArrayList<>(allCoursDemo), new ArrayList<>(allSallesDemo)); // Passer les listes de cours et salles

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Créer Nouveau Créneau");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            // Optionnel: Stage ownerStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            // dialogStage.initOwner(ownerStage);
            dialogStage.setScene(new Scene(dialogRoot));

            dialogStage.showAndWait(); // Attendre que le dialogue soit fermé

            EmploiDuTemps.Creneau nouveauCreneau = dialogController.getNouveauCreneau();
            if (nouveauCreneau != null) {
                allCreneaux.add(nouveauCreneau); // Ajouter à la liste globale en mémoire
                // Rafraîchir l'affichage principal
                // La manière la plus simple est de rappeler la méthode qui peuple le ListView principal.
                // Si "Tous les Emplois du Temps" est sélectionné ou si le nouveau créneau correspond au filtre actuel.
                displayScheduleForSelection();
                System.out.println("Nouveau créneau créé et ajouté : " + nouveauCreneau);
            }

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du dialogue de création de créneau:");
            e.printStackTrace();
            // Afficher un message d'erreur à l'utilisateur si vous avez un label pour cela.
        }
    }

    @FXML
    private void handleEditEntry(ActionEvent event) {
        String selectedItem = timetableDisplayListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && !selectedItem.startsWith("---")) { // Ignorer les séparateurs de date
            System.out.println("Action: Modifier le créneau sélectionné: " + selectedItem + " (Implémentation à venir)");
            // Identifier le créneau à partir de la chaîne (complexe) ou stocker les objets Creneau dans le ListView
            // Ouvrir un dialogue/formulaire pré-rempli
            timetableEntries.add("--- ACTION: Modification de créneau non implémentée ---");
        } else {
            System.out.println("Aucun créneau valide sélectionné pour modification.");
        }
    }
}