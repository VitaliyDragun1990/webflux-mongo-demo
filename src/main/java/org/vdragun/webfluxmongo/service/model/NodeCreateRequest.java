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
public class NodeCreateRequest {

    private String name;

    private String description;

    public NodeCreateRequest(String name) {
        this.name = name;
    }

    public boolean hasDescription() {
        return description != null;
    }
}
