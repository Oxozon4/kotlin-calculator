package com.oxozon.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class AdvancedCalculator : AppCompatActivity() {
    private var tvInput: TextView? = null
    private var isLastCharNumeric: Boolean = false
    private var isLastCharDot: Boolean = false
    private var isLastCharBackspace: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_calculator)
        tvInput = findViewById(R.id.tvInput)
    }

    fun onDigit(view: View) {
        tvInput?.append((view as Button).text)
        isLastCharNumeric = true
        isLastCharDot = false
        isLastCharBackspace = false
    }

    fun onClear(view: View) {
        tvInput?.text = ""
    }

    fun onBackspace(view: View) {
        if (isLastCharBackspace) {
            onClear(view)
            isLastCharBackspace = false
        } else {
            tvInput?.text = tvInput?.text?.dropLast(1)
            isLastCharBackspace = true
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
                isLastCharBackspace = false
            } else if (isLastCharNumeric && isOperatorAdded(it.toString())) {
                onEqual(view)
                tvInput?.append((view as Button).text)
                isLastCharDot = false
                isLastCharNumeric = false
                isLastCharBackspace = false
            }
        }

    }

    fun onEqual(view: View) {
        if (isLastCharNumeric) {
            var tvValue = tvInput?.text.toString()
            var prefix = ""
            isLastCharBackspace = false

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
                }

            } catch (e: java.lang.ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    private fun onChangeSign(view: View) {

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