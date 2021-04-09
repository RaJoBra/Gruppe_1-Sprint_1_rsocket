# How to Setup this project

## Anforderungen

- A Java SDK of version 15
- A working Java IDE (I’m using IntelliJ IDEA)
- Maven Version 3.6.3
  - Linux Bash/ZSH shell (https://docs.microsoft.com/en-us/windows/wsl/install-win10)

## Clone repo

Go to the Location you want to clone this repo to and open a powershell <br>
Type `git clone https://github.com/RaJoBra/Gruppe_1-Sprint_1_rsocket.git` <br>

Now Navigate in the Server folder and Download the CLI for rSocket `wget -O rsc.jar https://github.com/making/rsc/releases/download/0.4.2/rsc-0.4.2.jar`

## Open Project in your favorised IDE

## Start Server and Client

Navigate one Powershell into the client and one into the server, then run the command `./mvnw clean package spring-boot:run -DskipTests=true`. <br>
**IMPORTANT**: First start the server then the client

If everything worked, the server will now periodically display a message that it is connected to the client and the client will have a shell.

In the shell in the client you can now enter "request-response" or "stream" to start the corresponding request.

## What you can do now
In the client you can enter the following commands 
* create-trad
  * Will create a new trade 
  * User has to type in a Trade Name
* find-trade
  * User has to type in the Stock Exchange ID he is looking for 
  * Server will send the Stock Exchange if it exist
* delete-trade
  * User has to type in a Stock Exchange ID he want to Delete 
  * Stock Exchange will be deleted
* stream-selection
  * User has to Type in a Time Duration he wants the Trades off   
  * The client sends all the trades that were made in the past time period
  * *Attention*, you may have to type the number twice till the client will regonize it 