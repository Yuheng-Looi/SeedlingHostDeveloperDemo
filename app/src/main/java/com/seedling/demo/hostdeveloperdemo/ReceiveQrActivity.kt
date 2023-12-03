package com.seedling.demo.hostdeveloperdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ReceiveQrActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_qr)

        findViewById<TextView>(R.id.bank_info).text = MainActivity.bank_info
    }

}