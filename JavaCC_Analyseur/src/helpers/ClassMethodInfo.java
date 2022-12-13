package helpers;

import java.io.FileWriter;
import java.io.IOException;
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

    public void print(FileWriter file) {
        try {
            printClassName(file);
            if (calls.size() == 0) {
                file.write("\t\tHas no method(s)\n");
                return;
            }
            file.write("\t\tGraph of direct calls between methods\n");
            for (var call : calls) {
                call.print(file);
            }
            printCoupling(file);
        } catch (Exception e) {

        }
    }

    private void printCoupling(FileWriter file) throws IOException {
        if (calls.size() == 0) return;
        var totalCalls = new ArrayList<String>();
        for (var call : calls) {
            totalCalls.addAll(call.getCallees());
        }
        file.write("\t\tCoupling between classes:\n");
        if (totalCalls.size() == 0) {
            file.write("\t\t\tNo coupling between classes for this class\n");
            return;
        }

        var printedCalls = new ArrayList<String>();
        for (var call : totalCalls) {
            if (!printedCalls.contains(call)) {
                file.write("\t\t\tHas called: " + call + "() " + Collections.frequency(totalCalls, call) + " time(s)\n");
                printedCalls.add(call);
            }
        }
    }

    private void printClassName(FileWriter file)throws IOException {
        file.write("\n");
        file.write("\tClass: " + name + "\n");
    }

}
