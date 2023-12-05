package com.seedling.demo.hostdeveloperdemo

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast

@Suppress("DEPRECATION")
class AddCardActivity : AppCompatActivity() {

    private val TAG = "AddCardActivity"
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private lateinit var intentFilters: Array<IntentFilter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        findViewById<ImageView>(R.id.ivTickBig).visibility = View.GONE

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, AddCardActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )

        intentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))

    }

    private fun updateView() {
        findViewById<ImageView>(R.id.ivTickBig).visibility = View.VISIBLE
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            Log.d(TAG, "nfc TAG detected")
            val tag : Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            if (tag != null) {
                readNfcTag(tag)
            }
        }
    }

    private fun readNfcTag(tag: Tag) {
        val technologies = tag.techList
        for (tech in technologies) {
            when (tech) {
                NfcA::class.java.name -> readNfcA(tag) //only this will extract for demo
                MifareClassic::class.java.name -> readMifareClassic(tag)
                NdefFormatable::class.java.name -> handleNdefFormatable(tag)
                // Add more cases for other supported technologies as needed
            }
        }
    }

    private fun bytesToHexString(bytes: ByteArray): String {
        val stringBuilder = StringBuilder()
        for (byte in bytes) {
            stringBuilder.append(String.format("%02X", byte))
        }
        return stringBuilder.toString()
    }

    private fun readNfcA(tag: Tag) {
        // Handle NfcA technology
        val nfcA = NfcA.get(tag)
        val uidBytes: ByteArray = nfcA.tag.id
        val uidString: String = bytesToHexString(uidBytes)
        val uid = nfcA!!.sak // Example: Get the Select Application Code (SAK) as an integer
        if (MainActivity.cardList.addCard(uidString, "New user", uid.toString())) {
            Toast.makeText(this, "New card is added successfully", Toast.LENGTH_SHORT).show()
            updateView()
        } else
            Toast.makeText(this, "The card is already exist", Toast.LENGTH_SHORT).show()
    }

    private fun readMifareClassic(tag: Tag) {
        // Handle MifareClassic technology
        // val mifareClassic = MifareClassic.get(tag)
        // Read data from MifareClassic tag...

    }

    private fun handleNdefFormatable(tag: Tag) {
        // Handle NdefFormatable technology
        // val ndefFormatable = NdefFormatable.get(tag)
        // Format the tag or perform other actions...
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause")
        nfcAdapter.disableForegroundDispatch(this)
    }
}