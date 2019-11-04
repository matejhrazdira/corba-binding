#include "common.h"

#include "corbabindinglib.h"
#include "Token.h"

#include "../cpp-tao/SimpleS.h"

using namespace testutil;
using namespace corbabinding;

class SimpleObjectImpl: public virtual POA_SimpleIdl::SimpleObject {
public:
	SimpleObjectImpl(const std::string & name) :
		mName(name),
		log(getLog(StringBuilder() << "SimpleObject:" << name)),
		wrapper(new ServerWrapper<SimpleObjectImpl>(this, "SimpleObjectImpl<>"))
	{
		log.v() << "instance created" << done;
	}

	virtual char * getString () {
		return CORBA::string_dup(mName.c_str());
	}

    virtual void destroy () {
    	log.v() << "destroy()" << done;
    	wrapper->deactivate();
    }

	virtual ~SimpleObjectImpl() {
		log.v() << "instance destroyed" << done;
	}

	ServerWrapper<SimpleObjectImpl> * wrapper;

private:
	std::string mName;
	Log log;
};

class SimpleServerImpl: public virtual POA_SimpleIdl::SimpleServer {
public:
	SimpleServerImpl(Token & token, NativeWrapper * nativeWrapper) :
			log(getLog("SimpleServerImpl")), mToken(token), mNativeWrapper(nativeWrapper) {
		log.v() << "instance created (" << (void *) this << ")" << done;
	}

	virtual ~SimpleServerImpl(void) {
		log.v() << "instance destroyed (" << (void *) this << ")" << done;
	}

	virtual char * getString(void) {
		return CORBA::string_dup("Hello from server!!!");
	}

    virtual ::SimpleIdl::StructWithRealArrays * getStructWithArray (const ::SimpleIdl::StructWithRealArrays & inArg) {

    	log.v() << "received: " << inArg.longArr[1] << "; " << inArg.stringMember.in()  << "; " << inArg.structArr[1].stringMember << done;

    	SimpleIdl::StructWithRealArrays * result = new SimpleIdl::StructWithRealArrays();
    	result->stringMember = "Foo Bar Baz";
    	int count = sizeof(result->longArr) / sizeof(result->longArr[0]);
    	for (int i = 0; i < count; i++) {
    		result->longArr[i] = i * 2;
    	}
    	result->structArr[1].stringMember = "Hello from array!";
    	return result;
    }

	virtual void throwException(void) {
		SimpleIdl::SimpleException e;
		e.intMember = 123;
		throw e;
	}

	virtual void exit() {
		log.i() << "exit()" << done;
		Lock lock(mToken);
		mToken.broadcast();
	}

	virtual void throwNestedException (void) {
		SimpleIdl::SimpleServer::NestedException e;
		e.cause = "Method was called.";
		throw e;
	}

    virtual ::SimpleIdl::SimpleObject_ptr getSimpleObject (const char * name) {
    	auto res = new SimpleObjectImpl(name);
    	return res->wrapper->activate(mNativeWrapper);
    }

private:
	Log log;
	Token & mToken;
	NativeWrapper * mNativeWrapper;
};

class TestEventProducer : public AbsEventProducer<SimpleIdl::SimpleUnion> {
public:
	TestEventProducer() : AbsEventProducer(1, 1) {}

	virtual bool putData(CORBA::Any & event, const SimpleIdl::SimpleUnion & data) {
		event <<= data;
		return true;
	}
};

class TestEventConsumer : public AbsEventConsumer<SimpleIdl::SimpleUnion> {
public:
	TestEventConsumer(CORBA::ULong type) : AbsEventConsumer(type) {};

	virtual void onEvent(SimpleIdl::SimpleUnion * event) {
		const char * str = event->strInUnion();
		log.v() << "event : " << (str ? str : "nullptr") << done;
	}
};

struct AppContext {
	Token token;
	bool running = true;
	NativeWrapper * nativeWrapper;
	EventServiceWrapper * eventService1;
	EventServiceWrapper * eventService2;
};

void * runServer(void * arg) {
	Log log = getLog("runServer()");
	AppContext * ctx = (AppContext *) arg;
	{
		Token token;
		Lock lock(token);
		log.v() << "creating server" << done;
		auto server = new ServerWrapper<SimpleServerImpl>(new SimpleServerImpl(token, ctx->nativeWrapper), "SimpleServerImpl<>");
		log.v() << "activating and binding server" << done;
		ctx->nativeWrapper->bindName("SimpleServer", server->activate(ctx->nativeWrapper));
		log.v() << "waiting for server to finish" << done;
		token.wait();
		log.v() << "deactivating server" << done;
		server->deactivate();
		log.v() << "server finished" << done;
	}
	{
		log.v() << "stopping application" << done;
		Lock lock(ctx->token);
		log.v() << "running = false" << done;
		ctx->running = false;
	}
	log.v() << "finished" << done;
	return nullptr;
}

void produceEvents(Log & log, AppContext * ctx, EventServiceWrapper * eventService, const char * name) {
	auto producer = new TestEventProducer();
	producer->connect(eventService);
	while (true) {
		{
			Lock lock(ctx->token);
			if (!ctx->running) {
				break;
			}
		}
		SimpleIdl::SimpleUnion event;
		event.strInUnion((StringBuilder() << name << " : " << currentTime()).c_str());
		log.v() << "pushing event: " << event.strInUnion() << done;
		producer->push(event);
		ACE_OS::sleep(ACE_Time_Value(0, 500*1000));
	}
	log.v() << "producer finished" << done;
	producer->disconnect();
	log.v() << "finished" << done;
}

void * produceEvents1(void * arg) {
	Log log = getLog("produceEvents1()");
	AppContext * ctx = (AppContext *) arg;
	produceEvents(log, ctx, ctx->eventService1, "EventService #1");
	return nullptr;
}

void * produceEvents2(void * arg) {
	Log log = getLog("produceEvents2()");
	AppContext * ctx = (AppContext *) arg;
	produceEvents(log, ctx, ctx->eventService2, "EventService #2");
	return nullptr;
}

int main(int argc, char* argv[]) {
	Log log = getLog("main");
	log.i() << "\n\n\n\n\n" << done;
	log.i() << "server is starting..." << done;
	log.v() << "arguments: '" << byIndex(argv, argc, "', '") << "'" << done;

	try {
		AppContext ctx;
		ctx.nativeWrapper = new NativeWrapper(argc, argv);
		log.v() << "modified arguments: '" << byIndex(argv, argc, "', '") << "'" << done;
		ctx.eventService1 = new EventServiceWrapper(ctx.nativeWrapper, "corbaname:::2809#EventService1");
		ctx.eventService1->connect();
		ctx.eventService2 = new EventServiceWrapper(ctx.nativeWrapper, "corbaname:::2809#EventService2");
		ctx.eventService2->connect();

		ACE_Thread_Manager threadManager;

		threadManager.spawn(runServer, &ctx);
		threadManager.spawn(produceEvents1, &ctx);
		threadManager.spawn(produceEvents2, &ctx);

		TestEventConsumer * consumer = new TestEventConsumer(1);
		consumer->connect(ctx.eventService1);

		threadManager.wait();

		consumer->disconnect();

		log.i() << "exiting" << done;

		delete ctx.eventService1;
		delete ctx.eventService2;
		delete ctx.nativeWrapper;

	} catch (const CORBA::Exception & e) {
		log.e() << "exception " << e << done;
	}

	log.i() << "clean exit" << done;
	return 0;
}
