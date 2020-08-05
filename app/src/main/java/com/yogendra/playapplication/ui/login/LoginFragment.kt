package com.yogendra.playapplication.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.yogendra.playapplication.R
import com.yogendra.playapplication.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {


    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = LoginFragmentBinding.bind(
            inflater.inflate(
                R.layout.login_fragment,
                container,
                false
            )
        )
        viewModel = LoginViewmodel_Factory().create(LoginViewModel::class.java)
//        binding.loginmodel = viewModel


        binding.usernameEv.addTextChangedListener(getTextWatcher())
        binding.passwordEv.addTextChangedListener(getTextWatcher())


        subscribeUi()

        binding.loginButton.setOnClickListener {
            navigateToDetails(binding.root)
        }
        return binding.root;
    }

    private fun subscribeUi() {

        viewModel.loginInputState.observe(viewLifecycleOwner) { result ->
            binding.logindetailsstate = result

        }

    }

    fun navigateToDetails(view: View) {
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