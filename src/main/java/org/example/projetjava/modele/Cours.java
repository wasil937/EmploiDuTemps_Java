package org.example.projetjava.modele;

public class Cours {
    private String nom;
    private String type; // Ex : CM, TD, TP
    private int duree;   // en minutes
    private Enseignant enseignant;

    public Cours(String nom, String type, int duree, Enseignant enseignant) {
        this.nom = nom;
        this.type = type;
        this.duree = duree;
        this.enseignant = enseignant;
    }

    public String getNom() {
        return nom;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }
    public int getDuree() {return duree;}
    public String getType() {return type;}

    @Override
    public String toString() {
        return "Cours{" +
                "nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", duree=" + duree +
                ", enseignant=" + enseignant.getNom() +
                '}';
    }
}
