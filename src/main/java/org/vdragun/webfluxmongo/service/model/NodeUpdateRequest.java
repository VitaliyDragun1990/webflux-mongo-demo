package org.vdragun.webfluxmongo.service.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NodeUpdateRequest {

    private String id;

    private String name;

    private String description;

    public NodeUpdateRequest(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean hasDescription() {
        return description != null;
    }
}
