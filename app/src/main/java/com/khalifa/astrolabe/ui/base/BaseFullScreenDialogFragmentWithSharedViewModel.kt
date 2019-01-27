package com.khalifa.astrolabe.ui.base

import android.os.Bundle
import android.view.View
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.BaseSharedViewModel
import com.khalifa.astrolabe.viewmodel.BaseSharedViewModelOwner


abstract class
BaseFullScreenDialogFragmentWithSharedViewModel<out VM : BaseRxViewModel, out SVM: BaseSharedViewModel> :
        BaseFullScreenDialogFragment<VM>(),
        BaseSharedViewModelOwner<SVM> {

    private var _sharedViewModel: SVM? = null
    protected val sharedViewModel: SVM
        get() {
            _sharedViewModel = _sharedViewModel ?: getSharedViewModelInstance()
            return if (_sharedViewModel != null) _sharedViewModel as SVM
            else throw IllegalStateException(
                    "getSharedViewModelInstance() implementation returns null!"
            )
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerSharedVMEventHandlerSubscribers(this, sharedViewModel)
        super.onViewCreated(view, savedInstanceState)
    }
}