package helpers;

import java.util.ArrayList;
import java.util.List;

public class MethodCalls {
    private String methodName;
    private List<String> callees; // Class.Method TODO : this.method, method


    public MethodCalls() {
        callees = new ArrayList<>();
        methodName = null;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void addCallee(String method) {
        callees.add(method);
    }

    public boolean hasMethodName() {
        return methodName != null;
    }

    public void print() {
        System.out.println("\t" + methodName + " calls :");
        for (String callee : callees) {
            System.out.println("\t\t" + callee);
        }
    }
}