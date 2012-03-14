#!/bin/sh

echo "--:::traceroute www.umu.se:::--"
ssh lth traceroute www.umu.se
echo "--:::traceroute www.hgo.se:::--"
ssh lth traceroute www.hgo.se

echo "--:::traceroute and find the routers between your machines and www.stanford.edu and www.tu-berlin.de:::--"

ssh lth traceroute www.stanford.edu > tr1
ssh lth traceroute www.tu-berlin.de > tr2
diff -y tr1 tr2
