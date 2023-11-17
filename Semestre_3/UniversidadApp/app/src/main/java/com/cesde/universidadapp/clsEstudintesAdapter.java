package com.cesde.universidadapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class clsEstudintesAdapter extends RecyclerView.Adapter <clsEstudintesAdapter.clsestudiantesViewHolder> {
    private ArrayList<clsEstudiantes> listaEstudiantes;

    public clsEstudintesAdapter(ArrayList<clsEstudiantes> listaEstudiantes){
        this.listaEstudiantes = listaEstudiantes;
    }

    @NonNull
    @Override
    public clsEstudintesAdapter.clsestudiantesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull clsEstudintesAdapter.clsestudiantesViewHolder holder, int position) {
        holder.tvcarnet.setText(listaEstudiantes.get(position).getCarnet());
        holder.tvnombre.setText(listaEstudiantes.get(position).getNombre());
        holder.tvcarrera.setText(listaEstudiantes.get(position).getCarrera());
        holder.tvsemestre.setText(listaEstudiantes.get(position).getSemestre());

        if (listaEstudiantes.get(position).getActivo().equals("Si")) {
            holder.cbactivo.setChecked(true);
        }else {
            holder.cbactivo.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return listaEstudiantes.size();
    }

    public static class clsestudiantesViewHolder extends RecyclerView.ViewHolder {
        TextView tvcarnet, tvnombre, tvcarrera, tvsemestre;
        CheckBox cbactivo;
        public clsestudiantesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcarnet = itemView.findViewById(R.id.etcarnet);
            tvnombre = itemView.findViewById(R.id.etnombre);
            tvcarrera = itemView.findViewById(R.id.etcarrera);
            tvsemestre = itemView.findViewById(R.id.etsemestre);
            cbactivo = itemView.findViewById(R.id.cbactivo);
        }
    }
}
