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
        assertEquals("John Doe", dto.getName());
        assertEquals(1990, dto.getYearOfBirth());
    }
    
    @Test
    void shouldHandleNullValues() {
        // Given
        Person person = new Person(null, 0);
        
        // When
        PersonDto dto = PersonMapper.map(person);
        
        // Then
        assertNull(dto.getName());
        assertEquals(0, dto.getYearOfBirth());
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
        assertEquals("Alice", dto1.getName());
        assertEquals(1985, dto1.getYearOfBirth());
        assertEquals("Bob", dto2.getName());
        assertEquals(1995, dto2.getYearOfBirth());
    }
} 