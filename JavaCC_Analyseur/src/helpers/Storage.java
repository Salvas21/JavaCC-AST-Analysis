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

    public String getType(String key) {
        return variables.stream().filter(variable -> variable.getKey().equals(key)).findFirst().get().getType();
    }

    public void removeMethodVars(String methodName) {
        variables.removeIf(v -> v.getMethodName() != null && v.getMethodName().equals(methodName));
    }

    public void removeClassVars(String className) {
        variables.removeIf(v -> v.getClassName().equals(className));
    }

}
