package com.matejhrazdira.pojos;

public class CorbaProvider implements Disposable {

	private static final int NULL_PTR = 0x0;

	private long mNativeWrapper = NULL_PTR;

	public CorbaProvider(String[] orbArgs, String eventServiceStr) throws CorbaException {
		mNativeWrapper = init(orbArgs, eventServiceStr);
	}

	private native long init(final String[] orbArgs, final String eventServiceStr) throws CorbaException;

	public <T extends Disposable> T resolve(Class<T> cls, String corbaStr) throws CorbaException {
		if (mNativeWrapper == NULL_PTR) {
			throw new AlreadyDisposedException();
		}
		return resolveImpl(mNativeWrapper, cls.getName(), corbaStr);
	}

	private native <T extends Disposable> T resolveImpl(final long nativeWrapper, final String className, final String corbaStr) throws CorbaException;

	public void _dispose_() throws CorbaException {
		if (mNativeWrapper != NULL_PTR) {
			disposeImpl(mNativeWrapper);
			mNativeWrapper = NULL_PTR;
		}
	}

	private native void disposeImpl(final long nativeWrapper) throws CorbaException;

	public long getNativeWrapper() throws AlreadyDisposedException {
		if (mNativeWrapper == NULL_PTR) {
			throw new AlreadyDisposedException();
		}
		return mNativeWrapper;
	}
}
