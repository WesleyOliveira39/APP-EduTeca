package com.example.edutecav1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.edutecav1.databinding.ActivityDetailBinding;
import com.example.edutecav1.databinding.ActivityInsertLivroBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertLivro extends Base {

    ActivityInsertLivroBinding activityInsertLivroBinding;
    private ApiInterface apiInterface;
    private EditText edtNTitulo, edtNIsbn, edtNAutor, edtNEditora, edtNCategoria, edtNBiblioteca;
    //private ImageView imageNCapa;
    private Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityInsertLivroBinding = ActivityInsertLivroBinding.inflate(getLayoutInflater());
        setContentView(activityInsertLivroBinding.getRoot());

        edtNTitulo = findViewById(R.id.txtNTitulo);
        edtNIsbn = findViewById(R.id.txtNIsbn);
        edtNAutor = findViewById(R.id.txtNAutor);
        edtNEditora = findViewById(R.id.txtNEditora);
        edtNCategoria = findViewById(R.id.txtNCategoria);
        edtNBiblioteca = findViewById(R.id.txtNBiblioteca);
        //imageNCapa = findViewById(R.id.imgNCapa);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLivro("insert");
            }
        });
    }

    private void createLivro(final String key) {

        //getSupportActionBar().setTitle("Novo livro:");

        String titulo = edtNTitulo.getText().toString().trim();
        String isbn = edtNIsbn.getText().toString().trim();
        String autor = edtNAutor.getText().toString().trim();
        String editora = edtNEditora.getText().toString().trim();
        String categoria = edtNCategoria.getText().toString().trim();
        String biblioteca = edtNBiblioteca.getText().toString().trim();

        /*
        String picture = null;
        if (bitmap == null) {
            picture = "";
        } else {
            picture = getStringImage(bitmap);
        }*/

        apiInterface = ApiEduteca.getApiEduteca().create(ApiInterface.class);

        Call<Livro> call = apiInterface.createLivro(key, titulo, isbn, autor, editora, categoria, biblioteca);

        call.enqueue(new Callback<Livro>() {
            @Override
            public void onResponse(Call<Livro> call, Response<Livro> response) {

                Log.i(InsertLivro.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    Toast.makeText(InsertLivro.this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(InsertLivro.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Livro> call, Throwable t) {
                Toast.makeText(InsertLivro.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}