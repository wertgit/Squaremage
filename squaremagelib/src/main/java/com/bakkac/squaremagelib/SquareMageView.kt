package com.bakkac.squaremagelib

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.bakkac.squaremagelib.utils.SquareImageFilterType
import com.bakkac.squaremagelib.utils.SquareMultiplierType
import com.bakkac.squaremagelib.utils.px
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import java.util.*


class SquareMageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mSqaureSpacing = 50.px()
    var mSquareMultiplier = 2 // Default 2by2
    private var mSquareStrokePaint: Paint? = null
    private var mSquareFillPaint: Paint? = null
    private var mImageBitmap: Bitmap? = null
    private var mImageFilter: GPUImageFilter? = null
    private var mSquaresArray: ArrayList<Array<Rect>>
    private var mImagePosition: Pair<Int, Int>

    init {

        mSquareStrokePaint = Paint().apply {
            color = Color.BLACK
            isAntiAlias = true
            strokeWidth = 20.px().toFloat()
            style = Paint.Style.STROKE
        }

        mSquareFillPaint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        mImageBitmap = null
        mImageFilter = null
        mImagePosition = Pair(0, 0)
        mSquaresArray = initialiseMultiArray(mSquareMultiplier, mSquareMultiplier)

    }

    /**
     *  function intializes multidimensional array by setting the size and intial value.
     *  The multidimensional array is of size size1 by size2 and the intial value assigned is 0
     */
    private fun initialiseMultiArray(rows: Int, cols: Int): ArrayList<Array<Rect>> {
        val output: ArrayList<Array<Rect>> = arrayListOf()
        for (i in 0 until rows) {
            var array = arrayOf<Rect>()
            for (j in 0 until cols) {
                array += Rect()  // assigns zero as intial value
            }
            output += array
        }
        return output
    }

    private fun drawSquares(canvas: Canvas?) {

        val viewWidth = width - mSqaureSpacing.times(2)
        val viewHeight = height - mSqaureSpacing.times(2)
        val squareWidth = viewWidth / mSquareMultiplier
        val squareHeight = viewHeight / mSquareMultiplier

        // Fill the entire canvas with a background color.
        canvas?.drawColor(Color.LTGRAY)

        //canvas?.drawCircle(halfWidth.toFloat()/2+sqaureSpacing,halfHeight.toFloat()/2+sqaureSpacing,(halfWidth.toFloat()/2),squarePaint!!)

        drawSquare(
            mSquareStrokePaint,
            mSquareFillPaint,
            canvas,
            squareWidth,
            squareHeight
        )
    }

    private fun drawBitmap(square: Rect, canvas: Canvas?) {
        mImageBitmap?.run {
            canvas?.drawBitmap(this, square, square, null)
        }
    }

    /**
     * Performs filter on a bitmap passed and returns a filtered one
     */
    private fun getFilteredBitmap(
        bitmap: Bitmap,
        filter: GPUImageFilter,
        context: Context
    ): Bitmap {
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        gpuImage.setFilter(filter)
        return gpuImage.bitmapWithFilterApplied
    }

    private fun drawSquare(
        strokePaint: Paint?,
        fillPaint: Paint?,
        canvas: Canvas?,
        width: Int,
        height: Int
    ) {

        val spaceMultiplier = 2
        var leftPadding = 0
        var topPadding = 0

        for (row in 0 until mSquareMultiplier) {

            for (col in 0 until mSquareMultiplier) {

                /**
                 * Note: Draws Grid for Debug Purposes
                 */
//                val  paddingPaint = Paint().apply {
//                    setColor(Color.RED)
//                    setStrokeWidth(5F)
//                    setStyle(Paint.Style.STROKE)
//                }
//                val gridGuide = Rect(
//                    leftPadding + mSqaureSpacing,
//                    topPadding + mSqaureSpacing,
//                    leftPadding + width + mSqaureSpacing,
//                    topPadding + height + mSqaureSpacing
//                )
//                canvas?.drawRect(gridGuide, paddingPaint!!)


                val square = Rect(
                    leftPadding + mSqaureSpacing.times(spaceMultiplier),
                    topPadding + mSqaureSpacing.times(
                        spaceMultiplier
                    ),
                    leftPadding + width,
                    topPadding + height
                )

                strokePaint?.let { paint ->
                    canvas?.drawRect(square, paint)
                }
                fillPaint?.let { paint ->
                    canvas?.drawRect(square, paint)
                }

                // Add Square to Squares list
                if (!mSquaresArray.isNullOrEmpty())
                    mSquaresArray[row][col] = square

                leftPadding += width
            }
            topPadding += height
            leftPadding = 0 // reset left padding per row draw

        }
    }

    // Called when the view should render its content.
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSquares(canvas)
        drawBitmap(getDrawnSquare(mImagePosition), canvas)
        mImageFilter?.let {
            performFilter(it, mImageBitmap, canvas, getDrawnSquare(mImagePosition))
        }

    }

    private fun performFilter(
        filter: GPUImageFilter,
        bitmap: Bitmap?,
        canvas: Canvas?,
        square: Rect
    ) {
        bitmap?.let {
            val filteredBitmap = getFilteredBitmap(it, filter, context)
            canvas?.drawBitmap(filteredBitmap, square, square, null)
        }
    }

    fun getDrawnSquare(position: Pair<Int, Int>): Rect {
        return if (mSquaresArray.size <= 1) {
            mSquaresArray[0][0] // return the first square
        } else {
            // returns a square from a given dimensional position
            mSquaresArray[position.first][position.second]
        }
    }


    fun applyImageFilter(filterSquare: SquareImageFilterType) {
        mImageFilter?.let {
            // only set filter if its different from the currently set
            if (it.javaClass != filterSquare.javaClass) {
                mImageFilter = filterSquare.filterType
                refreshView()
            }
        } ?: run {
            // if null set the filter
            mImageFilter = filterSquare.filterType
            refreshView()
        }
    }

    fun setSquareMultiplier(value: SquareMultiplierType) {
        val multiplierValue = value.multiplierValue
        if (mSquareMultiplier != multiplierValue) {
            mSquareMultiplier = multiplierValue
            mSquaresArray = initialiseMultiArray(multiplierValue, multiplierValue)
            mImageBitmap = null
            refreshView()
        }
    }

    fun loadImage(bitmap: Bitmap?) {
        bitmap?.let {
            mImageBitmap = it  // set global bitmap to display image
            mImageFilter = null  // reset any current filters
            refreshView()
        }
    }

    private fun refreshView() {
        this.invalidate()   // refresh view to call onDraw
    }
}