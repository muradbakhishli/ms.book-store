package az.ingress.model.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class AccessTokenClaimsSet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String username;
    private String iss;

    @JsonProperty("exp")
    private Date expirationTime;

    @JsonProperty("iat")
    private Date createdTime;
}
