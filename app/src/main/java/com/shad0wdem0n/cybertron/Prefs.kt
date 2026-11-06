package com.shad0wdem0n.cybertron

import android.content.Context

/** Launcher settings. The engine will read these at start-up. */
object Prefs {
    val RESOLUTIONS = listOf("Native", "1080p", "720p", "540p", "480p")
    val GRAPHICS = listOf("Low", "Medium", "High")
    val FPS_CAPS = listOf("30", "45", "60", "Unlimited")

    private fun sp(ctx: Context) = ctx.getSharedPreferences("cybertron", Context.MODE_PRIVATE)

    fun getIndex(ctx: Context, key: String, default: Int) = sp(ctx).getInt(key, default)
    fun setIndex(ctx: Context, key: String, value: Int) = sp(ctx).edit().putInt(key, value).apply()

    fun resolution(ctx: Context) = RESOLUTIONS[getIndex(ctx, "resolution", 2).coerceIn(RESOLUTIONS.indices)]
    fun graphics(ctx: Context) = GRAPHICS[getIndex(ctx, "graphics", 1).coerceIn(GRAPHICS.indices)]
    fun fpsCap(ctx: Context) = FPS_CAPS[getIndex(ctx, "fps_cap", 0).coerceIn(FPS_CAPS.indices)]
}
