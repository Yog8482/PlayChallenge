package com.yogendra.playapplication.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.yogendra.playapplication.databinding.DetailsFragmentBinding
import com.yogendra.playapplication.di.Injectable
import com.yogendra.playapplication.di.injectViewModel
import javax.inject.Inject

class DetailsFragment : Fragment(), Injectable {


    private lateinit var viewModel: DetailsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: DetailsFragmentBinding
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root


        binding.detailsWebview.settings.javaScriptEnabled = true; // enable javascript

        binding.detailsWebview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressbar.visibility=View.GONE
            }
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }


        binding.detailsWebview.loadUrl(args.webUrl)

        return binding.root
    }



}