package br.com.documentsolutions.projetods;


class ListItem {

    private String ID;
    private String Nome;

    ListItem(String ID, String nome) {
        this.ID = ID;
        Nome = nome;
    }

    String getID() {
        return ID;
    }

    String getNome() {
        return Nome;
    }
}
