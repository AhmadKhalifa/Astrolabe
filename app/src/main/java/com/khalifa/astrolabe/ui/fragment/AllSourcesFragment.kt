package com.khalifa.astrolabe.ui.fragment

import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.adapter.ViewPagerAdapter
import com.khalifa.astrolabe.ui.base.BaseFullScreenDialogFragment
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.ui.widget.osmdroid.WMSOverlayWithOpacity
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.AllSourcesViewModel
import com.leinardi.android.speeddial.SpeedDialView
import kotlinx.android.synthetic.main.fragment_all_sources.*
import kotlinx.android.synthetic.main.fragment_all_sources.view.*
import org.osmdroid.tileprovider.tilesource.ITileSource

/**
 * @author Ahmad Khalifa
 */

class AllSourcesFragment :
        BaseFullScreenDialogFragment<AllSourcesViewModel>(),
        MapSourcesListFragment.OnFragmentInteractionListener,
        WMSServicesListFragment.OnFragmentInteractionListener,
        ViewPager.OnPageChangeListener {

    companion object {
        val TAG: String = MapSourcesListFragment::class.java.simpleName

        private const val TRANSLATION_Y_VALUE = 100f

        private const val KEY_CHILD_FRAGMENT =
                "com.khalifa.astrolabe.ui.fragment.MapSourcesListFragment.KEY_CHILD_FRAGMENT"

        private const val MAP_SOURCES_FRAGMENT_INDEX = 0
        private const val WMS_SERVICES_FRAGMENT_INDEX = 1

        enum class ChildFragment { MAP_SOURCES_FRAGMENT, WMS_SERVICES_FRAGMENT }

        fun showFragment(fragmentManager: FragmentManager?,
                         fragmentInteractionListener: OnFragmentInteractionListener,
                         childFragment: ChildFragment = ChildFragment.MAP_SOURCES_FRAGMENT) =
                fragmentManager?.let { manager ->
                    AllSourcesFragment().also { fragment ->
                        fragment.arguments = Bundle().apply {
                            putSerializable(KEY_CHILD_FRAGMENT, childFragment)
                        }
                        fragment.fragmentInteractionListener = fragmentInteractionListener
                        fragment.show(manager, TAG)
                    }
                }
    }

    interface OnFragmentInteractionListener {

        fun onMapOverlayAdded(mapOverlay: TilesOverlayWithOpacity)

        fun onWMSOverlayAdded(wmsOverlay: WMSOverlayWithOpacity)
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null
    private var _mapSourcesFragment: MapSourcesListFragment? = null
    private val mapSourcesFragment: MapSourcesListFragment
        get() {
            if (_mapSourcesFragment == null) {
                val existingFragment =
                        childFragmentManager.findFragmentByTag(MapSourcesListFragment.TAG)
                if (existingFragment != null) {
                    _mapSourcesFragment = existingFragment as MapSourcesListFragment
                }
            }
            _mapSourcesFragment = _mapSourcesFragment ?: MapSourcesListFragment.newInstance(this)
            return _mapSourcesFragment as MapSourcesListFragment
        }

    private var _wmsSourcesFragment: WMSServicesListFragment? = null
    private val wmsSourcesFragment: WMSServicesListFragment
        get() {
            if (_wmsSourcesFragment == null) {
                val existingFragment =
                        childFragmentManager.findFragmentByTag(WMSServicesListFragment.TAG)
                if (existingFragment != null) {
                    _wmsSourcesFragment = existingFragment as WMSServicesListFragment
                }
            }
            _wmsSourcesFragment = _wmsSourcesFragment ?: WMSServicesListFragment.newInstance(this)
            return _wmsSourcesFragment as WMSServicesListFragment
        }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_all_sources, container, false)
                    .also {
                        with(it.toolbar) {
                            setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
                            setNavigationOnClickListener { dismiss() }
                            setTitle(R.string.app_name)
                        }
                    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewPager) viewPager@ {
            adapter = ViewPagerAdapter(childFragmentManager).apply {
                addFragment(
                        mapSourcesFragment,
                        tabLayout,
                        R.string.maps,
                        R.drawable.ic_map_white_24dp
                )
                addFragment(
                        wmsSourcesFragment,
                        tabLayout,
                        R.string.wms_layers,
                        R.drawable.ic_layers_white_24dp
                )
            }
            addOnPageChangeListener(this@AllSourcesFragment)
        }
        setFloatingActionButtonVisible(false)
        floatingActionButton.setOnChangeListener(object : SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {
                when (viewModel.currentFragmentIndex) {
                    AllSourcesViewModel.MAP_SOURCES_FRAGMENT_INDEX -> {}
                    AllSourcesViewModel.WMS_SOURCES_FRAGMENT_INDEX ->
                        showNewWMSServicesDialogFragment()
                    else -> {}
                }
                floatingActionButton.close()
                return false
            }

            override fun onToggleChanged(isOpen: Boolean) {}
        })
        tabLayout.setupWithViewPager(viewPager)
        val childFragment =
                arguments?.getSerializable(KEY_CHILD_FRAGMENT) ?: ChildFragment.MAP_SOURCES_FRAGMENT
        if (childFragment != ChildFragment.MAP_SOURCES_FRAGMENT) {
            viewPager.setCurrentItem(
                    when (arguments?.getSerializable(KEY_CHILD_FRAGMENT)
                            ?: ChildFragment.MAP_SOURCES_FRAGMENT) {
                        ChildFragment.WMS_SERVICES_FRAGMENT -> {
                            setFloatingActionButtonVisible(true)
                            WMS_SERVICES_FRAGMENT_INDEX
                        }
                        else -> {
                            setFloatingActionButtonVisible(false)
                            MAP_SOURCES_FRAGMENT_INDEX
                        }
                    },
                    true
            )
        }
    }

    private fun setFloatingActionButtonVisible(visible: Boolean) =
            floatingActionButton.animate().translationY(if (visible) 0f else TRANSLATION_Y_VALUE)

    private fun showNewWMSServicesDialogFragment() =
            NewWMSServiceDialogFragment.showFragment(fragmentManager)

    override fun onPageScrollStateChanged(position: Int) {}

    override fun onPageScrolled(position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        viewModel.currentFragmentIndex = position
        when (position) {
            AllSourcesViewModel.MAP_SOURCES_FRAGMENT_INDEX -> onMapSourcesFragmentSelected()
            AllSourcesViewModel.WMS_SOURCES_FRAGMENT_INDEX -> onWMSSourcesFragmentSelected()
            else -> {}
        }
    }

    private fun onMapSourcesFragmentSelected() = setFloatingActionButtonVisible(false)

    private fun onWMSSourcesFragmentSelected() = setFloatingActionButtonVisible(true)

    override fun getViewModelInstance() = AllSourcesViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {}

    override fun onBaseMapSelected(tileSource: ITileSource) = dismiss()

    override fun onMapOverlayAdded(mapOverlay: TilesOverlayWithOpacity) {
        fragmentInteractionListener?.onMapOverlayAdded(mapOverlay)
        dismiss()
    }

    override fun onWMSOverlayAdded(wmsOverlay: WMSOverlayWithOpacity) {
        fragmentInteractionListener?.onWMSOverlayAdded(wmsOverlay)
        dismiss()
    }
}