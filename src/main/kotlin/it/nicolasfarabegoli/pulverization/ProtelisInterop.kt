package it.nicolasfarabegoli.pulverization

import it.unibo.alchemist.model.implementations.properties.ProtelisDevice
import it.unibo.alchemist.model.interfaces.Node.Companion.asProperty
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import kotlinx.coroutines.runBlocking

object ProtelisInterop {
    @JvmStatic
    fun AlchemistExecutionContext<*>.somethingHappened() {
        val connector = (deviceUID as ProtelisDevice<*>).node.asProperty<Any, ReconfigurableDevice>()
        runBlocking {
            connector.flow.emit(executionEnvironment.get("battery") as Double)
        }
    }
}
