package com.ccsw.tutorial_batch.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private Long id;
    private String title;
    private int ageRecommended;
    private int stock;

    // Constructor vacío
    public Game() {
        LOGGER.info("Game instance created with default constructor");
    }

    // Constructor completo
    public Game(Long id, String title, int ageRecommended, int stock) {
        this.id = id;
        this.title = title;
        this.ageRecommended = ageRecommended;
        this.stock = stock;
        LOGGER.info("Game created: id={}, title={}, ageRecommended={}, stock={}", id, title, ageRecommended, stock);
    }

    // Constructor para título y edad recomendada
    public Game(String title, int ageRecommended) {
        this.title = title;
        this.ageRecommended = ageRecommended;
        LOGGER.info("Game created with title={} and ageRecommended={}", title, ageRecommended);
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        LOGGER.debug("Setting id: {}", id);
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        LOGGER.debug("Setting title: {}", title);
        this.title = title;
    }

    public int getAgeRecommended() {
        return ageRecommended;
    }

    public void setAgeRecommended(int ageRecommended) {
        LOGGER.debug("Setting ageRecommended: {}", ageRecommended);
        this.ageRecommended = ageRecommended;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        LOGGER.debug("Setting stock: {}", stock);
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Game [id=" + id +
                ", title=" + title +
                ", ageRecommended=" + ageRecommended +
                ", stock=" + stock + "]";
    }
}