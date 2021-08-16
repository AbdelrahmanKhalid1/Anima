package com.ak.otaku_kun.custom;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ak.otaku_kun.R;

import java.util.Calendar;

public class DatePickerCustom extends LinearLayout {
    private ImageView increment, decrement;
    private EditText editText;
    private static final int maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
    private static final int minYear = 1970;
    private int currentYear;

    public DatePickerCustom(Context context) {
        super(context);
        onCreateView();
    }

    public DatePickerCustom(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        onCreateView();
    }

    public DatePickerCustom(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreateView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DatePickerCustom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        onCreateView();
    }

    private void onCreateView(){
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
        setPadding(8,8,8,8);

        //to prevent editText from auto focusing and opening keyboard
        setFocusableInTouchMode(true);

        decrement = new ImageView(getContext());
        decrement.setImageResource(R.drawable.ic_down);
        decrement.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.3f));
        decrement.setOnClickListener(onDecrementClickListener);
        addView(decrement);

        editText = new EditText(getContext());
        editText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.4f));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.CENTER);
        editText.addTextChangedListener(textWatcher);
        addView(editText);


        increment = new ImageView(getContext());
        increment.setImageResource(R.drawable.ic_up);
        increment.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,0.3f));
        increment.setOnClickListener(onIncrementClickListener);
        addView(increment);
    }

    private final OnClickListener onIncrementClickListener = view -> {
        if (currentYear < minYear) setCurrentYear(minYear);
        else if(currentYear < maxYear) setCurrentYear(currentYear + 1);
        else Toast.makeText(getContext(), "Out of Range 1970 - " + maxYear, Toast.LENGTH_SHORT).show();
    };

    private final OnClickListener onDecrementClickListener = view -> {
        if(currentYear > maxYear) setCurrentYear(maxYear);
        else if(currentYear > minYear) setCurrentYear(currentYear - 1);
        else Toast.makeText(getContext(), "Out of Range 1970 - " + maxYear, Toast.LENGTH_SHORT).show();
    };

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            if(!editable.toString().isEmpty())
                currentYear = Integer.parseInt(editable.toString());
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        editText.setEnabled(enabled);
        increment.setEnabled(enabled);
        decrement.setEnabled(enabled);
    }

    public void setCurrentYear(Integer currentYear) {
        if(currentYear == null)
            return;
        this.currentYear = currentYear;
        editText.setText(String.valueOf(currentYear));
    }

    public int getYear(){
        return currentYear;
    }
}
