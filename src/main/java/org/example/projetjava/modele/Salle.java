package org.example.projetjava.modele;

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

    public String getNom() {
        return numero;
    }

    public String getFormattedEquipements() {
        if (equipements == null || equipements.isEmpty()) {
            return "Aucun";
        }
        return String.join(", ", equipements);
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
