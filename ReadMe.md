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

## Error

The shell can not be closed in the usual way, work around is to close the powershell and restart it.

## ToDo

- Include method to exit spring boot shell
- End stream after a certain time
