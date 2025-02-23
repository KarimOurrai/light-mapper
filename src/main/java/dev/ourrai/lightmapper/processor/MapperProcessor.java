package dev.ourrai.lightmapper.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import dev.ourrai.lightmapper.annotations.MapField;
import dev.ourrai.lightmapper.annotations.Mapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
    "dev.ourrai.lightmapper.annotations.Mapper",
    "dev.ourrai.lightmapper.annotations.MapField"
})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MapperProcessor extends AbstractProcessor {
    
    private Elements elementUtils;
    private Filer filer;
    
    record MappingField(String sourceName, String targetName) {}
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Mapper.class).stream()
            .filter(element -> element.getKind() == ElementKind.CLASS)
            .map(TypeElement.class::cast)
            .forEach(classElement -> {
                var mapperAnnotation = classElement.getAnnotation(Mapper.class);
                var targetType = getTypeMirror(mapperAnnotation);
                generateMapper(classElement, targetType);
            });
        return true;
    }
    
    private TypeMirror getTypeMirror(Mapper mapper) {
        try {
            mapper.target();
        } catch (MirroredTypeException mte) {
            return mte.getTypeMirror();
        }
        return null;
    }
    
    private void generateMapper(TypeElement sourceClass, TypeMirror targetType) {
        var packageName = elementUtils.getPackageOf(sourceClass).getQualifiedName().toString();
        var className = sourceClass.getSimpleName() + "Mapper";
        
        var mappingFields = sourceClass.getEnclosedElements().stream()
            .filter(element -> element.getKind() == ElementKind.FIELD)
            .filter(element -> element.getAnnotation(MapField.class) != null)
            .map(element -> {
                var mapField = element.getAnnotation(MapField.class);
                var sourceName = element.getSimpleName().toString();
                var targetName = mapField.targetField().isEmpty() ? sourceName : mapField.targetField();
                return new MappingField(sourceName, targetName);
            })
            .toList();
            
        var constructorParams = mappingFields.stream()
            .map(field -> "source.get" + capitalize(field.sourceName) + "()")
            .collect(java.util.stream.Collectors.joining(", "));
        
        var mapMethod = MethodSpec.methodBuilder("map")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeName.get(targetType))
            .addParameter(TypeName.get(sourceClass.asType()), "source")
            .addStatement("return new $T(" + constructorParams + ")", TypeName.get(targetType))
            .build();
            
        var mapperClass = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC)
            .addMethod(mapMethod)
            .build();
            
        try {
            JavaFile.builder(packageName, mapperClass)
                .build()
                .writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
} 