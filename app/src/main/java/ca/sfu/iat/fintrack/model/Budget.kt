package ca.sfu.iat.fintrack.model

data class Budget(val name: String) {
    val spendingList = ArrayList<Record>()
}
