package org.vdragun.webfluxmongo.service;

import org.vdragun.webfluxmongo.service.model.NodeCreateRequest;
import org.vdragun.webfluxmongo.service.model.NodeModel;
import org.vdragun.webfluxmongo.service.model.NodeUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NodeService {

    Mono<NodeModel> createNode(NodeCreateRequest request);

    Mono<NodeModel> updateNode(NodeUpdateRequest request);

    Mono<NodeModel> findById(String id);

    Flux<NodeModel> findAll();

    Mono<Void> deleteById(String id);
}
