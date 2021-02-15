package org.vdragun.webfluxmongo.api.v1.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.vdragun.webfluxmongo.api.v1.dto.NodeDto;
import org.vdragun.webfluxmongo.api.v1.exception.mapper.ErrorHandler;
import org.vdragun.webfluxmongo.service.NodeService;
import org.vdragun.webfluxmongo.service.model.NodeCreateRequest;
import org.vdragun.webfluxmongo.service.model.NodeUpdateRequest;
import org.vdragun.webfluxmongo.validator.Validator;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class NodeRequestHandler {

    private static final String PATH_VARIABLE_ID = "id";

    private final NodeService service;

    private final ErrorHandler errorHandler;

    private final Validator validator;

    public Mono<ServerResponse> findNodeById(ServerRequest request) {
        LOG.debug("Received request to find node by id, request path:{}", request.requestPath());

        return service
                .findById(extractIdFrom(request))
                .map(n -> new NodeDto(n.getId(), n.getName(), n.getDescription()))
                .flatMap(this::handleReadResponse)
                .onErrorResume(e -> errorHandler.handlerError(e, request));
    }

    public Mono<ServerResponse> findAllNodes(ServerRequest request) {
        LOG.debug("Received request to find all nodes, request path:{}", request.requestPath());

        return handleReadResponse(
                service
                        .findAll()
                        .map(n -> new NodeDto(n.getId(), n.getName(), n.getDescription())));
    }

    public Mono<ServerResponse> createNode(ServerRequest request) {
        LOG.debug("Received request to createNode new node, request path:{}", request.requestPath());

        return request
                .bodyToMono(NodeDto.class)
                .flatMap(validator::validate)
                .map(dto -> new NodeCreateRequest(dto.getName(), dto.getDescription()))
                .flatMap(service::createNode)
                .flatMap(r -> ServerResponse.status(HttpStatus.CREATED).bodyValue(Map.of("id", r.getId())))
                .onErrorResume(e -> errorHandler.handlerError(e, request));
    }

    public Mono<ServerResponse> updateNode(ServerRequest request) {
        LOG.debug("Received request to updateNode existing node, request path:{}", request.requestPath());

        return request
                .bodyToMono(NodeDto.class)
                .flatMap(validator::validate)
                .map(dto -> new NodeUpdateRequest(extractIdFrom(request), dto.getName(), dto.getDescription()))
                .flatMap(service::updateNode)
                .flatMap(r -> ServerResponse.status(HttpStatus.OK).build())
                .onErrorResume(e -> errorHandler.handlerError(e, request));
    }

    public Mono<ServerResponse> deleteNode(ServerRequest request) {
        LOG.debug("Received request to deleteNode existing node, request path:{}", request.requestPath());

        return service
                .deleteById(extractIdFrom(request))
                .then(ServerResponse.ok().build())
                .onErrorResume(e -> errorHandler.handlerError(e, request));
    }

    private Mono<ServerResponse> handleReadResponse(Publisher<NodeDto> nodes) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(nodes, NodeDto.class);
    }

    private Mono<ServerResponse> handleReadResponse(Object responseBody) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseBody);
    }

    private String extractIdFrom(ServerRequest request) {
        return request.pathVariable(PATH_VARIABLE_ID);
    }
}
