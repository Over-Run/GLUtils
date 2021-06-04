# GLUtils
Utils for OpenGL for Java:coffee:.

## Use for depending on

```properties
GLUTILS_VERSION=0.1.0
```
```groovy
dependencies {
    implementation "io.github.over-run:glutils:${GLUTILS_VERSION}"
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
        prg = new GlProgram()
        prg.createVsh(ShaderReader.lines(cl, "shaders/sense.vsh"));
        prg.createFsh(ShaderReader.lines(cl, "shaders/sense.fsh"));
        prg.link();
        render();
    }
    static void close() {
        prg.close();
    }
}
```
