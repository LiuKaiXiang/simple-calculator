package com.gongyu.smile.simple_calculator;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class KeyboardView extends FrameLayout {

    private OnBtnClickListener listener;

    private String symbol = "+-×÷";

    private String prevText = null;

    public KeyboardView(@NonNull Context context) {
        this(context, null);
    }

    public KeyboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_keyboard, this);

        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_symbol_bracket_left, R.id.btn_symbol_bracket_right, R.id.btn_rollback,
            R.id.btn_clear, R.id.btn_symbol_add, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3,
            R.id.btn_symbol_subtract, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6,
            R.id.btn_symbol_multiply, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9,
            R.id.btn_symbol_divide, R.id.btn_number_dot, R.id.btn_number_0, R.id.btn_calculate})
    public void onViewClicked(View view) {
        if (listener == null) return;
        switch (view.getId()) {
            case R.id.btn_rollback:
                listener.onEditBtnClick(OnBtnClickListener.EDIT_CODE_ROLLBACK);
                break;
            case R.id.btn_clear:
                listener.onEditBtnClick(OnBtnClickListener.EDIT_CODE_CLEAR);
                break;
            case R.id.btn_calculate:
                listener.onEditBtnClick(OnBtnClickListener.EDIT_CODE_CALCULATE);
                break;
            case R.id.btn_symbol_bracket_left:
            case R.id.btn_symbol_bracket_right:
            case R.id.btn_symbol_add:
            case R.id.btn_symbol_subtract:
            case R.id.btn_symbol_multiply:
            case R.id.btn_symbol_divide:
                String symbol = ((Button) view).getText().toString();
                if (canInputSymbol(symbol)) {
                    outputText(symbol);
                }
                break;
            case R.id.btn_number_0:
            case R.id.btn_number_1:
            case R.id.btn_number_2:
            case R.id.btn_number_3:
            case R.id.btn_number_4:
            case R.id.btn_number_5:
            case R.id.btn_number_6:
            case R.id.btn_number_7:
            case R.id.btn_number_8:
            case R.id.btn_number_9:
            case R.id.btn_number_dot:
                String number = ((Button) view).getText().toString();
                if (canInputNumber(number)) {
                    outputText(number);
                }
                break;
        }
    }

    /**
     * 是否可以输入运算符
     *
     * @param s
     * @return
     */
    private boolean canInputSymbol(String s) {
        if (TextUtils.isEmpty(prevText)) {
            return true;
        }

        if (")".equals(prevText) && !symbol.contains(s)) {
            return true;
        }
        if ("(".equals(s) && symbol.contains(prevText)) {
            return true;
        }
        if (symbol.contains(prevText)) {
            return "(".equals(s) || ")".equals(s);
        }
        return true;
    }

    /**
     * 是否可以输入数字或小数点
     *
     * @param currentText
     * @return
     */
    private boolean canInputNumber(String currentText) {
        if (TextUtils.isEmpty(prevText)) {
            return true;
        }

        if (")".equals(prevText)) {
            return false;
        }
        if (currentText.equals(".") && ".".equals(prevText)) {
            return false;
        }
        return true;
    }

    private void outputText(String text) {
        prevText = text;
        listener.onOperationBtnClick(prevText);
    }

    public void setOnBtnClickListener(OnBtnClickListener listener) {
        this.listener = listener;
    }

    public interface OnBtnClickListener {
        int EDIT_CODE_ROLLBACK = 1;
        int EDIT_CODE_CLEAR = 2;
        int EDIT_CODE_CALCULATE = 3;

        void onOperationBtnClick(String text);

        void onEditBtnClick(int editCode);//editCode: 1:回退，2：清空，3：求值
    }
}
