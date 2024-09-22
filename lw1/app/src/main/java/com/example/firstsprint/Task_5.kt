enum class FrameMaterial {
    ALUMINUM,
    STEEL,
    CARBON_FIBER
}

enum class FuelType {
    DIESEL,
    GASOLINE_92,
    GASOLINE_95,
    GASOLINE_98,
    GASOLINE_100
}

enum class CarWheelType {
    ALLOY,
    FORGED,
    STAMPED
}

enum class AutopilotType {
    YANDEX,
    TESLA
}

sealed interface Vehicle

data object Scooter : Vehicle

data class Bicycle(
    val brand: String,
    val frontWheel: BicycleWheel,
    val rearWheel: BicycleWheel,
    val frame: FrameMaterial
) : Vehicle

data class BicycleWheel(
    val diameter: Double
)

sealed interface Car : Vehicle {
    val wheels: List<CarWheel>
}

data class Engine(
    val volume: Double = 60.0,
    val fuelType: FuelType = FuelType.GASOLINE_95,
)

data class GasolineCar(
    val engine: Engine = Engine(),
    val steeringWheel: Boolean = true,
    override val wheels: List<CarWheel> = List(4) { CarWheel() }
) : Car

data class ElectricCar(
    val electricMotorPower: Double = 150.0,
    val autopilot: AutopilotType = AutopilotType.YANDEX,
    override val wheels: List<CarWheel> = List(4) { CarWheel() },
) : Car

data class CarWheel(
    val diameter: Double = 16.0,
    val brand: String = "Generic",
    val wheelType: CarWheelType = CarWheelType.ALLOY
)

fun main() {
    val scooter = Scooter

    val bicycle = Bicycle(
        brand = "Giant",
        frontWheel = BicycleWheel(diameter = 27.5),
        rearWheel = BicycleWheel(diameter = 27.5),
        frame = FrameMaterial.ALUMINUM
    )

    val gasolineCar = GasolineCar()
    val electricCar = ElectricCar()

    println("Scooter: $scooter")
    println("Bicycle: $bicycle")
    println("Gasoline Car: $gasolineCar")
    println("Electric Car: $electricCar")
}