package com.khalifa.mapviewer.ui.activity

import android.os.Bundle
import com.khalifa.mapviewer.R
import com.khalifa.mapviewer.ui.base.BaseActivity
import com.khalifa.mapviewer.ui.fragment.SplashFragment
import com.khalifa.mapviewer.viewmodel.Error
import com.khalifa.mapviewer.viewmodel.Event
import com.khalifa.mapviewer.viewmodel.activity.SplashActivityViewModel
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
            _fragment = _fragment ?:
                    supportFragmentManager.findFragmentByTag(SplashFragment.TAG) as SplashFragment
            _fragment = _fragment ?:
                    SplashFragment.newInstance()
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
