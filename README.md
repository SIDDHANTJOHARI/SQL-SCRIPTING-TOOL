# SQL-SCRIPTING-TOOL
This is java based dekstop application which provides user an gui to interact with Java-derby database.

Technology: JAVA.

Database: java-derby.

## Benifits of using this Application:
1) It feels comfortable to use Graphical User Interface to perform  CRUD operations on java-derby database rather than command line.
2) It can generate Data Layer of any table just in one click.
3) Platform Independent as developed in java.
4) User dont have to worry writing Data Layer again again for many application.


## Getting Started.
Prerequisite: Java 8 or above, Java-derby.

Steps to use the Application:
## Step 1(Download jar and Extract it)
Download the [dowload.jar](https://github.com/SIDDHANTJOHARI/SQL-SCRIPTING-TOOL/blob/master/download.jar) File.

Extract it anywhere you want to.

After Extracting this structure is created

/SQLGUITOOL.jar
  
/lib
  
## Step 2(Instantiate Java-derby Server)
In this step as I have include java-dery as prerequisites it means you have already had downloaded it.

Now Start the the server in the parent folder of your database.

For example:

If your project structure is /project/db then start the server inside the project folder.

## Step 3(Execute  the Application)

For Executing the Application write the following command.
```java 
java -jar SQLGUITOOL.jar
```
![alt tag](https://github.com/SIDDHANTJOHARI/SQL-SCRIPTING-TOOL/blob/master/images/Screenshot%20from%202020-06-29%2022-47-41.png)


you will see this GUI.

Now Click on menu Button you will see these options.

1.Connect 
 This is to connect you to database.
After Clicking on Connect button.
you will see.
![alt tag](https://github.com/SIDDHANTJOHARI/SQL-SCRIPTING-TOOL/blob/master/images/Screenshot%20from%202020-06-29%2022-54-53.png)



Now you have to fill PortNumber, Server Name/IP , and Name of the database you want to create  or thename of a existence database to which you want to connect then click on Create/Click Button.

In my case I have a database named tmplacements and port Number 1527 and server Name :- localhost.

After Clicking on Connect You will see this window.
![alt tag](https://github.com/SIDDHANTJOHARI/SQL-SCRIPTING-TOOL/blob/master/images/Screenshot%20from%202020-06-29%2023-03-34.png)

There are 4 section here.
1. SQL Statement:-To fire SQL query on your database.
2. Output:- In this section you will see out put of your executed statement.
3. Error:- This Section will show error in your sql statement or error related to connection.
4. Database Tables:- This section contains all the tables which is in your database.

2.Disconnect
3.RAD Settings
4.Quit
