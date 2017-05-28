package com.matejhrazdira.pojos;

public class CorbaProvider {

	private static final int NULL_PTR = 0x0;

	private long mNativeWrapper = NULL_PTR;

	public CorbaProvider(String[] orbArgs, String eventServiceName) throws CorbaException {
		mNativeWrapper = init(orbArgs, eventServiceName);
	}

	private native long init(final String[] orbArgs, final String eventServiceName) throws CorbaException;

	public <T> T resolve(Class<T> cls, String name) throws CorbaException {
		if (mNativeWrapper == NULL_PTR) {
			throw new AlreadyDisposedException();
		}
		return resolveImpl(mNativeWrapper, cls.getName(), name);
	}

	private native <T> T resolveImpl(final long nativeWrapper, final String className, final String corbaName) throws CorbaException;

	public void dispose() {
		if (mNativeWrapper != NULL_PTR) {
			disposeImpl(mNativeWrapper);
			mNativeWrapper = NULL_PTR;
		}
	}

	private native void disposeImpl(final long nativeWrapper);

	public long getNativeWrapper() throws AlreadyDisposedException {
		if (mNativeWrapper == NULL_PTR) {
			throw new AlreadyDisposedException();
		}
		return mNativeWrapper;
	}
}
