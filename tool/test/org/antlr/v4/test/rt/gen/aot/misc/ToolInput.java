package org.antlr.v4.test.rt.gen.aot.misc;

/**
 * Created by jason on 3/28/15.
 */
public class ToolInput {
    public ToolInput() {
    }

    public ToolInput(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String name;
    public String code;

    @Override
    public String toString() {
        return "ToolInput{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToolInput input = (ToolInput) o;

        if (!name.equals(input.name)) return false;
        return code.equals(input.code);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}
