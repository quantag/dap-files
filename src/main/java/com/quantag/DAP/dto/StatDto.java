package com.quantag.DAP.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatDto {
    private boolean isDirectory;
    private String ctime;
    private String mtime;
    private long size;
}
