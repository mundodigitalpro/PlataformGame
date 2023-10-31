package com.josejordan.plataformgame
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var man: ImageView
    private lateinit var character: ImageView
    private val moveSpeed = 10
    private var isTouchingLeft = false
    private var isTouchingRight = false
    private var touchJob: Job? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        man = findViewById(R.id.man)
        character = findViewById(R.id.character)
        val gameLayout = findViewById<RelativeLayout>(R.id.gameLayout)
        gameLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (event.x < gameLayout.width / 2) {
                        isTouchingLeft = true
                    } else {
                        isTouchingRight = true
                    }
                    startUpdatingCharacter()
                }
                MotionEvent.ACTION_UP -> {
                    isTouchingLeft = false
                    isTouchingRight = false
                    touchJob?.cancel()
                }
            }
            true
        }
    }
    private fun checkCollision(): Boolean {
        return character.x < man.x + man.width &&
                character.x + character.width > man.x &&
                character.y < man.y + man.height &&
                character.y + character.height > man.y
    }


    private fun startUpdatingCharacter() {
        touchJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                updateCharacterPosition()
                delay(30)  // Similar a la espera de 30 ms que teníamos antes
            }
        }
    }

    private fun updateCharacterPosition() {
        if (isTouchingLeft) {
            moveCharacter(-moveSpeed)
        } else if (isTouchingRight) {
            moveCharacter(moveSpeed)
        }
        if (checkCollision()) {
            // ¡Colisión detectada!
            // Puedes detener el juego, mostrar un mensaje, etc. según lo necesites.
            touchJob?.cancel()
        }
    }

    private fun moveCharacter(speed: Int) {
        character.x += speed
    }


}
