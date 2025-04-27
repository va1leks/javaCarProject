package com.example.project.dto.get;

import com.example.project.constant.LogTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class GetRedyLogDTO {
    public LogTaskStatus taskStatus;
    public String url;
}
