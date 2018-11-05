package com.muholabs.kotone

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.*
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.button.ButtonInputDriver
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio

private val TAG = MainActivity::class.java.simpleName

class MainActivity : Activity() {

    private lateinit var led :Gpio
    private lateinit var btnA : Button

    private lateinit var buttonA : ButtonInputDriver
    private lateinit var buttonB : ButtonInputDriver
    private lateinit var buttonC : ButtonInputDriver

    private lateinit var buzzer : Speaker
    private lateinit var display: AlphanumericDisplay

    private val freq = hashMapOf(

            KEYCODE_A to 1000.0,
            KEYCODE_B to 3000.0,
            KEYCODE_C to 5000.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUp()

    }

    override fun onDestroy() {
        super.onDestroy()
       // led.close()
       // btnA.close()
        buttonA.unregister()
        buttonB.unregister()
        buttonC.unregister()
        buzzer.close()
        display.close()

    }

    //function to set up my app
    private fun setUp(){

        buttonA =RainbowHat.createButtonAInputDriver(KEYCODE_A)
        buttonB =RainbowHat.createButtonBInputDriver(KEYCODE_B)
        buttonC =RainbowHat.createButtonCInputDriver(KEYCODE_C)
        buzzer =RainbowHat.openPiezo()

        //register the buttons here
        buttonA.register()
        buttonB.register()
        buttonC.register()
       // led =RainbowHat.openLedRed()
       // btnA =RainbowHat.openButtonA()

        //play with the display

        display = RainbowHat.openDisplay()
        display.setEnabled(true)
        display.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
        display.display("PIANO")

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val freqToPlay = freq.get(keyCode)
        if (freqToPlay != null) {
            buzzer.play(freqToPlay)
            return true
        }
        else {
            return false
        }
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        buzzer.stop()
        return true
    }
}
