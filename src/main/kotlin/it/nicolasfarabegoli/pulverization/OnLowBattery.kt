package it.nicolasfarabegoli.pulverization

import it.nicolasfarabegoli.pulverization.runtime.dsl.model.ReconfigurationEvent
import it.unibo.alchemist.model.interfaces.Node
import it.unibo.alchemist.model.interfaces.NodeProperty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class OnLowBattery(override val node: Node<Any>) : ReconfigurationEvent<Double>(), NodeProperty<Any> {
    private val flow = MutableSharedFlow<Double>()
    private val reconfigurationCharge by GetMolecule

    override val events: Flow<Double> = flow.asSharedFlow()
    override val predicate: (Double) -> Boolean = { it < node.getConcentration(reconfigurationCharge) as Double }
    suspend fun updateBattery(newValue: Double) = flow.emit(newValue)

    override fun cloneOnNewNode(node: Node<Any>): NodeProperty<Any> = TODO("Not yet implemented")
}
