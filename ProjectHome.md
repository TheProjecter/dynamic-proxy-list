# Description #
Some services allow only a certain amount of connections per IP. Using this Library, you can easily circumvent this limitation: With each method call .getNextProxy() you will get another Proxy out of your customized proxy list. This list is read from a file and scanned for changes every 10 seconds.
After using the proxy for your request, a feedback to the framework is possible to make sure, that inactive proxies won't show up again.

# Getting started #
First you will need to set up a text file containing the initial proxies used. Let's call it proxies.txt and add the entries
```
SOCKS,8.8.8.8:1234
HTTP,8.8.4.4:1343
```
(Please substitute with your proxy list found on your favourite free-proxy-list site).
dynamic-proxy-list supports SOCKS and HTTP proxies.

After setting up your proxies.txt file, instantiate ProxyListContainer and start playing :)
```
ProxyListContainer plc = new ProxyListContainer("proxies.txt", 1); 
//second argument of constructor is threshold, after how many unsuccessful attempts the proxy-entry should be removed. 

ProxyEntry e = plc.getNextProxy();

//connect to your service using e.getProxy(). 

//do something..

e.reportProxyUsage(true); 
//true means, you successfully used this proxy. false -> well.. guess..? :) 
```

Not that hard, right? The library is thread safe. Moreover, a Timer which scans the proxy-file every 10 seconds is instantiated.. Changes will be detected and included in the data base.