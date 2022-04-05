package ca.sfu.iat.fintrack.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.adapters.ListAdapter
import ca.sfu.iat.fintrack.model.Record
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var period: String? = null
    private var budget: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            period = it.getString(ARG_PARAM1)
            budget = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list_recycler)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            val database = Firebase.database.reference
            val uid = FirebaseAuth.getInstance().currentUser?.uid
            database.child("users/$uid/budgets")
                .orderByChild("name")
                .equalTo("$budget").addListenerForSingleValueEvent(object: ValueEventListener {
                    var key: String? = null
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children) {
                            key = dataSnapshot.key.toString()
                        }

                        val recordsQuery = database.child("users/$uid/budgets/$key/records")
                        recordsQuery.addValueEventListener(object: ValueEventListener {
                            val recordsList = ArrayList<Record>()
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (dataSnapshot in snapshot.children) {
                                    val record: Record? = dataSnapshot.getValue<Record>()
                                    if (record != null) {
                                        recordsList.add(record)
                                    }
                                }
                                val dataGroupByDate = recordsList.groupBy { it.date }
                                adapter = ListAdapter(dataGroupByDate)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Getting Post failed, log a message
                                Log.w("Firebase", "loadPost:onCancelled", error.toException())
                            }
                        })

                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}