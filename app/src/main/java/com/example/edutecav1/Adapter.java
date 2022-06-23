package com.example.edutecav1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    List<Livro> livros, livrosFilter;
    private Context context;
    private RecyclerViewClickListener rListener;
    CustomFilter filter;

    public Adapter(List<Livro> livros, Context context, RecyclerViewClickListener listener) {
        this.livros = livros;
        this.livrosFilter = livros;
        this.context = context;
        this.rListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view, rListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.txtTitulo.setText(livros.get(position).getTitulo());
        holder.txtAutor.setText(livros.get(position).getAutor());
        holder.txtEditora.setText(livros.get(position).getEditora());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);

        Glide.with(context)
                .load(livros.get(position).getCapa())
                .apply(requestOptions)
                .into(holder.imgCapa);

        final Boolean favorito = livros.get(position).getFavorito();

        if (favorito){
            holder.imgFavorito.setImageResource(R.drawable.ic_staron);
        } else {
            holder.imgFavorito.setImageResource(R.drawable.ic_staroff);
        }

    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    @Override
    public Filter getFilter() {
        if (filter==null) {
            filter=new CustomFilter((ArrayList<Livro>) livrosFilter,this);

        }
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewClickListener rListener;
        private ImageView imgCapa, imgFavorito;
        private TextView txtTitulo, txtAutor, txtEditora;
        private LinearLayout linearLayout;
        //private RelativeLayout relativeLayout;

        public MyViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtAutor = itemView.findViewById(R.id.txtAutor);
            txtEditora = itemView.findViewById(R.id.txtEditora);
            imgCapa = itemView.findViewById(R.id.imgCapa);
            imgFavorito = itemView.findViewById(R.id.imgFavorito);
            linearLayout = itemView.findViewById(R.id.row_container);

            rListener = listener;
            linearLayout.setOnClickListener(this);
            imgFavorito.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.row_container:
                    rListener.onRowClick(linearLayout, getAdapterPosition());
                    break;
                case R.id.imgFavorito:
                    rListener.onLoveClick(imgFavorito, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);
        void onLoveClick(View view, int position);
    }

}