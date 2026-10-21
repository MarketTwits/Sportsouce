package com.markettwits

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

/** Only Android 8–9 need a runtime permission to save photos in the public gallery. */
internal class AndroidAlbumStoragePermissionActivity : Activity() {
    private var requestId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestId = intent.getStringExtra("requestId")
        if (requestId !in pending) { finish(); return }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) complete(true)
        else if (savedInstanceState == null) requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) complete(grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED)
    }

    private fun complete(granted: Boolean) { pending[requestId]?.complete(granted); finish() }
    override fun onDestroy() {
        if (isFinishing) pending[requestId]?.complete(false)
        super.onDestroy()
    }

    companion object {
        private val pending = mutableMapOf<String, CompletableDeferred<Boolean>>()
        suspend fun request(context: Context) = withContext(Dispatchers.Main.immediate) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) return@withContext
            val id = UUID.randomUUID().toString()
            val result = CompletableDeferred<Boolean>()
            pending[id] = result
            try {
                context.startActivity(Intent(context, AndroidAlbumStoragePermissionActivity::class.java)
                    .putExtra("requestId", id).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                check(result.await()) { "Storage permission was not granted" }
            } finally { pending.remove(id) }
        }
    }
}
