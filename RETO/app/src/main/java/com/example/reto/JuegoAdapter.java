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
    private OnJuegoClickListener listener;

    // Define la interfaz para manejar clics
    public interface OnJuegoClickListener {
        void onJuegoClick(Juego juego);
    }

    public JuegoAdapter(List<Juego> juegos, Context context, OnJuegoClickListener listener) {
        this.juegos = juegos;
        this.context = context;
        this.listener = listener;
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
        holder.bind(juego, listener);

        // Cargar la imagen usando Glide
        int resourceId = context.getResources().getIdentifier(juego.getImagen(), "drawable", context.getPackageName());
        Glide.with(context)
                .load(resourceId)
                .override(48, 48)
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

        public void bind(final Juego juego, final OnJuegoClickListener listener) {
            nombreTextView.setText(juego.getNombre());
            precioTextView.setText(juego.getPrecio());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onJuegoClick(juego);
                }
            });
        }
    }
}
