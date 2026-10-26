package com.example.campingplanner

import org.junit.Assert.assertEquals
import org.junit.Test

class PlannerDataTest {
    @Test
    fun progressIsZeroForAnEmptyList() {
        assertEquals(0f, packingProgress(emptyList()), 0f)
    }

    @Test
    fun progressReflectsTheFractionOfPackedItems() {
        val items = listOf(
            PackingItem(name = "Tent", isPacked = true),
            PackingItem(name = "Sleeping bag", isPacked = false),
            PackingItem(name = "Water", isPacked = true),
        )

        assertEquals(2f / 3f, packingProgress(items), 0.0001f)
    }
}
