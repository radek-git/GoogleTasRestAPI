package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskList {

    private String kind;
    private String id;
    private String etag;
    private String title;
    private String updated;
    private String selfLink;


}
