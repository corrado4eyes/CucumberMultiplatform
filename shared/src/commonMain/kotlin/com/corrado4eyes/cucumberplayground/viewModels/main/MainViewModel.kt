package com.corrado4eyes.cucumberplayground.viewModels.main

import com.corrado4eyes.cucumberplayground.login.AuthService
import com.corrado4eyes.cucumberplayground.models.TestConfiguration
import com.corrado4eyes.cucumberplayground.models.User
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class AppNavigator {
    object Loading : AppNavigator()
    object Login : AppNavigator()

    data class Home(
        val user: User,
    ) : AppNavigator()
}

class MainViewModel(
    private val testConfiguration: TestConfiguration?,
    private val authService: AuthService
) : BaseLifecycleViewModel() {

    private val _navState: MutableStateFlow<AppNavigator> = MutableStateFlow(AppNavigator.Loading)
    val navState = _navState.toInitializedObservable(AppNavigator.Loading, coroutineScope)

    init {
        coroutineScope.launch {
            if (testConfiguration != null) {
                if (testConfiguration.isLoggedIn) {
                    authService.login(testConfiguration.testEmail, "1234")
                }
            }
            authService.user.collect { user ->
                user?.let {
                    _navState.value = AppNavigator.Home(it)
                } ?: run { _navState.value = AppNavigator.Login }
            }
        }
    }
}
