package ast_analyseur.visitors;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import ast_analyseur.helpers.ClassMethodInfo;
import ast_analyseur.helpers.MethodCalls;

public class CustomVisitor extends ASTVisitor {
	private ArrayList<ClassMethodInfo> classMethodInfos = new ArrayList<ClassMethodInfo>();
	private Stack<ClassMethodInfo> currentClassMethodInfo = new Stack<ClassMethodInfo>();
	private Stack<MethodCalls> currentMethodCalls = new Stack<MethodCalls>();
	
	public void print(FileWriter resultsFile) {
		for (var c : classMethodInfos) {
			c.print(resultsFile);
		}
	}

	private void startClassInfo(String name) {
		var info = new ClassMethodInfo();
		info.setName(name);
		currentClassMethodInfo.push(info);
	}

	private void startMethodCalls(String name) {
		if (currentClassMethodInfo.size() == 0) return;
		var calls = new MethodCalls();
		calls.setMethodName(name);
		currentMethodCalls.push(calls);
	}

	private void addMethodCall(String type, String name) {
		if (currentMethodCalls.size() == 0) return;
		currentMethodCalls.peek().addCallee(type + "." + name);
	}

	private void endMethodCalls() {
		if (currentMethodCalls.size() == 0) return;
		currentClassMethodInfo.peek().addCalls(currentMethodCalls.pop());
	}

	private void endClassInfo() {
		if (currentClassMethodInfo.size() == 0) return;
		classMethodInfos.add(currentClassMethodInfo.pop());
	}
	
	public boolean visit(TypeDeclaration node) {
		if (!node.isInterface()) startClassInfo(node.getName().toString());
		return super.visit(node);
	}

	public void endVisit(TypeDeclaration node) {
		endClassInfo();
	}

	public boolean visit(MethodDeclaration node) {
		startMethodCalls(node.getName().toString());
		return super.visit(node);
	}

	public void endVisit(MethodDeclaration node) {
		endMethodCalls();
	}
	
	public boolean visit(MethodInvocation node) {
		if (node.resolveMethodBinding() != null) {
			addMethodCall(node.resolveMethodBinding().getDeclaringClass().getName(), node.getName().toString());
		}
		return super.visit(node);
	}
	
	

}
