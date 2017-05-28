#ifndef SRC_COMMON_NATIVEWRAPPER_H_
#define SRC_COMMON_NATIVEWRAPPER_H_

#include "TestUtil.h"
#include "Token.h"
#include <orbsvcs/CosNamingC.h>
#include <orbsvcs/RtecEventChannelAdminC.h>
#include <tao/PortableServer/PortableServer.h>
#include <ace/Thread_Manager.h>
#include <ace/Unbounded_Queue.h>

namespace corbabinding {

template<typename T>
class BlockingQueue {
public:
	BlockingQueue() : mActive(true) {}

	virtual ~BlockingQueue() {}

	void stop() {
		Lock lock(mToken);
		mActive = false;
		mToken.broadcast();
	}

	T * get() {
		Lock lock(mToken);
		T * item = 0x0;
		int res = mQueue.dequeue_head(item);
		if (res != -1) {
			return item;
		} else {
			if (mActive) {
				mToken.wait();
				return get();
			} else {
				return 0x0;
			}
		}
	}

	bool put(T * item) {
		Lock lock(mToken);
		if (mActive) {
			mQueue.enqueue_tail(item);
			mToken.broadcast();
			return true;
		} else {
			return false;
		}
	}
private:
	Token mToken;
	bool mActive;
	ACE_Unbounded_Queue<T *> mQueue;
};

class NativeWrapper {
public:
	NativeWrapper(int argc, ACE_TCHAR * argv[]);
	virtual ~NativeWrapper();
	CosNaming::NamingContext_ptr getNameService() { return mNameService.ptr(); }
	RtecEventChannelAdmin::EventChannel_ptr getEventService() { return mEventService.ptr(); }
	template<typename T> void bindName(const char * name, T object) {
		CosNaming::Name n(1);
		n.length(1);
		n[0].id = CORBA::string_dup(name);
		mNameService->bind(n, object);
	}
	void unbind(const char * name) {
		CosNaming::Name n(1);
		n.length(1);
		n[0].id = CORBA::string_dup(name);
		mNameService->unbind(n);
	}
	template<typename T> T * resolveName(const char * name, T * ptr) {
		CosNaming::Name n(1);
		n.length(1);
		n[0].id = CORBA::string_dup(name);
		CORBA::Object_var obj = mNameService->resolve(n);
		return T::_narrow(obj.in());
	}
	ACE_thread_t getOrbThread() { return mOrbThread; }
private:
	testutil::Log mLog;
	CORBA::ORB_var mOrb;
	PortableServer::POA_var mPoa;
	PortableServer::POAManager_var mPoaManager;
	CosNaming::NamingContext_var mNameService;
	RtecEventChannelAdmin::EventChannel_var mEventService;
	ACE_Thread_Manager mThreadManager;
	ACE_thread_t mOrbThread; // Proof of concept / debug only

	static void * runOrb(void * arg);
	static void * processEvents(void * arg);
};

} /* namespace corbabinding */

#endif /* SRC_COMMON_NATIVEWRAPPER_H_ */
