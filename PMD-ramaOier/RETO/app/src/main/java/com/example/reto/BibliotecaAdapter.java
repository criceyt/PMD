package com.example.reto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reto.database.modelo.Juego;

import java.util.List;

public class BibliotecaAdapter extends RecyclerView.Adapter<BibliotecaAdapter.ViewHolder> {

    private List<Juego> juegos;
    private Context context;

    public BibliotecaAdapter(List<Juego> juegos, Context context) {
        this.juegos = juegos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout item_biblioteca
        View view = LayoutInflater.from(context).inflate(R.layout.item_biblioteca, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Juego juego = juegos.get(position);
        holder.nombreTextView.setText(juego.getNombre());

        // Obtener el ID del recurso a partir del nombre de la imagen
        int imageResource = holder.itemView.getContext().getResources().getIdentifier(juego.getImagen(), "drawable", holder.itemView.getContext().getPackageName());

        // Verifica si el recurso existe
        if (imageResource != 0) {
            holder.imagenImageView.setImageResource(imageResource);
        } else {
            // Establecer una imagen por defecto o un recurso de error si no se encontró
            holder.imagenImageView.setImageResource(R.drawable.ic_launcher_foreground); // Cambia esto a tu imagen por defecto
        }
    }

    @Override
    public int getItemCount() {
        return juegos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        ImageView imagenImageView; // Si necesitas mostrar una imagen

        public ViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView); // Asegúrate de que este ID coincide
            imagenImageView = itemView.findViewById(R.id.imagenImageView); // Asegúrate de que este ID coincide
        }
    }
}