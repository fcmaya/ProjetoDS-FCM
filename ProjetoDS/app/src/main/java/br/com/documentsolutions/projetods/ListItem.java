package br.com.documentsolutions.projetods;

/**
 * Created by Maia on 20/12/2017.
 */

public class ListItem {

    private String ID;
    private String Nome;

    public ListItem(String ID, String nome) {
        this.ID = ID;
        Nome = nome;
    }

    public String getID() {
        return ID;
    }

    public String getNome() {
        return Nome;
    }
}
