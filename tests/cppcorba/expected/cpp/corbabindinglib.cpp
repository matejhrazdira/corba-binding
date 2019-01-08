#include "corbabindinglib.h"

namespace corbabinding {

NativeWrapper::NativeWrapper(int argc, ACE_TCHAR * argv[], const char * eventServiceStr, ThreadCleanup * cleanup) {
	mOrb = CORBA::ORB_init(argc, argv);

	CORBA::Object_var poaObject = mOrb->resolve_initial_references("RootPOA");
	mPoa = PortableServer::POA::_narrow(poaObject);
	mPoaManager = mPoa->the_POAManager();
	mPoaManager->activate();

	try {
		CORBA::Object_var namingServiceObject = mOrb->resolve_initial_references("NameService");
		mNameService = CosNaming::NamingContext::_narrow(namingServiceObject.in());
	} catch (CORBA::ORB::InvalidName & e) {
		// ignore... just not using NameService
	}

	CORBA::Object_var iorTableObject = mOrb->resolve_initial_references("IORTable");
	mIorTable = IORTable::Table::_narrow(iorTableObject.in());

	CORBA::Object_var eventServiceObject = mOrb->string_to_object(eventServiceStr);
	mEventService = RtecEventChannelAdmin::EventChannel::_narrow(eventServiceObject.in());

	mThreadCleanup = cleanup;

	mThreadManager.spawn(runOrb, this);
}

NativeWrapper::~NativeWrapper() {
	mPoaManager->deactivate(true, true);
	mOrb->shutdown(true);
	mOrb->destroy();
	mThreadManager.wait();
	delete mThreadCleanup;
}

void NativeWrapper::unbind(const char * name) {
	if (!CORBA::is_nil(mNameService)) {
		CosNaming::Name n(1);
		n.length(1);
		n[0].id = CORBA::string_dup(name);
		mNameService->unbind(n);
	}
}

CORBA::Object_ptr NativeWrapper::resolve(const char *corbaStr) {
	return mOrb->string_to_object(corbaStr);
}

void * NativeWrapper::runOrb(void * arg) {
	NativeWrapper * instance = (NativeWrapper *) arg;
	instance->mOrb->run();
	if (instance->mThreadCleanup) {
		instance->mThreadCleanup->cleanup();
	}
	return 0x0;
}

} /* namespace corbabinding */
