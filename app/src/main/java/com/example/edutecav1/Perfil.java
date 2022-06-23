package com.example.edutecav1;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity {

    private static final String TAG = Perfil.class.getSimpleName();
    private static String URL_READ = "http://192.168.15.2/apiEduteca/readUser.php";
    private static String URL_UPDATE = "http://192.168.15.2/apiEduteca/updateUser.php";
    private static String URL_SETTER = "http://192.168.15.2/apiEduteca/setFoto.php";
    private Menu menuUser;
    private EditText edtNome, edtEmail, edtCpf, edtTelefone, edtEndereco, edtCidade, edtUsername;
    private Button btnLogout;
    private FloatingActionButton btnFoto;
    private Bitmap bitmap;
    CircleImageView imgUser;
    SessionUser sessionUser;
    String getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        sessionUser = new SessionUser(this);
        sessionUser.checkLogin();

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtCpf = findViewById(R.id.edtCpf);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtEndereco = findViewById(R.id.edtEndereco);
        edtCidade = findViewById(R.id.edtCidade);
        edtUsername = findViewById(R.id.edtUsername);
        btnLogout = findViewById(R.id.btnLogout);
        btnFoto = findViewById(R.id.btnFoto);
        imgUser = findViewById(R.id.imgUser);

        HashMap<String, String> user = sessionUser.getUser();
        getId = user.get(sessionUser.ID);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionUser.logout();
            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFoto();
            }
        });

    }

    private void getUser(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")){

                                for (int i =0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String nome = object.getString("nome").trim();
                                    String email = object.getString("email").trim();
                                    String cpf = object.getString("cpf").trim();
                                    String telefone = object.getString("telefone").trim();
                                    String endereco = object.getString("endereco").trim();
                                    String cidade = object.getString("cidade").trim();
                                    String username = object.getString("username").trim();

                                    edtNome.setText(nome);
                                    edtEmail.setText(email);
                                    edtCpf.setText(cpf);
                                    edtTelefone.setText(telefone);
                                    edtEndereco.setText(endereco);
                                    edtCidade.setText(cidade);
                                    edtUsername.setText(username);

                                }

                            }

                        } catch (JSONException error) {
                            error.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Perfil.this, "Falha na busca do perfil..."+error.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Perfil.this, "Falha na busca do perfil..."+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("id_usuario", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_user, menu);

        menuUser = menu;
        menuUser.findItem(R.id.menu_save).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_update:

                edtNome.setFocusableInTouchMode(true);
                edtEmail.setFocusableInTouchMode(true);

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(edtNome, InputMethodManager.SHOW_IMPLICIT);

                menuUser.findItem(R.id.menu_update).setVisible(false);
                menuUser.findItem(R.id.menu_save).setVisible(true);

                return true;

            case R.id.menu_save:

                SaveUser();

                menuUser.findItem(R.id.menu_update).setVisible(true);
                menuUser.findItem(R.id.menu_save).setVisible(false);

                edtNome.setFocusableInTouchMode(false);
                edtEmail.setFocusableInTouchMode(false);
                edtNome.setFocusable(false);
                edtEmail.setFocusable(false);

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    //save
    private void SaveUser() {

        final String nomeSalvo = this.edtNome.getText().toString().trim();
        final String emailSalvo = this.edtEmail.getText().toString().trim();
        final String cpfSalvo = this.edtCpf.getText().toString().trim();
        final String telefoneSalvo = this.edtTelefone.getText().toString().trim();
        final String enderecoSalvo = this.edtEndereco.getText().toString().trim();
        final String cidadeSalvo = this.edtCidade.getText().toString().trim();
        final String usernameSalvo = this.edtUsername.getText().toString().trim();
        final String idSalvo = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Salvando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(Perfil.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                                sessionUser.createSession(nomeSalvo, emailSalvo, idSalvo);
                            }
                            startActivity(new Intent(Perfil.this, Main.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Perfil.this, "Error "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Perfil.this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nome", nomeSalvo);
                params.put("email", emailSalvo);
                params.put("id_usuario", idSalvo);
                params.put("cpf", cpfSalvo);
                params.put("telefone", telefoneSalvo);
                params.put("endereco", enderecoSalvo);
                params.put("cidade", cidadeSalvo);
                params.put("username", usernameSalvo);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void setFoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgUser.setImageBitmap(bitmap);

            } catch (IOException error) {
                error.printStackTrace();
            }

            setterFoto(getId, getStringImage(bitmap));

        }
    }

    private void setterFoto(final String id, final String foto) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SETTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(Perfil.this, "Success!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException error) {
                            error.printStackTrace();
                            progressDialog.dismiss();
                            //Toast.makeText(Perfil.this, "Tenta novamente!"+error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //Toast.makeText(Perfil.this, "Tenta novamente!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_usuario", id);
                params.put("foto", foto);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }
}
