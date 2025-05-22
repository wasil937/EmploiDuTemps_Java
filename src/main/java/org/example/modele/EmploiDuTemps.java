package org.example.modele;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmploiDuTemps {

    public static class Creneau {
        private Cours cours;
        private Salle salle;
        private LocalDateTime debut;
        private LocalDateTime fin;

        public Creneau(Cours cours, Salle salle, LocalDateTime debut, LocalDateTime fin) {
            this.cours = cours;
            this.salle = salle;
            this.debut = debut;
            this.fin = fin;
        }

        public Cours getCours() {
            return cours;
        }

        public Salle getSalle() {
            return salle;
        }

        public LocalDateTime getDebut() {
            return debut;
        }

        public LocalDateTime getFin() {
            return fin;
        }

        @Override
        public String toString() {
            return "Creneau{" +
                    "cours=" + cours +
                    ", salle=" + salle.getNumero() +
                    ", debut=" + debut +
                    ", fin=" + fin +
                    '}';
        }
    }

    private List<Creneau> creneaux;

    public EmploiDuTemps() {
        this.creneaux = new ArrayList<>();
    }

    public void ajouterCreneau(Creneau c) {
        creneaux.add(c);
    }

    public void afficher() {
        for (Creneau c : creneaux) {
            System.out.println(c);
        }
    }
}
