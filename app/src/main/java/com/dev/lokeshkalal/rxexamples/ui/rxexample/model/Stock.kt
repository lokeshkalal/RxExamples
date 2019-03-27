package com.dev.lokeshkalal.rxexamples.ui.rxexample.model

data class Stock(val ticker: String, val price: Double) {

    override fun toString(): String {
        return String.format("%s - %s", ticker, price)
    }
}
