package it.nicolasfarabegoli.pulverization.interop

import it.nicolasfarabegoli.pulverization.OnLowBattery
import it.nicolasfarabegoli.pulverization.configureRuntime
import it.nicolasfarabegoli.pulverization.runtime.PulverizationRuntime
import it.unibo.alchemist.model.implementations.properties.ProtelisDevice
import it.unibo.alchemist.model.interfaces.Node.Companion.asProperty
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.WeakHashMap

object ProtelisInterop {
    private val scope = CoroutineScope(Dispatchers.Unconfined)
    private val initialized: WeakHashMap<AlchemistExecutionContext<*>, Any> = WeakHashMap()

    @JvmStatic
    fun AlchemistExecutionContext<*>.onBatteryChangeEvent() {
        val connector = (deviceUID as ProtelisDevice<*>).node.asProperty<Any, OnLowBattery>()
        runBlocking {
            connector.updateBattery(executionEnvironment.get("currentCapacity") as Double)
            connector.results.first()
        }
    }

    @JvmStatic
    fun AlchemistExecutionContext<*>.startPulverization() {
        if (this !in initialized) {
            initialized[this] = true
            val deviceID = (deviceUID as ProtelisDevice<*>)
            val reconfigurationEvent = deviceID.node.asProperty<Any, OnLowBattery>()
            scope.launch {
                val config = configureRuntime(reconfigurationEvent)
                val runtime = PulverizationRuntime(deviceID.id.toString(), "smartphone", config)
                runtime.start()
            }
        }
    }
}
