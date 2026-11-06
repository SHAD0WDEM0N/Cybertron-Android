package com.shad0wdem0n.cybertron

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GameStorage.gameDir(this) // make sure Android/data/<pkg>/files/game exists on first launch

        setContentView(column().apply {
            addView(heading("Transformers: War for Cybertron"))
            addView(menuButton("Play") {
                if (GameStorage.hasGameFiles(this@MainActivity)) {
                    startActivity(Intent(this@MainActivity, GameActivity::class.java))
                } else {
                    Toast.makeText(this@MainActivity, "Game files not found. Open Game Files first.", Toast.LENGTH_LONG).show()
                }
            })
            addView(menuButton("Settings") { startActivity(Intent(this@MainActivity, SettingsActivity::class.java)) })
            addView(menuButton("Game Files") { startActivity(Intent(this@MainActivity, GameFilesActivity::class.java)) })
            addView(menuButton("Exit") { finishAffinity() })
        })
    }
}
