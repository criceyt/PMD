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

    private List<Juego> juegos;               // Lista de juegos a mostrar
    private Context context;                  // Contexto de la actividad para inflar vistas y cargar imágenes
    private OnJuegoClickListener listener;     // Interfaz para manejar clics en los items

    // Define la interfaz que permite manejar los clics en un juego
    public interface OnJuegoClickListener {
        void onJuegoClick(Juego juego);         // Método que se llama cuando se hace clic en un juego
    }

    // Constructor del adaptador que recibe la lista de juegos, el contexto y el listener para los clics
    public JuegoAdapter(List<Juego> juegos, Context context, OnJuegoClickListener listener) {
        this.juegos = juegos;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout de cada item del RecyclerView (cada juego)
        View view = LayoutInflater.from(context).inflate(R.layout.activity_juego, parent, false);
        return new ViewHolder(view);  // Devuelve un ViewHolder con la vista inflada
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtiene el juego de la lista en la posición actual
        Juego juego = juegos.get(position);

        // Vincula los datos del juego con las vistas del ViewHolder
        holder.bind(juego, listener);

        // Carga la imagen usando Glide (una librería para cargar imágenes de forma eficiente)
        int resourceId = context.getResources().getIdentifier(juego.getImagen(), "drawable", context.getPackageName());
        Glide.with(context)  // Contexto para cargar la imagen
                .load(resourceId)  // Carga la imagen desde el recurso identificado
                .override(48, 48)   // Establece el tamaño de la imagen (48x48 píxeles)
                .into(holder.imagenImageView);  // Asigna la imagen al ImageView del ViewHolder
    }

    @Override
    public int getItemCount() {
        // Devuelve el tamaño de la lista de juegos
        return juegos.size();
    }

    // ViewHolder es una clase interna que mantiene las referencias a las vistas de cada item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;      // TextView para mostrar el nombre del juego
        TextView precioTextView;      // TextView para mostrar el precio del juego
        ImageView imagenImageView;   // ImageView para mostrar la imagen del juego

        // Constructor que inicializa las vistas del item
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);  // Inicializa el nombre
            precioTextView = itemView.findViewById(R.id.precioTextView);  // Inicializa el precio
            imagenImageView = itemView.findViewById(R.id.imagenImageView);  // Inicializa la imagen
        }

        // Método que vincula el juego con las vistas y maneja el clic en el item
        public void bind(final Juego juego, final OnJuegoClickListener listener) {
            nombreTextView.setText(juego.getNombre());   // Establece el nombre del juego en el TextView
            precioTextView.setText(juego.getPrecio());   // Establece el precio del juego en el TextView

            // Configura un listener para el clic en el item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Llama al método onJuegoClick del listener cuando se hace clic en el item
                    listener.onJuegoClick(juego);
                }
            });
        }
    }
}

