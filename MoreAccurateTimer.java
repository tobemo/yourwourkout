package tobemo.yourworkout;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Schedule a countdown until a time in the future, with
 * regular notifications on intervals along the way.
 *
 * Eddited accordong to Sam on stackoverflow
 *
 * Example of showing a 30 second countdown in a text field:
 *
 * <pre class="prettyprint">
 * new MoreAccurateTimer(30000, 1000) {
 *
 *     public void onTick(long millisUntilFinished) {
 *         mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
 *     }
 *
 *     public void onFinish() {
 *         mTextField.setText("done!");
 *     }
 *  }.start();
 * </pre>
 *
 * The calls to {@link #onTick(long)} are synchronized to this object so that
 * one call to {@link #onTick(long)} won't ever occur before the previous
 * callback is complete.  This is only relevant when the implementation of
 * {@link #onTick(long)} takes an amount of time to execute that is significant
 * compared to the countdown interval.
 */
public abstract class MoreAccurateTimer {
    /**
     * Millis since epoch when alarm should stop.
     */
    private final long mMillisInFuture;
    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval;
    private long mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;

    private long mNextTime;
    /**
     * @param millisInFuture The number of millis in the future from the call
     *   to {@link #start()} until the countdown is done and {@link #onFinish()}
     *   is called.
     * @param countDownInterval The interval along the way to receive
     *   {@link #onTick(long)} callbacks.
     */
    public MoreAccurateTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }
    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }
    /**
     * Start the countdown.
     */
    public synchronized final MoreAccurateTimer start() {
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }

        mNextTime = SystemClock.uptimeMillis();
        mStopTimeInFuture = mNextTime + mMillisInFuture;

        mNextTime += mCountdownInterval;
        mHandler.sendMessageAtTime(mHandler.obtainMessage(MSG), mNextTime);
        return this;
    }
    /**
     * Callback fired on regular interval.
     * @param millisLeftUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisLeftUntilFinished);
    /**
     * Callback fired when the time is up.
     */
    public abstract void onFinish();
    private static final int MSG = 1;
    // handles counting down
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (MoreAccurateTimer.this) {
                final long millisLeft = mStopTimeInFuture - SystemClock.uptimeMillis();

                if (millisLeft <= 0) {
                    onFinish();
                } else {
                    onTick(millisLeft);

                    // Calculate next tick by adding the countdown interval from the original start time
                    // If user's onTick() took too long, skip the intervals that were already missed
                    long currentTime = SystemClock.uptimeMillis();
                    do {
                        mNextTime += mCountdownInterval;
                    } while (currentTime > mNextTime);

                    // Make sure this interval doesn't exceed the stop time
                    if(mNextTime < mStopTimeInFuture)
                        sendMessageAtTime(obtainMessage(MSG), mNextTime);
                    else
                        sendMessageAtTime(obtainMessage(MSG), mStopTimeInFuture);
                }
            }
        }
    };
}
