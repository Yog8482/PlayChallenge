package com.yogendra.playapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yogendra.playapplication.ProgressStatus
import com.yogendra.playapplication.adapter.HomeListAdapter
import com.yogendra.playapplication.databinding.HomeFragmentListBinding
import com.yogendra.playapplication.di.Injectable
import com.yogendra.playapplication.di.injectViewModel
import com.yogendra.playapplication.utilities.NoInternetException
import com.yogendra.socialmediamvvm.utils.ui_components.MultilineSnackbar
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable {


    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentListBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapter: HomeListAdapter by lazy { HomeListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)
        binding = HomeFragmentListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        viewModel.loadData()

        binding.list.adapter = adapter
        subscribeUi(adapter)


        binding.swiperefresh.setOnRefreshListener {
            viewModel.loadData()
            binding.swiperefresh.isRefreshing = false

        }
        return binding.root

    }


    private fun subscribeUi(adapter: HomeListAdapter) {
        binding.swiperefresh.isRefreshing = true

        viewModel.articles.observe(viewLifecycleOwner, Observer { result ->
            binding.swiperefresh.isRefreshing = false
            Log.i("HomeModel", "subscribeUi:$result")
            adapter.submitList(result)
            binding.hasArticles = result.isNotEmpty()
        })

        viewModel.progressStatus.observe(viewLifecycleOwner, Observer { status ->

            when (status) {
                ProgressStatus.LOADING.toString() -> binding.swiperefresh.isRefreshing = true

                ProgressStatus.COMPLTED.toString() -> binding.swiperefresh.isRefreshing = false

                ProgressStatus.ERROR.toString() -> binding.swiperefresh.isRefreshing = false

                ProgressStatus.NO_NETWORK.toString() -> {
                    binding.swiperefresh.isRefreshing = false

                    MultilineSnackbar(
                        binding.root,
                        NoInternetException().message.toString()
                    ).show()
                }

                else -> {
                    binding.swiperefresh.isRefreshing = false

                    MultilineSnackbar(
                        binding.root,
                        status
                    ).show()
                }

            }

        })

    }
}