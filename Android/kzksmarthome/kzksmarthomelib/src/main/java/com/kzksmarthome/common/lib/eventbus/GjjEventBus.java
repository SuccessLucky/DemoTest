package com.kzksmarthome.common.lib.eventbus;

import java.io.Serializable;

import android.os.Parcelable;

import com.kzksmarthome.common.app.SmartHomeAppLib;
import com.kzksmarthome.common.lib.ipc.BackProcMessenger;
import com.kzksmarthome.common.lib.ipc.ForeProcMessenger;
import com.kzksmarthome.common.lib.ipc.MessageConstant;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.ThreadMode;

public class GjjEventBus {
    public synchronized static GjjEventBus getInstance() {
        return new GjjEventBus();
    }
    
    /** For unit test primarily. */
    public static void clearCaches() {
        EventBus.clearCaches();
    }
    
    /**
     * Like {@link #register(Object)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(Object)}) to the given subscriber.
     */
    public void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }
    
    /**
     * Like {@link #register(Object)} with an additional subscriber priority to influence the order of event delivery.
     * Within the same delivery thread ({@link ThreadMode}), higher priority subscribers will receive events before
     * others with a lower priority. The default priority is 0. Note: the priority does *NOT* affect the order of
     * delivery among subscribers with different {@link ThreadMode}s!
     */
    public void register(Object subscriber, int priority) {
        EventBus.getDefault().register(subscriber, priority);
    }
    
    /**
     * Like {@link #register(Object)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(Object)}) to the given subscriber.
     */
    public void registerSticky(Object subscriber) {
        EventBus.getDefault().registerSticky(subscriber);
    }
    
    /**
     * Like {@link #register(Object, int)}, but also triggers delivery of the most recent sticky event (posted with
     * {@link #postSticky(Object)}) to the given subscriber.
     */
    public void registerSticky(Object subscriber, int priority) {
        EventBus.getDefault().registerSticky(subscriber, priority);
    }
    
    /** Only updates subscriptionsByEventType, not typesBySubscriber! Caller must update typesBySubscriber. */
    public boolean isRegistered(Object subscriber) {
        return EventBus.getDefault().isRegistered(subscriber);
    }
    
    /** Unregisters the given subscriber from all event classes. */
    public void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }
    
    /** Posts the given event to the event bus. */
    public void post(Object event) {
        EventBus.getDefault().post(event);
    }
    
    public void post(Parcelable event, boolean mutiProc){
        post(event, mutiProc, false);
    }
    
    /**
     * post event
     * 
     * @param event
     * @param multiProc
     * @param onlyCrossProc 只跨进程发送, 当前进程不发送
     */
    public void post(Serializable event, boolean multiProc, boolean onlyCrossProc) {
        if (!onlyCrossProc) {
            EventBus.getDefault().post(event);
        }
        if (multiProc) {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                ForeProcMessenger.getInstance().sendSerializable(
                        MessageConstant.MESSENGER_EVENT_FROM_FOREGROUND, event);
            } else if (SmartHomeAppLib.getInstance().isBackProcess()) {
                BackProcMessenger.getInstance().send(MessageConstant.MESSENGER_EVENT_FROM_BACKGROUND, event);
            }
        }
    }
    
    public void post(Parcelable event, boolean multiProc, boolean onlyCrossProc) {
        if (!onlyCrossProc) {
            EventBus.getDefault().post(event);
        }
        if (multiProc) {
            if (SmartHomeAppLib.getInstance().isForeProcess()) {
                ForeProcMessenger.getInstance().sendParcel(MessageConstant.MESSENGER_EVENT_FROM_FOREGROUND, event);
            } else if (SmartHomeAppLib.getInstance().isBackProcess()) {
                BackProcMessenger.getInstance().send(MessageConstant.MESSENGER_EVENT_FROM_BACKGROUND, event);
            }
        }
    }
    
    /**
     * Called from a subscriber's event handling method, further event delivery will be canceled. Subsequent
     * subscribers
     * won't receive the event. Events are usually canceled by higher priority subscribers (see
     * {@link #register(Object, int)}). Canceling is restricted to event handling methods running in posting thread
     * {@link ThreadMode#PostThread}.
     */
    public void cancelEventDelivery(Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }
    
    /**
     * Posts the given event to the event bus and holds on to the event (because it is sticky). The most recent sticky
     * event of an event's type is kept in memory for future access. This can be {@link #registerSticky(Object)} or
     * {@link #getStickyEvent(Class)}.
     */
    public void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }
    
    /**
     * Gets the most recent sticky event for the given type.
     *
     * @see #postSticky(Object)
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().getStickyEvent(eventType);
    }
    
    /**
     * Remove and gets the recent sticky event for the given event type.
     *
     * @see #postSticky(Object)
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().removeStickyEvent(eventType);
    }
    
    /**
     * Removes the sticky event if it equals to the given event.
     *
     * @return true if the events matched and the sticky event was removed.
     */
    public boolean removeStickyEvent(Object event) {
        return EventBus.getDefault().removeStickyEvent(event);
    }
    
    /**
     * Removes all sticky events.
     */
    public void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }
    
    public boolean hasSubscriberForEvent(Class<?> eventClass) {
        return EventBus.getDefault().hasSubscriberForEvent(eventClass);
    }
}
