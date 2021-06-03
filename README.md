# GLUtils
Utils for OpenGL for Java:coffee:.

## Example

```java
import org.overrun.glutils.*;
public class Example {
    public static void main(String[] args) {
        init();
        ClassLoader cl = Example.class.getClassLoader();
        try (GlProgram prg = new GlProgram()) {
            prg.createVsh(ShaderReader.lines(cl, "shaders/sense.vsh"));
            prg.createFsh(ShaderReader.lines(cl, "shaders/sense.fsh"));
            prg.link();
        }
        render();
    }
}
```
