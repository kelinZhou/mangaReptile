package com.neuifo.mangareptile.event;


import com.neuifo.domain.executor.PostExecutionThread;

/**
 * 事件总线
 */
public interface EventBus {

    /**
     * 事件
     */
    interface BusEvent {

    }

    /**
     * 空事件
     */
    final class VoidEvent implements BusEvent {
    }

    final class ImOfflineEvent implements BusEvent {
        public String event;
        public ImOfflineEvent(String event) {
            this.event = event;
        }
    }

    final class StringEvent implements BusEvent {
        public String event;

        public StringEvent(String event) {
            this.event = event;
        }
    }

    final class RefreshMessageEvent implements BusEvent {
    }


    final class JSEvent implements BusEvent {
        public static final String ACTION_UPLOAD_RESUME = "goUploadResume";

        public String action;

        public JSEvent(String event) {
            this.action = event;
        }
    }

    final class CurrentJobObjectiveChangedEvent implements BusEvent {
        private String currentJobIntention;

        public CurrentJobObjectiveChangedEvent(String currentJobIntention) {
            this.currentJobIntention = currentJobIntention;
        }

        public String getCurrentJobIntention() {
            return currentJobIntention;
        }
    }

    interface OnEventListener<S extends BusEvent> {
        void onEvent(S e);
    }


    <S extends EventBus.BusEvent> void postEvent(S event);

    <S extends EventBus.BusEvent> void postStickyEvent(S event);

    void removeAllStickyEvents();

    void unregister(final OnEventListener listener);

    <S extends BusEvent> void register(final Class<S> eventType, final OnEventListener<BusEvent> listener);

    void register(final OnEventListener<BusEvent> listener, final Class<BusEvent>... eventTypes);

    /**
     * 注册观察者
     *
     * @param eventType      事件类型。
     * @param listener       事件监听。
     */


    <E extends BusEvent> void registerSticky(final Class<E> eventType, final OnEventListener<BusEvent> listener);


    void registerSticky(OnEventListener<BusEvent> listener, Class<BusEvent>... eventTypes);

    /**
     * 注册sticky观察者
     *
     * @param eventType      事件类型。
     * @param listener       事件监听。
     * @param callbackThread 回调线程。
     */
    <S extends BusEvent> void registerSticky(final Class<S> eventType, final OnEventListener<BusEvent> listener, PostExecutionThread callbackThread);
}
