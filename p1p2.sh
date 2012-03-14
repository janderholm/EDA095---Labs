#!/bin/bash

echo "==== nslookup www.lth.se ===="
ssh lth nslookup www.lth.se
echo -e "==== END ====\n"

echo "==== nslookup 130.235.16.34 ===="
ssh lth nslookup 130.235.16.34
echo -e "==== END ====\n"

echo "==== tracepath www.umu.se ===="
ssh lth tracepath www.umu.se
echo -e "==== END ====\n"

echo "==== tracepath www.hgo.se ===="
ssh lth tracepath www.hgo.se
echo -e "==== END ====\n"

echo "==== tracepath www.tu-berlin.de ===="
ssh lth tracepath www.tu-berlin.de
echo -e "==== END ====\n"

echo "==== tracepath www.tu-berlin.de ===="
ssh lth tracepath www.tu-berlin.de
echo -e "==== END ====\n"

echo "==== netstat -i ===="
netstat -i
echo -e "==== END ====\n"

echo "==== netstat -rna ===="
netstat -rna
echo -e "==== END ====\n"

echo "==== netstat -a ===="
netstat -a
echo -e "==== END ====\n"



