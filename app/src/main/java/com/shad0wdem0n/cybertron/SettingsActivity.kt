package com.shad0wdem0n.cybertron

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(column().apply {
            addView(heading("Settings"))
            addView(label("Resolution"))
            addView(spinner("resolution", Prefs.RESOLUTIONS, 2))
            addView(label("Graphics"))
            addView(spinner("graphics", Prefs.GRAPHICS, 1))
            addView(label("Frame rate cap"))
            addView(spinner("fps_cap", Prefs.FPS_CAPS, 0))
            addView(menuButton("Back") { finish() })
        })
    }

    private fun spinner(key: String, options: List<String>, default: Int) = Spinner(this).apply {
        adapter = ArrayAdapter(this@SettingsActivity, android.R.layout.simple_spinner_dropdown_item, options)
        setSelection(Prefs.getIndex(this@SettingsActivity, key, default).coerceIn(options.indices))
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) =
                Prefs.setIndex(this@SettingsActivity, key, pos)
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }
}
