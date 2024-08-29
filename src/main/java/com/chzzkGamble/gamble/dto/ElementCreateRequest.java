package com.chzzkGamble.gamble.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class ElementCreateRequest {

    List<String> elements;

    public ElementCreateRequest(List<String> elements) {
        this.elements = elements;
    }
}
