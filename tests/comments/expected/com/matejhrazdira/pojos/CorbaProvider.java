package com.matejhrazdira.pojos;

public class CorbaProvider implements Disposable {

	private static final int NULL_PTR = 0x0;

	private long mNativeWrapper = NULL_PTR;

	public CorbaProvider(final String[] orbArgs) throws CorbaException {
		mNativeWrapper = init(orbArgs);
	}

	private native long init(final String[] orbArgs) throws CorbaException;

	public void connectEventService(final String eventServiceStr) throws CorbaException {
		if (mNativeWrapper == NULL_PTR) {
			throw new AlreadyDisposedException();
		}
		connectEventServiceImpl(mNativeWrapper, eventServiceStr);
	}

	private native void connectEventServiceImpl(final long nativeWrapper, final String eventServiceStr) throws CorbaException;

	public boolean eventServiceAlive() throws CorbaException {
		if (mNativeWrapper == NULL_PTR) {
			throw new AlreadyDisposedException();
		}
		return eventServiceAliveImpl(mNativeWrapper);
	}

	private native boolean eventServiceAliveImpl(final long nativeWrapper) throws CorbaException;

	public <T extends Disposable> T resolve(Class<T> cls, String corbaStr) throws CorbaException {
		if (mNativeWrapper == NULL_PTR) {
			throw new AlreadyDisposedException();
		}
		return resolveImpl(mNativeWrapper, cls.getName(), corbaStr);
	}

	private native <T extends Disposable> T resolveImpl(final long nativeWrapper, final String className, final String corbaStr) throws CorbaException;

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
