package helpers;

import java.util.HashSet;
import java.util.Set;


public class UmlGrapher {
   private String className = "";
   private Set<String> interfaces = new HashSet<String>();
   private Set<String> classesUsed = new HashSet<String>();
   private String extendsName = "";
   
   public UmlGrapher(String className) {
	   this.className = className;
   }
   
   public void printResult() {
	   System.out.println("===== " + className + " =====");
	    if (!extendsName.equals("")) {
			System.out.println(className + " ..> " + extendsName);
	    }
	    for (String iName: interfaces) {
			System.out.println(className + " ..> " + iName);
	    }
	    for (String cName: classesUsed) {
	      if (interfaces.contains(cName))
	      	continue;
	      if (cName.equals(extendsName) || cName.equals("String"))
	      	continue;
			System.out.println(className + " --> " + cName);
	    }
	    System.out.println();
   }
   
   public void setExtendName(String name) {
	   this.extendsName = name;
   }
   
   public void addInterface(String interfaceName) {
	   interfaces.add(interfaceName);
   }
   
   public void addClass(String className) {
	   classesUsed.add(className);
   }
   
   public String getName() {
	   return className;
   }
}
