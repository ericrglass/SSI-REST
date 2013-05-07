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
* Utilizing [GroundworkCSS](http://groundworkcss.github.com), [bootstrap-sass](https://github.com/thomas-mcdonald/bootstrap-sass), a CSS3, SASS, and JavaScript framework that makes responsive design easy
* Server side includes (SSI) with the [Apache Tomcat Java SSI Servlet](http://tomcat.apache.org/tomcat-5.5-doc/ssi-howto.html)
* Dynamic content with [Java API for RESTful Web Services (JAX-RS)](http://en.wikipedia.org/wiki/Java_API_for_RESTful_Web_Services) 1.1
* The [Jackson Java JSON-processor](http://jackson.codehaus.org/) is utilized to extend JAX-RS, along with its annotations
* Utilizing JSON for the initial rendering and for AJAX partial page rendering of dynamic content
* JEE6 Web Profile's [EJB 3.1 Lite](http://www.oracle.com/technetwork/articles/javaee/javaee6overview-part3-139660.html#ejblite) is used as Stateles Session [Enterprise JavaBeans](http://en.wikipedia.org/wiki/Enterprise_JavaBeans) integrated with JAX-RS 
* [Java Persistence API (JPA)](http://en.wikipedia.org/wiki/Java_Persistence_API) 2.1 Entity Java objects with [JavaScript Object Notation (JSON)](http://en.wikipedia.org/wiki/JSON) annotations
* [Contexts and Dependency Injection (CDI)](http://docs.oracle.com/javaee/6/tutorial/doc/giwhb.html) 1.0 is used to inject services and gain access to the JPA Entities
* Server side service delegation prior to SSI processing based on the [Delegation Design Pattern](http://en.wikipedia.org/wiki/Delegation_pattern)
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
    * DDL SQL files (.sql) are provided to be used with any database client like the Eclipse Database Development perspective
    * There are (.del) delimited files produced with the [Derby ij utility](http://db.apache.org/derby/papers/DerbyTut/ij_intro.html) that can be used for importing
        * Use the instructions from [Example importing all data from a delimited file](http://db.apache.org/derby/docs/10.4/tools/rtoolsimport91458.html) with these files

Additional Setup
----------------

* JBoss AS 7.1
* A database server, [MySQL](http://www.mysql.com/) is recommended
* The following resources are available for running either GlassFish 3.1.2 or JBoss AS 7.1 with a database server
   * For the MySQL database server in the docs/sample-db folder there is the file MySQLDump.sql that can be used to do a full restore of the ssi_rest database with all of the tables loaded with example data
   * For other database servers in the docs/sample-db folder there are the DDL SQL files (.sql) and the data in comma seperated values files (.csv) for each of the tables
   * The persistence.xml file in the folder src/main/resources/META-INF might need to be updated to reflect the database and JEE6 server being utilized
      * Comment out the \<provider\> and \<jta-data-source\> elements not needed and uncomment the ones needed
      * For example with JBoss AS 7.1 with a database server setup with the ssi_rest database, then you want the following to be uncommented
         * \<provider\>org.hibernate.ejb.HibernatePersistence\</provider\>
         * \<jta-data-source\>java:jboss/ssi_restDS\</jta-data-source\>
* Here is some information about setting up a datasource in JBoss AS 7.1
   * [JBoss AS 7.1 DataSource configuration](https://docs.jboss.org/author/display/AS71/DataSource+configuration)
   * [vkslabs.com - Adding MySQL data source to JBoss 7.X](http://vkslabs.com/adding-mysql-data-store-to-jboss-7-x/)
* Here is some information about setting up a datasource in GlassFish 3.1.2
   * [Oracle GlassFish Server 3.1 Quick Start Guide - Configuring an Oracle Data Source](http://docs.oracle.com/cd/E18930_01/html/821-2432/gkyan.html)
   * [itSolutionsForAll - How to create and configure JNDI DataSource for MySQL in GlassFish 3.1.1 Server](http://itsolutionsforall.com/datasource_jpa.php)

TODO
----

* A code maintenance Web page
* Utilize JSON in rendering Web pages
* A search with results Web page
* A CRUD hierarchical data Web page
* Production mode asset combining
* Add more documentation
