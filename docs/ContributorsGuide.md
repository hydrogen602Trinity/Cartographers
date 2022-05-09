# Table of Contents

- [Table of Contents](#table-of-contents)
- [Initial Setup](#initial-setup)
  - [Install Java 17](#install-java-17)
  - [Opening in VSCode](#opening-in-vscode)
  - [Installing VSCode Extensions](#installing-vscode-extensions)
- [Basic Layout of the Project & Running it](#basic-layout-of-the-project--running-it)
  - [Running the Frontend](#running-the-frontend)
  - [Running the Backend](#running-the-backend)
  - [Running the Database](#running-the-database)
- [Javadoc or Java not found](#javadoc-or-java-not-found)
  - [Windows](#windows)
  - [macOS/Linux](#macoslinux)
- [npm Conflicting dependencies](#npm-conflicting-dependencies)
  
# Initial Setup

## Install Java 17

**Install Java 17 before doing anything for best results**. See `INSTALL.md` for more information on how to install everything.

## Opening in VSCode

Open VSCode, then go to `File` > `Open Workspace from File...` and select `cartographers.code-workspace` in the root folder of the repository.

## Installing VSCode Extensions

On the left side of VSCode, open the `Extensions` tab and then enter `@recommended` in the extension search bar and install the listed dependencies. This list of dependencies should include things like `npm`, `Spring Boot Extension`, and `Extension Pack for Java`.

# Basic Layout of the Project & Running it

The project consists of three systems: the frontend, backend, and database. All three of these need to be running for the project to fully run locally, but it is possible to run the frontend alone locally and use the production backend and database.

## Running the Frontend

The frontend is launched with
```bash
npm start
```
After starting, it should automatically open a browser tab, but if not, navigate to `http://localhost:3000` in your browser.

To run the project without having to run the backend and database locally, it is possible to use the deployed backend by running
```bash
npm start-api-remote
```
instead.

## Running the Backend

To run the backend, open the `Java Projects` > `Java: Spring Boot Dashboard` tab in VS Code and click the play button on `ctmrepository-backend`. It should (after loading up) display the message in the debug console: `Started Application`

## Running the Database

After installing postgres, it should always be running in the background.

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

# npm Conflicting dependencies

The current frontend has conflicting dependencies due to projects using outdated versions of React. Newer versions of `npm` may complain about this, so add the `--legacy-peer-deps` flag to the end of the `npm` install commands.
