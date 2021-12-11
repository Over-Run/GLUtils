# GLUtils ![GitHub](https://img.shields.io/github/license/Over-run/GLUtils)

[![Java CI with Gradle](https://github.com/Over-Run/GLUtils/actions/workflows/gradle.yml/badge.svg?branch=1.x&event=push)](https://github.com/Over-Run/GLUtils/actions/workflows/gradle.yml)  
![GitHub all releases](https://img.shields.io/github/downloads/Over-Run/GLUtils/total)

![GitHub issues](https://img.shields.io/github/issues/Over-Run/GLUtils)
![GitHub pull requests](https://img.shields.io/github/issues-pr/Over-Run/GLUtils)  
![GitHub closed issues](https://img.shields.io/github/issues-closed/Over-Run/GLUtils)
![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed/Over-Run/GLUtils)

![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/Over-Run/GLUtils)
![Maven Central](https://img.shields.io/maven-central/v/io.github.over-run/glutils)
![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/io.github.over-run/glutils?server=https%3A%2F%2Fs01.oss.sonatype.org)

![Java Version](https://img.shields.io/badge/Java%20Version-8-red)

![GitHub Discussions](https://img.shields.io/github/discussions/Over-Run/GLUtils)

Utils for developing OpenGL for Java:coffee:.

[If there are any bugs, tell us!](https://github.com/Over-Run/GLUtils/issues/new)

## Use for depending on

```groovy
dependencies {
    implementation "io.github.over-run:glutils:1.6.0"
}
```

## Example

[https://github.com/squid233/lwjgl-example](https://github.com/squid233/lwjgl-example)

```java
import org.overrun.glutils.*;
public class Example {
    static GLProgram prg;
    public static void main(String[] args) {
        init();
        ClassLoader cl = Example.class.getClassLoader();
        prg = new GLProgram();
        prg.createVsh(ShaderReader.lines(cl, "shaders/scene.vsh"));
        prg.createFsh(ShaderReader.lines(cl, "shaders/scene.fsh"));
        prg.link();
        render();
    }
}
```

## Who are using

- [Overrun Organization](https://github.com/Over-Run/)
- [Funny Developers](https://github.com/Funny-Developers/)
