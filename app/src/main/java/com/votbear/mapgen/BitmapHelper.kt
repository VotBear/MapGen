package com.votbear.mapgen

import android.graphics.Bitmap
import android.graphics.Color

class BitmapHelper {

    companion object {
        fun getGreyscaleBitmap(array: Array<FloatArray>): Bitmap{
            val w = array[0].size
            val h = array.size

            val compare = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            for (y in 0 until h) {
              for (x in 0 until w) {
                    val a = 255
                    val rgb = (255 * array[x][y]).toInt()
                    compare.setPixel(x, y, Color.argb(a, rgb,rgb,rgb))
                }
            }
            return compare
        }

        fun getBwBitmap(array: Array<BooleanArray>): Bitmap{
            val w = array[0].size
            val h = array.size

            val convArr = ArrayList<FloatArray>()
            for (y in 0 until h) {
                val row = ArrayList<Float>()
                for (x in 0 until w) {
                    row.add(when(array[y][x]){ true -> 1f false -> 0f })
                }
                convArr.add(row.toFloatArray())
            }
            return getGreyscaleBitmap(convArr.toTypedArray())
        }

        fun getColBitmap(array: Array<IntArray>): Bitmap{
            val w = array[0].size
            val h = array.size

            val compare = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            for (y in 0 until h) {
                for (x in 0 until w) {
                    val C = Color.WHITE
                    compare.setPixel(x, y, array[y][x])
                }
            }
            return compare
        }

        fun getIntFromColor(r: Int, g: Int, b: Int): Int {
            val red = r shl 16 and 0x00FF0000 //Shift red 16-bits and mask out other stuff
            val green = g shl 8 and 0x0000FF00 //Shift Green 8-bits and mask out other stuff
            val blue = b and 0x000000FF //Mask out anything not blue.

            return -0x1000000 or red or green or blue //0xFF000000 for 100% Alpha. Bitwise OR everything together.
        }
    }
}