package ast_analyseur.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import ast_analyseur.visitors.CustomVisitor;

public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(window.getShell(), "AST_Analyseur", "Hello, Eclipse world");

		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		// Loop over all projects

		for (IProject project : projects) {
			try {
				if (!project.isOpen())
					continue;

				exploreProject(project);


			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private void exploreProject(IProject project) throws CoreException {
		if (!project.isOpen()) // Analyse only open projects
			return;

		System.out.println("Working in project " + project.getName());
		// check if we have a Java project
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			explorePackage(javaProject);

			System.out.println("Project (" + project.getName() + ") done");
		}
	}

	private void explorePackage(IJavaProject javaProject) throws JavaModelException {

		IPackageFragment[] packages = javaProject.getPackageFragments();
		try {
			System.err.println("CHANGE ABSOLUTE PATH OF RESULTS FILE IN 'SampleHandler.java -> explorePackage'");
			String path = "/Users/salvas/Documents/Universite/3_Session_Automne_2022/Analyse_programme/Projet-analyse/AST_Analyseur/results.txt";
			FileWriter resultsFile = new FileWriter(path, false);
			for (IPackageFragment mypackage : packages) {
				// Package fragments include all packages in the
				// classpath
				// We will only look at the packages from the source
				// folder

				if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
					System.out.println("_________________________________________");
					System.out.println("Package " + mypackage.getElementName());
					exploreCompiplationUnit(mypackage, resultsFile);
				}
			}
			resultsFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void exploreCompiplationUnit(IPackageFragment mypackage, FileWriter resultsFile) throws JavaModelException {
		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
			System.out.println("Parsing " + unit.getElementName());

			CompilationUnit parse = parseUnit(unit);
			CustomVisitor visitor = new CustomVisitor();
			try {
				parse.accept(visitor);
				visitor.print(resultsFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private CompilationUnit parseUnit(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS12);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		return (CompilationUnit) parser.createAST(null);
	}
}
