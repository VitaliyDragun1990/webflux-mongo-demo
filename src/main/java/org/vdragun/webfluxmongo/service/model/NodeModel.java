package org.vdragun.webfluxmongo.service.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
public class NodeModel {

    private String id;

    private String name;

    private String description;

    public NodeModel(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
