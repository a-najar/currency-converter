package com.geniusforapp.exchange.ui.converter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.geniusforapp.exchange.BuildConfig
import com.geniusforapp.exchange.R
import com.geniusforapp.exchange.domain.entities.Rate
import com.geniusforapp.exchange.domain.entities.formatCurrency
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_converter.*

class ConverterBottomSheet : BottomSheetDialogFragment() {
    private val args: ConverterBottomSheetArgs by navArgs()
    private val convertViewModel: ConverterViewModel by viewModels { ConverterFactory(rate = args.rate) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext()).apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_converter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
        convertViewModel.rateLiveData.observe(viewLifecycleOwner, rateObserver)
        convertViewModel.convertAmount.observe(viewLifecycleOwner, toObserver)

    }

    private fun initAction() {
        editTextFrom.setText(args.rate.baseRate.toString())
        buttonConvert.setOnClickListener {
            if (editTextFrom.text.isNullOrEmpty()) return@setOnClickListener
            convertViewModel.convert(
                editTextFrom.text.toString().toFloat()
            )
        }
    }

    private val toObserver = Observer<Float> {
        editTextTo.setText(it.toString())
    }

    private val rateObserver = Observer<Rate> {
        imageBase.load(String.format(BuildConfig.FLAG_URL, it.name.substring(0, 2)))
        textRate.text = getString(
            R.string.title_converter,
            it.base,
            it.price.formatCurrency(it.name)
        )
        textInputFrom.hint = getString(R.string.input_from_amount, it.base)
        textInputTo.hint = getString(R.string.input_to_amount, it.name)
    }
}

