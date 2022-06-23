package com.example.edutecav1;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    Adapter adapter;
    ArrayList<Livro> filterList;

    public CustomFilter(ArrayList<Livro> filterList, Adapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint != null && constraint.length() > 0)
        {
            constraint=constraint.toString().toUpperCase();
            ArrayList<Livro> filteredLivro = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                if(filterList.get(i).getTitulo().toUpperCase().contains(constraint))
                {
                    filteredLivro.add(filterList.get(i));
                }
            }

            results.count=filteredLivro.size();
            results.values=filteredLivro;

        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.livros= (ArrayList<Livro>) results.values;

        adapter.notifyDataSetChanged();

    }
}
