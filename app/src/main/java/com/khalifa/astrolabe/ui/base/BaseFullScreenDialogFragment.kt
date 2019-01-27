package com.khalifa.astrolabe.ui.base

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.viewmodel.BaseViewModel
import com.khalifa.astrolabe.viewmodel.BaseViewModelOwner

/**
 * @author Ahmad Khalifa
 */

abstract class BaseFullScreenDialogFragment<out VM : BaseViewModel> :
        DialogFragment(),
        BaseViewModelOwner<VM> {

    private var _viewModel: VM? = null

    protected val viewModel: VM
        get() {
            _viewModel = _viewModel ?: getViewModelInstance()
            return if (_viewModel != null) _viewModel as VM
            else throw IllegalStateException("getViewModelInstance() implementation returns null!")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(R.style.DialogAnimation)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerEventHandlerSubscribers(this, viewModel)
        registerLiveDataObservers()
        super.onViewCreated(view, savedInstanceState)
    }
}