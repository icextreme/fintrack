package ca.sfu.iat.fintrack.model

import java.time.LocalDate

data class Record(
    val recordName: String,
    val amount: Double,
    val category: String,
    val type: Type
) {
    val dateAdded: LocalDate = LocalDate.now()
}

/**
 * simulate API Call
 */
fun getScoreList(): ArrayList<Record> {
    val scoreList = ArrayList<Record>()
    scoreList.add(Record("January", 300.0, "Winter", Type.EXPENSE))
    scoreList.add(Record("February", 250.0,"Winter",Type.EXPENSE))
    scoreList.add(Record("March", 500.0,"Spring",Type.EXPENSE))
    scoreList.add(Record("April", 50.0,"Spring",Type.EXPENSE))
    scoreList.add(Record("May", 100.0,"Spring",Type.EXPENSE))
    scoreList.add(Record("June", 100.0,"Summer",Type.EXPENSE))
    scoreList.add(Record("July", 100.0, "Summer",Type.EXPENSE))
    scoreList.add(Record("August", 100.0, "Summer",Type.EXPENSE))
    scoreList.add(Record("September", 100.0,"Fall",Type.EXPENSE))
    scoreList.add(Record("October", 100.0,"Fall",Type.EXPENSE))
    scoreList.add(Record("November", 100.0,"Fall",Type.EXPENSE))
    scoreList.add(Record("December", 100.0,"Winter",Type.EXPENSE))

    return scoreList
}