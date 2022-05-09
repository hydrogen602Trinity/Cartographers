# Table of Contents

- [Table of Contents](#table-of-contents)
- [Basic Layout of the Project & Running it](#basic-layout-of-the-project--running-it)
  - [Running the Frontend](#running-the-frontend)
  - [Running the Backend](#running-the-backend)
  - [Running the Database](#running-the-database)
- [Documentation](#documentation)
- [Javadoc or Java not found](#javadoc-or-java-not-found)
  - [Windows](#windows)
  - [macOS/Linux](#macoslinux)
  
# Basic Layout of the Project & Running it
The project consists of three systems: the frontend, backend, and database. All three of these need to be running for the project to fully run locally, but it is possible to run the frontend alone locally and use the production backend and database.

## Running the Frontend
The frontend is launched with
```bash
npm start
```
After starting, it should automatically open a browser tab, but if not, navigate to `http://localhost:3000` in your browser.

## Running the Backend
To run the backend, open the `Java Projects` > `Java: Spring Boot Dashboard` tab in VS Code and click the play button on `ctmrepository-backend`. It should (after loading up) display the message in the debug console: `Started Application`

## Running the Database
After installing postgres, it should always be running in the background.

# Documentation


# Javadoc or Java not found
If there is an error that `javadoc` or `java` cannot be found, then it might be the case that the `JAVA_HOME` environment variable is not set or is pointing to the wrong location.

## Windows
> First, check to see if `JAVA_HOME` is defined by running the following PowerShell command:
> ```powershell
> [Environment]::GetEnvironmentVariable('JAVA_HOME')
> ```
>
> If `JAVA_HOME` was undefined or not set to Java 17, you can update it to match the default install location used by winget by running the following in a PowerShell terminal that was opened as an administrator:
> ```powershell
> [Environment]::SetEnvironmentVariable('JAVA_HOME','C:\Program Files\Microsoft\jdk-17.0.2.8-hotspot\',[EnvironmentVariableTarget]::Machine)
> ```
> 

## macOS/Linux
> To check the current location of your Java installation, run the following bash command:
>```bash
>echo $JAVA_HOME
>```
>
>If this prints nothing, then you need to set `JAVA_HOME`. First locate your Java 17 installation. The contents of the home folder for Java should include the following files/directories:
>```
>README.md            include              share
>LICENSE              bin                  libexec
>```
>Once you have the home directory for your Java installation, run the following command if you use `zsh` on macOS:
> ```sh
> echo 'export JAVA_HOME="<your path to java goes here>"' >> ~/.zshrc
> ```
>
> Alternatively, if you use `bash` with macOS then the following command should be used instead:
> ```bash
> echo 'export JAVA_HOME="<your path to java goes here>"' >> ~/.bash_profile
> ```
> 
> For Linux users, you will need to run a similar command to the previous one, but replacing `~/.bash_profile` with `~/.bashrc` as shown:
> ```bash
> echo 'export JAVA_HOME="<your path to java goes here>"' >> ~/.bashrc
> ```
>
> **Note:** If you don't know your default shell on macOS/Linux, you can run:
> ```bash
> ps -p $$
> ```
