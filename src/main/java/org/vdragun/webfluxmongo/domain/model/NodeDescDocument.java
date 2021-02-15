package org.vdragun.webfluxmongo.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "node")
@TypeAlias(NodeDescDocument.ALIAS)
public class NodeDescDocument extends NodeRootDocument {

    public static final String ALIAS = "nodeDesc";

    private String description;

    public NodeDescDocument(String name, String description) {
        super(name);
        this.description = description;
    }

    public NodeDescDocument(String id, String name, String description) {
        super(id, name);
        this.description = description;
    }
}
