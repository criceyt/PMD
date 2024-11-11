package com.example.reto.database.modelo;

public class Biblioteca {
    private int id;
    private int usuarioId;
    private int juegoId;


    public Biblioteca(int id, int usuarioId, int juegoId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.juegoId = juegoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getJuegoId() {
        return juegoId;
    }

    public void setJuegoId(int juegoId) {
        this.juegoId = juegoId;
    }
}
