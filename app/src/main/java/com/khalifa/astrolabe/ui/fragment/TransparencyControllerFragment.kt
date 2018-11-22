package com.khalifa.astrolabe.ui.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.ui.base.BaseBottomSheetDialogFragment
import com.khalifa.astrolabe.ui.widget.osmdroid.TilesOverlayWithOpacity
import com.khalifa.astrolabe.util.MapSourceUtil
import com.khalifa.astrolabe.viewmodel.Error
import com.khalifa.astrolabe.viewmodel.Event
import com.khalifa.astrolabe.viewmodel.fragment.implementation.TransparencyControllerViewModel
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.fragment_transparency_controller.*
import java.lang.IllegalStateException

class TransparencyControllerFragment :
        BaseBottomSheetDialogFragment<TransparencyControllerViewModel>() {

    companion object {

        private val TAG: String = TransparencyControllerFragment::class.java.simpleName

        private const val KEY_TILE_OVERLAY =
                "com.khalifa.astrolabe.ui.fragment.TransparencyControllerFragment.KEY_TILE_OVERLAY"

        fun showFragment(fragmentManager: FragmentManager?,
                         tilesOverlay: TilesOverlayWithOpacity,
                         onFragmentInteractionListener: OnFragmentInteractionListener) =
                fragmentManager?.let { manager ->
                    TransparencyControllerFragment().apply {
                        fragmentInteractionListener = onFragmentInteractionListener
                        arguments = Bundle().apply {
                            putSerializable(KEY_TILE_OVERLAY, tilesOverlay)
                        }
                    }.show(manager, TAG)
                }
    }

    interface OnFragmentInteractionListener {

        fun onTransparencyChanged(transparencyPercentage: Int)
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_transparency_controller, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tilesOverlay = arguments?.getSerializable(KEY_TILE_OVERLAY)?.let {
            it as TilesOverlayWithOpacity
        } ?: throw IllegalStateException("Unable to extract tile overlay from fragment arguments.")
        viewModel.tileOverlay.value = tilesOverlay
        doneButton.setOnClickListener { dismiss() }
        transparencySeekBar.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener {

            override fun onProgressChanged(bubbleSeekBar: BubbleSeekBar?,
                                           progress: Int,
                                           progressFloat: Float) {
                fragmentInteractionListener?.onTransparencyChanged(progress)
            }

            override fun getProgressOnActionUp(bubbleSeekBar: BubbleSeekBar?,
                                               progress: Int,
                                               progressFloat: Float) {

            }

            override fun getProgressOnFinally(bubbleSeekBar: BubbleSeekBar?,
                                              progress: Int, progressFloat: Float) {

            }
        }
    }

    private fun setTileOverlay(tilesOverlay: TilesOverlayWithOpacity) {
        iconImageView.setImageResource(MapSourceUtil.getImage(tilesOverlay.tileProvider().tileSource))
        nameTextView.text = MapSourceUtil.getName(tilesOverlay.tileProvider().tileSource)
        typeTextView.text = MapSourceUtil.getType(tilesOverlay.tileProvider().tileSource)
        transparencySeekBar.setProgress(tilesOverlay.transparencyPercentage.toFloat())
    }

    override fun getViewModelInstance() = TransparencyControllerViewModel.getInstance(this)

    override fun onEvent(event: Event) {}

    override fun onError(error: Error) {}

    override fun registerLiveDataObservers() {
        viewModel.tileOverlay.observe(this, Observer { it?.let(this::setTileOverlay)})
    }
}