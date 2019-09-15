package com.example.trashninja

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.trashninja.GameSummaryFragment.Companion.EXTRA_SCORE
import com.example.trashninja.Trash.Companion.GLASS
import com.example.trashninja.Trash.Companion.METAL_OR_PLASTIC
import com.example.trashninja.Trash.Companion.PAPER
import com.example.trashninja.Trash.Companion.RES_FAIL
import com.example.trashninja.Trash.Companion.RES_MISS
import com.example.trashninja.Trash.Companion.RES_ONGOING
import com.example.trashninja.Trash.Companion.RES_SUCCESS
import com.example.trashninja.Trash.Companion.STATE_THROW_GOOD
import com.example.trashninja.Trash.Companion.STATE_TOUCHED
import kotlinx.android.synthetic.main.fragment_game.*
import android.util.DisplayMetrics


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
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        Trash.screen_width = displayMetrics.widthPixels
        Trash.screen_height = displayMetrics.heightPixels

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

                if (millisUntilFinished.hashCode() % 55 == 0){
                    addImage(
                        (300..600).random().toFloat()/1000,
                        0.1f,
                        (0..99).random().toFloat()/10000,
                        (0..50).random().toFloat()/10000
                    )}

                val tc = trashes.toMutableList()
                tc.forEach {
                    if (it.state != STATE_TOUCHED) {
                        val res = it.updatePos()
                        if (res == RES_ONGOING) {
                            val constraintSet = ConstraintSet()
                            constraintSet.clone(main_container)
                            constraintSet.setVerticalBias(
                                it.imageView.id,
                                it.yPos
                            )
                            constraintSet.setHorizontalBias(
                                it.imageView.id,
                                it.xPos
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
                }
                time_view.text = "%1\$02d:%2\$02d".format(
                    (millisUntilFinished / 1000 / 60).toInt(),
                    (millisUntilFinished / 1000).toInt()
                )
            }

            override fun onFinish() {
                val bundle = bundleOf(EXTRA_SCORE to score)
                Navigation.findNavController(main_container)
                    .navigate(R.id.action_gameFragment_to_gameSummaryFragment, bundle)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addImage(
        xPos: Float,
        yPos: Float,
        xVel: Float,
        yVel: Float
    ) {
        val imageView = ImageView(this.context)

        val trashType = (0..2).random()

        val imgRes = when (trashType) {
            METAL_OR_PLASTIC -> R.drawable.ic_plastic_1
            PAPER -> R.drawable.ic_paper_1
            GLASS -> R.drawable.ic_glass_1
            else -> R.drawable.ic_glass_1
        }
        imageView.setImageResource(imgRes)
        val trash = Trash(imageView, xPos, yPos, xVel, yVel, trashType)
        imageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("GameFragment", "ACTION_DOWN")
                    trash.state = STATE_TOUCHED
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("GameFragment", "ACTION_UP x=${event.x} y=${event.y}")
                    val displayMetrics = DisplayMetrics()
                    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                    val width = displayMetrics.widthPixels
                    val height = displayMetrics.heightPixels
                    Log.d("GameFragment", "doThrow args: ${event.x / width}, ${event.y / height}")
                    trash.doThrow(event.x / width, event.y / height)
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d("GameFragment", "ACTION_MOVE x=${event.x} y=${event.y}")
                    val displayMetrics = DisplayMetrics()
                    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                    val width = displayMetrics.widthPixels
                    val height = displayMetrics.heightPixels
                    Log.d("GameFragment", "doThrow args: ${event.x / width}, ${event.y / height}")
                    //trash.followTouch(event.x / width, event.y / height)
                }
                else -> {
                }/*Log.d("GameFragment", event.action.toString())*/
            }
            true
        }
        imageView.id = View.generateViewId()

        imageView.setPadding(8.px, 8.px, 8.px, 8.px)

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

        trashes.add(trash)
    }

    private val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    companion object {
        private const val TIME_TO_COUNT_DOWN = 30_000L
        private const val COUNT_DOWN_INTERVAL = 10L
    }
}