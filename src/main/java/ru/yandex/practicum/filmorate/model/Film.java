package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {

    private int id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive
    private long duration;

    private int rate;

    private final Set<Integer> likes = new HashSet<>() ;

}