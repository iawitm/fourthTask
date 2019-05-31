package com.user.fourthtask.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateDialog extends DialogFragment {

    public Calendar dateAndTime = Calendar.getInstance();
    public DatePickerDialog dialog;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        dateAndTime = Calendar.getInstance();

        if (savedInstanceState != null){
            dateAndTime = (Calendar) savedInstanceState.getSerializable("CALENDAR_");
        }

        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setDate();
                ((MainActivity) getActivity()).setDate(simpleDateFormat.format(dateAndTime.getTime()));
            }
        };

        dialog = new DatePickerDialog(getActivity(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setDate();
        outState.putSerializable("CALENDAR_", dateAndTime);
        super.onSaveInstanceState(outState);
    }

    public void setDate(){
        dateAndTime.set(Calendar.YEAR, dialog.getDatePicker().getYear());
        dateAndTime.set(Calendar.MONTH, dialog.getDatePicker().getMonth());
        dateAndTime.set(Calendar.DAY_OF_MONTH, dialog.getDatePicker().getDayOfMonth());
    }
}
