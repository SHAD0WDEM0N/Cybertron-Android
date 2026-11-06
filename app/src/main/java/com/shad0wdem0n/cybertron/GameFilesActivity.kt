package com.shad0wdem0n.cybertron

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import kotlin.concurrent.thread

class GameFilesActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var importBtn: android.widget.Button
    private val pickFolder = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        status = TextView(this).apply { textSize = 14f; gravity = android.view.Gravity.CENTER }
        setContentView(column().apply {
            addView(heading("Game Files"))
            addView(status)
            importBtn = menuButton("Import game folder") { chooseFolder() }
            addView(importBtn)
            addView(menuButton("Back") { finish() })
        })
        refresh()
    }

    private fun refresh() {
        val dir = GameStorage.gameDir(this)
        val found = GameStorage.hasGameFiles(this)
        status.text = "Copy your own PC install into:\n${dir.absolutePath}\n\n" +
            "or import it from a folder.\n\n" +
            if (found) "Status: game files found (${GameStorage.sizeBytes(this) / (1024 * 1024)} MB)"
            else "Status: no game files yet"
    }

    private fun chooseFolder() {
        startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE), pickFolder)
    }

    @Deprecated("Kept dependency-free for now")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val uri = data?.data
        if (requestCode != pickFolder || resultCode != RESULT_OK || uri == null) return
        importBtn.isEnabled = false
        thread {
            var count = 0
            try {
                GameStorage.importTree(this, uri) { name ->
                    count++
                    if (count % 10 == 0) runOnUiThread { status.text = "Copying... $count files\n$name" }
                }
                runOnUiThread { importBtn.isEnabled = true; refresh() }
            } catch (e: Exception) {
                runOnUiThread { importBtn.isEnabled = true; status.text = "Import failed: ${e.message}" }
            }
        }
    }
}
