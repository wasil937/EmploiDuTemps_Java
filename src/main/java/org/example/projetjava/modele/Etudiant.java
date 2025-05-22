package org.example.projetjava.modele;

public class Etudiant extends Utilisateur {
    private String numeroEtudiant;

    public Etudiant(int id, String nom, String email, String motDePasse, String numeroEtudiant) {
        super(id, nom, email, motDePasse);
        this.numeroEtudiant = numeroEtudiant;
    }

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    @Override
    public String toString() {
        return super.toString() + ", Etudiant{" +
                "numeroEtudiant='" + numeroEtudiant + '\'' +
                '}';
    }
}
