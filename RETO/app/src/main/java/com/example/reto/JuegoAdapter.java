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

import com.bumptech.glide.Glide;  // Biblioteca externa para cargar imágenes de manera eficiente
import com.example.reto.database.modelo.Juego;

import java.util.List;

public class JuegoAdapter extends RecyclerView.Adapter<JuegoAdapter.ViewHolder> {

    private List<Juego> juegos;  // Lista de objetos Juego a mostrar en el RecyclerView
    private Context context;  // Contexto donde se utilizará este adaptador (por ejemplo, la actividad o fragmento)
    private OnJuegoClickListener listener;  // Interfaz para manejar los clics en los elementos de la lista

    // Define una interfaz para manejar clics en los elementos del RecyclerView
    public interface OnJuegoClickListener {
        void onJuegoClick(Juego juego);  // Método que se llama cuando se hace clic en un juego
    }

    // Constructor del adaptador, recibe la lista de juegos, el contexto y el listener para los clics
    public JuegoAdapter(List<Juego> juegos, Context context, OnJuegoClickListener listener) {
        this.juegos = juegos;
        this.context = context;
        this.listener = listener;
    }

    // Este método crea la vista de cada ítem del RecyclerView
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout 'activity_juego' que se usará para cada ítem del RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.activity_juego, parent, false);
        return new ViewHolder(view);  // Devuelve un nuevo ViewHolder con la vista inflada
    }

    // Este método enlaza los datos de cada ítem con las vistas de cada ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Juego juego = juegos.get(position);  // Obtiene el objeto Juego correspondiente a la posición

        // Llama al método bind() para establecer los datos del juego y configurar el clic
        holder.bind(juego, listener);

        // Carga la imagen del juego usando Glide
        int resourceId = context.getResources().getIdentifier(juego.getImagen(), "drawable", context.getPackageName());

        // Utiliza Glide para cargar la imagen desde los recursos (si existe) y ajustarla a un tamaño de 48x48
        Glide.with(context)
                .load(resourceId)  // Carga la imagen desde los recursos
                .override(48, 48)  // Define el tamaño de la imagen cargada
                .into(holder.imagenImageView);  // Coloca la imagen cargada en el ImageView
    }

    // Método para obtener el número de ítems (juegos) que hay en la lista
    @Override
    public int getItemCount() {
        return juegos.size();  // Devuelve el tamaño de la lista de juegos
    }

    // Clase ViewHolder que mantiene las vistas de cada ítem del RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;  // Vista para mostrar el nombre del juego
        TextView precioTextView;  // Vista para mostrar el precio del juego
        ImageView imagenImageView;  // Vista para mostrar la imagen del juego

        // Constructor que inicializa las vistas del layout 'activity_juego'
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);  // Enlaza el TextView para el nombre del juego
            precioTextView = itemView.findViewById(R.id.precioTextView);  // Enlaza el TextView para el precio del juego
            imagenImageView = itemView.findViewById(R.id.imagenImageView);  // Enlaza el ImageView para la imagen del juego
        }

        // Método para establecer los datos y configurar el clic en cada ítem
        public void bind(final Juego juego, final OnJuegoClickListener listener) {
            nombreTextView.setText(juego.getNombre());  // Establece el nombre del juego en el TextView
            precioTextView.setText(juego.getPrecio());  // Establece el precio del juego en el TextView

            // Establece un OnClickListener para el clic en el ítem
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onJuegoClick(juego);  // Llama al método onJuegoClick() cuando el usuario hace clic
                }
            });
        }
    }
}
