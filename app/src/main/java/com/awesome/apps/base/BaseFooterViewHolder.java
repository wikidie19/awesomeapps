package com.awesome.apps.base;

import android.view.View;

public abstract class BaseFooterViewHolder extends BaseViewHolder {

    public BaseFooterViewHolder(View itemView) {
        super(itemView, null);
    }

    public abstract void show();
    public abstract void hide();

}