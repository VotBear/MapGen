package com.votbear.mapgen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val DEFAULT_THRESH = 50
    val DEFAULT_LAND = 4
    val DEFAULT_WATER = 5
    val DIMEN_MIN_LIMIT = 30
    val DIMEN_DEFAULT = 100
    val DIMEN_MAX_LIMIT = 2000
    private fun ensureValid(str: String) : Int{
        val v = str.toIntOrNull()
        when(v) {
            null -> return DIMEN_DEFAULT
            else -> {
                if (v < DIMEN_MIN_LIMIT) return DIMEN_MIN_LIMIT
                if (v > DIMEN_MAX_LIMIT) return DIMEN_MAX_LIMIT
                return v
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (it:EditText in arrayOf(et_h, et_w)) {
            it.setText(DIMEN_DEFAULT.toString())
            it.onFocusChangeListener = object : View.OnFocusChangeListener{
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (!hasFocus) {
                        it.setText(ensureValid(it.text.toString()).toString())
                    }
                }
            }
        }

        btn_genca.setOnClickListener {
            generateCellularAutomata()
            seekbar_caiter.progress = 0
            seekbar_caiter.max = CellularAutomataHelper.ITERATIONS
            updateIter()
            seekbar_caiter.visibility = View.VISIBLE
            tv_iter.visibility = View.VISIBLE
        }

        seekbar_threshold.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_init_threshold.text = getString(R.string.neighbor_land, progress)
            }
        })
        seekbar_neigh_land.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_neigh_land.text = getString(R.string.neighbor_land, progress)
            }
        })
        seekbar_neigh_water.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tv_neigh_water.text = getString(R.string.neighbor_water, progress)
            }
        })
        seekbar_caiter.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateIter()
            }
        })

        seekbar_threshold.progress = DEFAULT_THRESH
        seekbar_neigh_land.progress = DEFAULT_LAND
        seekbar_neigh_water.progress = DEFAULT_WATER
    }
    fun updateIter() {
        val progress = seekbar_caiter.progress
        val bitmap = BitmapHelper.getBwBitmap(CellularAutomataHelper.getBoolMap(progress))
        tv_iter.text = getString(R.string.iteration, progress)
        iv_map.setImageBitmap(bitmap)
    }

    fun generateCellularAutomata() {
        val h = et_h.text.toString().toInt()
        val w = et_w.text.toString().toInt()
        val thresh = seekbar_threshold.progress
        val neigh_l = seekbar_neigh_land.progress
        val neigh_w = seekbar_neigh_water.progress
        CellularAutomataHelper.generateCA(h, w, thresh, neigh_l, neigh_w)
    }
}
