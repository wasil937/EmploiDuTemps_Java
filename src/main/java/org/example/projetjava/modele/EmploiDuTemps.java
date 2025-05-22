package org.example.projetjava.modele;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmploiDuTemps {

    public static class Creneau {
        private Cours cours;
        private Salle salle;
        private LocalDateTime debut;
        private LocalDateTime fin;
        private Etudiant etudiantConcerne; // NOUVEAU CHAMP (optionnel)

        // Constructeur original (si vous voulez le garder pour des créneaux généraux)
        public Creneau(Cours cours, Salle salle, LocalDateTime debut, LocalDateTime fin) {
            this(cours, salle, debut, fin, null);
        }

        // Nouveau constructeur avec Etudiant
        public Creneau(Cours cours, Salle salle, LocalDateTime debut, LocalDateTime fin, Etudiant etudiantConcerne) {
            this.cours = cours;
            this.salle = salle;
            this.debut = debut;
            this.fin = fin;
            this.etudiantConcerne = etudiantConcerne;
        }

        public Cours getCours() { return cours; }
        public Salle getSalle() { return salle; }
        public LocalDateTime getDebut() { return debut; }
        public LocalDateTime getFin() { return fin; }
        public Etudiant getEtudiantConcerne() { return etudiantConcerne; }

        @Override
        public String toString() {
            String base = "Creneau{" +
                    "cours=" + cours.getNom() +
                    ", salle=" + salle.getNumero() +
                    ", debut=" + debut +
                    ", fin=" + fin;
            if (etudiantConcerne != null) {
                base += ", etudiant=" + etudiantConcerne.getNom();
            }
            base += '}';
            return base;
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

    public List<Creneau> getCreneaux() {
        return creneaux;
    }
}

