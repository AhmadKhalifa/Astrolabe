package com.khalifa.mapViewer.ui.activity

import android.os.Bundle
import com.khalifa.mapViewer.R
import com.khalifa.mapViewer.ui.base.BaseActivity
import com.khalifa.mapViewer.ui.fragment.SplashFragment
import com.khalifa.mapViewer.viewmodel.Error
import com.khalifa.mapViewer.viewmodel.Event
import com.khalifa.mapViewer.viewmodel.activity.SplashActivityViewModel
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @author Ahmad Khalifa
 */

class SplashActivity :
        BaseActivity<SplashActivityViewModel>(),
        SplashFragment.OnFragmentInteractionListener {

    private var _fragment: SplashFragment? = null
    private val fragment: SplashFragment
        get() {
            if (_fragment == null) {
                val existingFragment = supportFragmentManager.findFragmentByTag(SplashFragment.TAG)
                if (existingFragment != null) {
                    _fragment = existingFragment as SplashFragment
                }
            }
            _fragment = _fragment ?: SplashFragment.newInstance()
            return _fragment as SplashFragment
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        with(supportFragmentManager) {
            if (findFragmentByTag(SplashFragment.TAG) == null)
                beginTransaction()
                        .add(fragmentPlaceHolderLayout.id, fragment, SplashFragment.TAG)
                        .commit()
        }
        viewModel.initialize()
    }

    override fun getViewModelInstance() = SplashActivityViewModel.getInstance(this)

    override fun handleEvent(event: Event) = when(event) {
        Event.EXIT_SPLASH_SCREEN -> {
            MapActivity.startActivity(this)
            finish()
        }
    }

    override fun handleError(error: Error) {}

    override fun registerLiveDataObservers() {}
}
