package com.example.edutecav1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.edutecav1.databinding.ActivityDetailBinding;
import com.example.edutecav1.databinding.ActivityMainBinding;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail extends Base {

    ActivityDetailBinding activityDetailBinding;
    private ApiInterface apiInterface;
    private EditText edtTitulo, edtIsbn, edtAutor, edtEditora, edtCategoria, edtBiblioteca;
    private ImageView imageCapa;
    private String titulo, isbn, autor, editora, capa, categoria, biblioteca;
    private int id_livro;
    private Button btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(activityDetailBinding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        edtTitulo = findViewById(R.id.txtTitulo);
        edtIsbn = findViewById(R.id.txtIsbn);
        edtAutor = findViewById(R.id.txtAutor);
        edtEditora = findViewById(R.id.txtEditora);
        edtCategoria = findViewById(R.id.txtCategoria);
        edtBiblioteca = findViewById(R.id.txtBiblioteca);
        imageCapa = findViewById(R.id.imgCapa);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();
        id_livro = intent.getIntExtra("id_livro", 0);
        titulo = intent.getStringExtra("titulo");
        isbn = intent.getStringExtra("isbn");
        autor = intent.getStringExtra("autor");
        editora = intent.getStringExtra("editora");
        capa = intent.getStringExtra("capa");
        categoria = intent.getStringExtra("categoria");
        biblioteca = intent.getStringExtra("biblioteca");

        setDataFromIntentExtra();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLivro("update");
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Detail.this);
                dialog.setMessage("Deseja deletar esse livro?");
                dialog.setPositiveButton("Sim" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteLivro("delete", titulo, capa);
                        finish();
                    }
                });
                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }


    private void setDataFromIntentExtra() {

        if (id_livro != 1) {

            //modoLeitura();
            getSupportActionBar().setTitle(titulo.toString());

            edtTitulo.setText(titulo);
            edtIsbn.setText(isbn);
            edtAutor.setText(autor);
            edtEditora.setText(editora);
            edtCategoria.setText(categoria);
            edtBiblioteca.setText(biblioteca);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.logo);
            requestOptions.error(R.drawable.logo);

            Glide.with(Detail.this)
                    .load(capa)
                    .apply(requestOptions)
                    .into(imageCapa);
        } else {
            getSupportActionBar().setTitle("Livro inválido!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateLivro(final String key) {

        //modoLeitura();

        String titulo = edtTitulo.getText().toString().trim();
        String isbn = edtIsbn.getText().toString().trim();
        String autor = edtAutor.getText().toString().trim();
        String editora = edtEditora.getText().toString().trim();
        String categoria = edtCategoria.getText().toString().trim();
        String biblioteca = edtBiblioteca.getText().toString().trim();


        apiInterface = ApiEduteca.getApiEduteca().create(ApiInterface.class);

        Call<Livro> call = apiInterface.updateLivro(key, titulo, isbn, autor, editora, categoria, biblioteca);

        call.enqueue(new Callback<Livro>() {
            @Override
            public void onResponse(Call<Livro> call, Response<Livro> response) {

                Log.i(Detail.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    Toast.makeText(Detail.this, "Atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //Toast.makeText(Detail.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(Call<Livro> call, Throwable t) {
                Toast.makeText(Detail.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLivro(final String key, final String titulo, final String capa) {

        //modoLeitura();

        apiInterface = ApiEduteca.getApiEduteca().create(ApiInterface.class);

        Call<Livro> call = apiInterface.deleteLivro(key, titulo, capa);

        call.enqueue(new Callback<Livro>() {
            @Override
            public void onResponse(Call<Livro> call, Response<Livro> response) {

                Log.i(Detail.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    //Toast.makeText(Detail.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Detail.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Livro> call, Throwable t) {
                //Toast.makeText(Detail.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    void modoLeitura(){

        edtTitulo.setFocusableInTouchMode(false);
        edtIsbn.setFocusableInTouchMode(false);
        edtAutor.setFocusableInTouchMode(false);
        edtEditora.setFocusableInTouchMode(false);
        edtCategoria.setFocusableInTouchMode(false);
        edtBiblioteca.setFocusableInTouchMode(false);

        edtTitulo.setFocusable(false);
        edtIsbn.setFocusable(false);
        edtAutor.setFocusable(false);
        edtEditora.setFocusable(false);
        edtCategoria.setFocusable(false);
        edtBiblioteca.setFocusable(false);
    }
}