package ca.sfu.iat.fintrack.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.activities.AddBudgetActivity
import ca.sfu.iat.fintrack.model.Record
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LandingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tabLayout: TabLayout
    private val database = Firebase.database.reference
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private var filterOptions: ArrayList<String> = ArrayList()

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
        val currentUser = auth.currentUser
        val uid = currentUser?.uid

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        val budgets = hashMapOf<String, String>()
        val budgetNames = mutableListOf("+ Add Budget")
        val spinnerBudget: Spinner = view.findViewById(R.id.spinnerBudget)
        val spinnerPeriod: Spinner = view.findViewById(R.id.spinnerPeriod)

        if (uid != null) {
            database.child("users").child(uid).child("budgets").addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (each in snapshot.children) {
                            val budgetId = each.key.toString()
                            Log.w("FirebaseAuth.getInstance.budgets", budgetId)
                            val budgetName = each.child("name").getValue<String>()
                            if (budgetName != null) {
                                Log.w("FirebaseAuth.getInstance.budgets", budgetName)
                                budgets[budgetName] = budgetId
                            }
                        }
                        setupSpinner(budgets, spinnerBudget, budgetNames)
                        updateTotalsTextView(uid, spinnerBudget)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(
                            "FirebaseAuth.getInstance.budgets.onCancelled",
                            "error",
                            error.toException()
                        )
                    }
                }
            )
        }

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

        spinnerBudget.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 0) {
                    val intent = Intent(activity, AddBudgetActivity::class.java)
                    activity?.startActivity(intent)
                    spinnerBudget.setSelection(1)
                } else {
                    when (tabLayout.selectedTabPosition) {
                        0 -> displayGraphFragment()
                        1 -> displayListFragment()
                    }
                    if (uid != null) {
                        updateTotalsTextView(uid, spinnerBudget)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        when (tab?.position) {
                            0 -> displayGraphFragment()
                            1 -> displayListFragment()
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }

                })
            }
        }

        spinnerPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                displayGraphFragment()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                displayGraphFragment()
            }
        }

        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tabLayout.selectedTabPosition) {
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
        val username =
            database.child("users/$uid/firstName").get().addOnSuccessListener {
                welcomeUserTextView.text = String.format(getString(R.string.welcome_user), it.value)
            }.addOnFailureListener {
                welcomeUserTextView.text = String.format(getString(R.string.welcome_user), "USER")
            }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LandingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun updateTotalsTextView(uid: String, spinnerBudget: Spinner) {
        FirebaseAuth.getInstance().currentUser?.let { it1 ->
            database.child("users/${it1.uid}/budgets")
                .orderByChild("name")
                .equalTo(spinnerBudget.selectedItem.toString())
                .addValueEventListener(
                    object : ValueEventListener {
                        var budgetId: String? = null
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (each in snapshot.children) {
                                budgetId = each.key.toString()
                                println("BUDGETID: ${budgetId}")
                            }

                            val recordsQuery =
                                database.child("users/$uid/budgets/$budgetId/records")
                            updateTotals(recordsQuery)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.w(
                                "FirebaseAuth.getInstance.budgets.onCancelled",
                                "error",
                                error.toException()
                            )
                        }
                    }
                )
        }
    }

    private fun updateTotals(dbRef: DatabaseReference) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var balance = 0.0
                var expense = 0.0
                var income = 0.0
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

                val balanceTextView = view?.findViewById<TextView>(R.id.textViewBalance)
                val expenseTextView = view?.findViewById<TextView>(R.id.textViewExpense)
                val incomeTextView = view?.findViewById<TextView>(R.id.textViewIncome)
                val str1 = "$ " + String.format("%.2f", balance) + "\nBalance"
                val str2 = "$ " + String.format("%.2f", expense) + "\nExpense"
                val str3 = "$ " + String.format("%.2f", income) + "\nIncome"
                balanceTextView?.text = str1
                expenseTextView?.text = str2
                incomeTextView?.text = str3
            }

            override fun onCancelled(error: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("Firebase", "loadPost:onCancelled", error.toException())
            }
        })
    }

    private fun displayListFragment() {
        val spinnerPeriod = view?.findViewById<Spinner>(R.id.spinnerPeriod)
        if (spinnerPeriod != null) {
            spinnerPeriod.visibility = View.GONE
        }
        parentFragmentManager.commit {
            val listStr = getFilterOptions()
            if (!listStr.contains("null")) {
                val listFragment = ListFragment.newInstance(listStr[0], listStr[1])
                replace(R.id.graphFragmentContainerView, listFragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    private fun displayGraphFragment() {
        val spinnerPeriod = view?.findViewById<Spinner>(R.id.spinnerPeriod)
        if (spinnerPeriod != null) {
            spinnerPeriod.visibility = View.VISIBLE
        }
        parentFragmentManager.commit {
            val listStr = getFilterOptions()
            if (!listStr.contains("null")) {
                val graphFragment = GraphFragment.newInstance(listStr[0], listStr[1])
                replace(R.id.graphFragmentContainerView, graphFragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

    private fun getFilterOptions(): ArrayList<String> {
        val periodFilter = view?.findViewById<Spinner>(R.id.spinnerPeriod)?.selectedItem.toString()
        val budgetFilter = view?.findViewById<Spinner>(R.id.spinnerBudget)?.selectedItem.toString()
        filterOptions.clear()
        filterOptions.add(periodFilter)
        filterOptions.add(budgetFilter)
        return filterOptions
    }

    private fun setupSpinner(
        budgets: HashMap<String, String>,
        spinnerBudget: Spinner,
        budgetList: MutableList<String>
    ) {

        for (key in budgets.keys.toList()) {
            if (!budgetList.contains(key)) {
                budgetList.add(key)
            }
        }

        context?.let {
            val spinnerArrayAdapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_item, budgetList)
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerBudget.adapter = spinnerArrayAdapter
        }

        spinnerBudget.setSelection(1)
    }
}