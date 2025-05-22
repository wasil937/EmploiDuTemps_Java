package org.example.modele;

public class Administrateur extends Utilisateur {
    public Administrateur(int id, String nom, String email, String motDePasse) {
        super(id, nom, email, motDePasse);
    }

    @Override
    public String toString() {
        return super.toString() + ", Administrateur";
    }
}
