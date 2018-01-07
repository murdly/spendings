package com.akarbowy.spendingsapp.ui.transaction;

import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;

class TransactionDataValidator {

    private final String errorMessage;

    private final TextInputEditText[] inputs;

    TransactionDataValidator(TextInputEditText[] inputs, String errorMessage) {
        this.inputs = inputs;
        this.errorMessage = errorMessage;

        watchAndShowErrorWhenInvalid();
    }

    public boolean validate() {
        boolean isValid = true;
        for (TextInputEditText input : inputs) {
            if (!validateInput(input)) {
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean validateInput(TextInputEditText input) {
        boolean isValid = input.getText() != null && !input.getText().toString().isEmpty();
        input.setError(isValid ? null : errorMessage);
        return isValid;
    }

    private void watchAndShowErrorWhenInvalid() {
        for (TextInputEditText input : inputs) {
            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    validateInput(input);
                }
            });
        }
    }

}
