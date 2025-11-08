package ca.metricalsky.winston.repository;

import ca.metricalsky.winston.entity.view.AuthorDetailsView;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuthorDetailsRepository {

    private final EntityManager entityManager;

    public List<AuthorDetailsView> findAuthorDetailsPage(Pageable pageable) {
        return entityManager.createQuery("""
                SELECT new ca.metricalsky.winston.entity.view.AuthorDetailsView(
                    a,
                    COUNT(DISTINCT v.channelId),
                    COUNT(DISTINCT c.videoId),
                    COUNT(c.id),
                    COUNT(c.parentId)
                    )
                FROM AuthorEntity a
                    LEFT JOIN CommentEntity c ON a.id = c.author.id
                    LEFT JOIN VideoEntity v ON c.videoId = v.id
                GROUP BY a.id
                ORDER BY a.displayName
                """, AuthorDetailsView.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
