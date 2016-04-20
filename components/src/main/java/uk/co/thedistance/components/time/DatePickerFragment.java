package uk.co.thedistance.components.time;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by benbaggley on 14/07/2014.
 */
public class DatePickerFragment extends AppCompatDialogFragment {

    private int themeId = -1;
    private DatePickerDialogCompat.OnDateSetListener listener;
    private Object tag;
    private String title;
    private long minDate = -1;
    private long maxDate = -1;

    public interface OnDialogShownListener {
        void onDialogShown(DatePickerDialogCompat dialog);
    }

    OnDialogShownListener onDialogShownListener;

    public void setOnDialogShownListener(OnDialogShownListener onDialogShownListener) {
        this.onDialogShownListener = onDialogShownListener;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    public void setListener(DatePickerDialogCompat.OnDateSetListener listener) {
        this.listener = listener;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setTheme(@StyleRes int themeId) {
        this.themeId = themeId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DatePickerFragment() {
    }

    public static DatePickerFragment newInstance(@NonNull Calendar calendar) {

        Bundle args = new Bundle();
        args.putSerializable("calendar", calendar);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        Calendar calendar;
        if (getArguments() != null) {
            calendar = (Calendar) getArguments().getSerializable("calendar");
        } else {
            calendar = Calendar.getInstance();
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialogCompat datePickerDialog;
        if (themeId == -1) {
            datePickerDialog = new DatePickerDialogCompat(getActivity(), listener, year, month, day);
        } else {
            datePickerDialog = new DatePickerDialogCompat(getActivity(), themeId, listener, year, month, day);
        }

        DatePicker picker = datePickerDialog.getDatePicker();
        if (tag != null) {
            picker.setTag(tag);
        }
        if (title != null) {
            datePickerDialog.setTitle(title);
        }

        if (minDate != -1) {
            picker.setMinDate(minDate);
        }
        if (maxDate != -1) {
            picker.setMaxDate(maxDate);
        }

        return datePickerDialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (onDialogShownListener != null)
            onDialogShownListener.onDialogShown((DatePickerDialogCompat) getDialog());

    }
}
