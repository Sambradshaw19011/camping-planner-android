package com.example.campingplanner

import java.util.UUID

data class TripDetails(
    val name: String = "",
    val destination: String = "",
    val startDate: String = "",
    val endDate: String = "",
)

data class PackingItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isPacked: Boolean = false,
)

data class PlannerData(
    val trip: TripDetails = TripDetails(),
    val items: List<PackingItem> = emptyList(),
)

fun packingProgress(items: List<PackingItem>): Float {
    if (items.isEmpty()) return 0f
    return items.count(PackingItem::isPacked).toFloat() / items.size
}
