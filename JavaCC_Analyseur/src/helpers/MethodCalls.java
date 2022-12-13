package helpers;

import java.util.ArrayList;
import java.util.List;

public class MethodCalls {
    private String methodName;
    private List<String> callees;


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

    public List<String> getCallees() {
        return callees;
    }

    public void addCallee(String method) {
        callees.add(method);
    }

    public void print() {
        if (callees.size() == 0) {
            System.out.println("\t\t\t\t" + "Method: " + methodName + "() calls no method(s)");
            return;
        }
        System.out.println("\t\t\t\t" + "Method: " + methodName + "() calls : ");
        for (String callee : callees) {
            System.out.println("\t\t\t\t\t" + callee + "()");
        }
    }
}