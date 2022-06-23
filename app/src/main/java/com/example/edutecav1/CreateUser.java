package com.example.edutecav1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateUser extends AppCompatActivity {

    private EditText edtNomeCreate, edtEmailCreate, edtSenhaCreate;
    private Button btnCadastrar;
    private ProgressBar progressCreate;
    private static String URL_CREATE = "http://192.168.15.2/apiEduteca/createUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        progressCreate = findViewById(R.id.progressCreate);
        edtNomeCreate = findViewById(R.id.edtNomeCreate);
        edtEmailCreate = findViewById(R.id.edtEmailCreate);
        edtSenhaCreate = findViewById(R.id.edtSenhaCreate);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cadastrar();
            }
        });

    }

    private void Cadastrar(){
        progressCreate.setVisibility(View.VISIBLE);
        btnCadastrar.setVisibility(View.GONE);

        final String nome = this.edtNomeCreate.getText().toString().trim();
        final String email = this.edtEmailCreate.getText().toString().trim();
        final String senha = this.edtSenhaCreate.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(CreateUser.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CreateUser.this, Login.class));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateUser.this, "Erro em cadastro! " + e.toString(), Toast.LENGTH_SHORT).show();
                            progressCreate.setVisibility(View.GONE);
                            btnCadastrar.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CreateUser.this, "Erro em cadastro! " + error.toString(), Toast.LENGTH_SHORT).show();
                        progressCreate.setVisibility(View.GONE);
                        btnCadastrar.setVisibility(View.VISIBLE);
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nome);
                params.put("email", email);
                params.put("senha", senha);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}
