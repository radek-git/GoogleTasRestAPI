package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {

    private String kind;
    private String etag;
    private String nextPageToken;
    private List<Task> items;
}


