package com.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nytimessearch.R;
import com.nytimessearch.models.FilterWrapper;
import com.nytimessearch.utils.DateUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class FilterFragment extends DialogFragment {

    private TextView tvBeginDate;
    private Spinner spSortOrder;
    private CheckBox cbArts;
    private CheckBox cbFashionStyle;
    private CheckBox cbSports;
    private Button btnSave;

    public interface FilterDialogListener {
        void onFinishEditDialog(FilterWrapper filter);
    }

    public FilterFragment() {

    }

    public static FilterFragment newInstance(FilterWrapper filter) {

        FilterFragment frag = new FilterFragment();

        Bundle args = new Bundle();
        if(filter != null) {
            args.putString("beginDate", filter.getBeginDate());
            args.putString("sort", filter.getSort());
            args.putBoolean("arts", filter.isArts());
            args.putBoolean("fashionStyle", filter.isFashionStyle());
            args.putBoolean("sports", filter.isSports());
        }
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.diagfrag_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBeginDate = (TextView) view.findViewById(R.id.tv_begin_date_value);
        if(getArguments().getString("beginDate")!=null &&
                !getArguments().getString("beginDate").isEmpty()) {
            tvBeginDate.setText(getArguments().getString("beginDate"));
        }

        tvBeginDate.setOnClickListener(v -> {

            // Get current date
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            // Create a date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    R.style.AppTheme, datePickerListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.setCancelable(true);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            Window window = datePickerDialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            datePickerDialog.show();
        });

        spSortOrder = (Spinner) view.findViewById(R.id.sp_sort_order);
        if(getArguments().getString("sort")!=null) {
            setSpinnerToValue(spSortOrder,getArguments().getString("sort"));
        }

        cbArts = (CheckBox) view.findViewById(R.id.cb_arts);
        if(getArguments().getBoolean("arts")) {
            cbArts.setChecked(true);
        }
        cbFashionStyle = (CheckBox) view.findViewById(R.id.cb_fashion_style);
        if(getArguments().getBoolean("fashionStyle")) {
            cbFashionStyle.setChecked(true);
        }
        cbSports = (CheckBox) view.findViewById(R.id.cb_sports);
        if(getArguments().getBoolean("sports")) {
            cbSports.setChecked(true);
        }

        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            FilterDialogListener listener = (FilterDialogListener) getActivity();
            listener.onFinishEditDialog(new FilterWrapper(tvBeginDate.getText().toString(),
                    spSortOrder.getSelectedItem().toString(),
                    cbArts.isChecked(),
                    cbFashionStyle.isChecked(),
                    cbSports.isChecked()));
            dismiss();
        });
    }

    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String beginDate = DateUtils.createBeginDateString(year, dayOfMonth, month);
                    tvBeginDate.setText(beginDate);
                }
            };


    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }


    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.6), (int) (size.x * 0.9));
        window.setGravity(Gravity.CENTER);
        // Calling super onResume after sizing
        super.onResume();

    }
}
