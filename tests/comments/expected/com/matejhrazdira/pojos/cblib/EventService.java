package com.matejhrazdira.pojos.cblib;

public class EventService implements Disposable {

	private static final long NULL_PTR = 0x0;

	private final String mEventServiceStr;

	private long mNativeWrapper = NULL_PTR;

	public EventService(final CorbaProvider corbaProvider, final String eventServiceStr) throws CorbaException {
		mEventServiceStr = eventServiceStr;
		mNativeWrapper = init(corbaProvider.getNativeWrapper(), eventServiceStr);
	}

	private native long init(final long nativeWrapper, String eventServiceStr);

	public final void connect() throws CorbaException {
		connectImpl(mNativeWrapper);
	}

	private native void connectImpl(long nativeWrapper) throws CorbaException;

	public final boolean isAlive() throws CorbaException {
		return isAliveImpl(mNativeWrapper);
	}

	private native boolean isAliveImpl(long nativeWrapper) throws CorbaException;

	@Override
	public Void _dispose_() throws CorbaException {
		if (mNativeWrapper != NULL_PTR) {
			disposeImpl(mNativeWrapper);
			mNativeWrapper = NULL_PTR;
		}
		return null;
	}

	private native void disposeImpl(final long nativeWrapper) throws CorbaException;

	public long getNativeWrapper() throws AlreadyDisposedException {
		if (mNativeWrapper == NULL_PTR) {
			throw new AlreadyDisposedException();
		}
		return mNativeWrapper;
	}
}
