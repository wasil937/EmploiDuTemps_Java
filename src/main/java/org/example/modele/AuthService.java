package org.example.modele;

import java.util.List;

public class AuthService {

    private List<Utilisateur> utilisateurs;

    public AuthService(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public Utilisateur login(String email, String motDePasse) {
        for (Utilisateur u : utilisateurs) {
            if (u.seConnecter(email, motDePasse)) {
                return u;
            }
        }
        return null;
    }
}
