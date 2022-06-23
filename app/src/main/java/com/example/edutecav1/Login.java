package com.example.edutecav1;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText edtEmailLogin, edtSenhaLogin;
    private Button btnLogar;
    private TextView txtRegisterUser;
    private ProgressBar progressLogin;
    private static String URL_LOGIN = "http://192.168.15.2/apiEduteca/login.php";
    SessionUser sessionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionUser = new SessionUser(this);

        progressLogin = findViewById(R.id.progressLogin);
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtSenhaLogin = findViewById(R.id.edtSenhaLogin);
        btnLogar = findViewById(R.id.btnLogar);
        txtRegisterUser = findViewById(R.id.txtRegisterUser);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailLogar = edtEmailLogin.getText().toString().trim();
                String senhaLogar = edtSenhaLogin.getText().toString().trim();

                if (!emailLogar.isEmpty() || !senhaLogar.isEmpty()) {
                    Logar(emailLogar, senhaLogar);
                } else {
                    edtEmailLogin.setError("Insere um email, por favor!");
                    edtSenhaLogin.setError("Insere uma senha, por favor!");
                }
            }
        });

        txtRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, CreateUser.class));
            }
        });

    }

    private void Logar(final String email, final String senha) {

        progressLogin.setVisibility(View.VISIBLE);
        btnLogar.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String nome = object.getString("nome").trim();
                                    String email = object.getString("email").trim();
                                    String id = object.getString("id_usuario").trim();

                                    sessionUser.createSession(nome, email, id);

                                    Intent intent = new Intent(Login.this, Main.class);
                                    intent.putExtra("nome", nome);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                    finish();

                                    progressLogin.setVisibility(View.GONE);
                                }

                            }

                        } catch (JSONException error) {
                            error.printStackTrace();
                            progressLogin.setVisibility(View.INVISIBLE);
                            btnLogar.setVisibility(View.VISIBLE);
                            Toast.makeText(Login.this, "Erro de login. Por favor, insere seus dados novamente!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressLogin.setVisibility(View.INVISIBLE);
                        btnLogar.setVisibility(View.VISIBLE);
                        Toast.makeText(Login.this, "Erro de login, por favor insere seus dados novamente!" +error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("senha", senha);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
