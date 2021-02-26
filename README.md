# Vaccine transportation simulator

This simulator is very simple, it defines a set of itineraries to move vaccine lots from one manufacturing plant to a target city. It offers data loaded from a file (see resources folder), and two APIs, one to get the current list of predefined transportation cost, and one POST to submit those records to the Kafka topic named: `vaccine.transportation`.

The interesting parts of this code is to use Microprofile reactive messaging to product Kafka records with a key. The values coming from a list. The app exposes a OpenAPI user interface at: [http://127.0.0.1:8080/q/swagger-ui](http://127.0.0.1:8080/swagger-ui).

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

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 

```shell
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`


## Deploy to OpenShift

* Deploy the configuration via the config map and secret for schema registry URL:

 ```shell
 oc apply -f src/main/kubernetes/configmap.yaml
 oc apply -f src/main/kubernetes/secret.yaml
 ```

* Deploy the app using quarkus extension:

 ```shell
 mvn clean generate-sources package -Dquarkus.kubernetes.deploy=true 
 ```

## Deploy the application with gitops

The build generates a `target/kubernetes/openshift.yaml` that was modified to remove the triggers and put into a separate [gitops repository](https://github.com/ibm-cloud-architecture/vaccine-gitops). To use this gitops repository (and the apps/order-mgt), for any modified code, build the docker image and push to docker registry.

```shell
docker build -f src/main/docker/Dockerfile.jvm -t ibmcase/vaccine-transport-simulator:1.0.0 .
docker push ibmcase/vaccine-transport-simulator:1.0.0
```

 ## Related guides

- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- RESTEasy JAX-RS ([guide](https://quarkus.io/guides/rest-json)): REST endpoint framework implementing JAX-RS and more