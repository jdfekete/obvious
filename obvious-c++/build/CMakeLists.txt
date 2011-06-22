################################################################################
#
#	@mainpage	obvious-c++
#
#	@file			CMakeList.txt
#
#	@brief			Configuration file for CMAKE.
#
#	@version		1.0
#
#	@date			21 may 2011
#
#	@author			Jean-Daniel Fekete
#
#	@section		Institution
#					Centre de Recherche INRIA Saclay--Ile-de-France
#							http://www.inria.fr/centre-de-recherche-inria/saclay-ile-de-france
#					Equipe AVIZ
#							http://www.aviz.fr/
#					Bat 490, Universite Paris-Sud
#					91405 Orsay Cedex
#					France
#
#	@section		Licence
#					???
#
#	@section		Copyright
#					(c) by INRIA, Jean-Daniel Fekete Jean-Daniel.Fekete@inria.fr, May 2011, 2011, version 1.0, Licence ???
#
################################################################################


# Minimum CMake version ########################################################
cmake_minimum_required(VERSION 2.8.0)

IF(COMMAND cmake_policy)
       cmake_policy(SET CMP0003 NEW)
ENDIF(COMMAND cmake_policy)

# Project name #################################################################
PROJECT(obvious)

SET (H_SOURCES
	../include/obvious/data/Table.hpp
	../include/obvious/data/Schema.hpp
	../include/obvious/data/event/TableListener.hpp
	../include/obvious/util/SmartPointer.hpp
	../include/obvious/impl/Column.hpp
	../include/obvious/impl/TableImpl.hpp
)

# Library source code ##########################################################
SET (CXX_SOURCES
	../src/obvious/data/Table.cxx
	../src/obvious/data/TableListener.cxx
#	../src/obvious/data/Schema.cxx
	../src/obvious/util/SmartPointer.cxx
	../src/obvious/impl/Column.cxx
	../src/obvious/impl/TableImpl.cxx
)


# Libraries ####################################################################
SET(optionalLibs)
SET(requiredLibs)


# Headers of the project #######################################################
INCLUDE_DIRECTORIES(../include)
INCLUDE_DIRECTORIES(../include/obvious)
INCLUDE_DIRECTORIES(../include/obvious/data)
INCLUDE_DIRECTORIES(../include/obvious/data/event)


# Create the library ###########################################################
ADD_LIBRARY (obvious STATIC ${H_SOURCES} ${CXX_SOURCES})
#TARGET_LINK_LIBRARIES(obvious ${requiredLibs} ${optionalLibs})


# Main program #################################################################
ADD_EXECUTABLE(test-table ../test/test-table.cxx)
TARGET_LINK_LIBRARIES(test-table obvious)
