package br.com.fintech.valoresareceber.dto;

import java.util.List;

public class InstructionalCardDTO {
    private String title;
    private List<String> steps;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
}