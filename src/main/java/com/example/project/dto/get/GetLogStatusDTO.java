package com.example.project.dto.get;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class GetLogStatusDTO {
    UUID taskId;
    String url;
}