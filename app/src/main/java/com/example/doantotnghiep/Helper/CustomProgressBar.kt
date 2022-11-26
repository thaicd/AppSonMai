package com.example.doantotnghiep.Helper

import android.R
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat


class CustomProgressBar {

    lateinit var dialog : Dialog
    constructor(activity : Activity) {
        dialog = Dialog(activity)
    }

    fun showProgressBar(activity: Activity) {
        if(dialog == null ) {
            dialog = Dialog(activity)
        }
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.example.doantotnghiep.R.layout.custom_layout_process)

        val window = dialog.window
        if (window == null) return
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val progressTv: TextView = dialog.findViewById(com.example.doantotnghiep.R.id.progress_tv)
        progressTv.setText(activity.getResources().getString(com.example.doantotnghiep.R.string.loading))
        progressTv.textSize = 19f
        dialog.show()
    }

    fun dismissDialog() {
        dialog?.dismiss()
    }
}