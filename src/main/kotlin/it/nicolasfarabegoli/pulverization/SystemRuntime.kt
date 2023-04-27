package it.nicolasfarabegoli.pulverization

import it.nicolasfarabegoli.pulverization.components.DeviceBehaviour
import it.nicolasfarabegoli.pulverization.components.DeviceCommunication
import it.nicolasfarabegoli.pulverization.components.DeviceSensorsContainer
import it.nicolasfarabegoli.pulverization.components.deviceCommunicationLogic
import it.nicolasfarabegoli.pulverization.components.deviceSmartphoneBehaviour
import it.nicolasfarabegoli.pulverization.components.deviceSensorsLogic
import it.nicolasfarabegoli.pulverization.dsl.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.model.Capability
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.DeploymentUnitRuntimeConfiguration
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.pulverizationRuntime

object Server : Host {
    override val hostname: String = "cloud"
    override val capabilities: Set<Capability> = setOf(HighCPU)
}

object Smartphone : Host {
    override val hostname: String = "smartphone"
    override val capabilities: Set<Capability> = setOf(SmartphoneDevice)
}

val hosts = setOf(Server, Smartphone)

suspend fun configureRuntime(
    deviceName: String,
): DeploymentUnitRuntimeConfiguration<Unit, Int, Int, Unit, Unit> {
    return pulverizationRuntime(systemConfiguration, deviceName, hosts) {
        DeviceBehaviour() withLogic ::deviceSmartphoneBehaviour startsOn Smartphone
        DeviceCommunication() withLogic ::deviceCommunicationLogic startsOn Server
        DeviceSensorsContainer() withLogic ::deviceSensorsLogic startsOn Smartphone

        reconfigurationRules {
            onDevice {
                OnLowBattery reconfigures { Behaviour movesTo Server }
            }
        }
    }
}
