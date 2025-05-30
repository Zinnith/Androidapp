package com.example.tippy

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator

private const val TAG="MainActivity"
private const val INITIAL_TIP_PERCENT=15;
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel:TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount:TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBaseAmount);
        seekBarTip=findViewById(R.id.seekBarTip);
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription=findViewById(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text="$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG,"onProgressChanged$progress")
                tvTipPercentLabel.text="$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }



            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        
        etBaseAmount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG,"afterTextChanged $s")
                computeTipAndTotal()
            }
        })

    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when(tipPercent){
            in 0..9->"Poor"
            in 10..14->"Good"
            in 15..19->"Great"
            else->"Amazing"
        }
        tvTipDescription.text=tipDescription
//        update the color based
        val color = ArgbEvaluator().evaluate( tipPercent.toFloat()/seekBarTip.max,
        ContextCompat.getColor(this , R.color.color_wort_tip),
        ContextCompat.getColor(this,R.color.color_bext_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
       }


    private fun computeTipAndTotal() {
        if(etBaseAmount.text.isEmpty()){
            tvTipAmount.text=""
            tvTotalAmount.text=""
            return
        }
        //1.Get the Value of the base;
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        //2.Compute the tip and Total
        val tipAmount = baseAmount *tipPercent/100
        val totalAmount = baseAmount+tipAmount
        //2 update the UI
        tvTipAmount.text="%.2f".format(tipAmount)
        tvTotalAmount.text="%2f".format(totalAmount)
    }
}

