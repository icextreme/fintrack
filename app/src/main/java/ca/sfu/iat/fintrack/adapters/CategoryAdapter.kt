package ca.sfu.iat.fintrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.model.Record

class CategoryAdapter(private val data: ArrayList<Record>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.category_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var output = "Name: ${data[position].recordName}\n" +
                "Amount: $${data[position].amount}\n" +
                "Category: ${data[position].category}\n" +
                "Type: ${data[position].type}"
        holder.textView.text = output
    }

    override fun getItemCount(): Int {
        return data.size
    }
}