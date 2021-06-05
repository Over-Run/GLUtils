# GLUtils
Utils for OpenGL for Java:coffee:.

[If there are any bugs, tell us!](https://github.com/Over-Run/GLUtils/issues/new)

## Use for depending on

```groovy
dependencies {
    implementation "io.github.over-run:glutils:0.2.0"
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
            prg = new GlProgram()
            prg.createVsh(ShaderReader.lines(cl, "shaders/sense.vsh"));
            prg.createFsh(ShaderReader.lines(cl, "shaders/sense.fsh"));
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
