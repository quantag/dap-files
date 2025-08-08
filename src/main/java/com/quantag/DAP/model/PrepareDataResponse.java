package com.quantag.DAP.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrepareDataResponse {
    int status;

    public PrepareDataResponse(int status) {
        this.status = status;
    }

}
