package com.example.trashninja

import android.util.DisplayMetrics
import android.widget.ImageView
import kotlin.math.sqrt

class Trash(
    var imageView: ImageView,
    var xPos: Float,
    var yPos: Float,
    var xVel: Float,
    var yVel: Float,
    val trashType: Int,
    var state: Int = STATE_FREEFALL
) {
    fun updatePos(): Int {
        if (state != STATE_TOUCHED) {
            xPos += xVel
            yPos += yVel
        }
        if (state == STATE_FREEFALL) {
            yVel += GRAVITY
        }
        if ((state == STATE_THROW_GOOD) && (yPos >= BIN_HEIGHT)) {
            return RES_SUCCESS
        }

        if ((xPos >= 1) || (xPos <= 0) || (yPos >= 1) || (yPos <= 0)) {
            if (state == STATE_THROW_BAD) {
                return RES_FAIL
            } else {
                return RES_MISS
            }
        }
        return RES_ONGOING
    }

    fun doThrow(xDelta: Float, yDelta: Float){
        var vectLength: Float = sqrt((xDelta*xDelta + yDelta*yDelta).toFloat())
        var scale: Float= sqrt(THROW_SPEED/vectLength)
        xVel = xDelta * scale
        yVel = yDelta * scale
        if(yDelta<=0){
            state = STATE_THROW_BAD
            return
        }
        var a: Float = yDelta.toFloat()/xDelta
        var b: Float = yPos - a*xPos
        var xDest: Float = (BIN_HEIGHT - b)/a

        when{
            (xDest>=0 && xDest<0.333) && (trashType == PAPER) -> state=STATE_THROW_GOOD
            (xDest>=0.333 && xDest<0.667) && (trashType == METAL_OR_PLASTIC) -> state=STATE_THROW_GOOD
            (xDest>=0.667 && xDest<=1) && (trashType == GLASS) -> state=STATE_THROW_GOOD
            else -> state=STATE_THROW_BAD
        }
    }


    companion object {
        const val TIME_TO_COUNT_DOWN = 30_000L
        const val COUNT_DOWN_INTERVAL = 10L
        const val GRAVITY: Float = 0.0001f
        const val PAPER = 0
        const val METAL_OR_PLASTIC = 1
        const val GLASS = 2
        const val STATE_FREEFALL = 1
        const val STATE_TOUCHED = 2
        const val STATE_THROW_BAD = 3
        const val STATE_THROW_GOOD = 4
        const val BIN_HEIGHT = 0.9f
        const val RES_ONGOING = 2
        const val RES_SUCCESS = 1
        const val RES_MISS = 0
        const val RES_FAIL = -1
        const val THROW_SPEED: Float = 0.001f
        var screen_width: Int = 0
        var screen_height: Int = 0
    }
}