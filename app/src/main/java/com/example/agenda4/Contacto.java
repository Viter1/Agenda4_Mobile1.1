package com.example.agenda4;



public class Contacto {
    private String nombre,apellido,email;

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


    @Override
    public String toString() {
        return
                nombre + '\n' +
                apellido + '\n' +
                 email + '\n'
                ;
    }
}
