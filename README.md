# DistSys-HW5
For submission of HW5 - Qi Liu and Luona Guo

Group member <br/>
-------------
Qi Liu, Luona Guo <br/>


Compile Instruction <br/>
-------------
use javac to compile all files<br />
`````
javac CvsLbsOz.java
javac CvsLbsKg.java
javac DiscoveryServer.java
javac Client.java
`````


Test Instruction <br/>
-------------
To testify our load balancing and fault tolerance work, please follow my steps for convenience:<br />
1.run DiscoveryServer on port 11111:
``````
java DiscoveryServer 11111
``````
2.run Lbs<->Oz conversion server and make replica
`````
java CvsLbsOz 2222
java CvsLbsOz 3333
java CvsLbsOz 4444
java CvsLbsOz 5555
`````
3.use Client to add servers we launched<br />
``````
java Client 127.0.0.1 11111
add lbs oz 127.0.0.1 2222
add lbs oz 127.0.0.1 3333
add lbs oz 127.0.0.1 4444
add lbs oz 127.0.0.1 5555
``````
4.lookup lbs oz servers, load balancing works here, the return value varies according to Round Robin
``````
look up lbs oz
look up lbs oz
look up lbs oz
look up lbs oz
look up lbs oz
look up lbs oz
``````
5.shut down 127.0.0.1 2222, here we simply close the console listening on port 2222<br />
6.lookup lbs oz servers, the "127.0.0.1 2222" server should disappear
``````
look up lbs oz
look up lbs oz
look up lbs oz
look up lbs oz
look up lbs oz
look up lbs oz
``````



Load Balancing<br/>
---------------
Round Robin Policy<br/>

We're using Round Robin policy to access conversion server from all of its duplicas. For example, assume we have three lbs<->oz conversion servers. For the first request we return the first of the three in the list (before return we check whether it's alive, see Fault Tolerance). The second gets the second server. The third gets the third server. The fourth request gets the first server. 


Fault Tolerance<br/>
---------------
1. Conversion Server crashed:<br/>
    When the client send LOOKUP command. Before we return a server, we check its availability. If it's alive, return the alive one. If it's crashed(timeout or no responding or response wrong ack info), try the next one in the list, until we get an alive one. Or we indicate "none is available for this type of service" if all servers are down.<br/> 
    We use a HashMap(key:conversion type, value:index to return) to record which server to use next.

2. Proxy Server crashed:<br/>
    Proxy Server may not work for 3 reasons:<br/>
    (1)conversion servers related are down;<br />
    (2)proxy server itself is down;<br />
    (3)conversion server and proxy server are all down;<br />
    the resolution is pretty the same with situation 1;

3. Discovery Server crashed:<br/>
    Simply connect to a replica Discovery Server. May need to implement this in Client side<br/> 
________________________________________________________________________________________________________________________________

