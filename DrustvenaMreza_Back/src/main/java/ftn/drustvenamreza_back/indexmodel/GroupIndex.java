package ftn.drustvenamreza_back.indexmodel;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "group_index")
public class GroupIndex {
    @Id
    private Long id;

    @Field(type = FieldType.Text, store = true, name = "name", analyzer = "serbian")
    private String name;

    @Field(type = FieldType.Text, store = true, name = "description", analyzer = "serbian")
    private String description;

    @Field(type = FieldType.Text, store = true, name = "file_content", analyzer = "serbian")
    private String fileContent;

    @Field(type = FieldType.Long, store = true, name = "number_of_posts")
    private Long numberOfPosts;

    @Field(type = FieldType.Text, store = true, name = "rules", analyzer = "serbian")
    private String rules;

    @Field(type = FieldType.Double, store = true, name = "average_likes")
    private Double averageLikes;
}
