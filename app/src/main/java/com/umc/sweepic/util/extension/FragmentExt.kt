package com.umc.sweepic.util.extension

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.umc.sweepic.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.snackBar(anchorView: View, message: () -> String) {
    Snackbar.make(anchorView, message(), Snackbar.LENGTH_SHORT).show()
}

fun Fragment.stringOf(@StringRes resId: Int) = getString(resId)

fun Fragment.colorOf(@ColorRes resId: Int) = ContextCompat.getColor(requireContext(), resId)

fun Fragment.drawableOf(@DrawableRes resId: Int) =
    ContextCompat.getDrawable(requireContext(), resId)

fun Fragment.setStatusBarColor(colorResId: Int) {
    activity?.let {
        val statusBarColor = ContextCompat.getColor(it, colorResId)
        it.window.statusBarColor = statusBarColor
    }
}

val Fragment.viewLifeCycle
    get() = viewLifecycleOwner.lifecycle

val Fragment.viewLifeCycleScope
    get() = viewLifecycleOwner.lifecycleScope

// 커스텀 스낵바 확장 함수
fun Fragment.showCustomSnackBar(view: View, message: String){
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)

    val snackBarView = snackBar.view
    val parentView = snackBarView.parent

    if (parentView is CoordinatorLayout) {
        val params = snackBarView.layoutParams as CoordinatorLayout.LayoutParams
        params.setMargins(50, 50, 50, 100)  // 원하는 마진 값 설정
        snackBarView.layoutParams = params
    } else if (parentView is FrameLayout) {
        val params = snackBarView.layoutParams as FrameLayout.LayoutParams
        params.setMargins(50, 50, 50, 100)  // 원하는 마진 값 설정
        snackBarView.layoutParams = params
    }

    val textView: TextView = snackBar.view.findViewById(com.google.android.material.R.id.snackbar_text)
    textView.setTextAppearance(R.style.TextAppearance_Sweepic_Warning)
    snackBar.setBackgroundTint(this.colorOf(R.color.white))

    snackBar.show()
}