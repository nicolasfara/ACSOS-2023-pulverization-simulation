package it.nicolasfarabegoli.pulverization

import it.nicolasfarabegoli.pulverization.runtime.PulverizationRuntime
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.ReconfigurationEvent
import it.unibo.alchemist.model.interfaces.Node
import it.unibo.alchemist.model.interfaces.NodeProperty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking

data class ReconfigurableDevice(override val node: Node<Any>) : ReconfigurationEvent<Double>, NodeProperty<Any> {
    private val internalFlow = MutableSharedFlow<Double>()
    val flow get() = internalFlow.also {
        if (!::runtime.isInitialized) {
            runBlocking {
                val config = configureRuntime(this@ReconfigurableDevice)
                val runtime = PulverizationRuntime(node.id.toString(), "smartphone", config)
                runtime.start()
            }
        }
    }
    override val events: Flow<Double> = flow
    override val predicate: (Double) -> Boolean = { it < 25.0 }
    private lateinit var runtime: PulverizationRuntime<Unit, Int, Int, Unit, Unit>

    override fun cloneOnNewNode(node: Node<Any>): NodeProperty<Any> = TODO("Not yet implemented")
}
