package org.example.projetjava.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.projetjava.modele.Cours;
import org.example.projetjava.modele.EmploiDuTemps;
import org.example.projetjava.modele.Enseignant;
import org.example.projetjava.modele.Salle; // Vous aurez besoin de Salle si vous affichez cette info

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherScheduleController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private ListView<String> scheduleListView;

    private Enseignant currentEnseignant;
    private ObservableList<String> scheduleEntries = FXCollections.observableArrayList();

    // Supposons une liste globale de tous les créneaux (pour la simulation)
    // Dans une vraie application, cela viendrait d'un service ou d'une base de données.
    private List<EmploiDuTemps.Creneau> allCreneauxGlobaux = new ArrayList<>();

    public TeacherScheduleController() {
        // Constructeur - initialiser les données de démonstration ici si nécessaire
        initializeDemoData(); // Pour peupler allCreneauxGlobaux
    }

    public void setEnseignant(Enseignant enseignant) {
        this.currentEnseignant = enseignant;
        updateWelcomeMessage();
        populateSchedule();
    }

    @FXML
    public void initialize() {
        scheduleListView.setItems(scheduleEntries);
    }

    private void updateWelcomeMessage() {
        if (currentEnseignant != null) {
            welcomeLabel.setText("Emploi du Temps de Prof. " + currentEnseignant.getNom());
        }
    }

    private void initializeDemoData() {
        // Création d'enseignants (dont celui qu'on utilisera pour le test, ex: Bob)
        Enseignant enseignantBob = new Enseignant(2, "Bob", "bob@ens.fr", "456", "Maths");
        Enseignant enseignantAliceProf = new Enseignant(1, "Alice Prof", "alice.prof@ens.fr", "prof123", "Physique"); // Un autre prof pour la diversité des cours

        // Création de quelques salles
        Salle salleC101 = new Salle("C101", 40, Arrays.asList("Vidéoprojecteur", "PC Enseignant"));
        Salle salleD202 = new Salle("D202", 30, Arrays.asList("Tableau Blanc"));

        // Création de quelques cours
        Cours coursMathsFondamentales = new Cours("Maths Fondamentales", "CM", 120, enseignantBob);
        Cours coursAlgebreAvancee = new Cours("Algèbre Avancée", "TD", 90, enseignantBob);
        Cours coursPhysiqueQuantique = new Cours("Physique Quantique", "CM", 120, enseignantAliceProf);
        Cours coursOptiqueTD = new Cours("Optique Géométrique", "TD", 90, enseignantAliceProf);


        // Ajout de créneaux à la liste globale
        // Créneaux pour Enseignant Bob
        allCreneauxGlobaux.add(new EmploiDuTemps.Creneau(coursMathsFondamentales, salleC101,
                LocalDateTime.of(2024, 5, 27, 10, 0), LocalDateTime.of(2024, 5, 27, 12, 0)));
        allCreneauxGlobaux.add(new EmploiDuTemps.Creneau(coursAlgebreAvancee, salleD202,
                LocalDateTime.of(2024, 5, 28, 14, 0), LocalDateTime.of(2024, 5, 28, 15, 30)));
        allCreneauxGlobaux.add(new EmploiDuTemps.Creneau(coursMathsFondamentales, salleC101, // Un autre cours pour Bob
                LocalDateTime.of(2024, 5, 29, 10, 0), LocalDateTime.of(2024, 5, 29, 12, 0)));


        // Créneaux pour Enseignant AliceProf (pour montrer que le filtre fonctionne)
        allCreneauxGlobaux.add(new EmploiDuTemps.Creneau(coursPhysiqueQuantique, salleC101,
                LocalDateTime.of(2024, 5, 27, 14, 0), LocalDateTime.of(2024, 5, 27, 16, 0)));
        allCreneauxGlobaux.add(new EmploiDuTemps.Creneau(coursOptiqueTD, salleD202,
                LocalDateTime.of(2024, 5, 30, 9, 0), LocalDateTime.of(2024, 5, 30, 10, 30)));
    }


    private void populateSchedule() {
        if (currentEnseignant == null) {
            return;
        }
        scheduleEntries.clear();

        // Filtrer les créneaux pour l'enseignant courant
        // On compare les IDs car ce sont des objets distincts même si les noms/emails sont identiques
        List<EmploiDuTemps.Creneau> teacherCreneaux = allCreneauxGlobaux.stream()
                .filter(creneau -> creneau.getCours().getEnseignant().getId() == currentEnseignant.getId())
                .sorted(Comparator.comparing(EmploiDuTemps.Creneau::getDebut))
                .collect(Collectors.toList());

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");

        if (teacherCreneaux.isEmpty()) {
            scheduleEntries.add("Aucun cours programmé pour cet enseignant.");
            return;
        }

        String currentDate = "";
        for (EmploiDuTemps.Creneau creneau : teacherCreneaux) {
            String creneauDateStr = creneau.getDebut().format(dateFormatter);
            if (!creneauDateStr.equals(currentDate)) {
                if (!scheduleEntries.isEmpty()) {
                    scheduleEntries.add(""); // Ligne vide pour séparer les jours
                }
                scheduleEntries.add("--- " + creneauDateStr.toUpperCase() + " ---");
                currentDate = creneauDateStr;
            }
            String typeCours = (creneau.getCours().getType() != null) ? creneau.getCours().getType() : "N/A";
            String entry = String.format("%s - %s : %s (%s) en salle %s",
                    creneau.getDebut().format(timeFormatter),
                    creneau.getFin().format(timeFormatter),
                    creneau.getCours().getNom(),
                    typeCours,
                    creneau.getSalle().getNumero()
            );
            scheduleEntries.add(entry);
        }
    }
}