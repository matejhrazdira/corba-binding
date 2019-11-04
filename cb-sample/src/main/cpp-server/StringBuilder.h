#ifndef UTILS_STRINGBUILDER_H_
#define UTILS_STRINGBUILDER_H_

#include <string>
#include <sstream>
#include <iomanip>

namespace testutil {

template <typename T> class Appendable;

class StringBuilder {
public:
	StringBuilder() {}

	template<typename T> inline StringBuilder & operator<<(const T &value) {
		mBuffer << value;
		return *this;
	}

	template<typename T> inline StringBuilder & operator<<(const Appendable<T> & value) {
		value.append(*this);
		return *this;
	}

	template<typename T> inline T operator>>(T (*func)(const StringBuilder &)) {
		return func(*this);
	}

	inline const std::string str() const {
		return mBuffer.str();
	}

	inline const char * c_str() const {
		mTmpStr = mBuffer.str();
		return mTmpStr.c_str();
	}
private:
	std::stringstream mBuffer;
	mutable std::string mTmpStr;
};

inline const std::string str(const StringBuilder & sb) {
	return sb.str();
}

inline const char * c_str(const StringBuilder & sb) {
	return sb.c_str();
}

template <typename T>
class Appendable {
public:
	Appendable(const T & impl) : mImpl(impl) {}
	void append(StringBuilder & builder) const {
		mImpl.append(builder);
	};
private:
	T mImpl;
};

template <typename T>
class IterableWrapper {
public:
	IterableWrapper(const T & iterable, const std::string & separator) : mIterable(iterable), mSeparator(separator) {}
	void append(StringBuilder & builder) const {
		auto endIt = mIterable.end();
		auto it = mIterable.begin();
		if (it != endIt) {
			builder << *(it++);
		}
		while (it != endIt) {
			builder << mSeparator << *(it++);
		}
	}
private:
	const T & mIterable;
	const std::string & mSeparator;
};

template <typename T> inline Appendable<IterableWrapper<T>> byIterator(const T & iterable, const std::string & separator = ", ") {
	return Appendable<IterableWrapper<T>>(IterableWrapper<T>(iterable, separator));
}

template <typename T, typename U>
class ArrayWrapper {
public:
	ArrayWrapper(const T & array, const U lenght, const std::string & separator) : mArray(array), mLength(lenght), mSeparator(separator) {}
	void append(StringBuilder & builder) const {
		if (mLength > 0) {
			builder << mArray[0];
		}
		for (U i = 1; i < mLength; i++) {
			builder << mSeparator << mArray[i];

		}
	}
private:
	const T & mArray;
	const U mLength;
	const std::string & mSeparator;
};

template <typename T, typename U> inline Appendable<ArrayWrapper<T, U>> byIndex(const T & array, const U length, const std::string & separator = ", ") {
	return Appendable<ArrayWrapper<T, U>>(ArrayWrapper<T, U>(array, length, separator));
}

template <typename T>
class ByteArrayWrapper {
public:
	ByteArrayWrapper(const void * & array, const T lenght) : mArray(array), mLength(lenght) {}
	void append(StringBuilder & builder) const {
		const unsigned char * in = (const unsigned char *) mArray;
		if (mLength > 0) {
			printSingleChar(builder, in[0]);
		}
		for (T i = 1; i < mLength; i++) {
			builder << " ";
			printSingleChar(builder, in[i]);
		}
		builder << std::setw(0) << std::setfill(' ') << std::dec;
	}
private:
	const void * mArray;
	const T mLength;

	inline void printSingleChar(StringBuilder & builder, const unsigned char & c) const {
		builder << std::setw(2) << std::setfill('0') << std::hex << (unsigned) c;
	}
};

template <typename T> inline Appendable<ByteArrayWrapper<T>> rawBytes(const void * array, const T length) {
	return Appendable<ByteArrayWrapper<T>>(ByteArrayWrapper<T>(array, length));
}

} /* namespace testutil */

#endif /* UTILS_STRINGBUILDER_H_ */
