package com.example.sickpredict.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.sickpredict.R

object DialogUtils {
    public fun buildLoadingDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setView(LayoutInflater.from(context).inflate(R.layout.loading_screen, null))
        builder.setCancelable(false)
        return builder.create()
    }
}
