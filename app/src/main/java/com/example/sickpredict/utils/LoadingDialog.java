package com.example.sickpredict.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {

    private Context context;
    private ProgressDialog progressDialog;

    public LoadingDialog(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
    }

    public void show() {
        progressDialog.show();
    }

    public void hide() {
        progressDialog.dismiss();
    }
}
