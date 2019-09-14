package com.example.trashninja

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game.*



class GameFragment : Fragment() {

    private lateinit var countDownTimer: CountDownTimer
    val imageViews = mutableListOf<ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addImage()
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
                imageViews.firstOrNull()?.let {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(main_container)
                    constraintSet.setVerticalBias(
                        it.id,
                        millisUntilFinished.toFloat() / TIME_TO_COUNT_DOWN.toFloat()
                    )
                    constraintSet.applyTo(main_container)
                }
                time_view.text = "%1\$02d:%2\$02d".format((millisUntilFinished / 1000/60).toInt() , (millisUntilFinished / 1000).toInt() )
            }

            override fun onFinish() {

            }
        }
    }

    private fun addImage() {
        val imageView = ImageView(this.context)

        imageView.setImageResource(R.drawable.ic_android_black_24dp)
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
        constraintSet.applyTo(main_container)
        imageViews.add(imageView)
    }

    companion object {
        private const val TIME_TO_COUNT_DOWN = 30_000L
        private const val COUNT_DOWN_INTERVAL = 10L
        private const val GRAVITY: Double = 0.0001
        private const val PAPER = 1
        private const val METAL_OR_PLASTIC = 2
        private const val GLASS = 3
        private const val STATE_FREEFALL = 1
        private const val STATE_TOUCHED = 2
        private const val STATE_THROW_BAD = 3
        private const val STATE_THROW_GOOD = 4
        private const val BIN_HEIGHT = 0.1
        private const val RES_ONGOING = 2
        private const val RES_SUCCESS = 1
        private const val RES_MISS = 0
        private const val RES_FAIL = -1
        private class Trash(var imageView: ImageView, var xPos: Double, var yPos: Double,
                            var xVel: Double, var yVel: Double,
                            val trashType: Int, var state: Int = STATE_FREEFALL)
        {
            fun updatePos(): Int{
                if(state != STATE_TOUCHED){
                    xPos += xVel
                    yPos += yVel
                }
                if(state == STATE_FREEFALL){
                    yVel += GRAVITY
                }
                if((state == STATE_THROW_GOOD) && (yPos <= BIN_HEIGHT)) {
                    return RES_SUCCESS
                }

                if((xPos >= 100) ||  (xPos <= 0) || (yPos >= 100) || (yPos <= 0)) {
                    if (state == STATE_THROW_BAD) {
                        return RES_FAIL
                    } else {
                        return RES_MISS
                    }
                }
                return RES_ONGOING
            }
        }
    }
}