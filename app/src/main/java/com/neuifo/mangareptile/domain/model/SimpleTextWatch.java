package com.neuifo.mangareptile.domain.model;

import android.text.Editable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

/**
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/5/4  下午1:19
 * <p><strong>版本: </strong> v 1.0.0
 */

public abstract class SimpleTextWatch extends TextWatchImpl {
    private final WeakReference<TextView> mView;

    protected SimpleTextWatch(@NonNull TextView textView) {
        this.mView = new WeakReference<TextView>(textView);
    }

    @Override
    public final void beforeTextChanged(@NotNull CharSequence s, int start, int count, int after) {
        TextView et = mView.get();
        if (et != null) {
            beforeTextChanged(et, s, start, count, after);
        }
    }

    @Override
    public final void afterTextChanged(Editable s) {
        TextView et = mView.get();
        if (et != null) {
            afterTextChanged(et, s);
        }
    }

    protected void beforeTextChanged(@NonNull TextView et, @NotNull CharSequence s, int start, int count, int after) {
    }

    protected abstract void afterTextChanged(@NonNull TextView et, @NonNull Editable s);
}
