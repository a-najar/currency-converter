package com.geniusforapp.exchange.ui.currencies

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import coil.api.load
import com.geniusforapp.exchange.BuildConfig
import com.geniusforapp.exchange.R
import com.geniusforapp.exchange.domain.entities.HttpCallFailureException
import com.geniusforapp.exchange.domain.entities.NoNetworkException
import com.geniusforapp.exchange.domain.entities.ServerUnreachableException
import kotlinx.android.synthetic.main.fragment_currencies.*


class CurrenciesFragment : Fragment(R.layout.fragment_currencies) {

    private val viewModel: CurrenciesViewModel by viewModels()

    private val currenciesAdapter: CurrenciesAdapter = CurrenciesAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initObservers()
        initActions()
    }

    private fun initActions() {
        cardHeader.setOnClickListener { listCurrencies.smoothScrollToPosition(0) }
    }

    private fun initObservers() {
        viewModel.currencyLiveData.observe(viewLifecycleOwner,
            Observer {
                currenciesAdapter.submitList(it)
            })

        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer { showError(it) })

        viewModel.loaderLiveData.observe(viewLifecycleOwner, Observer { showOrHideLoading(it) })

        viewModel.timerLiveData.observe(viewLifecycleOwner, Observer {
            currentProgress.progress = it.toInt()
            textProgress.text = it.toString()
        })

        viewModel.baseLiveData.observe(viewLifecycleOwner, Observer {
            imageBase.load(String.format(BuildConfig.FLAG_URL, it.substring(0, 2)))
            textBase.text = it
        })
    }

    private fun showOrHideLoading(loading: Boolean) {
        if (loading) progressBar.show() else progressBar.hide()
    }

    private fun showError(throwable: Throwable) {
        when (throwable) {
            is NoNetworkException -> showNoInternet()
            is ServerUnreachableException -> showNotRespondingError()
            is HttpCallFailureException -> showFailToRequest()
        }
    }

    private fun showFailToRequest() {}

    private fun showNotRespondingError() {}

    private fun showNoInternet() {}

    private fun initList() {
        currenciesAdapter.onRateClicked = {
            findNavController().navigate(CurrenciesFragmentDirections.openConverter(it))
        }
        with(listCurrencies) {
            adapter = currenciesAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }
}