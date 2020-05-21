package com.neuifo.mangareptile.ui.detail

import android.animation.Animator
import android.graphics.Rect
import android.transition.Transition
import android.transition.TransitionValues
import android.view.ViewGroup
import com.neuifo.data.domain.utils.LogHelper

class ExitTransition : Transition() {


    override fun captureStartValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        val rect = Rect()
        view.getHitRect(rect)
        LogHelper.system.e("开始图片位置:" + rect.top + "-------图片高度:" + rect.height())

    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        val rect = Rect()
        view.getHitRect(rect)
        LogHelper.system.e("结束图片位置:" + rect.top + "-------图片高度:" + rect.height())
    }


    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {
        return super.createAnimator(sceneRoot, startValues, endValues)
    }

}