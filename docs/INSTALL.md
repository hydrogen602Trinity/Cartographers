# Table of Contents

- [Table of Contents](#table-of-contents)
- [Source Code](#source-code)
  - [Windows](#windows)
  - [MacOS](#macos)
- [Dependencies](#dependencies)
  - [Windows](#windows-1)
  - [MacOS](#macos-1)
- [PostgreSQL Setup](#postgresql-setup)
  - [Windows](#windows-2)
  - [MacOS](#macos-2)
- [Database Setup](#database-setup)

# Source Code
To get started, you can clone the repository using the following command:
```
git clone https://github.com/hydrogen602Trinity/Cartographers.git
```

## Windows
>If using Windows, you may first need to install [Git for Windows](https://gitforwindows.org/). This can be done with the [Windows Pachage Manager CLI](https://winget.run/)* using the following command:
>```
>winget install -e --id Git.Git
>```
>
>\* *If following this guide on Windows, it is recommended to download winget if it is not already on your system, as the following instructions assume that winget is installed.*

## MacOS

>If git is not installed, install it with either
>```
>xcode-select –install
>```
>or
>```
>brew install git
>```
>\* *If following this guide on MacOS, it is recommended to download homebrew if it is not already installed on your system, as the following instructions assume that homebrew is installed. Instructions are available at `https://brew.sh/`*

# Dependencies
To run this project, there are several third-party applications that will need be installed on your system. These are listed below in addition to some optional applications which make development of the project easier.

- **Required**
    - Java 17
    - NodeJS
    - PostgreSQL
- **Recommended**
    - Visual Studio Code
        - *This project makes use of the VS Code workspace feature to enable development of the project. As a result, most instructions for navigating and running different project components are only applicable if VS Code is installed.*
    - Postman
        - *Although Postman requires an account to use, it is free and provides a helpful interface for testing http requests.*

## Windows
> The following is a list of terminal commands for installing required and recommended applications using the Windows Package Manager.
>
> **Required**
> ```
> winget install -e --id Microsoft.OpenJDK.17
> ```
> ```
> winget install -e --id OpenJS.NodeJS
> ```
> ```
> winget install -e --id PostgreSQL.PostgreSQL
> ```
>
> **Recommended**
> ```
> winget install -e --id Microsoft.VisualStudioCode
> ```
> ```
> winget install -e --id Postman.Postman
> ```

## MacOS
> The following is a list of terminal commands for installing required and recommended applications using homebrew.
>
> **Required**
> ```
> brew install openjdk@17
> ```
> ```
> brew install node
> ```
> ```
> brew install postgresql@13
> ```
>
> **Recommended**
> ```
> brew install --cask visual-studio-code
> ```
> ```
> brew install --cask postman
> ```



# PostgreSQL Setup
Run
```
psql -U postgres -c "\du"
```
If postgres is not `Superuser`, then skip this section on setting `PGUSER = postgres`.

While not required, it is convenient to set the default user for PostgreSQL to the user `postgres` by setting the following environment variable:
```
PGUSER = postgres
```
This will cause the 'psql' command-line utility to use the postgres superuser account as the default when the psql command is run.

## Windows
> On Windows, setting `PGUSER` can be accomplished by either opening `System Properties`, `Environment Variables...`, and then creating a new environment variable, or by running the following command in an Administrator PowerShell terminal:
> ```powershell
> [Environment]::SetEnvironmentVariable('PGUSER','postgres',[EnvironmentVariableTarget]::Machine)
> ```
>
> Next, ensure that the `psql` utility is on your system path. You can do this by running the following PowerShell command:
> ```powershell
> [Environment]::GetEnvironmentVariable('PATH') -split ';'
> ```
>
> If `C:\Program Files\PostgreSQL\14\bin\` was not listed, then you can add it to your system path by running the following command in PowerShell as an Administrator:
> ```powershell
> [Environment]::SetEnvironmentVariable('PATH',[Environment]::GetEnvironmentVariable('PATH')+';C:\Program Files\PostgreSQL\14\bin\',[EnvironmentVariableTarget]::Machine)
> ```
> To finish applying these changes, a system restart will likely be required.

## MacOS
> Setting `PGUSER` can be accomplished by running
> ```
> echo "export PGUSER=postgres" >> ~/.bash_profile
> ```

# Database Setup
To create the project database, run `psql` in a terminal and sign-in to the postgres superuser account. If installed with winget, the password for the postgres account should be `postgres` by default. If `PGUSER` was not defined previously, then you can sign-in by running `psql` with a specified user:
```
psql -U postgres
```

After signing in, you should first create a new user called `ctmadmin` with the password `s$cret`. Then, create a new database called `ctmdb` with `ctmadmin` as the database's owner:
```
CREATE ROLE ctmadmin WITH CREATEDB PASSWORD 's$cret';
```
> If this fails with 
> ```
> permission denied to create role
> ```
> then another account may be the database superuser.
> Run
> ```
> \du
> ```
> And identify the role with the `Create role` attribute. Exit postgres with
> ```
> \q
> ```
> And then sign in with
> ```
> psql -U the-username-with-create-role
> ```
> 

```
CREATE DATABASE ctmdb OWNER ctmadmin;
```

