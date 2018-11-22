package com.khalifa.astrolabe.ui.base

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.BaseViewModelOwner

/**
 * @author Ahmad Khalifa
 */

abstract class BaseBottomSheetDialogFragment<out VM : BaseRxViewModel> :
        BottomSheetDialogFragment(),
        BaseViewModelOwner<VM> {

    private var _viewModel: VM? = null
    private var _rootView: View? = null

    protected val rootView: View
        get() {
            return if (_rootView != null) _rootView as View
            else throw IllegalStateException(
                    "Root view cannot be null. " +
                            "It MUST hold the root view created in the onViewCreated method"
            )
        }

    protected val viewModel: VM
        get() {
            _viewModel = _viewModel ?: getViewModelInstance()
            return if (_viewModel != null) _viewModel as VM
            else throw IllegalStateException("getViewModelInstance() implementation returns null!")
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _rootView = view
        registerEventHandlerSubscribers(this, viewModel)
        registerLiveDataObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }
}