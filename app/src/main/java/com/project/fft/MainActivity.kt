package com.project.fft

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.random.Random
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var orderButton: Button
    private lateinit var vendorButton: Button

    // Screen dimensions
    private var screenWidth = 0
    private var screenHeight = 0

    // Speeds for floating icons (modify as needed)
    private val minSpeed = 1.5f
    private val maxSpeed = 7f

    // Fling deceleration factor (how quickly the fling slows down)
    private val decelerationFactor = 0.99f

    // Minimum speed threshold before returning to original floating behavior
    private val minFlingSpeedThreshold = 0.99f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        orderButton = findViewById(R.id.order_button)
        vendorButton=findViewById(R.id.vendor_button)

        orderButton.setOnClickListener {
            val intent = Intent(this, vendor_list::class.java)
            startActivity(intent)
        }
        vendorButton.setOnClickListener {
            val intent = Intent(this, vendor_login::class.java)
            startActivity(intent)
        }

        // Get the screen size
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        // Floating icons
        val floatingIcon1 = findViewById<ImageView>(R.id.floatingIcon1)
        val floatingIcon2 = findViewById<ImageView>(R.id.floatingIcon2)

        // Set floating behavior for icons
        setFloatingBehavior(floatingIcon1)
        setFloatingBehavior(floatingIcon2)

        // Add touch listener for dragging and throwing
        setTouchListener(floatingIcon1)
        setTouchListener(floatingIcon2)
    }

    private fun setFloatingBehavior(icon: ImageView) {
        // Generate random initial speeds for the icon
        var deltaX = randomSpeed()
        var deltaY = randomSpeed()

        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 16L // 16ms for smooth 60fps
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                // Move the icon by delta values
                val newX = icon.x + deltaX
                val newY = icon.y + deltaY

                // Check for boundaries and bounce if needed
                if (newX <= 0 || newX + icon.width >= screenWidth) {
                    deltaX = -deltaX // Reverse X direction
                }
                if (newY <= 0 || newY + icon.height >= screenHeight) {
                    deltaY = -deltaY // Reverse Y direction
                }

                // Apply the movement
                icon.x += deltaX
                icon.y += deltaY
            }
        }

        // Start the animation
        animator.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(icon: ImageView) {
        icon.setOnTouchListener(object : View.OnTouchListener {
            var dX = 0f
            var dY = 0f
            var isDragging = false
            var flingXSpeed = 0f
            var flingYSpeed = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = v.x - event.rawX
                        dY = v.y - event.rawY
                        isDragging = true
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (isDragging) {
                            // Move the icon with the user's finger
                            v.x = event.rawX + dX
                            v.y = event.rawY + dY
                        }
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        // Calculate the fling speed based on touch velocity
                        val flingDuration = 200L // Duration of the fling
                        val deltaX = (event.rawX + dX) - v.x
                        val deltaY = (event.rawY + dY) - v.y
                        flingXSpeed = deltaX / flingDuration
                        flingYSpeed = deltaY / flingDuration

                        // Start the fling animation
                        startFlingWithDeceleration(icon, flingXSpeed, flingYSpeed)
                        isDragging = false
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun startFlingWithDeceleration(icon: ImageView, flingXSpeed: Float, flingYSpeed: Float) {
        var deltaX = flingXSpeed
        var deltaY = flingYSpeed

        val flingAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 16L // 16ms for smooth 60fps
             repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                // Move the icon by fling speed
                val newX = icon.x + deltaX
                val newY = icon.y + deltaY

                // Check for boundaries and bounce if needed
                if (newX <= 0 || newX + icon.width >= screenWidth) {
                    deltaX = -deltaX // Reverse X direction
                }
                if (newY <= 0 || newY + icon.height >= screenHeight) {
                    deltaY = -deltaY // Reverse Y direction
                }

                // Apply the fling movement
                icon.x += deltaX
                icon.y += deltaY

                // Apply deceleration to the fling speeds
                deltaX *= decelerationFactor
                deltaY *= decelerationFactor

                // If speed gets very low, stop the fling and revert to original floating behavior
                if (abs(deltaX) < minFlingSpeedThreshold && abs(deltaY) < minFlingSpeedThreshold) {
                    this.cancel() // Stop fling animation
                    setFloatingBehavior(icon) // Revert to floating behavior
                }
            }
        }

        // Start the fling animation
        flingAnimator.start()
    }

    private fun randomSpeed(): Float {
        // Generate a random speed in the range [minSpeed, maxSpeed]
        val speed = Random.nextFloat() * (maxSpeed - minSpeed) + minSpeed
        return if (Random.nextBoolean()) speed else -speed // Randomize direction
    }
}