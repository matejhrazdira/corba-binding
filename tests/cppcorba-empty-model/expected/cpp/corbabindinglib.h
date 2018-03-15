#ifndef CORBABINDINGLIB_H_
#define CORBABINDINGLIB_H_

#include <orbsvcs/CosNamingC.h>
#include <orbsvcs/RtecEventChannelAdminC.h>
#include <orbsvcs/Event_Utilities.h>
#include <orbsvcs/RtecEventChannelAdminC.h>
#include <orbsvcs/RtecEventCommS.h>
#include <tao/PortableServer/PortableServer.h>
#include <ace/Thread_Manager.h>

namespace corbabinding {

class ThreadCleanup {
public:
	ThreadCleanup() {}
	virtual ~ThreadCleanup() {}

	virtual void cleanup() = 0;
};

class NativeWrapper {
public:
	NativeWrapper(int argc, ACE_TCHAR * argv[], const char * eventServiceName, ThreadCleanup * cleanup);

	virtual ~NativeWrapper();

	CosNaming::NamingContext_ptr getNameService() {
		return mNameService.ptr();
	}

	RtecEventChannelAdmin::EventChannel_ptr getEventService() {
		return mEventService.ptr();
	}

	PortableServer::POA_ptr getPoa() {
		return mPoa.ptr();
	}

	template<typename T> void bindName(const char * name, T object) {
		CosNaming::Name n(1);
		n.length(1);
		n[0].id = CORBA::string_dup(name);
		mNameService->bind(n, object);
	}

	void unbind(const char * name);

	CORBA::Object_ptr resolveName(const char * name);

private:
	CORBA::ORB_var mOrb;
	PortableServer::POA_var mPoa;
	PortableServer::POAManager_var mPoaManager;
	CosNaming::NamingContext_var mNameService;
	RtecEventChannelAdmin::EventChannel_var mEventService;
	ACE_Thread_Manager mThreadManager;
	ThreadCleanup * mThreadCleanup;

	static void * runOrb(void * arg);
};

class AnyEventConsumer: public POA_RtecEventComm::PushConsumer {
public:
	AnyEventConsumer(CORBA::ULong type) : mType(type) {}

	virtual ~AnyEventConsumer() {}

	void connect(NativeWrapper * nativeWraper) {
		mNativeWrapper = nativeWraper;
		ACE_ConsumerQOS_Factory qos;
		qos.start_disjunction_group();
		qos.insert_type(mType, 0);
		RtecEventChannelAdmin::ConsumerAdmin_var consumerAdmin = nativeWraper->getEventService()->for_consumers();
		mSupplierProxy = consumerAdmin->obtain_push_supplier();
		mMyself = _this();
		mSupplierProxy->connect_push_consumer(mMyself.in(), qos.get_ConsumerQOS());
	}

	void push(const RtecEventComm::EventSet& data) {
		for (CORBA::ULong i = 0; i != data.length(); ++i) {
			const RtecEventComm::Event &e = data[i];
			onAnyEvent(e.data.any_value);
		}
	}

	virtual void onAnyEvent(const CORBA::Any & event) = 0;

	void disconnect() {
		mSupplierProxy->disconnect_push_supplier();
		PortableServer::POA_ptr poa = mNativeWrapper->getPoa();
		PortableServer::ObjectId_var id = poa->reference_to_id(mMyself.ptr());
		poa->deactivate_object(id);
		mMyself = RtecEventComm::PushConsumer::_nil();
	}

	void disconnect_push_consumer() {
		mSupplierProxy = RtecEventChannelAdmin::ProxyPushSupplier::_nil();
	}

private:
	CORBA::ULong mType;
	RtecEventChannelAdmin::ProxyPushSupplier_var mSupplierProxy;
	RtecEventComm::PushConsumer_var mMyself;
	NativeWrapper * mNativeWrapper;
};

} /* namespace corbabinding */

#endif /* CORBABINDINGLIB_H_ */
