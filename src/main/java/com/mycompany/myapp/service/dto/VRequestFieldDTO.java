package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.VRequestField} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VRequestFieldDTO implements Serializable {

    private Long id;

    private Long endpointId;

    private String code;

    private String name;

    private VEndpointDTO vEndpoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Long endpointId) {
        this.endpointId = endpointId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VEndpointDTO getvEndpoint() {
        return vEndpoint;
    }

    public void setvEndpoint(VEndpointDTO vEndpoint) {
        this.vEndpoint = vEndpoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VRequestFieldDTO)) {
            return false;
        }

        VRequestFieldDTO vRequestFieldDTO = (VRequestFieldDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vRequestFieldDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VRequestFieldDTO{" +
            "id=" + getId() +
            ", endpointId=" + getEndpointId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", vEndpoint=" + getvEndpoint() +
            "}";
    }
}
