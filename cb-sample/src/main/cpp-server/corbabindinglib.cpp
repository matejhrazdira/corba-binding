#include "corbabindinglib.h"

namespace corbabinding {

NativeWrapper::NativeWrapper(int argc, ACE_TCHAR * argv[], ThreadCleanup * cleanup) : log(getLog("NativeWrapper")) {
	log.i() << "ctor()" << done;

	log.v() << "initialize ORB" << done;
	mOrb = CORBA::ORB_init(argc, argv);

	log.v() << "resolve and activate POA" << done;
	CORBA::Object_var poaObject = mOrb->resolve_initial_references("RootPOA");
	mPoa = PortableServer::POA::_narrow(poaObject);
	mPoaManager = mPoa->the_POAManager();
	mPoaManager->activate();

	log.v() << "resolve NameService" << done;
	try {
		CORBA::Object_var namingServiceObject = mOrb->resolve_initial_references("NameService");
		mNameService = CosNaming::NamingContext::_narrow(namingServiceObject.in());
	} catch (CORBA::ORB::InvalidName & e) {
		log.w() << "not using NameService" << done;
	}

	log.v() << "resolve IORTable" << done;
	CORBA::Object_var iorTableObject = mOrb->resolve_initial_references("IORTable");
	mIorTable = IORTable::Table::_narrow(iorTableObject.in());

	mThreadCleanup = cleanup;

	log.v() << "spawn ORB thread";
	mThreadManager.spawn(runOrb, this);
}

NativeWrapper::~NativeWrapper() {
	log.i() << "dtor()" << done;
	log.v() << "deactivating POA manager" << done;
	mPoaManager->deactivate(true, true);
	log.v() << "shutting down ORB" << done;
	mOrb->shutdown(true);
	log.v() << "destroying ORB" << done;
	mOrb->destroy();
	log.v() << "waiting for ORB thread to finish" << done;
	mThreadManager.wait();
	log.i() << "ORB thread finished, instance destroyed" << done;
	delete mThreadCleanup;
}

void NativeWrapper::unbind(const char * name) {
	log.i() << "unbinding name " << name << done;
	if (!CORBA::is_nil(mNameService)) {
		CosNaming::Name n(1);
		n.length(1);
		n[0].id = CORBA::string_dup(name);
		mNameService->unbind(n);
	}
}

CORBA::Object_ptr NativeWrapper::resolve(const char *corbaStr) {
	log.i() << "resolving " << corbaStr << done;
	return mOrb->string_to_object(corbaStr);
}

void * NativeWrapper::runOrb(void * arg) {
	NativeWrapper * instance = (NativeWrapper *) arg;
	Log & log = instance->log;
	log.i() << "ORB thread started" << done;
	instance->mOrb->run();
	log.i() << "ORB::run() finished" << done;
	if (instance->mThreadCleanup) {
		log.v() << "executing thread cleanup" << done;
		instance->mThreadCleanup->cleanup();
	}
	log.i() << "ORB thread finishing" << done;
	return 0x0;
}

EventServiceWrapper::~EventServiceWrapper() {}

void EventServiceWrapper::connect() {
	log.i() << "connecting event service " << mEventServiceStr << done;
	if (mNativeWrapper) {
		CORBA::Object_var eventServiceObject = mNativeWrapper->resolve(mEventServiceStr);
		mEventService = RtecEventChannelAdmin::EventChannel::_narrow(eventServiceObject.in());
	}
}

bool EventServiceWrapper::isAlive() {
	log.i() << "checking event service" << done;
	if (!CORBA::is_nil(mEventService)) {
		return !mEventService->_non_existent();
	} else {
		return false;
	}
}

} /* namespace corbabinding */
