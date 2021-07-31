# GLUtils

[![Java CI with Gradle](https://github.com/Over-Run/GLUtils/actions/workflows/gradle.yml/badge.svg)](https://github.com/Over-Run/GLUtils/actions/workflows/gradle.yml)  
Utils for developing OpenGL for Java:coffee:.

[If there are any bugs, tell us!](https://github.com/Over-Run/GLUtils/issues/new)

## Use for depending on

```groovy
dependencies {
    implementation "io.github.over-run:glutils:1.0.0"
}
```

## Example

```java
import org.overrun.glutils.*;
public class Example {
    static GlProgram prg;
    public static void main(String[] args) {
        init();
        ClassLoader cl = Example.class.getClassLoader();
        try {
            prg = new GlProgram();
            prg.createVsh(ShaderReader.lines(cl, "shaders/scene.vsh"));
            prg.createFsh(ShaderReader.lines(cl, "shaders/scene.fsh"));
            prg.link();
        } catch (Throwable e) {
            if (prg != null) {
                prg.close();
            }
        }
        render();
    }
}
```

## Who are using

- [OverRun Organization](https://github.com/Over-Run/)

##

<table>
<tr>
<td>-1.0,1.0</td><td>0.0,1.0</td><td>1.0,1.0</td>
</tr>
<tr>
<td>-1.0,0.0</td><td>0.0,0.0</td><td>1.0,0.0</td>
</tr>
<tr>
<td>-1.0,-1.0</td><td>0.0,-1.0</td><td>1.0,-1.0</td>
</tr>
</table>
