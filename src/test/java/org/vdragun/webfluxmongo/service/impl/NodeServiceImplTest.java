package org.vdragun.webfluxmongo.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.vdragun.webfluxmongo.config.ModelMapperConfig;
import org.vdragun.webfluxmongo.domain.model.NodeDescDocument;
import org.vdragun.webfluxmongo.domain.model.NodeRootDocument;
import org.vdragun.webfluxmongo.domain.repository.NodeRepository;
import org.vdragun.webfluxmongo.exception.ResourceNotFoundException;
import org.vdragun.webfluxmongo.service.model.NodeCreateRequest;
import org.vdragun.webfluxmongo.service.model.NodeModel;
import org.vdragun.webfluxmongo.service.model.NodeUpdateRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringJUnitConfig({ModelMapperConfig.class, NodeServiceImpl.class})
@ExtendWith(MockitoExtension.class)
@DisplayName("NodeServiceImpl")
class NodeServiceImplTest {

    private static final String NODE_NAME = "Node name";

    private static final String NODE_ID = "node_id";

    private static final String NODE_DESCRIPTION = "Node description";

    private static final String NODE_OLD_NAME = "Node old name";

    @MockBean
    private NodeRepository repositoryMock;

    @Autowired
    private NodeServiceImpl service;

    @Test
    void shouldCreateNodeRootDocumentIfNoDescriptionIsProvided() {
        NodeCreateRequest createRequest = new NodeCreateRequest(NODE_NAME);
        NodeRootDocument expectedToCreate = new NodeRootDocument(NODE_NAME);
        NodeRootDocument created = new NodeRootDocument(NODE_ID, NODE_NAME);
        NodeModel expectedResult = new NodeModel(NODE_ID, NODE_NAME);

        when(repositoryMock.save(eq(expectedToCreate))).thenReturn(Mono.just(created));

        StepVerifier.create(service.createNode(createRequest))
                .assertNext(result -> assertThat(result).isEqualTo(expectedResult))
                .verifyComplete();
    }

    @Test
    void shouldCreateNodeDescDocumentIfDescriptionIsProvided() {
        NodeCreateRequest createRequest = new NodeCreateRequest(NODE_NAME, NODE_DESCRIPTION);
        NodeDescDocument expectedToCreate = new NodeDescDocument(NODE_NAME, NODE_DESCRIPTION);
        NodeDescDocument created = new NodeDescDocument(NODE_ID, NODE_NAME, NODE_DESCRIPTION);
        NodeModel expectedResult = new NodeModel(NODE_ID, NODE_NAME, NODE_DESCRIPTION);

        when(repositoryMock.save(eq(expectedToCreate))).thenReturn(Mono.just(created));

        StepVerifier
                .create(service.createNode(createRequest))
                .assertNext(result -> assertThat(result).isEqualTo(expectedResult))
                .verifyComplete();
    }

    @Test
    void shouldUpdateAsNodeRootIfNoDescriptionProvided() {
        NodeUpdateRequest updateRequest = new NodeUpdateRequest(NODE_ID, NODE_NAME);
        NodeRootDocument target = new NodeRootDocument(NODE_ID, NODE_OLD_NAME);
        NodeRootDocument expectedSaved = new NodeRootDocument(NODE_ID, NODE_NAME);
        NodeModel expectedResult = new NodeModel(NODE_ID, NODE_NAME);

        when(repositoryMock.findById(eq(NODE_ID))).thenReturn(Mono.just(target));
        when(repositoryMock.save(eq(expectedSaved))).thenReturn(Mono.just(expectedSaved));

        StepVerifier
                .create(service.updateNode(updateRequest))
                .assertNext(result -> assertThat(result).isEqualTo(expectedResult))
                .verifyComplete();
    }

    @Test
    void shouldUpdateAsNodeDescIfDescriptionIsProvided() {
        NodeUpdateRequest updateRequest = new NodeUpdateRequest(NODE_ID, NODE_NAME, NODE_DESCRIPTION);
        NodeRootDocument target = new NodeRootDocument(NODE_ID, NODE_OLD_NAME);
        NodeDescDocument expectedSaved = new NodeDescDocument(NODE_ID, NODE_NAME, NODE_DESCRIPTION);
        NodeModel expectedResult = new NodeModel(NODE_ID, NODE_NAME, NODE_DESCRIPTION);

        when(repositoryMock.findById(eq(NODE_ID))).thenReturn(Mono.just(target));
        when(repositoryMock.save(eq(expectedSaved))).thenReturn(Mono.just(expectedSaved));

        StepVerifier
                .create(service.updateNode(updateRequest))
                .assertNext(result -> assertThat(result).isEqualTo(expectedResult))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfNoNodeWithProvidedIdentifierWhenUpdating() {
        NodeUpdateRequest updateRequest = new NodeUpdateRequest(NODE_ID, NODE_NAME, NODE_DESCRIPTION);
        when(repositoryMock.findById(eq(NODE_ID))).thenReturn(Mono.empty());

        StepVerifier
                .create(service.updateNode(updateRequest))
                .expectError(ResourceNotFoundException.class)
                .verifyThenAssertThat();
    }

    @Test
    void shouldReturnNodeWithProvidedIdentifier() {
        NodeRootDocument expectedNode = new NodeRootDocument(NODE_ID, NODE_NAME);
        NodeModel expectedResult = new NodeModel(NODE_ID, NODE_NAME);

        when(repositoryMock.findById(eq(NODE_ID))).thenReturn(Mono.just(expectedNode));

        StepVerifier
                .create(service.findById(NODE_ID))
                .assertNext(result -> assertThat(result).isEqualTo(expectedResult))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfNoNodeWithProvidedIdentifierWhenFindById() {
        when(repositoryMock.findById(eq(NODE_ID))).thenReturn(Mono.empty());

        StepVerifier
                .create(service.findById(NODE_ID))
                .expectError(ResourceNotFoundException.class)
                .verifyThenAssertThat();
    }

    @Test
    void shouldReturnAllAvailableNodes() {
        String rootNodeId = "root_node_id";
        String rootNodeName = "Root node name";
        String descNodeId = "desc_node_id";
        String descNodeName = "Desc node name";
        String descNodeDescription = "Desc node description";
        NodeRootDocument nodeRoot = new NodeRootDocument(rootNodeId, rootNodeName);
        NodeDescDocument nodeDesc = new NodeDescDocument(descNodeId, descNodeName, descNodeDescription);
        NodeModel expectedRoot = new NodeModel(rootNodeId, rootNodeName);
        NodeModel expectedDesc = new NodeModel(descNodeId, descNodeName, descNodeDescription);

        when(repositoryMock.findAll()).thenReturn(Flux.just(nodeRoot, nodeDesc));

        StepVerifier
                .create(service.findAll())
                .assertNext(result -> assertThat(result).isEqualTo(expectedRoot))
                .assertNext(result -> assertThat(result).isEqualTo(expectedDesc))
                .verifyComplete();
    }

    @Test
    void shouldCompleteIfSuccessfullyDeletedNodeByProvidedIdentifier() {
        when(repositoryMock.existsById(eq(NODE_ID))).thenReturn(Mono.just(true));
        when(repositoryMock.deleteById(eq(NODE_ID))).thenReturn(Mono.empty());

        StepVerifier
                .create(service.deleteById(NODE_ID))
                .expectComplete()
                .verifyThenAssertThat();
    }

    @Test
    void shouldReturnErrorIfFailedToDeleteNodeByProvidedIdentifier() {
        when(repositoryMock.existsById(eq(NODE_ID))).thenReturn(Mono.just(false));

        StepVerifier
                .create(service.deleteById(NODE_ID))
                .expectError(ResourceNotFoundException.class)
                .verifyThenAssertThat();
    }
}