package com.neuifo.mangareptile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;
import com.neuifo.mangareptile.R;
import com.neuifo.mangareptile.data.core.AppModule;

import org.jetbrains.annotations.NotNull;


/**
 * <strong>描述: </strong> Toast的工具类。
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/3/30  上午10:34
 * <p><strong>版本: </strong> v 1.0.0
 */

public class ToastUtil {
    /**
     * 用来获取Toast的文本内容的键。
     */
    private static final String TOAST_CONTENT = "type";
    /**
     * 用来获取Toast的显示时间的键。
     */
    private static final String TOAST_DURATION = "duration";
    /**
     * 用来获取Toast的布局资源的键。
     */
    private static final String TOAST_LAYOUT = "toast_layout";
    /**
     * 用来获取Toast的图标的键。
     */
    private static final String TOAST_ICON = "toast_icon";
    /**
     * Toast对象。
     */
    private static Toast sToast;


    /**
     * Handler对象，用来处理子线程的Toast。
     */
    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            getToastInstance(bundle.getInt(TOAST_DURATION),
                    bundle.getInt(TOAST_LAYOUT),
                    bundle.getString(TOAST_CONTENT),
                    bundle.getInt(TOAST_ICON)).show();
        }
    };

    //私有构造函数，防止别人创建本类对象
    private ToastUtil() {
    }

    public static void showLongToast(@StringRes int text) {
        showLongToast(getContext().getString(text));
    }

    public static void showLongToast(String text) {
        doOnShowToast(text, Toast.LENGTH_LONG, R.layout.toast_system_toast_layout, 0);
    }

    public static void showShortToast(@StringRes int text) {
        showShortToast(getContext().getString(text));
    }

    public static void showShortToast(String text) {
        doOnShowToast(text, Toast.LENGTH_SHORT, R.layout.toast_system_toast_layout, 0);
    }

    public static void showSuccessToast(@StringRes int text) {
        showSuccessToast(getContext().getString(text));
    }

    public static void showSuccessToast(String text) {
        doOnShowToast(text, Toast.LENGTH_SHORT, R.layout.toast_custom_toast_layout, R.drawable.ic_toast_success);
    }

    public static void showAlertToast(@StringRes int text) {
        showAlertToast(getContext().getString(text));
    }

    public static void showAlertToast(String text) {
        doOnShowToast(text, Toast.LENGTH_SHORT, R.layout.toast_custom_toast_layout, R.drawable.ic_toast_alert);
    }

    public static void showErrorToast(@StringRes int text) {
        showErrorToast(getContext().getString(text));
    }

    public static void showErrorToast(String text) {
        doOnShowToast(text, Toast.LENGTH_SHORT, R.layout.toast_custom_toast_layout, R.drawable.ic_toast_alert);
    }

    public static void showCustomToast(String text, @DrawableRes int icon) {
        doOnShowToast(text, Toast.LENGTH_SHORT, R.layout.toast_custom_toast_layout, icon);
    }

    /**
     * 向Handle发送消息执行弹出Toast
     *
     * @param text        要提示的文本
     * @param duration    Toast的时长
     * @param toastLayout toast的布局。
     */
    private static void doOnShowToast(String text, int duration, @LayoutRes int toastLayout, @DrawableRes int icon) {
        //如果是主线程直接showToast。
        if (TextUtils.equals("main", Thread.currentThread().getName())) {
            getToastInstance(duration, toastLayout, text, icon).show();
        } else {//如果是子线程则抛到主线程去showToast。
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString(TOAST_CONTENT, text);
            bundle.putInt(TOAST_DURATION, duration);
            bundle.putInt(TOAST_LAYOUT, toastLayout);
            bundle.putInt(TOAST_ICON, toastLayout);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
    }

    @SuppressLint("ShowToast")
    private static Toast getToastInstance(int duration, int toastLayout, String text, @DrawableRes int icon) {
        if (sToast == null) {
            sToast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);
        }
        LayoutInflater inflate = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflate == null) {
            inflate = LayoutInflater.from(getContext());
        }

        sToast.setView(inflate.inflate(toastLayout, null));
        ImageView ivImage = sToast.getView().findViewById(R.id.ivToastIcon);
        if (ivImage != null) {
            if (icon != 0) {
                ivImage.setVisibility(View.VISIBLE);
                ivImage.setImageResource(icon);
            } else {
                ivImage.setVisibility(View.GONE);
            }
            sToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            sToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
                    0,
                    getContext().getResources().getDimensionPixelSize(R.dimen.toastYOffset));
        }
        sToast.setDuration(duration);
        sToast.setText(text);
        return sToast;
    }

    private static Context getContext() {
        return AppModule.INSTANCE.getContext().getApplicationContext();
    }

    /**
     * 取消Toast的显示。
     */
    public static void cancel() {
        sToast.cancel();
    }

    public static void showSuccessSnackBar(@NotNull View anchorView, @NotNull CharSequence text, int delay) {
        if (delay > 100) {
            anchorView.postDelayed(() -> showSuccessSnackBar(anchorView, text), delay);
        } else {
            showSuccessSnackBar(anchorView, text);
        }
    }

    public static void showSuccessSnackBar(@NotNull View anchorView, @NotNull CharSequence text) {
        Snackbar snackbar = Snackbar.make(anchorView, text, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout sl = (Snackbar.SnackbarLayout) snackbar.getView();
        sl.setPadding(0, 0, 0, 0);
        sl.getLayoutParams().height = MeasureUtil.dp2px(anchorView.getContext(), 140);
        sl.removeAllViews();
        LayoutInflater.from(anchorView.getContext()).inflate(R.layout.layout_custom_snackbar_of_success, sl, true);
        ((TextView) sl.findViewById(R.id.tvMsg)).setText(text);
        snackbar.show();
    }
}
