#ifndef CORBABINDINGLIB_H_
#define CORBABINDINGLIB_H_

#include <orbsvcs/CosNamingC.h>
#include <orbsvcs/RtecEventChannelAdminC.h>
#include <orbsvcs/Event_Utilities.h>
#include <orbsvcs/RtecEventCommS.h>
#include <tao/IORTable/IORTable.h>
#include <tao/PortableServer/PortableServer.h>
#include <ace/Thread_Manager.h>

#include "common.h"

namespace corbabinding {

class ThreadCleanup {
public:
	ThreadCleanup() {}
	virtual ~ThreadCleanup() {}

	virtual void cleanup() = 0;
};

class NativeWrapper {
public:
	NativeWrapper(int argc, ACE_TCHAR * argv[], ThreadCleanup * cleanup = nullptr);

	virtual ~NativeWrapper();

	CosNaming::NamingContext_ptr getNameService() {
		return mNameService.ptr();
	}

	PortableServer::POA_ptr getPoa() {
		return mPoa.ptr();
	}

	template<typename T> void bindName(const char * name, T object) {
		log.i() << "binding object to name '" << name << "'" << done;
		if (!CORBA::is_nil(mNameService)) {
			CosNaming::Name n(1);
			n.length(1);
			n[0].id = CORBA::string_dup(name);
		    log.i() << "binding object to name '" << name << "' using NameService" << done;
			mNameService->rebind(n, object);
		}
		if (!CORBA::is_nil(mIorTable)) {
		    log.i() << "binding object to name '" << name << "' using IOR table" << done;
			CORBA::String_var objectStr = mOrb->object_to_string(object);
			mIorTable->rebind(name, objectStr);
		} else {
			throw CORBA::UNKNOWN(0, CORBA::CompletionStatus::COMPLETED_NO);
		}
	}

	void unbind(const char * name);

	CORBA::Object_ptr resolve(const char * corbaStr);

private:
	Log log;

	CORBA::ORB_var mOrb;
	PortableServer::POA_var mPoa;
	PortableServer::POAManager_var mPoaManager;
	CosNaming::NamingContext_var mNameService;
	IORTable::Table_var mIorTable;
	ACE_Thread_Manager mThreadManager;
	ThreadCleanup * mThreadCleanup;

	static void * runOrb(void * arg);
};

class EventServiceWrapper {
public:
	EventServiceWrapper(NativeWrapper * nativeWrapper, const char * eventServiceStr) :
		log(getLog("EventServiceWrapper")), mNativeWrapper(nativeWrapper), mEventServiceStr(eventServiceStr) {}

	RtecEventChannelAdmin::EventChannel_ptr getEventService() {
		return mEventService.ptr();
	}

	PortableServer::POA_ptr getPoa() {
		return mNativeWrapper->getPoa();
	}

	virtual ~EventServiceWrapper();

	void connect();

	bool isAlive();

private:
	Log log;

	NativeWrapper * mNativeWrapper;
	CORBA::String_var mEventServiceStr;
	RtecEventChannelAdmin::EventChannel_var mEventService;
};

class AnyEventConsumer: public POA_RtecEventComm::PushConsumer {
public:
	AnyEventConsumer(CORBA::ULong type, const char * name = "AnyEventConsumer") : log(getLog(name)), mType(type), mWrapper(nullptr) {
		log.i() << "ctor()" << done;
	}

	virtual ~AnyEventConsumer() {
		log.i() << "dtor()" << done;
	}

	void connect(EventServiceWrapper * wraper) {
		log.i() << "connect()" << done;
		mWrapper = wraper;
		ACE_ConsumerQOS_Factory qos;
		qos.start_disjunction_group();
		qos.insert_type(mType, 0);
		log.v() << "getting event channel" << done;
		RtecEventChannelAdmin::EventChannel_ptr eventService = wraper->getEventService();
		if (CORBA::is_nil(eventService)) {
			throw CORBA::UNKNOWN(0, CORBA::CompletionStatus::COMPLETED_NO);
		}
		log.v() << "getting consumer admin" << done;
		RtecEventChannelAdmin::ConsumerAdmin_var consumerAdmin = eventService->for_consumers();
		log.v() << "obtaining supplier proxy" << done;
		mSupplierProxy = consumerAdmin->obtain_push_supplier();
		log.v() << "activating self (_this());" << " (servant: " << _refcount_value() << ")" << done;
		RtecEventComm::PushConsumer_var self = _this();
		log.v() << "connecting push consumer (self); " << "(self: " << self->_refcount_value() << ", servant: " << _refcount_value() << ")" << done;
		mSupplierProxy->connect_push_consumer(self.in(), qos.get_ConsumerQOS());
		log.i() << "connected; " << "(self: " << self->_refcount_value() << ", servant: " << _refcount_value() << ")" << done;
	}

	void push(const RtecEventComm::EventSet& data) {
		for (CORBA::ULong i = 0; i != data.length(); ++i) {
			const RtecEventComm::Event &e = data[i];
			onAnyEvent(e.data.any_value);
		}
	}

	virtual void onAnyEvent(const CORBA::Any & event) = 0;

	void disconnect() {
		log.i() << "disconnect()" << done;
		log.v() << "disconnecting supplier proxy;" << " (servant: " << _refcount_value() << ")" << done;
		mSupplierProxy->disconnect_push_supplier();
		PortableServer::POA_ptr poa = mWrapper->getPoa();
		log.v() << "obtaining reference to servant" << done;
		PortableServer::ObjectId_var id = poa->servant_to_id(this);
		log.v() << "deactivating servant;" << " (servant: " << _refcount_value() << ")" << done;
		poa->deactivate_object(id);
		log.i() << "disconnected; removing ref " << " (servant: " << _refcount_value() << ")" << done;
		_remove_ref();
	}

	void disconnect_push_consumer() {
		log.w() << "disconnect_push_consumer()" << done;
		mSupplierProxy = RtecEventChannelAdmin::ProxyPushSupplier::_nil();
	}

protected:
	Log log;

private:
	CORBA::ULong mType;
	RtecEventChannelAdmin::ProxyPushSupplier_var mSupplierProxy;
	EventServiceWrapper * mWrapper;
};

template<typename T>
class AbsEventConsumer : public AnyEventConsumer {
public:
	AbsEventConsumer(CORBA::ULong type, const char * name = "EventConsumer") : AnyEventConsumer(type, name) {}

	void onAnyEvent(const CORBA::Any & e) {
		T * event;
		if ((e >>= event) == 0) {
			log.w() << "received invalid event" << testutil::done;
		} else {
			onEvent(event);
		}
	}

	virtual void onEvent(T * event) = 0;

};

template<typename T>
class AbsEventProducer: public POA_RtecEventComm::PushSupplier {
public:
	AbsEventProducer(RtecEventComm::EventSourceID sourceId, RtecEventComm::EventType type, const char * name = "AbsEventProducer") :
		log(getLog(name)), mSourceId(sourceId), mType(type), mWrapper(nullptr) {
		log.i() << "ctor()" << done;
	}

	virtual ~AbsEventProducer() {
		log.i() << "dtor()" << done;
	}

	void connect(EventServiceWrapper * wraper) {
		log.i() << "connect()" << done;
		mWrapper = wraper;
		ACE_SupplierQOS_Factory qos;
		qos.insert(mSourceId, mType, 0, 1);
		log.v() << "getting supplier admin" << done;
		RtecEventChannelAdmin::SupplierAdmin_var supplierAdmin = wraper->getEventService()->for_suppliers();
		log.v() << "obtaining consumer proxy" << done;
		mConsumerProxy = supplierAdmin->obtain_push_consumer();
		log.v() << "activating self (_this()); " << "(servant: " << _refcount_value() << ")" << done;
		RtecEventComm::PushSupplier_var self = _this();
		log.v() << "connecting push supplier (self); " << "(self: " << self->_refcount_value() << ", servant: " << _refcount_value() << ")" << done;
		mConsumerProxy->connect_push_supplier(self.in(), qos.get_SupplierQOS());
		log.i() << "connected; " << "(self: " << self->_refcount_value() << ", servant: " << _refcount_value() << done;
	}

	void disconnect() {
		log.i() << "disconnect()" << done;
		log.v() << "disconnecting consumer proxy; " << "(servant: " << _refcount_value() << ")" << done;
		mConsumerProxy->disconnect_push_consumer();
		PortableServer::POA_ptr poa = mWrapper->getPoa();
		PortableServer::ObjectId_var id = poa->servant_to_id(this);
		log.v() << "deactivating servant; " << "(servant: " << _refcount_value() << ")" << done;
		poa->deactivate_object(id);
		log.i() << "disconnected, removing ref " << "(servant: " << _refcount_value() << ")" << done;
		_remove_ref();
	}

	void disconnect_push_supplier(void) {
		log.w() << "disconnect_push_supplier()" << done;
		mConsumerProxy = RtecEventChannelAdmin::ProxyPushConsumer::_nil();
	}

	void push(const T & data) {
		if (CORBA::is_nil(mConsumerProxy.in())) {
			return;
		}
		RtecEventComm::EventSet eventSet(1);
		eventSet.length(1);
		RtecEventComm::Event &e = eventSet[0];
		e.header.source = mSourceId;
		e.header.type = mType;
		if (!putData(e.data.any_value, data)) {
			return;
		}
		mConsumerProxy->push(eventSet);
		return;
	}

	virtual bool putData(CORBA::Any & event, const T & data) = 0;

protected:
	Log log;

private:
	RtecEventComm::EventSourceID mSourceId;
	RtecEventComm::EventType mType;
	RtecEventChannelAdmin::ProxyPushConsumer_var mConsumerProxy;
	EventServiceWrapper * mWrapper;
};

template <typename T>
class ServerWrapper {
public:
	ServerWrapper(T * servant, const char * name = "ActiveObject") : log(getLog(name)), mServant(servant), mNativeWrapper(nullptr) {
		log.i() << "ctor()" << done;
	}

	~ServerWrapper() {
		log.i() << "dtor()" << done;
		log.v() << "removing ref on servant; " << "(servant: " << mServant->_refcount_value() << ")" << done;
		mServant->_remove_ref();
		log.i() << "instance destroyed" << done;
	}

	typename T::_stub_ptr_type activate(NativeWrapper * nativeWrapper) {
		log.i() << "activate()" << done;
		mNativeWrapper = nativeWrapper;
		log.v() << "activating servant self (_this());" << " (servant: " << mServant->_refcount_value() << ")" << done;
		typename T::_stub_ptr_type servantSelf = mServant->_this();
		log.i() << "activated; " << "(servant: " << mServant->_refcount_value() << ", servant self: " << servantSelf->_refcount_value() << ")" << done;
		return servantSelf;
	}

	void deactivate() {
		log.i() << "deactivate()" << done;
		PortableServer::POA_ptr poa = mNativeWrapper->getPoa();
		log.v() << "obtaining reference to servant servant" << done;
		PortableServer::ObjectId_var id = poa->servant_to_id(mServant);
		log.v() << "deactivating servant; " << "(servant: " << mServant->_refcount_value() << ")" << done;
		poa->deactivate_object(id);
		log.i() << "deactivated; " << "(servant: " << mServant->_refcount_value() << ")" << done;
		delete this;
	}

protected:
	Log log;

private:
	T * mServant;
	NativeWrapper * mNativeWrapper;
};

} /* namespace corbabinding */

#endif /* CORBABINDINGLIB_H_ */
