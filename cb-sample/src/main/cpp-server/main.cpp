#include "common.h"

#include "NativeWrapper.h"
#include "EventProducer.h"

#include "../cpp-tao/SimpleS.h"

using namespace testutil;
using namespace corbabinding;

class SimpleServerImpl: public virtual POA_SimpleIdl::SimpleServer {
public:
	SimpleServerImpl(Token & token) :
			log(getLog("ServerImpl")), mToken(token) {
	}

	virtual ~SimpleServerImpl(void) {
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

private:
	Log log;
	Token & mToken;
};

class TestEventProducer : public EventProducer<SimpleIdl::SimpleUnion> {
public:
	TestEventProducer() : EventProducer(1, 1) {}
};

int main(int argc, char* argv[]) {
	Log log = getLog("main");
	log.i() << "server is starting..." << done;
	log.v() << "arguments: '" << byIndex(argv, argc, "', '") << "'" << done;

	try {
		Token t;
		Lock lock(t);
		SimpleServerImpl serverImpl(t);
		NativeWrapper wrapper(argc, argv);
		log.v() << "modified arguments: '" << byIndex(argv, argc, "', '") << "'" << done;
		SimpleIdl::SimpleServer_var server = serverImpl._this();
		wrapper.bindName("SimpleServer", server.in());
//		t.wait();
		TestEventProducer producer;
		producer.connect(wrapper);
		while (true) {
			SimpleIdl::SimpleUnion event;
			event.strInUnion(currentTime().c_str());
			producer.push(event);
			ACE_OS::sleep(ACE_Time_Value(0, 500*1000));
		}
		log.i() << "exiting" << done;
	} catch (const CORBA::Exception & e) {
		log.e() << "exception " << e << done;
	}

	log.i() << "clean exit" << done;
	return 0;
}
