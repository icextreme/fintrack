package ca.sfu.iat.fintrack

import android.content.Context

class Datasource(val context: Context) {
    fun getDataList(): Array<String> {
        return context.resources.getStringArray(R.array.flower_array)
    }
}
