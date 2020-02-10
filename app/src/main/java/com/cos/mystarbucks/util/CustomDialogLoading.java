package com.cos.mystarbucks.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.cos.mystarbucks.R;

public class CustomDialogLoading extends Dialog {
    private ImageView imageView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_loading);

        imageView = findViewById(R.id.iv_loading);
        Glide.with(context).load(R.drawable.loading).into(imageView);
    }

    public CustomDialogLoading(@NonNull Context context) {
        super(context);
        this.context = context;

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCancelable(false);
    }
}
