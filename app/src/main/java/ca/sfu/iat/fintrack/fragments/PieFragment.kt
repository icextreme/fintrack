package ca.sfu.iat.fintrack.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ca.sfu.iat.fintrack.R
import ca.sfu.iat.fintrack.model.Record
import android.graphics.Color
import ca.sfu.iat.fintrack.model.getScoreList
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
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
    private var scoreList = ArrayList<Record>()
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

    private fun loadPieChart(data: ArrayList<Record>) {
        val scorePerSeason = data.groupBy { it.category }
            .mapValues { (_, score) ->
                score.sumOf { it.amount }
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


}