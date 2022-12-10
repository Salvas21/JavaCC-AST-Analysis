package helpers;

import java.util.ArrayList;

public class ClassMethodInfo {
    private String name;
    private ArrayList<MethodCalls> calls;

    public ClassMethodInfo() {
        name = "";
        calls = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCalls(MethodCalls calls) {
        this.calls.add(calls);
    }

    public void print() {
        System.out.println(name);
        for (var call : calls) {
            call.print();
        }
    }

}
