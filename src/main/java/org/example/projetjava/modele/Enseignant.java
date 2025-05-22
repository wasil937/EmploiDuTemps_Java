package org.example.projetjava.modele;

public class Enseignant extends Utilisateur {
    private String matiere;

    public Enseignant(int id, String nom, String email, String motDePasse, String matiere) {
        super(id, nom, email, motDePasse);
        this.matiere = matiere;
    }

    public String getMatiere() {
        return matiere;
    }

    @Override
    public String getRole() {
        return "Enseignant";
    }


    @Override
    public String toString() {
        return super.toString() + ", Enseignant{" +
                "matiere='" + matiere + '\'' +
                '}';
    }
}
