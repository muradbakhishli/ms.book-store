package az.ingress.model.cache;

import az.ingress.model.jwt.AccessTokenClaimsSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Builder
public class AuthCacheData implements Serializable {
    private static final long serialVersionUID = 1L;
    private AccessTokenClaimsSet accessTokenClaimsSet;
    private String publicKey;
}
