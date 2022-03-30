package ca.sfu.iat.fintrack.model

import ca.sfu.iat.fintrack.FirebaseHandler
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

@IgnoreExtraProperties
data class Record(
    val recordName: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val type: String = "",
    var date: String = "01/01/1970"
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "recordName" to recordName,
            "amount" to amount,
            "category" to category,
            "type" to type,
            "date" to date
        )
    }
}

/**
 * simulate API Call
 */
fun getScoreList(): ArrayList<Record> {
    val scoreList = ArrayList<Record>()
    scoreList.add(Record("January", 300.0, "Winter", Type.EXPENSE.toString()))
    scoreList.add(Record("February", 250.0,"Winter",Type.EXPENSE.toString()))
    scoreList.add(Record("March", 500.0,"Spring",Type.EXPENSE.toString()))
    scoreList.add(Record("April", 50.0,"Spring",Type.EXPENSE.toString()))
    scoreList.add(Record("May", 100.0,"Spring",Type.EXPENSE.toString()))
    scoreList.add(Record("June", 100.0,"Summer",Type.EXPENSE.toString()))
    scoreList.add(Record("July", 100.0, "Summer",Type.EXPENSE.toString()))
    scoreList.add(Record("August", 100.0, "Summer",Type.EXPENSE.toString()))
    scoreList.add(Record("September", 100.0,"Fall",Type.EXPENSE.toString()))
    scoreList.add(Record("October", 100.0,"Fall",Type.EXPENSE.toString()))
    scoreList.add(Record("November", 100.0,"Fall",Type.EXPENSE.toString()))
    scoreList.add(Record("December", 100.0,"Winter",Type.EXPENSE.toString()))

    return scoreList
}