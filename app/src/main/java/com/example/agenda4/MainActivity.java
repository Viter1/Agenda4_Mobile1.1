package com.example.agenda4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tv_nombre,tv_apellido,tv_email;
    EditText et_nombre,et_apellido,et_email;
    Button   btn_boton;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);
        tv_apellido = (TextView)findViewById(R.id.tv_apellido);
        tv_email = (TextView)findViewById(R.id.tv_email);
        et_nombre =  findViewById(R.id.et_nombre);
        et_apellido = findViewById(R.id.et_apellido);
        et_email = findViewById(R.id.et_email);
        btn_boton = findViewById(R.id.btn_boton);
        queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        final String url = "http://192.168.1.42:8080/Agenda3.0/Agenda3";




        btn_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatos(url);
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
                        Toast toastInsertado =
                                Toast.makeText(getApplicationContext(),
                                        "Insertado", Toast.LENGTH_SHORT);

                        toastInsertado.show();
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
