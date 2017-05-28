#!/bin/bash
###########################################################################

echo "Executing sample"
# exec 2> /dev/null

gnome-terminal --disable-factory -x tao_cosnaming -ORBDottedDecimalAddresses 1 -ORBListenEndpoints iiop://:2809 &
perl -e "select(undef,undef,undef,2);"

gnome-terminal --disable-factory -x tao_rtevent -ORBDottedDecimalAddresses 1 -ORBInitRef NameService=corbaloc:iiop::2809/NameService &
perl -e "select(undef,undef,undef,1);"


gnome-terminal --disable-factory -x ./server -ORBDottedDecimalAddresses 1 -ORBInitRef NameService=corbaloc:iiop::2809/NameService &
perl -e "select(undef,undef,undef,2);"

gnome-terminal --disable-factory -x tao_nslist -ORBDottedDecimalAddresses 1 -ORBInitRef NameService=corbaloc:iiop::2809/NameService &

read -p "Press any key to terminate...
" -n1 -s

echo "Terminating"

kill 0



