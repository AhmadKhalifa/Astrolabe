package com.khalifa.astrolabe.ui.base

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.viewmodel.BaseViewModel
import com.khalifa.astrolabe.viewmodel.BaseViewModelOwner

/**
 * @author Ahmad Khalifa
 */

abstract class BaseDialogFragment<out VM : BaseViewModel> :
        DialogFragment(),
        BaseViewModelOwner<VM> {

    private var _viewModel: VM? = null

    protected val viewModel: VM
        get() {
            _viewModel = _viewModel ?: getViewModelInstance()
            return if (_viewModel != null) _viewModel as VM
            else throw IllegalStateException("getViewModelInstance() implementation returns null!")
        }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerEventHandlerSubscribers(this, viewModel)
        registerLiveDataObservers()
        super.onViewCreated(view, savedInstanceState)
    }
}