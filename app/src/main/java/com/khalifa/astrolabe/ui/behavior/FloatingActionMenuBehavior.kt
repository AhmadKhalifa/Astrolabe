package com.khalifa.astrolabe.ui.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar.SnackbarLayout
import android.util.AttributeSet
import android.view.View
import com.leinardi.android.speeddial.SpeedDialView


class FloatingActionMenuBehavior (context: Context, attrs: AttributeSet) :
        CoordinatorLayout.Behavior<SpeedDialView>() {

    override fun layoutDependsOn(parent: CoordinatorLayout,
                                 child: SpeedDialView,
                                 dependency: View): Boolean {
        return dependency is SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout,
                                        child: SpeedDialView,
                                        dependency: View): Boolean {
        val translationY = Math.min(0f, dependency.translationY - dependency.height)
        child.translationY = translationY
        return true
    }
}