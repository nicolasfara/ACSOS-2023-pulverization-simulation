package it.nicolasfarabegoli.pulverization.interop

import it.nicolasfarabegoli.pulverization.ReconfigurableDevice
import it.nicolasfarabegoli.pulverization.configureRuntime
import it.nicolasfarabegoli.pulverization.runtime.PulverizationRuntime
import it.unibo.alchemist.model.implementations.properties.ProtelisDevice
import it.unibo.alchemist.model.interfaces.Node.Companion.asProperty
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import kotlinx.coroutines.runBlocking

object ProtelisInterop {
    @JvmStatic
    fun AlchemistExecutionContext<*>.onBatteryChangeEvent() {
        val connector = (deviceUID as ProtelisDevice<*>).node.asProperty<Any, ReconfigurableDevice>()
        runBlocking {
            connector.flow.emit(executionEnvironment.get("battery") as Double)
        }
    }

    @JvmStatic
    fun AlchemistExecutionContext<*>.startPulverization() {
        val deviceID = (deviceUID as ProtelisDevice<*>)
        val r = deviceID.node.asProperty<Any, ReconfigurableDevice>()
        runBlocking {
            val config = configureRuntime(r)
            val runtime = PulverizationRuntime(deviceID.id.toString(), "smartphone", config)
            runtime.start()
        }
    }
}
