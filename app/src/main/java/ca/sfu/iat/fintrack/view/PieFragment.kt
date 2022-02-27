package ca.sfu.iat.fintrack.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.Score
import android.content.ContentValues
import android.graphics.Color
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
 * Use the [PieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PieFragment : Fragment() {
    private lateinit var pieChart : PieChart
    private var scoreList = ArrayList<Score>()
    val pieEntries: ArrayList<PieEntry> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pie, container, false)
        pieChart = view.findViewById(R.id.pie)
        scoreList = getScoreList()
        println(scoreList)
        initPieChart()
        loadPieChart(scoreList)
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

    private fun loadPieChart(data: ArrayList<Score>) {
        val scorePerSeason = data.groupBy { it.season }
            .mapValues { (_, score) ->
                score.sumOf { it.score }
            }
        for (k in scorePerSeason) {
            println(k.value.toString() + k.key)
            pieEntries.add(PieEntry(k.value.toFloat(), k.key))
        }
        val pieDataSet = PieDataSet(pieEntries, "Seasonal")
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
        pieChart.centerText = "Spending by Season"
        pieChart.setCenterTextSize(24f)
        pieChart.description.isEnabled = false
        val l = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = true
        pieChart.animateY(1400, Easing.EaseInOutQuad)
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


}