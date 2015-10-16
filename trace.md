Client:<br/>
`````````
liuqi627: ~/hw5/DistSys-HW5 $ java Client 127.0.0.1 11111
Now you can type your command...(format: <unit1> <unit2> <amount>)
lbs oz 12
Connection to discovery server created!
 
ip and port are: 127.0.0.1 2222<br/>
Connection to conversion server created!<br/>
 
the amount is: 192.0<br/>
 
Now you can type your command...<br/>
lbs kg 3<br/>
Connection error occured. Auto-connect to standby discoveryserver...<br/>
Now you can type your command...<br/>
lbs kg 3<br/>
Connection to discovery server created!<br/>
 
ip and port are: 127.0.0.1 3333<br/>
Connection to conversion server created!<br/>
the amount is: 1.3636363636363635<br/>
 
Now you can type your command...<br/>
 ``````

DiscoveryServer(master):<br/>
`````
Started server on port 11111<br/>
 
Accepted connection from client<br/>
input is add lbs oz 127.0.0.1 2222<br/>
[SUCCESS] add new conversion type<br/>
this while loop is ended<br/>
 
Accepted connection from client<br/>
input is add lbs kg 127.0.0.1 3333<br/>
[SUCCESS] add new conversion type<br/>
this while loop is ended<br/>
 
Accepted connection from client<br/>
input is lookup lbs oz<br/>
[SUCCESS] 127.0.0.1 2222<br/>
this while loop is ended<br/>

Accepted connection from client<br/>
input is add lbs oz 127.0.0.1 5555<br/>
[SUCCESS] add new server<br/>
this while loop is ended<br/>
^Cliuqi627: ~/hw5/DistSys-HW5 $  <br/>

`````
DiscoveryServer(standby):<br/>
`````
Started server on port 22222<br/>
 
Accepted connection from client<br/>
input is add lbs oz 127.0.0.1 2222<br/>
[SUCCESS] add new conversion type<br/>
this while loop is ended<br/>
 
Accepted connection from client<br/>
input is add lbs kg 127.0.0.1 3333<br/>
[SUCCESS] add new conversion type<br/>
this while loop is ended<br/>
 
Accepted connection from client<br/>
input is add lbs oz 127.0.0.1 5555<br/>
[SUCCESS] add new server<br/>
this while loop is ended<br/>
 
Accepted connection from client<br/>
input is lookup lbs kg<br/>
``````

ConversionServer(lbs <-> kg):<br/>
`````
liuqi627: ~/hw5/DistSys-HW5 $ java CvsLbsKg 3333<br/>
Started server on port 3333<br/>
 
Accepted connection from client<br/>
Received message: check<br/>
 
Accepted connection from client<br/>
Received message: lbs kg 3<br/>
`````

ConversionServer(lbs <-> oz):<br/>
`````
liuqi627: ~/hw5/DistSys-HW5 $ java CvsLbsOz 2222<br/>
Started server on port 2222<br/>
 
Accepted connection from client<br/>
Received message: check<br/>
 
Accepted connection from client<br/>
Received message: lbs oz 12<br/>
`````
