package com.khalifa.mapViewer.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import com.khalifa.mapViewer.R
import com.khalifa.mapViewer.ui.base.BaseActivity
import com.khalifa.mapViewer.ui.fragment.MapFragment
import com.khalifa.mapViewer.viewmodel.Error
import com.khalifa.mapViewer.viewmodel.Event
import com.khalifa.mapViewer.viewmodel.activity.MapActivityViewModel
import kotlinx.android.synthetic.main.activity_map.*
import org.osmdroid.config.Configuration

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
            if (_fragment == null) {
                val existingFragment = supportFragmentManager.findFragmentByTag(MapFragment.TAG)
                if (existingFragment != null) {
                    _fragment = existingFragment as MapFragment
                }
            }
            _fragment = _fragment ?: MapFragment.newInstance()
            return _fragment as MapFragment
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance() .load(
                applicationContext,
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
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
