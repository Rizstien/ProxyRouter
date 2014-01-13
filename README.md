ProxyRouter
===========

ProxyRouter acts as a proxy server to route requests from one domain to other yet isolating them to communicate directly.
It is best suited for auditing and reporting purposes in an environment where many web apps communicating with each other

DB Setup
========
Run db scripts first. That will create 2 tables.
One for logging and other for url/handler_class/destination_url mapping.
Add new handler classes as per requirment for each new context path and add an entry of it in REQUEST_ROUTER table.


Getting Started
===============
Go to 'ProxyServer.java' and run it as Java Project or alternativley run 'proxy-server.jar'.
This will start run embedded-jetty server and load all handler classes mapped with thier respective context paths as mentioned in db.

Testing
=======

Now generate an HTTP POST request via any REST client or run 'TestProxyServer.java' for testing.
There is 'application.properties' file for db configurations.

Suggestions are always welcomed
