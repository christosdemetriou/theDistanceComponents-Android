package uk.co.thedistance.components.time;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class RangeTimePickerDialog extends TimePickerDialog {

    private final Calendar calendar = Calendar.getInstance();
    private final Calendar calendar2 = Calendar.getInstance();
    private TimeZone timeZone = TimeZone.getTimeZone("UTC");
    private final OnTimeSetListener mListener;
    private Date mMinTime;
    private Date mMaxTime;
    private int interval = 1;
    private int maxIntervals = 60;
    private TimePicker mPicker;

    public RangeTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
        mListener = listener;
        calendar.set(0, 0, 0, hourOfDay, minute, 0);
        init();
    }

    public RangeTimePickerDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, themeResId, listener, hourOfDay, minute, is24HourView);
        mListener = listener;
        calendar.set(0, 0, 0, hourOfDay, minute, 0);
        init();
    }

    private void init() {
        calendar2.set(0, 0, 0, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        setTimeZone(timeZone);
    }

    private void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        calendar.setTimeZone(timeZone);
        calendar2.setTimeZone(timeZone);
    }

    Handler updateHandler = new Handler();

    class UpdateRunnable implements Runnable {
        int hour;
        int minute;

        public UpdateRunnable(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        @Override
        public void run() {
            if (mPicker == null) {
                return;
            }
            mPicker.post(new Runnable() {
                @Override
                public void run() {
                    updateTime(0, 1);
                    updateTime(hour, minute);
                }
            });
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                fixTime(calendar);
                if (mListener != null) {
                    mListener.onTimeSet(mPicker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    public void fixTime(Calendar calendar) {
        int minute = calendar.get(Calendar.MINUTE);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        float intervalsF = (float) minute / interval;
        int intervals;
        int newMinute;
        int newHour = hourOfDay;

        if ((minute - (Math.floor(intervalsF) * interval)) < ((Math.ceil(intervalsF) * interval) - minute)) {
            intervals = (int) Math.floor(intervalsF);
        } else {
            intervals = (int) Math.ceil(intervalsF);
        }

        while (intervals >= maxIntervals) {
            intervals -= maxIntervals;
        }

        newMinute = intervals * interval;

        calendar.set(Calendar.HOUR_OF_DAY, newHour);
        calendar.set(Calendar.MINUTE, newMinute);

        if (mMinTime != null && calendar.getTime().before(mMinTime)) {
            calendar.setTime(mMinTime);
            newHour = calendar.get(Calendar.HOUR_OF_DAY);
            newMinute = calendar.get(Calendar.MINUTE);
        } else if (mMaxTime != null && calendar.getTime().after(mMaxTime)) {
            calendar.setTime(mMaxTime);
            newHour = calendar.get(Calendar.HOUR_OF_DAY);
            newMinute = calendar.get(Calendar.MINUTE);
        }

        calendar.set(Calendar.HOUR_OF_DAY, newHour);
        calendar.set(Calendar.MINUTE, newMinute);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        super.onTimeChanged(view, hourOfDay, minute);

        if (mPicker == null) {
            mPicker = view;
        }
        updateHandler.removeCallbacksAndMessages(null);

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        fixTime(calendar);

        int newHour = calendar.get(Calendar.HOUR_OF_DAY);
        int newMinute = calendar.get(Calendar.MINUTE);

        if (newHour != hourOfDay || newMinute != minute) {
            updateHandler.postDelayed(new UpdateRunnable(newHour, newMinute), 300);
        }
    }

    public void setMaxTime(int hours, int minutes) {
        calendar2.set(Calendar.HOUR_OF_DAY, hours);
        calendar2.set(Calendar.MINUTE, minutes);
        mMaxTime = calendar2.getTime();
    }

    public void setMinTime(int hours, int minutes) {
        calendar2.set(Calendar.HOUR_OF_DAY, hours);
        calendar2.set(Calendar.MINUTE, minutes);
        mMinTime = calendar2.getTime();
    }

    public void setInterval(int interval) {
        this.interval = interval;
        maxIntervals = 60 / interval;
    }
}
