package com.quizbot.service;

import com.quizbot.model.Flashcard;
import com.quizbot.repository.FlashcardRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlashcardServiceTest {
    @Mock
    private FlashcardRepository flashcardRepository;
    
    @Mock
    private AIMLService aimlService;
    
    @Mock
    private PointsService pointsService;
    
    private FlashcardService flashcardService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        flashcardService = new FlashcardService(flashcardRepository, aimlService, pointsService);
    }
    
    @Test
    void testGenerateFlashcards() {
        // Prepare test data
        String topic = "Java";
        String language = "EN";
        int count = 2;
        List<Flashcard> mockFlashcards = new ArrayList<>();
        mockFlashcards.add(new Flashcard("What is Java?", "A programming language"));
        mockFlashcards.add(new Flashcard("What is JVM?", "Java Virtual Machine"));
        
        // Mock dependencies
        when(aimlService.generateFlashcards(topic, language, count)).thenReturn(mockFlashcards);
        when(flashcardRepository.saveAll(mockFlashcards)).thenReturn(mockFlashcards);
        
        // Test
        List<Flashcard> result = flashcardService.generateFlashcards(topic, language, count);
        
        // Verify
        assertEquals(2, result.size());
        verify(flashcardRepository).saveAll(mockFlashcards);
    }
    
    @Test
    void testGetAllFlashcards() {
        // Prepare test data
        List<Flashcard> mockFlashcards = new ArrayList<>();
        mockFlashcards.add(new Flashcard("Question 1", "Answer 1"));
        mockFlashcards.add(new Flashcard("Question 2", "Answer 2"));
        
        // Mock dependencies
        when(flashcardRepository.findAll()).thenReturn(mockFlashcards);
        
        // Test
        List<Flashcard> result = flashcardService.getAllFlashcards();
        
        // Verify
        assertEquals(2, result.size());
    }
    
    @Test
    void testMarkFlashcard() {
        // Test data
        String id = "123";
        boolean isCorrect = true;
        Flashcard mockFlashcard = new Flashcard("Question", "Answer");
        
        // Mock dependencies
        when(flashcardRepository.findById(id)).thenReturn(Optional.of(mockFlashcard));
        when(flashcardRepository.save(mockFlashcard)).thenReturn(mockFlashcard);
        
        // Test
        flashcardService.markFlashcard(id, isCorrect);
        
        // Verify
        verify(flashcardRepository).findById(id);
        verify(flashcardRepository).save(mockFlashcard);
        if (isCorrect) {
            verify(pointsService).addPoints(1);
        }
    }
}

