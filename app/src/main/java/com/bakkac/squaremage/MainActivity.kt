package com.bakkac.squaremage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bakkac.squaremagelib.utils.SquareImageFilterType
import com.bakkac.squaremagelib.utils.SquareMultiplierType
import com.bakkac.squaremagelib.utils.decodeSampledBitmapFromResource
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonFilter1.setOnClickListener {
            customview.applyImageFilter(SquareImageFilterType.GRAYSCALE)
        }

        buttonFilter2.setOnClickListener {
            customview.applyImageFilter(SquareImageFilterType.TOONFILTER)
        }

        button_load_image.setOnClickListener {
            val bitmap =  decodeSampledBitmapFromResource(resources, R.drawable.uganda, customview.getDrawnSquare(Pair(0,0)).width()/customview.mSquareMultiplier,customview.getDrawnSquare(Pair(0,0)).height()/customview.mSquareMultiplier)
            customview.loadImage(bitmap)
        }

        button2by2.setOnClickListener {
            customview.setSquareMultiplier(SquareMultiplierType.TWO_BY_TWO)
        }

        button3by3.setOnClickListener {
            customview.setSquareMultiplier(SquareMultiplierType.THREE_BY_THREE)
        }

    }



}