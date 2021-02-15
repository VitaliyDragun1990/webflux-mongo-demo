package org.vdragun.webfluxmongo.api.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.vdragun.webfluxmongo.api.v1.dto.NodeDto;
import org.vdragun.webfluxmongo.api.v1.exception.mapper.ErrorHandler;
import org.vdragun.webfluxmongo.api.v1.handler.NodeRequestHandler;
import org.vdragun.webfluxmongo.api.v1.router.RouterProvider;
import org.vdragun.webfluxmongo.service.NodeService;
import org.vdragun.webfluxmongo.service.model.NodeCreateRequest;
import org.vdragun.webfluxmongo.service.model.NodeModel;
import org.vdragun.webfluxmongo.service.model.NodeUpdateRequest;
import org.vdragun.webfluxmongo.validator.Validator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.vdragun.webfluxmongo.api.v1.router.RouterProvider.BASE_URL;

@WebFluxTest
@Import({RouterProvider.class, NodeRequestHandler.class})
@MockBean(ErrorHandler.class)
class NodeFunctionalEndpointsTest {

    private static final String NODE_A_NAME = "Node name A";

    private static final String NODE_B_NAME = "Node name A";

    private static final String NODE_DESCRIPTION = "Node description";

    private static final String NODE_A_ID = "a_node_id";

    private static final String NODE_B_ID = "b_node_id";

    @MockBean
    private NodeService serviceMock;

    @MockBean
    private Validator validatorMock;

    @Autowired
    private WebTestClient testClient;

    @BeforeEach
    void setUp() {
        when(validatorMock.validate(any())).then(i -> Mono.just(i.getArgument(0)));
    }

    @Test
    void shouldCreateNewNode() {
        NodeDto requestDto = new NodeDto(NODE_A_NAME, NODE_DESCRIPTION);
        NodeCreateRequest expectedRequest = new NodeCreateRequest(NODE_A_NAME, NODE_DESCRIPTION);
        NodeModel expectedResultModel = new NodeModel(NODE_A_ID, NODE_A_NAME, NODE_DESCRIPTION);
        Map<String, Object> expectedResultDto = Map.of("id", NODE_A_ID);

        when(serviceMock.createNode(eq(expectedRequest))).thenReturn(Mono.just(expectedResultModel));

        testClient
                .post().uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestDto), NodeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
                }).value(result -> assertThat(result).isEqualTo(expectedResultDto));
    }

    @Test
    void shouldUpdateExistingNode() {
        NodeDto requestDto = new NodeDto(NODE_A_NAME, NODE_DESCRIPTION);
        NodeUpdateRequest expectedRequest = new NodeUpdateRequest(NODE_A_ID, NODE_A_NAME, NODE_DESCRIPTION);
        NodeModel expectedResultModel = new NodeModel(NODE_A_ID, NODE_A_NAME, NODE_DESCRIPTION);

        when(serviceMock.updateNode(eq(expectedRequest))).thenReturn(Mono.just(expectedResultModel));

        testClient
                .put().uri(BASE_URL + "/{id}", NODE_A_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(requestDto), NodeDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

    @Test
    void shouldDeleteExistingNode() {
        when(serviceMock.deleteById(eq(NODE_A_ID))).thenReturn(Mono.empty());

        testClient
                .delete().uri(BASE_URL + "/{id}", NODE_A_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

    @Test
    void shouldFindNodeByIdentifier() {
        NodeModel expectedResponseModel = new NodeModel(NODE_A_ID, NODE_A_NAME, NODE_DESCRIPTION);
        NodeDto expectedResponseDto = new NodeDto(NODE_A_ID, NODE_A_NAME, NODE_DESCRIPTION);

        when(serviceMock.findById(eq(NODE_A_ID))).thenReturn(Mono.just(expectedResponseModel));

        testClient
                .get().uri(BASE_URL + "/{id}", NODE_A_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(NodeDto.class).value(result -> assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedResponseDto));
    }

    @Test
    void shouldFindAllNodes() {
        NodeModel expectedModelA = new NodeModel(NODE_A_ID, NODE_A_NAME, NODE_DESCRIPTION);
        NodeModel expectedModelB = new NodeModel(NODE_B_ID, NODE_B_NAME, NODE_DESCRIPTION);
        NodeDto expectedResponseDtoA = new NodeDto(NODE_A_ID, NODE_A_NAME, NODE_DESCRIPTION);
        NodeDto expectedResponseDtoB = new NodeDto(NODE_B_ID, NODE_B_NAME, NODE_DESCRIPTION);

        when(serviceMock.findAll()).thenReturn(Flux.just(expectedModelA, expectedModelB));

        testClient
                .get().uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(NodeDto.class)
                .value(dtos -> assertThat(dtos).usingElementComparatorIgnoringFields("id").contains(expectedResponseDtoA, expectedResponseDtoB));
    }
}