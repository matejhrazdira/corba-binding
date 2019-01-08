cmake_minimum_required(VERSION 3.1)

project(cb-sample)

set(CMAKE_CXX_STANDARD 11)

find_package(JNI)

if (JNI_FOUND)
	message (STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
	message (STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
	include_directories(${JNI_INCLUDE_DIRS})
else()
	message(FATAL_ERROR "Cannot find JNI." )
endif()

# library with idl stuff
file(GLOB IDLS "src/main/idl/*.idl")
foreach(GENERATE_IDL ${IDLS})
	get_filename_component(IDLNAME ${GENERATE_IDL} NAME_WE)
	set(OUT_NAME src/main/cpp-tao/${IDLNAME})
	list(APPEND IDL_COMPILED_FILES ${OUT_NAME}C.h ${OUT_NAME}C.cpp ${OUT_NAME}S.h ${OUT_NAME}S.cpp)
	add_custom_command(
		OUTPUT ${OUT_NAME}C.h ${OUT_NAME}C.cpp ${OUT_NAME}S.h ${OUT_NAME}S.cpp
		COMMAND tao_idl -aw -Cw -GI -o ./src/main/cpp-tao/ -Isrc/main/idl ${GENERATE_IDL}
		COMMENT "Processing ${GENERATE_IDL}"
	)
endforeach(GENERATE_IDL)
set_source_files_properties(${IDL_COMPILED_FILES} PROPERTIES GENERATED TRUE)
add_library(tao-idl-generated SHARED ${IDL_COMPILED_FILES})

# dependencies
set(CORBA_LIBS TAO_CosNaming TAO_RTEvent_Serv TAO_RTEvent_Skel TAO_RTEvent TAO_Svc_Utils TAO_Messaging TAO_PI TAO_CodecFactory TAO_Valuetype TAO_PortableServer TAO_IORTable TAO_AnyTypeCode TAO ACE)
set(ALL_LIBS tao-idl-generated ${CORBA_LIBS})

# server
file(GLOB SERVER_SRC "src/main/cpp-server/*.cpp")
add_executable(server ${SERVER_SRC})
target_link_libraries(server ${ALL_LIBS})

# jni library
file(GLOB CPP_SRC "src/main/cpp/*.cpp")
file(GLOB CB_SRC "src/main/cpp-cb/*.cpp")
add_library(jnilibs SHARED ${CPP_SRC} ${CB_SRC})
target_link_libraries(jnilibs ${ALL_LIBS})

