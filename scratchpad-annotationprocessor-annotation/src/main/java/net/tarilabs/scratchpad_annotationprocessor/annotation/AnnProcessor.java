package net.tarilabs.scratchpad_annotationprocessor.annotation;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.google.auto.service.AutoService;

@AutoService(Processor.class)
public class AnnProcessor extends AbstractProcessor {

	@Override
	  public boolean process(final Set< ? extends TypeElement > annotations, 
	      final RoundEnvironment roundEnv) {
		    
	    for( final Element element: roundEnv.getElementsAnnotatedWith( MyAnnotation.class ) ) {
	      if( element instanceof TypeElement ) {
	        final TypeElement typeElement = ( TypeElement )element;
	        final PackageElement packageElement = 
	          ( PackageElement )typeElement.getEnclosingElement();

	        try {
	          final String className = typeElement.getSimpleName().toString();
	          final JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(
	            packageElement.getQualifiedName() + "." + className);
	          Writer writter = fileObject.openWriter() ;
	          try {
	            writter.append( "package " + packageElement.getQualifiedName() + ";" );
	            writter.append( "\n\n");
	            writter.append( "public class " + className + " {" );
	            writter.append( "\n");
	            writter.append( "}");
	          } catch (Exception e) {
	        	  e.printStackTrace();
	          } finally {
	        	  writter.close();
	          }
	        } catch( final IOException ex ) {
	          processingEnv.getMessager().printMessage(Kind.ERROR, ex.getMessage());
	        }
	      }
	    }
			
	    // Claiming that annotations have been processed by this processor 
	    return true;
	  }
	
	  @Override
	  public Set<String> getSupportedAnnotationTypes() {
	    Set<String> annotataions = new LinkedHashSet<String>();
	    annotataions.add(MyAnnotation.class.getCanonicalName());
	    return annotataions;
	  }

	  @Override
	  public SourceVersion getSupportedSourceVersion() {
	    return SourceVersion.latestSupported();
	  }

}
