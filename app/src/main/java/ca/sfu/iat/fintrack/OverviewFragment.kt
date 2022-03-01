package ca.sfu.iat.fintrack
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.commit
import androidx.fragment.app.replace


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            displayBarFragment()
        }
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        displayBarFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        view.findViewById<Button>(R.id.button3).setOnClickListener {
            if (savedInstanceState == null) {
                displayBarFragment()
            }
        }
        view.findViewById<Button>(R.id.button4).setOnClickListener {
            if (savedInstanceState == null) {
                displayPieFragment()
            }
        }

        val spinnerPeriod: Spinner = view.findViewById(R.id.spinnerPeriod)
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
        val spinnerBudget: Spinner = view.findViewById(R.id.spinnerBudget)
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
    private fun displayBarFragment() {
        parentFragmentManager.commit {
            replace<BarFragment>(R.id.graphFragmentContainerView)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
    private fun displayPieFragment() {
        parentFragmentManager.commit {
            replace<PieFragment>(R.id.graphFragmentContainerView)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}