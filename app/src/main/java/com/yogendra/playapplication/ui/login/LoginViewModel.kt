package com.yogendra.playapplication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yogendra.playapplication.LoginMockApiCall
import com.yogendra.playapplication.MOCK_EMAIL
import com.yogendra.playapplication.MOCK_PASSWORD
import com.yogendra.playapplication.data.requests.LoginRequest
import com.yogendra.playapplication.di.CoroutineScopeIO
import com.yogendra.playapplication.repository.KeysRepository
import com.yogendra.playapplication.repository.LoginRepository
import com.yogendra.playapplication.ui.login.validation.LoginInputDataWithState
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val keysrepository: KeysRepository,
    @CoroutineScopeIO private val ioCoroutineScope: CoroutineScope
) : ViewModel() {

    private val _loginDetailsState: MutableLiveData<LoginInputDataWithState> = MutableLiveData()
    val loginInputState: LiveData<LoginInputDataWithState>
        get() = _loginDetailsState


    fun loginDataChanged(email: String?, password: String?) {
        _loginDetailsState.setValue(LoginInputDataWithState(email, password))
    }

    fun invalidateLoginPage() {
        _loginDetailsState.postValue(null)
        repository.invalidateProgressStatus()
        keysrepository.keyDownloadStatus.postValue(null)
    }

    fun downloadTopStories() {
        keysrepository.downloadTopstories(scope = ioCoroutineScope)
    }

    fun downloadDataStatus(): LiveData<String> {
        return keysrepository.keyDownloadStatus
    }

    fun getProgressStatus(): LiveData<String> {
        return repository.getProgressStatus()
    }

    fun getLogin(loginRequest: LoginRequest) {
        if (loginRequest.email.equals(MOCK_EMAIL) && loginRequest.password.equals(MOCK_PASSWORD)) {
            repository.performLogin(ioCoroutineScope, LoginMockApiCall.SUCCESS, loginRequest)
        } else if (loginRequest.email.equals(MOCK_EMAIL) || loginRequest.password.equals(
                MOCK_PASSWORD
            )
        ) {
            repository.performLogin(ioCoroutineScope, LoginMockApiCall.UN_AUTHORIZED, loginRequest)
        } else {
            repository.performLogin(ioCoroutineScope, LoginMockApiCall.BAD_REQUEST, loginRequest)

        }

    }


}