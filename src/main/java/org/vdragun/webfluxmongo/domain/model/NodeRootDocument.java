package org.vdragun.webfluxmongo.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = "node")
@TypeAlias(NodeRootDocument.ALIAS)
public class NodeRootDocument {

    public static final String ALIAS = "nodeRoot";

    @Id
    private String id;

    private String name;

    public NodeRootDocument(String name) {
        this.name = name;
    }
}
