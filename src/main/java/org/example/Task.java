package org.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private String kind;
    private String id;
    private String etag;
    private String title;
    private String updated;
    private String selfLink;
    private String parent;
    private String position;
    private String notes;
    private String status;
    private String due;
    private String completed;
    private Boolean deleted;
    private Boolean hidden;
    private List<Link> links;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Link {
        private String type;
        private String description;
        private String link;
    }
}