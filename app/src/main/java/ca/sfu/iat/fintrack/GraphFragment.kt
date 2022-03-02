package ca.sfu.iat.fintrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.commit
import androidx.fragment.app.replace

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GraphFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var pieButton: Button
    private lateinit var barButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        // TODO: Rename and change types and number of parameters
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
            replace<BarFragment>(R.id.GraphFragmentContainer)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
    private fun displayPieFragment() {
        parentFragmentManager.commit {
            replace<PieFragment>(R.id.GraphFragmentContainer)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}