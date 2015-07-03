package blade.migrate.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * Parses a java file and provides some  methods for finding search results
 */
public class JavaFileChecker {


	/**
	 * initialize the Checker
	 * @param file java file
	 */
	public JavaFileChecker(File file) {
		this.file = file;
		this.fileHelper = new FileHelper();

		try {
			synchronized (map) {
				WeakReference<CompilationUnit> astRef = map.get(file);

				if (astRef == null || astRef.get() == null) {
					final CompilationUnit newAst = createJavaClassVisitor();

					map.put(file, new WeakReference<CompilationUnit>(newAst));

					this.ast = newAst;
				}
				else {
					this.ast = astRef.get();
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public List<SearchResult> findCatchExceptions(final String[] exceptions) {

		final List<SearchResult> searchResults = new ArrayList<>();

		ast.accept(new ASTVisitor() {

			@Override
			public boolean visit(CatchClause node){
				String exceptionTypeName = node.getException().getType().toString();
				boolean retVal = false;

				for (String exceptionType : exceptions) {
					if ( exceptionTypeName.equals(exceptionType)){
						final int startLine = ast.getLineNumber(node.getException().getStartPosition());
						final int startOffset = node.getException().getStartPosition();

						int endLine = ast.getLineNumber(node.getException().getStartPosition() + node.getException().getLength());
							int endOffset = node.getException().getStartPosition() + node.getException().getLength();
							searchResults
									.add(new SearchResult(file, startOffset,
										endOffset, startLine, endLine));

							retVal = true;
					}
				}

				return retVal;
			}
		});

		return searchResults;

	}


	public SearchResult findImport(final String importName) {
		final List<SearchResult> searchResults = new ArrayList<>();

		ast.accept(new ASTVisitor() {

			@Override
			public boolean visit(ImportDeclaration node) {
				if (importName.equals(node.getName().toString())) {
					int startLine = ast.getLineNumber(node.getName()
						.getStartPosition());
					int startOffset = node.getName().getStartPosition();
					int endLine = ast.getLineNumber(node.getName()
						.getStartPosition() + node.getName().getLength());
					int endOffset = node.getName().getStartPosition() +
						node.getName().getLength();

					searchResults.add(new SearchResult(file, startOffset,
						endOffset, startLine, endLine));
				}

				return false;
			};
		});

		if (0 != searchResults.size()) {
			return searchResults.get(0);
		}

		return null;
	}

	public List<SearchResult> findMethodDeclaration(
		final String name, final String[] params) {

		final List<SearchResult> searchResults = new ArrayList<>();

		ast.accept(new ASTVisitor() {

			@Override
			public boolean visit(MethodDeclaration node) {
				boolean sameParmSize = true;
				String methodName = node.getName().toString();
				List<?> parmsList = node.parameters();

				if (name.equals(methodName) &&
					params.length == parmsList.size()) {

					for (int i = 0; i < params.length; i++) {
						if (!(params[i].trim().equals(parmsList.get(i)
								.toString()
								.substring(0, params[i].trim().length())))) {
							sameParmSize = false;
							break;
						}
					}
				} else {
					sameParmSize = false;
				}

				if (sameParmSize) {
					final int startLine = ast.getLineNumber(node.getName()
						.getStartPosition());
					final int startOffset = node.getName().getStartPosition();
					node.accept(new ASTVisitor() {

						@Override
						public boolean visit(Block node) {

							// SimpleName parent can not be MarkerAnnotation and
							// SimpleType
							// SingleVariableDeclaration node contains the
							// parms's type

							int endLine = ast.getLineNumber(node
								.getStartPosition());
							int endOffset = node.getStartPosition();
							searchResults
									.add(new SearchResult(file, startOffset,
										endOffset, startLine, endLine));

							return false;
						};
					});
				}

				return false;
			}
		});

		return searchResults;
	}

	/**
	 * find the method invocations for a particular method on a given type or expression
	 *
	 * @param typeHint the type hint to use when matching expressions
	 * @param expressionValue    the expression only value (no type hint)
	 * @param methodName     the method name
	 * @return    search results
	 */
	public List<SearchResult> findMethodInvocations(
		final String typeHint, final String expressionValue, final String methodName,
		final String[] methodParamTypes) {
		final List<SearchResult> searchResults = new ArrayList<>();

		ast.accept(new ASTVisitor() {

			@Override
			public boolean visit(MethodInvocation node) {
				String methodNameValue = node.getName().toString();
				Expression expression = node.getExpression();
				ITypeBinding type = null;

				if (expression != null) {
					type = expression.resolveTypeBinding();
				}

				if (((methodName.equals(methodNameValue)) || ("*".equals(methodName))) &&
						// if typeHint is not null it must match the type hint and ignore the expression
						((typeHint != null && type != null && type.getName().equals(typeHint))  ||
						// with no typeHint then expressions can be used to match Static invocation
						 (typeHint == null && expression != null && expression.toString().equals(expressionValue)))) {

					boolean match = false;

					if (methodParamTypes != null) {
						Object[] args = node.arguments().toArray();

						if (args != null && args.length == methodParamTypes.length) {
							boolean possibleMatch = true;

							for(int i = 0; i < args.length; i++) {
								Object arg = args[i];

								if (arg instanceof SimpleName) {
									SimpleName simpleName = (SimpleName)arg;

									ITypeBinding argType = simpleName.resolveTypeBinding();

									if (argType != null && argType.getName().equals(methodParamTypes[i])) {
										continue;
									}
									else {
										possibleMatch = false;
										break;
									}
								}
							}

							if (possibleMatch) {
								match = true;
							}
						}
					}
					else {
						match = true;
					}

					if (match) {
						final int startOffset = expression.getStartPosition();
						final int startLine = ast.getLineNumber(startOffset);
						final int endOffset = node.getStartPosition() + node.getLength();
						final int endLine = ast.getLineNumber(endOffset);

						searchResults.add(new SearchResult(file, startOffset,
							endOffset, startLine, endLine));
					}
				}

				return true;
			}
		});

		return searchResults;
	}

	@SuppressWarnings("unchecked")
	private CompilationUnit createJavaClassVisitor()
		throws FileNotFoundException, IOException {

		ASTParser parser = ASTParser.newParser(AST.JLS4);

		Map<String, String> options = JavaCore.getOptions();

		JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options);

		parser.setCompilerOptions(options);

		//setUnitName for resolve bindings
		String unitName = file.getName();
		parser.setUnitName(unitName);

		String[] sources = { "" };
		String[] classpath = { "" };
		//setEnvironment for resolve bindings even if the args is empty
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);

		parser.setResolveBindings(true);
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);
		parser.setSource(fileHelper.readFile(file).toCharArray());
		parser.setIgnoreMethodBodies(false);

		return (CompilationUnit)parser.createAST(null);
	}

	private final CompilationUnit ast;
	private final File file;
	private final FileHelper fileHelper;
	private static Map<File, WeakReference<CompilationUnit>> map = new WeakHashMap<>();

}