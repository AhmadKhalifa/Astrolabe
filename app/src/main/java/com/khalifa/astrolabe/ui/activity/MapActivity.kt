package com.khalifa.astrolabe.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.base.BaseActivity
import com.khalifa.astrolabe.ui.fragment.MapFragment
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.activity.MapActivityViewModel
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

    override fun onBackPressed() {
        if (fragment.isInDrawingMode()) {
            fragment.cancelDrawingMode()
        } else {
            super.onBackPressed()
        }
    }

    override fun getViewModelInstance() = MapActivityViewModel.getInstance(this)

    override fun onEvent(event: Event) {

    }

    override fun onError(error: Error) {

    }

    override fun registerLiveDataObservers() {

    }
}
