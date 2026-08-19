package com.yumedev.seijakulistkmp.core.utils

import android.content.Context
import android.widget.Toast

actual class ToastManager(private val context: Context) {
    actual fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
