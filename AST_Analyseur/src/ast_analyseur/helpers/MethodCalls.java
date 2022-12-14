package ast_analyseur.helpers;

import java.io.FileWriter;
import java.io.IOException;
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

    public void print(FileWriter file)throws IOException {
        if (callees.size() == 0) {
            file.write("\t\t\t\t" + "Method: " + methodName + "() calls no method(s)\n");
            return;
        }
        file.write("\t\t\t\t" + "Method: " + methodName + "() calls : \n");
        for (String callee : callees) {
            file.write("\t\t\t\t\t" + callee + "()\n");
        }
    }
}