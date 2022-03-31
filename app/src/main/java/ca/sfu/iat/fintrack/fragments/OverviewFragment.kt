package ca.sfu.iat.fintrack.fragments
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.activities.AddBudgetActivity
import ca.sfu.iat.fintrack.model.Budget
import ca.sfu.iat.fintrack.model.Record
import com.google.android.material.tabs.TabLayout
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
 * Use the [LandingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var isGraphView: Boolean = true
    private lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            displayGraphFragment()
        }
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        displayGraphFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Init Firebase database
        val database = Firebase.database.reference
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tabLayout.selectedTabPosition) {
                    0 -> displayGraphFragment()
                    1 -> displayListFragment()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        val welcomeUserTextView: TextView = view.findViewById(R.id.textViewWelcome)
        val username = Firebase.database.reference.child("users/$uid/firstName").get().addOnSuccessListener {
            welcomeUserTextView.text = String.format(getString(R.string.welcome_user), it.value)
        }.addOnFailureListener {
            welcomeUserTextView.text = String.format(getString(R.string.welcome_user), "USER")
        }

        val spinnerPeriod: Spinner = view.findViewById(R.id.spinnerPeriod)
        val spinnerBudget: Spinner = view.findViewById(R.id.spinnerBudget)

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.period_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerPeriod.adapter = adapter
            }
        }

        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.budget_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerBudget.adapter = adapter
            }
        }

        spinnerBudget.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 2) {
                    val intent = Intent (activity, AddBudgetActivity::class.java)
                    activity?.startActivity(intent)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val recordsQuery = database.child("users/$uid/records")
        recordsQuery.addValueEventListener(object: ValueEventListener {
            val budget = Budget("default")
            override fun onDataChange(snapshot: DataSnapshot) {
                var balance: Double = 0.0
                var expense: Double = 0.0
                var income: Double = 0.0
                for (dataSnapshot in snapshot.children) {
                    val record: Record? = dataSnapshot.getValue<Record>()
                    if (record != null) {
                        if (record.type == getString(R.string.income)) {
                            income += record.amount
                            balance += record.amount
                        } else if (record.type == getString((R.string.expense))) {
                            expense += record.amount
                            balance -= record.amount
                        }
                    }
                }
                val balanceTextView :TextView = view.findViewById(R.id.textViewBalance)
                val expenseTextView :TextView = view.findViewById(R.id.textViewExpense)
                val incomeTextView :TextView = view.findViewById(R.id.textViewIncome)
                val str1 = "$ " + String.format("%.2f", balance) + "\nBalance"
                val str2 = "$ " + String.format("%.2f", expense) + "\nExpense"
                val str3 = "$ " + String.format("%.2f", income) + "\nIncome"
                balanceTextView.text = str1
                expenseTextView.text = str2
                incomeTextView.text = str3
            }

            override fun onCancelled(error: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase", "loadPost:onCancelled", error.toException())
            }
        })

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LandingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LandingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun displayListFragment() {
        parentFragmentManager.commit {
            replace<ListFragment>(R.id.graphFragmentContainerView)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun displayGraphFragment() {
        parentFragmentManager.commit {
            replace<GraphFragment>(R.id.graphFragmentContainerView)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}