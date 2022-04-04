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
