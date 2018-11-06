package com.khalifa.mapViewer.ui.base

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import com.khalifa.mapViewer.viewmodel.BaseRxViewModel
import com.khalifa.mapViewer.viewmodel.BaseViewModelOwner

/**
 * @author Ahmad Khalifa
 */

@SuppressLint("Registered")
abstract class BaseActivity<out VM : BaseRxViewModel> :
        AppCompatActivity(),
        BaseViewModelOwner<VM> {

    private var _viewModel: VM? = null

    protected val viewModel: VM
        get() {
            _viewModel = _viewModel ?: getViewModelInstance()
            return if (_viewModel != null) _viewModel as VM
            else throw IllegalStateException("getViewModelInstance() implementation returns null!")
        }

    override fun onResume() {
        super.onResume()
        registerEventHandlerSubscribers(this, viewModel)
        registerLiveDataObservers()
    }

    override fun onDestroy() {
        viewModel.clearDisposables()
        super.onDestroy()
    }
}