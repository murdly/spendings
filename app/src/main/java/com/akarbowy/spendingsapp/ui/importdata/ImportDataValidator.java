package com.akarbowy.spendingsapp.ui.importdata;


import com.akarbowy.spendingsapp.data.external.ImportData;

import java.util.List;

public class ImportDataValidator {

    private final List<ImportData> data;

    public ImportDataValidator(List<ImportData> data) {
        this.data = data;
    }

    public boolean validate(){
        boolean isValid = true;

        for (ImportData d : data) {
            if(!isCategorySet(d)){
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean isCategorySet(ImportData data) {
        return data.isCategoryChosen();
    }
}
