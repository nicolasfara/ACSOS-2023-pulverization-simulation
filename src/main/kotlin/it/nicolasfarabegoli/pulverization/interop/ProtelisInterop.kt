package it.nicolasfarabegoli.pulverization.interop

import it.nicolasfarabegoli.pulverization.OnLowBattery
import it.nicolasfarabegoli.pulverization.configureRuntime
import it.nicolasfarabegoli.pulverization.runtime.PulverizationRuntime
import it.unibo.alchemist.model.implementations.properties.ProtelisDevice
import it.unibo.alchemist.model.interfaces.Node.Companion.asProperty
import it.unibo.alchemist.protelis.AlchemistExecutionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

object ProtelisInterop {
    private val scope = CoroutineScope(Dispatchers.Unconfined)

    @JvmStatic
    fun AlchemistExecutionContext<*>.onBatteryChangeEvent() {
        val connector = (deviceUID as ProtelisDevice<*>).node.asProperty<Any, OnLowBattery>()
        runBlocking {
            connector.flow.emit(executionEnvironment.get("battery") as Double)
        }
    }

    @JvmStatic
    fun AlchemistExecutionContext<*>.startPulverization() {
        val deviceID = (deviceUID as ProtelisDevice<*>)
        val reconfigurationEvent = deviceID.node.asProperty<Any, OnLowBattery>()
        scope.launch {
            val config = configureRuntime(reconfigurationEvent)
            val runtime = PulverizationRuntime(deviceID.id.toString(), "smartphone", config)
            runtime.start()
            while (true) { delay(100.milliseconds) }
        }
    }
}
