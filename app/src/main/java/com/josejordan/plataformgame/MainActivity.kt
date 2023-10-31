package com.josejordan.plataformgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import android.os.Handler
import android.os.Looper


class MainActivity : AppCompatActivity() {
    private lateinit var character: ImageView
    private val moveSpeed = 10

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isTouchingLeft) {
                moveCharacter(-moveSpeed)
            } else if (isTouchingRight) {
                moveCharacter(moveSpeed)
            }
            handler.postDelayed(this, 30)  // Repetir cada 30ms
        }
    }

    private var isTouchingLeft = false
    private var isTouchingRight = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        character = findViewById(R.id.character)

        val gameLayout = findViewById<RelativeLayout>(R.id.gameLayout)
        gameLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (event.x < gameLayout.width / 2) {
                        // Si tocas la mitad izquierda de la pantalla
                        isTouchingLeft = true
                    } else {
                        // Si tocas la mitad derecha de la pantalla
                        isTouchingRight = true
                    }
                    handler.post(updateRunnable)
                }
                MotionEvent.ACTION_UP -> {
                    isTouchingLeft = false
                    isTouchingRight = false
                    handler.removeCallbacks(updateRunnable)
                }
            }
            true
        }
    }

    private fun moveCharacter(speed: Int) {
        character.x += speed
    }
}

