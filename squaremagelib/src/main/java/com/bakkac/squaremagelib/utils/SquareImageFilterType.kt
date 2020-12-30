package com.bakkac.squaremagelib.utils

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter

enum class SquareImageFilterType(val filterType: GPUImageFilter) {
    TOONFILTER(GPUImageToonFilter()), GRAYSCALE(GPUImageGrayscaleFilter())
}
