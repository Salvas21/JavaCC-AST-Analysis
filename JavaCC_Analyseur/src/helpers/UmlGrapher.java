package helpers;

import java.io.FileWriter;
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
   
   public void printResult(FileWriter file) {
	   try {
		   file.write("===== " + className + " =====" + "\n");
		   if (!extendsName.equals("")) {
			   file.write(className + " ..> " + extendsName + "\n");
		   }
		   for (String iName: interfaces) {
			   file.write(className + " ..> " + iName + "\n");
		   }
		   for (String cName: classesUsed) {
			   if (interfaces.contains(cName))
				   continue;
			   if (cName.equals(extendsName) || cName.equals("String"))
				   continue;
			   file.write(className + " --> " + cName + "\n");
		   }
		   file.write("\n");
	   } catch (Exception e) {

	   }
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
