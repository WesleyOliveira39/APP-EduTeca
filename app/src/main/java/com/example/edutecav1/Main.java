package com.example.edutecav1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edutecav1.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends Base {

    ActivityMainBinding activityMainBinding;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Adapter adapter;
    private List<Livro> livrosList;
    ApiInterface apiInterface;
    Adapter.RecyclerViewClickListener listener;
    ProgressBar progressBar;
    FloatingActionButton insertLivro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        alocarTitulo("EduTeca");

        loadLocale();

        apiInterface = ApiEduteca.getApiEduteca().create(ApiInterface.class);

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listener = new Adapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(Main.this, Detail.class);
                intent.putExtra("id_usuario", livrosList.get(position).getId_livro());
                intent.putExtra("titulo", livrosList.get(position).getTitulo());
                intent.putExtra("isbn", livrosList.get(position).getIsbn());
                intent.putExtra("autor", livrosList.get(position).getAutor());
                intent.putExtra("editora", livrosList.get(position).getEditora());
                intent.putExtra("capa", livrosList.get(position).getCapa());
                intent.putExtra("categoria", livrosList.get(position).getCategoria());
                intent.putExtra("biblioteca", livrosList.get(position).getBiblioteca());
                startActivity(intent);

            }

            @Override
            public void onLoveClick(View view, int position) {

                final int id_livro = livrosList.get(position).getId_livro();
                final Boolean favorito = livrosList.get(position).getFavorito();
                final ImageView imgFavorito = view.findViewById(R.id.imgFavorito);

                if (favorito) {
                    imgFavorito.setImageResource(R.drawable.ic_staroff);
                    livrosList.get(position).setFavorito(false);
                    setterFavorito("setFavorito", id_livro, false);
                    adapter.notifyDataSetChanged();
                } else {
                    imgFavorito.setImageResource(R.drawable.ic_staron);
                    livrosList.get(position).setFavorito(true);
                    setterFavorito("setFavorito", id_livro, true);
                    adapter.notifyDataSetChanged();
                }

            }
        };

        insertLivro = findViewById(R.id.insertLivro);
        insertLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main.this, InsertLivro.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName())
        );
        searchView.setQueryHint("Pesquisar Livro...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);

        return true;
    }

    public void getLivros(){

        Call<List<Livro>> call = apiInterface.getLivros();
        call.enqueue(new Callback<List<Livro>>() {
            @Override
            public void onResponse(Call<List<Livro>> call, Response<List<Livro>> response) {
                progressBar.setVisibility(View.GONE);
                livrosList = response.body();
                Log.i(Main.class.getSimpleName(), response.body().toString());
                adapter = new Adapter(livrosList, Main.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Livro>> call, Throwable t) {
                Toast.makeText(Main.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setterFavorito(final String key, final int id_livro, final Boolean favorito){

        Call<Livro> call = apiInterface.setterFavorito(key, id_livro, favorito);

        call.enqueue(new Callback<Livro>() {
            @Override
            public void onResponse(Call<Livro> call, Response<Livro> response) {

                Log.i(Main.class.getSimpleName(), "Response "+response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    Toast.makeText(Main.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Main.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Livro> call, Throwable t) {
                Toast.makeText(Main.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLivros();
    }
}
