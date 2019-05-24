# Code and Build

## Website Artifacts

- [index.html](view/index.html): async invoke Web API to display user & server IP addresses.
- [favicon.ico](view/favicon.ico): the website icon.

---

## Lambda Function for Web API

### Java Classes

- [MyIpApp](api-lambda/src/main/java/kalinchih/my_ip/MyIpApp.java): the Lambda handler class to retrieve user and server IP addresses.
- [MyIpRequest](api-lambda/src/main/java/kalinchih/my_ip/MyIpRequest.java): to wrap the request.
- [MyIpResponse](api-lambda/src/main/java/kalinchih/my_ip/MyIpResponse.java): to wrap the response.
- [ServerIpNotFoundError](api-lambda/src/main/java/kalinchih/my_ip/ServerIpNotFoundError.java): to wrap the inner exception when something wrong in retrieving server IP.
- [UserIpNotFoundError](api-lambda/src/main/java/kalinchih/my_ip/UserIpNotFoundError.java): to wrap the error message when something wrong in retrieving user IP.

### Unit Tests

- [TBD](TBD): the unit test class.
- [TBD](Codecoverage): unit test coverage report.

### How to Build It?

Use the following [Maven](https://maven.apache.org/) command to run all unit tests and release a build.

```
mvn package
```

### Artifact Folder

- api-lambda/artifact/
