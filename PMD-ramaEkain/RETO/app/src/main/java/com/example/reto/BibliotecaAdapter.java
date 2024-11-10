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

    private List<Juego> juegos;   // Lista de objetos Juego que se van a mostrar
    private Context context;       // Contexto de la actividad donde se usa el adaptador

    // Constructor que recibe la lista de juegos y el contexto
    public BibliotecaAdapter(List<Juego> juegos, Context context) {
        this.juegos = juegos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout item_biblioteca, que es el diseño individual de cada item en el RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_biblioteca, parent, false);
        return new ViewHolder(view); // Devuelve el ViewHolder con la vista inflada
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtiene el objeto 'Juego' correspondiente a la posición actual
        Juego juego = juegos.get(position);

        // Asigna el nombre del juego al TextView
        holder.nombreTextView.setText(juego.getNombre());

        // Obtiene el ID del recurso de la imagen a partir del nombre de la imagen en el objeto 'Juego'
        int imageResource = holder.itemView.getContext().getResources().getIdentifier(
                juego.getImagen(),   // Nombre de la imagen en el objeto 'Juego'
                "drawable",          // Tipo de recurso (drawable)
                holder.itemView.getContext().getPackageName() // Paquete de la app
        );

        // Verifica si el recurso de la imagen fue encontrado
        if (imageResource != 0) {
            // Si se encuentra el recurso, se establece como la imagen en el ImageView
            holder.imagenImageView.setImageResource(imageResource);
        } else {
            // Si no se encuentra el recurso, se asigna una imagen predeterminada
            holder.imagenImageView.setImageResource(R.drawable.ic_launcher_foreground); // Cambia a una imagen predeterminada de tu elección
        }
    }

    @Override
    public int getItemCount() {
        // Retorna la cantidad de elementos (juegos) que hay en la lista
        return juegos.size();
    }

    // ViewHolder es una clase interna que mantiene las referencias a las vistas del item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;   // TextView para mostrar el nombre del juego
        ImageView imagenImageView; // ImageView para mostrar la imagen del juego

        // Constructor del ViewHolder, donde se inicializan las vistas
        public ViewHolder(View itemView) {
            super(itemView);
            // Inicializa las vistas del layout item_biblioteca
            nombreTextView = itemView.findViewById(R.id.nombreTextView); // Asegúrate de que el ID coincida con el del layout
            imagenImageView = itemView.findViewById(R.id.imagenImageView); // Asegúrate de que el ID coincida con el del layout
        }
    }
}
