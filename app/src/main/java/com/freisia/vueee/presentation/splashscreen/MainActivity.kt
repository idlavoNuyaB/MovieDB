package com.freisia.vueee.presentation.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.freisia.vueee.databinding.ActivityMainBinding
import com.freisia.vueee.presentation.list.ListActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed({
            startActivity(
                Intent(
                    this@MainActivity,
                    ListActivity::class.java
                )
            )
            finish()
        }, 2000)
    }
}
