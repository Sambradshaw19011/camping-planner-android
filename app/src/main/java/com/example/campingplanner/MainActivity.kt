package com.example.campingplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Forest = Color(0xFF345B40)
private val DeepForest = Color(0xFF263F30)
private val Cream = Color(0xFFF5F3EC)
private val Muted = Color(0xFF6E766D)
private val WarmWhite = Color(0xFFFFFEFA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = PlannerStore(applicationContext)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Forest,
                    onPrimary = Color.White,
                    background = Cream,
                    surface = WarmWhite,
                    onSurface = DeepForest,
                    secondary = Color(0xFFB77C4D),
                ),
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
                    PlannerScreen(store)
                }
            }
        }
    }
}

@Composable
private fun PlannerScreen(store: PlannerStore) {
    var planner by remember { mutableStateOf(store.load()) }
    var itemName by remember { mutableStateOf("") }
    var editingTrip by remember { mutableStateOf(false) }
    var tripDraft by remember { mutableStateOf(planner.trip) }
    var message by remember { mutableStateOf<String?>(null) }

    fun updatePlanner(updated: PlannerData) {
        planner = updated
        store.save(updated)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 28.dp, bottom = 32.dp),
    ) {
        Text(
            text = "CAMPING PLANNER",
            color = Forest,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
        )
        Spacer(Modifier.height(16.dp))
        TripCard(
            trip = planner.trip,
            onEdit = {
                tripDraft = planner.trip
                editingTrip = true
            },
        )
        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text("Packing list", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(3.dp))
                Text("Get ready for the great outdoors.", color = Muted, fontSize = 14.sp)
            }
            Text(
                "${planner.items.count(PackingItem::isPacked)} / ${planner.items.size}",
                color = Forest,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
        Spacer(Modifier.height(14.dp))
        androidx.compose.material3.LinearProgressIndicator(
            progress = { packingProgress(planner.items) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Forest,
            trackColor = Color(0xFFE1E4DA),
        )
        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = itemName,
                onValueChange = {
                    itemName = it
                    message = null
                },
                modifier = Modifier.weight(1f),
                label = { Text("Add an item") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )
            Spacer(Modifier.width(10.dp))
            Button(
                onClick = {
                    val cleanName = itemName.trim()
                    if (cleanName.isEmpty()) {
                        message = "Enter an item name first."
                    } else {
                        updatePlanner(
                            planner.copy(items = planner.items + PackingItem(name = cleanName)),
                        )
                        itemName = ""
                        message = "Added to your packing list."
                    }
                },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text("Add")
            }
        }
        message?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Forest, fontSize = 13.sp)
        }
        Spacer(Modifier.height(12.dp))

        if (planner.items.isEmpty()) {
            EmptyPackingList()
        } else {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = WarmWhite),
            ) {
                planner.items.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = item.isPacked,
                            modifier = Modifier.semantics {
                                contentDescription = item.name
                            },
                            onCheckedChange = { checked ->
                                updatePlanner(
                                    planner.copy(
                                        items = planner.items.map { current ->
                                            if (current.id == item.id) {
                                                current.copy(isPacked = checked)
                                            } else {
                                                current
                                            }
                                        },
                                    ),
                                )
                            },
                        )
                        Text(
                            text = item.name,
                            modifier = Modifier.weight(1f),
                            color = if (item.isPacked) Muted else DeepForest,
                            textDecoration = if (item.isPacked) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            },
                            fontSize = 15.sp,
                        )
                        TextButton(
                            modifier = Modifier.semantics {
                                contentDescription = "Remove ${item.name}"
                            },
                            onClick = {
                                updatePlanner(
                                    planner.copy(items = planner.items.filterNot { it.id == item.id }),
                                )
                                message = "Item removed."
                            },
                        ) {
                            Text("Remove", color = Muted, fontSize = 12.sp)
                        }
                    }
                    if (index < planner.items.lastIndex) {
                        Divider(
                            modifier = Modifier.padding(start = 58.dp, end = 16.dp),
                            color = Color(0xFFECEBE4),
                        )
                    }
                }
            }
        }
    }

    if (editingTrip) {
        TripEditorDialog(
            trip = tripDraft,
            onTripChange = { tripDraft = it },
            onDismiss = { editingTrip = false },
            onSave = {
                updatePlanner(planner.copy(trip = tripDraft))
                editingTrip = false
                message = "Trip details saved."
            },
        )
    }
}

@Composable
private fun TripCard(trip: TripDetails, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Forest),
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (trip.name.isBlank()) "Your next adventure" else trip.name,
                        color = Color.White,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = trip.destination.ifBlank { "A little fresh air is calling" },
                        color = Color(0xFFE0E9DB),
                        fontSize = 14.sp,
                    )
                }
                Spacer(Modifier.width(8.dp))
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF496F53),
                    modifier = Modifier.size(54.dp),
                ) {
                    BoxCenter {
                        Text("🏕️", fontSize = 28.sp)
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            Text(
                text = tripDateLabel(trip),
                color = Color(0xFFE0E9DB),
                fontSize = 13.sp,
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onEdit,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF54775B),
                    contentColor = Color.White,
                ),
            ) {
                Text(if (trip.name.isBlank()) "Plan your trip" else "Edit trip")
            }
        }
    }
}

@Composable
private fun BoxCenter(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

@Composable
private fun EmptyPackingList() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("🌲", fontSize = 30.sp)
            Spacer(Modifier.height(10.dp))
            Text("Your list is ready to grow", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text("Add the first thing you want to pack.", color = Muted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun TripEditorDialog(
    trip: TripDetails,
    onTripChange: (TripDetails) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Plan your trip") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedTextField(
                    value = trip.name,
                    onValueChange = { onTripChange(trip.copy(name = it)) },
                    label = { Text("Trip name") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = trip.destination,
                    onValueChange = { onTripChange(trip.copy(destination = it)) },
                    label = { Text("Destination (optional)") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = trip.startDate,
                    onValueChange = { onTripChange(trip.copy(startDate = it)) },
                    label = { Text("Start date (optional)") },
                    placeholder = { Text("e.g. Jun 12") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = trip.endDate,
                    onValueChange = { onTripChange(trip.copy(endDate = it)) },
                    label = { Text("End date (optional)") },
                    placeholder = { Text("e.g. Jun 15") },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

private fun tripDateLabel(trip: TripDetails): String {
    val start = trip.startDate.trim()
    val end = trip.endDate.trim()
    return when {
        start.isNotEmpty() && end.isNotEmpty() -> "$start  –  $end"
        start.isNotEmpty() -> "Starting $start"
        end.isNotEmpty() -> "Until $end"
        else -> "Your trip dates"
    }
}
