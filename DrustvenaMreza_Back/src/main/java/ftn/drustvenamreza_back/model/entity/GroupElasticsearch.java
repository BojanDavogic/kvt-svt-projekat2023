package ftn.drustvenamreza_back.indexrepository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
@Getter
@Setter
@Document(indexName = "groups")
public class GroupElasticsearch {

    @Id
    private Long id;
    private String name;
    private String description;
}
