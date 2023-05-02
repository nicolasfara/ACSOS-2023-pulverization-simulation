package it.nicolasfarabegoli.pulverization

import it.nicolasfarabegoli.pulverization.runtime.dsl.model.ReconfigurationEvent
import it.unibo.alchemist.model.interfaces.Node
import it.unibo.alchemist.model.interfaces.NodeProperty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

data class ReconfigurableDevice(override val node: Node<Any>) : ReconfigurationEvent<Double>, NodeProperty<Any> {
    val flow = MutableSharedFlow<Double>()

    override val events: Flow<Double> = flow
    override val predicate: (Double) -> Boolean = { it < 25.0 }

    override fun toString() = "ReconfigurableDevice[${node.id}]"

    override fun cloneOnNewNode(node: Node<Any>): NodeProperty<Any> = TODO("Not yet implemented")
}
