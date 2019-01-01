package com.votbear.mapgen

import java.util.*
import kotlin.collections.ArrayList


class CellularAutomataHelper {
    companion object {
        val ITERATIONS = 30
        var mapIterations: Array<Array<BooleanArray>>? = null
        val dx = intArrayOf(0, 1, 1, 1, 0, -1, -1, -1)
        val dy = intArrayOf(1, 1, 0, -1, -1, -1, 0, 1)

        fun generateRandomMap(h:Int, w:Int, thresh: Int) : Array<BooleanArray> {
            val rand = Random()
            val ret = ArrayList<BooleanArray>()
            for (r in 0 until h) {
                val row = ArrayList<Boolean>()
                for (c in 0 until w) {
                    val cur = rand.nextInt(101)
                    row.add(cur >= thresh)
                }
                ret.add(row.toBooleanArray())
            }
            return ret.toTypedArray()
        }

        fun generateNext(arr:Array<BooleanArray>, neighL: Int, neighW: Int) : Array<BooleanArray> {
            val h = arr.size
            val w = arr[0].size
            val ret = ArrayList<BooleanArray>()
            for (r in 0 until h) {
                val row = ArrayList<Boolean>()
                for (c in 0 until w) {
                    var sum = 0
                    for (i in 0 until dx.size) {
                        val nr = r + dy[i]
                        val nc = c + dx[i]

                        if (nr < 0 || nc < 0 || nr >=h || nc>=w) continue // Treat border as water
                        else if (arr[nr][nc]) sum++
                    }

                    val cur = when (arr[r][c]) {
                        true -> sum >= neighL
                        false -> sum >= neighW
                    }
                    row.add(cur)
                }
                ret.add(row.toBooleanArray())
            }
            return ret.toTypedArray()
        }

        fun generateCA(h:Int, w:Int, thresh:Int, neighL:Int, neighW:Int) {
            val iters = ArrayList<Array<BooleanArray>>()

            val initialArray = generateRandomMap(h, w, thresh)
            iters.add(initialArray.clone())

            var curArray = initialArray
            for(i in 0 until ITERATIONS) {
                curArray = generateNext(curArray, neighL, neighW)
                iters.add(curArray.clone())
            }
            mapIterations = iters.toTypedArray()
        }

        fun getBoolMap(iter:Int) : Array<BooleanArray> {
            return mapIterations!![iter]
        }
    }
}