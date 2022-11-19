package com.oxozon.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.math.*

class AdvancedCalculator : AppCompatActivity() {
    private var tvInput: TextView? = null
    private var isLastCharNumeric: Boolean = false
    private var isLastCharDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_calculator)
        tvInput = findViewById(R.id.tvInput)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("calculatorText_key", tvInput?.text.toString())
        outState.putBoolean("lastCharNumeric_key", isLastCharNumeric)
        outState.putBoolean("lastCharDot_key", isLastCharDot)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        tvInput?.text = savedInstanceState.getString("calculatorText_key")
        isLastCharNumeric = savedInstanceState.getBoolean("lastCharNumeric_key")
        isLastCharDot = savedInstanceState.getBoolean("lastCharDot_key")
        super.onRestoreInstanceState(savedInstanceState)
    }

    fun onDigit(view: View) {
        tvInput?.append((view as Button).text)
        isLastCharNumeric = true
        isLastCharDot = false
    }

    fun onClear(view: View) {
        tvInput?.text = ""
    }

    fun onBackspace(view: View) {
        tvInput?.text = tvInput?.text?.dropLast(1)
        if (tvInput?.text?.toString()?.length == 0) {
            isLastCharNumeric = false
            isLastCharDot = false
            return
        }
        val lastChar = tvInput?.text?.toString()?.last() ?: return
        if (isValueOperator(lastChar)) {
            isLastCharNumeric = false
            isLastCharDot = false
        } else if (lastChar.equals(".")) {
            isLastCharNumeric = false
            isLastCharDot = true
        } else {
            isLastCharNumeric = true
            isLastCharDot = false
        }
    }

    fun onDecimalPoint(view: View) {
        if (isLastCharNumeric && !isLastCharDot) {
            tvInput?.append(".")
            isLastCharNumeric = false
            isLastCharDot = true
        }
    }

    fun onOperator(view: View) {
        tvInput?.text?.let {
            if (isLastCharNumeric && !isOperatorAdded(it.toString())) {
                tvInput?.append((view as Button).text)
                isLastCharDot = false
                isLastCharNumeric = false
            } else if (isLastCharNumeric && isOperatorAdded(it.toString())) {
                onEqual(view)
                tvInput?.append((view as Button).text)
                isLastCharDot = false
                isLastCharNumeric = false
            }
        }
    }

    fun onAdvancedOperator(view: View) {
        tvInput?.text?.let {
            if (isLastCharNumeric) {
                val operator: String = (view as Button).text.toString()
                var tvValue = tvInput?.text.toString().toDouble()
                var result: String? = null

                isLastCharDot = false
                isLastCharNumeric = true
                if (operator == "sin") {
                    result = sin(tvValue).toString()
                } else if (operator == "cos") {
                    result = cos(tvValue).toString()
                } else if (operator == "tan") {
                    result = tan(tvValue).toString()
                } else if (operator == "ln") {
                    result = ln(tvValue).toString()
                } else if (operator == "sqrt") {
                    result = sqrt(tvValue).toString()
                } else if (operator == "x^2") {
                    result = tvValue.pow(2).toString()
                } else if (operator == "x^y") {
                    tvInput?.append("^")
                    isLastCharNumeric = false
                    return
                } else if (operator == "log") {
                    tvInput?.append("log")
                    isLastCharNumeric = false
                    return
                }
                tvInput?.text = result?.let { it1 -> removeZeroAfterDot(it1) }
            }
        }
    }

    fun onEqual(view: View) {
        if (isLastCharNumeric) {
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try {
                if (tvValue.startsWith("-")) {
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }
                if (tvValue.contains("-")) {
                    val splitValue = tvValue.split("-")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }

                    tvInput?.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                } else if (tvValue.contains("+")) {
                    val splitValue = tvValue.split("+")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }

                    tvInput?.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                } else if (tvValue.contains("/")) {
                    val splitValue = tvValue.split("/")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (two == '0'.toString()) {
                        Toast.makeText(
                            applicationContext,
                            "Dividing by 0 is not permitted! Choose another number.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }

                    tvInput?.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                } else if (tvValue.contains("*")) {
                    val splitValue = tvValue.split("*")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }

                    tvInput?.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                } else if (tvValue.contains("^")) {
                    val splitValue = tvValue.split("^")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }
                    tvInput?.text = removeZeroAfterDot((one.toDouble().pow(two.toDouble())).toString())
                } else if (tvValue.contains("log")) {
                    val splitValue = tvValue.split("log")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (prefix.isNotEmpty()) {
                        one = prefix + one
                    }
                    tvInput?.text = removeZeroAfterDot(log(one.toDouble(), (two.toDouble())).toString())
                }

            } catch (e: java.lang.ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    fun onChangeSign(view: View) {
        if (isLastCharNumeric) {
            var value = tvInput?.text.toString().toDouble()
            value = if (value > 0) -value else abs(value)
            tvInput?.text = removeZeroAfterDot(value.toString())
        }
    }

    private fun removeZeroAfterDot(result: String): String {
        var value = result
        if (value.contains(".0")) {
            value = result.substring(0, result.length - 2)
        }
        return value
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")
        }
    }

    private fun isValueOperator(value: Char?): Boolean {
        if (value == null) {
            return false
        }
        return value.equals("/")
                || value.equals("*")
                || value.equals("+")
                || value.equals("-")
    }

}