package com.neuifo.mangareptile.domain.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class DialogHelper extends Dialog {
    private Activity context;

    private DialogHelper(Activity context, int style) {
        super(context, style);
        this.context = context;
    }

    private DialogHelper(Activity context, boolean cancelable,
                         OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    public void dismiss() {
        if (context != null)
            try {
                super.dismiss();
            } catch (Exception e) {

            }
    }

    public static class Builder {

        private Activity mContext;
        private View mContentView;
        private int mContentLayout;
        private ViewGroup.LayoutParams mContentParams;
        private int style;
        // Window默认就是MATCH_PARENT, 但是我们是dialog，一般不需要全屏，所以可以设置为WRAP_CONTENT
        private int width = WindowManager.LayoutParams.WRAP_CONTENT; // WindowManager.LayoutParams.MATCH_PARENT;
        private int height = WindowManager.LayoutParams.WRAP_CONTENT;
        private int gravity;
        private boolean isCancelable;
        private int x;
        private int y;

        public Builder(Activity context) {
            this.mContext = context;
        }

        public Builder setContentView(View view) {
            this.mContentView = view;
            return this;
        }

        public Builder setContentView(View view, int w, int h) {
            this.mContentView = view;
            this.mContentParams = new ViewGroup.LayoutParams(w, h);
            return this;
        }

        public Builder setContentView(int layout) {
            this.mContentLayout = layout;
            return this;
        }

        public Builder setStyle(int style) {
            this.style = style;
            return this;
        }


        public Builder setPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.isCancelable = cancelable;
            return this;
        }

        public Dialog createDialog() {
            Dialog dialog = createDialog(mContext, mContentLayout, mContentView, mContentParams);
            return dialog;
        }

        public Dialog createDialog(Activity context, View contentView) {
            return createDialog(context, 0, contentView, mContentParams, width, height, isCancelable, style);
        }

        public Dialog createDialog(Activity context, int contentViewLayout, View contentView, ViewGroup.LayoutParams contentLayoutParams) {
            return createDialog(context, contentViewLayout, contentView, contentLayoutParams, width, height, isCancelable, style);
        }

        public Dialog createDialog(Activity context, int contentViewLayout, View contentView, ViewGroup.LayoutParams contentLayoutParams, int width, int height, boolean cancellable, int style) {
            Dialog dialog = new DialogHelper(context, style);
            // requestFeature() must be called before adding content
            // 设置requestFeature要放在setContentView之前
            // dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            dialog.setCanceledOnTouchOutside(cancellable);
            dialog.setCancelable(cancellable);
            if (contentView != null) {
                if (contentLayoutParams != null) {
                    dialog.setContentView(contentView, contentLayoutParams);
                } else {
                    dialog.setContentView(contentView);
                }
            } else if (contentViewLayout != 0) {
                dialog.setContentView(contentViewLayout);
            }


            WindowManager.LayoutParams params = dialog.getWindow()
                    .getAttributes();

            params.width = width;
            // 设置dialog中decor的宽和高，注意需要放在setContentView之后
            // 因为dialog默认WRAP_CONTENT，如果在setContentView之前设置，就会被覆盖成WRAP_CONTENT
            params.height = height;
            if (x != 0) {
                params.x = x;
            }
            if (y != 0) {
                params.y = y;
            }
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setGravity((gravity == 0) ? Gravity.CENTER : gravity);
            return dialog;
        }
    }

    private static int dip2px(float dpValue, Context mContext) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}