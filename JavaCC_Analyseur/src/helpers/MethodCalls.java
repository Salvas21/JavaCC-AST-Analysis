package helpers;

import java.util.ArrayList;
import java.util.List;

public class MethodCalls {
	private String className;
    private String methodName;
    private List<String> callees;

    public MethodCalls() {
        callees = new ArrayList<>();
        className = null;
        methodName = null;
    }

    public MethodCalls(String className, String methodName) {
    	this.className = className;
        this.methodName = methodName;
        callees = new ArrayList<>();
    }
    
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    
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

    public void setCallees(List<String> callees) {
        this.callees = callees;
    }
    
    public void addCallee(String method) {
    	callees.add(method);
    }
    
    public boolean hasMethodName() {
    	return methodName != null;
    }
    
    public void print() {
    	System.out.println(className + "." + methodName + " calls : ");
    	for (String s : callees) {
    		System.out.println("\t" + s);
    	}
    }
}