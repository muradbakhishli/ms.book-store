package az.ingress.model.criteria;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.USE_DEFAULTS;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(USE_DEFAULTS)
public class PageCriteria {
    private int page = 0;
    private int size = 10;
}
