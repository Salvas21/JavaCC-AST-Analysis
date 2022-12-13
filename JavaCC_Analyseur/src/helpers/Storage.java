package helpers;

import java.util.ArrayList;
import java.util.List;

public class Storage {

    private List<Variable> variables = new ArrayList<>();

    public void add(String className, String methodName, String type, String key) {
        Variable var = new Variable();
        var.setClassName(className);
        var.setMethodName(methodName);
        var.setType(type);
        var.setKey(key);
        variables.add(var);
    }

    /**
     * Gets the type of a variable key (so it's class) or else if it doesn't find a variable
     * (might be a static class call so key = Object) return itself
     * @param key
     * @return
     */
    public String getType(String key) {
        Variable def = new Variable();
        def.setType(key);
        return variables.stream().filter(variable -> variable.getKey().equals(key)).findFirst().orElse(def).getType();
    }

    public void removeMethodVars(String methodName) {
        variables.removeIf(v -> v.getMethodName() != null && v.getMethodName().equals(methodName));
    }

    public void removeClassVars(String className) {
        variables.removeIf(v -> v.getClassName().equals(className));
    }

}
