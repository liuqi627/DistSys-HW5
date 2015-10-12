# DistSys-HW5
For submission of HW5 - Qi Liu and Luona Guo

Group member <br/>
-------------
Qi Liu, Luona Guo <br/>
________________________________________________________________________________________________________________________________

Compile Instruction <br/>
-------------
use javac to compile all files<br />
________________________________________________________________________________________________________________________________

Test Instruction <br/>
-------------
Use Client to add/remove/lookup servers<br />
________________________________________________________________________________________________________________________________

Load Balancing<br/>
---------------
Round Robin Policy<br/>

We're using Round Robin policy to access conversion server from all of its duplicas. For example, assume we have three lbs<->oz conversion servers. For the first request we return the first of the three in the list (before return we check whether it's alive, see Fault Tolerance). The second gets the second server. The third gets the third server. The fourth request gets the first server. 


Fault Tolerance<br/>
---------------
1. Conversion Server crashed:<br/>
    When the client send LOOKUP command. Before we return a server, we check its availability. If it's alive, return the alive one. If it's crashed(timeout or no responding or response wrong ack info), try the next one in the list, until we get an alive one. Or we indicate "none is available for this type of service" if all servers are down.<br/> 

2. Proxy Server crashed:<br/>
    Proxy Server may not work for 3 reasons:<br/>
    (1)conversion servers related are down;
    (2)proxy server itself is down;
    (2)conversion server and proxy server are all down;
    the resolution is pretty the same with situation 1;
________________________________________________________________________________________________________________________________

