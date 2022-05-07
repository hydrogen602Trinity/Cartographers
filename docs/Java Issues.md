# Table of Contents

  - [Javadoc or Java not found](#javadoc-or-java-not-found)


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
