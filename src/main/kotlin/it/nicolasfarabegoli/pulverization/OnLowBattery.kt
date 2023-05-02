package it.nicolasfarabegoli.pulverization

import it.nicolasfarabegoli.pulverization.runtime.dsl.model.ReconfigurationEvent
import it.unibo.alchemist.model.interfaces.Node
import it.unibo.alchemist.model.interfaces.NodeProperty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OnLowBattery(override val node: Node<Any>) : ReconfigurationEvent<Double>, NodeProperty<Any> {
    private val flow = MutableStateFlow(100.0)
    override val events: Flow<Double> = flow.asStateFlow()
    override val predicate: (Double) -> Boolean = { it < 25.0 }

    fun updateBattery(newValue: Double) = flow.update { newValue }

    override fun cloneOnNewNode(node: Node<Any>): NodeProperty<Any> = TODO("Not yet implemented")
}
