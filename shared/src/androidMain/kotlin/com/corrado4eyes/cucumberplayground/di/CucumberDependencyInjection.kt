@file:JvmName("AndroidDependencyInjection")

package com.corrado4eyes.cucumberplayground.di

import com.corrado4eyes.cucumberplayground.models.TestConfiguration
import com.corrado4eyes.cucumberplayground.viewModels.home.HomeViewModel
import com.corrado4eyes.cucumberplayground.viewModels.login.LoginViewModel
import com.corrado4eyes.cucumberplayground.viewModels.main.MainViewModel
import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.hud.BaseHUD
import com.splendo.kaluga.hud.HUD
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.ModuleDeclaration
import kotlin.coroutines.CoroutineContext

internal actual object PlatformModuleFactory : BasePlatformModuleFactory() {

    override val declaration: ModuleDeclaration = {
        factory<BaseAlertPresenter.Builder> { AlertPresenter.Builder() }
        factory<BaseHUD.Builder> { HUD.Builder() }
        viewModel {
            LoginViewModel()
        }
        viewModel {
            HomeViewModel(get(), get())
        }
        viewModel { (testConfig: TestConfiguration?) ->
            MainViewModel(testConfig)
        }
    }

    override fun platformDefinitions(): PlatformDefinitions = object : PlatformDefinitions {
        override val Scope.serviceCoroutineContext: CoroutineContext
            get() = Dispatchers.Default
    }
}

internal actual val appDeclaration: KoinAppDeclaration = {
    androidContext(ApplicationHolder.applicationContext)
}
