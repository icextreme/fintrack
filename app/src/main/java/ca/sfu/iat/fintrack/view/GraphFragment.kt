package ca.sfu.iat.fintrack.view

import ca.sfu.iat.fintrack.Score
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.sfu.iat.fintrack.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


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
    private lateinit var barChart : BarChart
    private var scoreList = ArrayList<Score>()
    val entries: ArrayList<BarEntry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_graph, container, false)
//        val bundle = arguments
//        val choice = bundle!!.getString("graphType")
//        Log.i("s", choice.toString())
        barChart = view.findViewById(R.id.bar)
        scoreList = getScoreList()
        initBarChart()
        loadBarChart(scoreList)

        return view
    }

    private fun loadBarChart(data: ArrayList<Score>) {
        for (i in data.indices) {
            val score = data[i]
            entries.add(BarEntry(i.toFloat(), score.score.toFloat()))
        }
        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.JOYFUL_COLORS)

        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.invalidate()
    }

    private fun initBarChart() {
//        hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        barChart.axisRight.isEnabled = false

        //remove legend
        barChart.legend.isEnabled = false

        //remove description label
        barChart.description.isEnabled = false

        //add animation
        barChart.animateY(3000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

    }

    /**
     * simulate API Call
     */
    private fun getScoreList(): ArrayList<Score> {
        scoreList.add(Score("January", 300.0, "Winter"))
        scoreList.add(Score("February", 250.0,"Winter"))
        scoreList.add(Score("March", 500.0,"Spring"))
        scoreList.add(Score("April", 50.0,"Spring"))
        scoreList.add(Score("May", 100.0,"Spring"))
        scoreList.add(Score("June", 100.0,"Summer"))
        scoreList.add(Score("July", 100.0, "Summer"))
        scoreList.add(Score("August", 100.0, "Summer"))
        scoreList.add(Score("September", 100.0,"Fall"))
        scoreList.add(Score("October", 100.0,"Fall"))
        scoreList.add(Score("November", 100.0,"Fall"))
        scoreList.add(Score("December", 100.0,"Winter"))

        return scoreList
    }

    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d(ContentValues.TAG, "getAxisLabel: index $index")
            return if (index < scoreList.size) {
                scoreList[index].name
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
            GraphFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}