package com.example.asynctest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleHomeAdapter extends RecyclerView.Adapter<RecycleHomeAdapter.ViewHolder> {

    List<Book> books;
    Context ctx;
    RecycleViewOnclick recycleViewOnclick;
    public RecycleHomeAdapter(List<Book> books, Context ctx, RecycleViewOnclick onclick){
        this.books = books;
        this.ctx = ctx;
        this.recycleViewOnclick = onclick;
    }

    @NonNull
    @Override
    public RecycleHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycle_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleHomeAdapter.ViewHolder holder, int position) {
        holder.text.setText(books.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public interface RecycleViewOnclick{
        void onItemClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements RecycleViewOnclick {
        TextView text;
        CardView homecard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.bookTitle);
            homecard = itemView.findViewById(R.id.homeCard);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(view, getAdapterPosition());
                }
            });
        }

        @Override
        public void onItemClick(View v, int position) {
            recycleViewOnclick.onItemClick(v, getAdapterPosition());
        }
    }
}
