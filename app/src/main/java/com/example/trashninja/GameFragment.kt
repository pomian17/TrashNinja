package com.example.trashninja

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.example.trashninja.Trash.Companion.METAL_OR_PLASTIC
import com.example.trashninja.Trash.Companion.RES_FAIL
import com.example.trashninja.Trash.Companion.RES_MISS
import com.example.trashninja.Trash.Companion.RES_ONGOING
import com.example.trashninja.Trash.Companion.RES_SUCCESS
import kotlinx.android.synthetic.main.fragment_game.*


class GameFragment : Fragment() {

    private lateinit var countDownTimer: CountDownTimer
    private var score: Int = 0
        set(value) {
            score_view.text = value.toString()
            field = value
        }
    val trashes = mutableListOf<Trash>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addImage(0.5f, 0.1f, 0.0f, 0.0f, METAL_OR_PLASTIC)
        setupTimer()
        countDownTimer.start()
        score_view.text = "0"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }

    private fun setupTimer() {
        countDownTimer = object : CountDownTimer(TIME_TO_COUNT_DOWN, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                trashes.forEach {
                    var res = it.updatePos()
                    if (res == RES_ONGOING) {
                        val constraintSet = ConstraintSet()
                        constraintSet.clone(main_container)
                        constraintSet.setVerticalBias(
                            it.imageView.id,
                            it.yPos
                        )
                        constraintSet.applyTo(main_container)
                    } else {
                        main_container.removeView(it.imageView)
                        trashes.remove(it)
                        when (res) {
                            RES_SUCCESS -> score += 5
                            RES_MISS -> score -= 2
                            RES_FAIL -> score -= 10
                        }
                    }
                }
                time_view.text = "%1\$02d:%2\$02d".format(
                    (millisUntilFinished / 1000 / 60).toInt(),
                    (millisUntilFinished / 1000).toInt()
                )
            }

            override fun onFinish() {

            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addImage(
        xPos: Float,
        yPos: Float,
        xVel: Float,
        yVel: Float,
        trashType: Int
    ) {
        val imageView = ImageView(this.context)

        imageView.setImageResource(R.drawable.ic_android_black_24dp)
        imageView.setOnTouchListener { _, event ->
            when (event.action) {
                //TODO
                MotionEvent.ACTION_DOWN -> Log.d("GameFragment", "ACTION_DOWN")
                MotionEvent.ACTION_UP -> Log.d("GameFragment", "ACTION_DOWN")
                else -> Log.d("GameFragment", event.action.toString())
            }
            true
        }
        imageView.id = View.generateViewId()

        main_container.addView(imageView)
        val constraintSet = ConstraintSet()
        constraintSet.clone(main_container)

        constraintSet.connect(
            imageView.id,
            ConstraintSet.START,
            main_container.id,
            ConstraintSet.START,
            0
        )

        constraintSet.connect(
            imageView.id,
            ConstraintSet.TOP,
            main_container.id,
            ConstraintSet.TOP,
            0
        )

        constraintSet.connect(
            imageView.id,
            ConstraintSet.END,
            main_container.id,
            ConstraintSet.END,
            0
        )

        constraintSet.connect(
            imageView.id,
            ConstraintSet.BOTTOM,
            main_container.id,
            ConstraintSet.BOTTOM,
            0
        )

        constraintSet.setVerticalBias(
            imageView.id,
            yPos
        )
        constraintSet.setHorizontalBias(
            imageView.id,
            xPos
        )
        constraintSet.applyTo(main_container)
        trashes.add(Trash(imageView, xPos, yPos, xVel, yVel, trashType))

    }

    companion object {
        private const val TIME_TO_COUNT_DOWN = 30_000L
        private const val COUNT_DOWN_INTERVAL = 10L
    }
}