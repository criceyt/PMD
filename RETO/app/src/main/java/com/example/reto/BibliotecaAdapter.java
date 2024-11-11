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

    private List<Juego> juegos;  // Lista de objetos Juego que se mostrarán en el RecyclerView
    private Context context;  // Contexto de la actividad o fragmento donde se usará el adaptador

    // Constructor que recibe la lista de juegos y el contexto
    public BibliotecaAdapter(List<Juego> juegos, Context context) {
        this.juegos = juegos;
        this.context = context;
    }

    // Método para crear una nueva vista de ítem (ViewHolder)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout item_biblioteca que representa cada celda del RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_biblioteca, parent, false);
        return new ViewHolder(view);  // Crea y retorna un nuevo ViewHolder con la vista inflada
    }

    // Método para asignar los datos a las vistas del ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Juego juego = juegos.get(position);  // Obtiene el objeto Juego correspondiente a la posición

        // Establece el nombre del juego en el TextView
        holder.nombreTextView.setText(juego.getNombre());

        // Obtiene el ID del recurso de imagen a partir del nombre del archivo de la imagen (que se guarda en el objeto Juego)
        int imageResource = holder.itemView.getContext().getResources().getIdentifier(juego.getImagen(), "drawable", holder.itemView.getContext().getPackageName());

        // Verifica si se encontró el recurso de imagen
        if (imageResource != 0) {
            // Si el recurso existe, asigna la imagen al ImageView
            holder.imagenImageView.setImageResource(imageResource);
        } else {
            // Si no se encontró el recurso, establece una imagen por defecto o de error
            holder.imagenImageView.setImageResource(R.drawable.ic_launcher_foreground); // Cambia esto a tu imagen por defecto
        }
    }

    // Método para obtener el número total de ítems (juegos) en el RecyclerView
    @Override
    public int getItemCount() {
        return juegos.size();  // Devuelve el tamaño de la lista de juegos
    }

    // Clase ViewHolder que mantiene las vistas de cada ítem (un juego) en el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;  // Vista para mostrar el nombre del juego
        ImageView imagenImageView;  // Vista para mostrar la imagen del juego

        // Constructor que asigna las vistas del layout item_biblioteca a las variables
        public ViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);  // Obtiene la vista TextView para el nombre del juego
            imagenImageView = itemView.findViewById(R.id.imagenImageView);  // Obtiene la vista ImageView para la imagen del juego
        }
    }
}
