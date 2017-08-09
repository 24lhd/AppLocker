package com.lhd.setpin;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.lhd.applock.R;
import com.lhd.lockpin.LockPinPresenter;
import com.lhd.lockpin.LockPinPresenterImpl;

/**
 * Created by D on 8/9/2017.
 */

public class SetPinFragment extends Fragment implements SetPinView, View.OnClickListener {
    private WindowManager windowManager;
    private View view;
    WindowManager.LayoutParams params;
    private LockPinPresenter lockPinPresenter;
    private SetPinPresenter setPinPresenter;
    private EditText txtPin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.set_pin_layout, null));
    }

    @Override
    public View initView(View view) {
        lockPinPresenter = new LockPinPresenterImpl(getContext());
        setPinPresenter = new SetPinPresenterImpl(getContext());
        txtPin = (EditText) view.findViewById(R.id.set_pin_txt_input_code);
        txtPin.setText("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txtPin.setShowSoftInputOnFocus(true);
        }
        txtPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pin = charSequence.toString();
                if (pinInPut.length() == 4){
                    password1=pin;
                }
                  //  setPinPresenter.checkPassCode();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    String pinInPut = "";
    String password1 = "";
    String password2 = "";

    @Override
    public String getPinInput() {
        return null;
    }

    @Override
    public void showError(String s) {

    }

    @Override
    public void pass() {

    }

    @Override
    public void onClick(View view) {

    }

}
