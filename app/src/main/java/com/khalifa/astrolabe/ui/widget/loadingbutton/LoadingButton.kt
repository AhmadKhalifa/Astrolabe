package com.khalifa.astrolabe.ui.widget.loadingbutton

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.khalifa.astrolabe.R
import com.khalifa.astrolabe.util.ColorUtil

/**
 * @author Ahmad Khalifa
 */

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var button: Button
    private var progressBar: ProgressBar

    var isLoading = false
        set(loading) {
            field = loading
            if (loading) {
                button.text = ""
                button.isEnabled = false
                progressBar.visibility = View.VISIBLE
            } else {
                button.isEnabled = true
                button.text = text
                progressBar.visibility = View.GONE
            }
        }

    var text: String? = null
        set(value) {
            field = value
            button.text = value
        }

    var buttonColor: Int = 0
        set(value) {
            field = value
            drawButton()
        }

    var textColor: Int = 0
        set(value) {
            field = value
            drawButton()
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.loading_button, this, true)
        button = findViewById(R.id.btn)
        progressBar = findViewById(R.id.pb)
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
        text = a.getString(R.styleable.LoadingButton_buttonText)
        buttonColor = a.getColor(R.styleable.LoadingButton_buttonColor, 0)
        textColor = a.getColor(R.styleable.LoadingButton_textColor, 0)
        drawButton()
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) =
            button.setOnClickListener(onClickListener)

    private fun drawButton() {
        button.text = text
        if (buttonColor != 0)
            button.backgroundTintList = ColorStateList.valueOf(buttonColor)

        if (textColor != 0) {
            button.setTextColor(textColor)
            progressBar.indeterminateTintList = ColorStateList.valueOf(textColor)
        } else {
            button.setTextColor(
                    if (ColorUtil.isColorDark(buttonColor)) Color.WHITE else Color.BLACK
            )
            progressBar.indeterminateTintList = ColorStateList.valueOf(
                    if (ColorUtil.isColorDark(buttonColor)) Color.WHITE else Color.BLACK
            )
        }
    }
}