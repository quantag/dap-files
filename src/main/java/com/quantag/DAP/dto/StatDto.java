package com.quantag.DAP.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatDto {
    @JsonProperty("isDirectory")
    private boolean directory;

    private String ctime;
    private String mtime;
    private long size;
}
