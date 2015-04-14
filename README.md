Pony - Music Streamer (Prototype)
=================================

This is the first version of [Pony Music Streamer](https://github.com/DenisDorokhov/pony). It is here just for history :-) New version has a better architecture, faster scan process, multi-user support, fancy Bootstrap design, more powerful REST API, easier installation, etc. etc.

Installation
------------

What you will need:

* compiled WAR file
* Apache Tomcat server
* MySQL database

Installation steps:

1. Create MySQL database. Give a user all permissions for your newly created database to make automatic installation possible.
2. Create folder named ".pony" in home directory of user that you will use to run the application. In this folder create file named "pony.properties" and define there database connection details:

	db.url=jdbc:mysql://localhost:3306/<b>[YOUR MYSQL DATABASE NAME]</b>?useUnicode=yes&characterEncoding=UTF-8  
	db.username=<b>[YOUR MYSQL USER]</b>  
	db.password=<b>[YOUR MYSQL PASSWORD]</b>  

3. Install compiled WAR file to your Apache Tomcat server.
4. Open the application in your browser. Database schema will be installed automatically.
5. Set up your music folders in application settings window and scan your library.
6. Enjoy listening to you MP3 collection :-)

Development Hints
-----------------

Configuration options for server.web WAR are loaded the following way:

1. Option is looked up in ${user.home}/.pony/pony.properties
2. If option is not found, it's looked up in WEB-INF/pony.properties

Configuration options for server.core integration tests are loaded the following way:

1. Option is looked up in ${user.home}/.pony\_test/pony.properties
2. If option is not found, it's looked up in classpath:pony.properties

Application supports running in Jetty or Tomcat application containers.
