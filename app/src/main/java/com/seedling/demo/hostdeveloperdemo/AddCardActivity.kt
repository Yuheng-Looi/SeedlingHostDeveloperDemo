package com.seedling.demo.hostdeveloperdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            val ivTickBig = findViewById<ImageView>(R.id.ivTickBig)
            ivTickBig.visibility = View.VISIBLE
        }
    }
}