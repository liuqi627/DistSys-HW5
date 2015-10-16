Client:
liuqi627: ~/hw5/DistSys-HW5 $ java Client 127.0.0.1 11111
Now you can type your command...(format: <unit1> <unit2> <amount>)
lbs oz 12
Connection to discovery server created!
 
ip and port are: 127.0.0.1 2222
Connection to conversion server created!
 
the amount is: 192.0
 
Now you can type your command...
lbs kg 3
Connection error occured. Auto-connect to standby discoveryserver...
Now you can type your command...
lbs kg 3
Connection to discovery server created!
 
ip and port are: 127.0.0.1 3333
Connection to conversion server created!
the amount is: 1.3636363636363635
 
Now you can type your command...
 

DiscoveryServer(master):

Started server on port 11111
 
Accepted connection from client
input is add lbs oz 127.0.0.1 2222
[SUCCESS] add new conversion type
this while loop is ended
 
Accepted connection from client
input is add lbs kg 127.0.0.1 3333
[SUCCESS] add new conversion type
this while loop is ended
 
Accepted connection from client
input is lookup lbs oz
[SUCCESS] 127.0.0.1 2222
this while loop is ended

Accepted connection from client
input is add lbs oz 127.0.0.1 5555
[SUCCESS] add new server
this while loop is ended
^Cliuqi627: ~/hw5/DistSys-HW5 $  

DiscoveryServer(standby):
Started server on port 22222
 
Accepted connection from client
input is add lbs oz 127.0.0.1 2222
[SUCCESS] add new conversion type
this while loop is ended
 
Accepted connection from client
input is add lbs kg 127.0.0.1 3333
[SUCCESS] add new conversion type
this while loop is ended
 
Accepted connection from client
input is add lbs oz 127.0.0.1 5555
[SUCCESS] add new server
this while loop is ended
 
Accepted connection from client
input is lookup lbs kg

ConversionServer(lbs <-> kg):
liuqi627: ~/hw5/DistSys-HW5 $ java CvsLbsKg 3333
Started server on port 3333
 
Accepted connection from client
Received message: check
 
Accepted connection from client
Received message: lbs kg 3

ConversionServer(lbs <-> oz):
liuqi627: ~/hw5/DistSys-HW5 $ java CvsLbsOz 2222
Started server on port 2222
 
Accepted connection from client
Received message: check
 
Accepted connection from client
Received message: lbs oz 12

