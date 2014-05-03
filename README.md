Pony
====

Configuration options for server.web WAR are loaded the following way:

1. Option is looked up in ${user.home}/.pony/pony.properties
2. If option is not found, it's looked up in WEB-INF/pony.properties

Configuration options for server.core integration tests are loaded the following way:

1. Option is looked up in ${user.home}/.pony\_test/pony.properties
2. If option is not found, it's looked up in classpath:pony.properties

To avoid out of memory errors when running GWT project from IDE, please use the following VM options: -Xmx256m -XX:MaxPermSize=96M

Application supports running in Jetty or Tomcat application containers.

For those who want to use sway.fm media keys support on Mac OS X: you need to install Play Button iTunes Patch available at http://www.thebitguru.com/projects/iTunesPatch.
