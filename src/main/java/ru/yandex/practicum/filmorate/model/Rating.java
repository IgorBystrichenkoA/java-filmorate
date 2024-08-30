package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Rating {
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    @NotNull
    private String name;

    public Rating(String name) {
        this.name = name;
    }
}