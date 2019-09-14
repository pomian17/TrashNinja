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
                val secondsToEnd = (millisUntilFinished / 1000).toInt() + 1
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
    }
}