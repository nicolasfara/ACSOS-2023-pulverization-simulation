package it.nicolasfarabegoli.pulverization

import it.nicolasfarabegoli.pulverization.runtime.dsl.model.ReconfigurationEvent
import kotlinx.coroutines.flow.Flow

object OnLowBattery : ReconfigurationEvent<Double> {
    override val events: Flow<Double>
        get() = TODO("Not yet implemented")
    override val predicate: (Double) -> Boolean = { it < 25.0 }
}
