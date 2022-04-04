package ca.sfu.iat.fintrack.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.model.Record
import android.graphics.Color
import android.util.Log
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PieFragment : Fragment() {
    private var period: String? = null
    private var budget: String? = null
    private lateinit var pieChart : PieChart
    private var scoreList = ArrayList<Record>()
    private val pieEntries: ArrayList<PieEntry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            period = it.getString(ARG_PARAM1)
            budget = it.getString(ARG_PARAM2)
            println("$period, $budget REZ PIE FRAG")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pie, container, false)
        pieChart = view.findViewById(R.id.pie)
        val database = Firebase.database.reference
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val recordsQuery = database.child("users/$uid/records")
        recordsQuery.addValueEventListener(object: ValueEventListener {
            val recordsList = ArrayList<Record>()
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val record: Record? = dataSnapshot.getValue<Record>()
                    if (record != null) {
                        recordsList.add(record)
                    }
                }
                scoreList = recordsList
                initPieChart()
                loadPieChart(scoreList)
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
         * @return A new instance of fragment PieFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PieFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun loadPieChart(data: ArrayList<Record>) {
        val scorePerSeason = data.groupBy { it.category }
            .mapValues { (_, score) ->
                score.sumOf { it.amount }
            }
        for (k in scorePerSeason) {
            println(k.value.toString() + k.key)
            pieEntries.add(PieEntry(k.value.toFloat(), k.key))
        }
        val pieDataSet = PieDataSet(pieEntries, "Spend Category")
        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.invalidate()
    }

    private fun initPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText = "Spending by Category"
        pieChart.setCenterTextSize(24f)
        pieChart.description.isEnabled = false
        pieChart.setTouchEnabled(false)
        val l = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = true
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }
}