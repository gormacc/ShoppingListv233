package com.example.maciej.shoppinglist

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView


class MyAdapter(private val myDataset: ArrayList<Product>) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    private lateinit var db : MySQLiteHelper

    class ViewHolder(var v: View) : RecyclerView.ViewHolder(v){
        val textView = v.findViewById<TextView>(R.id.my_text_view)!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_item, parent, false)

        db = MySQLiteHelper(parent.context)

        val viewHolder = ViewHolder(view)

        viewHolder.textView.setOnClickListener { view ->
            showEditDialog(parent.context, viewHolder.adapterPosition)
        }

        return viewHolder
    }

    fun removeAt(position: Int) {
        db.deleteProduct(Product(myDataset[position].Id, myDataset[position].Desc))
        myDataset.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = myDataset[position].Desc
    }

    override fun getItemCount() = myDataset.size

    fun showEditDialog(ctx: Context, position: Int) {

        val builder = AlertDialog.Builder(ctx)
        val inflater = LayoutInflater.from(ctx)
        val dialogView = inflater.inflate(R.layout.add_dialog, null)
        builder.setView(dialogView)

        val editText = dialogView.findViewById<EditText>(R.id.add_edit_text)
        var prod = myDataset[position]
        editText.text.append(prod.Desc)

        builder.setTitle(ctx.getString(R.string.edit_element))
        builder.setMessage(ctx.getString(R.string.edit_elem_desc))
        builder.setPositiveButton(ctx.getString(R.string.save), { dialog, whichButton ->

            prod.Desc = editText.text.toString()
            notifyItemChanged(position)
            db.updateProduct(myDataset[position])
            if(ctx is MainActivity){
                if(ctx.fragment.preferenceManager!=null && ctx.fragment.preferenceManager.sharedPreferences.getBoolean(ctx.getString(R.string.sort_setting_key), false)){
                    myDataset.sortBy { it.Desc }
                    notifyDataSetChanged()
                }
            }
        })
        builder.setNegativeButton(ctx.getString(R.string.cancel), { dialog, whichButton ->
            //pass
        })
        val b = builder.create()
        b.show()

    }

}
