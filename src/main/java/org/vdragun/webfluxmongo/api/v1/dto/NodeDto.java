package org.vdragun.webfluxmongo.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(value = "id", allowGetters = true)
@JsonInclude(Include.NON_NULL)
public class NodeDto {

    private String id;

    private String name;

    private String description;

    public NodeDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
