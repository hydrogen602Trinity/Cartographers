# Table of Contents

* [Source Code](#source-code)
* [Dependencies](#dependencies)
* [PostgreSQL Setup](#postgresql-setup)
* [Database Setup](#database-setup)

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

# Dependencies
To run this project, the following will need to be installed:
- Java 17
- NodeJS
- PostgreSQL

## Windows
> To install these packages on Windows, simply run the following commands:
> ```
> winget install -e --id Microsoft.OpenJDK.17
> ```
> ```
> winget install -e --id OpenJS.NodeJS
> ```
> ```
> winget install -e --id PostgreSQL.PostgreSQL
> ```

# PostgreSQL Setup
While not required, it is convenient to set the default user for PostgreSQL to the user `postgres` by setting the following environment variable:
```
PGUSER = postgres
```
This will cause `psql.exe` command-line utility to use the `postgres` superuser account as the default when the `psql` command is run.

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

# Database Setup
To create the project database, run `psql` in a terminal and sign-in to the `postgres` superuser account. If installed with `winget`, the password for the `postgres` account should be `postgres` by default. If `PGUSER` was not defined previously, then you can sign-in by running `psql` with a specified user:
```
psql -U postgres
```

After signing in, create a new user called `ctmadmin` with the password `s$cret` and create a database named `ctmdb` with the new user with the following commands:
```
CREATE ROLE ctmadmin CREATEDB WITH PASSWORD 's$cret';
```
```
CREATE DATABASE ctmdb OWNER ctmadmin;
```
