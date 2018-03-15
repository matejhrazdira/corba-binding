#include "corbabindinglib.h"

namespace corbabinding {

NativeWrapper::NativeWrapper(int argc, ACE_TCHAR * argv[], const char * eventServiceName, ThreadCleanup * cleanup) {
	mOrb = CORBA::ORB_init(argc, argv);

	CORBA::Object_var poaObject = mOrb->resolve_initial_references("RootPOA");
	mPoa = PortableServer::POA::_narrow(poaObject);
	mPoaManager = mPoa->the_POAManager();
	mPoaManager->activate();

	CORBA::Object_var naming_context_object = mOrb->resolve_initial_references("NameService");
	mNameService = CosNaming::NamingContext::_narrow(naming_context_object.in());

	CosNaming::Name name(1);
	name.length(1);
	name[0].id = CORBA::string_dup(eventServiceName);
	CORBA::Object_var ec_object = mNameService->resolve(name);
	mEventService = RtecEventChannelAdmin::EventChannel::_narrow(ec_object.in());

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
	CosNaming::Name n(1);
	n.length(1);
	n[0].id = CORBA::string_dup(name);
	mNameService->unbind(n);
}

CORBA::Object_ptr NativeWrapper::resolveName(const char * name) {
	CosNaming::Name n(1);
	n.length(1);
	n[0].id = CORBA::string_dup(name);
	return mNameService->resolve(n);
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
