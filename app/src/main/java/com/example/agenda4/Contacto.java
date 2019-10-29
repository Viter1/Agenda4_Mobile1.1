package com.example.agenda4;

import java.util.ArrayList;
import java.util.List;

public class Contacto {
    private String nombre,apellido,email;
    public static List<Contacto> CONTACTOS = new ArrayList<>();

    public Contacto(String nombre, String apellido, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }
}
