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
javac PrxOzKg.java
`````


Test Instruction <br/>
-------------
To testify our load balancing and fault tolerance work, please follow my steps:<br />
1.run DiscoveryServer(master) on port 11111 and DiscoveryServer(standby) on port 22222:
``````
java DiscoveryServer 11111
java DiscoveryServer 22222
``````
2.run 3 Lbs<->Oz conversion servers in 3 consoles to make replica,they'll auto-notify master Discovery server, and if added successfully master node will notify standby to update the table as well
`````
java CvsLbsOz 2222
java CvsLbsOz 3333
java CvsLbsOz 4444
`````
3.run 3 Lbs<->Kg conversion servers in 3 consoles to make replica
`````
java CvsLbsKg 5555
java CvsLbsKg 6666
java CvsLbsKg 7777
`````
4.use Client to lookup servers we just launched, the client connects to master node, if no responding, connect to standby node whose port number is 22222<br />
``````
java Client 127.0.0.1 11111
``````
or if master node(127.0.0.1 11111)crashed, use standby one:
``````
java Client 127.0.0.1 22222
``````
5.lookup lbs oz servers in client, load balancing works here, the return value varies according to Round Robin
``````
look up lbs oz
look up oz lbs
look up lbs oz
look up lbs kg
look up lbs kg
look up kg oz
``````
6.shut down 127.0.0.1 2222, here we hit ctrl+c in the console that is listening on port 2222, then lookup lbs oz servers, the "127.0.0.1 2222" server should disappear
``````
look up lbs oz
look up lbs oz
look up lbs oz
``````
7.launch oz<->kg proxy server(3 arguments are: proxy port, oz<->lbs port, lbs<->kg port)
``````
java PrxOzKg 9999 3333 5555
``````
8.lookup in client, the proxy server ip and port appears
``````
lookup oz kg
``````
9.shut down (ctrl + c in console running it) master discovery server, use client to connect to standby node
``````
java Client 127.0.0.1 22222
``````
10.test lookup for oz<->kg(proxy server) or oz<->lbs(conversion server)
``````
lookup oz kg
lookup lbs oz
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
    For Discovery Server, we have two: master and standby. When the master node crashes, simply connect to the standby Discovery Server. The standby Discovery Server maintains the discovery table in master node<br/> 


