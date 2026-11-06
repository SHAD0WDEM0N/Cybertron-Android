package com.shad0wdem0n.cybertron

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

/** Tiny programmatic UI helpers (no layout XML or extra dependencies yet). */
internal fun Activity.dp(v: Int) = (v * resources.displayMetrics.density).toInt()

internal fun Activity.column(): LinearLayout = LinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL
    gravity = Gravity.CENTER
    setPadding(dp(32), dp(16), dp(32), dp(16))
}

internal fun Activity.heading(text: String) = TextView(this).apply {
    this.text = text
    textSize = 28f
    gravity = Gravity.CENTER
    setPadding(0, 0, 0, dp(16))
}

internal fun Activity.menuButton(text: String, onClick: () -> Unit) = Button(this).apply {
    this.text = text
    setOnClickListener { onClick() }
    layoutParams = LinearLayout.LayoutParams(dp(320), LinearLayout.LayoutParams.WRAP_CONTENT).apply {
        topMargin = dp(8)
    }
}

internal fun Activity.label(text: String): View = TextView(this).apply {
    this.text = text
    textSize = 16f
    gravity = Gravity.CENTER
    setPadding(0, dp(12), 0, dp(4))
}
