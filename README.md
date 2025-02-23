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

## Detailed Usage

### Basic Mapping
The simplest way to map objects is to use the `@MapField` annotation without any parameters:

```java
@Mapper(target = UserDto.class)
public class User {
    @MapField
    private String username;  // Will map to UserDto.username
    @MapField
    private String email;     // Will map to UserDto.email
}
```

### Custom Field Names
Use `targetField` to map to differently named fields:

```java
@Mapper(target = CustomerDto.class)
public class User {
    @MapField(targetField = "customerName")
    private String name;
    
    @MapField(targetField = "customerEmail")
    private String email;
}
```

### Generated Mapper
LightMapper generates a static mapper class with the following naming convention:
```java
// For a class named 'User', generates:
public class UserMapper {
    public static UserDto map(User source) {
        // Generated mapping code
    }
}
```

## Limitations
- Only supports direct field mapping
- Fields must have matching types
- No support for nested object mapping
- No support for collection mapping
- Source and target classes must have appropriate getters/setters

## Building from Source
```bash
git clone https://github.com/yourusername/lightmapper.git
cd lightmapper
./gradlew build
```

## Running Tests
```bash
./gradlew test
```

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
MIT License - see the [LICENSE](LICENSE) file for details

## Authors
- Karim Ourrai - *Initial work*

## Acknowledgments
- Inspired by MapStruct and other mapping libraries
- Built with Java and Gradle
    