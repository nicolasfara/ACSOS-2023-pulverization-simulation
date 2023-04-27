package it.nicolasfarabegoli.pulverization.components

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.Sensor
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.runtime.componentsref.BehaviourRef
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.milliseconds

class SensorDevice : Sensor<Int> {
    override suspend fun sense(): Int {
        TODO("Not yet implemented")
    }
}

class DeviceSensorsContainer : SensorsContainer() {
    override val context: Context by inject()

    override suspend fun initialize() {
        this += SensorDevice()
    }
}

suspend fun deviceSensorsLogic(
    sensors: SensorsContainer,
    behaviourRef: BehaviourRef<Int>
) = coroutineScope {
    sensors.get<SensorDevice> {
        while (true) {
            behaviourRef.sendToComponent(sense())
            delay(100.milliseconds)
        }
    }
}
