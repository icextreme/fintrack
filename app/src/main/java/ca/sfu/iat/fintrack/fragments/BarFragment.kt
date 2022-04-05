package ca.sfu.iat.fintrack.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.model.Record
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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


class BarFragment : Fragment() {
    private var period: String? = null
    private var budget: String? = null
    private lateinit var barChart: BarChart
    private var filteredList = HashMap<String, Double>()
    private val entries: ArrayList<BarEntry> = ArrayList()

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
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_bar, container, false)

        barChart = view.findViewById(R.id.bar)
        val database = Firebase.database.reference
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        database.child("users/$uid/budgets")
            .orderByChild("name")
            .equalTo("$budget").addListenerForSingleValueEvent(object : ValueEventListener {
                var key: String? = null
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        key = dataSnapshot.key.toString()
                    }

                    val recordsQuery = database.child("users/$uid/budgets/$key/records")
                    recordsQuery.addValueEventListener(object : ValueEventListener {
                        val recordsList = ArrayList<Record>()
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot in snapshot.children) {
                                val record: Record? = dataSnapshot.getValue<Record>()
                                if (record != null && record.type == getString(R.string.expense)) {
                                    recordsList.add(record)
                                }
                            }
                            initBarChart()
                            loadBarChart(recordsList)
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
        return view
    }

    private fun loadBarChart(data: ArrayList<Record>) {
        val filteredValue = filterBarChart(data)
        filteredList = filteredValue as HashMap<String, Double>
        var count = 0.0
        for (i in filteredValue.entries) {
            entries.add(BarEntry(count.toFloat(), i.value.toFloat()))
            count += 1
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.JOYFUL_COLORS)
        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.invalidate()

    }

    private fun filterBarChart(data: ArrayList<Record>): Map<String, Double> {
        var filteredValue = HashMap<String, Double>() as Map<String, Double>
        if (period.equals(getString(R.string.yearly))) {
            filteredValue = data.groupBy {
                it.date.split("/")[2]
            }.mapValues { (_, score) ->
                score.sumOf { it.amount }
            }
        }

        if (period.equals(getString(R.string.monthly))) {
            filteredValue = data.groupBy {
                it.date.split("/")[0]
            }.mapValues { (_, score) ->
                score.sumOf { it.amount }
            }
        }
        return filteredValue
    }

    private fun initBarChart() {
        // hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        // remove right y-axis
        barChart.axisRight.isEnabled = false

        // remove legend
        barChart.legend.isEnabled = false

        // remove description label
        barChart.description.isEnabled = false

        // add animation
        barChart.animateY(3000)

        // remove zoom
        barChart.setTouchEnabled(false)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            val map1 = mapOf(
                "01" to "Jan", "02" to "Feb", "03" to "Mar", "04" to "Apr",
                "05" to "May", "06" to "Jun", "07" to "Jul", "08" to "Aug", "09" to "Sep",
                "10" to "Oct", "11" to "Nov", "12" to "Dec"
            )
            return if (index < filteredList.entries.size) {
                if (filteredList.keys.toTypedArray()[index] in map1) {
                    map1.getValue(filteredList.keys.toTypedArray()[index])
                } else {
                    filteredList.keys.toTypedArray()[index]
                }
            } else {
                ""
            }
        }
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
            BarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}