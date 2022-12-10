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

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void addCallee(String method) {
        callees.add(method);
    }

    public void print() {
        System.out.println("\t" + methodName + " calls :");
        for (String callee : callees) {
            System.out.println("\t\t" + callee);
        }
    }
}