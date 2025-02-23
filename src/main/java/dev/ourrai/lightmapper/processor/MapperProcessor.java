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
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
    "dev.ourrai.lightmapper.annotations.Mapper",
    "dev.ourrai.lightmapper.annotations.MapField"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class MapperProcessor extends AbstractProcessor {
    
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Mapper.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                continue;
            }
            
            TypeElement classElement = (TypeElement) element;
            Mapper mapperAnnotation = classElement.getAnnotation(Mapper.class);
            TypeMirror targetType = getTypeMirror(mapperAnnotation);
            
            generateMapper(classElement, targetType);
        }
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
        String packageName = elementUtils.getPackageOf(sourceClass).getQualifiedName().toString();
        String className = sourceClass.getSimpleName() + "Mapper";
        
        MethodSpec.Builder mapMethodBuilder = MethodSpec.methodBuilder("map")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeName.get(targetType))
            .addParameter(TypeName.get(sourceClass.asType()), "source");
            
        mapMethodBuilder.addStatement("$T target = new $T()", 
            TypeName.get(targetType), TypeName.get(targetType));
            
        for (Element enclosed : sourceClass.getEnclosedElements()) {
            if (enclosed.getKind() != ElementKind.FIELD) {
                continue;
            }
            
            MapField mapField = enclosed.getAnnotation(MapField.class);
            if (mapField == null) {
                continue;
            }
            
            String fieldName = enclosed.getSimpleName().toString();
            String targetFieldName = mapField.targetField().isEmpty() ? 
                fieldName : mapField.targetField();
                
            String getterName = "get" + capitalize(fieldName);
            String setterName = "set" + capitalize(targetFieldName);
            
            mapMethodBuilder.addStatement("target.$L(source.$L())", 
                setterName, getterName);
        }
        
        mapMethodBuilder.addStatement("return target");
        
        TypeSpec mapperClass = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC)
            .addMethod(mapMethodBuilder.build())
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