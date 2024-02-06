# COGIP Client Shell Documentation

This Client shell is built using Java, Spring, and Maven.

## All dependencies

This shell use the following dependencies: Spring Boot, Spring Shell, Lombok, and Spring Web.


## Build
Execute the following command from the parent directory to build the jar file:
```
./mvnw clean install -Dmaven.test.skip=true
```

## Run
From the parent directory, execute the following command to start the application:
```
java -jar target/cogip_CLI-0.0.1-SNAPSHOT.jar
```

You should notice the application starting up,
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::             (v2.0.0.M5)

2017-10-17 16:11:18.065  INFO 21210 --- [           main] com.basaki.Application                   : Starting Application on jdoe-001 with PID 21210 (/Users/john.doe/spring-shell-example/target/spring-shell-example-1.0.0.jar started by john doe in /Users/john.doe/Development/examples/spring-shell-example)
2017-10-17 16:11:18.069  INFO 21210 --- [           main] com.basaki.Application                   : No active profile set, falling back to default profiles: default
2017-10-17 16:11:18.144  INFO 21210 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@34033bd0: startup date [Tue Oct 17 16:11:18 PDT 2017]; root of context hierarchy
2017-10-17 16:11:19.547  INFO 21210 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
shell:>
```

## Commands

The rest-shell provides the following commands:

| Command        | Parameters                                                       | Optional Parameters                                              | Description                                                |
|----------------|------------------------------------------------------------------|------------------------------------------------------------------|------------------------------------------------------------|
| `help`         | -                                                                | -                                                                | Display all the commands and their descriptions.           |
| `login`        | `--username`, `--password`                                       | -                                                                | Login by passing these parameters.                         |
| `allusers`     | -                                                                | `--pretty`                                                       | Get all users, use `--pretty` for a formatted response.    |
| `userid`       | `--id`                                                           | `--pretty`                                                       | Get a user by ID, use `--pretty` for formatting.           |
| `adduser`      | `--username`, `--password`                                       | `--role`                                                         | Create a user with an optional role.                       |
| `edituser`     | `--id`                                                           | `--username`, `--password`, `--role`                             | Edit a user with optional parameters.                      |
| `deluser`      | `--id`                                                           | -                                                                | Delete a user by ID.                                       |
| `allinvoices`  | -                                                                | `--pretty`                                                       | Get all invoices, use `--pretty` for a formatted response. |
| `invoiceid`    | `--id`                                                           | `--pretty`                                                       | Get an invoice by ID, use `--pretty` for formatting.       |
| `addinvoice`   | `--contactId`, `--companyId`                                     | -                                                                | Create an invoice with contact and company ID.             |
| `editinvoice`  | `--id`                                                           | `--companyId`, `--contactId`                                     | Edit an invoice with optional parameters.                  |
| `delinvoice`   | `--id`                                                           | -                                                                | Delete an invoice by ID.                                   |
| `allcontacts`  | -                                                                | `--pretty`                                                       | Get all contacts, use `--pretty` for a formatted response. |
| `contactid`    | `--id`                                                           | `--pretty`                                                       | Get a contact by ID, use `--pretty` for formatting.        |
| `addcontact`   | `--firstname`, `--lastname`, `--phone`, `--email`, `--companyId` | -                                                                | Create a contact with specified parameters.                |
| `editcontact`  | `--id`                                                           | `--firstname`, `--lastname`, `--phone`, `--email`, `--companyId` | Edit a contact with optional parameters.                   |
| `delcontact`   | `--id`                                                           | -                                                                | Delete a contact by ID.                                    |


## Creators
- [Justine](https://github.com/JustineLeleu/)
- [Virginie](https://github.com/vdourson2/)
- [Valentin](https://github.com/Valentin-Lefort)