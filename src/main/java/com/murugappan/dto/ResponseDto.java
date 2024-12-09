package com.murugappan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    @JsonProperty("unix")
    Long unix;
    @JsonProperty("utc")
    String utc;
}
