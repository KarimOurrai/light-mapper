# LightMapper

A lightweight Java library for compile-time object mapping. Uses annotation processing to generate type-safe mappers without reflection, making it fast and efficient.

## Features
- 🚀 Compile-time mapping generation
- 🔒 Type-safe mapping
- 🎯 Custom field name mapping
- 🪶 Zero runtime overhead
- 📦 No runtime dependencies

## Quick Start
```java
@Mapper(target = PersonDto.class)
public class Person {
    @MapField
    private String name;
    
    @MapField(targetField = "yearOfBirth")
    private int birthYear;
}

// Generated mapper usage
PersonDto dto = PersonMapper.map(person);
```

## Installation
```gradle
dependencies {
    implementation 'dev.ourrai:lightmapper:1.0-SNAPSHOT'
    annotationProcessor 'dev.ourrai:lightmapper:1.0-SNAPSHOT'
}
```

## License
MIT License
    