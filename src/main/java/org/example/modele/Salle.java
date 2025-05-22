package org.example.modele;

import java.util.List;

public class Salle {
    private String numero;
    private int capacite;
    private List<String> equipements;

    public Salle(String numero, int capacite, List<String> equipements) {
        this.numero = numero;
        this.capacite = capacite;
        this.equipements = equipements;
    }

    public String getNumero() {
        return numero;
    }

    public int getCapacite() {
        return capacite;
    }

    public List<String> getEquipements() {
        return equipements;
    }

    @Override
    public String toString() {
        return "Salle{" +
                "numero='" + numero + '\'' +
                ", capacite=" + capacite +
                ", equipements=" + equipements +
                '}';
    }
}
