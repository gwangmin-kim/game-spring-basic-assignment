package com.gamebasic.game.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RenameRequest {
    @NotNull
    @Size(min = 2, max = 12)
    private String playerName;
}
