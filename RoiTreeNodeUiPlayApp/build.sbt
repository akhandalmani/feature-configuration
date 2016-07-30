name := """RoiWareHouseProject"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  "mysql" % "mysql-connector-java" % "5.1.28",
  "org.json"%"org.json"%"chargebee-1.0",
  "org.codehaus.jettison" % "jettison" % "1.1",
  "org.codehaus.jackson"%"jackson-core-asl"%"1.9.13",
  "org.codehaus.jackson"%"jackson-mapper-asl"%"1.9.13",
  "org.webjars" % "angularjs" % "1.2.13",
   "com.getusroi" % "roi-framework" % "1.0.0-SNAPSHOT",
   "com.google.code.gson" % "gson" % "2.6.2",
  cache,
  "org.osgi" % "org.osgi.core" % "4.2.0"
 
)


resolvers += (
    "Local Maven Repository" at ""+Path.userHome.asFile.toURI.toURL+".m2/repository"
)


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator
