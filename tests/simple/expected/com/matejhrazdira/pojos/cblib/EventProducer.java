package com.matejhrazdira.pojos.cblib;

public class EventProducer<T> implements Disposable {

	private static final int NULL_PTR = 0x0;

	private long mNativeProducer = NULL_PTR;

	public EventProducer(final EventService eventService, int source, int subscription, Class<T> eventClass) throws CorbaException {
		mNativeProducer = connectProducer(eventService.getNativeWrapper(), source, subscription, eventClass.getName());
	}

	private native long connectProducer(final long nativeWrapper, final int source, final int subscription, final String name) throws CorbaException;

	public void pushEvent(final T eventData) throws CorbaException {
		if (mNativeProducer == NULL_PTR) {
			throw new AlreadyDisposedException();
		}
		pushEventImpl(mNativeProducer, eventData);
	}

	private native void pushEventImpl(final long nativeWrapper, final T eventData) throws CorbaException;

	@Override
	public Void _dispose_() throws CorbaException {
		if (mNativeProducer != NULL_PTR) {
			disposeImpl(mNativeProducer);
			mNativeProducer = NULL_PTR;
		}
		return null;
	}

	private native void disposeImpl(final long nativeWrapper) throws CorbaException;
}
