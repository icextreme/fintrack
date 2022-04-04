package ca.sfu.iat.fintrack.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.commit
import ca.sfu.iat.fintrack.R

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GraphFragment : Fragment() {
    private var period: String? = null
    private var budget: String? = null
    private lateinit var pieButton: Button
    private lateinit var barButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            displayBarFragment()
        }
        arguments?.let {
            period = it.getString(ARG_PARAM1)
            budget = it.getString(ARG_PARAM2)
            println("$period, $budget REZ GRAPH FRAG")
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
        val view = inflater.inflate(R.layout.fragment_graph, container, false)

        // Inflate the layout for this fragment
        barButton = view.findViewById(R.id.buttonBar)
        pieButton = view.findViewById(R.id.buttonPie)
        barButton.setOnClickListener {
            if (savedInstanceState == null) {
                displayBarFragment()
            }
        }
        pieButton.setOnClickListener {
            if (savedInstanceState == null) {
                displayPieFragment()
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
         * @return A new instance of fragment GraphFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GraphFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun displayBarFragment() {
        parentFragmentManager.commit {
            if (period != null && budget != null) {
                val barFragment = BarFragment.newInstance(period.toString(), budget.toString())
                replace(R.id.GraphFragmentContainer, barFragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }
    private fun displayPieFragment() {
        parentFragmentManager.commit {
            if (period != null && budget != null) {
                val pieFragment = PieFragment.newInstance(period.toString(), budget.toString())
                replace(R.id.GraphFragmentContainer, pieFragment)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }
}