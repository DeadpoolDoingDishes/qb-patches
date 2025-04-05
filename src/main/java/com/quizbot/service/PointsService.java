package com.quizbot.service;

import com.quizbot.model.Points;
import com.quizbot.repository.PointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointsService {
    private final PointsRepository repository;
    
    @Autowired
    public PointsService(PointsRepository repository) {
        this.repository = repository;
    }
    
    public Points getPoints() {
        List<Points> pointsList = repository.findAll();
        if (pointsList.isEmpty()) {
            Points points = new Points();
            return repository.save(points);
        }
        return pointsList.get(0);
    }
    
    public Points addPoints(int pointsToAdd) {
        Points points = getPoints();
        points.addPoints(pointsToAdd);
        return repository.save(points);
    }
} 