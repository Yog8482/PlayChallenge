package com.yogendra.playapplication.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.yogendra.playapplication.ProgressStatus
import com.yogendra.playapplication.data.requests.LoginRequest
import com.yogendra.playapplication.databinding.LoginFragmentBinding
import com.yogendra.playapplication.di.Injectable
import com.yogendra.playapplication.di.injectViewModel
import com.yogendra.playapplication.utilities.NoInternetException
import com.yogendra.socialmediamvvm.utils.ui_components.MultilineSnackbar
import javax.inject.Inject

class LoginFragment : Fragment(), Injectable {


    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = injectViewModel(viewModelFactory)
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.progressbar.visibility = View.GONE

        binding.usernameEv.addTextChangedListener(getTextWatcher())
        binding.passwordEv.addTextChangedListener(getTextWatcher())


        subscribeUi()

        binding.loginButton.setOnClickListener {

            viewModel.getLogin(
                LoginRequest(
                    viewModel.loginInputState.value?.email!!,
                    viewModel.loginInputState.value?.password!!
                )
            )
        }
        return binding.root;
    }


    private fun subscribeUi() {

        viewModel.loginInputState.observe(viewLifecycleOwner) { result ->
            binding.logindetailsstate = result

        }

        viewModel.getProgressStatus().observe(viewLifecycleOwner, Observer { status ->

            when (status) {
                ProgressStatus.LOADING.toString() -> binding.progressbar.visibility = View.VISIBLE

                ProgressStatus.COMPLTED.toString() -> {
                    binding.usernameEv.text = null
                    binding.passwordEv.text = null
                    binding.progressbar.visibility = View.GONE
                }

                ProgressStatus.ERROR.toString() -> binding.progressbar.visibility = View.GONE

                ProgressStatus.NO_NETWORK.toString() -> {
                    binding.progressbar.visibility = View.GONE

                    MultilineSnackbar(
                        binding.root,
                        NoInternetException().message.toString()
                    ).show()
                }

                else -> {
                    binding.progressbar.visibility = View.GONE
                    if (status.contains("Login Success")) {

                        viewModel.downloadTopStories()
                    }
                    MultilineSnackbar(
                        binding.root,
                        status.toString()
                    ).show()
                }

            }

        })



        viewModel.downloadDataStatus().observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                when (status) {
                    ProgressStatus.LOADING.toString() -> binding.progressbar.visibility =
                        View.VISIBLE

                    ProgressStatus.COMPLTED.toString() -> {
                        MultilineSnackbar(
                            binding.root,
                            "Top stories Downloaded"
                        ).show()
                        navigateToDetails(binding.root)

                    }

                    ProgressStatus.ERROR.toString() -> binding.progressbar.visibility = View.GONE

                    ProgressStatus.NO_NETWORK.toString() -> {
                        binding.progressbar.visibility = View.GONE

                        MultilineSnackbar(
                            binding.root,
                            NoInternetException().message.toString()
                        ).show()
                    }

                    else -> {
                        binding.progressbar.visibility = View.GONE
                        MultilineSnackbar(
                            binding.root,
                            status.toString()
                        ).show()
                    }

                }
            }

        })

    }

    fun navigateToDetails(view: View) {
        viewModel.invalidateLoginPage()

        val directions =
            LoginFragmentDirections.actionLoginFragmentToHomeFragment(
            )

        view.findNavController().navigate(
            directions
        )
    }

    fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // Do nothing.
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {

                viewModel.loginDataChanged(
                    binding.usernameEv.text?.toString(),
                    binding.passwordEv.text?.toString()
                )
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        }
    }
}