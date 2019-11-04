package com.matejhrazdira.pojos.cblib;

public abstract class EventConsumer<T> implements Disposable {

	private static final long NULL_PTR = 0x0;

	private long mNativeConsumer = NULL_PTR;

	public EventConsumer(final EventService eventService, int subscription, Class<T> eventClass) throws CorbaException {
		mNativeConsumer = connectConsumer(eventService.getNativeWrapper(), subscription, eventClass.getName());
	}

	private native long connectConsumer(final long nativeWrapper, final int subscription, final String name) throws CorbaException;

	public abstract void onEvent(T event);

	@Override
	public Void _dispose_() throws CorbaException {
		if (mNativeConsumer != NULL_PTR) {
			disposeImpl(mNativeConsumer);
			mNativeConsumer = NULL_PTR;
		}
		return null;
	}

	private native void disposeImpl(final long nativeWrapper) throws CorbaException;
}
