#!/bin/bash

#1
echo "==== netstat -i ===="
ssh lth netstat -i
echo -e "==== END ====\n"

#2
echo "Where do all your packets go when they are not destined for the local network? What is the localhost address?" 
echo "==== netstat -rna ===="
ssh lth netstat -rna
echo "Packets not destined for the local network (mask 0.0.0.0) goes through the gateway." 
echo "route for the lo interface is implicit and not declared on the routing table (anymore). localhost = 127.0.0.1"
echo -e "==== END ====\n"

#3
echo "==== netstat -a | uniq -w 4 ===="
ssh lth netstat -a | uniq -w 4
echo -e "==== END ====\n"
