package com.khalifa.mapviewer.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.khalifa.mapviewer.R
import com.khalifa.mapviewer.ui.base.BaseActivity
import com.khalifa.mapviewer.ui.fragment.MapFragment
import com.khalifa.mapviewer.viewmodel.Error
import com.khalifa.mapviewer.viewmodel.Event
import com.khalifa.mapviewer.viewmodel.activity.MapActivityViewModel
import kotlinx.android.synthetic.main.activity_map.*

/**
 * @author Ahmad Khalifa
 */

class MapActivity :
        BaseActivity<MapActivityViewModel>(),
        MapFragment.OnFragmentInteractionListener {

    companion object {

        fun startActivity(context: Context?) = context?.let {
            it.startActivity(Intent(it, MapActivity::class.java))
        }
    }

    private var _fragment: MapFragment? = null
    private val fragment: MapFragment
        get() {
            _fragment = _fragment ?:
                    supportFragmentManager.findFragmentByTag(MapFragment.TAG) as MapFragment
            _fragment = _fragment ?:
                    MapFragment.newInstance()
            return _fragment as MapFragment
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        with(supportFragmentManager) {
            if (findFragmentByTag(MapFragment.TAG) == null)
                beginTransaction()
                        .add(fragmentPlaceHolderLayout.id, fragment, MapFragment.TAG)
                        .commit()
        }
    }

    override fun getViewModelInstance() = MapActivityViewModel.getInstance(this)

    override fun handleEvent(event: Event) {

    }

    override fun handleError(error: Error) {

    }

    override fun registerLiveDataObservers() {

    }
}
