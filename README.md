SSI-REST
========

This JEE6 Maven WAR project\prototype provides a way to construct
mobile first responsive Web design applications in a quick manner.
It utilizes several of the JEE6 standards along with the current
Web standards to produce rich Web applications that are viewable on any device.
It demenstrates the use of clean semantic HTML5 mark-up with only
server side includes (SSI) directives and the use of HTML5 data attributes, no JSP\JSF tags.
The dynamic content is rendered utilizing JavaScript libraries, JSON, and
Java API for RESTful Web Services (JAX-RS).
This prototype can easily be the foundation for a production Web application.

This Java Enterprise Edition 6 Web application has the following features
-------------------------------------------------------------------------

* Clean semantic HTML5 mark-up
* CSS3 Responsive Web Design with mobile first
* [Modernizr](http://modernizr.com/), [jQuery](http://jquery.com/), polyfill, and plug-in JavaScript libraries
* Utilizing [GroundworkCSS](http://groundworkcss.github.com), a CSS3, SASS, and JavaScript framework that makes responsive design easy
* Server side includes (SSI) with the [Apache Tomcat Java SSI Servlet](http://tomcat.apache.org/tomcat-5.5-doc/ssi-howto.html)
* Dynamic content with [Java API for RESTful Web Services (JAX-RS)](http://en.wikipedia.org/wiki/Java_API_for_RESTful_Web_Services) 1.1
* The [Jackson Java JSON-processor](http://jackson.codehaus.org/) is utilized to extend JAX-RS, along with its annotations
* Utilizing JSON for the initial rendering and for AJAX partial page rendering of dynamic content
* JEE6 Web Profile's [EJB 3.1 Lite](http://www.oracle.com/technetwork/articles/javaee/javaee6overview-part3-139660.html#ejblite) is used as Stateles Session [Enterprise JavaBeans](http://en.wikipedia.org/wiki/Enterprise_JavaBeans) integrated with JAX-RS 
* [Java Persistence API (JPA)](http://en.wikipedia.org/wiki/Java_Persistence_API) 2.1 Entity Java objects with [JavaScript Object Notation (JSON)](http://en.wikipedia.org/wiki/JSON) annotations
* [Contexts and Dependency Injection (CDI)](http://docs.oracle.com/javaee/6/tutorial/doc/giwhb.html) 1.0 is used to inject services and gain access to the JPA Entities
* Demonstrate single code maintenance Web pages
* Incorporate search, edit, add, and delete Web pages for hierarchical data
* Production mode features for browser performance
    * HTML compressing with in-line CSS and JavaScript minification
    * Asset or resource combining to minimize browser request

Minimal Setup
-------------

* NetBeans 7.3 ; or Eclipse JUNO with JBoss Tools or JBoss Developer Studio 6
* GlassFish 3.1.2 with JavaDB (Apache Derby) sun-appserv-samples database
    * For Eclipse or JBoss Developer Studio you will need the [Oracle GlassFish Server Tools part of Oracle Enterprise Pack for Eclipse](http://www.oracle.com/technetwork/developer-tools/eclipse/downloads/index.html)
* Tables: CUSTOMER, DISCOUNT_CODE, MANUFACTURER, MICRO_MARKET, PRODUCT_CODE, PRODUCT, and PURCHASE_ORDER
* If you need to create and load these tables with data the scripts are in the folder: docs/sample-db
    * NetBeans Grab Structures are provided and can be used with the [Database Explorer UI's Recreate Table feature](https://db.netbeans.org/uispecs/DBModuleUISpec.html#2.4.2.2)
    * DDL SQL files are provided to be used with any database client like the Eclipse Database Development perspective
    * There are (del) delimited files produced with the [Derby ij utility](http://db.apache.org/derby/papers/DerbyTut/ij_intro.html) that can be used for importing
        * Use the instructions from [Example importing all data from a delimited file](http://db.apache.org/derby/docs/10.4/tools/rtoolsimport91458.html) with these files

TODO
----

* A code maintenance Web page
* Utilize JSON in rendering Web pages
* A search with results Web page
* A CRUD hierarchical data Web page
* Production mode asset combining
* Add more documentation
