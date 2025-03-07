package ca.metricalsky.yt.comments.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class AuthorDto {

    @NonNull
    private String id = "";

    @NonNull
    private String displayName = "";
}
