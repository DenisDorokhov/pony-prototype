Pony
====

Configuration options for server.web WAR are loaded the following way:

1. Option is looked up in ${user.home}/.pony/pony.properties
2. If option is not found, it's looked up in WEB-INF/pony.properties

Configuration options for server.core integration tests are loaded the following way:

1. Option is looked up in ${user.home}/.pony\_test/pony.properties
2. If option is not found, it's looked up in classpath:pony.properties

To make server.web work you will need to define path to your MP3 library by adding "library.path" option to your configuration file. It's a temporary solution until we have settings UI in the application.

To avoid out of memory errors when running GWT project from IDE, please use the following VM options: -Xmx256m -XX:MaxPermSize=96M

Application supports running with Jetty application container.
