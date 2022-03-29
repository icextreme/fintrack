package ca.sfu.iat.fintrack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.sfu.iat.fintrack.R


class ListAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val recyclerView: RecyclerView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.date_header)
            recyclerView = view.findViewById(R.id.category_recycler)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position]
        viewHolder.recyclerView.apply {
            layoutManager = LinearLayoutManager(viewHolder.recyclerView.context)
            val dummyItems = arrayOf("Restaurant", "Clothing", "Gas", "Pet")
            adapter = CategoryAdapter(dummyItems)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}