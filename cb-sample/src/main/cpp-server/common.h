#ifndef SRC_COMMON_H_
#define SRC_COMMON_H_

#include "TestUtil.h"

#include <ace/String_Base.h>
#include <tao/Exception.h>

#include <iostream>

using namespace testutil;

inline Log getLog(const std::string & tag) { return Log(tag, std::cout, std::cout, std::cerr); }

inline StringBuilder & operator<<(StringBuilder & sb, const CORBA::Exception & e) {
	return sb << "CORBA::Exception: " << e._name() << " /// " << e._info().c_str() << " /// " << e._rep_id();
}

#endif /* SRC_COMMON_H_ */
