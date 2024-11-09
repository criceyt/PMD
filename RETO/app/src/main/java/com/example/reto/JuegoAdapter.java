// JuegoAdapter.java
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

public class JuegoAdapter extends RecyclerView.Adapter<JuegoAdapter.ViewHolder> {

    private List<Juego> juegos;
    private Context context;

    public JuegoAdapter(List<Juego> juegos, Context context) {
        this.juegos = juegos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_juego, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Juego juego = juegos.get(position);
        holder.nombreTextView.setText(juego.getNombre());
        holder.precioTextView.setText(juego.getPrecio());

        // Cargar la imagen usando Glide
        int resourceId = context.getResources().getIdentifier(juego.getImagen(), "drawable", context.getPackageName());
        Glide.with(context)
                .load(resourceId) // Usar el ID del recurso
                .override(48, 48) // Ajustar el tamaño de la imagen
                .into(holder.imagenImageView);
    }

    @Override
    public int getItemCount() {
        return juegos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView precioTextView;
        ImageView imagenImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            precioTextView = itemView.findViewById(R.id.precioTextView);
            imagenImageView = itemView.findViewById(R.id.imagenImageView);
        }
    }
}
