package com.example.edutecav1;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("getLivros.php")
    Call<List<Livro>> getLivros();

    @FormUrlEncoded
    @POST("setFavorito.php")
    Call<Livro> setterFavorito(
            @Field("key") String key,
            @Field("id_livro") int id_livro,
            @Field("favorito") boolean favorito);

    @FormUrlEncoded
    @POST("updateLivro.php")
    Call<Livro> updateLivro(
            @Field("key") String key,
            @Field("titulo") String titulo,
            @Field("isbn") String isbn,
            @Field("autor") String autor,
            @Field("editora") String editora,
            @Field("categoria") String categoria,
            @Field("biblioteca") String biblioteca);

    @FormUrlEncoded
    @POST("deleteLivro.php")
    Call<Livro> deleteLivro(
            @Field("key") String key,
            @Field("titulo") String titulo,
            @Field("capa") String capa);

    @FormUrlEncoded
    @POST("insertLivro.php")
    Call<Livro> createLivro(
            @Field("key") String key,
            @Field("titulo") String titulo,
            @Field("isbn") String isbn,
            @Field("autor") String autor,
            @Field("editora") String editora,
            @Field("categoria") String categoria,
            @Field("biblioteca") String biblioteca);
}

