package com.neuifo.mangareptile.utils;


public class FinishHelper {
    private long createAt;

    public static FinishHelper createInstance() {
        return new FinishHelper();
    }

    private FinishHelper() {
    }

    public boolean canFinish() {
        long curTime = System.currentTimeMillis();
        if (curTime - createAt <= 1000) {
            ToastUtil.cancel();
            return true;
        } else {
            ToastUtil.showShortToast(String.format("再按一次退出程序"));
            createAt = curTime;
            return false;
        }
    }
}
