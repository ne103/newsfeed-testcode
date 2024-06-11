package org.example.newsfeed.dto;

import lombok.Getter;

@Getter
public class SearchRequestDTO {
    private String firstDate;
    private String lastDate;
}
/* Request 날짜 형식
    {
    "firstDate": "2024-01-01",
    "lastDate": "2024-01-02"
    }
*/