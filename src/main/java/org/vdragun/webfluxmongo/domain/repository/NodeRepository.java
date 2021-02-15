package org.vdragun.webfluxmongo.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.vdragun.webfluxmongo.domain.model.NodeRootDocument;

public interface NodeRepository extends ReactiveMongoRepository<NodeRootDocument, String> {
}
