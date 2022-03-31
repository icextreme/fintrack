package ca.sfu.iat.fintrack.model

data class Budget(val name: String) {
    val balance: Double = 0.0
    val expense: Double = 0.0
    val income: Double = 0.0
    val spendingList = ArrayList<Record>()
}
