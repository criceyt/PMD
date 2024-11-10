package com.example.reto.database.modelo;

public class Juego {
    private int id;
    private String nombre;
    private String precio;
    private String descripcion;
    private String imagen;
    private String trailer;


    // Constructor que incluye 'tieneJuego'
    public Juego(int id, String nombre, String precio, String descripcion, String imagen, String trailer) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.trailer = trailer;
    }
    public Juego(String nombre, String imagen){
        this.nombre = nombre;
        this.imagen = imagen;
    }
    public Juego() {

    }

    // Getters y setters para los campos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }


}
