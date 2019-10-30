package com.example.agenda4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    TextView tv_nombre,tv_apellido,tv_email;
    ListView lv_lista;
    EditText et_nombre,et_apellido,et_email;
    Button   btn_boton , btn_listar;
    RequestQueue queue;
    ArrayList<Contacto> test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        tv_apellido = (TextView)findViewById(R.id.tv_apellido);
        tv_email = (TextView)findViewById(R.id.tv_email);
       lv_lista = (ListView) findViewById(R.id.lv_lista);
        et_nombre =  findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_email = findViewById(R.id.et_email);
        btn_boton = findViewById(R.id.btn_boton);
        btn_listar = findViewById(R.id.btn_listar);
        queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        final String url = "http://192.168.1.39:8080/Agenda3.0/Agenda3";
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
    public void imprimir(final ArrayList<Contacto> misContactos){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,misContactos);
        lv_lista.setAdapter(arrayAdapter);
        lv_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,"Clikado en el item "+i+ "\n" +misContactos.get(i).toString(),Toast.LENGTH_SHORT).show();
            }
        });

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
                        String respuesta = response.trim().replace("null","");
                        String res = respuesta.trim();
                        System.out.println(res);
                        InputStream inputStream = new ByteArrayInputStream(res.getBytes(Charset.forName("UTF-8")));
                        XmlPullParserFactory parserFactory;
                        StringBuilder builder = new StringBuilder();
                        try {
                            ParsearXML toXML  = new ParsearXML();
                             ArrayList<Contacto> misContactos =  (ArrayList) toXML.parsear(inputStream);
                            test = new ArrayList<Contacto>();
                            for (Contacto con : misContactos){
                                String nombre = con.getNombre();
                                String apellido = con.getApellido();
                                String email = con.getEmail();
                               test.add(new Contacto(nombre,apellido,email));
                            }
                            imprimir(test);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
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


