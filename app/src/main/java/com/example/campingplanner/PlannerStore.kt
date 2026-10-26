package com.example.campingplanner

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.UUID

class PlannerStore(context: Context) {
    private val preferences =
        context.getSharedPreferences("camping_planner", Context.MODE_PRIVATE)

    fun load(): PlannerData {
        val trip = TripDetails(
            name = preferences.getString(KEY_TRIP_NAME, "").orEmpty(),
            destination = preferences.getString(KEY_DESTINATION, "").orEmpty(),
            startDate = preferences.getString(KEY_START_DATE, "").orEmpty(),
            endDate = preferences.getString(KEY_END_DATE, "").orEmpty(),
        )
        val items = try {
            val savedItems = JSONArray(preferences.getString(KEY_ITEMS, "[]"))
            buildList {
                for (index in 0 until savedItems.length()) {
                    val item = savedItems.getJSONObject(index)
                    val name = item.optString("name").trim()
                    if (name.isNotEmpty()) {
                        add(
                            PackingItem(
                                id = item.optString("id").ifBlank { UUID.randomUUID().toString() },
                                name = name,
                                isPacked = item.optBoolean("isPacked"),
                            ),
                        )
                    }
                }
            }
        } catch (exception: JSONException) {
            Log.e(TAG, "Could not read the saved packing list.", exception)
            emptyList()
        }
        return PlannerData(trip = trip, items = items)
    }

    fun save(data: PlannerData) {
        val items = JSONArray().apply {
            data.items.forEach { item ->
                put(
                    JSONObject()
                        .put("id", item.id)
                        .put("name", item.name)
                        .put("isPacked", item.isPacked),
                )
            }
        }
        preferences.edit()
            .putString(KEY_TRIP_NAME, data.trip.name)
            .putString(KEY_DESTINATION, data.trip.destination)
            .putString(KEY_START_DATE, data.trip.startDate)
            .putString(KEY_END_DATE, data.trip.endDate)
            .putString(KEY_ITEMS, items.toString())
            .apply()
    }

    private companion object {
        const val TAG = "PlannerStore"
        const val KEY_TRIP_NAME = "trip_name"
        const val KEY_DESTINATION = "destination"
        const val KEY_START_DATE = "start_date"
        const val KEY_END_DATE = "end_date"
        const val KEY_ITEMS = "packing_items"
    }
}
