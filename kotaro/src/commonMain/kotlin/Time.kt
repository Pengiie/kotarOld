package dev.pengie.kotaro

object Time {

    /**
     * The time elapsed since the last frame in seconds
     */
    var deltaTime: Float = 0f
        private set

    /**
     * The total application time in seconds
     */
    var time: Float = 0f
        private set

    /**
     * The instantaneous frames per second
     */
    var fps: Int = 0
        private set
    private var lastTime: Float = 0f

    /**
     * Updates application timings, requires window to be initialized beforehand.
     */
    internal fun updateTimings() {
        time = Application.window.getTime().toFloat()

        deltaTime = time - lastTime
        fps = (1f / deltaTime).toInt()

        lastTime = time
    }

}