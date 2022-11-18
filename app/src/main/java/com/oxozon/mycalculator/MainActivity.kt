package com.oxozon.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var basicCalculatorButton: Button? = null
    private var advancedCalculatorButton: Button? = null
    private var aboutButton: Button? = null
    private var exitButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        basicCalculatorButton = findViewById<Button>(R.id.btnBasicCalculator)
        basicCalculatorButton?.setOnClickListener {
            val intent = Intent(this, BasicCalculator::class.java)
            startActivity(intent)
        }

        advancedCalculatorButton = findViewById<Button>(R.id.btnAdvancedCalculator)
        advancedCalculatorButton?.setOnClickListener {
            val advancedIntent = Intent(this, AdvancedCalculator::class.java)
            startActivity(advancedIntent)
        }

        aboutButton = findViewById(R.id.btnAbout)
        aboutButton?.setOnClickListener {
            val aboutIntent = Intent(this, About::class.java)
            startActivity(aboutIntent)
        }

        exitButton = findViewById(R.id.btnExit)
        exitButton?.setOnClickListener {
            finish()
        }
    }
}