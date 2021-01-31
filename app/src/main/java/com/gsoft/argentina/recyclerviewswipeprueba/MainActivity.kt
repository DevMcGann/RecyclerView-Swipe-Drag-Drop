package com.gsoft.argentina.recyclerviewswipeprueba

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var  recyclerAdaptador: Adaptador

    private var lenguages = mutableListOf<String>()
    private var lista = mutableListOf<String>()

    private lateinit var lenguageBorrado : String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lenguages.add("Kotlin")
        lenguages.add("Dart")
        lenguages.add("Swift")


        lista.addAll(lenguages)

        recyclerView = findViewById(R.id.rv_lista)
        recyclerAdaptador = Adaptador(lista)

        recyclerView.adapter = recyclerAdaptador

        //val itemTouchHelper = ItemTouchHelper(simpleCallback)
        //itemTouchHelper.attachToRecyclerView(recyclerView)

        val agregar : FloatingActionButton = findViewById(R.id.b_add)

        agregar.setOnClickListener(){
            var editText = EditText(this@MainActivity)

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Agregar")
            builder.setCancelable(true)
            builder.setView(editText)

            builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{dialog, which ->
                lista.clear()
                lista.addAll(lenguages)
                recyclerView.adapter!!.notifyDataSetChanged()
            })

            builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener{dialog, which ->
                lista.add(editText.text.toString())
                recyclerView.adapter!!.notifyDataSetChanged()
            })

            builder.show()
        }

///////////////////////////// coso de botones ///////////////////////
        object : SwipeHelper(this, recyclerView, false) {

            override fun instantiateUnderlayButton(
                    viewHolder: RecyclerView.ViewHolder?,
                    underlayButtons: MutableList<UnderlayButton>?
            ) {
                // Archive Button
                underlayButtons?.add(SwipeHelper.UnderlayButton(
                        "Eliminar",
                        AppCompatResources.getDrawable(
                                this@MainActivity,
                                R.drawable.ic_delete
                        ),
                        Color.parseColor("#FF0000"), Color.parseColor("#ffffff"),
                        UnderlayButtonClickListener { pos: Int ->
                            lista.removeAt(pos);
                            recyclerAdaptador.notifyItemRemoved(pos)
                        }

                ))


                underlayButtons?.add(SwipeHelper.UnderlayButton(
                        "Editar",
                        AppCompatResources.getDrawable(
                                this@MainActivity,
                                R.drawable.ic_edit
                        ),
                        Color.parseColor("#127512"), Color.parseColor("#ffffff"),
                        UnderlayButtonClickListener { pos: Int ->

                            var editText = EditText(this@MainActivity)
                            editText.setText(lista[pos])

                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle("Actualizar")
                            builder.setCancelable(true)
                            builder.setView(editText)

                            builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{dialog, which ->
                                lista.clear()
                                lista.addAll(lenguages)
                                recyclerView.adapter!!.notifyDataSetChanged()
                            })

                            builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener{dialog, which ->
                                lista[pos] = editText.text.toString()
                                recyclerView.adapter!!.notifyItemChanged(pos)
                            })

                            builder.show()
                            recyclerAdaptador.notifyItemChanged(pos)
                        }

                ))


            }
        }
        ///////////////////////////// coso de botones ///////////////////////

        } //ON CREATE


/*    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var posicionInicio = viewHolder.adapterPosition
            var posicionFinal = target.adapterPosition

            Collections.swap(lista, posicionInicio, posicionFinal)
            recyclerView.adapter?.notifyItemMoved(posicionInicio, posicionFinal)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var position : Int  = viewHolder.adapterPosition

            when(direction){
                ItemTouchHelper.LEFT ->{
                    lenguageBorrado = lista[position]//para recuperar con undo
                    lista.removeAt(position)
                    recyclerAdaptador.notifyItemRemoved(position)

                    Snackbar.make(recyclerView, "$lenguageBorrado fue eliminado", Snackbar.LENGTH_LONG).setAction("Undo?", View.OnClickListener {
                        lista.add(position, lenguageBorrado)
                        recyclerAdaptador.notifyItemInserted(position)
                    } ).show()
                }
                ItemTouchHelper.RIGHT -> {
                    var editText = EditText(this@MainActivity)
                    editText.setText(lista[position])

                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("Actualizar")
                    builder.setCancelable(true)
                    builder.setView(editText)

                    builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener{dialog, which ->
                        lista.clear()
                        lista.addAll(lenguages)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    })

                    builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener{dialog, which ->
                        lista[position] = editText.text.toString()
                        recyclerView.adapter!!.notifyItemChanged(position)
                    })

                    builder.show()
                }
            }
        }

    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        var item: MenuItem = menu!!.findItem(R.id.action_search)

        if (item != null){
            var searchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText!!.isNotEmpty()){
                        lista.clear()
                        var busqueda = newText.toLowerCase(Locale.getDefault())

                        for (lenguage in lenguages){
                            if (lenguage.toLowerCase(Locale.getDefault()).contains(busqueda)){
                                lista.add(lenguage)
                            }
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                    }else{
                        lista.clear()
                        lista.addAll(lenguages)
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }

                    return true
                }

            })
        }


        return super.onCreateOptionsMenu(menu)
    }
}