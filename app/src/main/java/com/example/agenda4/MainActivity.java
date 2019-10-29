package com.example.agenda4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tv_nombre,tv_apellido,tv_email,tv_test;
    EditText et_nombre,et_apellido,et_email;
    Button   btn_boton , btn_listar;
    RequestQueue queue;
    WebView wb_listado;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        tv_apellido = (TextView)findViewById(R.id.tv_apellido);
        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_test = (TextView)findViewById(R.id.tv_test);
        et_nombre =  findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_email = findViewById(R.id.et_email);
        btn_boton = findViewById(R.id.btn_boton);
        btn_listar = findViewById(R.id.btn_listar);
//       wb_listado = findViewById(R.id.wb_listado);
        queue = Volley.newRequestQueue(this);


        // Request a string response from the provided URL.
        final String url = "http://10.34.84.240:8080/Agenda3.0/Agenda3";




        btn_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatos(url);
            }
        });

        btn_listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_nombre.setText("");
                et_apellido.setText("");
                et_email.setText("");
                listar(url);
            }
        });


    }

    private void parseXML(InputStream in ) {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = in;
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            proceso_parseo(parser);

        } catch (XmlPullParserException e) {

        } catch (IOException e) {
        }
    }

    private void proceso_parseo(XmlPullParser parser) throws IOException, XmlPullParserException{
        ArrayList<Contacto> players = new ArrayList<>();
        int eventType = parser.getEventType();
        Contacto cuenta_de_contactos = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("contacto".equals(eltName)) {
                        cuenta_de_contactos = new Contacto();
                        players.add(cuenta_de_contactos);
                    } else if (cuenta_de_contactos != null) {
                        if ("nombre".equals(eltName)) {
                            cuenta_de_contactos.nombre = parser.nextText();
                        } else if ("apellido".equals(eltName)) {
                            cuenta_de_contactos.apellido = parser.nextText();
                        } else if ("email".equals(eltName)) {
                            cuenta_de_contactos.email = parser.nextText();
                        }
                    }
                    break;
            }

            eventType = parser.next();
        }

        imprimir_contactos(players);
    }

    private void imprimir_contactos(ArrayList<Contacto> players) {
        StringBuilder builder = new StringBuilder();

        for (Contacto player : players) {
            builder.append(player.nombre).append("\n").
                    append(player.apellido).append("\n").
                    append(player.email).append("\n\n");
        }

        tv_test.setText(builder.toString());
    }







    private void enviarDatos(final String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        Toast toastInsertado;
                        String respuesta = response.trim();

                        if (respuesta!=null){
                            toastInsertado = Toast.makeText(getApplicationContext(),
                                    "TODO BIEN MAQUINA", Toast.LENGTH_SHORT);

                        }else {
                            toastInsertado = Toast.makeText(getApplicationContext(),
                                    "TODO MAL MAQUINA", Toast.LENGTH_SHORT);
                        }
                        toastInsertado.show();
                        et_nombre.setText("");
                        et_apellido.setText("");
                        et_email.setText("");
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response","Error");
                        Toast toastError = Toast.makeText(getApplicationContext(),"No ha sido Insertado",Toast.LENGTH_SHORT);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nombre",et_nombre.getText().toString());
                params.put("apellido", et_apellido.getText().toString());
                params.put("email",et_email.getText().toString());
                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void listar(final String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
//                        System.out.println(response);
                        String respuesta = response.trim().replace("null","");
                        String res = respuesta.trim();
                        System.out.println(res);
                        InputStream inputStream = new ByteArrayInputStream(res.getBytes(Charset.forName("UTF-8")));

                        parseXML(inputStream);
//                        wb_listado.setWebViewClient(new WebViewClient());
//                        wb_listado.getSettings().setJavaScriptEnabled(true);
//                        wb_listado.loadData(respuesta,"text/html", null);

//                        XmlPullParserFactory parserFactory;

//
//                        StringBuilder builder = new StringBuilder();
//                        try {
//
//
//                           ArrayList<Contacto>misContactos =  (ArrayList<Contacto>)toXML.parsear(inputStream);
//                            for (Contacto con : misContactos){
//                                builder.append(con.getNombre()).append("\n");
//                                builder.append(con.getApellido()).append("\n");
//                                builder.append(con.getEmail()).append("\n");
//                            }
//
//                            tv_test.setText(builder.toString());
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response","Error");
                        Toast toastError = Toast.makeText(getApplicationContext(),"No ha sido Insertado",Toast.LENGTH_SHORT);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nombre",et_nombre.getText().toString());
                params.put("apellido", et_apellido.getText().toString());
                params.put("email",et_email.getText().toString());

                return params;
            }
        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}


