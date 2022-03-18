package ca.sfu.iat.fintrack.model

import android.content.Context
import ca.sfu.iat.fintrack.R

class Datasource(val context: Context) {
    fun getDataList(): Array<String> {
        return context.resources.getStringArray(R.array.flower_array)
    }
}
