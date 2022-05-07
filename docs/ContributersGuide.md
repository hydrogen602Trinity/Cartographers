# Table Of Contents

- [Table Of Contents](#table-of-contents)
- [Java Issues](#java-issues)
  - [Javadoc or Java not found](#javadoc-or-java-not-found)


# Java Issues
## Javadoc or Java not found

If there is an error that `javadoc` or `java` cannot be found, then it might be the case that the `JAVA_HOME` environment variable is not set. In that case, run
```bash
echo $JAVA_HOME
```
on Linux/macOS.

If this prints nothing, then you need to set `JAVA_HOME`. First locate your java installation. Where this is depends on your OS and installer. The contents of the home folder for java should look like:
```
README.md            include              share
LICENSE              bin                  libexec
```
Once you have your java folder directory

- Linux with Bash
  
  ```bash
  echo 'export JAVA_HOME="<your path to java goes here>"' >> ~/.bashrc
  ```
- MacOS with Bash

  ```bash
  echo 'export JAVA_HOME="<your path to java goes here>"' >> ~/.bash_profile
  ```
- Linux/MacOS with Zsh:

  ```bash
  echo 'export JAVA_HOME="<your path to java goes here>"' >> ~/.zshrc
  ```

> Note: If you don't know your default shell on Linux/MacOS, run
> ```bash
> ps -p $$
> ```
