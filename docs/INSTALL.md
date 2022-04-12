# Table of Contents

* [Source Code](#source-code)
* [Dependencies](#dependencies)
* [PostgreSQL Setup](#postgresql-setup)

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
While not required, it is convenient to set the default user for PostgreSQL to the user `postgres` by setting the following environment variable (OS-specific instructions below):
```
PGUSER = postgres
```
This will cause `psql.exe` command-line utility to use the `postgres` superuser account as the default when the `psql` command is run.

## Windows
> On Windows, setting `PGUSER` can be accomplished by either opening `System Properties`, `Environment Variables...`, and then creating a new environment variable, or by running the following command in an Administrator PowerShell terminal using the built-in `setx.exe` utility:
> ```powershell
> setx PGUSER "postgres" /M
> ```
> For this change to take effect, a system restart will likely be required.
