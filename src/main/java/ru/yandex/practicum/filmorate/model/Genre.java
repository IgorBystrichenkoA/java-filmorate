package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Genre {
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    @NotNull
    private String name;
}
