package com.lhd.setpin;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lhd.applock.R;
import com.lhd.lockpin.LockPinPresenter;
import com.lhd.lockpin.LockPinPresenterImpl;
import com.lhd.main.MainActivity;
import com.lhd.module.Config;
import com.lhd.module.MyLog;

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
        password1 = "";
        password2 = "";
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
                Log.e("setpin", pin);
                Log.e("setpin", pin.length()+"");
                if (pin.length() == 4 && password1.equals("")) {
                    txtPin.setText("");
                    txtPin.setHint(getResources().getString(R.string.set_pin_confirm_pin_code1_2));
                    password1 = pin;
                } else if (pin.length() == 4 && !password1.isEmpty()) {
                    password2 = pin;
                    if (password1.equals(password2)) {
                        byte[]   bytesEncoded = Base64.encode(password2.getBytes(),101);
                        Log.e("setpin", "Giống nhau "+new String(bytesEncoded ));
                        txtPin.setText("");
                        MyLog.putStringValueByName(getContext(), Config.LOG_APP,Config.PIN_CODE,new String(bytesEncoded ));
                        ((MainActivity)getActivity()).startSettingFragment();
                    } else {
                        Log.e("setpin", "Khác nhau");
                        password1 = "";
                        password2 = "";
                        txtPin.setHint(getResources().getString(R.string.set_pin_confirm_pin_code));
                        Toast.makeText(getContext(),getResources().getString(R.string.set_pin_fail_pin_code),Toast.LENGTH_SHORT).show();

                    }
                }
                //  setPinPresenter.checkPassCode();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    String pin = "";
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
