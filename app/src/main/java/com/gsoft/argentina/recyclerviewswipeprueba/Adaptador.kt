package com.gsoft.argentina.recyclerviewswipeprueba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView

class Adaptador(listaDeElementos: MutableList<String>) : RecyclerView.Adapter<Adaptador.EjemploViewHolder>() {


    private var lista : MutableList<String> = listaDeElementos



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjemploViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent , false)
        return EjemploViewHolder(vista)
    }

    override fun onBindViewHolder(holder: EjemploViewHolder, position: Int) {
        holder.texto.text = lista[position]
    }

    override fun getItemCount(): Int {
        return lista.size
    }


    inner class EjemploViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val texto : TextView

        init {
            texto = itemView.findViewById(R.id.t_texto)

            itemView.setOnClickListener(){
                val position : Int = adapterPosition
                Toast.makeText(itemView.context, "Hiciste click en  ${lista[position]}", Toast.LENGTH_LONG).show()
            }
        }


    }

}




