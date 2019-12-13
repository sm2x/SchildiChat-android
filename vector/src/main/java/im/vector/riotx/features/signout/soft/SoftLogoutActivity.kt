/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.riotx.features.signout.soft

import android.content.Context
import android.content.Intent
import androidx.core.view.isVisible
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import im.vector.matrix.android.api.failure.GlobalError
import im.vector.matrix.android.api.session.Session
import im.vector.riotx.R
import im.vector.riotx.core.di.ScreenComponent
import im.vector.riotx.core.extensions.replaceFragment
import im.vector.riotx.features.MainActivity
import im.vector.riotx.features.MainActivityArgs
import im.vector.riotx.features.login.LoginActivity
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import javax.inject.Inject

/**
 * In this screen, the user is viewing a message informing that he has been logged out
 * Extends LoginActivity to get the login with SSO and forget password functionality for (nearly) free
 */
class SoftLogoutActivity : LoginActivity() {

    private val softLogoutViewModel: SoftLogoutViewModel by viewModel()

    @Inject lateinit var softLogoutViewModelFactory: SoftLogoutViewModel.Factory
    @Inject lateinit var session: Session

    override fun injectWith(injector: ScreenComponent) {
        super.injectWith(injector)
        injector.inject(this)
    }

    override fun initUiAndData() {
        super.initUiAndData()

        softLogoutViewModel
                .subscribe(this) {
                    updateWithState(it)
                }
    }

    override fun addFirstFragment() {
        replaceFragment(R.id.loginFragmentContainer, SoftLogoutFragment::class.java)
    }

    private fun updateWithState(softLogoutViewState: SoftLogoutViewState) {
        if (softLogoutViewState.asyncLoginAction is Success) {
            MainActivity.restartApp(this, MainActivityArgs())
        }

        loginLoading.isVisible = softLogoutViewState.isLoading()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SoftLogoutActivity::class.java)
        }
    }

    override fun handleInvalidToken(globalError: GlobalError.InvalidToken) {
        // No op here
        Timber.w("Ignoring invalid token global error")
    }
}
