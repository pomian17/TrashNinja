package com.example.trashninja

import android.widget.ImageView

private class Trash(
    var imageView: ImageView, var xPos: Double, var yPos: Double,
    var xVel: Double, var yVel: Double,
    val trashType: Int, var state: Int = STATE_FREEFALL
) {
    fun updatePos(): Int {
        if (state != STATE_TOUCHED) {
            xPos += xVel
            yPos += yVel
        }
        if (state == STATE_FREEFALL) {
            yVel += GRAVITY
        }
        if ((state == STATE_THROW_GOOD) && (yPos <= BIN_HEIGHT)) {
            return RES_SUCCESS
        }

        if ((xPos >= 100) || (xPos <= 0) || (yPos >= 100) || (yPos <= 0)) {
            if (state == STATE_THROW_BAD) {
                return RES_FAIL
            } else {
                return RES_MISS
            }
        }
        return RES_ONGOING
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
    }
}