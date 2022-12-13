package helpers;

import java.util.ArrayList;
import java.util.Collections;

public class ClassMethodInfo {
    private String name;
    private ArrayList<MethodCalls> calls;

    public ClassMethodInfo() {
        name = "";
        calls = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCalls(MethodCalls calls) {
        this.calls.add(calls);
    }

    public void print() {
        printClassName();
        if (calls.size() == 0) {
            System.out.println("\t\tHas no method(s)");
            return;
        }
        System.out.println("\t\tGraph of direct calls between methods");
        for (var call : calls) {
            call.print();
        }
        printCoupling();
    }

    private void printCoupling() {
        if (calls.size() == 0) return;
        var totalCalls = new ArrayList<String>();
        for (var call : calls) {
            totalCalls.addAll(call.getCallees());
        }
        System.out.println("\t\tCoupling between classes:");
        if (totalCalls.size() == 0) {
            System.out.println("\t\t\tNo coupling between classes for this class");
            return;
        }

        var printedCalls = new ArrayList<String>();
        for (var call : totalCalls) {
            if (!printedCalls.contains(call)) {
                System.out.println("\t\t\tHas called: " + call + "() " + Collections.frequency(totalCalls, call) + " time(s)");
                printedCalls.add(call);
            }
        }
    }

    private void printClassName() {
        System.out.println();
        System.out.println("\tClass: " + name);
    }

}
