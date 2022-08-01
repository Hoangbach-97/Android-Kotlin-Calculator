package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calculator.databinding.ActivityMainBinding
import java.lang.NumberFormatException


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var operand1: Double? = null
    private var pendingOperation = "="
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        create callback function
        val listener = View.OnClickListener {
            val b = it as Button
            binding.newNumber.append(b.text)
            Log.d("TAG", "onCreate: ${b.text}")
        }
        binding.button0.setOnClickListener {
            val b = it as Button
            binding.newNumber.append(b.text)
        }
//register callback with event click
        binding.button1.setOnClickListener(listener)
        binding.button2.setOnClickListener(listener)
        binding.button3.setOnClickListener(listener)
        binding.button4.setOnClickListener(listener)
        binding.button5.setOnClickListener(listener)
        binding.button6.setOnClickListener(listener)
        binding.button7.setOnClickListener(listener)
        binding.button8.setOnClickListener(listener)
        binding.button9.setOnClickListener(listener)
        binding.buttonDot.setOnClickListener(listener)

//        create a Callback
        val opListener = View.OnClickListener {
            val op = (it as Button).text.toString()
            try {
                val value = binding.newNumber.text.toString().toDouble()
                performOperation(value, op)
                //input until value  correct format number
            } catch (e: NumberFormatException) {
                binding.newNumber.setText("")
            }
            pendingOperation = op
            binding.operation.text = pendingOperation
            Log.d("TAG", "onCreate: $op")
        }
//        register Callback for each button
        binding.buttonEqual.setOnClickListener(opListener)
        binding.buttonDivide.setOnClickListener(opListener)
        binding.buttonPlus.setOnClickListener(opListener)
        binding.buttonMinus.setOnClickListener(opListener)
        binding.buttonMultiply.setOnClickListener(opListener)


        binding.buttonNeg?.setOnClickListener {
            val value = binding.newNumber.text.toString()
            if (value.isEmpty()) {
                binding.newNumber.setText("-")
            } else {
                try {
                    var negValue = value.toDouble()
                    negValue *= -1
                    binding.newNumber.setText(negValue.toString())
                } catch (e: NumberFormatException) {
                    binding.newNumber.setText("")
                }
            }
        }

    }

    //        Reset value using LONG PRESS ON VIEW
    private val gestureDetector = GestureDetector(object : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            Toast.makeText(applicationContext, "Reset values", Toast.LENGTH_LONG).show()
            binding.newNumber.setText("")
            binding.operation.setText("")
            binding.result.setText("")
        }
    })

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    //    private var operand1: Double? = null
//    private var operand2: Double = 0.0
    private fun performOperation(value: Double, op: String) {
//        check if not input
        if (operand1 == null) {
            Log.d("TAG", "performOperation: $value")
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                //get operation: +-*/
                pendingOperation = op
            }
            when (pendingOperation) {
                "=" -> operand1 = value
//                Check divide by zero
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        binding.result.setText(operand1.toString()) //Display final result
        binding.newNumber.setText("") //reset number input
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState.getString("performOperation", "=")
        operand1 = savedInstanceState.getDouble("operand1")
        Log.d("TAG", "onRestoreInstanceState: CALLED*****************")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("operand1", operand1!!)
        outState.putString("performOperation", pendingOperation)
        Log.d("TAG", "onSaveInstanceState: CALLED************** ")
    }
}