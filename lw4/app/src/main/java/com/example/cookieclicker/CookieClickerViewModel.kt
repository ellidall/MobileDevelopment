import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookieclicker.ItemBuilding
import com.example.cookieclicker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CookieClickerState(
    val cookieCount: Int = 0,
    val cookiesPerSecond: Int = 0,
    val elapsedTime: Long = 0,
    val buildings: List<ItemBuilding> = emptyList()
)

class CookieClickerViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        CookieClickerState(
            cookieCount = 0,
            cookiesPerSecond = 0,
            elapsedTime = 0,
            buildings = listOf(
                ItemBuilding("Ферма", R.drawable.ic_cookie, price = 20, count = 0, increase = 1),
                ItemBuilding("Станок", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок2", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок3", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок4", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок5", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок6", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок7", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок8", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок9", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок10", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
                ItemBuilding("Станок!!!!!", R.drawable.ic_cookie, price = 50, count = 0, increase = 2),
            )
        )
    )
    val state = _state.asStateFlow()

    private val _toastMessages = MutableSharedFlow<String>()
    val toastMessages = _toastMessages.asSharedFlow()
    private var milestones = 50

    fun clickCookie() {
        _state.value = _state.value.copy(
            cookieCount = _state.value.cookieCount + 1
        )
        updatePurchasableStatus()
        checkToastCondition()
    }

    fun buyBuilding(building: ItemBuilding) {
        val currentState = _state.value
        if (currentState.cookieCount < building.price) {
            viewModelScope.launch {
                _toastMessages.emit("Not enough cookies")
            }
            return
        }

        val updatedBuildings = currentState.buildings.map { currentBuilding ->
            if (currentBuilding.name == building.name) {
                currentBuilding.copy(count = currentBuilding.count + 1)
            } else {
                currentBuilding
            }
        }

        _state.value = currentState.copy(
            cookieCount = currentState.cookieCount - building.price,
            cookiesPerSecond = updatedBuildings.sumOf { it.totalIncrease() },
            buildings = updatedBuildings
        )

        checkToastCondition()
    }

    init {
        startCookieGeneration()
    }

    private fun startCookieGeneration() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentState = _state.value
                _state.value = currentState.copy(
                    cookieCount = currentState.cookieCount + currentState.cookiesPerSecond,
                    elapsedTime = currentState.elapsedTime + 1
                )
                updatePurchasableStatus()
                checkToastCondition()
            }
        }
    }

    private fun updatePurchasableStatus() {
        val currentCookies = _state.value.cookieCount
        val updatedBuildings = _state.value.buildings.map { building ->
            building.copy(isPurchasable = currentCookies >= building.price)
        }

        _state.value = _state.value.copy(buildings = updatedBuildings)
    }

    private fun checkToastCondition() {
        val cookieCount = state.value.cookieCount
        if (milestones <= cookieCount) {
            viewModelScope.launch {
                _toastMessages.emit("You reached $milestones cookies!")
            }
        }
    }
}
