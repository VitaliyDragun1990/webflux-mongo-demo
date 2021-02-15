package org.vdragun.webfluxmongo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.vdragun.webfluxmongo.domain.model.NodeDescDocument;
import org.vdragun.webfluxmongo.domain.model.NodeRootDocument;
import org.vdragun.webfluxmongo.domain.repository.NodeRepository;
import org.vdragun.webfluxmongo.exception.ResourceNotFoundException;
import org.vdragun.webfluxmongo.service.NodeService;
import org.vdragun.webfluxmongo.service.model.NodeCreateRequest;
import org.vdragun.webfluxmongo.service.model.NodeModel;
import org.vdragun.webfluxmongo.service.model.NodeUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class NodeServiceImpl implements NodeService {

    private static final String ERROR_NO_NODE_BY_ID = "Fail to find node with id:[{}]";

    private final NodeRepository repository;

    private final ModelMapper mapper;

    @Override
    public Mono<NodeModel> createNode(NodeCreateRequest request) {
        LOG.debug("Received request to createNode new node:{}", request);

        return request.hasDescription()
                ? repository.save(new NodeDescDocument(request.getName(), request.getDescription())).map(this::toModel)
                : repository.save(new NodeRootDocument(request.getName())).map(this::toModel);
    }

    @Override
    public Mono<NodeModel> updateNode(NodeUpdateRequest request) {
        LOG.debug("Received request to updateNode node:{}", request);

        return repository
                .findById(request.getId())
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(ERROR_NO_NODE_BY_ID, request.getId())))
                .flatMap(node -> request.hasDescription()
                        ? repository.save(new NodeDescDocument(node.getId(), request.getName(), request.getDescription())).map(this::toModel)
                        : repository.save(new NodeRootDocument(node.getId(), request.getName())).map(this::toModel));
    }

    @Override
    public Mono<NodeModel> findById(String id) {
        LOG.debug("Received request to find node with id:[{}]", id);

        return repository
                .findById(id)
                .switchIfEmpty(Mono.error(() -> new ResourceNotFoundException(ERROR_NO_NODE_BY_ID, id)))
                .map(this::toModel);
    }

    @Override
    public Flux<NodeModel> findAll() {
        LOG.debug("Received request to find all available nodes.");

        return repository
                .findAll()
                .map(this::toModel);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        LOG.debug("Received request to deleteNode node with id:[{}]", id);

        return repository
                .existsById(id)
                .flatMap(result -> Boolean.TRUE.equals(result) ? repository.deleteById(id)
                        : Mono.error(() -> new ResourceNotFoundException(ERROR_NO_NODE_BY_ID, id)));
    }

    private NodeModel toModel(NodeRootDocument node) {
        return mapper.map(node, NodeModel.class);
    }
}
