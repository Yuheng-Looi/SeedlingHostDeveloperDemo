package com.seedling.demo.hostdeveloperdemo

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast

class AddCardActivity : AppCompatActivity() {

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private lateinit var intentFilters: Array<IntentFilter>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        findViewById<ImageView>(R.id.ivTickBig).visibility = View.GONE

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC Adapter not available", Toast.LENGTH_SHORT).show()
        }

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, AddCardActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE
        )

        intentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Toast.makeText(this, "NewIntent: ${intent?.action}", Toast.LENGTH_LONG).show()
        if (intent?.action == null) {
            Log.d(MainActivity.TAG, "nfc(null) action detected")
            findViewById<ImageView>(R.id.ivTickBig).visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(MainActivity.TAG, "onResume")
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
    }
}