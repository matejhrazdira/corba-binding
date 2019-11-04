#ifndef SRC_COMMON_H_
#define SRC_COMMON_H_

#include "TestUtil.h"

#include <ace/String_Base.h>
#include <tao/Exception.h>

#include <algorithm>
#include <iostream>

using namespace testutil;

inline Log getLog(const std::string & tag) { return Log(tag, std::cout, std::cout, std::cerr); }
inline Log getLog(const StringBuilder & tag) { return Log(tag.c_str(), std::cout, std::cout, std::cerr); }

inline StringBuilder & operator<<(StringBuilder & sb, const CORBA::Exception & e) {
	std::string info = e._info().c_str();
	std::replace(info.begin(), info.end(), '\n', ' ');
	return sb << "CORBA::Exception: " << e._name() << "; " << info;
}

#endif /* SRC_COMMON_H_ */
