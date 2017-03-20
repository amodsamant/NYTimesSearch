package com.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.nytimessearch.R;
import com.nytimessearch.databinding.DiagfragFilterBinding;
import com.nytimessearch.models.FilterWrapper;
import com.nytimessearch.utils.DateUtils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Modal overlay for setting filters
 *
 */
public class FilterFragment extends DialogFragment {

    //Store the binding
    private DiagfragFilterBinding binding;

    public interface FilterDialogListener {
        void onFinishEditDialog(FilterWrapper filter);
    }

    public FilterFragment() {}

    // Function to create new instance of fragment
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
        binding = DataBindingUtil.inflate(inflater,R.layout.diagfrag_filter,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments().getString("beginDate")!=null &&
                !getArguments().getString("beginDate").isEmpty()) {
            binding.tvBeginDateValue.setText(getArguments().getString("beginDate"));
        }

        binding.tvBeginDateValue.setOnClickListener(v -> {
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

        if(getArguments().getString("sort")!=null) {
            setSpinnerToValue(binding.spSortOrder,getArguments().getString("sort"));
        }

        if(getArguments().getBoolean("arts")) {
            binding.cbArts.setChecked(true);
        }
        if(getArguments().getBoolean("fashionStyle")) {
            binding.cbFashionStyle.setChecked(true);
        }
        if(getArguments().getBoolean("sports")) {
            binding.cbSports.setChecked(true);
        }

        binding.btnSave.setOnClickListener(v -> {
            FilterDialogListener listener = (FilterDialogListener) getActivity();
            listener.onFinishEditDialog(new FilterWrapper(
                    binding.tvBeginDateValue.getText().toString(),
                    binding.spSortOrder.getSelectedItem().toString(),
                    binding.cbArts.isChecked(),
                    binding.cbFashionStyle.isChecked(),
                    binding.cbSports.isChecked()));
            dismiss();
        });
    }

    // Date picker listener to get the date in appropriate format
    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String beginDate = DateUtils.createBeginDateString(year, dayOfMonth, month);
                    binding.tvBeginDateValue.setText(beginDate);
                }
            };

    /**
     * Set the spinner to its appropriate value when opening the filter modal overlay again
     * @param spinner
     * @param value
     */
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

    /**
     * Display the filter dialog with correct dimensions
     */
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
