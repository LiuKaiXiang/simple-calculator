package com.gongyu.smile.simple_calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gongyu.smile.simple_calculator.operation.InfixToSuffixUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_equation_left)
    TextView tvEquationLeft;
    @BindView(R.id.tv_equation_fight)
    TextView tvEquationFight;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.ckv_keyboard)
    KeyboardView ckvKeyboard;

    private String operation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        ckvKeyboard.setOnBtnClickListener(new KeyboardView.OnBtnClickListener() {
            @Override
            public void onOperationBtnClick(String text) {
                operation += text;
                tvEquationLeft.setText(operation);
            }

            @Override
            public void onEditBtnClick(int editCode) {
                if (editCode == EDIT_CODE_CALCULATE) {
                    calculate();
                    return;
                }

                if (editCode == EDIT_CODE_CLEAR) {
                    operation = "";
                } else if (editCode == EDIT_CODE_ROLLBACK && !TextUtils.isEmpty(operation)) {
                    operation = operation.substring(0, operation.length() - 1);
                }
                tvEquationLeft.setText(operation);
            }
        });
    }

    private void calculate() {
        InfixToSuffixUtils utils = new InfixToSuffixUtils();
        tvEquationFight.setText(utils.infixToSuffix(operation));
        tvResult.setText(utils.getResult());
    }
}
