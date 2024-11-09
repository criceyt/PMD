package com.example.reto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reto.database.modelo.Juego;

import java.util.List;

public class JuegoBibliotecaAdapter extends RecyclerView.Adapter<JuegoBibliotecaAdapter.ViewHolder> {

    private List<Juego> juegos;
    private Context context;

    public JuegoBibliotecaAdapter(List<Juego> juegos, Context context) {
        this.juegos = juegos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_biblioteca, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Juego juego = juegos.get(position);
        holder.bind(juego);

        // Cargar la imagen usando Glide
        int resourceId = context.getResources().getIdentifier(juego.getImagen(), "drawable", context.getPackageName());
        Glide.with(context)
                .load(resourceId)
                .override(100, 100)
                .into(holder.imagenImageView);
    }

    @Override
    public int getItemCount() {
        return juegos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenImageView;
        TextView nombreTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenImageView = itemView.findViewById(R.id.imagenImageView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
        }

        public void bind(Juego juego) {
            nombreTextView.setText(juego.getNombre());
        }
    }
}
