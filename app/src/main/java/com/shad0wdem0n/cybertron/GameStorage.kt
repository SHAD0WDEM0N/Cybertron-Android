package com.shad0wdem0n.cybertron

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import java.io.File

/** Where the user's own game files live and how they get imported. */
object GameStorage {

    /** Android/data/<package>/files/game  - created on first launch, never via a crash. */
    fun gameDir(ctx: Context): File {
        val base = ctx.getExternalFilesDir(null) ?: ctx.filesDir
        return File(base, "game").also { it.mkdirs() }
    }

    /** TODO: replace with a precise check once the exe name / key packages are known (docs/ANALYSIS.md). */
    fun hasGameFiles(ctx: Context): Boolean =
        gameDir(ctx).walkTopDown().any { it.isFile && it.extension.equals("exe", ignoreCase = true) }

    fun sizeBytes(ctx: Context): Long =
        gameDir(ctx).walkTopDown().filter { it.isFile }.sumOf { it.length() }

    /** Copy a folder chosen with ACTION_OPEN_DOCUMENT_TREE into the game dir. Call off the main thread. */
    fun importTree(ctx: Context, treeUri: Uri, onFile: (String) -> Unit) {
        val rootId = DocumentsContract.getTreeDocumentId(treeUri)
        copyDir(ctx.contentResolver, treeUri, rootId, gameDir(ctx), onFile)
    }

    private fun safe(name: String) = name.replace('/', '_').replace('\\', '_').ifEmpty { "_" }

    private fun copyDir(cr: ContentResolver, tree: Uri, parentId: String, dest: File, onFile: (String) -> Unit) {
        dest.mkdirs()
        val children = DocumentsContract.buildChildDocumentsUriUsingTree(tree, parentId)
        val cols = arrayOf(
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_MIME_TYPE
        )
        cr.query(children, cols, null, null, null)?.use { c ->
            while (c.moveToNext()) {
                val id = c.getString(0)
                val name = safe(c.getString(1) ?: continue)
                val mime = c.getString(2)
                if (mime == DocumentsContract.Document.MIME_TYPE_DIR) {
                    copyDir(cr, tree, id, File(dest, name), onFile)
                } else {
                    val uri = DocumentsContract.buildDocumentUriUsingTree(tree, id)
                    cr.openInputStream(uri)?.use { input ->
                        File(dest, name).outputStream().use { out -> input.copyTo(out, 1 shl 20) }
                    }
                    onFile(name)
                }
            }
        }
    }
}
