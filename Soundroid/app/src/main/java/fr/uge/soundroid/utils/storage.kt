package fr.uge.soundroid.utils

import android.content.Context
import java.io.ObjectOutputStream
import java.util.*

fun persistDump(context: Context) {
    val fileOutput = context.openFileOutput("dump-" + Date().toString(), Context.MODE_PRIVATE)
    val outputStream = ObjectOutputStream(fileOutput)

    outputStream.writeChars(DatabaseService.exportToJson())
    outputStream.close()
}