package com.matejhrazdira.pojos;

public abstract class EventConsumer<T> {

	private static final int NULL_PTR = 0x0;

	private long mNativeConsumer = NULL_PTR;

	public EventConsumer(final CorbaProvider corbaProvider, int subscription, Class<T> eventClass) throws CorbaException {
		mNativeConsumer = connectConsumer(corbaProvider.getNativeWrapper(), subscription, eventClass.getName());
	}

	private native long connectConsumer(final long nativeWrapper, final int subscription, final String name) throws CorbaException;

	public abstract void onEvent(T event);

	public void dispose() {
		if (mNativeConsumer != NULL_PTR) {
			disposeImpl(mNativeConsumer);
			mNativeConsumer = NULL_PTR;
		}
	}

	private native void disposeImpl(final long nativeWrapper);
}
