package com.example.agenda4;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParsearXML {
    private static final String ns = null;

    private static final String ETIQUETA_AGENDA = "agenda";
    private static final String ETIQUETA_CONTACTO = "contacto";
    private static final String ETIQUETA_NOMBRE = "nombre";
    private static final String ETIQUETA_APELLIDO = "apellido";
    private static final String ETIQUETA_EMAIL = "email";

    public List<Contacto> parsear(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return leerContactos(parser);
        } finally {
            in.close();
        }
    }

    private List<Contacto> leerContactos(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<Contacto>  lista_contactos = new ArrayList<Contacto>();

        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_AGENDA);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombreEtiqueta = parser.getName();
            // Buscar etiqueta <hotel>
            if (nombreEtiqueta.equals(ETIQUETA_CONTACTO)) {
                lista_contactos.add(leerContacto(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return lista_contactos;
    }

    private Contacto leerContacto(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CONTACTO);
        String nombre = null;
        String apellido = null;
        String email = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name){
                case ETIQUETA_NOMBRE:
                    nombre = leerNombre(parser);
                    break;
                case ETIQUETA_APELLIDO:
                    apellido = leerApellido(parser);
                    break;
                case ETIQUETA_EMAIL:
                    email = leerEmail(parser);
                    break;
            }

            }
        return new Contacto();

    }



        private String leerNombre(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_NOMBRE);
            String nombre = obtenerTexto(parser);
            parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_NOMBRE);
            return nombre;
        }

    private String leerApellido(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_APELLIDO);
        String apellido = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_APELLIDO);
        return apellido;
    }

    private String leerEmail(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_EMAIL);
        String email = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_EMAIL);
        return email;
    }

    private String obtenerTexto(XmlPullParser parser) throws IOException, XmlPullParserException {
        String resultado = "";
        if (parser.next() == XmlPullParser.TEXT) {
            resultado = parser.getText();
            parser.nextTag();
        }
        return resultado;
    }


    private void saltarEtiqueta(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
