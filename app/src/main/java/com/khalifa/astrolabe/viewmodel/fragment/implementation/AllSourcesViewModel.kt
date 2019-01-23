package com.khalifa.astrolabe.viewmodel.fragment.implementation

import android.arch.lifecycle.ViewModelProviders
import com.khalifa.astrolabe.ui.fragment.AllSourcesFragment
import com.khalifa.astrolabe.viewmodel.BaseRxViewModel
import com.khalifa.astrolabe.viewmodel.fragment.IAllSourcesViewModel

/**
 * @author Ahmad Khalifa
 */

class AllSourcesViewModel : BaseRxViewModel(), IAllSourcesViewModel {

    companion object {
        @JvmStatic
        fun getInstance(allSourcesFragment: AllSourcesFragment): AllSourcesViewModel =
                ViewModelProviders
                        .of(allSourcesFragment)
                        .get(AllSourcesViewModel::class.java)
    }
}