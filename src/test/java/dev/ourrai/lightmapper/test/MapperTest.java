package dev.ourrai.lightmapper.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapperTest {
    
    @Test
    void shouldMapBasicFields() {
        // Given
        Person person = new Person("John Doe", 1990);
        
        // When
        PersonDto dto = PersonMapper.map(person);
        
        // Then
        assertEquals("John Doe", dto.name());
        assertEquals(1990, dto.yearOfBirth());
    }
    
    @Test
    void shouldHandleNullValues() {
        // Given
        Person person = new Person(null, 0);
        
        // When
        PersonDto dto = PersonMapper.map(person);
        
        // Then
        assertNull(dto.name());
        assertEquals(0, dto.yearOfBirth());
    }
    
    @Test
    void shouldMapMultipleInstances() {
        // Given
        Person person1 = new Person("Alice", 1985);
        Person person2 = new Person("Bob", 1995);
        
        // When
        PersonDto dto1 = PersonMapper.map(person1);
        PersonDto dto2 = PersonMapper.map(person2);
        
        // Then
        assertEquals("Alice", dto1.name());
        assertEquals(1985, dto1.yearOfBirth());
        assertEquals("Bob", dto2.name());
        assertEquals(1995, dto2.yearOfBirth());
    }
} 