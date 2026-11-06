package com.shad0wdem0n.cybertron

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

/** Placeholder: the engine (platform + cpu + renderer) will be hosted here. */
class GameActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(TextView(this).apply {
            gravity = android.view.Gravity.CENTER
            textSize = 16f
            text = "Engine not implemented yet.\n\n" +
                "Resolution: ${Prefs.resolution(this@GameActivity)}\n" +
                "Graphics: ${Prefs.graphics(this@GameActivity)}\n" +
                "FPS cap: ${Prefs.fpsCap(this@GameActivity)}"
        })
    }
}
