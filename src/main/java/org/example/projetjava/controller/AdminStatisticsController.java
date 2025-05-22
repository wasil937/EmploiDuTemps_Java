package org.example.projetjava.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.example.projetjava.modele.*; // Importer tous les modèles

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminStatisticsController {

    @FXML
    private TextArea roomUsageTextArea;

    @FXML
    private TextArea teacherHoursTextArea;

    @FXML
    private TextArea courseHoursTextArea;

    @FXML
    public void initialize() {
        calculateAndDisplayRoomUsage();
        calculateAndDisplayTeacherHours();
        calculateAndDisplayCourseHours();
    }

    private void calculateAndDisplayRoomUsage() {
        StringBuilder sb = new StringBuilder();
        Map<String, Long> roomUsageMap = SharedDataRepository.ALL_CRENEAUX.stream()
                .collect(Collectors.groupingBy(creneau -> creneau.getSalle().getNumero(), Collectors.counting()));

        if (roomUsageMap.isEmpty()) {
            sb.append("Aucun créneau programmé pour analyser l'utilisation des salles.\n");
        } else {
            SharedDataRepository.ALL_SALLES.forEach(salle -> {
                long count = roomUsageMap.getOrDefault(salle.getNumero(), 0L);
                sb.append("Salle '").append(salle.getNumero()).append("': Utilisée pour ")
                        .append(count).append(" créneau(x).\n");
            });
        }
        roomUsageTextArea.setText(sb.toString());
    }

    private void calculateAndDisplayTeacherHours() {
        StringBuilder sb = new StringBuilder();
        // On regroupe les créneaux par enseignant puis on somme les durées
        Map<Enseignant, Long> teacherTotalMinutesMap = SharedDataRepository.ALL_CRENEAUX.stream()
                .collect(Collectors.groupingBy(
                        creneau -> creneau.getCours().getEnseignant(),
                        Collectors.summingLong(creneau ->
                                ChronoUnit.MINUTES.between(creneau.getDebut(), creneau.getFin())
                        )
                ));

        if (teacherTotalMinutesMap.isEmpty()) {
            sb.append("Aucun créneau programmé pour calculer les heures des enseignants.\n");
        } else {
            teacherTotalMinutesMap.forEach((enseignant, totalMinutes) -> {
                double hours = totalMinutes / 60.0;
                sb.append("Prof. ").append(enseignant.getNom()).append(": ")
                        .append(String.format("%.2f", hours)).append(" heure(s) de cours programmée(s).\n");
            });
        }
        // Lister aussi les profs qui n'ont pas de cours assignés via les créneaux
        SharedDataRepository.ALL_USERS.stream()
                .filter(u -> u instanceof Enseignant)
                .map(u -> (Enseignant)u)
                .filter(e -> !teacherTotalMinutesMap.containsKey(e))
                .forEach(e -> sb.append("Prof. ").append(e.getNom()).append(": 0.00 heure(s) de cours programmée(s) via créneaux.\n"));

        teacherHoursTextArea.setText(sb.toString());
    }

    private void calculateAndDisplayCourseHours() {
        StringBuilder sb = new StringBuilder();
        Map<String, Long> courseTotalMinutesMap = SharedDataRepository.ALL_CRENEAUX.stream()
                .collect(Collectors.groupingBy(
                        creneau -> creneau.getCours().getNom(), // Regrouper par nom de cours
                        Collectors.summingLong(creneau ->
                                ChronoUnit.MINUTES.between(creneau.getDebut(), creneau.getFin())
                        )
                ));

        if (courseTotalMinutesMap.isEmpty()) {
            sb.append("Aucun créneau programmé pour calculer les heures par matière.\n");
        } else {
            courseTotalMinutesMap.forEach((courseName, totalMinutes) -> {
                double hours = totalMinutes / 60.0;
                sb.append("Matière '").append(courseName).append("': ")
                        .append(String.format("%.2f", hours)).append(" heure(s) programmée(s).\n");
            });
        }
        // Lister aussi les cours qui n'ont pas de créneaux
        SharedDataRepository.ALL_COURS.stream()
                .filter(c -> !courseTotalMinutesMap.containsKey(c.getNom()))
                .forEach(c -> sb.append("Matière '").append(c.getNom()).append("': 0.00 heure(s) programmée(s) via créneaux.\n"));

        courseHoursTextArea.setText(sb.toString());
    }
}