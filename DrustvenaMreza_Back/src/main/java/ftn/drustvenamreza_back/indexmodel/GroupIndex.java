package ftn.drustvenamreza_back.indexmodel;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Document(indexName = "group_index")
public class GroupIndex {
    @Id
    private String id;
}
