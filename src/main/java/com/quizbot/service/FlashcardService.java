package com.quizbot.service;

import com.quizbot.model.Flashcard;
import com.quizbot.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlashcardService {
    private final FlashcardRepository repository;
    private final AIMLService aimlService;
    private final PointsService pointsService;

    @Autowired
    public FlashcardService(FlashcardRepository repository, AIMLService aimlService, PointsService pointsService) {
        this.repository = repository;
        this.aimlService = aimlService;
        this.pointsService = pointsService;
    }

    public List<Flashcard> generateFlashcards(String topic, String language, int count) {
        List<Flashcard> flashcards = aimlService.generateFlashcards(topic, language, count);
        return repository.saveAll(flashcards);
    }

    public List<Flashcard> getAllFlashcards() {
        return repository.findAll();
    }

    public void markFlashcard(String id, boolean isCorrect) {
        Optional<Flashcard> flashcardOpt = repository.findById(id);
        if (flashcardOpt.isPresent()) {
            Flashcard flashcard = flashcardOpt.get();
            flashcard.setCorrect(isCorrect);
            repository.save(flashcard);
            
            // Add points if the answer was correct
            if (isCorrect) {
                pointsService.addPoints(1);
            }
        }
    }
}

