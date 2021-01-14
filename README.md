# Vaccine transportation simulator

This simulator is very simple, it defines a set of itineraries to move vaccine lots from one manufacturing plant to a target city. It offers data loaded from a file (see resources folder), and two APIs, one to get the current list of predefined transportation cost, and one POST to submit those records to the Kafka topic named: ``.

The interesting parts of this code is to use Microprofile reactive messaging to product Kafka records with a key. The values coming from a list. The app exposes a OpenAPI user interface at: [http://127.0.0.1:8080/swagger-ui](http://127.0.0.1:8080/swagger-ui).

The code was started with OpenShift DO CLI:

```shell
odo create --starter
# then select the quarkus template
```

## Requirements

To compile and run this demo you will need:

- JDK 1.8+
- GraalVM

### Configuring GraalVM and JDK 1.8+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 1.8+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image-guide)
for help setting up your environment.

## Building the application

Launch the Maven build on the checked out sources of this demo:

> ./mvnw install

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

> ./mvnw quarkus:dev


### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it:

> ./mvnw install

Then run it:

> java -jar ./target/getting-started-1.0-SNAPSHOT-runner.jar

Have a look at how fast it boots, or measure the total native memory consumption.

### Run Quarkus as a native executable

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

> ./mvnw install -Dnative

After getting a cup of coffee, you'll be able to run this executable directly:

> ./target/getting-started-1.0-SNAPSHOT-runner


## Deploy to OpenShift

* Deploy the configuration via the config map:

 ```shell
 oc apply -f src/main/kubernetes/configmap.yaml
 ```

* Deploy the app using quarkus extension:

 ```shell
 mvn clean package -Dquarkus.kubernetes.deploy=true 
 ```