package com.example.edutecav1;

import com.google.gson.annotations.SerializedName;

public class Livro {

    @SerializedName("id_livro") private int id_livro;
    @SerializedName("titulo") private String titulo;
    @SerializedName("isbn") private String isbn;
    @SerializedName("autor") private String autor;
    @SerializedName("editora") private String editora;
    @SerializedName("capa") private String capa;
    @SerializedName("categoria") private String categoria;
    @SerializedName("biblioteca") private String biblioteca;
    @SerializedName("favorito") private Boolean favorito;
    @SerializedName("value") private String value;
    @SerializedName("message") private String massage;

    public int getId_livro() {
        return id_livro;
    }

    public void setId_livro(int id_livro) {
        this.id_livro = id_livro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getCapa() {
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(String biblioteca) {
        this.biblioteca = biblioteca;
    }

    public Boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(Boolean favorito) {
        this.favorito = favorito;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}

